package me.bantling.micro.util;

import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

/**
 * {@code Range} provides range checks for int, long, and double types in the form of java.util.function predicate types.
 */
public class Range {
    // Can't construct
    private Range() {
        throw new RuntimeException();
    }
    
    /**
     * {@code OUT_OF_RANGE_FMT} is Formatting string for out of range errors
     */
    static String OUT_OF_RANGE_FMT = "%s is an invalid %s value, it must be in the range [%s, %s]";

    /**
     * Check that an int value is in the range [min, max]
     * 
     * @param value the value to check
     * @param min the minimum value allowed
     * @param max the maximum value allowed
     * @return the value that passed the range check
     */
    public static int checkIntRangeP(final int value, final int min, final int max) {
        if ((value < min) || (value > max)) {
            throw new IllegalArgumentException(String.format(
                OUT_OF_RANGE_FMT,
                Integer.valueOf(value),
                Integer.valueOf(min),
                Integer.valueOf(max)
            ));
        }
        
        return value;
    }

    /**
     * Check that an Integer value is in the range [min, max]
     * 
     * @param value the value to check
     * @param min the minimum value allowed
     * @param max the maximum value allowed
     */
    public static Integer checkIntRangeO(final Integer value, final int min, final int max) {
        return Integer.valueOf(checkIntRangeP(value.intValue(), min, max));
    }
    
    /**
     * Validate that an int is in the range of [Byte.MIN_VALUE, Byte.MAX_VALUE].
     * This allows a caller to provide an int where a byte is needed without casting the int to a byte.
     * 
     * @param value a value appropriate for a byte
     * @return value cast to a byte
     */
    public static byte checkIntInByteRangeP(final int value) {
        return (byte)(checkIntRangeP(value, Byte.MIN_VALUE, Byte.MAX_VALUE));
    }
    
    /**
     * Validate that an int is in the range of [Byte.MIN_VALUE, Byte.MAX_VALUE].
     * This allows a caller to provide an int where a byte is needed without casting the int to a byte.
     * 
     * @param value a value appropriate for a byte
     * @return value wrapped in Byte
     */
    public static Byte checkIntInByteRangeO(final int value) {
        return Byte.valueOf((byte)(checkIntRangeP(value, Byte.MIN_VALUE, Byte.MAX_VALUE)));
    }
    
    /**
     * Validate that an int is in the range of [Short.MIN_VALUE, Short.MAX_VALUE].
     * This allows a caller to provide an int where a short is needed without casting the int to a short.
     * 
     * @param value a value appropriate for a short
     * @return value cast to a short
     */
    public static short checkIntInShortRangeP(final int value) {
        return (short)(checkIntRangeP(value, Short.MIN_VALUE, Short.MAX_VALUE));
    }
    
    /**
     * Validate that an int is in the range of [Short.MIN_VALUE, Short.MAX_VALUE].
     * This allows a caller to provide an int where a short is needed without casting the int to a short.
     * 
     * @param value a value appropriate for a short
     * @return value wrapped in Short
     */
    public static Short checkIntInShortRangeO(final int value) {
        return Short.valueOf((short)(checkIntRangeP(value, Short.MIN_VALUE, Short.MAX_VALUE)));
    }
    
    /**
     * Validate that an int is in the range of [Character.MIN_VALUE, Character.MAX_VALUE].
     * This allows a caller to provide an int where a char is needed without casting the int to a char.
     * 
     * @param value a value appropriate for a char
     * @return value cast to a char
     */
    public static char checkIntInCharRangeP(final int value) {
        return (char)(checkIntRangeP(value, Character.MIN_VALUE, Character.MAX_VALUE));
    }
    
    /**
     * Validate that an int is in the range of [Character.MIN_VALUE, Character.MAX_VALUE].
     * This allows a caller to provide an int where a char is needed without casting the int to a char.
     * 
     * @param value a value appropriate for a char
     * @return value wrapped in Character
     */
    public static Character checkIntInCharRangeO(final int value) {
        return Character.valueOf((char)(checkIntRangeP(value, Character.MIN_VALUE, Character.MAX_VALUE)));
    }

    /**
     * Check that a long value is in the range [min, max]
     * 
     * @param value the value to check
     * @param min the minimum value allowed
     * @param max the maximum value allowed
     */
    public static long checkLongRangeP(final long value, final long min, final long max) {
        if ((value < min) || (value > max)) {
            throw new IllegalArgumentException(String.format(
                OUT_OF_RANGE_FMT,
                Long.valueOf(value),
                Long.valueOf(min),
                Long.valueOf(max)
            ));
        }
        
        return value;
    }

    /**
     * Check that a Long value is in the range [min, max]
     * 
     * @param value the value to check
     * @param min the minimum value allowed
     * @param max the maximum value allowed
     */
    public static Long checkLongRangeO(final Long value, final long min, final long max) {
        return Long.valueOf(checkLongRangeP(value.longValue(), min, max));
    }

