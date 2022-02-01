package me.bantling.micro.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public class TestParser {
	@Test
	public void parseObject() {
		{
			final String[] goodCases = {
				"\r{}\n",
				" { } ",
				"{\"a\":\"b\"}",
				"{ \"a\": 1 \n, \"c\" :true\r, \"d\" : null}",
				"{\"e\": [\"f\"]}",
			};
			
			final JSONValue[] goodResults = {
				JSONValue.ofObject(Map.of()),
				JSONValue.ofObject(Map.of()),
				JSONValue.ofObject(Map.of("a", JSONValue.ofString("b"))),
				JSONValue.ofObject(Map.of(
					"a", JSONValue.ofNumber(new JSONNumber("1", true, "1", "", true, "")),
					"c", JSONValue.TRUE_VALUE,
					"d", JSONValue.NULL_VALUE
				)),
				JSONValue.ofObject(Map.of(
					"e", JSONValue.ofArray(List.of(JSONValue.ofString("f")))
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
		final JSONValue expected = JSONValue.ofObject(Map.of(
			"a", JSONValue.ofNumber(new JSONNumber("1", true, "1", "", true, "")),
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
				"[ 1, \"a\" ]",
				"[ 1, { \"a\": 2 }, 3 ]",
			};
			
			final JSONValue[][] goodResults = {
				new JSONValue[0],
				new JSONValue[] {
					JSONValue.ofNumber(new JSONNumber("1", true, "1", "", true, "")),
					JSONValue.ofString("a")
				},
				new JSONValue[] {
					JSONValue.ofNumber(new JSONNumber("1", true, "1", "", true, "")),
					JSONValue.ofObject(Map.of(
						"a", JSONValue.ofNumber(new JSONNumber("2", true, "2", "", true, ""))
					)),
					JSONValue.ofNumber(new JSONNumber("3", true, "3", "", true, ""))
				},
			};
			
			for (int i = 0; i < goodCases.length; i++) {
				final String test = goodCases[i];
				final JSONValue[] allExpected = goodResults[i];
				
				final Parser p = new Parser(new StringReader(test));
				if (allExpected.length == 0) {
					assertTrue(p.parse().isEmpty());
//					System.out.println("empty array");
				} else {
					for (final JSONValue expected : allExpected) {
						final Optional<JSONValue> actual = p.parse();
						assertTrue(actual.isPresent());
						assertEquals(expected, actual.get());
//						System.out.println(expected);
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
			JSONValue.ofNumber(new JSONNumber("1", true, "1", "", true, "")),
			JSONValue.ofObject(Map.of(
				"a", JSONValue.ofNumber(new JSONNumber("2", true, "2", "", true, ""))
			)),
			JSONValue.ofNumber(new JSONNumber("3", true, "3", "", true, ""))
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
}
