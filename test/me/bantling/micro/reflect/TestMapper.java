package me.bantling.micro.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import me.bantling.micro.util.Collections;
import me.bantling.micro.util.Tuple;
import me.bantling.micro.util.Wrapper;

@SuppressWarnings({ "static-method", "unchecked", "unused" })
public class TestMapper {
    final static class Customer1 {
        private String firstName;
        private String lastName;
        
        Customer1() {
            firstName = null;
            lastName = null;
        }
    }

    static class Customer2 {
        private final String firstName = null;
        private final String lastName  = null;
    }

    static class Customer3 {
        private String firstName;
        private String lastName;
    }
    
    void testMapper(final Class<?> typ, final Mapper<?> mapper) throws Throwable {
        assertTrue(typ == mapper.clazz);
        
        assertEquals(
            Collections.setOf("firstName", "lastName"),
            mapper.getters.keySet()
        );
        
        assertEquals(
            Collections.setOf("firstName", "lastName"),
            mapper.setters.keySet()
        );
        
        Object obj = typ.getDeclaredConstructor().newInstance();
        
        mapper.setters.get("firstName").invoke(obj, "John");
        mapper.setters.get("lastName").invoke(obj, "Doe");
        assertEquals("John", mapper.getters.get("firstName").invoke(obj));
        assertEquals("Doe", mapper.getters.get("lastName").invoke(obj));
        
        final Mapper<Object> m2 = (Mapper<Object>)(mapper);
        m2.setProperty(obj, "lastName", "Smith");
        assertEquals("Smith", m2.getProperty(obj, "lastName"));
        
        Map<String, Object> map = Collections.mapOf(
            Tuple.of("firstName", "Jane"),
            Tuple.of("lastName", "Doe")
        );
        m2.populateInstance(obj, map);
        
        map = new HashMap<>();
        m2.populateMap(map, obj);
        assertEquals(
            Collections.mapOf(
                Tuple.of("firstName", "Jane"),
                Tuple.of("lastName", "Doe")
            ),
            map
        );
        
        obj = m2.convertMap(
            Collections.mapOf(
                Tuple.of("firstName", "John"),
                Tuple.of("lastName", "Smith")
            )
        );
        map = m2.convertInstance(obj);
        assertEquals(
            Collections.mapOf(
                Tuple.of("firstName", "John"),
                Tuple.of("lastName", "Smith")
            ),
            map
        );
    }
    
    @Test
    void scanOf() throws Throwable {
        for (final Class<?> custType : new Class<?>[] {Customer1.class, Customer2.class, Customer3.class}) {
            testMapper(custType, Mapper.scan(custType));
            testMapper(custType, Mapper.of(custType));
            testMapper(custType, Mapper.of(custType.getName()));
        }
    }
}
