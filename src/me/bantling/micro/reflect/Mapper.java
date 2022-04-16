package me.bantling.micro.reflect;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import me.bantling.micro.function.Try;

/**
 * Map a set of values to/from a composite object. If the object contains composite objects, it is up to the
 * caller to use a separate mapper for the contained objects. A composite object is simply an object that is not in the
 * java.lang package that has at least one instance field to populate. Instance fields may be non-public and/or final.
 * All fields of the class itself and all superclasses are included in the mapper. 
 * 
 * A mapper does not throw any exceptions, but java will throw exceptions if a mapper is used incorrectly - for example,
 * if a mapper is created for an abstract class, it is up to the caller to ensure that the objects the mapper operates
 * on are concrete instances.
 * 
 * The implementation guarantees a given composite object class is only scanned once to create a mapper for it, even if
 * multiple threads are simultaneously creating mappers.
 * 
 * Objects can be populated two ways:
 * - By successive calling  
 * 
 * @param T the type of object to map
 */
public class Mapper<T> {
    static final String NO_SUCH_PROPERTY_ERROR_MSG = "Class %s has no property named %s";  
    
    /**
     * Map a fully qualified string class name to a {@code Mapper} instance
     */
    static final Map<Class<?>, Mapper<?>> MAPPERS_BY_CLASS_NAME = new ConcurrentHashMap<>();
    
    /**
     * The class being mapped
     */
    final Class<T> clazz;
    
    /**
     * The constructor for the class being mapped
     */
    final MethodHandle constructor;
    
    /**
     * The getters for reading the values from the object
     */
    final Map<String, MethodHandle> getters;
    
    /**
     * The setters for writing values to the object
     */
    final Map<String, MethodHandle> setters;
    
    /**
     * Constructor that initializes the constructor to the no argument constructor of the given type,
     * and sets the getters and setters maps to new empty maps.
     * 
     * @param clazz the class to map
     * @throws if the class does not have a constructor of no arguments
     */
    private Mapper(final Class<T> clazz) throws Exception {
        this.clazz = clazz;
        
        this.constructor = Utils.constructorOf(clazz);
        this.getters = new HashMap<>();
        this.setters = new HashMap<>();
    }
    
    /**
     * Scan the given class for fields and/or accessors, and return a {@code Mapper} instance for it
     * 
     * @param clazz the class to scan
     * @return {@code Mapper} instance
     */
    static <T> Mapper<T> scan(final Class<T> clazz) {
        return Try.get(() -> {
            final Mapper<T> mapper = new Mapper<>(clazz);
            
            // Collect all fields
            for (Class<?> c = clazz; c != Object.class; c = c.getSuperclass()) {
                for (final Field f : c.getDeclaredFields()) {
                    // Allow access to non-public and/or final fields
                    f.setAccessible(true);
                    
                    // Map pair of getter/setter handles, using field name as is
                    // (should already have first letter lower cased)
                    final String propertyName = f.getName();
                    mapper.getters.put(propertyName, Utils.getterOf(f));
                    mapper.setters.put(propertyName, Utils.setterOf(f));
                }
            }
            
            return mapper;
        });
    }
    
    /**
     * Scan the given class object
     * 
     * @param clazz the class to scan for accessors
     * @return singleton Mapper instance for the given class
     */
    @SuppressWarnings("unchecked")
    public static <T> Mapper<T> of(final Class<T> clazz) {
        return (Mapper<T>)(MAPPERS_BY_CLASS_NAME.computeIfAbsent(
            clazz,
            Mapper::scan
        ));
    }
    
    /**
     * Convenience method that accepts a string class name, for cases where the caller knows the string name
     * (eg from a cconfiguration file). Just acquires the class object via {@link Class#forName(String)} and calls
     * {@link #of(Class)}.
     * 
     * @param className fully qualified string name of a class to scan for accessors
     * @return singleton Mapper instance for the given class
     * @throws {@link ClassNotFoundException} if the class does not exist
     */
    @SuppressWarnings("unchecked")
    public static <T> Mapper<T> of(final String className) {
        return (Mapper<T>)(Mapper.of(Utils.classOf(className)));
    }
    
    /**
     * Get the value of a property of the object 
     * 
     * @param <V> the expected type of the property value
     * @param object the object to read the property of
     * @param name the name of the property
     * @return property value
     * @throws IllegalArgumentException if the property does not exist
     */
    public <V> V getProperty(
        final T object,
        final String name
    ) {
        return Try.get(() -> {
            final MethodHandle getter = getters.get(name);
            
            if (getter == null) {
                throw new IllegalArgumentException(String.format(
                    NO_SUCH_PROPERTY_ERROR_MSG,
                    clazz.getName(),
                    name
                ));
            }
            
            return (V)(getter.invoke(object));
        });
    }
    
    /**
     * Set the value of a property of the object
     * 
     * @param object the object to set the property of
     * @param name the name of the property
     * @param value the value of the property
     * @throws IllegalArgumentException if the property does not exist
     * @throws if the value is not the correct type
     */
    public void setProperty(
        final T object,
        final String name,
        final Object value
    ) {
        Try.to(() -> {
            final MethodHandle setter = setters.get(name);
            
            if (setter == null) {
                throw new IllegalArgumentException(String.format(
                    NO_SUCH_PROPERTY_ERROR_MSG,
                    clazz.getName(),
                    name
                ));
            }
            
            setter.invoke(object, value);
        });
    }
    
    /**
     * Populate the given map with the property values of the instance. 
     * 
     * @param values the map to populate
     * @param object the object to get property values from
     */
    public void populateMap(final Map<String, Object> values, final T object) {
       for (final Map.Entry<String, MethodHandle> property : getters.entrySet()) {
           final String name = property.getKey();
           values.put(name, getProperty(object, name));
       }
    }
    
    /**
     * Populate the given instance with the map of property names to values. 
     * 
     * @param object the object to populate
     * @param values the map of property values
     * @throws IllegalArgumentException if a property does not exist
     * @throws if the value of a property is not the correct type
     */
    public void populateInstance(final T object, final Map<String, Object> values) {
       for (final Map.Entry<String, Object> property : values.entrySet()) {
           setProperty(object, property.getKey(), property.getValue());
       }
    }
    
    /**
     * Convenience method that creates a new map, populates the map with the given instance, and returns the map
     *    
     * @param values
     * @return new populated instance
     */
    public Map<String, Object> convertInstance(final T object) {
        final Map<String, Object> map = new HashMap<>();
        populateMap(map, object);
        return map;
    }
    
    /**
     * Convenience method that creates a new instance, populates the instance with the given map, and returns the instance
     *    
     * @param values
     * @return new populated instance
     */
    public T convertMap(final Map<String, Object> values) {
        return Try.get(() -> {
           final T object = (T)(constructor.invoke());
           populateInstance(object, values);
           return object;
        });
    }
}
