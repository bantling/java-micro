package me.bantling.micro.util;

import java.util.Arrays;
import java.util.Objects;

/**
 * Create a tuple of 0 to 6 immutable values that can be different types.
 * Designed for the following patterns:
 * 
 * 1. Use one specific tuple size of different types
 * - Declare type as Tuple.{Two,Three,Four,Five,Six} using a separate generic type for each value
 * - Call appropriate Tuple.{of,ofNullable} overload that accepts the same number of arguments from 2 to 6
 * 
 * EG:
 * Tuple.Two<String, Integer> Foo() {
 *   ...
 *   return Tuple.of("foo", 2);
 *   OR
 *   return Tuple.ofNullable("foo", null); 
 * }
 * 
 * 2. Use one specific tuple size of the same type
 * - Declare type as Tuple.{Two,Three,Four,Five,Six}Of<T> with a single generic value
 * - Call appropriate Tuple.ofSame{Nullable?}() overload that accepts N args
 * 
 * EG:
 * Tuple.TwoOf<String> Foo() {
 *   ...
 *   return Tuple.ofSame("foo", "bar")
 *   OR
 *   return Tuple.ofSameNullable(null, "bar")
 * }
 * 
 * 3. Create a List or Set of tuples of a specific size, with different or same types
 * - Declare type as:
 *   {List,Set}<Tuple.{Two,Three,Four,Five,Six}<T,U,...>>
 *   OR
 *   {List,Set}<Tuple.{Two,Three,Four,Five,Six}Of<T>>
 * - Use Collections.{listOf,setOf}(Tuple.of{Nullable?}(N args)...)
 *    OR Collections.{listOf,setOf)(Tuple.ofSame{Nullable?}(N args)...)
 * 
 * EG:
 * List<Tuple.Two<String, Integer>> list = Collections.listOf(
 *   Tuple.of("foo", 2),
 *   Tuple.ofNullable("foo", null),
 *   ...
 * );
 * 
 * OR
 * 
 * List<Tuple.TwoOf<String>> list = Collections.listOf(
 *   Tuple.ofSame("foo", "bar"),
 *   Tuple.ofSameNullable("foo", null),
 *   ...
 * )
 * 
 * 4. Create a List or Set of up to a max number of tuples
 * - Declare type as
 *   Tuple.UpTo{Two,Three,Four,Five,Six}<T,U,...>
 *   OR
 *   Tuple.UpTo{Two,Three,Four,Five,Six}Of<T>
 * - Use Collections.{listOf,setOf}(Tuple.upTo{Two,Three,Four,Five,Six}{Of?}{Nullable?}(0..N values))
 * - Test number of values in a particular tuple with count() method
 * - NOTE: there are no               upToN{Of?}Nullable() methods of no args,
 *         since they are the same as upToN{Of?}()         methods of no args 
 * 
 * EG:
 * List<Tuple.UpToThree<String, Integer, Double>> list = Collections.listOf(
 *   Tuple.upToThree(),
 *   Tuple.upToThree("foo"),
 *   Tuple.upToThree("bar", 1),
 *   Tuple.upToThree("bar", 1, 3.25),
 *   Tuple.upToThreeNullable(null),
 *   Tuple.upToThreeNullable("baz", null),
 *   Tuple.upToThreeNullable("baz", 1, null),
 *   ...
 * );
 * list.get(0).count() => 0
 * list.get(1).count() => 1
 * list.get(2).count() => 2
 * list.get(3).count() => 3
 * list.get(4).count() => 1
 * list.get(5).count() => 2
 * list.get(6).count() => 3
 * 
 * OR
 * 
 * List<Tuple.UpToThreeOf<Integer>> list = Collections.listOf(
 *   Tuple.upToThreeOf(),
 *   Tuple.upToThreeOf(1),
 *   Tuple.upToThreeOf(1, 2),
 *   Tuple.upToThreeOf(1, 2, 3),
 *   Tuple.upToThreeOfNullable(null),
 *   Tuple.upToThreeOfNullable(1, null),
 *   Tuple.upToThreeOfNullable(1, 2, null),
 *   ...
 * );
 * list.get(0).count() => 0
 * list.get(1).count() => 1
 * list.get(2).count() => 2
 * list.get(3).count() => 3
 * list.get(4).count() => 1
 * list.get(5).count() => 2
 * list.get(6).count() => 3
 */
public class Tuple {
    private Tuple() {
        throw new RuntimeException();
    }
    
    // ==== Base Class
    
    /**
     * Base provides the functionality to back all the use cases
     * 
     * @param <T> first value type
     * @param <U> second value type
     * @param <V> third value type
     * @param <W> fourth value type
     * @param <X> fifth value type
     * @param <Y> sixth value type
     */
    static class Base<T, U, V, W, X, Y> {
        /**
         * the number of fields actually used
         */
        final int count;
        
        /**
         * The first value
         */
        final T t;
        
        /**
         * The second value
         */
        final U u;
        
        /**
         * The third value
         */
        final V v;
        
        /**
         * The fourth value
         */
        final W w;
        
        /**
         * The fifth value
         */
        final X x;
        
        /**
         * The sixth value
         */
        final Y y;
        
        /**
         * Construct with the given values
         * 
         * @param count the number of values actually used 
         * @param t the first value
         * @param u the second value
         * @param v the third value
         * @param w the fourth value
         * @param x the fifth value
         * @param y the sixth value
         */
        Base(
            final int count,
            final T t,
            final U u,
            final V v,
            final W w,
            final X x,
            final Y y
        ) { 
            this.count = count;
            this.t = t;
            this.u = u;
            this.v = v;
            this.w = w;
            this.x = x;
            this.y = y;
        }
        
        /**
         * Hash all the provided values in a fashion compatible with {@link Arrays#hashCode(Object[])}.
         * If the instance is an UpTo{Two,Three,Four,Five,Six}{Same?} object,
         * the number of provided values may be less than the generic signature. 
         */
        @Override
        public int hashCode() {
            int hashCode = 1;
            
            if (count >= 1) {
                hashCode = 31 * hashCode + Objects.hashCode(t);
            }
            
            if (count >= 2) {
                hashCode = 31 * hashCode + Objects.hashCode(u);
            }
            
            if (count >= 3) {
                hashCode = 31 * hashCode + Objects.hashCode(v);
            }
            
            if (count >= 4) {
                hashCode = 31 * hashCode + Objects.hashCode(w);
            }
            
            if (count >= 5) {
                hashCode = 31 * hashCode + Objects.hashCode(x);
            }
            
            if (count >= 6) {
                hashCode = 31 * hashCode + Objects.hashCode(y);
            }
            
            return hashCode;
        }
        
