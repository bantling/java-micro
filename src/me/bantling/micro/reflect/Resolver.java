package me.bantling.micro.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import me.bantling.micro.util.Collections;

/**
 * Resolve types
 */
public class Resolver {
    static final String UNRESOLVABLE_TYPE_FMT = "Type implementation %s is not handled";
    
    /**
     * Resolve a Type to a single class, as follows:
     * - Class => Class
     * - GenericArrayType => the class object for an array of resolved generic component type
     * - ParameterizedType => resolve raw type
     * - TypeVariable => resolve first bound if there is one, else Object.class
     * - WildcardType => resolve first upper bound if there is one, else Object.class
     * 
     * @param type the type to resolve
     * @return resolved class type
     * @throws {@link #UNRESOLVABLE_TYPE_FMT} for an unrecognised Type implementation
     */
    public static Class<?> resolveType(final Type type) {
        if (type instanceof Class) {
            return (Class<?>)(type);
        }
        if (type instanceof GenericArrayType) {
            return Array.newInstance(
                resolveType(((GenericArrayType)(type)).getGenericComponentType()),
                0
            ).getClass();
        }
        if (type instanceof ParameterizedType) {
            return resolveType(((ParameterizedType)(type)).getRawType());
        }
        if (type instanceof TypeVariable) {
            final Type[] bounds = ((TypeVariable<?>)(type)).getBounds();
            return bounds.length > 0 ? resolveType(bounds[0]) : Object.class;
        }
        if (type instanceof WildcardType) {
            final Type[] upperBounds = ((WildcardType)(type)).getUpperBounds();
            return upperBounds.length > 0 ? resolveType(upperBounds[0]) : Object.class;
        }
        
        // A situation we have not accounted for, perhaps a feature of a newer Java version
        throw new IllegalArgumentException(String.format(UNRESOLVABLE_TYPE_FMT, type == null ? null : type.getClass()));        
    }
    
    /**
     * Expand a raw type into a list of the raw type itself, and the resolved type of all TypeVariables.
     * EG:
     * List.class = [List.class, Object.class]
     * Map.class  = [Map.class, Object.class, Object.class]
     * 
     * class Foo<T extends Number> {...}
     * Foo.class = [Foo.class, Number.class]  
     * 
     * @param rawType
     * @return
     */
    public static List<Class<?>> expandRawType(Class<?> rawType) {
        final List<Class<?>> result = new LinkedList<>();
        result.add(rawType);
        
        for (final TypeVariable<?> tv : rawType.getTypeParameters()) {
            result.add(resolveType(tv));
        }
        
        return result;
    }
    
    /**
     * Resolve a Type into an array containing the resolved Type, and if the Type has parameters, resolve those parameter types.
     * If the Type does not have parameters, but the class it refers to does, then resolve them accordingly.
     * See second example below for a case where the Type has no args but the underlying Class does.
     *  
     * - Class => [Class]
     * - List = [List, Object]
     * - List<Number> = [List, Number]
     * - List<List<Number>> = [List, List]
     * - Map<String, Number> = [Map, String, Number]
     * - Map<List<String>, Set<Number>> = [Map, List, Set]
     * 
     * @param type the type to resolve
     * @return
     */
    public static List<Class<?>> resolveTypeAndArgs(final Type type) {
        @SuppressWarnings("unchecked")
        final Function<Type, List<Class<?>>>[] classResolver = new Function[1];
        
        classResolver[0] = classResolver[0] = t -> {
            if (t instanceof Class) {
                return Collections.listOf((Class<?>)(t));
            }
            if (t instanceof GenericArrayType) {
                return Collections.listOf(Array.newInstance(
                    classResolver[0].apply(((GenericArrayType)(t)).getGenericComponentType()).get(0),
                    0
                ).getClass());
            }
            if (t instanceof ParameterizedType) {
                final ParameterizedType pt = (ParameterizedType)(t);
                final Type[] args = pt.getActualTypeArguments();
                final List<Class<?>> resolvedTypeAndArgs = classResolver[0].apply(pt.getRawType());
                for (final Type arg : args) {
                    resolvedTypeAndArgs.addAll(classResolver[0].apply(arg));
                }
                
                return resolvedTypeAndArgs;
            }
            if (t instanceof TypeVariable) {
                final Type[] bounds = ((TypeVariable<?>)(t)).getBounds();
                if (bounds.length == 0) {
                    return Collections.listOf(Object.class);
                }
                return classResolver[0].apply(bounds[0]);
            }
            if (t instanceof WildcardType) {
                final Type[] bounds = ((WildcardType)(t)).getUpperBounds();
                if (bounds.length == 0) {
                    return Collections.listOf(Object.class);
                }
                return classResolver[0].apply(bounds[0]);
            }
    
            // A situation we have not accounted for, perhaps a feature of a newer Java version
            throw new IllegalArgumentException(String.format(UNRESOLVABLE_TYPE_FMT, t == null ? null : t.getClass()));
        };
        
        List<Class<?>> resolvedType = classResolver[0].apply(type);
        if (resolvedType.size() == 1) {
            resolvedType = expandRawType(resolvedType.get(0)); 
        }
        
        return resolvedType;
    }
}
