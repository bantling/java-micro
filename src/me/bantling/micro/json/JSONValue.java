package me.bantling.micro.json;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/*
 * A single value inside a JSON document.
 *  
 * In the case of OBJECT and ARRAY types, the value is recursive:
 * - An OBJECT is a Map<String, JSONValue>, where the value of any given key can be an OBJECT or ARRAY
 * - An ARRAY is a List<JSONVAlue>, where any given value can be an OBJECT or ARRAY
 * 
 * numbers are handled specially by JSONNumber, which has conversion methods for the following types:
 * int, long, BigInteger, float, double, and BigDecimal 
 */
public class JSONValue {
	public enum Type {
		STRING,
		NUMBER,
		BOOLEAN,
		NULL,
		OBJECT,
		ARRAY
	}

	// Singletons for values with static text
	public static final JSONValue TRUE_VALUE = new JSONValue(Type.BOOLEAN, Boolean.TRUE);
	public static final JSONValue FALSE_VALUE = new JSONValue(Type.BOOLEAN, Boolean.FALSE);
	public static final JSONValue NULL_VALUE = new JSONValue(Type.NULL, null);
	
	static final JSONValueException NOT_A_STRING  = new JSONValueException("Not a STRING value");
	static final JSONValueException NOT_A_NUMBER  = new JSONValueException("Not a NUMBER value");
	static final JSONValueException NOT_A_BOOLEAN = new JSONValueException("Not a BOOLEAN value");
	static final JSONValueException NOT_AN_OBJECT = new JSONValueException("Not a STRING value");
	static final JSONValueException NOT_AN_ARRAY  = new JSONValueException("Not an ARRAY value");
	
	private final Type type;
	private final Object instance;
	
	// ==== Constructors
	
	private JSONValue(
		final Type type,
		final Object instance
	) {
		this.type = type;
		this.instance = instance;
	}
	
	public static JSONValue of(final String value) {
		return new JSONValue(Type.STRING, value);
	}
	
	public static JSONValue of(final JSONNumber value) {
		return new JSONValue(Type.NUMBER, value);
	}
	
	public static JSONValue of(final boolean value) {
		return value ? TRUE_VALUE : FALSE_VALUE;
	}
    
    public static JSONValue of(final Boolean value) {
        return ((value != null) && value.booleanValue()) ? TRUE_VALUE : FALSE_VALUE;
    }
	
	public static JSONValue ofNull() {
		return NULL_VALUE;
	}
	
	public static JSONValue of(final Map<String, JSONValue> value) {
		return new JSONValue(Type.OBJECT, value != null ? value : new HashMap<>());
	}
	
	public static JSONValue of(final List<JSONValue> value) {
		return new JSONValue(Type.ARRAY, value != null ? value : new LinkedList<>());
	}
	
	// ==== Object
	
	@Override
	public int hashCode() {
		return Objects.hashCode(instance);
	}
	
	@Override
	public boolean equals(final Object o) {
		boolean equals = o == this;
		if ((! equals) && (o instanceof JSONValue)) {
			final JSONValue obj = (JSONValue)(o);
			equals = Objects.equals(instance, obj.instance);
		}
		
		return equals;
	}
	
	@Override
	public String toString() {
		return
			"JSONValue[type=" + type +
			",instance=" + instance +
			"]";
	}
	
	// ==== Tests
	
	public boolean isString() {
		return type == Type.STRING;
	}
	
	public boolean isNumber() {
		return type == Type.NUMBER;
	}
	
	public boolean isBoolean() {
		return type == Type.BOOLEAN;
	}
	
	public boolean isNull() {
		return type == Type.NULL;
	}
	
	public boolean isObject() {
		return type == Type.OBJECT;
	}
	
	public boolean isArray() {
		return type == Type.ARRAY;
	}
	
	// ==== Unwrappers
	
	// Return the value as a string, or die if it is not a String
	public String asString() throws JSONValueException {
		if (type != Type.STRING) {
			throw NOT_A_STRING;
		}
		
		return (String)(instance);
	}
	
	// Return the value as a number, or die if it is not a number
	public JSONNumber asNumber() throws JSONValueException {
		if (type != Type.NUMBER) {
			throw NOT_A_NUMBER;
		}
		
		return (JSONNumber)(instance);
	}
	
	// Return the value as a boolean, or die if it is not a boolean
	public boolean asBoolean() throws JSONValueException {
		if (type != Type.BOOLEAN) {
			throw NOT_A_BOOLEAN;
		}
		
		return ((Boolean)(instance)).booleanValue();
	}

	// Return the value as an object, or die if it is not an object
	public Map<String, JSONValue> asObject() throws JSONValueException {
		if (type != Type.OBJECT) {
			throw NOT_AN_OBJECT;
		}
		
		@SuppressWarnings("unchecked")
		final Map<String, JSONValue> m = (Map<String, JSONValue>)(instance);
		return m;
	}

	// Return the value as an array, or die if it is not an array.
	// Uses list instead of array, as lists are generally more convenient.
	public List<JSONValue> asArray() throws JSONValueException {
		if (type != Type.ARRAY) {
			throw NOT_AN_ARRAY;
		}
		
		@SuppressWarnings("unchecked")
		final List<JSONValue> l = (List<JSONValue>)(instance);
		return l;
	}
	
	// ==== Accessors
	
	public Type getType() {
	    return type;
	}
	
	public Object getInstance() {
	    return instance;
	}
}
