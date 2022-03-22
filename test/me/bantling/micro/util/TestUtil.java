package me.bantling.micro.util;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings({ "static-method", "boxing" })
public class TestUtil {
    @Test
    public void cons() throws Throwable {
        final Constructor<Collections> cons = Collections.class.getDeclaredConstructor();
        cons.setAccessible(true);
        try {
            cons.newInstance();
        } catch (final Throwable t) {
            assertTrue(t.getCause() instanceof RuntimeException);
        }
    }
    
    @Test
    public void listBuilder() {
        // 0, 100 - 101, 200 - 202, ..., 900 - 909
        final List<Integer> expected = new LinkedList<>();
        for (int i = 0; i <= 9; i++) {
            for (int j = i * 100; j <= i * 101; j++) {
                expected.add(Integer.valueOf(j));
            }
        }
        
        final List<Integer> actual = Collections.listOf(
            0,
            100, 101,
            200, 201, 202,
            300, 301, 302, 303,
            400, 401, 402, 403, 404,
            500, 501, 502, 503, 504, 505,
            600, 601, 602, 603, 604, 605, 606,
            700, 701, 702, 703, 704, 705, 706, 707,
            800, 801, 802, 803, 804, 805, 806, 807, 808,
            900, 901, 902, 903, 904, 905, 906, 907, 908, 909
        );
        
        assertEquals(expected, actual);
    }
}
