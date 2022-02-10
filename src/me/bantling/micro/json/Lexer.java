package me.bantling.micro.json;

import java.io.PushbackReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import me.bantling.micro.util.Unicode;

/*
 * Lex the next JSON token from a Reader.
 * 
 * Since a Reader is, in general, not rereadable, Lexer is also an Iterator and Iterable, and offers a stream method.
 * Lexer itself is not reusable - while the underlying stream can be reset, Lexer cannot, and it wraps the reader with
 * a PushBackReader that can back up twice, since a Unicode code point can be up to two UTF-16 characters.
 * 
 * The user must only use one of the lex/Iterator/Iterable/Stream paradigms, or the lexer may give unexpected results:
 * - Loop using lex() until an empty Optional is returned
 * - Loop using hasNext()/next() until hasNext() returns false
 * - Loop using a generic for loop (equivalent to using hasNext()/next())
 * - Loop using a stream
 * 
 * Iterating the lexer allows iterating legal tokens regardless of whether the sequence makes any sense or not.
 * See Parser for iterating legal tokens only in a legal order.
 */
public final class Lexer implements Iterator<LexerToken>, Iterable<LexerToken> {
	static final RuntimeException INCOMPLETE_STRING            = new RuntimeException("Unexpected EOF: incomplete string");
	static final RuntimeException NO_ASCII_CONTROL             = new RuntimeException("Strings cannot contain ASCII control characters");
	static final RuntimeException INCOMPLETE_BACKSLASH_ESCAPE  = new RuntimeException("Unexpected EOF: incomplete backslash escape");
	static final RuntimeException INCOMPLETE_UNICODE_ESCAPE    = new RuntimeException("Unexpected EOF: incomplete unicode escape");
	static final String           INVALID_UNICODE_ESCAPE_FMT   = "Invalid unicode escape: \\u%s";
	static final String           INVALID_BACKSLASH_ESCAPE_FMT = "Invalid backslash escape: \\%s";
	
	static final RuntimeException INCOMPLETE_NEGATIVE_NUMBER   = new RuntimeException("Unexpected EOF reading a negative number");
	static final RuntimeException MINUS_SIGN_REQUIRES_DIGIT    = new RuntimeException("The minus sign for a number must be followed by a digit");
	static final RuntimeException DECIMAL_POINT_REQUIRES_DIGIT = new RuntimeException("The decimal point in a number must be followed by a digit");
	static final RuntimeException EXPONENT_REQUIRES_DIGIT      = new RuntimeException("The exponent character in a number must be followed by an optional sign and one or more digits");

	static final RuntimeException BOOLEAN_SPELLED_TRUE_OR_FALSE = new RuntimeException("A boolean value must be spelled true or false in lower case");
	static final RuntimeException NULL_SPELLING                 = new RuntimeException("A null value must be spelled null in lower case");
	static final String           INVALID_CHARACTER_FMT         = "Invalid JSON input: character %s at position %s";
	
	// Underlying Reader
	private final PushbackReader reader;
	
	// Current 1-based line and character position within input - make available to parser
	private boolean skipLF;
	int currentLine = 1;
	int currentPos = 0;
	
	// Last token iterated
	Optional<LexerToken> token;
	boolean searched;
	
	// Get next code point from reader
	private int nextCodePoint() {
		final int codePoint = Unicode.nextCodePoint(
			reader,
			() -> String.format("%d:%d", Integer.valueOf(currentLine), Integer.valueOf(currentPos))
		);
		if (codePoint >= 0) {
			if (codePoint == '\r') {
				skipLF = true;
				currentLine++;
				currentPos = 0;
			} else if (codePoint == '\n') {
				if (skipLF) {
					skipLF = false;
				} else {
					currentLine++;
					currentPos = 0;
				}
			} else {
				currentPos++;
			}
		}
		
		return codePoint;
	}
	
	// Unread a code point
	private void unreadCodePoint(final int codePoint) {
		Unicode.unreadCodePoint(reader, codePoint);
		currentPos--;
	}
	
	// Construct
	public Lexer(
		final Reader reader
	) {
		// Must be able to push back two bytes
		this.reader = new PushbackReader(reader, 2);
	}
	
