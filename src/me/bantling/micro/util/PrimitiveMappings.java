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
    public static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = Collections.unmodifiableMapOf(
        Tuple.of(void.class,    Void.class),
        Tuple.of(boolean.class, Boolean.class),
        Tuple.of(byte.class,    Byte.class),
        Tuple.of(char.class,    Character.class),
        Tuple.of(short.class,   Short.class),
        Tuple.of(int.class,     Integer.class),
        Tuple.of(long.class,    Long.class),
        Tuple.of(float.class,   Float.class),
        Tuple.of(double.class,  Double.class)
    );

    /**
     * Map wrappers types to their primitive types
     */
    public static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE = Collections.unmodifiableMapOf(
        Tuple.of(Void.class,      void.class),
        Tuple.of(Boolean.class,   boolean.class),
        Tuple.of(Byte.class,      byte.class),
        Tuple.of(Character.class, char.class),
        Tuple.of(Short.class,     short.class),
        Tuple.of(Integer.class,   int.class),
        Tuple.of(Long.class,      long.class),
        Tuple.of(Float.class,     float.class),
        Tuple.of(Double.class,    double.class)
    );
    
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
    public static final Map<Class<?>, Object> PRIMITIVE_TO_ZERO_VALUE = Collections.unmodifiableMapOf(
        Tuple.of(void.class,    null),
        Tuple.of(boolean.class, Boolean.FALSE),
        Tuple.of(byte.class,    ZERO_BYTE),
        Tuple.of(char.class,    ZERO_CHARACTER),
        Tuple.of(short.class,   ZERO_SHORT),
        Tuple.of(int.class,     ZERO_INTEGER),
        Tuple.of(long.class,    ZERO_LONG),
        Tuple.of(float.class,   ZERO_FLOAT),
        Tuple.of(double.class,  ZERO_DOUBLE)
    );
    
    /**
     * Map primitives wrapper types to their zero values, and String to ""
     */
    public static final Map<Class<?>, Object> WRAPPER_TO_ZERO_VALUE = Collections.unmodifiableMapOf(
        Tuple.of(Void.class,      null),
        Tuple.of(Boolean.class,   Boolean.FALSE),
        Tuple.of(Byte.class,      ZERO_BYTE),
        Tuple.of(Character.class, ZERO_CHARACTER),
        Tuple.of(Short.class,     ZERO_SHORT),
        Tuple.of(Integer.class,   ZERO_INTEGER),
        Tuple.of(Long.class,      ZERO_LONG),
        Tuple.of(Float.class,     ZERO_FLOAT),
        Tuple.of(Double.class,    ZERO_DOUBLE),
        Tuple.of(String.class,    EMPTY_STRING)
    );

    /**
     * Map primitives and their wrapper types to their zero values, and String to ""
     */
    public static final Map<Class<?>, Object> PRIMITIVE_AND_WRAPPER_TO_ZERO_VALUE = Collections.unmodifiableMapOf(
        new HashMap<Class<?>, Object>(),
        PRIMITIVE_TO_ZERO_VALUE,
        WRAPPER_TO_ZERO_VALUE
    );
    
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
