package me.bantling.micro.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Collections {
    private Collections() {
        throw new RuntimeException();
    }

    /**
     * Build a list of hard-coded items of any length, in batches of 1 to 10 items.
     * 
     * @param <E> the type of element to build a list of
     */
    public static class ListBuilder<E> {
        private final List<E> list;
        
        ListBuilder(
            final List<E> list
        ) {
            this.list = list;
        }
        
        public ListBuilder<E> add(
            final E e
        ) {
            list.add(e);
            return this;
        }
        
        public ListBuilder<E> add(
            final E e1,
            final E e2
        ) {
            list.add(e1);
            list.add(e2);
            return this;
        }
        
        public ListBuilder<E> add(
            final E e1,
            final E e2,
            final E e3
        ) {
            list.add(e1);
            list.add(e2);
            list.add(e3);
            return this;
        }
        
        public ListBuilder<E> add(
            final E e1,
            final E e2,
            final E e3,
            final E e4
        ) {
            list.add(e1);
            list.add(e2);
            list.add(e3);
            list.add(e4);
            return this;
        }
        
        public ListBuilder<E> add(
            final E e1,
            final E e2,
            final E e3,
            final E e4,
            final E e5
        ) {
            list.add(e1);
            list.add(e2);
            list.add(e3);
            list.add(e4);
            list.add(e5);
            return this;
        }
        
        public ListBuilder<E> add(
            final E e1,
            final E e2,
            final E e3,
            final E e4,
            final E e5,
            final E e6
        ) {
            list.add(e1);
            list.add(e2);
            list.add(e3);
            list.add(e4);
            list.add(e5);
            list.add(e6);
            return this;
        }
        
        public ListBuilder<E> add(
            final E e1,
            final E e2,
            final E e3,
            final E e4,
            final E e5,
            final E e6,
            final E e7
        ) {
            list.add(e1);
            list.add(e2);
            list.add(e3);
            list.add(e4);
            list.add(e5);
            list.add(e6);
            list.add(e7);
            return this;
        }
        
        public ListBuilder<E> add(
            final E e1,
            final E e2,
            final E e3,
            final E e4,
            final E e5,
            final E e6,
            final E e7,
            final E e8
        ) {
            list.add(e1);
            list.add(e2);
            list.add(e3);
            list.add(e4);
            list.add(e5);
            list.add(e6);
            list.add(e7);
            list.add(e8);
            return this;
        }
        
        public ListBuilder<E> add(
            final E e1,
            final E e2,
            final E e3,
            final E e4,
            final E e5,
            final E e6,
            final E e7,
            final E e8,
            final E e9
        ) {
            list.add(e1);
            list.add(e2);
            list.add(e3);
            list.add(e4);
            list.add(e5);
            list.add(e6);
            list.add(e7);
            list.add(e8);
            list.add(e9);
            return this;
        }
        
        public ListBuilder<E> add(
            final E e1,
            final E e2,
            final E e3,
            final E e4,
            final E e5,
            final E e6,
            final E e7,
            final E e8,
            final E e9,
            final E e10
        ) {
            list.add(e1);
            list.add(e2);
            list.add(e3);
            list.add(e4);
            list.add(e5);
            list.add(e6);
            list.add(e7);
            list.add(e8);
            list.add(e9);
            list.add(e10);
            return this;
        }
        
        /**
         * Add all the elements of a collection to this list.
         * 
         * @param c the collection of elements to add 
         * @return this builder
         */
        public ListBuilder<E> add(
            final Collection<E> c
        ) {
            list.addAll(c);
            return this;
        }
        
        public List<E> done() {
            return list;
        }
        
        public List<E> toUnmodifiableList() {
            return java.util.Collections.unmodifiableList(list);
        }
    }
    
    /**
     * Build a list of items of type T into the given list.
     * Duplicates values can be added.
     * 
     * @param <E> the type of element to add to the list
     * @param list the list to add elements to
     * @return a builder for the given list
     */
    public static <E> ListBuilder<E> listOf(final List<E> list) {
        return new ListBuilder<>(list);
    }

    /**
     * Build a set of hard-coded items of any length, in batches of 1 to 10 items.
     * Duplicate values result in an error, as they are considered a mistake in the coding.
     * 
     * @param <E> the type of element to build a set of
     * @throws {@link IllegalArgumentException} if duplicate values are added 
     */
    public static class SetBuilder<E> {
        /**
         * The error message format for adding duplicate set values
         */
        static final String DUPLICATE_VALUE_MSG = "Duplicate element: %s";
        
        private final Set<E> set;
        
        SetBuilder(
            final Set<E> list
        ) {
            this.set = list;
        }
        
        /**
         * Add one item to the set, and throw an {@link IllegalArgumentException} if it is a duplicate
         * 
         * @param e the element to add
         */
        void addOne(final E e) {
            if (! set.add(e)) {
                throw new IllegalArgumentException(String.format(DUPLICATE_VALUE_MSG, e));
            }
        }
        
        public SetBuilder<E> add(
            final E e
        ) {
            addOne(e);
            return this;
        }
        
        public SetBuilder<E> add(
            final E e1,
            final E e2
        ) {
            addOne(e1);
            addOne(e2);
            return this;
        }
        
        public SetBuilder<E> add(
            final E e1,
            final E e2,
            final E e3
        ) {
            addOne(e1);
            addOne(e2);
            addOne(e3);
            return this;
        }
        
        public SetBuilder<E> add(
            final E e1,
            final E e2,
            final E e3,
            final E e4
        ) {
            addOne(e1);
            addOne(e2);
            addOne(e3);
            addOne(e4);
            return this;
        }
        
        public SetBuilder<E> add(
            final E e1,
            final E e2,
            final E e3,
            final E e4,
            final E e5
        ) {
            addOne(e1);
            addOne(e2);
            addOne(e3);
            addOne(e4);
            addOne(e5);
            return this;
        }
        
        public SetBuilder<E> add(
            final E e1,
            final E e2,
            final E e3,
            final E e4,
            final E e5,
            final E e6
        ) {
            addOne(e1);
            addOne(e2);
            addOne(e3);
            addOne(e4);
            addOne(e5);
            addOne(e6);
            return this;
        }
        
        public SetBuilder<E> add(
            final E e1,
            final E e2,
            final E e3,
            final E e4,
            final E e5,
            final E e6,
            final E e7
        ) {
            addOne(e1);
            addOne(e2);
            addOne(e3);
            addOne(e4);
            addOne(e5);
            addOne(e6);
            addOne(e7);
            return this;
        }
        
        public SetBuilder<E> add(
            final E e1,
            final E e2,
            final E e3,
            final E e4,
            final E e5,
            final E e6,
            final E e7,
            final E e8
        ) {
            addOne(e1);
            addOne(e2);
            addOne(e3);
            addOne(e4);
            addOne(e5);
            addOne(e6);
            addOne(e7);
            addOne(e8);
            return this;
        }
        
        public SetBuilder<E> add(
            final E e1,
            final E e2,
            final E e3,
            final E e4,
            final E e5,
            final E e6,
            final E e7,
            final E e8,
            final E e9
        ) {
            addOne(e1);
            addOne(e2);
            addOne(e3);
            addOne(e4);
            addOne(e5);
            addOne(e6);
            addOne(e7);
            addOne(e8);
            addOne(e9);
            return this;
        }
        
        public SetBuilder<E> add(
            final E e1,
            final E e2,
            final E e3,
            final E e4,
            final E e5,
            final E e6,
            final E e7,
            final E e8,
            final E e9,
            final E e10
        ) {
            addOne(e1);
            addOne(e2);
            addOne(e3);
            addOne(e4);
            addOne(e5);
            addOne(e6);
            addOne(e7);
            addOne(e8);
            addOne(e9);
            addOne(e10);
            return this;
        }
        
        /**
         * Add all the elemnents of another collection to this set.
         * 
         * @param c collection of elements to add 
         * @return this builder
         */
        public SetBuilder<E> add(
            final Collection<E> c
        ) {
            for (final E e : c) {
                addOne(e);
            }
            return this;
        }
        
        public Set<E> done() {
            return set;
        }
        
        public Set<E> toUnmodifiableSet() {
            return java.util.Collections.unmodifiableSet(set);
        }
    }
    
    /**
     * Build a set of items of type T into the given set
     * 
     * @param <E> the type of element to build a set of
     * @param set the set to add elements into
     * @return a builder for the given set
     */
    public static <E> SetBuilder<E> setOf(final Set<E> set) {
        return new SetBuilder<>(set);
    }

    /**
     * Build a map of hard-coded key value pairs of any length, in batches of 1 to 10 pairs.
     * Duplicate keys result in an error, as they are considered a mistake in the coding.
     * 
     * @param <K> the key type
     * @param <V> the value type
     * @throws {@link IllegalArgumentException} if duplicate keys are added
     */
    public static class MapBuilder<K, V> {
        /**
         * The error message format for adding duplicate map keys
         */
        static final String DUPLICATE_KEY_MSG = "Duplicate key: %s";
        
        private final Map<K, V> map;
        
        MapBuilder(
            final Map<K, V> map
        ) {
            this.map = map;
        }
        
        private void addOne(final K k, final V v) {
            if (map.put(k, v) != null) {
                throw new IllegalArgumentException(String.format(DUPLICATE_KEY_MSG, k));
            }
        }
        
        public MapBuilder<K, V> add(
            final K k, final V v
        ) {
            addOne(k, v);
            return this;
        }
        
        public MapBuilder<K, V> add(
            final K k1, final V v1,
            final K k2, final V v2
        ) {
            addOne(k1, v1);
            addOne(k2, v2);
            return this;
        }
        
        public MapBuilder<K, V> add(
            final K k1, final V v1,
            final K k2, final V v2,
            final K k3, final V v3
        ) {
            addOne(k1, v1);
            addOne(k2, v2);
            addOne(k3, v3);
            return this;
        }
        
        public MapBuilder<K, V> add(
            final K k1, final V v1,
            final K k2, final V v2,
            final K k3, final V v3,
            final K k4, final V v4
        ) {
            addOne(k1, v1);
            addOne(k2, v2);
            addOne(k3, v3);
            addOne(k4, v4);
            return this;
        }
        
        public MapBuilder<K, V> add(
            final K k1, final V v1,
            final K k2, final V v2,
            final K k3, final V v3,
            final K k4, final V v4,
            final K k5, final V v5
        ) {
            addOne(k1, v1);
            addOne(k2, v2);
            addOne(k3, v3);
            addOne(k4, v4);
            addOne(k5, v5);
            return this;
        }
        
        public MapBuilder<K, V> add(
            final K k1, final V v1,
            final K k2, final V v2,
            final K k3, final V v3,
            final K k4, final V v4,
            final K k5, final V v5,
            final K k6, final V v6
        ) {
            addOne(k1, v1);
            addOne(k2, v2);
            addOne(k3, v3);
            addOne(k4, v4);
            addOne(k5, v5);
            addOne(k6, v6);
            return this;
        }
        
        public MapBuilder<K, V> add(
            final K k1, final V v1,
            final K k2, final V v2,
            final K k3, final V v3,
            final K k4, final V v4,
            final K k5, final V v5,
            final K k6, final V v6,
            final K k7, final V v7
        ) {
            addOne(k1, v1);
            addOne(k2, v2);
            addOne(k3, v3);
            addOne(k4, v4);
            addOne(k5, v5);
            addOne(k6, v6);
            addOne(k7, v7);
            return this;
        }
        
        public MapBuilder<K, V> add(
            final K k1, final V v1,
            final K k2, final V v2,
            final K k3, final V v3,
            final K k4, final V v4,
            final K k5, final V v5,
            final K k6, final V v6,
            final K k7, final V v7,
            final K k8, final V v8
        ) {
            addOne(k1, v1);
            addOne(k2, v2);
            addOne(k3, v3);
            addOne(k4, v4);
            addOne(k5, v5);
            addOne(k6, v6);
            addOne(k7, v7);
            addOne(k8, v8);
            return this;
        }
        
        public MapBuilder<K, V> add(
            final K k1, final V v1,
            final K k2, final V v2,
            final K k3, final V v3,
            final K k4, final V v4,
            final K k5, final V v5,
            final K k6, final V v6,
            final K k7, final V v7,
            final K k8, final V v8,
            final K k9, final V v9
        ) {
            addOne(k1, v1);
            addOne(k2, v2);
            addOne(k3, v3);
            addOne(k4, v4);
            addOne(k5, v5);
            addOne(k6, v6);
            addOne(k7, v7);
            addOne(k8, v8);
            addOne(k9, v9);
            return this;
        }
        
        public MapBuilder<K, V> add(
            final K k1, final V v1,
            final K k2, final V v2,
            final K k3, final V v3,
            final K k4, final V v4,
            final K k5, final V v5,
            final K k6, final V v6,
            final K k7, final V v7,
            final K k8, final V v8,
            final K k9, final V v9,
            final K k10, final V v10
        ) {
            addOne(k1, v1);
            addOne(k2, v2);
            addOne(k3, v3);
            addOne(k4, v4);
            addOne(k5, v5);
            addOne(k6, v6);
            addOne(k7, v7);
            addOne(k8, v8);
            addOne(k9, v9);
            addOne(k10, v10);
            return this;
        }
        
        /**
         * Add all the key/value pairs of another map to this map.
         * 
         * @param m map of key/value pairs to add
         * @return this builder
         */
        public MapBuilder<K, V> addAll(
            final Map<K, V> m
        ) {
            for (final Map.Entry<K, V> e : m.entrySet()) {
                addOne(e.getKey(), e.getValue());
            }
            
            return this;
        }
        
        public Map<K, V> done() {
            return map;
        }
        
        public Map<K, V> toUnmodifiableMap() {
            return java.util.Collections.unmodifiableMap(map);
        }
    }
    
    /**
     * Build a map of K, V pairs into the given map
     * 
     * @param <K> the key type
     * @param <V> the value type
     * @param map the map to build
     * @return a builder for the given map
     */
    public static <K, V> MapBuilder<K, V> mapOf(final Map<K, V> map) {
        return new MapBuilder<>(map);
    }
    
    /**
     * Remove all instances of an element in a collection.
     * 
     * @param <E> the type of element
     * @param c the collection to remove elements from
     * @param element the element to remove
     */
    public static <E> void removeAll(final Collection<E> c, final E element) {
        for (final Iterator<E> iter = c.iterator(); iter.hasNext(); iter.next()) {
            if (iter.next().equals(element)) {
                iter.remove();
            }
        }
    }
}
