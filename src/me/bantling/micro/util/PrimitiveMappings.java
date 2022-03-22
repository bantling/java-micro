package me.bantling.micro.util;

import java.util.HashMap;
import java.util.Map;

/**
 * {@code PrimitiveMappings} is a mapping of primitive class types with their associated wrapper types and zero values.
 */
public class PrimitiveMappings {
    /**
     * Map primitives types to their wrapper types
     */
    public static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = Collections.map(
        new HashMap<Class<?>, Class<?>>()
    ).add(
        void.class,    Void.class,
        boolean.class, Boolean.class,
        byte.class,    Byte.class,
        char.class,    Character.class,
        short.class,   Short.class,
        int.class,     Integer.class,
        long.class,    Long.class,
        float.class,   Float.class,
        double.class,  Double.class
    ).toUnmodifiableMap();

    /**
     * Map wrappers types to their primitive types
     */
    public static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE = Collections.map(
        new HashMap<Class<?>, Class<?>>()
    ).
    add(
        Void.class,      void.class,
        Boolean.class,   boolean.class,
        Byte.class,      byte.class,
        Character.class, char.class,
        Short.class,     short.class,
        Integer.class,   int.class,
        Long.class,      long.class,
        Float.class,     float.class,
        Double.class,    double.class
    ).
    toUnmodifiableMap();
    
    /**
     * Zero Byte constant
     */
    public static final Byte ZERO_BYTE = Byte.valueOf((byte)(0));
    
    /**
     * Zero Character constant
     */
    public static final Character ZERO_CHARACTER = Character.valueOf('\0');

    /**
     * Zero Short constant
     */
    public static final Short ZERO_SHORT = Short.valueOf((short)(0));

    /**
     * Zero Integer constant
     */
    public static final Integer ZERO_INTEGER = Integer.valueOf(0);

    /**
     * Zero Long constant
     */
    public static final Long ZERO_LONG = Long.valueOf(0L);

    /**
     * Zero Float constant
     */
    public static final Float ZERO_FLOAT = Float.valueOf(0.0f);

    /**
     * Zero Double constant
     */
    public static final Double ZERO_DOUBLE = Double.valueOf(0.0);
    
    /**
     * Empty String constant
     */
    public static final String EMPTY_STRING = "";
    
    /**
     * Map primitives types to their zero values
     */
    public static final Map<Class<?>, Object> PRIMITIVE_TO_ZERO_VALUE = Collections.map(
        new HashMap<Class<?>, Object>()
    ).
    add(
        void.class,    null,
        boolean.class, Boolean.FALSE,
        byte.class,    ZERO_BYTE,
        char.class,    ZERO_CHARACTER,
        short.class,   ZERO_SHORT,
        int.class,     ZERO_INTEGER,
        long.class,    ZERO_LONG,
        float.class,   ZERO_FLOAT,
        double.class,  ZERO_DOUBLE
    ).
    toUnmodifiableMap();
    
    /**
     * Map primitives wrapper types to their zero values, and String to ""
     */
    public static final Map<Class<?>, Object> WRAPPER_TO_ZERO_VALUE = Collections.map(
        new HashMap<Class<?>, Object>()
    ).
    add(
        Void.class,      null,
        Boolean.class,   Boolean.FALSE,
        Byte.class,      ZERO_BYTE,
        Character.class, ZERO_CHARACTER,
        Short.class,     ZERO_SHORT,
        Integer.class,   ZERO_INTEGER,
        Long.class,      ZERO_LONG,
        Float.class,     ZERO_FLOAT,
        Double.class,    ZERO_DOUBLE,
        String.class,    EMPTY_STRING
    ).
    toUnmodifiableMap();

    /**
     * Map primitives and their wrapper types to their zero values, and String to ""
     */
    public static final Map<Class<?>, Object> PRIMITIVE_AND_WRAPPER_TO_ZERO_VALUE = Collections.map(
        new HashMap<Class<?>, Object>()
    ).
    addAll(PRIMITIVE_TO_ZERO_VALUE).
    addAll(WRAPPER_TO_ZERO_VALUE).
    toUnmodifiableMap();
    
    /**
     * Determine if the given object is a non-null instance of the given type.
     * If the type is primitive, the object must be the associated wrapper type.
     * 
     * @param value
     * @param type
     * @return true if value is a non-null instance of the appropriate type
     */
    public static boolean isInstance(
        final Object value,
        final Class<?> type
    ) {
        return (value != null) && (type.isPrimitive() ? PRIMITIVE_TO_WRAPPER.get(type) : type).isInstance(value);
    }
}