    /**
     * Check that a float value is in the range [min - delta, max - delta]
     * 
     * @param value the value to check
     * @param min the minimum value allowed
     * @param max the maximum value allowed
     */
    public static float checkFloatRangeP(final float value, final float min, final float max, final float delta) {
        final float d = Math.abs(delta);
        if ((value < min - d) || (value > max + d)) {
            throw new IllegalArgumentException(String.format(
                OUT_OF_RANGE_FMT,
                Float.valueOf(value),
                Float.valueOf(min),
                Float.valueOf(max)
            ));
        }
        
        return value;
    }

    /**
     * Check that a Float value is in the range [min - delta, max - delta]
     * 
     * @param value the value to check
     * @param min the minimum value allowed
     * @param max the maximum value allowed
     */
    public static Float checkFloatRangeO(final Float value, final float min, final float max, final float delta) {
        return Float.valueOf(checkFloatRangeP(value.floatValue(), min, max, delta));
    }

    /**
     * Check that a double value is in the range [min - delta, max - delta]
     * 
     * @param value the value to check
     * @param min the minimum value allowed
     * @param max the maximum value allowed
     */
    public static double checkDoubleRangeP(final double value, final double min, final double max, final double delta) {
        final double d = Math.abs(delta);
        if ((value < min - d) || (value > max + d)) {
            throw new IllegalArgumentException(String.format(
                OUT_OF_RANGE_FMT,
                Double.valueOf(value),
                Double.valueOf(min),
                Double.valueOf(max)
            ));
        }
        
        return value;
    }

    /**
     * Check that a Double value is in the range [min - delta, max - delta]
     * 
     * @param value the value to check
     * @param min the minimum value allowed
     * @param max the maximum value allowed
     */
    public static Double checkDoubleRangeO(final Double value, final double min, final double max, final double delta) {
        return Double.valueOf(checkDoubleRangeP(value.doubleValue(), min, max, delta));
    }
    
    /**
     * {@code IntRange} is the base class for checking an int is within a range of [min, max]
     * 
     * Note: it is impossible to implement both IntPredicate and Predicate, as the two interfaces both have a negate()
     * method of no arguments that return a new predicate. This results in two methods that differ only by return type.
     */
    static class IntRange {
        /**
         * {@code} min is the minimum value
         */
        private final int min;

        /**
         * {@code} max is the maximum value
         */
        private final int max;
        
        /**
         * Construct an IntRange given min and max values
         * 
         * @param min minimum value
         * @param max maximum value
         */
        private IntRange(final int min, final int max) {
            this.min = min;
            this.max = max;
        }
        
        /**
         * Test the given int
         * 
         * @param value to test 
         * @return true if value in [min, max]
         */
        boolean checkRange(int value) {
            return (value >= min) && (value <= max);
        }
    }
    
    /**
     * {@code IntPRange} is an {@link IntPredicate} that tests that an int is in a closed range of [min, max]
     */
    public static class IntRangeP extends IntRange implements IntPredicate {
        /**
         * Construct an IntRange given min and max values
         * 
         * @param min minimum value
         * @param max maximum value
         */
        private IntRangeP(final int min, final int max) {
            super(min, max);
        }
        
        /**
         * Test the given int
         * 
         * @param value to test 
         * @return true if value in [min, max]
         */
        @Override
        public boolean test(int value) {
            return checkRange(value);
        }
    }
    
    /**
     * {@code IntORange} is an {@link Predicate} that tests that an int is in a closed range of [min, max]
     */
    public static class IntRangeO extends IntRange implements Predicate<Integer> {
        /**
         * Construct an IntRange given min and max values
         * 
         * @param min minimum value
         * @param max maximum value
         */
        private IntRangeO(final int min, final int max) {
            super(min, max);
        }
        
        /**
         * Test the given int
         * 
         * @param value to test 
         * @return true if value in [min, max]
         */
        @Override
        public boolean test(final Integer value) {
            return checkRange(value.intValue());
        }
    }

    /**
     * Return an {@link #IntRangeP} given min and max values
     * 
     * @param min minimum value
     * @param max maximum value
     */
    public static IntRangeP intRangeCheckP(final int min, final int max) {
        return new IntRangeP(min, max);
    }

    /**
     * Return an {@link #IntRangeO} given min and max values
     * 
     * @param min minimum value
     * @param max maximum value
     */
    public static IntRangeO intRangeCheckO(final int min, final int max) {
        return new IntRangeO(min, max);
    }
    
    /**
     * {@code LongRange} is the base class for checking a long is within a range of [min, max]
     * 
     * Note: it is impossible to implement both LongPredicate and Predicate, as the two interfaces both have a negate()
     * method of no arguments that return a new predicate. This results in two methods that differ only by return type.
     */
    static class LongRange {
        /**
         * {@code} min is the minimum value
         */
        private final long min;

        /**
         * {@code} max is the maximum value
         */
        private final long max;
        
