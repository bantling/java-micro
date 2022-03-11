package me.bantling.micro.util;

import java.util.function.Predicate;

public class Functions {
    /**
     * Singleton instance of a Predicate that always returns true
     */
    static final Predicate<?> TRUE_PREDICATE = $ -> true;
    
    /**
     * Singleton instance of a Predicate that always returns false
     */
    static final Predicate<?> FALSE_PREDICATE = $ -> false;
    
    /**
     * A predicate that is always true for any value
     * 
     * @param <T>
     * @return {@link #TRUE_PREDICATE} cast to Predicate<T>
     */
    public static <T> Predicate<T> truePredicate() {
        @SuppressWarnings("unchecked")
        final Predicate<T> predicate = (Predicate<T>)(TRUE_PREDICATE);
        return predicate;
    }
    
    /**
     * A predicate that is always false for any value
     * 
     * @param <T>
     * @return {@link #FALSE_PREDICATE} cast to Predicate<T>
     */
    public static <T> Predicate<T> falsePredicate() {
        @SuppressWarnings("unchecked")
        final Predicate<T> predicate = (Predicate<T>)(FALSE_PREDICATE);
        return predicate;
    }
}
