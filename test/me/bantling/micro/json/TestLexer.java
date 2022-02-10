package me.bantling.micro.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import me.bantling.micro.util.TestUnicode;
import me.bantling.micro.util.Unicode;

@SuppressWarnings("static-method")
public class TestLexer {
	@Test
	public void lexString() {
		{
			final String[] goodCases = {
				" \n\"\"",
				"\r\"a\"",
                "\r\n\t\"a\"",
				"\"aß東𐐀\"",
				"\"\\\"\\\\\\//\\b\\f\\n\\r\\t\\u0041\\u00df\\u6771\\ud801\\udc00\\u0001\"",
			};
			final String[] goodResults = {
				"",
				"a",
				"a",
				"aß東𐐀",
				"\"\\//\b\f\n\r\tAß東𐐀\u0001",
			};
			
			for (int i = 0; i < goodCases.length; i++) {
				final String test = goodCases[i];
				final String expected = goodResults[i];
				
				final Optional<LexerToken> actual = new Lexer(new StringReader(test)).lex();
				assertTrue(actual.isPresent());
				assertEquals(expected, actual.get().token);
			}
		}

		{
			final String[] badCases = {
				"\"",
				"\"\u0001\"",
				"\"\\",
				"\"\\u",
				"\"\\u1",
				"\"\\u12",
				"\"\\u123",
				"\"\\ughij",
				"\"\\z",
			};
			
			final String[] messages = {
					Lexer.INCOMPLETE_STRING.getMessage(),
					Lexer.NO_ASCII_CONTROL.getMessage(),
					Lexer.INCOMPLETE_BACKSLASH_ESCAPE.getMessage(),
					Lexer.INCOMPLETE_UNICODE_ESCAPE.getMessage(),
					Lexer.INCOMPLETE_UNICODE_ESCAPE.getMessage(),
					Lexer.INCOMPLETE_UNICODE_ESCAPE.getMessage(),
					Lexer.INCOMPLETE_UNICODE_ESCAPE.getMessage(),
					String.format(Lexer.INVALID_UNICODE_ESCAPE_FMT, "ghij"),
                    String.format(Lexer.INVALID_BACKSLASH_ESCAPE_FMT, "z"),
			};
			
			for (int i = 0; i < badCases.length; i++) {
				try {
					new Lexer(new StringReader(badCases[i])).lex();
					Assert.fail("Bad string cases must fail");
				} catch (final RuntimeException e) {
					assertEquals(messages[i], e.getMessage());
				}
			}
		}
	}
	
