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
 * - Call appropriate Tuple.Same.of{Nullable?}() overload that accepts N args
 * 
 * EG:
 * Tuple.TwoOf<String> Foo() {
 *   ...
 *   return Tuple.Same.of("foo", "bar")
 *   OR
 *   return Tuple.Same.ofNullable(null, "bar")
 * }
 * 
 * 3. Create a List or Set of tuples of a specific size, with different or same types
 * - Declare type as:
 *   {List,Set}<Tuple.{Two,Three,Four,Five,Six}<T,U,...>>
 *   OR
 *   {List,Set}<Tuple.{Two,Three,Four,Five,Six}Of<T>>
 * - Use Collections.{listOf,setOf}(Tuple.of{Nullable?}(N args)...)
 *    OR Collections.{listOf,setOf)(Tuple.Same.of{Nullable?}(N args)...)
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
 *   Tuple.Same.of("foo", "bar"),
 *   Tuple.Same.ofNullable("foo", null),
 *   ...
 * )
 * 
 * 4. Create a List or Set of tuples that have a variable number of values
 * - Declare type as
 *   Tuple.UpTo{Two,Three,Four,Five,Six}<T,U,...>
 *   OR
 *   Tuple.UpTo{Two,Three,Four,Five,Six}Of<T>
 * - Use Collections.{listOf,setOf}(Tuple.UpTo.{two,three,four,five,six}{Nullable?}(0..N values))
 * - Test number of values in a particular tuple with count() method
 * - NOTE: there are no               upToN{Of?}Nullable() methods of no args,
 *         since they are the same as upToN{Of?}()         methods of no args 
 * 
 * EG:
 * List<Tuple.UpToThree<String, Integer, Double>> list = Collections.listOf(
 *   Tuple.UpTo.three(),
 *   Tuple.UpTo.three("foo"),
 *   Tuple.UpTo.three("bar", 1),
 *   Tuple.UpTo.three("bar", 1, 3.25),
 *   Tuple.UpTo.threeNullable(null),
 *   Tuple.UpTo.threeNullable("baz", null),
 *   Tuple.UpTo.threeNullable("baz", 1, null),
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
 *   Tuple.UpToSame.three(),
 *   Tuple.UpToSame.three(1),
 *   Tuple.UpToSame.three(1, 2),
 *   Tuple.UpToSame.three(1, 2, 3),
 *   Tuple.UpToSame.threeNullable(null),
 *   Tuple.UpToSame.threeNullable(1, null),
 *   Tuple.UpToSame.threeNullable(1, 2, null),
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
 * 5. Create a tuple that acts as a union, where only one value is set
 * - Declare type as Tuple.Union{Two,Three,Four,Five,Six}<T,U,...>
 * - Use Tuple.Union.{two,three,four,five,six}{Nullable?}(position, value), where position is from 1 to N
 * - Call the getPosition() method the union to get the position that has a value
 * 
 * EG:
 * Tuple.UnionThree<String, Integer, Double> u = Tuple.Union.of(2, 5)
 * u.getPosition() = 2
 * u.get1() and u.get3() throw IllegalArgumentException
 * u.get2() = 5
 */
public final class Tuple {
    
    // ==== Utility methods
    
    /**
     * UNION_ERROR_FMT is the error format string to use if the wrong member of a union is accessed.
     */
    public static final String UNION_ERROR_FMT = "Only member %d of the union is available";
    
    /**
     * Verify that the caller has accessed the correct member of a union
     *   
     * @param position the position of the union that is accessible
     * @param requested the position the caller is trying to read
     * @throws IllegalArgumentException if expected != actual
     */
    static void checkUnionPosition(
        final int expected,
        final int actual
    ) {
        if (expected != actual) {
            throw new IllegalArgumentException(
                String.format(UNION_ERROR_FMT, Integer.valueOf(expected))
            );
        }
    }
    
    // ==== Base Classes
    
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
        final int size;
        
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
         * @param size the number of values actually used 
         * @param t the first value
         * @param u the second value
         * @param v the third value
         * @param w the fourth value
         * @param x the fifth value
         * @param y the sixth value
         */
        Base(
            final int size,
            final T t,
            final U u,
            final V v,
            final W w,
            final X x,
            final Y y
        ) { 
            this.size = size;
            this.t = t;
            this.u = u;
            this.v = v;
            this.w = w;
            this.x = x;
            this.y = y;
        }
        
        /**
         * Hash all the provided values in a fashion compatible with {@link Arrays#hashCode(Object[])}. 
         */
        @Override
        public int hashCode() {
            int hashCode = 1;
            
            if (size >= 1) {
                hashCode = 31 * hashCode + Objects.hashCode(t);
            }
            
            if (size >= 2) {
                hashCode = 31 * hashCode + Objects.hashCode(u);
            }
            
            if (size >= 3) {
                hashCode = 31 * hashCode + Objects.hashCode(v);
            }
            
            if (size >= 4) {
                hashCode = 31 * hashCode + Objects.hashCode(w);
            }
            
            if (size >= 5) {
                hashCode = 31 * hashCode + Objects.hashCode(x);
            }
            
            if (size >= 6) {
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
                    (size == obj.size) &&
                    ((size < 1) || Objects.equals(t, obj.t)) &&
                    ((size < 2) || Objects.equals(u, obj.u)) &&
                    ((size < 3) || Objects.equals(v, obj.v)) &&
                    ((size < 4) || Objects.equals(w, obj.w)) &&
                    ((size < 5) || Objects.equals(x, obj.x)) &&
                    ((size < 6) || Objects.equals(y, obj.y));
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
                ((size < 1) ? "" :       String.valueOf(t)) +
                ((size < 2) ? "" : "," + String.valueOf(u)) +
                ((size < 3) ? "" : "," + String.valueOf(v)) +
                ((size < 4) ? "" : "," + String.valueOf(w)) +
                ((size < 5) ? "" : "," + String.valueOf(x)) +
                ((size < 6) ? "" : "," + String.valueOf(y)) + 
                ")";
        }
    }
    
    static class UpToBase<T, U, V, W, X, Y> extends Base<T, U, V, W, X, Y> {
        final int count;
        
        UpToBase(
            final int size,
            final int count,
            final T t,
            final U u,
            final V v,
            final W w,
            final X x,
            final Y y
        ) {
            super(size, t, u, v, w, x, y);
            this.count = count;
        }
        
        public int getCount() {
            return count;
        }
        
        /**
         * Hash all the provided values in a fashion compatible with {@link Arrays#hashCode(Object[])}. 
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
         * The string is of the form (first, second, ...), but only the first count values are included
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
    
    static class UnionBase<T, U, V, W, X, Y> extends Base<T, U, V, W, X, Y> {
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
        UnionBase(
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
        
        @Override
        public int hashCode() {
            int hashCode = 0;
            
            switch(size) {
            case 1:
                if (t != null) {
                    hashCode = t.hashCode();
                }
                break;

            case 2:
                if (u != null) {
                    hashCode = u.hashCode();
                }
                break;

            case 3:
                if (v != null) {
                    hashCode = v.hashCode();
                }
                break;

            case 4:
                if (w != null) {
                    hashCode = w.hashCode();
                }
                break;

            case 5:
                if (x != null) {
                    hashCode = x.hashCode();
                }
                break;

            default:
                if (y != null) {
                    hashCode = y.hashCode();
                }
            }
            
            return hashCode;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("(");
            
            switch(size) {
            case 1:
                sb.append(t);
                break;
    
            case 2:
                sb.append(u);
                break;
    
            case 3:
                sb.append(v);
                break;
    
            case 4:
                sb.append(w);
                break;
    
            case 5:
                sb.append(x);
                break;
    
            default:
                sb.append(y);
            }
        
            return sb.append(")").toString();
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
    public static class UpToTwo<T, U> extends UpToBase<T, U, Void, Void, Void, Void> {
        UpToTwo(
            final int count,
            final T t,
            final U u
        ) {
            super(2, count, t, u, null, null, null, null);
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
    
    /**
     * A union of two values of different types
     */
    public static class UnionTwo<T, U> extends UnionBase<T, U, Void, Void, Void, Void> {
        UnionTwo(
            final int position,
            final T t,
            final U u
        ) {
            super(position, t, u, null, null, null, null);
        }
        
        /**
         * get the position
         * 
         * @return the position of the set value
         */
        public int getPosition() {
            return size;
        }

        /**
         * get first value
         * 
         * @return first value
         * @throws IllegalArgumentException if position != 1
         */
        public T get1() {
            checkUnionPosition(size, 1);
            return t;
        }

        /**
         * get second value
         * 
         * @return second value
         * @throws IllegalArgumentException if position != 2
         */
        public U get2() {
            checkUnionPosition(size, 2);
            return u;
        }
    }

    /**
     * A union of two values of the same type
     */
    public static class UnionTwoOf<T> extends UnionTwo<T, T> {
        UnionTwoOf(
            final int position,
            final T t,
            final T u
        ) {
            super(position, t, u);
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
            return size;
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
    
    /**
     * A union of three values of different types
     */
    public static class UnionThree<T, U, V> extends UnionBase<T, U, V, Void, Void, Void> {
        UnionThree(
            final int position,
            final T t,
            final U u,
            final V v
        ) {
            super(position, t, u, v, null, null, null);
        }
        
        /**
         * get the position
         * 
         * @return the position of the set value
         */
        public int getPosition() {
            return size;
        }

        /**
         * get first value
         * 
         * @return first value
         * @throws IllegalArgumentException if position != 1
         */
        public T get1() {
            checkUnionPosition(size, 1);
            return t;
        }

        /**
         * get second value
         * 
         * @return second value
         * @throws IllegalArgumentException if position != 2
         */
        public U get2() {
            checkUnionPosition(size, 2);
            return u;
        }

        /**
         * get third value
         * 
         * @return third value
         * @throws IllegalArgumentException if position != 3
         */
        public V get3() {
            checkUnionPosition(size, 3);
            return v;
        }
    }

    /**
     * A union of three values of the same type
     */
    public static class UnionThreeOf<T> extends UnionThree<T, T, T> {
        UnionThreeOf(
            final int position,
            final T t,
            final T u,
            final T v
        ) {
            super(position, t, u, v);
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
            return size;
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
    
    /**
     * A union of four values of different types
     */
    public static class UnionFour<T, U, V, W> extends UnionBase<T, U, V, W, Void, Void> {
        UnionFour(
            final int position,
            final T t,
            final U u,
            final V v,
            final W w
        ) {
            super(position, t, u, v, w, null, null);
        }
        
        /**
         * get the position
         * 
         * @return the position of the set value
         */
        public int getPosition() {
            return size;
        }

        /**
         * get first value
         * 
         * @return first value
         * @throws IllegalArgumentException if position != 1
         */
        public T get1() {
            checkUnionPosition(size, 1);
            return t;
        }

        /**
         * get second value
         * 
         * @return second value
         * @throws IllegalArgumentException if position != 2
         */
        public U get2() {
            checkUnionPosition(size, 2);
            return u;
        }

        /**
         * get third value
         * 
         * @return third value
         * @throws IllegalArgumentException if position != 3
         */
        public V get3() {
            checkUnionPosition(size, 3);
            return v;
        }

        /**
         * get fourth value
         * 
         * @return fourth value
         * @throws IllegalArgumentException if position != 4
         */
        public W get4() {
            checkUnionPosition(size, 4);
            return w;
        }
    }

    /**
     * A union of four values of the same type
     */
    public static class UnionFourOf<T> extends UnionFour<T, T, T, T> {
        UnionFourOf(
            final int position,
            final T t,
            final T u,
            final T v,
            final T w
        ) {
            super(position, t, u, v, w);
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
            return size;
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
    
    /**
     * A union of five values of different types
     */
    public static class UnionFive<T, U, V, W, X> extends UnionBase<T, U, V, W, X, Void> {
        UnionFive(
            final int position,
            final T t,
            final U u,
            final V v,
            final W w,
            final X x
        ) {
            super(position, t, u, v, w, x, null);
        }
        
        /**
         * get the position
         * 
         * @return the position of the set value
         */
        public int getPosition() {
            return size;
        }

        /**
         * get first value
         * 
         * @return first value
         * @throws IllegalArgumentException if position != 1
         */
        public T get1() {
            checkUnionPosition(size, 1);
            return t;
        }

        /**
         * get second value
         * 
         * @return second value
         * @throws IllegalArgumentException if position != 2
         */
        public U get2() {
            checkUnionPosition(size, 2);
            return u;
        }

        /**
         * get third value
         * 
         * @return third value
         * @throws IllegalArgumentException if position != 3
         */
        public V get3() {
            checkUnionPosition(size, 3);
            return v;
        }

        /**
         * get fourth value
         * 
         * @return fourth value
         * @throws IllegalArgumentException if position != 4
         */
        public W get4() {
            checkUnionPosition(size, 4);
            return w;
        }

        /**
         * get fifth value
         * 
         * @return fifth value
         * @throws IllegalArgumentException if position != 5
         */
        public X get5() {
            checkUnionPosition(size, 5);
            return x;
        }
    }

    /**
     * A union of five values of the same type
     */
    public static class UnionFiveOf<T> extends UnionFive<T, T, T, T, T> {
        UnionFiveOf(
            final int position,
            final T t,
            final T u,
            final T v,
            final T w,
            final T x
        ) {
            super(position, t, u, v, w, x);
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
            return size;
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
    
    /**
     * A union of six values of different types
     */
    public static class UnionSix<T, U, V, W, X, Y> extends UnionBase<T, U, V, W, X, Y> {
        UnionSix(
            final int position,
            final T t,
            final U u,
            final V v,
            final W w,
            final X x,
            final Y y
        ) {
            super(position, t, u, v, w, x, y);
        }
        
        /**
         * get the position
         * 
         * @return the position of the set value
         */
        public int getPosition() {
            return size;
        }

        /**
         * get first value
         * 
         * @return first value
         * @throws IllegalArgumentException if position != 1
         */
        public T get1() {
            checkUnionPosition(size, 1);
            return t;
        }

        /**
         * get second value
         * 
         * @return second value
         * @throws IllegalArgumentException if position != 2
         */
        public U get2() {
            checkUnionPosition(size, 2);
            return u;
        }

        /**
         * get third value
         * 
         * @return third value
         * @throws IllegalArgumentException if position != 3
         */
        public V get3() {
            checkUnionPosition(size, 3);
            return v;
        }

        /**
         * get fourth value
         * 
         * @return fourth value
         * @throws IllegalArgumentException if position != 4
         */
        public W get4() {
            checkUnionPosition(size, 4);
            return w;
        }

        /**
         * get fifth value
         * 
         * @return fifth value
         * @throws IllegalArgumentException if position != 5
         */
        public X get5() {
            checkUnionPosition(size, 5);
            return x;
        }

        /**
         * get sixth value
         * 
         * @return sixthvalue
         * @throws IllegalArgumentException if position != 6
         */
        public Y get6() {
            checkUnionPosition(size, 6);
            return y;
        }
    }

    /**
     * A union of six values of the same type
     */
    public static class UnionSixOf<T> extends UnionSix<T, T, T, T, T, T> {
        UnionSixOf(
            final int position,
            final T t,
            final T u,
            final T v,
            final T w,
            final T x,
            final T y
        ) {
            super(position, t, u, v, w, x, y);
        }
    }
    
    // ==== Constructor classes
    
    /**
     * A class that provides named constructors for fixed size tuples with single type
     */
    public static class Same {
        // ==== Two named constructors
        
        /**
         * Construct a tuple of two non-null values of the same type
         *  
         * @param <T> type
         * @param t first value
         * @param u second value
         * @return tuple of two values
         */
        public static <T> TwoOf<T> of(
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
        public static <T> TwoOf<T> ofNullable(
            final T t,
            final T u
        ) {
            return new TwoOf<>(
                t,
                u
            );
        }
        
        // ==== Three named constructors
        
        /**
         * Construct a tuple of three non-null values of the same type
         *  
         * @param <T> type
         * @param t first value
         * @param u second value
         * @param v third value
         * @return tuple of three values
         */
        public static <T> ThreeOf<T> of(
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
        public static <T> ThreeOf<T> ofNullable(
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
        
        // ==== Four named constructors
        
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
        public static <T> FourOf<T> of(
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
        public static <T> FourOf<T> ofNullable(
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
        
        // ==== Five named constructors
        
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
        public static <T> FiveOf<T> of(
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
        public static <T> FiveOf<T> ofNullable(
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
        
        // ==== Six named constructors
        
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
        public static <T> SixOf<T> of(
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
        public static <T> SixOf<T> ofNullable(
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
    }

    /**
     * A class that provides named constructors for variable size tuples with different types
     */
    public static class UpTo {
        
        // ==== Two named constructors

        /**
         * Construct a tuple capable of holding up to two values of different types with no values
         *  
         * @param <T> first type
         * @param <U> second type
         * @return tuple of up to two values that is empty
         */
        public static <T, U> UpToTwo<T, U> two(
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
        public static <T, U> UpToTwo<T, U> two(
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
        public static <T, U> UpToTwo<T, U> twoNullable(
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
        public static <T, U> UpToTwo<T, U> two(
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
        public static <T, U> UpToTwo<T, U> twoNullable(
            final T t,
            final U u
        ) {
            return new UpToTwo<>(
                2,
                t,
                u
            );
        }
        
        // ==== Three named constructors
        
        /**
         * Construct a tuple capable of holding up to three values of different types with no values
         *  
         * @param <T> first type
         * @param <U> second type
         * @param <V> third type
         * @return tuple of up to three values that is empty
         */
        public static <T, U, V> UpToThree<T, U, V> three(
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
        public static <T, U, V> UpToThree<T, U, V> three(
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
        public static <T, U, V> UpToThree<T, U, V> threeNullable(
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
        public static <T, U, V> UpToThree<T, U, V> three(
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
        public static <T, U, V> UpToThree<T, U, V> threeNullable(
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
        public static <T, U, V> UpToThree<T, U, V> three(
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
        public static <T, U, V> UpToThree<T, U, V> threeNullable(
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
        
        // ==== Four named constructors

        /**
         * Construct a tuple capable of holding up to four values of different types with no values
         *  
         * @param <T> first type
         * @param <U> second type
         * @param <V> third type
         * @param <W> fourth type
         * @return tuple of up to four values that is empty
         */
        public static <T, U, V, W> UpToFour<T, U, V, W> four(
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
        public static <T, U, V, W> UpToFour<T, U, V, W> four(
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
        public static <T, U, V, W> UpToFour<T, U, V, W> fourNullable(
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
        public static <T, U, V, W> UpToFour<T, U, V, W> four(
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
        public static <T, U, V, W> UpToFour<T, U, V, W> fourNullable(
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
        public static <T, U, V, W> UpToFour<T, U, V, W> four(
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
        public static <T, U, V, W> UpToFour<T, U, V, W> fourNullable(
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
        public static <T, U, V, W> UpToFour<T, U, V, W> four(
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
        public static <T, U, V, W> UpToFour<T, U, V, W> fourNullable(
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
        
        // ==== Five named constructors

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
        public static <T, U, V, W, X> UpToFive<T, U, V, W, X> five(
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
        public static <T, U, V, W, X> UpToFive<T, U, V, W, X> five(
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
        public static <T, U, V, W, X> UpToFive<T, U, V, W, X> fiveNullable(
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
        public static <T, U, V, W, X> UpToFive<T, U, V, W, X> five(
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
        public static <T, U, V, W, X> UpToFive<T, U, V, W, X> fiveNullable(
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
        public static <T, U, V, W, X> UpToFive<T, U, V, W, X> five(
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
        public static <T, U, V, W, X> UpToFive<T, U, V, W, X> fiveNullable(
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
        public static <T, U, V, W, X> UpToFive<T, U, V, W, X> five(
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
        public static <T, U, V, W, X> UpToFive<T, U, V, W, X> fiveNullable(
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
        public static <T, U, V, W, X> UpToFive<T, U, V, W, X> five(
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
        public static <T, U, V, W, X> UpToFive<T, U, V, W, X> fiveNullable(
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
        
        // ==== Six named constructors

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
        public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> six(
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
        public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> six(
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
        public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> sixNullable(
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
        public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> six(
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
        public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> sixNullable(
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
        public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> six(
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
        public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> sixNullable(
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
        public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> six(
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
        public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> sixNullable(
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
        public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> six(
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
        public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> sixNullable(
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
        public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> six(
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
        public static <T, U, V, W, X, Y> UpToSix<T, U, V, W, X, Y> sixNullable(
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
    }

    /**
     * A class that provides named constructors for variable size tuples with a single type
     */
    public static class UpToSame {
        
        // ==== Two named constructors

        /**
         * Construct a tuple capable of holding up to two values of the same type with no values
         *  
         * @param <T> type
         * @return tuple of up to two values that is empty
         */
        public static <T> UpToTwoOf<T> two(
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
        public static <T> UpToTwoOf<T> two(
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
        public static <T> UpToTwoOf<T> twoNullable(
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
        public static <T> UpToTwoOf<T> two(
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
        public static <T> UpToTwoOf<T> twoNullable(
            final T t,
            final T u
        ) {
            return new UpToTwoOf<>(
                2,
                t,
                u
            );
        }
        
        // ==== Three named constructors

        /**
         * Construct a tuple capable of holding up to three values of the same type with no values
         *  
         * @param <T> type
         * @return tuple of up to three values that is empty
         */
        public static <T> UpToThreeOf<T> three(
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
        public static <T> UpToThreeOf<T> three(
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
        public static <T> UpToThreeOf<T> threeNullable(
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
        public static <T> UpToThreeOf<T> three(
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
        public static <T> UpToThreeOf<T> threeNullable(
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
        public static <T> UpToThreeOf<T> three(
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
        public static <T> UpToThreeOf<T> threeNullable(
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
        
        // ==== Four named constructors

        /**
         * Construct a tuple capable of holding up to four values of the same type with no values
         *  
         * @param <T> type
         * @return tuple of up to four values that is empty
         */
        public static <T> UpToFourOf<T> four(
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
        public static <T> UpToFourOf<T> four(
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
        public static <T> UpToFourOf<T> fourNullable(
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
        public static <T> UpToFourOf<T> four(
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
        public static <T> UpToFourOf<T> fourNullable(
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
        public static <T> UpToFourOf<T> four(
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
        public static <T> UpToFourOf<T> fourNullable(
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
        public static <T> UpToFourOf<T> four(
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
        public static <T> UpToFourOf<T> fourNullable(
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
        
        // ==== Five named constructors

        /**
         * Construct a tuple capable of holding up to five values of the same type with no values
         *  
         * @param <T> type
         * @return tuple of up to five values that is empty
         */
        public static <T> UpToFiveOf<T> five(
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
        public static <T> UpToFiveOf<T> five(
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
        public static <T> UpToFiveOf<T> fiveNullable(
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
        public static <T> UpToFiveOf<T> five(
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
        public static <T> UpToFiveOf<T> fiveNullable(
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
        public static <T> UpToFiveOf<T> five(
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
        public static <T> UpToFiveOf<T> fiveNullable(
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
        public static <T> UpToFiveOf<T> five(
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
        public static <T> UpToFiveOf<T> fiveNullable(
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
        public static <T> UpToFiveOf<T> five(
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
        public static <T> UpToFiveOf<T> fiveNullable(
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
        
        // ==== Six named constructors

        /**
         * Construct a tuple capable of holding up to six values of the same type with no values
         *  
         * @param <T> type
         * @return tuple of up to six values that is empty
         */
        public static <T> UpToSixOf<T> six(
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
        public static <T> UpToSixOf<T> six(
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
        public static <T> UpToSixOf<T> sixNullable(
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
        public static <T> UpToSixOf<T> six(
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
        public static <T> UpToSixOf<T> sixNullable(
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
        public static <T> UpToSixOf<T> six(
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
        public static <T> UpToSixOf<T> sixNullable(
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
        public static <T> UpToSixOf<T> six(
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
        public static <T> UpToSixOf<T> sixNullable(
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
        public static <T> UpToSixOf<T> six(
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
        public static <T> UpToSixOf<T> sixNullable(
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
        public static <T> UpToSixOf<T> six(
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
        public static <T> UpToSixOf<T> sixNullable(
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

    /**
     * A class that provides named constructors for unions with different types
     */
    public static class Union {
        
        // ==== Two named constructors
        
        public static <T, U> UnionTwo<T, U> twoFirst(final T t) {
            return new UnionTwo<>(
                1,
                Objects.requireNonNull(t, "t"),
                null
            );
        }
        
        public static <T, U> UnionTwo<T, U> twoFirstNullable(final T t) {
            return new UnionTwo<>(
                1,
                t,
                null
            );
        }
        
        public static <T, U> UnionTwo<T, U> twoSecond(final U u) {
            return new UnionTwo<>(
                2,
                null,
                u
            );
        }
        
        public static <T, U> UnionTwo<T, U> twoSecondNullable(final U u) {
            return new UnionTwo<>(
                2,
                null,
                u
            );
        }
        
        // ==== Three named constructors
        
        public static <T, U, V> UnionThree<T, U, V> threeFirst(final T t) {
            return new UnionThree<>(
                1,
                Objects.requireNonNull(t, "t"),
                null,
                null
            );
        }
        
        public static <T, U, V> UnionThree<T, U, V> threeFirstNullable(final T t) {
            return new UnionThree<>(
                1,
                t,
                null,
                null
            );
        }
        
        public static <T, U, V> UnionThree<T, U, V> threeSecond(final U u) {
            return new UnionThree<>(
                2,
                null,
                Objects.requireNonNull(u, "u"),
                null
            );
        }
        
        public static <T, U, V> UnionThree<T, U, V> threeSecondNullable(final U u) {
            return new UnionThree<>(
                2,
                null,
                u,
                null
            );
        }
        
        public static <T, U, V> UnionThree<T, U, V> threeThird(final V v) {
            return new UnionThree<>(
                3,
                null,
                null,
                Objects.requireNonNull(v, "v")
            );
        }
        
        public static <T, U, V> UnionThree<T, U, V> threeThirdNullable(final V v) {
            return new UnionThree<>(
                3,
                null,
                null,
                v
            );
        }
        
        // ==== Four named constructors
        
        public static <T, U, V, W> UnionFour<T, U, V, W> fourFirst(final T t) {
            return new UnionFour<>(
                1,
                Objects.requireNonNull(t, "t"),
                null,
                null,
                null
            );
        }
        
        public static <T, U, V, W> UnionFour<T, U, V, W> fourFirstNullable(final T t) {
            return new UnionFour<>(
                1,
                t,
                null,
                null,
                null
            );
        }
        
        public static <T, U, V, W> UnionFour<T, U, V, W> fourSecond(final U u) {
            return new UnionFour<>(
                2,
                null,
                Objects.requireNonNull(u, "u"),
                null,
                null
            );
        }
        
        public static <T, U, V, W> UnionFour<T, U, V, W> fourSecondNullable(final U u) {
            return new UnionFour<>(
                2,
                null,
                u,
                null,
                null
            );
        }
        
        public static <T, U, V, W> UnionFour<T, U, V, W> fourThird(final V v) {
            return new UnionFour<>(
                3,
                null,
                null,
                Objects.requireNonNull(v, "v"),
                null
            );
        }
        
        public static <T, U, V, W> UnionFour<T, U, V, W> fourThirdNullable(final V v) {
            return new UnionFour<>(
                3,
                null,
                null,
                v,
                null
            );
        }
        
        public static <T, U, V, W> UnionFour<T, U, V, W> fourFourth(final W w) {
            return new UnionFour<>(
                4,
                null,
                null,
                null,
                Objects.requireNonNull(w, "w")
            );
        }
        
        public static <T, U, V, W> UnionFour<T, U, V, W> fourFourthNullable(final W w) {
            return new UnionFour<>(
                4,
                null,
                null,
                null,
                w
            );
        }
        
        // ==== Five named constructors
        
        public static <T, U, V, W, X> UnionFive<T, U, V, W, X> fiveFirst(final T t) {
            return new UnionFive<>(
                1,
                Objects.requireNonNull(t, "t"),
                null,
                null,
                null,
                null
            );
        }
        
        public static <T, U, V, W, X> UnionFive<T, U, V, W, X> fiveFirstNullable(final T t) {
            return new UnionFive<>(
                1,
                t,
                null,
                null,
                null,
                null
            );
        }
        
        public static <T, U, V, W, X> UnionFive<T, U, V, W, X> fiveSecond(final U u) {
            return new UnionFive<>(
                2,
                null,
                Objects.requireNonNull(u, "u"),
                null,
                null,
                null
            );
        }
        
        public static <T, U, V, W, X> UnionFive<T, U, V, W, X> fiveSecondNullable(final U u) {
            return new UnionFive<>(
                2,
                null,
                u,
                null,
                null,
                null
            );
        }
        
        public static <T, U, V, W, X> UnionFive<T, U, V, W, X> fiveThird(final V v) {
            return new UnionFive<>(
                3,
                null,
                null,
                Objects.requireNonNull(v, "v"),
                null,
                null
            );
        }
        
        public static <T, U, V, W, X> UnionFive<T, U, V, W, X> fiveThirdNullable(final V v) {
            return new UnionFive<>(
                3,
                null,
                null,
                v,
                null,
                null
            );
        }
        
        public static <T, U, V, W, X> UnionFive<T, U, V, W, X> fiveFourth(final W w) {
            return new UnionFive<>(
                4,
                null,
                null,
                null,
                Objects.requireNonNull(w, "w"),
                null
            );
        }
        
        public static <T, U, V, W, X> UnionFive<T, U, V, W, X> fiveFourthNullable(final W w) {
            return new UnionFive<>(
                4,
                null,
                null,
                null,
                w,
                null
            );
        }
        
        public static <T, U, V, W, X> UnionFive<T, U, V, W, X> fiveFifth(final X x) {
            return new UnionFive<>(
                5,
                null,
                null,
                null,
                null,
                Objects.requireNonNull(x, "x")
            );
        }
        
        public static <T, U, V, W, X> UnionFive<T, U, V, W, X> fiveFifthNullable(final X x) {
            return new UnionFive<>(
                5,
                null,
                null,
                null,
                null,
                x
            );
        }
        
        // ==== Six named constructors
        
        public static <T, U, V, W, X, Y> UnionSix<T, U, V, W, X, Y> sixFirst(final T t) {
            return new UnionSix<>(
                1,
                Objects.requireNonNull(t, "t"),
                null,
                null,
                null,
                null,
                null
            );
        }
        
        public static <T, U, V, W, X, Y> UnionSix<T, U, V, W, X, Y> sixFirstNullable(final T t) {
            return new UnionSix<>(
                1,
                t,
                null,
                null,
                null,
                null,
                null
            );
        }
        
        public static <T, U, V, W, X, Y> UnionSix<T, U, V, W, X, Y> sixSecond(final U u) {
            return new UnionSix<>(
                2,
                null,
                Objects.requireNonNull(u, "u"),
                null,
                null,
                null,
                null
            );
        }
        
        public static <T, U, V, W, X, Y> UnionSix<T, U, V, W, X, Y> sixSecondNullable(final U u) {
            return new UnionSix<>(
                2,
                null,
                u,
                null,
                null,
                null,
                null
            );
        }
        
        public static <T, U, V, W, X, Y> UnionSix<T, U, V, W, X, Y> sixThird(final V v) {
            return new UnionSix<>(
                3,
                null,
                null,
                Objects.requireNonNull(v, "v"),
                null,
                null,
                null
            );
        }
        
        public static <T, U, V, W, X, Y> UnionSix<T, U, V, W, X, Y> sixThirdNullable(final V v) {
            return new UnionSix<>(
                3,
                null,
                null,
                v,
                null,
                null,
                null
            );
        }
        
        public static <T, U, V, W, X, Y> UnionSix<T, U, V, W, X, Y> sixFourth(final W w) {
            return new UnionSix<>(
                4,
                null,
                null,
                null,
                Objects.requireNonNull(w, "w"),
                null,
                null
            );
        }
        
        public static <T, U, V, W, X, Y> UnionSix<T, U, V, W, X, Y> sixFourthNullable(final W w) {
            return new UnionSix<>(
                4,
                null,
                null,
                null,
                w,
                null,
                null
            );
        }
        
        public static <T, U, V, W, X, Y> UnionSix<T, U, V, W, X, Y> sixFifth(final X x) {
            return new UnionSix<>(
                5,
                null,
                null,
                null,
                null,
                Objects.requireNonNull(x, "x"),
                null
            );
        }
        
        public static <T, U, V, W, X, Y> UnionSix<T, U, V, W, X, Y> sixFifthNullable(final X x) {
            return new UnionSix<>(
                5,
                null,
                null,
                null,
                null,
                x,
                null
            );
        }
        
        public static <T, U, V, W, X, Y> UnionSix<T, U, V, W, X, Y> sixSixth(final Y y) {
            return new UnionSix<>(
                6,
                null,
                null,
                null,
                null,
                null,
                Objects.requireNonNull(y, "y")
            );
        }
        
        public static <T, U, V, W, X, Y> UnionSix<T, U, V, W, X, Y> sixSixthNullable(final Y y) {
            return new UnionSix<>(
                6,
                null,
                null,
                null,
                null,
                null,
                y
            );
        }
    }

    /**
     * A class that provides named constructors for unions with different types
     */
    public static class UnionSame {
        
        // ==== Two named constructors
        
        public static <T> UnionTwoOf<T> twoFirst(final T t) {
            return new UnionTwoOf<>(
                1,
                Objects.requireNonNull(t, "t"),
                null
            );
        }
        
        public static <T> UnionTwoOf<T> twoFirstNullable(final T t) {
            return new UnionTwoOf<>(
                1,
                t,
                null
            );
        }
        
        public static <T> UnionTwoOf<T> twoSecond(final T u) {
            return new UnionTwoOf<>(
                2,
                null,
                u
            );
        }
        
        public static <T> UnionTwoOf<T> twoSecondNullable(final T u) {
            return new UnionTwoOf<>(
                2,
                null,
                u
            );
        }
        
        // ==== Three named constructors
        
        public static <T> UnionThreeOf<T> threeFirst(final T t) {
            return new UnionThreeOf<>(
                1,
                Objects.requireNonNull(t, "t"),
                null,
                null
            );
        }
        
        public static <T> UnionThreeOf<T> threeFirstNullable(final T t) {
            return new UnionThreeOf<>(
                1,
                t,
                null,
                null
            );
        }
        
        public static <T> UnionThreeOf<T> threeSecond(final T u) {
            return new UnionThreeOf<>(
                2,
                null,
                Objects.requireNonNull(u, "u"),
                null
            );
        }
        
        public static <T> UnionThreeOf<T> threeSecondNullable(final T u) {
            return new UnionThreeOf<>(
                2,
                null,
                u,
                null
            );
        }
        
        public static <T> UnionThreeOf<T> threeThird(final T v) {
            return new UnionThreeOf<>(
                3,
                null,
                null,
                Objects.requireNonNull(v, "v")
            );
        }
        
        public static <T> UnionThreeOf<T> threeThirdNullable(final T v) {
            return new UnionThreeOf<>(
                3,
                null,
                null,
                v
            );
        }
        
        // ==== Four named constructors
        
        public static <T> UnionFourOf<T> fourFirst(final T t) {
            return new UnionFourOf<>(
                1,
                Objects.requireNonNull(t, "t"),
                null,
                null,
                null
            );
        }
        
        public static <T> UnionFourOf<T> fourFirstNullable(final T t) {
            return new UnionFourOf<>(
                1,
                t,
                null,
                null,
                null
            );
        }
        
        public static <T> UnionFourOf<T> fourSecond(final T u) {
            return new UnionFourOf<>(
                2,
                null,
                Objects.requireNonNull(u, "u"),
                null,
                null
            );
        }
        
        public static <T> UnionFourOf<T> fourSecondNullable(final T u) {
            return new UnionFourOf<>(
                2,
                null,
                u,
                null,
                null
            );
        }
        
        public static <T> UnionFourOf<T> fourThird(final T v) {
            return new UnionFourOf<>(
                3,
                null,
                null,
                Objects.requireNonNull(v, "v"),
                null
            );
        }
        
        public static <T> UnionFourOf<T> fourThirdNullable(final T v) {
            return new UnionFourOf<>(
                3,
                null,
                null,
                v,
                null
            );
        }
        
        public static <T> UnionFourOf<T> fourFourth(final T w) {
            return new UnionFourOf<>(
                4,
                null,
                null,
                null,
                Objects.requireNonNull(w, "w")
            );
        }
        
        public static <T> UnionFourOf<T> fourFourthNullable(final T w) {
            return new UnionFourOf<>(
                4,
                null,
                null,
                null,
                w
            );
        }
        
        // ==== Five named constructors
        
        public static <T> UnionFiveOf<T> fiveFirst(final T t) {
            return new UnionFiveOf<>(
                1,
                Objects.requireNonNull(t, "t"),
                null,
                null,
                null,
                null
            );
        }
        
        public static <T> UnionFiveOf<T> fiveFirstNullable(final T t) {
            return new UnionFiveOf<>(
                1,
                t,
                null,
                null,
                null,
                null
            );
        }
        
        public static <T> UnionFiveOf<T> fiveSecond(final T u) {
            return new UnionFiveOf<>(
                2,
                null,
                Objects.requireNonNull(u, "u"),
                null,
                null,
                null
            );
        }
        
        public static <T> UnionFiveOf<T> fiveSecondNullable(final T u) {
            return new UnionFiveOf<>(
                2,
                null,
                u,
                null,
                null,
                null
            );
        }
        
        public static <T> UnionFiveOf<T> fiveThird(final T v) {
            return new UnionFiveOf<>(
                3,
                null,
                null,
                Objects.requireNonNull(v, "v"),
                null,
                null
            );
        }
        
        public static <T> UnionFiveOf<T> fiveThirdNullable(final T v) {
            return new UnionFiveOf<>(
                3,
                null,
                null,
                v,
                null,
                null
            );
        }
        
        public static <T> UnionFiveOf<T> fiveFourth(final T w) {
            return new UnionFiveOf<>(
                4,
                null,
                null,
                null,
                Objects.requireNonNull(w, "w"),
                null
            );
        }
        
        public static <T> UnionFiveOf<T> fiveFourthNullable(final T w) {
            return new UnionFiveOf<>(
                4,
                null,
                null,
                null,
                w,
                null
            );
        }
        
        public static <T> UnionFiveOf<T> fiveFifth(final T x) {
            return new UnionFiveOf<>(
                5,
                null,
                null,
                null,
                null,
                Objects.requireNonNull(x, "x")
            );
        }
        
        public static <T> UnionFiveOf<T> fiveFifthNullable(final T x) {
            return new UnionFiveOf<>(
                5,
                null,
                null,
                null,
                null,
                x
            );
        }
        
        // ==== Six named constructors
        
        public static <T> UnionSixOf<T> sixFirst(final T t) {
            return new UnionSixOf<>(
                1,
                Objects.requireNonNull(t, "t"),
                null,
                null,
                null,
                null,
                null
            );
        }
        
        public static <T> UnionSixOf<T> sixFirstNullable(final T t) {
            return new UnionSixOf<>(
                1,
                t,
                null,
                null,
                null,
                null,
                null
            );
        }
        
        public static <T> UnionSixOf<T> sixSecond(final T u) {
            return new UnionSixOf<>(
                2,
                null,
                Objects.requireNonNull(u, "u"),
                null,
                null,
                null,
                null
            );
        }
        
        public static <T> UnionSixOf<T> sixSecondNullable(final T u) {
            return new UnionSixOf<>(
                2,
                null,
                u,
                null,
                null,
                null,
                null
            );
        }
        
        public static <T> UnionSixOf<T> sixThird(final T v) {
            return new UnionSixOf<>(
                3,
                null,
                null,
                Objects.requireNonNull(v, "v"),
                null,
                null,
                null
            );
        }
        
        public static <T> UnionSixOf<T> sixThirdNullable(final T v) {
            return new UnionSixOf<>(
                3,
                null,
                null,
                v,
                null,
                null,
                null
            );
        }
        
        public static <T> UnionSixOf<T> sixFourth(final T w) {
            return new UnionSixOf<>(
                4,
                null,
                null,
                null,
                Objects.requireNonNull(w, "w"),
                null,
                null
            );
        }
        
        public static <T> UnionSixOf<T> sixFourthNullable(final T w) {
            return new UnionSixOf<>(
                4,
                null,
                null,
                null,
                w,
                null,
                null
            );
        }
        
        public static <T> UnionSixOf<T> sixFifth(final T x) {
            return new UnionSixOf<>(
                5,
                null,
                null,
                null,
                null,
                Objects.requireNonNull(x, "x"),
                null
            );
        }
        
        public static <T> UnionSixOf<T> sixFifthNullable(final T x) {
            return new UnionSixOf<>(
                5,
                null,
                null,
                null,
                null,
                x,
                null
            );
        }
        
        public static <T> UnionSixOf<T> sixSixth(final T y) {
            return new UnionSixOf<>(
                6,
                null,
                null,
                null,
                null,
                null,
                Objects.requireNonNull(y, "y")
            );
        }
        
        public static <T> UnionSixOf<T> sixSixthNullable(final T y) {
            return new UnionSixOf<>(
                6,
                null,
                null,
                null,
                null,
                null,
                y
            );
        }
    }
    
    // ==== Named constructors for fixed tuples with different types
    
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
}
