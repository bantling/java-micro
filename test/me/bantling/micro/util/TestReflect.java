package me.bantling.micro.util;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

@SuppressWarnings({ "static-method", "static-access", "rawtypes" })
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
        
        final Tuple.Same same = new Tuple.Same();
        final List<Tuple.TwoOf<Type>> testCases = Collections.listOf(
            same.of(int.class, int.class),
            same.of(Integer.class, Integer.class),
            same.of(GAT1.class.getDeclaredField("array").getGenericType(), Object[].class),
            same.of(GAT2.class.getDeclaredField("array").getGenericType(), Number[].class),
            same.of(GAT3.class.getDeclaredField("array").getGenericType(), Set[].class),
            same.of(GAT4.class.getDeclaredField("array").getGenericType(), Object[][].class),
            same.of(GAT5.class.getDeclaredField("array").getGenericType(), Number[][][].class),
            same.of(GAT6.class.getDeclaredField("array").getGenericType(), Set[][][][].class),
            same.of(PT1.class.getDeclaredField("list").getGenericType(), List.class),
            same.of(PT2.class.getDeclaredField("list").getGenericType(), List.class),
            same.of(PT3.class.getDeclaredField("list").getGenericType(), List.class),
            same.of(TV1.class.getDeclaredField("value").getGenericType(), Object.class),
            same.of(TV2.class.getDeclaredField("value").getGenericType(), Number.class),
            same.of(TV3.class.getDeclaredField("value").getGenericType(), List.class),
            same.of(((ParameterizedType)(WT1.class.getDeclaredField("list").getGenericType())).getActualTypeArguments()[0], Object.class),
            same.of(((ParameterizedType)(WT2.class.getDeclaredField("list").getGenericType())).getActualTypeArguments()[0], Number.class),
            same.of(((ParameterizedType)(WT3.class.getDeclaredField("list").getGenericType())).getActualTypeArguments()[0], Set.class)
        );
        
        for (final Tuple.TwoOf<Type> testCase : testCases) {
            assertEquals(testCase.get2(), Reflect.resolveType(testCase.get1()));
        }
    }
    
    @Test
    public void expandRawType() throws Throwable {
        final Tuple.UpToSame uptoSame = new Tuple.UpToSame();
        final List<Tuple.UpToFourOf<Class<?>>> testCases = Collections.listOf(
            uptoSame.four(List.class, List.class, Object.class),
            uptoSame.four(Map.class, Map.class, Object.class, Object.class),
            uptoSame.four(TV2.class, TV2.class, Number.class)
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
        final Tuple t = new Tuple();
        final List<Tuple.Two<Type, List<Type>>> testCases = Collections.listOf(
            t.of(int.class, Collections.listOf(int.class)),
            t.of(Integer.class, Collections.listOf(Integer.class)),
            t.of(GAT1.class.getDeclaredField("array").getGenericType(), Collections.listOf(Object[].class)),
            t.of(GAT2.class.getDeclaredField("array").getGenericType(), Collections.listOf(Number[].class)),
            t.of(GAT4.class.getDeclaredField("array").getGenericType(), Collections.listOf(Object[][].class)),
            t.of(GAT5.class.getDeclaredField("array").getGenericType(), Collections.listOf(Number[][][].class)),
            t.of(GAT6.class.getDeclaredField("array").getGenericType(), Collections.listOf(Set[][][][].class)),
            t.of(PT1.class.getDeclaredField("list").getGenericType(), Collections.listOf(List.class, Object.class)),
            t.of(PT2.class.getDeclaredField("list").getGenericType(), Collections.listOf(List.class, Number.class)),
            t.of(PT3.class.getDeclaredField("list").getGenericType(), Collections.listOf(List.class, List.class, Number.class)),
            t.of(TV1.class.getDeclaredField("value").getGenericType(), Collections.listOf(Object.class)),
            t.of(TV2.class.getDeclaredField("value").getGenericType(), Collections.listOf(Number.class)),
            t.of(TV3.class.getDeclaredField("value").getGenericType(), Collections.listOf(List.class, String.class)),
            t.of(((ParameterizedType)(WT1.class.getDeclaredField("list").getGenericType())).getActualTypeArguments()[0], Collections.listOf(Object.class)),
            t.of(((ParameterizedType)(WT2.class.getDeclaredField("list").getGenericType())).getActualTypeArguments()[0], Collections.listOf(Number.class))
        );

        for (final Tuple.Two<Type, List<Type>> testCase : testCases) {
            assertEquals(testCase.get2(), Reflect.resolveTypeAndArgs(testCase.get1()));
        }
    }
}