        /**
         * Two Tuples are equal if they have the same count, and the first count values are equal.
         * They do not have be the same subclass type. 
         */
        @Override
        public boolean equals(final Object o) {
            boolean equals = o == this;
            if ((! equals) && (o instanceof Base)) {
                @SuppressWarnings("unchecked")
                final Base<T, U, V, W, X, Y> obj = (Base<T, U, V, W, X, Y>)(o);
                equals =
                    (count == obj.count) &&
                    ((count < 1) || Objects.equals(t, obj.t)) &&
                    ((count < 2) || Objects.equals(u, obj.u)) &&
                    ((count < 3) || Objects.equals(v, obj.v)) &&
                    ((count < 4) || Objects.equals(w, obj.w)) &&
                    ((count < 5) || Objects.equals(x, obj.x)) &&
                    ((count < 6) || Objects.equals(y, obj.y));
            }
            
            return equals;
        }
        
        /**
         * The string is of the form (first, second, ...), where only the first count values are included
         */
        @Override
        public String toString() {
            return
                "(" +
                ((count < 1) ? "" :       String.valueOf(t)) +
                ((count < 2) ? "" : "," + String.valueOf(u)) +
                ((count < 3) ? "" : "," + String.valueOf(v)) +
                ((count < 4) ? "" : "," + String.valueOf(w)) +
                ((count < 5) ? "" : "," + String.valueOf(x)) +
                ((count < 6) ? "" : "," + String.valueOf(y)) + 
                ")";
        }
    }
    
    // ==== Two classes
    
    /**
     * Two values of different types
     * 
     * @param <T> first type
     * @param <U> second type
     */
    public static class Two<T, U> extends Base<T, U, Void, Void, Void, Void> {
        Two(
            final T t,
            final U u
        ) {
            super(2, t, u, null, null, null, null);
        }

        /**
         * get first value
         * 
         * @return first value
         */
        public T get1() {
            return t;
        }

        /**
         * get second value
         * 
         * @return second value
         */
        public U get2() {
            return u;
        }
    }

    /**
     * Two values of the same type
     * 
     * @param <T> value type
     */
    public static class TwoOf<T> extends Two<T, T> {
        TwoOf(
            final T t,
            final T u
        ) {
            super(t, u);
        }
    }

    /**
     * Up to two values of different types
     * 
     * @param <T> first type
     * @param <U> second type
     */
    public static class UpToTwo<T, U> extends Base<T, U, Void, Void, Void, Void> {
        UpToTwo(
            final int count,
            final T t,
            final U u
        ) {
            super(count, t, u, null, null, null, null);
        }
        
        /**
         * get the count
         * 
         * @return the count
         */
        public int getCount() {
            return count;
        }

        /**
         * get first value
         * 
         * @return first value
         */
        public T get1() {
            return t;
        }

        /**
         * get second value
         * 
         * @return second value
         */
        public U get2() {
            return u;
        }
    }

    /**
     * Up to two values of the same type
     * 
     * @param <T> value type
     */
    public static class UpToTwoOf<T> extends UpToTwo<T, T> {
        UpToTwoOf(
            final int count,
            final T t,
            final T u
        ) {
            super(count, t, u);
        }
    }
    
    // ==== Three classes

    /**
     * Three values of different types
     * 
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     */
    public static class Three<T, U, V> extends Base<T, U, V, Void, Void, Void> {
        Three(
            final T t,
            final U u,
            final V v
        ) {
            super(3, t, u, v, null, null, null);
        }

        /**
         * get first value
         * 
         * @return first value
         */
        public T get1() {
            return t;
        }

        /**
         * get second value
         * 
         * @return second value
         */
        public U get2() {
            return u;
        }

        /**
         * get third value
         * 
         * @return third value
         */
        public V get3() {
            return v;
        }
    }

    /**
     * Three values of the same type
     * 
     * @param <T> value type
     */
    public static class ThreeOf<T> extends Three<T, T, T> {
        ThreeOf(
            final T t,
            final T u,
            final T v
        ) {
            super(t, u, v);
        }
    }

    /**
     * Up to three values of different types
     * 
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     */
    public static class UpToThree<T, U, V> extends Base<T, U, V, Void, Void, Void> {
        UpToThree(
            final int count,
            final T t,
            final U u,
            final V v
        ) {
            super(count, t, u, v, null, null, null);
        }
        
        /**
         * get the count
         * 
         * @return the count
         */
        public int getCount() {
            return count;
        }

        /**
         * get first value
         * 
         * @return first value
         */
        public T get1() {
            return t;
        }

        /**
         * get second value
         * 
         * @return second value
         */
        public U get2() {
            return u;
        }

        /**
         * get third value
         * 
         * @return third value
         */
        public V get3() {
            return v;
        }
    }

    /**
     * Up to three values of the same type
     * 
     * @param <T> value type
     */
    public static class UpToThreeOf<T> extends UpToThree<T, T, T> {
        UpToThreeOf(
            final int count,
            final T t,
            final T u,
            final T v
        ) {
            super(count, t, u, v);
        }
    }
    
    // ==== Four classes

    /**
     * Four values of different types
     * 
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     */
    public static class Four<T, U, V, W> extends Base<T, U, V, W, Void, Void> {
        Four(
            final T t,
            final U u,
            final V v,
            final W w
        ) {
            super(4, t, u, v, w, null, null);
        }

        /**
         * get first value
         * 
         * @return first value
         */
        public T get1() {
            return t;
        }

        /**
         * get second value
         * 
         * @return second value
         */
        public U get2() {
            return u;
        }

        /**
         * get third value
         * 
         * @return third value
         */
        public V get3() {
            return v;
        }

        /**
         * get fourth value
         * 
         * @return fourth value
         */
        public W get4() {
            return w;
        }
    }

    /**
     * Four values of the same type
     * 
     * @param <T> value type
     */
    public static class FourOf<T> extends Four<T, T, T, T> {
        FourOf(
            final T t,
            final T u,
            final T v,
            final T w
        ) {
            super(t, u, v, w);
        }
    }

    /**
     * Up to four values of different types
     * 
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     */
    public static class UpToFour<T, U, V, W> extends Base<T, U, V, W, Void, Void> {
        UpToFour(
            final int count,
            final T t,
            final U u,
            final V v,
            final W w
        ) {
            super(count, t, u, v, w, null, null);
        }
        
        /**
         * get the count
         * 
         * @return the count
         */
        public int getCount() {
            return count;
        }

        /**
         * get first value
         * 
         * @return first value
         */
        public T get1() {
            return t;
        }

        /**
         * get second value
         * 
         * @return second value
         */
        public U get2() {
            return u;
        }

        /**
         * get third value
         * 
         * @return third value
         */
        public V get3() {
            return v;
        }