	@Test
	public void lexNumber() {
		{
			final String[] goodCases = {
				"0",
				"12",
				"1.2",
				"12.3",
				"1.23",
				"12.34",
				"-1",
				"1.2e3",
				"12.34E56",
				"-12.34e+56",
				"-12.34E-56",
				"1e2",
				"1E+2",
				"1e-2a",
				"1a",
				"-1a",
			};
			final LexerToken[] goodResults = {
				new LexerToken("0", true, "0", "", true, ""),
				new LexerToken("12", true, "12", "", true, ""),
				new LexerToken("1.2", true, "1", "2", true, ""),
				new LexerToken("12.3", true, "12", "3", true, ""),
				new LexerToken("1.23", true, "1", "23", true, ""),
				new LexerToken("12.34", true, "12", "34", true, ""),
				new LexerToken("-1", false, "1", "", true, ""),
				new LexerToken("1.2e3", true, "1", "2", true, "3"),
				new LexerToken("12.34E56", true, "12", "34", true, "56"),
				new LexerToken("-12.34e+56", false, "12", "34", true, "56"),
				new LexerToken("-12.34E-56", false, "12", "34", false, "56"),
				new LexerToken("1e2", true, "1", "", true, "2"),
				new LexerToken("1E+2", true, "1", "", true, "2"),
				new LexerToken("1e-2", true, "1", "", false, "2"),
				new LexerToken("1", true, "1", "", true, ""),
				new LexerToken("-1", false, "1", "", true, ""),
			};
			
			for (int i = 0; i < goodCases.length; i++) {
				final String test = goodCases[i];
				final LexerToken expected = goodResults[i];
				
				final Optional<LexerToken> actual = new Lexer(new StringReader(test)).lex();
				assertTrue(actual.isPresent());
				assertEquals(expected, actual.get());
			}
		}

		{
			final String[] badCases = {
				"-",
				"-a",
                "-!",
				"1.",
				"1.a",
				"1e",
				"1E+",
				"1e-",
				"1ea",
			};
			
			final String[] messages = {
					Lexer.INCOMPLETE_NEGATIVE_NUMBER.getMessage(),
					Lexer.MINUS_SIGN_REQUIRES_DIGIT.getMessage(),
                    Lexer.MINUS_SIGN_REQUIRES_DIGIT.getMessage(),
					Lexer.DECIMAL_POINT_REQUIRES_DIGIT.getMessage(),
					Lexer.DECIMAL_POINT_REQUIRES_DIGIT.getMessage(),
					Lexer.EXPONENT_REQUIRES_DIGIT.getMessage(),
					Lexer.EXPONENT_REQUIRES_DIGIT.getMessage(),
					Lexer.EXPONENT_REQUIRES_DIGIT.getMessage(),
					Lexer.EXPONENT_REQUIRES_DIGIT.getMessage(),
			};
			
			for (int i = 0; i < badCases.length; i++) {
				try {
					new Lexer(new StringReader(badCases[i])).lex();
					Assert.fail("Bad number cases must fail");
				} catch (final RuntimeException e) {
					assertEquals(messages[i], e.getMessage());
				}
			}
		}	
	}
	
	@Test
	public void lexBoolean() {
		{
			final String[] goodCases = {
				"true",
				"false",
			};
			final LexerToken[] goodResults = {
				new LexerToken(LexerToken.Type.TRUE, "true"),
				new LexerToken(LexerToken.Type.FALSE, "false"),
			};
			
			for (int i = 0; i < goodCases.length; i++) {
				final String test = goodCases[i];
				final LexerToken expected = goodResults[i];
				
				final Optional<LexerToken> actual = new Lexer(new StringReader(test)).lex();
				assertTrue(actual.isPresent());
				assertEquals(expected, actual.get());
			}
		}

		{
			final String[] badCases = {
				"t",
				"tr",
				"tru",
				"tx",
				"trx",
				"trux",
				"f",
				"fa",
				"fal",
				"fals",
				"fx",
				"fax",
				"falx",
				"falsx",
			};
			
			for (int i = 0; i < badCases.length; i++) {
				try {
					new Lexer(new StringReader(badCases[i])).lex();
					Assert.fail("Bad number cases must fail");
				} catch (final RuntimeException e) {
					assertEquals(Lexer.BOOLEAN_SPELLED_TRUE_OR_FALSE, e);
				}
			}
		}	
	}
	
	@Test
	public void lexNull() {
		{
			final String goodCase = "null";
			final LexerToken expected = new LexerToken(LexerToken.Type.NULL, "null");
			final Optional<LexerToken> actual = new Lexer(new StringReader(goodCase)).lex();
			assertTrue(actual.isPresent());
			assertEquals(expected, actual.get());
		}

		{
			final String[] badCases = {
				"n",
				"nu",
				"nul",
				"nx",
				"nux",
				"nulx",
			};
			
			for (int i = 0; i < badCases.length; i++) {
				try {
					new Lexer(new StringReader(badCases[i])).lex();
					Assert.fail("Bad number cases must fail");
				} catch (final RuntimeException e) {
					assertEquals(Lexer.NULL_SPELLING, e);
				}
			}
		}	
	}
	