        /**
         * Construct an LongRange given min and max values
         * 
         * @param min minimum value
         * @param max maximum value
         */
        private LongRange(final long min, final long max) {
            this.min = min;
            this.max = max;
        }
        
        /**
         * Test the given long
         * 
         * @param value to test 
         * @return true if value in [min, max]
         */
        boolean checkRange(long value) {
            return (value >= min) && (value <= max);
        }
    }
    
    /**
     * {@code LongPRange} is a {@link LongPredicate} that tests that a long is in a closed range of [min, max]
     */
    public static class LongRangeP extends LongRange implements LongPredicate {
        /**
         * Construct a LongRange given min and max values
         * 
         * @param min minimum value
         * @param max maximum value
         */
        private LongRangeP(final long min, final long max) {
            super(min, max);
        }

        /**
         * Test the given long
         * 
         * @param value to test 
         * @return true if value in [min, max]
         */
        @Override
        public boolean test(long value) {
            return checkRange(value);
        }
    }
    
    /**
     * {@code LongORange} is a {@link Predicate} that tests that a long is in a closed range of [min, max]
     */
    public static class LongRangeO extends LongRange implements Predicate<Long> {
        /**
         * Construct a LongRange given min and max values
         * 
         * @param min minimum value
         * @param max maximum value
         */
        private LongRangeO(final long min, final long max) {
            super(min, max);
        }

        /**
         * Test the given long
         * 
         * @param value to test 
         * @return true if value in [min, max]
         */
        @Override
        public boolean test(Long value) {
            return checkRange(value.longValue());
        }
    }

    /**
     * Return a {@link #LongRangeP} given min and max values
     * 
     * @param min minimum value
     * @param max maximum value
     */
    public static LongRangeP longRangeCheckP(final long min, final long max) {
        return new LongRangeP(min, max);
    }

    /**
     * Return a {@link #LongRangeO} given min and max values
     * 
     * @param min minimum value
     * @param max maximum value
     */
    public static LongRangeO longRangeCheckO(final long min, final long max) {
        return new LongRangeO(min, max);
    }

    /**
     * {@code DoubleRange} is the base class for checking a double is within a range of [min - delta, max + delta]
     * 
     * Note: it is impossible to implement both DoublePredicate and Predicate, as the two interfaces both have a negate()
     * method of no arguments that return a new predicate. This results in two methods that differ only by return type.
     */
    static class DoubleRange {
        /**
         * {@code} min is the minimum value
         */
        private final double min;

        /**
         * {@code} max is the maximum value
         */
        private final double max;
        /**
         * Construct a DoubleRange given min, max, and epsilon values
         * 
         * @param min minimum value
         * @param max maximum value
         * @param delta delta value
         */
        private DoubleRange(final double min, final double max, final double delta) {
            final double d = Math.abs(delta);
            this.min = min - d;
            this.max = max - d;
        }
        
        boolean checkRange(double value) {
            return (value >= min) && (value <= max);
        }
    }
    
    /**
     * {@code DoubleRangeP} is a {@link DoublePredicate} that tests that a double is in a closed range of [min - delta, max + delta]
     */
    public static class DoubleRangeP extends DoubleRange implements DoublePredicate {
        /**
         * Construct a DoubleRange given min, max, and epsilon values
         * 
         * @param min minimum value
         * @param max maximum value
         * @param delta delta value
         */
        private DoubleRangeP(final double min, final double max, final double delta) {
            super(min, max, delta);
        }

        /**
         * Test the given double
         * 
         * @param value to test 
         * @return true if value in [min, max]
         */
        @Override
        public boolean test(double value) {
            return checkRange(value);
        }
    }
    
    /**
     * {@code DoubleRangeO} is a {@link Predicate} that tests that a double is in a closed range of [min - delta, max + delta]
     */
    public static class DoubleRangeO extends DoubleRange implements Predicate<Double> {
        /**
         * Construct a DoubleORange given min and max values
         * 
         * @param min minimum value
         * @param max maximum value
         */
        private DoubleRangeO(final double min, final double max, final double delta) {
            super(min, max, delta);
        }

        /**
         * Test the given double
         * 
         * @param value to test 
         * @return true if value in [min, max]
         */
        @Override
        public boolean test(final Double value) {
            return checkRange(value.doubleValue());
        }
    }

    /**
     * Return a {@link #DoubleRangeP} given min and max values
     * 
     * @param min minimum value
     * @param max maximum value
     * @param delta delta value
     */
    public static DoubleRangeP doubleRangeCheckP(final double min, final double max, final double delta) {
        return new DoubleRangeP(min, max, delta);
    }

    /**
     * Return a {@link #DoubleRangeO} given min and max values
     * 
     * @param min minimum value
     * @param max maximum value
     */
    public static DoubleRangeO doubleRangeCheckO(final double min, final double max, final double delta) {
        return new DoubleRangeO(min, max, delta);
    }
}
