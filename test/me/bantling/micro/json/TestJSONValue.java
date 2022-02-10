package me.bantling.micro.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.Test;

@SuppressWarnings({ "static-method", "unlikely-arg-type" })
public class TestJSONValue {
    @Test
    void stringValue() {
        final JSONValue str = JSONValue.of("abc");
        
        assertEquals(Objects.hashCode("abc"), str.hashCode());
        assertTrue(str.equals(str));
        assertTrue(str.equals(JSONValue.of("abc")));
        assertFalse(str.equals("abc"));
        assertEquals("JSONValue[type=STRING,instance=abc]", str.toString());
        
        assertTrue(str.isString());
        assertFalse(str.isNumber());
        assertFalse(str.isBoolean());
        assertFalse(str.isNull());
        assertFalse(str.isObject());
        assertFalse(str.isArray());
        
        assertEquals("abc", str.asString());
        try {
            str.asNumber();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_A_NUMBER, t);
        }
        try {
            str.asBoolean();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_A_BOOLEAN, t);
        }
        try {
            str.asObject();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_AN_OBJECT, t);
        }
        try {
            str.asArray();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_AN_ARRAY, t);
        }
        
        assertEquals(JSONValue.Type.STRING, str.getType());
        assertEquals("abc", str.getInstance());
    }

    @Test
    void numberValue() {
        final JSONNumber instance = JSONNumber.of(5);
        final JSONValue number = JSONValue.of(instance);
        
        assertEquals(instance.hashCode(), number.hashCode());
        assertTrue(number.equals(number));
        assertTrue(number.equals(JSONValue.of(JSONNumber.of(5))));
        assertEquals("JSONValue[type=NUMBER,instance=5]", number.toString());
        
        assertFalse(number.isString());
        assertTrue(number.isNumber());
        assertFalse(number.isBoolean());
        assertFalse(number.isNull());
        assertFalse(number.isObject());
        assertFalse(number.isArray());
        
        assertEquals(instance, number.asNumber());
        try {
            number.asString();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_A_STRING, t);
        }
        try {
            number.asBoolean();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_A_BOOLEAN, t);
        }
        try {
            number.asObject();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_AN_OBJECT, t);
        }
        try {
            number.asArray();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_AN_ARRAY, t);
        }
        
        assertEquals(JSONValue.Type.NUMBER, number.getType());
        assertEquals(instance, number.getInstance());
    }

    @Test
    void booleanValue() {
        final JSONValue bool = JSONValue.of(true);
        
        assertEquals(Boolean.hashCode(true), bool.hashCode());
        assertTrue(bool.equals(bool));
        assertTrue(bool.equals(JSONValue.of(Boolean.TRUE)));
        assertEquals("JSONValue[type=BOOLEAN,instance=true]", bool.toString());
        
        assertFalse(bool.isString());
        assertFalse(bool.isNumber());
        assertTrue(bool.isBoolean());
        assertFalse(bool.isNull());
        assertFalse(bool.isObject());
        assertFalse(bool.isArray());
        
        assertTrue(bool.asBoolean());
        try {
            bool.asString();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_A_STRING, t);
        }
        try {
            bool.asNumber();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_A_NUMBER, t);
        }
        try {
            bool.asObject();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_AN_OBJECT, t);
        }
        try {
            bool.asArray();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_AN_ARRAY, t);
        }
        
        assertEquals(JSONValue.Type.BOOLEAN, bool.getType());
        assertEquals(Boolean.TRUE, bool.getInstance());
        assertFalse(JSONValue.of(false).asBoolean());
        assertFalse(JSONValue.of(Boolean.FALSE).asBoolean());
        assertFalse(JSONValue.of((Boolean)(null)).asBoolean());
    }

    @Test
    void nullValue() {
        final JSONValue nul = JSONValue.ofNull();
        
        assertEquals(0, nul.hashCode());
        assertTrue(nul.equals(nul));
        assertTrue(nul.equals(JSONValue.ofNull()));
        assertEquals("JSONValue[type=NULL,instance=null]", nul.toString());
        
        assertFalse(nul.isString());
        assertFalse(nul.isNumber());
        assertFalse(nul.isBoolean());
        assertTrue(nul.isNull());
        assertFalse(nul.isObject());
        assertFalse(nul.isArray());
        
        try {
            nul.asString();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_A_STRING, t);
        }
        try {
            nul.asNumber();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_A_NUMBER, t);
        }
        try {
            nul.asBoolean();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_A_BOOLEAN, t);
        }
        try {
            nul.asObject();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_AN_OBJECT, t);
        }
        try {
            nul.asArray();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_AN_ARRAY, t);
        }
        
        assertEquals(JSONValue.Type.NULL, nul.getType());
    }

    @Test
    void objectValue() {
        final Map<String, JSONValue> map = Collections.singletonMap("a", JSONValue.of("b"));
        final JSONValue object = JSONValue.of(map);
        
        assertEquals(map.hashCode(), object.hashCode());
        assertTrue(object.equals(object));
        assertTrue(object.equals(JSONValue.of(Collections.singletonMap("a", JSONValue.of("b")))));
        assertEquals("JSONValue[type=OBJECT,instance={a=JSONValue[type=STRING,instance=b]}]", object.toString());
        
        assertFalse(object.isString());
        assertFalse(object.isNumber());
        assertFalse(object.isBoolean());
        assertFalse(object.isNull());
        assertTrue(object.isObject());
        assertFalse(object.isArray());
        
        assertTrue(map == object.asObject());
        try {
            object.asString();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_A_STRING, t);
        }
        try {
            object.asNumber();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_A_NUMBER, t);
        }
        try {
            object.asBoolean();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_A_BOOLEAN, t);
        }
        try {
            object.asArray();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_AN_ARRAY, t);
        }
        
        assertEquals(JSONValue.Type.OBJECT, object.getType());
        assertEquals(map, object.getInstance());
        final Map<String, JSONValue> emptyMap = Collections.emptyMap();
        assertEquals(emptyMap, JSONValue.of((Map<String, JSONValue>)(null)).asObject());
    }

    @Test
    void arrayValue() {
        final List<JSONValue> list = Collections.singletonList(JSONValue.of("c"));
        final JSONValue array = JSONValue.of(list);
        
        assertEquals(list.hashCode(), array.hashCode());
        assertTrue(array.equals(array));
        assertTrue(array.equals(JSONValue.of(Collections.singletonList(JSONValue.of("c")))));
        assertEquals("JSONValue[type=ARRAY,instance=[JSONValue[type=STRING,instance=c]]]", array.toString());
        
        assertFalse(array.isString());
        assertFalse(array.isNumber());
        assertFalse(array.isBoolean());
        assertFalse(array.isNull());
        assertFalse(array.isObject());
        assertTrue(array.isArray());
        
        assertTrue(list == array.asArray());
        try {
            array.asString();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_A_STRING, t);
        }
        try {
            array.asNumber();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_A_NUMBER, t);
        }
        try {
            array.asBoolean();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_A_BOOLEAN, t);
        }
        try {
            array.asObject();
            fail("Must die");
        } catch (final Throwable t) {
            assertEquals(JSONValue.NOT_AN_OBJECT, t);
        }
        
        assertEquals(JSONValue.Type.ARRAY, array.getType());
        assertEquals(list, array.getInstance());
        final List<JSONValue> emptyList = Collections.emptyList();
        assertEquals(emptyList, JSONValue.of((List<JSONValue>)(null)).asArray());
    }
}
