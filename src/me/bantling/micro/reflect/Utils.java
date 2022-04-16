package me.bantling.micro.reflect;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import me.bantling.micro.function.Try;

public class Utils {
    /**
     * Singleton Lookup instance
     */
    static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    
    /**
     * Get the class for a fully qualified string class name
     * 
     * @param name class name
     * @return Class object
     * @throws wrapped ClassNotFoundException if the class does not exist
     */
    public static Class<?> classOf(final String name) {
        return Try.get(() -> Class.forName(name));
    }
    
    /**
     * Get a MethodHandle no argument constructor of a class.
     * The returned handle requires no arguments.
     * 
     * @param clazz the class to get the constructor of
     * @return MethodHandle that constructs instances of clazz
     */
    public static MethodHandle constructorOf(final Class<?> clazz) {
        return Try.get(() -> {
            final Constructor<?> cons = clazz.getDeclaredConstructor();
            cons.setAccessible(true);
            return LOOKUP.unreflectConstructor(cons);
        });
    }
    
    /**
     * Get a MethodHandle to read the value of a field.
     * The returned handle will need an instance to operate on unless the field is static.
     * The field may be non-public and/or final.
     * 
     * @param field the field to get the getter of
     * @return MethodHandle that reads the value of the field
     */
    public static MethodHandle getterOf(final Field field) {
        return Try.get(() -> {
            field.setAccessible(true);
            return LOOKUP.unreflectGetter(field);
        });
    }
    
    /**
     * Get a MethodHandle to set the value of a field.
     * The returned handle will need an instance to operate on unless the field is static.
     * The handle will need the value to set the field to.
     * The field may be non-public and/or final.
     * 
     * @param field the field to get the getter of
     * @return MethodHandle that reads the value of the field
     */
    public static MethodHandle setterOf(final Field field) {
        return Try.get(() -> {
            field.setAccessible(true);
            return LOOKUP.unreflectSetter(field);
        });
    }
}
