package me.bantling.micro.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;

import org.junit.jupiter.api.Test;

import me.bantling.micro.util.Collections;
import me.bantling.micro.util.Tuple;

@SuppressWarnings({ "static-method", "static-access", "unchecked" })
public class TestServerBuilder {
    @Test
    void variablePathPartRegex() {
        // A path part must be either {name} or not start with a {
        final Tuple.Same same = new Tuple.Same();
        final List<Tuple.TwoOf<String>> testCases = Collections.listOf(
            // Success
            same.of("{f}",   "f"),
            same.of("{foo}", "foo"),
            // Failure
            same.ofNullable("fixed",   null),
            same.ofNullable("{",       null),
            same.ofNullable("}",       null),
            same.ofNullable("{}",      null),
            same.ofNullable("{1}",     null),
            same.ofNullable("{foo1}",  null),
            same.ofNullable("a{bar}",  null),
            same.ofNullable("{bar}b",  null),
            same.ofNullable("a{bar}b", null)
        );
        
        for (final Tuple.TwoOf<String> testCase : testCases) {
            final String test = testCase.get1();
            final String expected = testCase.get2();
            
            final Matcher m = ServerBuilder.VARIABLE_PATH_PART.matcher(test);
            if (expected != null) {
                // Success
                assertTrue(m.matches());
                assertEquals(expected, m.group(1));
            } else {
                // Failure
                assertFalse(m.matches());
            }
        }
    }
    
    @Test
    void queryParamsPart() {
        // A query param must be name:type[+]?
        final List<Tuple.UpToFourOf<String>> testCases = Collections.listOf(
            // Success
            Tuple.UpToSame.four("foo:string",  "foo", "string"),
            Tuple.UpToSame.four("foo:string+", "foo", "string", "+"),
            // Failure
            Tuple.UpToSame.four("foo"),
            Tuple.UpToSame.four("{"),
            Tuple.UpToSame.four("}"),
            Tuple.UpToSame.four("{}"),
            Tuple.UpToSame.four("{1}"),
            Tuple.UpToSame.four("{foo1}")
        );
        
        for (final Tuple.UpToFourOf<String> testCase : testCases) {
            final int count = testCase.getCount();
            final String test = testCase.get1();
            final String name = testCase.get2();
            final String type = testCase.get3();
            final String optional = testCase.get4();
            
            final Matcher m = ServerBuilder.QUERY_PARAMS_PART.matcher(test);
            if (count > 1) {
                // Success
                assertTrue(m.matches());
                assertEquals(name, m.group(1));
                assertEquals(type, m.group(2));
                assertEquals(optional, m.group(3));
            } else {
                // Failure
                assertFalse(m.matches());
            }
        }
    }
    
    @Test
    void parseURL() {
        final Function<Object[], PathElement> makePathPart = array -> {
            final PathElement pathPart = new PathElement();
            
//            pathPart.fixedPart = (String)(array[0]);
//            if (array.length > 1) {
//                pathPart.variablePart = (VariablePart)(array[1]);
//            }
            if (array.length > 2) {
                pathPart.nextParts = (Map<PathElement, PathElement>)(array[2]);
            }
            
            return pathPart; 
        };
        
        final List<Tuple.Two<String, List<PathElement>>> testCases = Collections.listOf(
            Tuple.of(
                "/foo",
                Collections.listOf(
                    makePathPart.apply(new Object[] {"foo"})
                )
            )
        );
    }
}