	@Test
	public void lexOthers() {
		{
			final String[] goodCases = {
				",",
				"{",
				":",
				"}",
				"[",
				"]",
			};
			final LexerToken[] goodResults = {
				LexerToken.COMMA_TOKEN,
				LexerToken.OPEN_BRACE_TOKEN,
				LexerToken.COLON_TOKEN,
				LexerToken.CLOSE_BRACE_TOKEN,
				LexerToken.OPEN_BRACKET_TOKEN,
				LexerToken.CLOSE_BRACKET_TOKEN,
			};
			
			for (int i = 0; i < goodCases.length; i++) {
				final String test = goodCases[i];
				final LexerToken expected = goodResults[i];
				
				final Optional<LexerToken> actual = new Lexer(new StringReader(test)).lex();
				assertTrue(actual.isPresent());
				assertEquals(expected, actual.get());
			}
		}

		{
			final String[] badCases = {
				"~",
				"x",
				"<",
				"ß",
				"東",
				"𐐀",
			};
			
			for (int i = 0; i < badCases.length; i++) {
				try {
					new Lexer(new StringReader(badCases[i])).lex();
					Assert.fail("Bad lex cases must fail");
				} catch (final RuntimeException e) {
					String escaped = Unicode.formatAsUnicodeEscapes(badCases[i].codePointAt(0));
					assertEquals(
						String.format(
							Lexer.INVALID_CHARACTER_FMT,
							escaped,
							"1:1"
						),
						e.getMessage()
					);
				}
			}
		}	
	}
	
	@Test
	public void lexLoop() throws Throwable {
		// Data to use
		final Reader source = new StringReader("12.34-5E-7\"dude\"truefalsenull,{:}[]");
		final LexerToken[] expected = {
			new LexerToken("12.34", true, "12", "34", true, ""),
			new LexerToken("-5E-7", false, "5", "", false, "7"),
			new LexerToken(LexerToken.Type.STRING, "dude"),
			LexerToken.TRUE_TOKEN,
			LexerToken.FALSE_TOKEN,
			LexerToken.NULL_TOKEN,
			LexerToken.COMMA_TOKEN,
			LexerToken.OPEN_BRACE_TOKEN,
			LexerToken.COLON_TOKEN,
			LexerToken.CLOSE_BRACE_TOKEN,
			LexerToken.OPEN_BRACKET_TOKEN,
			LexerToken.CLOSE_BRACKET_TOKEN,
		};
		
		// Loop using lex()
		{
			source.reset();
			Lexer l = new Lexer(source);
			int i = 0;
			for (Optional<LexerToken> t = l.lex(); t.isPresent(); t = l.lex()) {
				assertEquals(Optional.of(expected[i++]), t);
			}
			assertEquals(expected.length, i);
			assertEquals(Optional.empty(), l.lex());
		}
		
		// Loop using hasNext and next
		{
			source.reset();
			Lexer l = new Lexer(source);
			int i = 0;
			for (Iterator<LexerToken> it = l; it.hasNext();) {
				assertEquals(expected[i++], it.next());
			}
			assertEquals(expected.length, i);
			assertEquals(Optional.empty(), l.lex());
		}
		
		// Loop using generic for loop
		{
			source.reset();
			Lexer l = new Lexer(source);
			int i = 0;
			for (LexerToken t : l) {
				assertEquals(expected[i++], t);
			}
			assertEquals(expected.length, i);
			assertEquals(Optional.empty(), l.lex());
		}
		
		// Loop using stream
		{
			source.reset();
			Lexer l = new Lexer(source);
			int[] i = {0};
			assertEquals(
				expected.length,
				l.stream().
					peek(t -> assertEquals(expected[i[0]++], t)).
					count()
			);
			assertEquals(expected.length, i[0]);
			assertEquals(Optional.empty(), l.lex());
		}
	}
	
	@Test
	public void other() {
	    {
    	    // Invalid unicode
    	    final Reader testCase = new StringReader("\ud801");
            try {
                new Lexer(testCase).lex();
                fail("Must die");
            } catch (final Throwable t) {
                assertTrue(t instanceof RuntimeException);
                assertTrue(t.getCause() instanceof IOException);
                assertEquals(String.format(TestUnicode.HIGH_SURROGATE_EOF, "1:0"), t.getCause().getMessage());
            }
	    }
	}
}
