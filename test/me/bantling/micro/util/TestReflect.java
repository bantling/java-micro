package me.bantling.micro.util;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

@SuppressWarnings({ "static-method", "rawtypes" })
public class TestReflect {
    static class GAT1<T> {
        T[] array;
    }

    static class GAT2<T extends Number> {
        T[] array;
    }

    static class GAT3<T extends Set<?>> {
        T[] array;
    }

    static class GAT4<T> {
        T[][] array;
    }

    static class GAT5<T extends Number> {
        T[][][] array;
    }

    static class GAT6<T extends Set<?>> {
        T[][][][] array;
    }
    
    static class PT1 {
        List list;
    }
    
    static class PT2 {
        List<Number> list;
    }
    
    static class PT3 {
        List<List<Number>> list;
    }
    
    static class TV1<T> {
        T value;
    }
    
    static class TV2<T extends Number> {
        T value;
    }
    
    static class TV3<T extends List<String>> {
        T value;
    }
    
    static class WT1 {
        List<?> list;
    }
    
    static class WT2 {
        List<? extends Number> list;
    }
    
    static class WT3 {
        List<? extends Set<?>> list;
    }
    
    @Test
    public void resolveType() throws Throwable {
        // input, expected result
        final List<Tuple.TwoOf<Type>> testCases = Collections.listOf(
            Tuple.ofSame(int.class, int.class),
            Tuple.ofSame(Integer.class, Integer.class),
            Tuple.ofSame(GAT1.class.getDeclaredField("array").getGenericType(), Object[].class),
            Tuple.ofSame(GAT2.class.getDeclaredField("array").getGenericType(), Number[].class),
            Tuple.ofSame(GAT3.class.getDeclaredField("array").getGenericType(), Set[].class),
            Tuple.ofSame(GAT4.class.getDeclaredField("array").getGenericType(), Object[][].class),
            Tuple.ofSame(GAT5.class.getDeclaredField("array").getGenericType(), Number[][][].class),
            Tuple.ofSame(GAT6.class.getDeclaredField("array").getGenericType(), Set[][][][].class),
            Tuple.ofSame(PT1.class.getDeclaredField("list").getGenericType(), List.class),
            Tuple.ofSame(PT2.class.getDeclaredField("list").getGenericType(), List.class),
            Tuple.ofSame(PT3.class.getDeclaredField("list").getGenericType(), List.class),
            Tuple.ofSame(TV1.class.getDeclaredField("value").getGenericType(), Object.class),
            Tuple.ofSame(TV2.class.getDeclaredField("value").getGenericType(), Number.class),
            Tuple.ofSame(TV3.class.getDeclaredField("value").getGenericType(), List.class),
            Tuple.ofSame(((ParameterizedType)(WT1.class.getDeclaredField("list").getGenericType())).getActualTypeArguments()[0], Object.class),
            Tuple.ofSame(((ParameterizedType)(WT2.class.getDeclaredField("list").getGenericType())).getActualTypeArguments()[0], Number.class),
            Tuple.ofSame(((ParameterizedType)(WT3.class.getDeclaredField("list").getGenericType())).getActualTypeArguments()[0], Set.class)
        );
        
        for (final Tuple.TwoOf<Type> testCase : testCases) {
            assertEquals(testCase.get2(), Reflect.resolveType(testCase.get1()));
        }
    }
    
    @Test
    public void expandRawType() throws Throwable {
        final List<Tuple.UpToFourOf<Class<?>>> testCases = Collections.listOf(
            Tuple.upToFourOf(List.class, List.class, Object.class),
            Tuple.upToFourOf(Map.class, Map.class, Object.class, Object.class),
            Tuple.upToFourOf(TV2.class, TV2.class, Number.class)
        );
        
        for (final Tuple.UpToFourOf<Class<?>> testCase : testCases) {
            final List<Class<?>> expandedRawType = Reflect.expandRawType(testCase.get1());
            
            assertEquals(testCase.get2(), expandedRawType.get(0));
            assertEquals(testCase.get3(), expandedRawType.get(1));
            if (testCase.getCount() > 3) {
                assertEquals(testCase.get4(), expandedRawType.get(2));
            }
        }
    }
    
    @Test
    public void resolveTypeAndArgs() throws Throwable {
        // input, expected result
        final List<Tuple.Two<Type, List<Type>>> testCases = Collections.listOf(
            Tuple.of(int.class, Collections.listOf(int.class)),
            Tuple.of(Integer.class, Collections.listOf(Integer.class)),
            Tuple.of(GAT1.class.getDeclaredField("array").getGenericType(), Collections.listOf(Object[].class)),
            Tuple.of(GAT2.class.getDeclaredField("array").getGenericType(), Collections.listOf(Number[].class)),
            Tuple.of(GAT4.class.getDeclaredField("array").getGenericType(), Collections.listOf(Object[][].class)),
            Tuple.of(GAT5.class.getDeclaredField("array").getGenericType(), Collections.listOf(Number[][][].class)),
            Tuple.of(GAT6.class.getDeclaredField("array").getGenericType(), Collections.listOf(Set[][][][].class)),
            Tuple.of(PT1.class.getDeclaredField("list").getGenericType(), Collections.listOf(List.class, Object.class)),
            Tuple.of(PT2.class.getDeclaredField("list").getGenericType(), Collections.listOf(List.class, Number.class)),
            Tuple.of(PT3.class.getDeclaredField("list").getGenericType(), Collections.listOf(List.class, List.class, Number.class)),
            Tuple.of(TV1.class.getDeclaredField("value").getGenericType(), Collections.listOf(Object.class)),
            Tuple.of(TV2.class.getDeclaredField("value").getGenericType(), Collections.listOf(Number.class)),
            Tuple.of(TV3.class.getDeclaredField("value").getGenericType(), Collections.listOf(List.class, String.class)),
            Tuple.of(((ParameterizedType)(WT1.class.getDeclaredField("list").getGenericType())).getActualTypeArguments()[0], Collections.listOf(Object.class)),
            Tuple.of(((ParameterizedType)(WT2.class.getDeclaredField("list").getGenericType())).getActualTypeArguments()[0], Collections.listOf(Number.class))
        );

        for (final Tuple.Two<Type, List<Type>> testCase : testCases) {
            assertEquals(testCase.get2(), Reflect.resolveTypeAndArgs(testCase.get1()));
        }
    }
}
