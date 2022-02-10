package me.bantling.micro.json;

import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

// Parse a series of lexical tokens from a reader.
// The parser ensures the tokens arrive in a correct order for a JSON document.
// The result of the parse is a JSONValue that is either a document or an array.
public class Parser implements Iterator<JSONValue>, Iterable<JSONValue> {
	private static final RuntimeException START_BRACE_OR_BRACKET    = new RuntimeException("A JSON document must be begin with a curly brace or opening square bracket");
	private static final RuntimeException OBJECT_FIRST_KEY          = new RuntimeException("A JSON object must a string key or closing brace after opening brace");
	private static final RuntimeException OBJECT_KEY_COLON          = new RuntimeException("A JSON object key name must be followed by a colon");
	private static final RuntimeException OBJECT_KEY_COLON_VALUE    = new RuntimeException("A JSON object key name and colon must be followed by a value");
	private static final RuntimeException OBJECT_VALUE_COMMA_BRACE  = new RuntimeException("A JSON object value must be followed by a comma or closing brace");
	private static final RuntimeException OBJECT_COMMA_KEY          = new RuntimeException("A JSON object cannot have a trailing comma after the last key value pair");
	private static final RuntimeException ARRAY_VALUE_OR_BRACKET    = new RuntimeException("A JSON array opening square bracket must be followed by a value or closing square bracket");
	private static final RuntimeException ARRAY_COMMA_OR_BRACKET    = new RuntimeException("A JSON array element must be followed by a comma or closing square bracket");
	private static final RuntimeException ARRAY_COMMA_VALUE         = new RuntimeException("A JSON array cannot have a trailing comma after the last value");
	
	private enum State {
		START,
		ARRAY_NEXT_ELEMENT,
		STOP
	}
	
	// Underlying Lexer
	private final Lexer lexer;
	private State state;
	private LexerToken token;
	private Optional<JSONValue> value;
	private boolean searched;
	
	// Construct
	public Parser(
		final Reader reader
	) {
		this.lexer = new Lexer(reader);
		this.state = State.START;
	}
	
	// Take a peek at next token, caching the value for return by expect.
	// Once peek caches a token, all further calls to peek before the next call to expect will return the cached value.
	// Only expect will clear the cached value.
	// If the caller does not the value retained, then caller has to nullify token field.
	private LexerToken.Type peek() {
		token = token != null ? token : lexer.next();
		return token.type;
	}
	
	// unread puts a token returned by expect into the cache for next cal to expect
	private void unread(final LexerToken t) {
		token = t;
	}
	
	// Clear nullifies cached token
	private void clear() {
		token = null;
	}
	
	/*
	 * Expect one of the token types given to occur next in the lexer.
	 * If no matching token type is found, the error is thrown.
	 * If peek is true, the returned token is retained for the next call to expect, without invoking the lexer.
	 */
	private LexerToken expect(
		final RuntimeException error,
		final LexerToken.Type... expectedTypes
	) {
		// Return peeked token from last call, or lex the next one
		final LexerToken result = token != null ? token : lexer.next();
		// nullify retained token so previous peek is not infinite
		token = null;
		
		// See if the type matches any expected type
		for (LexerToken.Type expectedType : expectedTypes) {
			if (result.type == expectedType) {
				return result;
			}
		}
		
		// Die if no expected token type is found
		throw error;
	}
	
	// parseAnyValue parses any value
	private JSONValue parseAnyValue(
		final RuntimeException error
	) {
		final LexerToken firstToken = expect(
			error,
			LexerToken.Type.OPEN_BRACE,
			LexerToken.Type.OPEN_BRACKET,
			LexerToken.Type.STRING,
			LexerToken.Type.NUMBER,
			LexerToken.Type.TRUE,
			LexerToken.Type.FALSE,
			LexerToken.Type.NULL
		);
		
		switch (firstToken.type) {
			case OPEN_BRACE:
				unread(firstToken);
				return parseObject();
				
			case OPEN_BRACKET:
				unread(firstToken);
				return parseArray();
			
			case STRING:
				return JSONValue.of(firstToken.token);
				
			case NUMBER:
				return JSONValue.of(
					new JSONNumber(
						firstToken.token,
						firstToken.positive,
						firstToken.integer,
						firstToken.fractional,
						firstToken.positiveExponent,
						firstToken.exponent
					)
				);
				
			case TRUE:
			case FALSE:
				return JSONValue.of(firstToken.type == LexerToken.Type.TRUE);
			
			// Must be NULL
			default:
				return JSONValue.ofNull();
		}
	}
	
