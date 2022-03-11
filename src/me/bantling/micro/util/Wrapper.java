package me.bantling.micro.util;

import static me.bantling.micro.util.PrimitiveMappings.PRIMITIVE_AND_WRAPPER_TO_ZERO_VALUE;
import static me.bantling.micro.util.PrimitiveMappings.ZERO_BYTE;
import static me.bantling.micro.util.PrimitiveMappings.ZERO_CHARACTER;
import static me.bantling.micro.util.PrimitiveMappings.ZERO_DOUBLE;
import static me.bantling.micro.util.PrimitiveMappings.ZERO_FLOAT;
import static me.bantling.micro.util.PrimitiveMappings.ZERO_INTEGER;
import static me.bantling.micro.util.PrimitiveMappings.ZERO_LONG;
import static me.bantling.micro.util.PrimitiveMappings.ZERO_SHORT;
import static me.bantling.micro.util.Range.checkIntInByteRangeO;
import static me.bantling.micro.util.Range.checkIntInCharRangeO;
import static me.bantling.micro.util.Range.checkIntInShortRangeO;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A wrapper around a value. Wrapper differs from {@link Optional} in a number of ways:
 * - The value is mutable
 * - There are no primmitive specialization classes - primitives are handled by named constructors that store the
 *   associated wrapper
 * - The default empty value is an appropriately typed false, zero, "", or null value
 * - A different empty value can be provided
 * - The empty value cannot be changed
 * - The present value can be the same as the empty value but still be present: eg a String can have an empty value of
 *   "", yet be present as ""
 * - Can be constructed from a primitive or object class type instead of a value
 * - The {@link #get()} method does not throw if the value is empty, it returns the empty value
 * - The {@link #getOrDie()} method throws only if the value is empty
 * - Wrapper is an {@link EventRegistry.Delegate<T>}, that will notify any listeners added when the value is changed via
 *   {@link #set(Object)}. If a listener throws an exception, the value is not changed.
 * - Some transforms on the value are provided
 */
public class Wrapper<T> extends EventRegistry.Delegate<T> {
    static NullPointerException VALUE_IS_EMPTY = new NullPointerException("The value is empty");
    
    /**
     * Flag to mark the value as present or empty
     */
    private boolean present;
    
    /**
     * The value to return when present
     */
    private T presentValue;
    
    /**
     * The value to return when empty
     */
    private final T emptyValue;
    
    /**
     * Construct a wrapper 
     * 
     * @param present true if the value is present
     * @param presentValue the initial present value
     * @param emptyValue the empty value
     */
    Wrapper(
        final boolean present,
        final T presentValue,
        final T emptyValue
    ) {
        super();
        this.present = present;
        this.presentValue = presentValue;
        this.emptyValue = emptyValue;
    }
    
    /**
     * Construct an empty wrapper of a boolean, where the empty value is false
     * 
     * @return empty boolean wrapper
     */
    public static Wrapper<Boolean> ofEmptyBoolean() {
        return new Wrapper<>(false, Boolean.FALSE, Boolean.FALSE);
    }

    /**
     * Construct a present wrapper of the given boolean, where the empty value is false
     * 
     * @return present boolean wrapper
     */
    public static Wrapper<Boolean> ofBoolean(final boolean intialValue) {
        return new Wrapper<>(true, Boolean.valueOf(intialValue), Boolean.FALSE);
    }

    /**
     * Construct a present wrapper of the given boolean, with and chosen empty value
     * 
     * @return present boolean wrapper
     */
    public static Wrapper<Boolean> ofBoolean(final boolean intialValue, final boolean emptyValue) {
        return new Wrapper<>(true, Boolean.valueOf(intialValue), Boolean.valueOf(emptyValue));
    }
    
    /**
     * Construct an empty wrapper of a byte, where the empty value is 0
     * 
     * @return empty byte wrapper
     */
    public static Wrapper<Byte> ofEmptyByte() {
        return new Wrapper<>(false, ZERO_BYTE, ZERO_BYTE);
    }

    /**
     * Construct a present wrapper of the given byte, where the empty value is 0
     * 
     * @return present byte wrapper
     */
    public static Wrapper<Byte> ofByte(final int intialValue) {
        return new Wrapper<>(true, checkIntInByteRangeO(intialValue), ZERO_BYTE);
    }

    /**
     * Construct a present wrapper of the given byte, with and chosen empty value
     * 
     * @return present byte wrapper
     */
    public static Wrapper<Byte> ofByte(final int intialValue, final int emptyValue) {
        return new Wrapper<>(true, checkIntInByteRangeO(intialValue), checkIntInByteRangeO(emptyValue));
    }
    
    /**
     * Construct an empty wrapper of a short, where the empty value is 0
     * 
     * @return empty short wrapper
     */
    public static Wrapper<Short> ofEmptyShort() {
        return new Wrapper<>(false, ZERO_SHORT, ZERO_SHORT);
    }

    /**
     * Construct a present wrapper of the given short, where the empty value is 0
     * 
     * @return present short wrapper
     */
    public static Wrapper<Short> ofShort(final int intialValue) {
        return new Wrapper<>(true, checkIntInShortRangeO(intialValue), ZERO_SHORT);
    }

    /**
     * Construct a present wrapper of the given short, with and chosen empty value
     * 
     * @return present short wrapper
     */
    public static Wrapper<Short> ofShort(final int intialValue, final int emptyValue) {
        return new Wrapper<>(true, checkIntInShortRangeO(intialValue), checkIntInShortRangeO(emptyValue));
    }
    
    /**
     * Construct an empty wrapper of a character, where the empty value is 0
     * 
     * @return empty character wrapper
     */
    public static Wrapper<Character> ofEmptyChar() {
        return new Wrapper<>(false, ZERO_CHARACTER, ZERO_CHARACTER);
    }

    /**
     * Construct a present wrapper of the given character, where the empty value is 0
     * 
     * @return present character wrapper
     */
    public static Wrapper<Character> ofChar(final int intialValue) {
        return new Wrapper<>(true, checkIntInCharRangeO(intialValue), ZERO_CHARACTER);
    }

    /**
     * Construct a present wrapper of the given character, with and chosen empty value
     * 
     * @return present character wrapper
     */
    public static Wrapper<Character> ofChar(final int intialValue, final int emptyValue) {
        return new Wrapper<>(true, checkIntInCharRangeO(intialValue), checkIntInCharRangeO(emptyValue));
    }
    
    /**
     * Construct an empty wrapper of an int, where the empty value is 0
     * 
     * @return empty int wrapper
     */
    public static Wrapper<Integer> ofEmptyInt() {
        return new Wrapper<>(false, ZERO_INTEGER, ZERO_INTEGER);
    }

    /**
     * Construct a present wrapper of the given int, where the empty value is 0
     * 
     * @return present int wrapper
     */
    public static Wrapper<Integer> ofInt(final int intialValue) {
        return new Wrapper<>(true, Integer.valueOf(intialValue), ZERO_INTEGER);
    }

    /**
     * Construct a present wrapper of the given int, with and chosen empty value
     * 
     * @return present int wrapper
     */
    public static Wrapper<Integer> ofInt(final int intialValue, final int emptyValue) {
        return new Wrapper<>(true, Integer.valueOf(intialValue), Integer.valueOf(emptyValue));
    }
    
    /**
     * Construct an empty wrapper of an long, where the empty value is 0
     * 
     * @return empty long wrapper
     */
    public static Wrapper<Long> ofEmptyLong() {
        return new Wrapper<>(false, ZERO_LONG, ZERO_LONG);
    }

    /**
     * Construct a present wrapper of the given long, where the empty value is 0
     * 
     * @return present long wrapper
     */
    public static Wrapper<Long> ofLong(final long intialValue) {
        return new Wrapper<>(true, Long.valueOf(intialValue), ZERO_LONG);
    }

    /**
     * Construct a present wrapper of the given long, with and chosen empty value
     * 
     * @return present long wrapper
     */
    public static Wrapper<Long> ofLong(final long intialValue, final long emptyValue) {
        return new Wrapper<>(true, Long.valueOf(intialValue), Long.valueOf(emptyValue));
    }
    
    /**
     * Construct an empty wrapper of an float, where the empty value is 0
     * 
     * @return empty float wrapper
     */
    public static Wrapper<Float> ofEmptyFloat() {
        return new Wrapper<>(false, ZERO_FLOAT, ZERO_FLOAT);
    }

    /**
     * Construct a present wrapper of the given float, where the empty value is 0
     * 
     * @return present float wrapper
     */
    public static Wrapper<Float> ofFloat(final float intialValue) {
        return new Wrapper<>(true, Float.valueOf(intialValue), ZERO_FLOAT);
    }

    /**
     * Construct a present wrapper of the given float, with and chosen empty value
     * 
     * @return present float wrapper
     */
    public static Wrapper<Float> ofFloat(final float intialValue, final float emptyValue) {
        return new Wrapper<>(true, Float.valueOf(intialValue), Float.valueOf(emptyValue));
    }
    
    /**
     * Construct an empty wrapper of an double, where the empty value is 0
     * 
     * @return empty double wrapper
     */
    public static Wrapper<Double> ofEmptyDouble() {
        return new Wrapper<>(false, ZERO_DOUBLE, ZERO_DOUBLE);
    }

    /**
     * Construct a present wrapper of the given double, where the empty value is 0
     * 
     * @return present double wrapper
     */
    public static Wrapper<Double> ofDouble(final double intialValue) {
        return new Wrapper<>(true, Double.valueOf(intialValue), ZERO_DOUBLE);
    }

    /**
     * Construct a present wrapper of the given double, with and chosen empty value
     * 
     * @return present double wrapper
     */
    public static Wrapper<Double> ofDouble(final double intialValue, final double emptyValue) {
        return new Wrapper<>(true, Double.valueOf(intialValue), Double.valueOf(emptyValue));
    }
    
    /**
     * Construct an empty wrapper of a String, where the empty value is ""
     * 
     * @return empty String wrapper
     */
    public static Wrapper<String> ofEmptyString() {
        return new Wrapper<>(false, "", "");
    }

    /**
     * Construct a present wrapper of the given String, where the empty value is ""
     * 
     * @return present String wrapper
     */
    public static Wrapper<String> ofString(final String intialValue) {
        return new Wrapper<>(true, intialValue, "");
    }

    /**
     * Construct a present wrapper of the given String, with and chosen String value
     * 
     * @return present String wrapper
     */
    public static Wrapper<String> ofString(final String intialValue, final String emptyValue) {
        return new Wrapper<>(true, intialValue, emptyValue);
    }

    /**
     * Construct an empty Wrapper where empty value is null
     */
    public static <T> Wrapper<T> ofEmpty() {
        return new Wrapper<>(false, null, null);
    }

    /**
     * Construct an empty Wrapper where the empty value is the value passed
     * 
     * @param value initial empty value
     */
    public static <T> Wrapper<T> ofEmpty(final T value) {
        return new Wrapper<>(false, value, value);
    }

    /**
     * Construct a present Wrapper with present value given and empty value of null
     * 
     * @param presentValue
     */
    public static <T> Wrapper<T> of(final T presentValue) {
        return new Wrapper<>(true, presentValue, null);
    }

    /**
     * Construct a present Wrapper with given present and empty values
     * 
     * @param presentValue
     * @param emptyValue
     */
    public static <T> Wrapper<T> of(final T presentValue, final T emptyValue) {
        return new Wrapper<>(true, presentValue, emptyValue);
    }

    /**
     * Construct an empty Wrapper of given type where the empty value is false, zero, "", or null 
     * 
     * @param typ the type of value to make an empty wwrapper of
     */
    public static <T> Wrapper<T> ofEmptyType(final Class<T> typ) {
        @SuppressWarnings("unchecked")
        final T emptyValue = (T)(PRIMITIVE_AND_WRAPPER_TO_ZERO_VALUE.get(typ));
        return new Wrapper<>(false, emptyValue, emptyValue);
    }
    
    /**
     * Test if the wrapper is present
     * 
     * @return true if the value is present
     */
    public boolean isPresent() {
        return present;
    }
    
    /**
     * Test if the wrapper is empty
     * 
     * @return true if the value is empty
     */
    public boolean isEmpty() {
        return !present;
    }
    
    /**
     * Get the present value, which may be equal to the empty value
     * 
     * @return present or empty value
     */
    public T get() {
        return presentValue;
    }
    
    /**
     * Get the present value, or die if not present
     * 
     * @return present
     * @throws {@link #VALUE_IS_EMPTY}
     */
    public T getOrDie() {
        if (present) {
            return presentValue;
        }
        
        throw VALUE_IS_EMPTY;
    }
    
    /**
     * Return the empty value, which may or may not be the current value
     * 
     * @return empty value
     */
    public T getEmptyValue() {
        return emptyValue;
    }
    
    /**
     * Set the value. If any listeners of the changed value throw an exception, then the value is not changed and the
     * exception propagates.
     * 
     * @param value to set
     */
    public void set(final T value) {
        this.eventRegistry.sendEvent(value);
        this.present = true;
        this.presentValue = value;
    }
    
    /**
     * Mark the wrapper as empty
     */
    public void empty() {
        present = false;
        // Set present value to empty value so that get() returns empty value
        presentValue = emptyValue;
    }
    
    // ==== Transforms
    
    /**
     * Map to a new wrapper of the same type.
     * If this value is empty, the wrapper is empty, otherwise the mapper function is invoked to generate it.
     * The mapper may return null, causing the new wrapper to be empty.
     * The new wrapper has the same empty value as this wrapper.
     * 
     * @param mapper the mapping function
     * @return new wrapper of mapped or empty value
     */
    public Wrapper<T> map(final Function<T, T> mapper) {
        return new Wrapper<>(present,  present ? mapper.apply(presentValue) : emptyValue, emptyValue);
    }

    /**
     * Map to a wrapper of another type.
     * If this value is empty, the wrapper is empty, otherwise the mapper function is invoked to generate it.
     * The mapper may return null, causing the new wrapper to be empty.
     * The new wrapper has a null empty value.
     * 
     * @param <U> type to map to
     * @param mapper the mapping function
     * @return new wrapper of mapped or empty value
     */
    public <U> Wrapper<U> mapTo(final Function<T, U> mapper) {
        return new Wrapper<>(present, present ? mapper.apply(presentValue) : null, null);
    }
    
    /**
     * Map to a wrapper of another type.
     * If this value is empty, the wrapper is empty, otherwise the mapper function is invoked to generate it.
     * The mapper may return null, causing the new wrapper to be empty.
     * The new wrapper has the provided empty value.
     * 
     * @param <U> type to map to
     * @param mapper the mapping function
     * @param emptyUValue the empty value of the mapped type
     * @return new wrapper of mapped or empty value
     */
    public <U> Wrapper<U> mapTo(final Function<T, U> mapper, final U emptyUValue) {
        return new Wrapper<>(present, present ? mapper.apply(presentValue) : emptyUValue, emptyUValue);
    }
    
    /**
     * Hash the present flag and wrapped value
     * 
     * @throws IllegalStateException if the type has not been set
     */
    @Override
    public int hashCode() {
        return (31 * (present ? Objects.hashCode(presentValue) : 0)) + Boolean.hashCode(present);
    }
    
    /**
     * Compare this Wrapper against another Wrapper for equality, based on two criteria:
     * - The present flags match
     * - If present, the values match
     * 
     * @return true if o is an equal Wrapper 
     * @throws IllegalStateException if the type has not been set
     */
    @Override
    public boolean equals(final Object o) {
        boolean equals = o == this;
        if ((! equals) && (o instanceof Wrapper)) {
            final Wrapper<?> obj = (Wrapper<?>)(o);
            equals = (present == obj.present) &&
                     ((!present) || Objects.equals(presentValue, obj.presentValue));
        }
        
        return equals;
    }
    
    /**
     * Convert this wrapper to a string
     * 
     * @return "Wrapper[Objects.toString(value)]"
     * @throws IllegalStateException if the type has not been set
     */
    @Override
    public String toString() {
        return Wrapper.class.getSimpleName() + '[' + Objects.toString(presentValue) + ']';
    }
}