	// Lex a string, which contains everything between a pait of double quotes.
	// We have to interpret some escape sequences.
	private LexerToken lexString() {
		final StringBuilder sb = new StringBuilder();
		
		// Initial " already swallowed, collect all before next "
		for (int theChar = nextCodePoint(); theChar != '"'; theChar = nextCodePoint()) {
			if (theChar < 0) {
				throw INCOMPLETE_STRING;
			}
			
			if (theChar < ' ') {
				throw NO_ASCII_CONTROL;
			}
			
			// Can't have backslash by itself, or control chars
			if (theChar == '\\') {
				// Need next char has to be ", \, /, b, f, n, r, t, u
				theChar = nextCodePoint();
				if (theChar < 0) {
					throw INCOMPLETE_BACKSLASH_ESCAPE;
				}
				
				switch (theChar) {
					case '"':
						sb.appendCodePoint(theChar);
						break;
						
					case '\\':
						sb.appendCodePoint(theChar);
						break;
						
					case '/':
						sb.appendCodePoint(theChar);
						break;
						
					case 'b':
						sb.append('\b');
						break;
						
					case 'f':
						sb.append('\f');
						break;
						
					case 'n':
						sb.append('\n');
						break;
						
					case 'r':
						sb.append('\r');
						break;
						
					case 't':
						sb.append('\t');
						break;
						
					case 'u': {
						// u must be followed by 4 hex digits
						final StringBuilder u = new StringBuilder();
						for (int i = 1; i <= 4; i++) {
							theChar = nextCodePoint();
							if (theChar < 0) {
								throw INCOMPLETE_UNICODE_ESCAPE;
							}
							u.appendCodePoint(theChar);
						}
						
						try {
							sb.append((char)(Integer.parseUnsignedInt(u.toString(), 16)));
						} catch (@SuppressWarnings("unused") final NumberFormatException e) {
							throw new RuntimeException(String.format(INVALID_UNICODE_ESCAPE_FMT, u.toString()));
						}
						break;
					}
						
					default:
						throw new RuntimeException(String.format(INVALID_BACKSLASH_ESCAPE_FMT, Character.toString(theChar)));
				}
			} else {
				sb.append(Character.toChars(theChar));
			}
		}
		
		// Closing " swalled by for loop
		return new LexerToken(LexerToken.Type.STRING, sb.toString());
	}
	
	// Lex a number, that is described by the following regex:
	// -?[0-9]+(.[0-9]+([eE][-+]?[0-9]+)?)?
	private LexerToken lexNumber(final int firstChar) {
		final StringBuilder sb = new StringBuilder();
		boolean positive = true;
		final StringBuilder integer = new StringBuilder();
		final StringBuilder fractional = new StringBuilder();
		boolean positiveExponent = true;
		final StringBuilder exponent = new StringBuilder();
		
		// Append first char, which is either a minus or digit
		sb.append((char)(firstChar));
		
		// If first char is -, there must be at least one digit for integer
		int theChar;
		if (firstChar == '-') {
			positive = false;
			
			theChar = nextCodePoint();
			if (theChar < 0) {
				throw INCOMPLETE_NEGATIVE_NUMBER;
			}
			
			if ((theChar < '0') || (theChar > '9')) {
				throw MINUS_SIGN_REQUIRES_DIGIT;
			}
			sb.append((char)(theChar));
			integer.append((char)(theChar));
		} else {
			// first char must be a digit, add it to integer
			integer.append((char)(firstChar));
		}

		// Have minus and first digit. Consume more digits.
		for (theChar = nextCodePoint(); ((theChar >= '0') && (theChar <= '9')); theChar = nextCodePoint()) {
			sb.append((char)(theChar));
			integer.append((char)(theChar));
		}
		
		// We read an unused non-digit char that may not be part of this token
		// Next block either:
		// - consumes it, reads some digits, and ends with some other unread non-digit char that may not be part of this token
		// OR
		// - ignores it because it isn't a dot, leaving it unread
		// Either way, it ends with some unread non-digit character
		
		if (theChar >= 0) {
			// Have minus integer. May be followed by dot fractional.
			if (theChar == '.') {
				sb.append('.');
				
				// At least one digit is required
				theChar = nextCodePoint();
				if ((theChar < '0') || (theChar > '9')) {
					throw DECIMAL_POINT_REQUIRES_DIGIT;
				}
				sb.append((char)(theChar));
				fractional.append((char)(theChar));
				
				// Consume more digits
				for (theChar = nextCodePoint(); ((theChar >= '0') && (theChar <= '9')); theChar = nextCodePoint()) {
					sb.append((char)(theChar));
					fractional.append((char)(theChar));
				}
			}
		}
		
		// As noted above, we read an unused non-digit char that may not be part of this token.
		// The next block is like the previous - it either consumes an exponent, sign, and digits or doesn't.
		// Either way, it ends with an unsed non-digit char.
			
		if (theChar >= 0) {
			// Have minus integer dot fractional. May be followed by exponent.
			if ((theChar == 'e') || (theChar == 'E')) {
				sb.append((char)(theChar));
				
				// May be followed by - or +
				theChar = nextCodePoint();
				if ((theChar == '-') || (theChar == '+')) {
					sb.append((char)(theChar));
					positiveExponent = theChar == '+';
					
					theChar = nextCodePoint();
				}
				
				// At least one digit is required after optional sign
				if ((theChar < '0') || (theChar > '9')) {
					throw EXPONENT_REQUIRES_DIGIT;
				}
				sb.append((char)(theChar));
				exponent.append((char)(theChar));
				
				// Consume more digits
				for (theChar = nextCodePoint(); ((theChar >= '0') && (theChar <= '9')); theChar = nextCodePoint()) {
					sb.append((char)(theChar));
					exponent.append((char)(theChar));
				}
			}
		}
		
		// Unread unused char (unless it is EOF), it is first char of next token
		if (theChar >= 0) {
			unreadCodePoint(theChar);
		}
		
		// Construct with number properties
		return new LexerToken(
			sb.toString(),
			positive,
			integer.toString(),
			fractional.toString(),
			positiveExponent,
			exponent.toString()
		);
	}
	