	// Parse an object 
	private JSONValue parseObject() {
		// Consume brace we know exists
		expect(null, LexerToken.Type.OPEN_BRACE);
		
		// Create map for object, retain original key order in case it matters to the consumer
		final Map<String, JSONValue> objectMap = new LinkedHashMap<>();
		
		// Check if closing brace is next, if so, we're done - empty object
		if (peek() == LexerToken.Type.CLOSE_BRACE) {
			// Consume brace
			clear();
		} else {
			// Get first key
			final String firstKey = expect(OBJECT_FIRST_KEY, LexerToken.Type.STRING).token;
			
			// Consume required colon
			expect(OBJECT_KEY_COLON, LexerToken.Type.COLON);
			
			// Consume required value and map it
			objectMap.put(firstKey, parseAnyValue(OBJECT_KEY_COLON_VALUE));
			
			// Consume zero or more additional key/value pairs
			while (
				expect(
					OBJECT_VALUE_COMMA_BRACE,
					LexerToken.Type.COMMA,
					LexerToken.Type.CLOSE_BRACE
				).type == LexerToken.Type.COMMA
			) {
				final String nextKey = expect(OBJECT_COMMA_KEY, LexerToken.Type.STRING).token;
				
				// Consume required colon
				expect(OBJECT_KEY_COLON, LexerToken.Type.COLON);
				
				// Consume required value and map it
				objectMap.put(nextKey, parseAnyValue(OBJECT_KEY_COLON_VALUE));
			}
		}
		
		return JSONValue.of(objectMap);
	}
	
	private JSONValue parseArray() {
		// Consume bracket, we don't need it
		expect(null, LexerToken.Type.OPEN_BRACKET);
		
		// Create list for object
		final List<JSONValue> arrayList = new LinkedList<>();
		
		// Cannot be closing bracket next, as parse method already checks empty arrays
		// Get first value
		arrayList.add(parseAnyValue(ARRAY_VALUE_OR_BRACKET));
		
		// Add any number of additional values preceded by commas
		while (
			expect(
				ARRAY_COMMA_OR_BRACKET,
				LexerToken.Type.COMMA,
				LexerToken.Type.CLOSE_BRACKET
			).type == LexerToken.Type.COMMA
		) {
			arrayList.add(parseAnyValue(ARRAY_COMMA_VALUE));
		}
		
		return JSONValue.of(arrayList);
	}
	
	// Top level parse method that starts parsing, or resumes where it left off
	public Optional<JSONValue> parse() {
		JSONValue result;
		
		switch (state) {
			case START:
				final LexerToken firstToken = expect(
					START_BRACE_OR_BRACKET,
					LexerToken.Type.OPEN_BRACE,
					LexerToken.Type.OPEN_BRACKET
				);
				
				switch (firstToken.type) {
					case OPEN_BRACE:
						unread(firstToken);
						result = parseObject();
						state = State.STOP;
						break;
					
					// Must be OPEN_BRACKET
					default:
						// Check if closing bracket is next, if so, we're done - no values to return
						if (peek() == LexerToken.Type.CLOSE_BRACKET) {
							// Consume bracket
							clear();
							result = null;
							state = State.STOP;
						} else {
							// Get first value
							result = parseAnyValue(ARRAY_VALUE_OR_BRACKET);
							state = State.ARRAY_NEXT_ELEMENT;
						}
						break;
				}
				break;
			
			case ARRAY_NEXT_ELEMENT: {
				if (expect(
					ARRAY_COMMA_OR_BRACKET,
					LexerToken.Type.COMMA,
					LexerToken.Type.CLOSE_BRACKET
				).type == LexerToken.Type.CLOSE_BRACKET) {
					// Check if closing bracket is next, if so, we're done - no values to return
					result = null;
					state = State.STOP;
				} else {
					// Get next value
					result = parseAnyValue(ARRAY_COMMA_VALUE);
				}
				break;
			}
			
			// Must be STOP. Do nothing, result is already null, so actions below are correct.
			default:
				result = null;
		}
		
		// Save value in case iteration used
		value = Optional.ofNullable(result);
		return value;
	}
	
	// ==== Iterator
	
	@Override
	public boolean hasNext() {
		if (! searched) {
			parse();
			// Allow any number of hasNext() calls in a row without next(), like other Java containers
			searched = true;
		}
		
		return value.isPresent();
	}
	
	@Override
	public JSONValue next() {
		// Can call next without hasNext, like other Java conrainers
		hasNext();
		searched = false;
		return value.orElse(null);
	}
	
	// ==== Iterable
	
	@Override
	public Iterator<JSONValue> iterator() {
		return this;
	}
	
	// ==== stream
	
	/*
	 *  Stream view of the json values:
	 * - a single top level object, or
	 * - each immediate child object/array in a top level array
	 */
	public Stream<JSONValue> stream() {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this, 0), false);
	}
}
