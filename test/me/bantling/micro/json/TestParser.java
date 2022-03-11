package me.bantling.micro.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import me.bantling.micro.util.Collections;

@SuppressWarnings("static-method")
public class TestParser {
	@Test
	public void parseObject() {
		{
			final String[] goodCases = {
				"\r{}\n",
				" { } ",
				"{\"a\":\"b\"}",
				"{ \"a\": 1 \n, \"c\" :true\r,\"b\":false, \"d\" : null}",
				"{\"e\": [\"f\"]}",
			};
			
			final JSONValue[] goodResults = {
				JSONValue.of(Map.of()),
				JSONValue.of(Map.of()),
				JSONValue.of(Map.of("a", JSONValue.of("b"))),
				JSONValue.of(Map.of(
					"a", JSONValue.of(new JSONNumber("1", true, "1", "", true, "")),
					"c", JSONValue.TRUE_VALUE,
                    "b", JSONValue.FALSE_VALUE,
					"d", JSONValue.NULL_VALUE
				)),
				JSONValue.of(Map.of(
					"e", JSONValue.of(List.of(JSONValue.of("f")))
				)),
			};
			
			for (int i = 0; i < goodCases.length; i++) {
				final String test = goodCases[i];
				final JSONValue expected = goodResults[i];
				
				final Optional<JSONValue> actual = new Parser(new StringReader(test)).parse();
				assertTrue(actual.isPresent());
				assertEquals(expected, actual.get());
			}
		}
	}
	
	@Test
	public void parseObjectLoop() throws Throwable {
		// Data to use
		final Reader source = new StringReader("{ \"a\": 1 , \"c\" :true, \"d\" : null}");
		final JSONValue expected = JSONValue.of(Map.of(
			"a", JSONValue.of(new JSONNumber("1", true, "1", "", true, "")),
			"c", JSONValue.TRUE_VALUE,
			"d", JSONValue.NULL_VALUE
		));
		
		// Loop using parse()
		{
			source.reset();
			Parser p = new Parser(source);
			assertEquals(Optional.of(expected), p.parse());
			assertEquals(Optional.empty(), p.parse());
		}
		
		// Loop using hasNext and next
		{
			source.reset();
			Parser p = new Parser(source);
			assertTrue(p.hasNext());
			assertEquals(expected, p.next());
			assertFalse(p.hasNext());
		}
		
		// Loop using generic for loop
		{
			source.reset();
			Parser p = new Parser(source);
			int i = 0;
			for (JSONValue j : p) {
				assertEquals(expected, j);
				i++;
			}
			assertEquals(1, i);
			assertEquals(Optional.empty(), p.parse());
		}
		
		// Loop using stream
		{
			source.reset();
			Parser p = new Parser(source);
			int i[] = {0};
			assertEquals(
				p.stream().
					peek(t -> { assertEquals(expected, t); i[0]++; }).
					count(),
				1
			);
			assertEquals(1, i[0]);
			assertEquals(Optional.empty(), p.parse());
		}
	}

	@Test
	public void parseArray() {
		{
			final String[] goodCases = {
				"[]",
				"[1]",
				"[ 1, \"a\" ]",
				"[ 1, { \"a\": 2 }, 3 ]",
				"[[\"a\",\"b\"]]"
			};
			
			final JSONValue[][] goodResults = {
				new JSONValue[0],
                new JSONValue[] {
                    JSONValue.of(new JSONNumber("1", true, "1", "", true, ""))
                },
				new JSONValue[] {
					JSONValue.of(new JSONNumber("1", true, "1", "", true, "")),
					JSONValue.of("a")
				},
				new JSONValue[] {
					JSONValue.of(new JSONNumber("1", true, "1", "", true, "")),
					JSONValue.of(Map.of(
						"a", JSONValue.of(new JSONNumber("2", true, "2", "", true, ""))
					)),
					JSONValue.of(new JSONNumber("3", true, "3", "", true, ""))
				},
				new JSONValue[] {
			        JSONValue.of(
		                Collections.listOf(new LinkedList<JSONValue>()).
		                    add(
	                            JSONValue.of("a"),
                                JSONValue.of("b")
                            ).
		                done()
	                )
				}
			};
			
			for (int i = 0; i < goodCases.length; i++) {
				final String test = goodCases[i];
				final JSONValue[] allExpected = goodResults[i];
				
				final Parser p = new Parser(new StringReader(test));
				if (allExpected.length == 0) {
					assertTrue(p.parse().isEmpty());
				} else {
					for (final JSONValue expected : allExpected) {
						final Optional<JSONValue> actual = p.parse();
						assertTrue(actual.isPresent());
						assertEquals(expected, actual.get());
					}
				}
			}
		}
	}
	
