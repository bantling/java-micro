package me.bantling.micro.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public final class Collections {
    private Collections() {
        throw new RuntimeException();
    }
    
    /**
     * Create a {@link ArrayList} of the items given.
     * 
     * @param <E> the type of elements to add to the list
     * @param firstElement the first element to add
     * @param moreElements additional elements to add, if any
     * @return a list of all the elements
     */
    @SafeVarargs
    public static <E> List<E> listOf(
        final E firstElement,
        final E... moreElements
    ) {
        final List<E> list = new ArrayList<>(1 + moreElements.length);
        list.add(firstElement);
        for (final E nextElement : moreElements) {
            list.add(nextElement);
        }
        
        return list;
    }
    
    /**
     * Create an unmodifiable list of the items given.
     * 
     * @param <E> the type of elements to add to the list
     * @param firstElement the first element to add
     * @param moreElements additional elements to add, if any
     * @return an unmodifiable list of all the elements
     */
    @SafeVarargs
    public static <E> List<E> unmodifiableListOf(
        final E firstElement,
        final E... moreElements
    ) {
        return java.util.Collections.unmodifiableList(listOf(firstElement, moreElements));
    }
    
    /**
     * The error message format for adding duplicate set values
     */
    static final String DUPLICATE_VALUE_MSG = "Duplicate element: %s";
        
    /**
     * Add one item to the set, throwing an {@link IllegalArgumentException} if it is a duplicate
     * 
     * @param <E> the type of element to add
     * @param set the set to add the element to
     * @param e the element to add
     */
    static <E> void addOne(final Set<E> set, final E e) {
        if (! set.add(e)) {
            throw new IllegalArgumentException(String.format(DUPLICATE_VALUE_MSG, e));
        }
    }
    
    /**
     * Add all the elements to the set, throwing an {@link IllegalArgumentException} if any item is a duplicate
     * 
     * @param <E> the type of elements to add
     * @param set the set to add the elements to
     * @param firstElement the first element to add
     * @param moreElements additional elements to add
     * @return the given set, with all the elements added
     */
    static <E> Set<E> addElements(
        final Set<E> set,
        final E firstElement,
        final E[] moreElements
    ) {
        addOne(set, firstElement);
        for (final E element : moreElements) {
            addOne(set, element);
        }
        
        return set;
    }
    
    /**
     * Create a {@link HashSet} of the items given.
     * 
     * @param <E> the type of elements to add
     * @param firstElement the first element to add
     * @param moreElements additional elements to add
     * @return a set of all the elements
     * @throws IllegalArgumentException if a duplicate value is provided
     */
    @SafeVarargs
    public static <E> Set<E> setOf(
        final E firstElement,
        final E... moreElements
    ) {
        return addElements(
            new HashSet<>(),
            firstElement,
            moreElements
        );
    }
    
    /**
     * Create an unmodifiable set of the items given.
     * 
     * @param <E> the type of elements to add
     * @param firstElement the first element to add
     * @param moreElements additional elements to add
     * @return an unmodifiable set of all the elements
     * @throws IllegalArgumentException if a duplicate value is provided
     */
    @SafeVarargs
    public static <E> Set<E> unmodifiableSetOf(
        final E firstElement,
        final E... moreElements
    ) {
        return java.util.Collections.unmodifiableSet(setOf(firstElement, moreElements));
    }
    
    /**
     * Create a {@link LinkedHashSet} of the items given.
     * 
     * @param <E> the type of elements to add
     * @param firstElement the first element to add
     * @param moreElements additional elements to add
     * @return a set of all the elements that iterate in the order given
     * @throws IllegalArgumentException if a duplicate value is provided
     */
    @SafeVarargs
    public static <E> Set<E> orderedSetOf(
        final E firstElement,
        final E... moreElements
    ) {
        return addElements(
            new LinkedHashSet<>(),
            firstElement,
            moreElements
        );
    }
    
    /**
     * Create an unmodifiable {@link LinkedHashSet} of items.
     * 
     * @param <E> the type of elements to add
     * @param firstElement the first element to add
     * @param moreElements additional elements to add
     * @return an unmodifiable set of all the elements that iterate in the order given
     * @throws IllegalArgumentException if a duplicate value is provided
     */
    @SafeVarargs
    public static <E> Set<E> unmodifiableOrderedSetOf(
        final E firstElement,
        final E... moreElements
    ) {
        return java.util.Collections.unmodifiableSet(orderedSetOf(firstElement, moreElements));
    }
    
    /**
     * Create a {@link TreeSet} of the items given.
     * 
     * @param <E> the type of elements to add
     * @param firstElement the first element to add
     * @param moreElements additional elements to add
     * @return a set of all the elements that iterate in the order given
     * @throws IllegalArgumentException if a duplicate value is provided
     */
    @SafeVarargs
    public static <E> Set<E> sortedSetOf(
        final E firstElement,
        final E... moreElements
    ) {
        return addElements(
            new TreeSet<>(),
            firstElement,
            moreElements
        );
    }
    
    /**
     * Create an unmodifiable {@link TreeSet} of items.
     * 
     * @param <E> the type of elements to add
     * @param firstElement the first element to add
     * @param moreElements additional elements to add
     * @return an unmodifiable set of all the elements that iterate in the order given
     * @throws IllegalArgumentException if a duplicate value is provided
     */
    @SafeVarargs
    public static <E> Set<E> unmodifiableSortedSetOf(
        final E firstElement,
        final E... moreElements
    ) {
        return java.util.Collections.unmodifiableSet(sortedSetOf(firstElement, moreElements));
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
     * Build a map of K, V pairs into a {@list HashMap}.
     * 
     * @param <K> the key type
     * @param <V> the value type
     * @return a builder for the given map
     */
    public static <K, V> MapBuilder<K, V> map() {
        return new MapBuilder<>(new HashMap<>());
    }
    
    /**
     * Build a map of K, V pairs into the given map
     * 
     * @param <K> the key type
     * @param <V> the value type
     * @param map the map to build
     * @return a builder for the given map
     */
    public static <K, V> MapBuilder<K, V> map(final Map<K, V> map) {
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