	private LexerToken lexBoolean(final int firstChar) {
		// First char is t or f
		LexerToken result = null;
		if (firstChar == 't') {
			if (nextCodePoint() == 'r') {
				if (nextCodePoint() == 'u') {
					if (nextCodePoint() == 'e') {
						result = LexerToken.TRUE_TOKEN;
					}
				}
			}
		} else {
			if (nextCodePoint() == 'a') {
				if (nextCodePoint() == 'l') {
					if (nextCodePoint() == 's') {
						if (nextCodePoint() == 'e') {
							result = LexerToken.FALSE_TOKEN;
						}
					}
				}
			}
		}
		
		if (result == null) {
			throw BOOLEAN_SPELLED_TRUE_OR_FALSE;
		}
		
		return result;
	}
	
	private LexerToken lexNull() {
		// First char is n
		LexerToken result = null;
		if (nextCodePoint() == 'u') {
			if (nextCodePoint() == 'l') {
				if (nextCodePoint() == 'l') {
					result = LexerToken.NULL_TOKEN;
				}
			}
		}
		
		if (result == null) {
			throw NULL_SPELLING;
		}
		
		return result;
	}
	
	// Lex next token.
	// If there are no more tokens, an empty Optional is returned.
	// Otherwise, a Optional containing the next token is returned.
	public Optional<LexerToken> lex() {
		LexerToken result = null;

		// Skip whitespace
		int theChar = ' '; // guarantee while loop executes at least once
		while ((theChar == ' ') || (theChar == '\n') || (theChar == '\r') || (theChar == '\t')) {
			theChar = nextCodePoint();
		}
		
		// Do nothing if EOF, else parse next token
		if (theChar >= 0) {
			if ((theChar >= '0') && (theChar <= '9')) {
				result = lexNumber(theChar);
			} else {
				switch (theChar) {
					case '-':
						result = lexNumber(theChar);
						break;
						
					case '"':
						result = lexString();
						break;
					
					case 't':
						result = lexBoolean(theChar);
						break;
						
					case 'f':
						result = lexBoolean(theChar);
						break;
						
					case 'n':
						result = lexNull();
						break;
						
					case ',':
						result = LexerToken.COMMA_TOKEN;
						break;
					
					case '{':
						result = LexerToken.OPEN_BRACE_TOKEN;
						break;
						
					case ':':
						result = LexerToken.COLON_TOKEN;
						break;
						
					case '}':
						result = LexerToken.CLOSE_BRACE_TOKEN;
						break;
						
					case '[':
						result = LexerToken.OPEN_BRACKET_TOKEN;
						break;
						
					case ']':
						result = LexerToken.CLOSE_BRACKET_TOKEN;
						break;
						
					default: {
						throw new RuntimeException(String.format(
							Lexer.INVALID_CHARACTER_FMT,
							Unicode.formatAsUnicodeEscapes(theChar),
							String.format("%d:%d", Integer.valueOf(currentLine), Integer.valueOf(currentPos))
						));
					}
				}
			}
		}
	
		// Save token in case iteration used
		token = Optional.ofNullable(result);
		return token;
	}
	
	// ==== Iterator
	
	@Override
	public boolean hasNext() {
		if (! searched) {
			lex();
			// Allow any number of hasNext() calls in a row without next(), like other Java containers
			searched = true;
		}
		return token.isPresent();
	}
	
	@Override
	public LexerToken next() {
		// Can call next without hasNext, like other Java conrainers
		hasNext();
		searched = false;
		return token.orElse(null);
	}
	
	// ==== Iterable
	
	@Override
	public Iterator<LexerToken> iterator() {
		return this;
	}
	
	// ==== stream
	
	// Stream view of the lexical tokens
	public Stream<LexerToken> stream() {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this, 0), false);
	}
}