        /**
         * get fourth value
         * 
         * @return fourth value
         */
        public W get4() {
            return w;
        }
    }

    /**
     * Up to four values of the same type
     * 
     * @param <T> value type
     */
    public static class UpToFourOf<T> extends UpToFour<T, T, T, T> {
        UpToFourOf(
            final int count,
            final T t,
            final T u,
            final T v,
            final T w
        ) {
            super(count, t, u, v, w);
        }
    }
    
    // ==== Five classes

    /**
     * Five values of different types
     * 
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     */
    public static class Five<T, U, V, W, X> extends Base<T, U, V, W, X, Void> {
        Five(
            final T t,
            final U u,
            final V v,
            final W w,
            final X x
        ) {
            super(5, t, u, v, w, x, null);
        }

        /**
         * get first value
         * 
         * @return first value
         */
        public T get1() {
            return t;
        }

        /**
         * get second value
         * 
         * @return second value
         */
        public U get2() {
            return u;
        }

        /**
         * get third value
         * 
         * @return third value
         */
        public V get3() {
            return v;
        }

        /**
         * get fourth value
         * 
         * @return fourth value
         */
        public W get4() {
            return w;
        }

        /**
         * get fifth value
         * 
         * @return fifth value
         */
        public X get5() {
            return x;
        }
    }

    /**
     * Five values of the same type
     * 
     * @param <T> value type
     */
    public static class FiveOf<T> extends Five<T, T, T, T, T> {
        FiveOf(
            final T t,
            final T u,
            final T v,
            final T w,
            final T x
        ) {
            super(t, u, v, w, x);
        }
    }

    /**
     * Up to five values of different types
     * 
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     */
    public static class UpToFive<T, U, V, W, X> extends Base<T, U, V, W, X, Void> {
        UpToFive(
            final int count,
            final T t,
            final U u,
            final V v,
            final W w,
            final X x
        ) {
            super(count, t, u, v, w, x, null);
        }
        
        /**
         * get the count
         * 
         * @return the count
         */
        public int getCount() {
            return count;
        }

        /**
         * get first value
         * 
         * @return first value
         */
        public T get1() {
            return t;
        }

        /**
         * get second value
         * 
         * @return second value
         */
        public U get2() {
            return u;
        }

        /**
         * get third value
         * 
         * @return third value
         */
        public V get3() {
            return v;
        }

        /**
         * get fourth value
         * 
         * @return fourth value
         */
        public W get4() {
            return w;
        }

        /**
         * get fifth value
         * 
         * @return fifth value
         */
        public X get5() {
            return x;
        }
    }

    /**
     * Up to five values of the same type
     * 
     * @param <T> value type
     */
    public static class UpToFiveOf<T> extends UpToFive<T, T, T, T, T> {
        UpToFiveOf(
            final int count,
            final T t,
            final T u,
            final T v,
            final T w,
            final T x
        ) {
            super(count, t, u, v, w, x);
        }
    }
    
    // ==== Six classes

    /**
     * Six values of different types
     * 
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param <Y> sixth type
     */
    public static class Six<T, U, V, W, X, Y> extends Base<T, U, V, W, X, Y> {
        Six(
            final T t,
            final U u,
            final V v,
            final W w,
            final X x,
            final Y y
        ) {
            super(6, t, u, v, w, x, y);
        }

        /**
         * get first value
         * 
         * @return first value
         */
        public T get1() {
            return t;
        }

        /**
         * get second value
         * 
         * @return second value
         */
        public U get2() {
            return u;
        }

        /**
         * get third value
         * 
         * @return third value
         */
        public V get3() {
            return v;
        }

        /**
         * get fourth value
         * 
         * @return fourth value
         */
        public W get4() {
            return w;
        }

        /**
         * get fifth value
         * 
         * @return fifth value
         */
        public X get5() {
            return x;
        }

        /**
         * get sixth value
         * 
         * @return sixth value
         */
        public Y get6() {
            return y;
        }
    }

    /**
     * Six values of the same type
     * 
     * @param <T> value type
     */
    public static class SixOf<T> extends Six<T, T, T, T, T, T> {
        SixOf(
            final T t,
            final T u,
            final T v,
            final T w,
            final T x,
            final T y
        ) {
            super(t, u, v, w, x, y);
        }
    }

    /**
     * Up to six values of different types
     * 
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param <Y> sixth type
     */
    public static class UpToSix<T, U, V, W, X, Y> extends Base<T, U, V, W, X, Y> {
        UpToSix(
            final int count,
            final T t,
            final U u,
            final V v,
            final W w,
            final X x,
            final Y y
        ) {
            super(count, t, u, v, w, x, y);
        }
        
        /**
         * get the count
         * 
         * @return the count
         */
        public int getCount() {
            return count;
        }

        /**
         * get first value
         * 
         * @return first value
         */
        public T get1() {
            return t;
        }

        /**
         * get second value
         * 
         * @return second value
         */
        public U get2() {
            return u;
        }

        /**
         * get third value
         * 
         * @return third value
         */
        public V get3() {
            return v;
        }

        /**
         * get fourth value
         * 
         * @return fourth value
         */
        public W get4() {
            return w;
        }

        /**
         * get fifth value
         * 
         * @return fifth value
         */
        public X get5() {
            return x;
        }

        /**
         * get sixth value
         * 
         * @return sixth value
         */
        public Y get6() {
            return y;
        }
    }

    /**
     * Up to six values of the same type
     * 
     * @param <T> value type
     */
    public static class UpToSixOf<T> extends UpToSix<T, T, T, T, T, T> {
        UpToSixOf(
            final int count,
            final T t,
            final T u,
            final T v,
            final T w,
            final T x,
            final T y
        ) {
            super(count, t, u, v, w, x, y);
        }
    }
    
    // ==== Two Named constructors
    
    /**
     * Construct a tuple of two non-null values of different types
     *  
     * @param <T> first type
     * @param <U> second type
     * @param t first value
     * @param u second value
     * @return tuple of two values
     */
    public static <T, U> Two<T, U> of(
        final T t,
        final U u
    ) {
        return new Two<>(
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u")
        );
    }

    /**
     * Construct a tuple of two nullable values of different types
     *  
     * @param <T> first type
     * @param <U> second type
     * @param t first value
     * @param u second value
     * @return tuple of two values
     */
    public static <T, U> Two<T, U> ofNullable(
        final T t,
        final U u
    ) {
        return new Two<>(
            t,
            u
        );
    }

    /**
     * Construct a tuple of two non-null values of the same type
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @return tuple of two values
     */
    public static <T> TwoOf<T> ofSame(
        final T t,
        final T u
    ) {
        return new TwoOf<>(
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u")
        );
    }

