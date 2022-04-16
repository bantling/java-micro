package me.bantling.micro.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public final class Collections {
    private Collections() {
        throw new RuntimeException();
    }
    
    // ==== List
    
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
     * Create a {@link ArrayList} of the lists given.
     * 
     * @param <E> the type of elements to add to the list
     * @param firstList the first list to add
     * @param moreLists additional lists to add, if any
     * @return a list of all the elements of all the given lists
     */
    @SafeVarargs
    public static <E> List<E> listOf(
        final List<E> firstList,
        final List<E>... moreLists
    ) {
        int size = firstList.size();
        for (final List<E> l : moreLists) {
            size += l.size();
        }
        
        final List<E> list = new ArrayList<>(size);
        list.addAll(firstList);
        for (final List<E> nextList: moreLists) {
            list.addAll(nextList);
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
        final List<E> firstList,
        final List<E>... moreLists
    ) {
        return java.util.Collections.unmodifiableList(listOf(firstList, moreLists));
    }
    
    // ==== Set
    
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
     * Add the elements of firstSet and all moreSets sets to a set. 
     * @param <E> the type of elements to add
     * @param set the set to add the elements to
     * @param firstSet the first set to add the elements of
     * @param moreSets additional sets to add the elements of
     * @return the given set, with all the elements of the other sets added
     */
    static <E> Set<E> addSets(
        final Set<E> set,
        final Set<E> firstSet,
        final Set<E>[] moreSets
    ) {
        for (final E element : firstSet) {
            addOne(set, element);
        }
        for (final Set<E> s : moreSets) {
            for (final E element : s) {
                addOne(set, element);
            }
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
     * Create a {@link HashSet} of the sets given.
     * 
     * @param <E> the type of elements to add
     * @param firstSet the first set to add
     * @param moreSets additional sets to add
     * @return a set of all the elements of the given sets
     * @throws IllegalArgumentException if a duplicate value is provided
     */
    @SafeVarargs
    public static <E> Set<E> setOf(
        final Set<E> firstSet,
        final Set<E>... moreSets
    ) {
        return addSets(new HashSet<>(), firstSet, moreSets);
    }
    
    /**
     * Create an unmodifiable set of the sets given.
     * 
     * @param <E> the type of elements to add
     * @param firstSet the first set to add
     * @param moreSets additional sets to add
     * @return a set of all the elements of the given sets
     * @throws IllegalArgumentException if a duplicate value is provided
     */
    @SafeVarargs
    public static <E> Set<E> unmodifiableSetOf(
        final Set<E> firstSet,
        final Set<E>... moreSets
    ) {
        return java.util.Collections.unmodifiableSet(setOf(firstSet, moreSets));
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
     * Create a {@link LinkedHashSet} of the sets given.
     * 
     * @param <E> the type of elements to add
     * @param firstSet the first set to add
     * @param moreSets additional sets to add
     * @return a set of all the elements of the given sets
     * @throws IllegalArgumentException if a duplicate value is provided
     */
    @SafeVarargs
    public static <E> Set<E> orderedSetOf(
        final Set<E> firstSet,
        final Set<E>... moreSets
    ) {
        return addSets(new LinkedHashSet<>(), firstSet, moreSets);
    }
    
    /**
     * Create an unmodifiable set of the sets given.
     * 
     * @param <E> the type of elements to add
     * @param firstSet the first set to add
     * @param moreSets additional sets to add
     * @return a set of all the elements of the given sets
     * @throws IllegalArgumentException if a duplicate value is provided
     */
    @SafeVarargs
    public static <E> Set<E> unmodifiableOrderedSetOf(
        final Set<E> firstSet,
        final Set<E>... moreSets
    ) {
        return java.util.Collections.unmodifiableSet(orderedSetOf(firstSet, moreSets));
    }
    
    /**
     * Create a {@link TreeSet} of the items given.
     * 
     * @param <E> the type of elements to add
     * @param firstElement the first element to add
     * @param moreElements additional elements to add
     * @return a set of all the elements that iterate in sorted order
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
     * @return an unmodifiable set of all the elements that iterate in sorted order
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
     * Create a {@link LinkedHashSet} of the sets given.
     * 
     * @param <E> the type of elements to add
     * @param firstSet the first set to add
     * @param moreSets additional sets to add
     * @return a set of all the elements of the given sets
     * @throws IllegalArgumentException if a duplicate value is provided
     */
    @SafeVarargs
    public static <E> Set<E> sortedSetOf(
        final Set<E> firstSet,
        final Set<E>... moreSets
    ) {
        return addSets(new TreeSet<>(), firstSet, moreSets);
    }
    
    /**
     * Create an unmodifiable set of the sets given.
     * 
     * @param <E> the type of elements to add
     * @param firstSet the first set to add
     * @param moreSets additional sets to add
     * @return a set of all the elements of the given sets
     * @throws IllegalArgumentException if a duplicate value is provided
     */
    @SafeVarargs
    public static <E> Set<E> unmodifiableSortedSetOf(
        final Set<E> firstSet,
        final Set<E>... moreSets
    ) {
        return java.util.Collections.unmodifiableSet(sortedSetOf(firstSet, moreSets));
    }
    
    // ==== Map
    
    /**
     * The error message format for adding duplicate keys
     */
    static final String DUPLICATE_KEY_MSG = "Duplicate key: %s";
        
    /**
     * Add one key/value pair to the map, throwing an {@link IllegalArgumentException} if it is a key
     * 
     * @param <K> the type of key to add
     * @param <V> the type of value to add
     * @param map the map to add the key/value pair to
     * @param k the key to add
     * @param v the value for the added key
     */
    static <K, V> void addOne(final Map<K, V> map, final K k, final V v) {
        if (map.containsKey(k)) {
            throw new IllegalArgumentException(String.format(DUPLICATE_KEY_MSG, k));
        }
        
        map.put(k, v);
    }
    
    /**
     * Add all the elements to the map, throwing an {@link IllegalArgumentException} if any item is a duplicate
     * 
     * @param <K> the type of key to add
     * @param <V> the type of value to add
     * @param map the map to add the key/value pair to
     * @param firstPair the first key/value pair to add
     * @param morePairs additional key/value pairs to add
     * @return the given map, with all the key/value pairs added
     */
    static <K, V> Map<K, V> addPairs(
        final Map<K, V> map,
        final Tuple.Two<K, V> firstPair,
        final Tuple.Two<K, V>[] morePairs
    ) {
        addOne(map, firstPair.get1(), firstPair.get2());
        for (final Tuple.Two<K, V> pair : morePairs) {
            addOne(map, pair.get1(), pair.get2());
        }
        
        return map;
    }
    
    /**
     * 
     * @param <K> the type of key to add
     * @param <V> the type of value to add
     * @param map the map to add the key/value pairs to
     * @param firstMap the first map to add the key/value pairs of
     * @param moreMaps additional maops to add the key/value pairs of
     * @return the given map, with all the key/value pairs of all the maps added
     */
    static <K, V> Map<K, V> addMaps(
        final Map<K, V> map,
        final Map<K, V> firstMap,
        final Map<K, V>[] moreMaps
    ) {
        for (final Map.Entry<K, V> entry : firstMap.entrySet()) {
            addOne(map, entry.getKey(), entry.getValue());
        }
        for (final Map<K, V> m : moreMaps) {
            for (final Map.Entry<K, V> entry : m.entrySet()) {
                addOne(map, entry.getKey(), entry.getValue());
            }
        }
        
        return map;
    }
    
    /**
     * Create a {@link HashMap} of the key/value pairs given.
     * 
     * @param <K> the type of keys to add
     * @param <V> the type of values to add
     * @param firstPair the first key/value pair to add
     * @param morePairs additional key/value pairs to add
     * @return a map of all the elements
     * @throws IllegalArgumentException if a duplicate key is provided
     */
    @SafeVarargs
    public static <K, V> Map<K, V> mapOf(
        final Tuple.Two<K, V> firstPair,
        final Tuple.Two<K, V>... morePairs
    ) {
        return addPairs(
            new HashMap<>(),
            firstPair,
            morePairs
        );
    }
    
    /**
     * Create an unmodifiable map of the key/value pairs given.
     * 
     * @param <K> the type of keys to add
     * @param <V> the type of values to add
     * @param firstPair the first key/value pair to add
     * @param morePairs additional key/value pairs to add
     * @return a map of all the elements
     * @throws IllegalArgumentException if a duplicate key is provided
     */
    @SafeVarargs
    public static <K, V> Map<K, V> unmodifiableMapOf(
        final Tuple.Two<K, V> firstPair,
        final Tuple.Two<K, V>... morePairs
    ) {
        return java.util.Collections.unmodifiableMap(mapOf(firstPair, morePairs));
    }
    
    /**
     * Create a {@link HashMap} of the key/value pairs of all the given maps.
     * 
     * @param <K> the type of keys to add
     * @param <V> the type of values to add
     * @param firstMap the first map to add the key/value pairs of
     * @param moreMaps additional maps to add the key/value pairs of
     * @return a map of all the elements
     * @throws IllegalArgumentException if a duplicate key is provided
     */
    @SafeVarargs
    public static <K, V> Map<K, V> mapOf(
        final Map<K, V> firstMap,
        final Map<K, V>... moreMaps
    ) {
        return addMaps(new HashMap<>(), firstMap, moreMaps);
    }
    
    /**
     * Create an unmodifiable map of the key/value pairs given.
     * 
     * @param <K> the type of keys to add
     * @param <V> the type of values to add
     * @param firstMap the first mapo to add the key/value pairs of
     * @param moreMaps additional maps to add the key/value pairs of
     * @return a map of all the elements
     * @throws IllegalArgumentException if a duplicate key is provided
     */
    @SafeVarargs
    public static <K, V> Map<K, V> unmodifiableMapOf(
        final Map<K, V> firstMap,
        final Map<K, V>... moreMaps
    ) {
        return java.util.Collections.unmodifiableMap(mapOf(firstMap, moreMaps));
    }
    
    /**
     * Create a {@link LinkedHashMap} of the key/value pairs given.
     * 
     * @param <K> the type of keys to add
     * @param <V> the type of values to add
     * @param firstPair the first key/value pair to add
     * @param morePairs additional key/value pairs to add
     * @return a map of all the elements that iterate in the order given
     * @throws IllegalArgumentException if a duplicate key is provided
     */
    @SafeVarargs
    public static <K, V> Map<K, V> orderedMapOf(
        final Tuple.Two<K, V> firstPair,
        final Tuple.Two<K, V>... morePairs
    ) {
        return addPairs(
            new LinkedHashMap<>(),
            firstPair,
            morePairs
        );
    }
    
    /**
     * Create an unmodifiable {@link LinkedHashMap} of the key/value pairs given.
     * 
     * @param <K> the type of keys to add
     * @param <V> the type of values to add
     * @param firstPair the first key/value pair to add
     * @param morePairs additional key/value pairs to add
     * @return a map of all the elements
     * @throws IllegalArgumentException if a duplicate key is provided
     */
    @SafeVarargs
    public static <K, V> Map<K, V> unmodifiableOrderedMapOf(
        final Tuple.Two<K, V> firstPair,
        final Tuple.Two<K, V>... morePairs
    ) {
        return java.util.Collections.unmodifiableMap(orderedMapOf(firstPair, morePairs));
    }
    
    /**
     * Create a {@link LinkedHashMap} of the key/value pairs of all the given maps.
     * 
     * @param <K> the type of keys to add
     * @param <V> the type of values to add
     * @param firstMap the first map to add the key/value pairs of
     * @param moreMaps additional maps to add the key/value pairs of
     * @return a map of all the elements
     * @throws IllegalArgumentException if a duplicate key is provided
     */
    @SafeVarargs
    public static <K, V> Map<K, V> orderedMapOf(
        final Map<K, V> firstMap,
        final Map<K, V>... moreMaps
    ) {
        return addMaps(new LinkedHashMap<>(), firstMap, moreMaps);
    }
    
    /**
     * Create an unmodifiable ordered map of the key/value pairs given.
     * 
     * @param <K> the type of keys to add
     * @param <V> the type of values to add
     * @param firstMap the first mapo to add the key/value pairs of
     * @param moreMaps additional maps to add the key/value pairs of
     * @return a map of all the elements
     * @throws IllegalArgumentException if a duplicate key is provided
     */
    @SafeVarargs
    public static <K, V> Map<K, V> unmodifiableOrderedMapOf(
        final Map<K, V> firstMap,
        final Map<K, V>... moreMaps
    ) {
        return java.util.Collections.unmodifiableMap(orderedMapOf(firstMap, moreMaps));
    }
    
    /**
     * Create a {@link TreeMap} of the key/value pairs given.
     * 
     * @param <K> the type of keys to add
     * @param <V> the type of values to add
     * @param firstPair the first key/value pair to add
     * @param morePairs additional key/value pairs to add
     * @return a map of all the elements that iterate in sorted order
     * @throws IllegalArgumentException if a duplicate key is provided
     */
    @SafeVarargs
    public static <K, V> Map<K, V> sortedMapOf(
        final Tuple.Two<K, V> firstPair,
        final Tuple.Two<K, V>... morePairs
    ) {
        return addPairs(
            new TreeMap<>(),
            firstPair,
            morePairs
        );
    }
    
    /**
     * Create an unmodifiable {@link TreeMap} of the key/value pairs given.
     * 
     * @param <K> the type of keys to add
     * @param <V> the type of values to add
     * @param firstPair the first key/value pair to add
     * @param morePairs additional key/value pairs to add
     * @return an unmodifiable map of all the elements that iterate in sorted order
     * @throws IllegalArgumentException if a duplicate key is provided
     */
    @SafeVarargs
    public static <K, V> Map<K, V> unmodifiableSortedMapOf(
        final Tuple.Two<K, V> firstPair,
        final Tuple.Two<K, V>... morePairs
    ) {
        return java.util.Collections.unmodifiableMap(sortedMapOf(firstPair, morePairs));
    }
    
    /**
     * Create a {@link TreeMap} of the key/value pairs of all the given maps.
     * 
     * @param <K> the type of keys to add
     * @param <V> the type of values to add
     * @param firstMap the first map to add the key/value pairs of
     * @param moreMaps additional maps to add the key/value pairs of
     * @return a map of all the elements
     * @throws IllegalArgumentException if a duplicate key is provided
     */
    @SafeVarargs
    public static <K, V> Map<K, V> sortedMapOf(
        final Map<K, V> firstMap,
        final Map<K, V>... moreMaps
    ) {
        return addMaps(new TreeMap<>(), firstMap, moreMaps);
    }
    
    /**
     * Create an unmodifiable sorted map of the key/value pairs given.
     * 
     * @param <K> the type of keys to add
     * @param <V> the type of values to add
     * @param firstMap the first mapo to add the key/value pairs of
     * @param moreMaps additional maps to add the key/value pairs of
     * @return a map of all the elements
     * @throws IllegalArgumentException if a duplicate key is provided
     */
    @SafeVarargs
    public static <K, V> Map<K, V> unmodifiableSortedMapOf(
        final Map<K, V> firstMap,
        final Map<K, V>... moreMaps
    ) {
        return java.util.Collections.unmodifiableMap(sortedMapOf(firstMap, moreMaps));
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