	@Test
	public void parseArrayLoop() throws Throwable {
		// Data to use
		final Reader source = new StringReader("[ 1, { \"a\": 2 }, 3 ]");
		final JSONValue[] expected = new JSONValue[] {
			JSONValue.of(new JSONNumber("1", true, "1", "", true, "")),
			JSONValue.of(Map.of(
				"a", JSONValue.of(new JSONNumber("2", true, "2", "", true, ""))
			)),
			JSONValue.of(new JSONNumber("3", true, "3", "", true, ""))
		};
		
		// Loop using parse()
		{
			source.reset();
			Parser p = new Parser(source);
			int i = 0;
			for (Optional<JSONValue> t = p.parse(); t.isPresent(); t = p.parse()) {
				assertEquals(Optional.of(expected[i++]), t);
			}
			assertEquals(expected.length, i);
			assertEquals(Optional.empty(), p.parse());
		}
		
		// Loop using hasNext and next
		{
			source.reset();
			Parser p = new Parser(source);
			int i = 0;
			for (Iterator<JSONValue> it = p; it.hasNext();) {
				assertEquals(expected[i++], it.next());
			}
			assertEquals(expected.length, i);
			assertEquals(Optional.empty(), p.parse());
		}
		
		// Loop using generic for loop
		{
			source.reset();
			Parser p = new Parser(source);
			int i = 0;
			for (JSONValue j : p) {
				assertEquals(expected[i++], j);
			}
			assertEquals(expected.length, i);
			assertEquals(Optional.empty(), p.parse());
		}
		
		// Loop using stream
		{
			source.reset();
			Parser p = new Parser(source);
			int i[] = {0};
			assertEquals(
				expected.length,
				p.stream().
					peek(t -> assertEquals(expected[i[0]++], t)).
					count()
			);
			assertEquals(expected.length, i[0]);
			assertEquals(Optional.empty(), p.parse());
		}
	}
	
	@Test
	public void peek() throws Throwable {
	    final Method peek = Parser.class.getDeclaredMethod("peek");
	    peek.setAccessible(true);
	    
	    final Parser p = new Parser(new StringReader("1"));
	    assertEquals(LexerToken.Type.NUMBER, peek.invoke(p));
        assertEquals(LexerToken.Type.NUMBER, peek.invoke(p));
	}
	
	@Test
	public void expect() throws Throwable {
	    final Method expect = Parser.class.getDeclaredMethod("expect", RuntimeException.class, LexerToken.Type[].class);
	    expect.setAccessible(true);
	    
	    final Parser p = new Parser(new StringReader("1 2 3"));
	    final RuntimeException error = new RuntimeException();
	    try {
	        expect.invoke(p, error, new LexerToken.Type[0]);
	    } catch (final InvocationTargetException e) {
	        assertTrue(error == e.getCause());
	    }
	    
	    assertEquals(
            new LexerToken("2", true, "2", "", true, ""),
            expect.invoke(p, error, new LexerToken.Type[] {LexerToken.Type.NUMBER})
        );
	}
}