    /**
     * Construct a tuple of two nullable values of the same type
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @return tuple of two values
     */
    public static <T> TwoOf<T> ofSameNullable(
        final T t,
        final T u
    ) {
        return new TwoOf<>(
            t,
            u
        );
    }

    /**
     * Construct a tuple capable of holding up to two values of different types with no values
     *  
     * @param <T> first type
     * @param <U> second type
     * @return tuple of up to two values that is empty
     */
    public static <T, U> UpToTwo<T, U> upToTwo(
    ) {
        return new UpToTwo<>(
            0,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to two values of different types with one non-null value
     *  
     * @param <T> first type
     * @param <U> second type
     * @param t value
     * @return tuple of up to two values that has one value
     */
    public static <T, U> UpToTwo<T, U> upToTwo(
        final T t
    ) {
        return new UpToTwo<>(
            1,
            Objects.requireNonNull(t, "t"),
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to two values of different types with one nullable value
     *  
     * @param <T> first type
     * @param <U> second type
     * @param t value
     * @return tuple of up to two values that has one value
     */
    public static <T, U> UpToTwo<T, U> upToTwoNullable(
        final T t
    ) {
        return new UpToTwo<>(
            1,
            t,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to two values of different types with two non-null values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param t first value
     * @param u second value
     * @return tuple of up to two values that has two values
     */
    public static <T, U> UpToTwo<T, U> upToTwo(
        final T t,
        final U u
    ) {
        return new UpToTwo<>(
            2,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u")
        );
    }

    /**
     * Construct a tuple capable of holding up to two values of different types with two nullable values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param t first value
     * @param u second value
     * @return tuple of up to two values that has two values
     */
    public static <T, U> UpToTwo<T, U> upToTwoNullable(
        final T t,
        final U u
    ) {
        return new UpToTwo<>(
            2,
            t,
            u
        );
    }

    /**
     * Construct a tuple capable of holding up to two values of the same type with no values
     *  
     * @param <T> type
     * @return tuple of up to two values that is empty
     */
    public static <T> UpToTwoOf<T> upToTwoOf(
    ) {
        return new UpToTwoOf<>(
            0,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to two values of the same type with one non-null value
     *  
     * @param <T> type
     * @param t value
     * @return tuple of up to two values that has one value
     */
    public static <T> UpToTwoOf<T> upToTwoOf(
        final T t
    ) {
        return new UpToTwoOf<>(
            1,
            Objects.requireNonNull(t, "t"),
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to two values of the same type with one nullable value
     *  
     * @param <T> type
     * @param t value
     * @return tuple of up to two values that has one value
     */
    public static <T> UpToTwoOf<T> upToTwoOfNullable(
        final T t
    ) {
        return new UpToTwoOf<>(
            1,
            t,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to two values of the same type with two non-null values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @return tuple of up to two values that has two values
     */
    public static <T> UpToTwoOf<T> upToTwoOf(
        final T t,
        final T u
    ) {
        return new UpToTwoOf<>(
            2,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u")
        );
    }

    /**
     * Construct a tuple capable of holding up to two values of the same type with two nullable values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @return tuple of up to two values that has two values
     */
    public static <T> UpToTwoOf<T> upToTwoOfNullable(
        final T t,
        final T u
    ) {
        return new UpToTwoOf<>(
            2,
            t,
            u
        );
    }
    
    // ==== Three Named constructors

    /**
     * Construct a tuple of three non-null values of different types
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param t first value
     * @param u second value
     * @param v third value
     * @return tuple of three values
     */
    public static <T, U, V> Three<T, U, V> of(
        final T t,
        final U u,
        final V v
    ) {
        return new Three<>(
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v")
        );
    }
    
    /**
     * Construct a tuple of three nullable values of different types
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param t first value
     * @param u second value
     * @param v third value
     * @return tuple of three values
     */
    public static <T, U, V> Three<T, U, V> ofNullable(
        final T t,
        final U u,
        final V v
    ) {
        return new Three<>(
            t,
            u,
            v
        );
    }
    
    /**
     * Construct a tuple of three non-null values of the same type
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @return tuple of three values
     */
    public static <T> ThreeOf<T> ofSame(
        final T t,
        final T u,
        final T v
    ) {
        return new ThreeOf<>(
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v")
        );
    }
    

    /**
     * Construct a tuple of three nullable values of the same type
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @return tuple of three values
     */
    public static <T> ThreeOf<T> ofSameNullable(
        final T t,
        final T u,
        final T v
    ) {
        return new ThreeOf<>(
            t,
            u,
            v
        );
    }

    /**
     * Construct a tuple capable of holding up to three values of different types with no values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @return tuple of up to three values that is empty
     */
    public static <T, U, V> UpToThree<T, U, V> upToThree(
    ) {
        return new UpToThree<>(
            0,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to three values of different types with one non-null value
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param t value
     * @return tuple of up to three values that has one value
     */
    public static <T, U, V> UpToThree<T, U, V> upToThree(
        final T t
    ) {
        return new UpToThree<>(
            1,
            Objects.requireNonNull(t, "t"),
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to three values of different types with one nullable value
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param t value
     * @return tuple of up to three values that has one value
     */
    public static <T, U, V> UpToThree<T, U, V> upToThreeNullable(
        final T t
    ) {
        return new UpToThree<>(
            1,
            t,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to three values of different types with two non-null values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param t first value
     * @param u second value
     * @return tuple of up to three values that has two values
     */
    public static <T, U, V> UpToThree<T, U, V> upToThree(
        final T t,
        final U u
    ) {
        return new UpToThree<>(
            2,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to three values of different types with two nullable values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param t first value
     * @param u second value
     * @return tuple of up to three values that has two values
     */
    public static <T, U, V> UpToThree<T, U, V> upToThreeNullable(
        final T t,
        final U u
    ) {
        return new UpToThree<>(
            2,
            t,
            u,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to three values of different types with three non-null values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param t first value
     * @param u second value
     * @param v third value
     * @return tuple of up to three values that has three values
     */
    public static <T, U, V> UpToThree<T, U, V> upToThree(
        final T t,
        final U u,
        final V v
    ) {
        return new UpToThree<>(
            3,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v")
        );
    }

    /**
     * Construct a tuple capable of holding up to three values of different types with three nullable values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param t first value
     * @param u second value
     * @param v third value
     * @return tuple of up to three values that has three values
     */
    public static <T, U, V> UpToThree<T, U, V> upToThreeNullable(
        final T t,
        final U u,
        final V v
    ) {
        return new UpToThree<>(
            3,
            t,
            u,
            v
        );
    }

    /**
     * Construct a tuple capable of holding up to three values of the same type with no values
     *  
     * @param <T> type
     * @return tuple of up to three values that is empty
     */
    public static <T> UpToThreeOf<T> upToThreeOf(
    ) {
        return new UpToThreeOf<>(
            0,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to three values of the same type with one non-null value
     *  
     * @param <T> type
     * @param t value
     * @return tuple of up to three values that has one value
     */
    public static <T> UpToThreeOf<T> upToThreeOf(
        final T t
    ) {
        return new UpToThreeOf<>(
            1,
            Objects.requireNonNull(t, "t"),
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to three values of the same type with one nullable value
     *  
     * @param <T> type
     * @param t value
     * @return tuple of up to three values that has one value
     */
    public static <T> UpToThreeOf<T> upToThreeOfNullable(
        final T t
    ) {
        return new UpToThreeOf<>(
            1,
            t,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to three values of the same type with two non-null values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @return tuple of up to three values that has two values
     */
    public static <T> UpToThreeOf<T> upToThreeOf(
        final T t,
        final T u
    ) {
        return new UpToThreeOf<>(
            2,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to three values of the same type with two nullable values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @return tuple of up to three values that has two values
     */
    public static <T> UpToThreeOf<T> upToThreeOfNullable(
        final T t,
        final T u
    ) {
        return new UpToThreeOf<>(
            2,
            t,
            u,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to three values of the same type with three non-null values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @return tuple of up to three values that has three values
     */
    public static <T> UpToThreeOf<T> upToThreeOf(
        final T t,
        final T u,
        final T v
    ) {
        return new UpToThreeOf<>(
            3,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v")
        );
    }

    /**
     * Construct a tuple capable of holding up to three values of the same type with three nullable values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @return tuple of up to three values that has three values
     */
    public static <T> UpToThreeOf<T> upToThreeOfNullable(
        final T t,
        final T u,
        final T v
    ) {
        return new UpToThreeOf<>(
            3,
            t,
            u,
            v
        );
    }
    
    // ==== Four Named constructors

    /**
     * Construct a tuple of four non-null values of different types
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @return tuple of four values
     */
    public static <T, U, V, W> Four<T, U, V, W> of(
        final T t,
        final U u,
        final V v,
        final W w
    ) {
        return new Four<>(
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            Objects.requireNonNull(w, "w")
        );
    }
    
    /**
     * Construct a tuple of four nullable values of different types
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @return tuple of four values
     */
    public static <T, U, V, W> Four<T, U, V, W> ofNullable(
        final T t,
        final U u,
        final V v,
        final W w
    ) {
        return new Four<>(
            t,
            u,
            v,
            w
        );
    }
    
    /**
     * Construct a tuple of four non-null values of the same type
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @return tuple of four values
     */
    public static <T> FourOf<T> ofSame(
        final T t,
        final T u,
        final T v,
        final T w
    ) {
        return new FourOf<>(
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            Objects.requireNonNull(w, "w")
        );
    }

    /**
     * Construct a tuple of four nullable values of the same type
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @return tuple of four values
     */
    public static <T> FourOf<T> ofSameNullable(
        final T t,
        final T u,
        final T v,
        final T w
    ) {
        return new FourOf<>(
            t,
            u,
            v,
            w
        );
    }

    /**
     * Construct a tuple capable of holding up to four values of different types with no values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @return tuple of up to four values that is empty
     */
    public static <T, U, V, W> UpToFour<T, U, V, W> upToFour(
    ) {
        return new UpToFour<>(
            0,
            null,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to four values of different types with one non-null value
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param t value
     * @return tuple of up to four values that has one value
     */
    public static <T, U, V, W> UpToFour<T, U, V, W> upToFour(
        final T t
    ) {
        return new UpToFour<>(
            1,
            Objects.requireNonNull(t, "t"),
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to four values of different types with one nullable value
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param t value
     * @return tuple of up to four values that has one value
     */
    public static <T, U, V, W> UpToFour<T, U, V, W> upToFourNullable(
        final T t
    ) {
        return new UpToFour<>(
            1,
            t,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to four values of different types with two non-null values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param t first value
     * @param u second value
     * @return tuple of up to four values that has two values
     */
    public static <T, U, V, W> UpToFour<T, U, V, W> upToFour(
        final T t,
        final U u
    ) {
        return new UpToFour<>(
            2,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to four values of different types with two nullable values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param t first value
     * @param u second value
     * @return tuple of up to four values that has two values
     */
    public static <T, U, V, W> UpToFour<T, U, V, W> upToFourNullable(
        final T t,
        final U u
    ) {
        return new UpToFour<>(
            2,
            t,
            u,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to four values of different types with three non-null values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @return tuple of up to four values that has three values
     */
    public static <T, U, V, W> UpToFour<T, U, V, W> upToFour(
        final T t,
        final U u,
        final V v
    ) {
        return new UpToFour<>(
            3,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to four values of different types with three nullable values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @return tuple of up to four values that has three values
     */
    public static <T, U, V, W> UpToFour<T, U, V, W> upToFourNullable(
        final T t,
        final U u,
        final V v
    ) {
        return new UpToFour<>(
            3,
            t,
            u,
            v,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to four values of different types with four non-null values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w third value
     * @return tuple of up to four values that has four values
     */
    public static <T, U, V, W> UpToFour<T, U, V, W> upToFour(
        final T t,
        final U u,
        final V v,
        final W w
    ) {
        return new UpToFour<>(
            4,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            Objects.requireNonNull(w, "w")
        );
    }

    /**
     * Construct a tuple capable of holding up to four values of different types with four nullable values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @return tuple of up to four values that has four values
     */
    public static <T, U, V, W> UpToFour<T, U, V, W> upToFourNullable(
        final T t,
        final U u,
        final V v,
        final W w
    ) {
        return new UpToFour<>(
            4,
            t,
            u,
            v,
            w
        );
    }

    /**
     * Construct a tuple capable of holding up to four values of the same type with no values
     *  
     * @param <T> type
     * @return tuple of up to four values that is empty
     */
    public static <T> UpToFourOf<T> upToFourOf(
    ) {
        return new UpToFourOf<>(
            0,
            null,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to four values of the same type with one non-null value
     *  
     * @param <T> type
     * @param t value
     * @return tuple of up to four values that has one value
     */
    public static <T> UpToFourOf<T> upToFourOf(
        final T t
    ) {
        return new UpToFourOf<>(
            1,
            Objects.requireNonNull(t, "t"),
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to four values of the same type with one nullable value
     *  
     * @param <T> type
     * @param t value
     * @return tuple of up to four values that has one value
     */
    public static <T> UpToFourOf<T> upToFourOfNullable(
        final T t
    ) {
        return new UpToFourOf<>(
            1,
            t,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to four values of the same type with two non-null values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @return tuple of up to four values that has two values
     */
    public static <T> UpToFourOf<T> upToFourOf(
        final T t,
        final T u
    ) {
        return new UpToFourOf<>(
            2,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to four values of the same type with two nullable values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @return tuple of up to four values that has two values
     */
    public static <T> UpToFourOf<T> upToFourOfNullable(
        final T t,
        final T u
    ) {
        return new UpToFourOf<>(
            2,
            t,
            u,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to four values of the same type with three non-null values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @return tuple of up to four values that has three values
     */
    public static <T> UpToFourOf<T> upToFourOf(
        final T t,
        final T u,
        final T v
    ) {
        return new UpToFourOf<>(
            3,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to four values of the same type with three nullable values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @return tuple of up to four values that has three values
     */
    public static <T> UpToFourOf<T> upToFourOfNullable(
        final T t,
        final T u,
        final T v
    ) {
        return new UpToFourOf<>(
            3,
            t,
            u,
            v,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to four values of the same type with four non-null values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param v fourth value
     * @return tuple of up to four values that has four values
     */
    public static <T> UpToFourOf<T> upToFourOf(
        final T t,
        final T u,
        final T v,
        final T w
    ) {
        return new UpToFourOf<>(
            4,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            Objects.requireNonNull(w, "w")
        );
    }

    /**
     * Construct a tuple capable of holding up to four values of the same type with four nullable values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @return tuple of up to four values that has four values
     */
    public static <T> UpToFourOf<T> upToFourOfNullable(
        final T t,
        final T u,
        final T v,
        final T w
    ) {
        return new UpToFourOf<>(
            4,
            t,
            u,
            v,
            w
        );
    }
    
    // ==== Five Named constructors

    /**
     * Construct a tuple of five non-null values of different types
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @param x fifth value
     * @return tuple of five values
     */
    public static <T, U, V, W, X> Five<T, U, V, W, X> of(
        final T t,
        final U u,
        final V v,
        final W w,
        final X x
    ) {
        return new Five<>(
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            Objects.requireNonNull(w, "w"),
            Objects.requireNonNull(x, "x")
        );
    }
    
    /**
     * Construct a tuple of five nullable values of different types
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @param x fifth value
     * @return tuple of five values
     */
    public static <T, U, V, W, X> Five<T, U, V, W, X> ofNullable(
        final T t,
        final U u,
        final V v,
        final W w,
        final X x
    ) {
        return new Five<>(
            t,
            u,
            v,
            w,
            x
        );
    }
    
    /**
     * Construct a tuple of five non-null values of the same type
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @param x fifth value
     * @return tuple of five values
     */
    public static <T> FiveOf<T> ofSame(
        final T t,
        final T u,
        final T v,
        final T w,
        final T x
    ) {
        return new FiveOf<>(
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            Objects.requireNonNull(w, "w"),
            Objects.requireNonNull(x, "x")
        );
    }

    /**
     * Construct a tuple of five nullable values of the same type
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @param x fifth value
     * @return tuple of five values
     */
    public static <T> FiveOf<T> ofSameNullable(
        final T t,
        final T u,
        final T v,
        final T w,
        final T x
    ) {
        return new FiveOf<>(
            t,
            u,
            v,
            w,
            x
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of different types with no values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @return tuple of up to five values that is empty
     */
    public static <T, U, V, W, X> UpToFive<T, U, V, W, X> upToFive(
    ) {
        return new UpToFive<>(
            0,
            null,
            null,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of different types with one non-null value
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param t value
     * @return tuple of up to five values that has one value
     */
    public static <T, U, V, W, X> UpToFive<T, U, V, W, X> upToFive(
        final T t
    ) {
        return new UpToFive<>(
            1,
            Objects.requireNonNull(t, "t"),
            null,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of different types with one nullable value
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param t value
     * @return tuple of up to five values that has one value
     */
    public static <T, U, V, W, X> UpToFive<T, U, V, W, X> upToFiveNullable(
        final T t
    ) {
        return new UpToFive<>(
            1,
            t,
            null,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of different types with two non-null values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param t first value
     * @param u second value
     * @return tuple of up to five values that has two values
     */
    public static <T, U, V, W, X> UpToFive<T, U, V, W, X> upToFive(
        final T t,
        final U u
    ) {
        return new UpToFive<>(
            2,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of different types with two nullable values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param t first value
     * @param u second value
     * @return tuple of up to five values that has two values
     */
    public static <T, U, V, W, X> UpToFive<T, U, V, W, X> upToFiveNullable(
        final T t,
        final U u
    ) {
        return new UpToFive<>(
            2,
            t,
            u,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of different types with three non-null values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @return tuple of up to five values that has three values
     */
    public static <T, U, V, W, X> UpToFive<T, U, V, W, X> upToFive(
        final T t,
        final U u,
        final V v
    ) {
        return new UpToFive<>(
            3,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of different types with three nullable values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @return tuple of up to five values that has three values
     */
    public static <T, U, V, W, X> UpToFive<T, U, V, W, X> upToFiveNullable(
        final T t,
        final U u,
        final V v
    ) {
        return new UpToFive<>(
            3,
            t,
            u,
            v,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of different types with four non-null values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w third value
     * @return tuple of up to five values that has four values
     */
    public static <T, U, V, W, X> UpToFive<T, U, V, W, X> upToFive(
        final T t,
        final U u,
        final V v,
        final W w
    ) {
        return new UpToFive<>(
            4,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            Objects.requireNonNull(w, "w"),
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of different types with four nullable values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @return tuple of up to five values that has four values
     */
    public static <T, U, V, W, X> UpToFive<T, U, V, W, X> upToFiveNullable(
        final T t,
        final U u,
        final V v,
        final W w
    ) {
        return new UpToFive<>(
            4,
            t,
            u,
            v,
            w,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of different types with five non-null values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @param x fifth value
     * @return tuple of up to five values that has five values
     */
    public static <T, U, V, W, X> UpToFive<T, U, V, W, X> upToFive(
        final T t,
        final U u,
        final V v,
        final W w,
        final X x
    ) {
        return new UpToFive<>(
            5,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            Objects.requireNonNull(w, "w"),
            Objects.requireNonNull(x, "x")
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of different types with five nullable values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @param x fifth value
     * @return tuple of up to five values that has five values
     */
    public static <T, U, V, W, X> UpToFive<T, U, V, W, X> upToFiveNullable(
        final T t,
        final U u,
        final V v,
        final W w,
        final X x
    ) {
        return new UpToFive<>(
            5,
            t,
            u,
            v,
            w,
            x
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of the same type with no values
     *  
     * @param <T> type
     * @return tuple of up to five values that is empty
     */
    public static <T> UpToFiveOf<T> upToFiveOf(
    ) {
        return new UpToFiveOf<>(
            0,
            null,
            null,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of the same type with one non-null value
     *  
     * @param <T> type
     * @param t value
     * @return tuple of up to five values that has one value
     */
    public static <T> UpToFiveOf<T> upToFiveOf(
        final T t
    ) {
        return new UpToFiveOf<>(
            1,
            Objects.requireNonNull(t, "t"),
            null,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of the same type with one nullable value
     *  
     * @param <T> type
     * @param t value
     * @return tuple of up to five values that has one value
     */
    public static <T> UpToFiveOf<T> upToFiveOfNullable(
        final T t
    ) {
        return new UpToFiveOf<>(
            1,
            t,
            null,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of the same type with two non-null values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @return tuple of up to five values that has two values
     */
    public static <T> UpToFiveOf<T> upToFiveOf(
        final T t,
        final T u
    ) {
        return new UpToFiveOf<>(
            2,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of the same type with two nullable values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @return tuple of up to five values that has two values
     */
    public static <T> UpToFiveOf<T> upToFiveOfNullable(
        final T t,
        final T u
    ) {
        return new UpToFiveOf<>(
            2,
            t,
            u,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of the same type with three non-null values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @return tuple of up to five values that has three values
     */
    public static <T> UpToFiveOf<T> upToFiveOf(
        final T t,
        final T u,
        final T v
    ) {
        return new UpToFiveOf<>(
            3,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of the same type with three nullable values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @return tuple of up to five values that has three values
     */
    public static <T> UpToFiveOf<T> upToFiveOfNullable(
        final T t,
        final T u,
        final T v
    ) {
        return new UpToFiveOf<>(
            3,
            t,
            u,
            v,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of the same type with four non-null values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param v fourth value
     * @return tuple of up to five values that has four values
     */
    public static <T> UpToFiveOf<T> upToFiveOf(
        final T t,
        final T u,
        final T v,
        final T w
    ) {
        return new UpToFiveOf<>(
            4,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            Objects.requireNonNull(w, "w"),
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of the same type with four nullable values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @return tuple of up to five values that has four values
     */
    public static <T> UpToFiveOf<T> upToFiveOfNullable(
        final T t,
        final T u,
        final T v,
        final T w
    ) {
        return new UpToFiveOf<>(
            4,
            t,
            u,
            v,
            w,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of the same type with five non-null values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param v fourth value
     * @param x fifth value
     * @return tuple of up to five values that has five values
     */
    public static <T> UpToFiveOf<T> upToFiveOf(
        final T t,
        final T u,
        final T v,
        final T w,
        final T x
    ) {
        return new UpToFiveOf<>(
            5,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            Objects.requireNonNull(w, "w"),
            Objects.requireNonNull(x, "x")
        );
    }

    /**
     * Construct a tuple capable of holding up to five values of the same type with five nullable values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @param x fifth value
     * @return tuple of up to five values that has five values
     */
    public static <T> UpToFiveOf<T> upToFiveOfNullable(
        final T t,
        final T u,
        final T v,
        final T w,
        final T x
    ) {
        return new UpToFiveOf<>(
            5,
            t,
            u,
            v,
            w,
            x
        );
    }
    
    // ==== Six Named constructors

    /**
     * Construct a tuple of six non-null values of different types
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param <Y> sixth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @param x fifth value
     * @param y sixth value
     * @return tuple of six values
     */
    public static <T, U, V, W, X, Y> Six<T, U, V, W, X, Y> of(
        final T t,
        final U u,
        final V v,
        final W w,
        final X x,
        final Y y
    ) {
        return new Six<>(
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            Objects.requireNonNull(w, "w"),
            Objects.requireNonNull(x, "x"),
            Objects.requireNonNull(y, "y")
        );
    }
    
    /**
     * Construct a tuple of six nullable values of different types
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param <Y> sixth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @param x fifth value
     * @param y sixth value
     * @return tuple of six values
     */
    public static <T, U, V, W, X, Y> Six<T, U, V, W, X, Y> ofNullable(
        final T t,
        final U u,
        final V v,
        final W w,
        final X x,
        final Y y
    ) {
        return new Six<>(
            t,
            u,
            v,
            w,
            x,
            y
        );
    }
    
    /**
     * Construct a tuple of six non-null values of the same type
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @param x fifth value
     * @param y sixth value
     * @return tuple of six values
     */
    public static <T> SixOf<T> ofSame(
        final T t,
        final T u,
        final T v,
        final T w,
        final T x,
        final T y
    ) {
        return new SixOf<>(
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            Objects.requireNonNull(w, "w"),
            Objects.requireNonNull(x, "x"),
            Objects.requireNonNull(y, "y")
        );
    }

    /**
     * Construct a tuple of six nullable values of the same type
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @param x fifth value
     * @param y sixth value
     * @return tuple of six values
     */
    public static <T> SixOf<T> ofSameNullable(
        final T t,
        final T u,
        final T v,
        final T w,
        final T x,
        final T y
    ) {
        return new SixOf<>(
            t,
            u,
            v,
            w,
            x,
            y
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of different types with no values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param <Y> sixth type
     * @return tuple of up to six values that is empty
     */
    public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> upToSix(
    ) {
        return new UpToSix<>(
            0,
            null,
            null,
            null,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of different types with one non-null value
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param <Y> sixth type
     * @param t value
     * @return tuple of up to six values that has one value
     */
    public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> upToSix(
        final T t
    ) {
        return new UpToSix<>(
            1,
            Objects.requireNonNull(t, "t"),
            null,
            null,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of different types with one nullable value
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param <Y> sixth type
     * @param t value
     * @return tuple of up to six values that has one value
     */
    public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> upToSixNullable(
        final T t
    ) {
        return new UpToSix<>(
            1,
            t,
            null,
            null,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of different types with two non-null values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param <Y> sixth type
     * @param t first value
     * @param u second value
     * @return tuple of up to six values that has two values
     */
    public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> upToSix(
        final T t,
        final U u
    ) {
        return new UpToSix<>(
            2,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            null,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of different types with two nullable values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param <Y> sixth type
     * @param t first value
     * @param u second value
     * @return tuple of up to six values that has two values
     */
    public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> upToSixNullable(
        final T t,
        final U u
    ) {
        return new UpToSix<>(
            2,
            t,
            u,
            null,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of different types with three non-null values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param <Y> sixth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @return tuple of up to six values that has three values
     */
    public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> upToSix(
        final T t,
        final U u,
        final V v
    ) {
        return new UpToSix<>(
            3,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of different types with three nullable values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param <Y> sixth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @return tuple of up to six values that has three values
     */
    public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> upToSixNullable(
        final T t,
        final U u,
        final V v
    ) {
        return new UpToSix<>(
            3,
            t,
            u,
            v,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of different types with four non-null values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param <Y> sixth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w third value
     * @return tuple of up to six values that has four values
     */
    public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> upToSix(
        final T t,
        final U u,
        final V v,
        final W w
    ) {
        return new UpToSix<>(
            4,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            Objects.requireNonNull(w, "w"),
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of different types with four nullable values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param <Y> sixth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @return tuple of up to six values that has four values
     */
    public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> upToSixNullable(
        final T t,
        final U u,
        final V v,
        final W w
    ) {
        return new UpToSix<>(
            4,
            t,
            u,
            v,
            w,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of different types with five non-null values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param <Y> sixth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @param x fifth value
     * @return tuple of up to six values that has five values
     */
    public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> upToSix(
        final T t,
        final U u,
        final V v,
        final W w,
        final X x
    ) {
        return new UpToSix<>(
            5,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            Objects.requireNonNull(w, "w"),
            Objects.requireNonNull(x, "x"),
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of different types with five nullable values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param <Y> sixth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @param x fifth value
     * @return tuple of up to six values that has five values
     */
    public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> upToSixNullable(
        final T t,
        final U u,
        final V v,
        final W w,
        final X x
    ) {
        return new UpToSix<>(
            5,
            t,
            u,
            v,
            w,
            x,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of different types with six non-null values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param <Y> sixth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @param x fifth value
     * @param y sixth value
     * @return tuple of up to six values that has six values
     */
    public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> upToSix(
        final T t,
        final U u,
        final V v,
        final W w,
        final X x,
        final Y y
    ) {
        return new UpToSix<>(
            6,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            Objects.requireNonNull(w, "w"),
            Objects.requireNonNull(x, "x"),
            Objects.requireNonNull(y, "y")
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of different types with six nullable values
     *  
     * @param <T> first type
     * @param <U> second type
     * @param <V> third type
     * @param <W> fourth type
     * @param <X> fifth type
     * @param <Y> sixth type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @param x fifth value
     * @param y sixth value
     * @return tuple of up to six values that has six values
     */
    public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> upToSixNullable(
        final T t,
        final U u,
        final V v,
        final W w,
        final X x,
        final Y y
    ) {
        return new UpToSix<>(
            6,
            t,
            u,
            v,
            w,
            x,
            y
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of the same type with no values
     *  
     * @param <T> type
     * @return tuple of up to six values that is empty
     */
    public static <T> UpToSixOf<T> upToSixOf(
    ) {
        return new UpToSixOf<>(
            0,
            null,
            null,
            null,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of the same type with one non-null value
     *  
     * @param <T> type
     * @param t value
     * @return tuple of up to six values that has one value
     */
    public static <T> UpToSixOf<T> upToSixOf(
        final T t
    ) {
        return new UpToSixOf<>(
            1,
            Objects.requireNonNull(t, "t"),
            null,
            null,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of the same type with one nullable value
     *  
     * @param <T> type
     * @param t value
     * @return tuple of up to six values that has one value
     */
    public static <T> UpToSixOf<T> upToSixOfNullable(
        final T t
    ) {
        return new UpToSixOf<>(
            1,
            t,
            null,
            null,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of the same type with two non-null values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @return tuple of up to six values that has two values
     */
    public static <T> UpToSixOf<T> upToSixOf(
        final T t,
        final T u
    ) {
        return new UpToSixOf<>(
            2,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            null,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of the same type with two nullable values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @return tuple of up to six values that has two values
     */
    public static <T> UpToSixOf<T> upToSixOfNullable(
        final T t,
        final T u
    ) {
        return new UpToSixOf<>(
            2,
            t,
            u,
            null,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of the same type with three non-null values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @return tuple of up to six values that has three values
     */
    public static <T> UpToSixOf<T> upToSixOf(
        final T t,
        final T u,
        final T v
    ) {
        return new UpToSixOf<>(
            3,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of the same type with three nullable values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @return tuple of up to six values that has three values
     */
    public static <T> UpToSixOf<T> upToSixOfNullable(
        final T t,
        final T u,
        final T v
    ) {
        return new UpToSixOf<>(
            3,
            t,
            u,
            v,
            null,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of the same type with four non-null values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param v fourth value
     * @return tuple of up to six values that has four values
     */
    public static <T> UpToSixOf<T> upToSixOf(
        final T t,
        final T u,
        final T v,
        final T w
    ) {
        return new UpToSixOf<>(
            4,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            Objects.requireNonNull(w, "w"),
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of the same type with four nullable values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @return tuple of up to six values that has four values
     */
    public static <T> UpToSixOf<T> upToSixOfNullable(
        final T t,
        final T u,
        final T v,
        final T w
    ) {
        return new UpToSixOf<>(
            4,
            t,
            u,
            v,
            w,
            null,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of the same type with five non-null values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param v fourth value
     * @param x fifth value
     * @return tuple of up to six values that has five values
     */
    public static <T> UpToSixOf<T> upToSixOf(
        final T t,
        final T u,
        final T v,
        final T w,
        final T x
    ) {
        return new UpToSixOf<>(
            5,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            Objects.requireNonNull(w, "w"),
            Objects.requireNonNull(x, "x"),
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of the same type with five nullable values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @param x fifth value
     * @return tuple of up to six values that has five values
     */
    public static <T> UpToSixOf<T> upToSixOfNullable(
        final T t,
        final T u,
        final T v,
        final T w,
        final T x
    ) {
        return new UpToSixOf<>(
            5,
            t,
            u,
            v,
            w,
            x,
            null
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of the same type with six non-null values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param v fourth value
     * @param x fifth value
     * @param y sixth value
     * @return tuple of up to six values that has six values
     */
    public static <T> UpToSixOf<T> upToSixOf(
        final T t,
        final T u,
        final T v,
        final T w,
        final T x,
        final T y
    ) {
        return new UpToSixOf<>(
            6,
            Objects.requireNonNull(t, "t"),
            Objects.requireNonNull(u, "u"),
            Objects.requireNonNull(v, "v"),
            Objects.requireNonNull(w, "w"),
            Objects.requireNonNull(x, "x"),
            Objects.requireNonNull(y, "y")
        );
    }

    /**
     * Construct a tuple capable of holding up to six values of the same type with six nullable values
     *  
     * @param <T> type
     * @param t first value
     * @param u second value
     * @param v third value
     * @param w fourth value
     * @param x fifth value
     * @param y sixth value
     * @return tuple of up to six values that has six values
     */
    public static <T> UpToSixOf<T> upToSixOfNullable(
        final T t,
        final T u,
        final T v,
        final T w,
        final T x,
        final T y
    ) {
        return new UpToSixOf<>(
            6,
            t,
            u,
            v,
            w,
            x,
            y
        );
    }
}
