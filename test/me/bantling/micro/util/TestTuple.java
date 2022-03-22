package me.bantling.micro.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import me.bantling.micro.util.Tuple.Five;
import me.bantling.micro.util.Tuple.FiveOf;
import me.bantling.micro.util.Tuple.Four;
import me.bantling.micro.util.Tuple.FourOf;
import me.bantling.micro.util.Tuple.Six;
import me.bantling.micro.util.Tuple.SixOf;
import me.bantling.micro.util.Tuple.Three;
import me.bantling.micro.util.Tuple.ThreeOf;
import me.bantling.micro.util.Tuple.Two;
import me.bantling.micro.util.Tuple.TwoOf;
import me.bantling.micro.util.Tuple.UpToFive;
import me.bantling.micro.util.Tuple.UpToFiveOf;
import me.bantling.micro.util.Tuple.UpToFour;
import me.bantling.micro.util.Tuple.UpToFourOf;
import me.bantling.micro.util.Tuple.UpToSix;
import me.bantling.micro.util.Tuple.UpToSixOf;
import me.bantling.micro.util.Tuple.UpToThree;
import me.bantling.micro.util.Tuple.UpToThreeOf;
import me.bantling.micro.util.Tuple.UpToTwo;
import me.bantling.micro.util.Tuple.UpToTwoOf;

@SuppressWarnings({ "static-method", "boxing" })
public class TestTuple {
    @Test
    public void tuple() {
        try {
            final Constructor<?> cons = Tuple.class.getDeclaredConstructor();
            cons.setAccessible(true);
            cons.newInstance();
            fail("Should not be able to construct");
        } catch (final Throwable t) {
            assertTrue(t instanceof InvocationTargetException);
            assertTrue(t.getCause() instanceof RuntimeException);
            assertNull(t.getCause().getMessage());
        }
    }
    
//    @Test
//    public void base() {
//        // Equals method
//        
//        {
//            final Base<Integer, Integer, Integer, Integer, Integer, Integer> base1a =
//                new Base<>(0, null, null, null, null, null, null);
//            final Base<Integer, Integer, Integer, Integer, Integer, Integer> base1b =
//                    new Base<>(0, null, null, null, null, null, null);
//            final Base<Integer, Integer, Integer, Integer, Integer, Integer> base2 =
//                    new Base<>(1, null, null, null, null, null, null);
//            
//            // ==
//            assertEquals(base1a, base1a);
//            // != && instance && count == 0
//            assertEquals(base1a, base1b);
//            // != && !instance
//            assertNotEquals(base1a, "");
//             != && instance && count != 0
//            assertNotEquals(base1a, base2);
//        }
//        
//        {
//            // != && instance && count == 1 && t == t
//            assertEquals(
//                new Base<>(1, "", null, null, null, null, null),
//                new Base<>(1, "", null, null, null, null, null)
//            );
//
//            // != && instance && count == 1 && t != t
//            assertNotEquals(
//                new Base<>(1, "",   null, null, null, null, null),
//                new Base<>(1, null, null, null, null, null, null)
//            );
//        }
//        
//        {
//            // != && instance && count == 2 && u == u
//            assertEquals(
//                new Base<>(2, "", "", null, null, null, null),
//                new Base<>(2, "", "", null, null, null, null)
//            );
//
//            // != && instance && count == 2 && u != u
//            assertNotEquals(
//                new Base<>(2, "", "",   null, null, null, null),
//                new Base<>(2, "", null, null, null, null, null)
//            );
//        }
//        
//        {
//            // != && instance && count == 3 && v === v
//            assertEquals(
//                new Base<>(3, "", "", "", null, null, null),
//                new Base<>(3, "", "", "", null, null, null)
//            );
//
//            // != && instance && count == 3 && v != v
//            assertNotEquals(
//                new Base<>(3, "", "", "",   null, null, null),
//                new Base<>(3, "", "", null, null, null, null)
//            );
//        }
//        
//        {
//            // != && instance && count == 4 && w === w
//            assertEquals(
//                new Base<>(4, "", "", "", "", null, null),
//                new Base<>(4, "", "", "", "", null, null)
//            );
//
//            // != && instance && count == 4 && w != w
//            assertNotEquals(
//                new Base<>(4, "", "", "", "",   null, null),
//                new Base<>(4, "", "", "", null, null, null)
//            );
//        }
//        
//        {
//            // != && instance && count == 5 && x == x
//            assertEquals(
//                new Base<>(5, "", "", "", "", "", null),
//                new Base<>(5, "", "", "", "", "", null)
//            );
//
//            // != && instance && count == 5 && x != x
//            assertNotEquals(
//                new Base<>(5, "", "", "", "", "",   null),
//                new Base<>(5, "", "", "", "", null, null)
//            );
//        }
//        
//        {
//            // != && instance && count == 6 && y == y
//            assertEquals(
//                new Base<>(6, "", "", "", "", "", ""),
//                new Base<>(6, "", "", "", "", "", "")
//            );
//
//            // != && instance && count == 6 && y != y
//            assertNotEquals(
//                new Base<>(6, "", "", "", "", "", ""  ),
//                new Base<>(6, "", "", "", "", "", null)
//            );
//        }
//    }
    
    @Test
    public void two() {
        final Two<String, Integer> t = Tuple.of("abc", 1);

        assertTrue(Two.class == t.getClass());
        assertEquals(Objects.hash("abc", 1), t.hashCode());
        
        assertEquals(t, t);
        assertEquals(t, Tuple.of("abc", 1));
        assertNotEquals(t, Tuple.of(1, "abc"));
        assertNotEquals(t, Tuple.of("abc", 2));
        assertNotEquals(t, "");
        
        assertEquals("(abc,1)", t.toString());
        
        assertEquals(2, t.count);
        assertEquals("abc", t.get1());
        assertEquals(1, t.get2());
        assertNull(t.v);
        assertNull(t.w);
        assertNull(t.x);
        assertNull(t.y);

        final Two<Integer, Integer> tt = Tuple.ofNullable(null, 1);
        assertTrue(Two.class == tt.getClass());
        assertEquals("(null,1)", tt.toString());
    }
    
    @Test
    public void twoOf() {
        final TwoOf<Integer> t = Tuple.ofSame(1, 2);
        
        assertEquals(Objects.hash(1, 2), t.hashCode());
        
        assertEquals(t, t);
        assertEquals(t, Tuple.of(1, 2));
        assertNotEquals(t, Tuple.of(2, 1));
        assertNotEquals(t, Tuple.of(1, 3));
        assertNotEquals(t, "");
        
        assertEquals("(1,2)", t.toString());
        
        assertEquals(2, t.count);
        assertEquals(1, t.get1());
        assertEquals(2, t.get2());
        assertNull(t.v);
        assertNull(t.w);
        assertNull(t.x);
        assertNull(t.y);

        final TwoOf<Integer> tt = Tuple.ofSameNullable(null, 1);
        assertEquals("(null,1)", tt.toString());
    }
    
    @Test
    public void upToTwo() {
        {
            final UpToTwo<String, Integer> t = Tuple.upToTwo();
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.upToTwo());
            assertNotEquals(t, Tuple.upToTwo(1));
            assertNotEquals(t, "");
            
            assertEquals("()", t.toString());
            
            assertEquals(0, t.count);
            assertNull(t.t);
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
        }
        
        {
            final UpToTwo<String, Integer> t = Tuple.upToTwo("abc");
            
            assertEquals(Objects.hash("abc"), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.upToTwo("abc"));
            assertNotEquals(t, Tuple.upToTwo(1));
            assertNotEquals(t, "");
            
            assertEquals("(abc)", t.toString());
            
            assertEquals(1, t.getCount());
            assertEquals("abc", t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UpToTwo<Integer, Integer> tt = Tuple.upToTwoNullable(null);
            assertTrue(UpToTwo.class == tt.getClass());
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UpToTwo<String, Integer> t = Tuple.upToTwo("abc", 1);
            
            assertEquals(Objects.hash("abc", 1), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of("abc", 1));
            assertNotEquals(t, Tuple.of(1, "abc"));
            assertNotEquals(t, Tuple.of("abc", 2));
            assertNotEquals(t, "");
            
            assertEquals("(abc,1)", t.toString());

            assertEquals(2, t.getCount());
            assertEquals("abc", t.get1());
            assertEquals(1, t.get2());
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UpToTwo<Integer, Integer> tt = Tuple.upToTwoNullable(null, 1);
            assertTrue(UpToTwo.class == tt.getClass());
            assertEquals("(null,1)", tt.toString());
        }
    }
    
    @Test
    public void upToTwoOf() {
        {
            final UpToTwoOf<Integer> t = Tuple.upToTwoOf();
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.upToTwo());
            assertNotEquals(t, Tuple.upToTwo(1));
            
            assertEquals("()", t.toString());
            
            assertEquals(0, t.count);
            assertNull(t.t);
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
        }
        
        {
            final UpToTwoOf<Integer> t = Tuple.upToTwoOf(1);
            
            assertEquals(Objects.hash(1), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.upToTwo(1));
            assertNotEquals(t, Tuple.upToTwo(2));
            
            assertEquals("(1)", t.toString());

            assertEquals(1, t.getCount());
            assertEquals(1, t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UpToTwoOf<Integer> tt = Tuple.upToTwoOfNullable(null);
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UpToTwoOf<Integer> t = Tuple.upToTwoOf(1, 2);
            
            assertEquals(Objects.hash(1, 2), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of(1, 2));
            assertNotEquals(t, Tuple.of(2, 1));
            assertNotEquals(t, Tuple.of(1, 3));
            
            assertEquals("(1,2)", t.toString());

            assertEquals(2, t.getCount());
            assertEquals(1, t.get1());
            assertEquals(2, t.get2());
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UpToTwoOf<Integer> tt = Tuple.upToTwoOfNullable(null, 1);
            assertEquals("(null,1)", tt.toString());
        }
    }
    
    @Test
    public void three() {
        final Three<String, Integer, String> t = Tuple.of("abc", 1, "def");
        
        assertEquals(Objects.hash("abc", 1, "def"), t.hashCode());
        
        assertEquals(t, t);
        assertEquals(t, Tuple.of("abc", 1, "def"));
        assertNotEquals(t, Tuple.of(1, "abc", "def"));
        assertNotEquals(t, Tuple.of("abc", 1, 2));
        
        assertEquals("(abc,1,def)", t.toString());

        assertEquals(3, t.count);
        assertEquals("abc", t.get1());
        assertEquals(1, t.get2());
        assertEquals("def", t.get3());
        assertNull(t.w);
        assertNull(t.x);
        assertNull(t.y);
        
        final Three<Integer, Integer, Integer> tt = Tuple.ofNullable(null, 1, 2);
        assertTrue(Three.class == tt.getClass()); 
        assertEquals("(null,1,2)", tt.toString());
    }
    
    @Test
    public void threeOf() {
        final ThreeOf<Integer> t = Tuple.ofSame(1, 2, 3);
        
        assertEquals(Objects.hash(1, 2, 3), t.hashCode());
        
        assertEquals(t, t);
        assertEquals(t, Tuple.of(1, 2, 3));
        assertNotEquals(t, Tuple.of(2, 1, 3));
        assertNotEquals(t, Tuple.of(1, 2, 4));
        
        assertEquals("(1,2,3)", t.toString());
        
        assertEquals(3, t.count);
        assertEquals(1, t.get1());
        assertEquals(2, t.get2());
        assertEquals(3, t.get3());
        assertNull(t.w);
        assertNull(t.x);
        assertNull(t.y);
        
        final ThreeOf<Integer> tt = Tuple.ofSameNullable(null, 1, 2);
        assertEquals("(null,1,2)", tt.toString());
    }
    
    @Test
    public void upToThree() {
        {
            final UpToThree<String, Integer, String> t = Tuple.upToThree();
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.upToTwo());
            assertNotEquals(t, Tuple.upToTwo("abc"));
            
            assertEquals("()", t.toString());

            assertEquals(0, t.getCount());
            assertNull(t.t);
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
        }
        
        {
            final UpToThree<String, Integer, String> t = Tuple.upToThree("abc");
            assertEquals(Objects.hash("abc"), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.upToTwo("abc"));
            assertNotEquals(t, Tuple.upToTwo(1));
            
            assertEquals("(abc)", t.toString());

            assertEquals(1, t.getCount());
            assertEquals("abc", t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            final UpToThree<Integer, Integer, Integer> tt = Tuple.upToThreeNullable(null);
            assertTrue(UpToThree.class == tt.getClass());
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UpToThree<String, Integer, String> t = Tuple.upToThree("abc", 1);
            
            assertEquals(Objects.hash("abc", 1), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of("abc", 1));
            assertNotEquals(t, Tuple.of(1, "abc"));
            assertNotEquals(t, Tuple.of("abc", 2));
            
            assertEquals("(abc,1)", t.toString());

            assertEquals(2, t.getCount());
            assertEquals("abc", t.get1());
            assertEquals(1, t.get2());
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            final UpToThree<Integer, Integer, Integer> tt = Tuple.upToThreeNullable(null, 1);
            assertTrue(UpToThree.class == tt.getClass());
            assertEquals("(null,1)", tt.toString());
        }
        
        {
            final UpToThree<String, Integer, String> t = Tuple.upToThree("abc", 1, "def");
            
            assertEquals(Objects.hash("abc", 1, "def"), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of("abc", 1, "def"));
            assertNotEquals(t, Tuple.of(1, "abc", "def"));
            assertNotEquals(t, Tuple.of("abc", 1, 2));
            
            assertEquals("(abc,1,def)", t.toString());

            assertEquals(3, t.getCount());
            assertEquals("abc", t.get1());
            assertEquals(1, t.get2());
            assertEquals("def", t.get3());
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            final UpToThree<Integer, Integer, Integer> tt = Tuple.upToThreeNullable(null, 1, 2);
            assertTrue(UpToThree.class == tt.getClass());
            assertEquals("(null,1,2)", tt.toString());
        }
    }
    
    @Test
    public void upToThreeOf() {
        {
            final UpToThreeOf<Integer> t = Tuple.upToThreeOf();
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.upToTwo());
            assertNotEquals(t, Tuple.upToTwo(1));
            
            assertEquals("()", t.toString());
            
            assertEquals(0, t.getCount());
            assertNull(t.t);
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
        }
        
        {
            final UpToThreeOf<Integer> t = Tuple.upToThreeOf(1);
            
            assertEquals(Objects.hash(1), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.upToTwo(1));
            assertNotEquals(t, Tuple.upToTwo(2));
            
            assertEquals("(1)", t.toString());

            assertEquals(1, t.getCount());
            assertEquals(1, t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            final UpToThreeOf<Integer> tt = Tuple.upToThreeOfNullable(null);
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UpToThreeOf<Integer> t = Tuple.upToThreeOf(1, 2);
            
            assertEquals(Objects.hash(1, 2), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of(1, 2));
            assertNotEquals(t, Tuple.of(2, 1));
            assertNotEquals(t, Tuple.of(1, 3));
            
            assertEquals("(1,2)", t.toString());

            assertEquals(2, t.getCount());
            assertEquals(1, t.get1());
            assertEquals(2, t.get2());
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            assertEquals("(null,1)", Tuple.upToThreeOfNullable(null, 1).toString());
        }
        
        {
            final UpToThreeOf<Integer> t = Tuple.upToThreeOf(1, 2, 3);
            
            assertEquals(Objects.hash(1, 2, 3), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of(1, 2, 3));
            assertNotEquals(t, Tuple.of(2, 1, 3));
            assertNotEquals(t, Tuple.of(1, 2, 4));
            
            assertEquals("(1,2,3)", t.toString());

            assertEquals(3, t.getCount());
            assertEquals(1, t.get1());
            assertEquals(2, t.get2());
            assertEquals(3, t.get3());
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            assertEquals("(null,1,2)", Tuple.upToThreeOfNullable(null, 1, 2).toString());
        }
    }
    
    @Test
    public void four() {
        final Four<String, Integer, String, Integer> t = Tuple.of("abc", 1, "def", 2);
        
        assertEquals(Objects.hash("abc", 1, "def", 2), t.hashCode());
        
        assertEquals(t, t);
        assertEquals(t, Tuple.of("abc", 1, "def", 2));
        assertNotEquals(t, Tuple.of(1, "abc", "def", 2));
        assertNotEquals(t, Tuple.of("abc", 1, "def", 3));
        
        assertEquals("(abc,1,def,2)", t.toString());

        assertEquals(4, t.count);
        assertEquals("abc", t.get1());
        assertEquals(1, t.get2());
        assertEquals("def", t.get3());
        assertEquals(2, t.get4());
        assertNull(t.x);
        assertNull(t.y);

        final Four<Integer, Integer, Integer, Integer> tt = Tuple.ofNullable(null, 1, 2, 3);
        assertTrue(Four.class == tt.getClass());
        assertEquals("(null,1,2,3)", tt.toString());
    }
    
    @Test
    public void fourOf() {
        final FourOf<Integer> t = Tuple.ofSame(1, 2, 3, 4);
        
        assertEquals(Objects.hash(1, 2, 3, 4), t.hashCode());
        
        assertEquals(t, t);
        assertEquals(t, Tuple.of(1, 2, 3, 4));
        assertNotEquals(t, Tuple.of(2, 1, 3, 4));
        assertNotEquals(t, Tuple.of(1, 2, 3, 5));
        
        assertEquals("(1,2,3,4)", t.toString());
        
        assertEquals(4, t.count);
        assertEquals(1, t.get1());
        assertEquals(2, t.get2());
        assertEquals(3, t.get3());
        assertEquals(4, t.get4());
        assertNull(t.x);
        assertNull(t.y);
        
        assertEquals("(null,1,2,3)", Tuple.ofSameNullable(null,1,2,3).toString());
    }
    
    @Test
    public void upToFour() {
        {
            final UpToFour<String, Integer, String, Integer> t = Tuple.upToFour();
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.upToTwo());
            assertNotEquals(t, Tuple.upToTwo("abc"));
            
            assertEquals("()", t.toString());

            assertEquals(0, t.getCount());
            assertNull(t.t);
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
        }
        
        {
            final UpToFour<String, Integer, String, Integer> t = Tuple.upToFour("abc");
            
            assertEquals(Objects.hash("abc"), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.upToTwo("abc"));
            assertNotEquals(t, Tuple.upToTwo(1));
            
            assertEquals("(abc)", t.toString());

            assertEquals(1, t.getCount());
            assertEquals("abc", t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UpToFour<Integer, Integer, Integer, Integer> tt = Tuple.upToFourNullable(null);
            assertTrue(UpToFour.class == tt.getClass());
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UpToFour<String, Integer, String, Integer> t = Tuple.upToFour("abc", 1);
            
            assertEquals(Objects.hash("abc", 1), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of("abc", 1));
            assertNotEquals(t, Tuple.of(1, "abc"));
            assertNotEquals(t, Tuple.of("abc", 2));
            
            assertEquals("(abc,1)", t.toString());

            assertEquals(2, t.getCount());
            assertEquals("abc", t.get1());
            assertEquals(1, t.get2());
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UpToFour<Integer, Integer, Integer, Integer> tt = Tuple.upToFourNullable(null, 1);
            assertTrue(UpToFour.class == tt.getClass());
            assertEquals("(null,1)", tt.toString());
        }
        
        {
            final UpToFour<String, Integer, String, Integer> t = Tuple.upToFour("abc", 1, "def");
            
            assertEquals(Objects.hash("abc", 1, "def"), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of("abc", 1, "def"));
            assertNotEquals(t, Tuple.of(1, "abc", "def"));
            assertNotEquals(t, Tuple.of("abc", 1, 2));
            
            assertEquals("(abc,1,def)", t.toString());

            assertEquals(3, t.getCount());
            assertEquals("abc", t.get1());
            assertEquals(1, t.get2());
            assertEquals("def", t.get3());
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UpToFour<Integer, Integer, Integer, Integer> tt = Tuple.upToFourNullable(null, 1, 2);
            assertTrue(UpToFour.class == tt.getClass());
            assertEquals("(null,1,2)", tt.toString());
        }
        
        {
            final UpToFour<String, Integer, String, Integer> t = Tuple.upToFour("abc", 1, "def", 2);
            
            assertEquals(Objects.hash("abc", 1, "def", 2), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of("abc", 1, "def", 2));
            assertNotEquals(t, Tuple.of(1, "abc", "def", 2));
            assertNotEquals(t, Tuple.of("abc", 1, "def", 3));
            
            assertEquals("(abc,1,def,2)", t.toString());

            assertEquals(4, t.getCount());
            assertEquals("abc", t.get1());
            assertEquals(1, t.get2());
            assertEquals("def", t.get3());
            assertEquals(2, t.get4());
            assertNull(t.x);
            assertNull(t.y);

            final UpToFour<Integer, Integer, Integer, Integer> tt = Tuple.upToFourNullable(null, 1, 2, 3);
            assertTrue(UpToFour.class == tt.getClass());
            assertEquals("(null,1,2,3)", tt.toString());
        }
    }
    
    @Test
    public void upToFourOf() {
        {
            final UpToFourOf<Integer> t = Tuple.upToFourOf();
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.upToTwo());
            assertNotEquals(t, Tuple.upToTwo(1));
            
            assertEquals("()", t.toString());

            assertEquals(0, t.getCount());
            assertNull(t.t);
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
        }
        
        {
            final UpToFourOf<Integer> t = Tuple.upToFourOf(1);
            
            assertEquals(Objects.hash(1), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.upToTwo(1));
            assertNotEquals(t, Tuple.upToTwo(2));
            
            assertEquals("(1)", t.toString());

            assertEquals(1, t.getCount());
            assertEquals(1, t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            assertEquals("(null)", Tuple.upToFourOfNullable(null).toString());
        }
        
        {
            final UpToFourOf<Integer> t = Tuple.upToFourOf(1, 2);
            
            assertEquals(Objects.hash(1, 2), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of(1, 2));
            assertNotEquals(t, Tuple.of(2, 1));
            assertNotEquals(t, Tuple.of(1, 3));
            
            assertEquals("(1,2)", t.toString());

            assertEquals(2, t.getCount());
            assertEquals(1, t.get1());
            assertEquals(2, t.get2());
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            assertEquals("(null,1)", Tuple.upToFourOfNullable(null, 1).toString());
        }
        
        {
            final UpToFourOf<Integer> t = Tuple.upToFourOf(1, 2, 3);
            
            assertEquals(Objects.hash(1, 2, 3), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of(1, 2, 3));
            assertNotEquals(t, Tuple.of(2, 1, 3));
            assertNotEquals(t, Tuple.of(1, 2, 4));
            
            assertEquals("(1,2,3)", t.toString());

            assertEquals(3, t.getCount());
            assertEquals(1, t.get1());
            assertEquals(2, t.get2());
            assertEquals(3, t.get3());
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            assertEquals("(null,1,2)", Tuple.upToFourOfNullable(null, 1, 2).toString());
        }
        
        {
            final UpToFourOf<Integer> t = Tuple.upToFourOf(1, 2, 3, 4);
            
            assertEquals(Objects.hash(1, 2, 3, 4), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of(1, 2, 3, 4));
            assertNotEquals(t, Tuple.of(2, 1, 3, 4));
            assertNotEquals(t, Tuple.of(1, 2, 3, 5));
            
            assertEquals("(1,2,3,4)", t.toString());

            assertEquals(4, t.getCount());
            assertEquals(1, t.get1());
            assertEquals(2, t.get2());
            assertEquals(3, t.get3());
            assertEquals(4, t.get4());
            assertNull(t.x);
            assertNull(t.y);
            
            assertEquals("(null,1,2,3)", Tuple.upToFourOfNullable(null, 1, 2, 3).toString());
        }
    }
    
    @Test
    public void five() {
        final Five<String, Integer, String, Integer, String> t = Tuple.of("abc", 1, "def", 2, "ghi");
        
        assertEquals(Objects.hash("abc", 1, "def", 2, "ghi"), t.hashCode());
        
        assertEquals(t, t);
        assertEquals(t, Tuple.of("abc", 1, "def", 2, "ghi"));
        assertNotEquals(t, Tuple.of(1, "abc", "def", 2, "ghi"));
        assertNotEquals(t, Tuple.of("abc", 1, "def", 2, 3));
        
        assertEquals("(abc,1,def,2,ghi)", t.toString());

        assertEquals(5, t.count);
        assertEquals("abc", t.get1());
        assertEquals(1, t.get2());
        assertEquals("def", t.get3());
        assertEquals(2, t.get4());
        assertEquals("ghi", t.get5());
        assertNull(t.y);
        
        final Five<Integer, Integer, Integer, Integer, Integer> tt = Tuple.ofNullable(null, 1, 2, 3 ,4);
        assertTrue(Five.class == tt.getClass());
        assertEquals("(null,1,2,3,4)", tt.toString());
    }
    
    @Test
    public void fiveOf() {
        final FiveOf<Integer> t = Tuple.ofSame(1, 2, 3, 4, 5);
        
        assertEquals(Objects.hash(1, 2, 3, 4, 5), t.hashCode());
        
        assertEquals(t, t);
        assertEquals(t, Tuple.of(1, 2, 3, 4, 5));
        assertNotEquals(t, Tuple.of(2, 1, 3, 4, 5));
        assertNotEquals(t, Tuple.of(1, 2, 3, 4, 6));
        
        assertEquals("(1,2,3,4,5)", t.toString());
        
        assertEquals(5, t.count);
        assertEquals(1, t.get1());
        assertEquals(2, t.get2());
        assertEquals(3, t.get3());
        assertEquals(4, t.get4());
        assertEquals(5, t.get5());
        assertNull(t.y);
        
        assertEquals("(null,1,2,3,4)", Tuple.ofSameNullable(null, 1, 2, 3, 4).toString());
    }
    
    @Test
    public void upToFive() {
        {
            final UpToFive<String, Integer, String, Integer, String> t = Tuple.upToFive();
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.upToTwo());
            assertNotEquals(t, Tuple.upToTwo("abc"));
            
            assertEquals("()", t.toString());

            assertEquals(0, t.getCount());
            assertNull(t.t);
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            assertTrue(UpToFive.class == t.getClass());
        }
        
        {
            final UpToFive<String, Integer, String, Integer, String> t = Tuple.upToFive("abc");
            
            assertEquals(Objects.hash("abc"), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.upToTwo("abc"));
            assertNotEquals(t, Tuple.upToTwo(1));
            
            assertEquals("(abc)", t.toString());

            assertEquals(1, t.getCount());
            assertEquals("abc", t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UpToFive<Integer, Integer, Integer, Integer, Integer> tt = Tuple.upToFiveNullable(null);
            assertTrue(UpToFive.class == tt.getClass());
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UpToFive<String, Integer, String, Integer, String> t = Tuple.upToFive("abc", 1);
            
            assertEquals(Objects.hash("abc", 1), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of("abc", 1));
            assertNotEquals(t, Tuple.of(1, "abc"));
            assertNotEquals(t, Tuple.of("abc", 2));
            
            assertEquals("(abc,1)", t.toString());

            assertEquals(2, t.getCount());
            assertEquals("abc", t.get1());
            assertEquals(1, t.get2());
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UpToFive<Integer, Integer, Integer, Integer, Integer> tt = Tuple.upToFiveNullable(null, 1);
            assertTrue(UpToFive.class == tt.getClass());
            assertEquals("(null,1)", tt.toString());
        }
        
        {
            final UpToFive<String, Integer, String, Integer, String> t = Tuple.upToFive("abc", 1, "def");
            
            assertEquals(Objects.hash("abc", 1, "def"), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of("abc", 1, "def"));
            assertNotEquals(t, Tuple.of(1, "abc", "def"));
            assertNotEquals(t, Tuple.of("abc", 1, 2));
            
            assertEquals("(abc,1,def)", t.toString());

            assertEquals(3, t.getCount());
            assertEquals("abc", t.get1());
            assertEquals(1, t.get2());
            assertEquals("def", t.get3());
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UpToFive<Integer, Integer, Integer, Integer, Integer> tt = Tuple.upToFiveNullable(null, 1, 2);
            assertTrue(UpToFive.class == tt.getClass());
            assertEquals("(null,1,2)", tt.toString());
        }
        
        {
            final UpToFive<String, Integer, String, Integer, String> t = Tuple.upToFive("abc", 1, "def", 2);
            
            assertEquals(Objects.hash("abc", 1, "def", 2), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of("abc", 1, "def", 2));
            assertNotEquals(t, Tuple.of(1, "abc", "def", 2));
            assertNotEquals(t, Tuple.of("abc", 1, "def", 3));
            
            assertEquals("(abc,1,def,2)", t.toString());

            assertEquals(4, t.getCount());
            assertEquals("abc", t.get1());
            assertEquals(1, t.get2());
            assertEquals("def", t.get3());
            assertEquals(2, t.get4());
            assertNull(t.x);
            assertNull(t.y);

            final UpToFive<Integer, Integer, Integer, Integer, Integer> tt = Tuple.upToFiveNullable(null, 1, 2, 3);
            assertTrue(UpToFive.class == tt.getClass());
            assertEquals("(null,1,2,3)", tt.toString());
        }
        
        {
            final UpToFive<String, Integer, String, Integer, String> t = Tuple.upToFive("abc", 1, "def", 2, "ghi");
            
            assertEquals(Objects.hash("abc", 1, "def", 2, "ghi"), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of("abc", 1, "def", 2, "ghi"));
            assertNotEquals(t, Tuple.of(1, "abc", "def", 2, "ghi"));
            assertNotEquals(t, Tuple.of("abc", 1, "def", 2, 3));
            
            assertEquals("(abc,1,def,2,ghi)", t.toString());

            assertEquals(5, t.getCount());
            assertEquals("abc", t.get1());
            assertEquals(1, t.get2());
            assertEquals("def", t.get3());
            assertEquals(2, t.get4());
            assertEquals("ghi", t.get5());
            assertNull(t.y);

            final UpToFive<Integer, Integer, Integer, Integer, Integer> tt = Tuple.upToFiveNullable(null, 1, 2, 3, 4);
            assertTrue(UpToFive.class == tt.getClass());
            assertEquals("(null,1,2,3,4)", tt.toString());
        }
    }
    
    @Test
    public void upToFiveOf() {
        {
            final UpToFiveOf<Integer> t = Tuple.upToFiveOf();
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.upToTwo());
            assertNotEquals(t, Tuple.upToTwo(1));
            
            assertEquals("()", t.toString());

            assertEquals(0, t.getCount());
            assertNull(t.t);
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
        }
        
        {
            final UpToFiveOf<Integer> t = Tuple.upToFiveOf(1);
            
            assertEquals(Objects.hash(1), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.upToTwo(1));
            assertNotEquals(t, Tuple.upToTwo(2));
            
            assertEquals("(1)", t.toString());

            assertEquals(1, t.getCount());
            assertEquals(1, t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            assertEquals("(null)", Tuple.upToFiveOfNullable(null).toString());
        }
        
        {
            final UpToFiveOf<Integer> t = Tuple.upToFiveOf(1, 2);
            
            assertEquals(Objects.hash(1, 2), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of(1, 2));
            assertNotEquals(t, Tuple.of(2, 1));
            assertNotEquals(t, Tuple.of(1, 3));
            
            assertEquals("(1,2)", t.toString());

            assertEquals(2, t.getCount());
            assertEquals(1, t.get1());
            assertEquals(2, t.get2());
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            assertEquals("(null,1)", Tuple.upToFiveOfNullable(null, 1).toString());
        }
        
        {
            final UpToFiveOf<Integer> t = Tuple.upToFiveOf(1, 2, 3);
            
            assertEquals(Objects.hash(1, 2, 3), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of(1, 2, 3));
            assertNotEquals(t, Tuple.of(2, 1, 3));
            assertNotEquals(t, Tuple.of(1, 2, 4));
            
            assertEquals("(1,2,3)", t.toString());

            assertEquals(3, t.getCount());
            assertEquals(1, t.get1());
            assertEquals(2, t.get2());
            assertEquals(3, t.get3());
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            assertEquals("(null,1,2)", Tuple.upToFiveOfNullable(null, 1, 2).toString());
        }
        
        {
            final UpToFiveOf<Integer> t = Tuple.upToFiveOf(1, 2, 3, 4);
            
            assertEquals(Objects.hash(1, 2, 3, 4), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of(1, 2, 3, 4));
            assertNotEquals(t, Tuple.of(2, 1, 3, 4));
            assertNotEquals(t, Tuple.of(1, 2, 3, 5));
            
            assertEquals("(1,2,3,4)", t.toString());

            assertEquals(4, t.getCount());
            assertEquals(1, t.get1());
            assertEquals(2, t.get2());
            assertEquals(3, t.get3());
            assertEquals(4, t.get4());
            assertNull(t.x);
            assertNull(t.y);
            
            assertEquals("(null,1,2,3)", Tuple.upToFiveOfNullable(null, 1, 2, 3).toString());
        }
        
        {
            final UpToFiveOf<Integer> t = Tuple.upToFiveOf(1, 2, 3, 4, 5);
            
            assertEquals(Objects.hash(1, 2, 3, 4, 5), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of(1, 2, 3, 4, 5));
            assertNotEquals(t, Tuple.of(2, 1, 3, 4, 5));
            assertNotEquals(t, Tuple.of(1, 2, 3, 4, 6));
            
            assertEquals("(1,2,3,4,5)", t.toString());

            assertEquals(5, t.getCount());
            assertEquals(1, t.get1());
            assertEquals(2, t.get2());
            assertEquals(3, t.get3());
            assertEquals(4, t.get4());
            assertEquals(5, t.get5());
            assertNull(t.y);
            
            assertEquals("(null,1,2,3,4)", Tuple.upToFiveOfNullable(null, 1, 2, 3, 4).toString());
        }
    }
    
    @Test
    public void six() {
        final Six<String, Integer, String, Integer, String, Integer> t = Tuple.of("abc", 1, "def", 2, "ghi", 3);
        
        assertEquals(Objects.hash("abc", 1, "def", 2, "ghi", 3), t.hashCode());
        
        assertEquals(t, t);
        assertEquals(t, Tuple.of("abc", 1, "def", 2, "ghi", 3));
        assertNotEquals(t, Tuple.of(1, "abc", "def", 2, "ghi", 3));
        assertNotEquals(t, Tuple.of("abc", 1, "def", 2, "ghi", 4));
        
        assertEquals("(abc,1,def,2,ghi,3)", t.toString());

        assertEquals(6, t.count);
        assertEquals("abc", t.get1());
        assertEquals(1, t.get2());
        assertEquals("def", t.get3());
        assertEquals(2, t.get4());
        assertEquals("ghi", t.get5());
        assertEquals(3, t.get6());

        final Six<Integer, Integer, Integer, Integer, Integer, Integer> tt = Tuple.ofNullable(null, 1, 2, 3, 4, 5);
        assertTrue(Six.class == tt.getClass());
        assertEquals("(null,1,2,3,4,5)", tt.toString());
    }
    
    @Test
    public void sixOf() {
        final SixOf<Integer> t = Tuple.ofSame(1, 2, 3, 4, 5, 6);
        
        assertEquals(Objects.hash(1, 2, 3, 4, 5, 6), t.hashCode());
        
        assertEquals(t, t);
        assertEquals(t, Tuple.of(1, 2, 3, 4, 5, 6));
        assertNotEquals(t, Tuple.of(2, 1, 3, 4, 5, 6));
        assertNotEquals(t, Tuple.of(1, 2, 3, 4, 5, 7));
        
        assertEquals("(1,2,3,4,5,6)", t.toString());
        
        assertEquals(6, t.count);
        assertEquals(1, t.get1());
        assertEquals(2, t.get2());
        assertEquals(3, t.get3());
        assertEquals(4, t.get4());
        assertEquals(5, t.get5());
        assertEquals(6, t.get6());
        
        assertEquals("(null,1,2,3,4,5)", Tuple.ofSameNullable(null, 1, 2, 3, 4, 5).toString());
    }
    
    @Test
    public void upToSix() {
        {
            final UpToSix<String, Integer, String, Integer, String, Integer> t = Tuple.upToSix();
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.upToTwo());
            assertNotEquals(t, Tuple.upToTwo("abc"));
            
            assertEquals("()", t.toString());

            assertEquals(0, t.getCount());
            assertNull(t.t);
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
        }
        
        {
            final UpToSix<String, Integer, String, Integer, String, Integer> t = Tuple.upToSix("abc");
            
            assertEquals(Objects.hash("abc"), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.upToTwo("abc"));
            assertNotEquals(t, Tuple.upToTwo(1));
            
            assertEquals("(abc)", t.toString());

            assertEquals(1, t.getCount());
            assertEquals("abc", t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UpToSix<Integer, Integer, Integer, Integer, Integer, Integer> tt = Tuple.upToSixNullable(null);
            assertTrue(UpToSix.class == tt.getClass());
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UpToSix<String, Integer, String, Integer, String, Integer> t = Tuple.upToSix("abc", 1);
            
            assertEquals(Objects.hash("abc", 1), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of("abc", 1));
            assertNotEquals(t, Tuple.of(1, "abc"));
            assertNotEquals(t, Tuple.of("abc", 2));
            
            assertEquals("(abc,1)", t.toString());

            assertEquals(2, t.getCount());
            assertEquals("abc", t.get1());
            assertEquals(1, t.get2());
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UpToSix<Integer, Integer, Integer, Integer, Integer, Integer> tt = Tuple.upToSixNullable(null, 1);
            assertTrue(UpToSix.class == tt.getClass());
            assertEquals("(null,1)", tt.toString());
        }
        
        {
            final UpToSix<String, Integer, String, Integer, String, Integer> t = Tuple.upToSix("abc", 1, "def");
            
            assertEquals(Objects.hash("abc", 1, "def"), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of("abc", 1, "def"));
            assertNotEquals(t, Tuple.of(1, "abc", "def"));
            assertNotEquals(t, Tuple.of("abc", 1, 2));
            
            assertEquals("(abc,1,def)", t.toString());

            assertEquals(3, t.getCount());
            assertEquals("abc", t.get1());
            assertEquals(1, t.get2());
            assertEquals("def", t.get3());
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UpToSix<Integer, Integer, Integer, Integer, Integer, Integer> tt = Tuple.upToSixNullable(null, 1, 2);
            assertTrue(UpToSix.class == tt.getClass());
            assertEquals("(null,1,2)", tt.toString());
        }
        
        {
            final UpToSix<String, Integer, String, Integer, String, Integer> t = Tuple.upToSix("abc", 1, "def", 2);
            
            assertEquals(Objects.hash("abc", 1, "def", 2), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of("abc", 1, "def", 2));
            assertNotEquals(t, Tuple.of(1, "abc", "def", 2));
            assertNotEquals(t, Tuple.of("abc", 1, "def", 3));
            
            assertEquals("(abc,1,def,2)", t.toString());

            assertEquals(4, t.getCount());
            assertEquals("abc", t.get1());
            assertEquals(1, t.get2());
            assertEquals("def", t.get3());
            assertEquals(2, t.get4());
            assertNull(t.x);
            assertNull(t.y);

            final UpToSix<Integer, Integer, Integer, Integer, Integer, Integer> tt = Tuple.upToSixNullable(null, 1, 2, 3);
            assertTrue(UpToSix.class == tt.getClass());
            assertEquals("(null,1,2,3)", tt.toString());
        }
        
        {
            final UpToSix<String, Integer, String, Integer, String, Integer> t = Tuple.upToSix("abc", 1, "def", 2, "ghi");
            
            assertEquals(Objects.hash("abc", 1, "def", 2, "ghi"), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of("abc", 1, "def", 2, "ghi"));
            assertNotEquals(t, Tuple.of(1, "abc", "def", 2, "ghi"));
            assertNotEquals(t, Tuple.of("abc", 1, "def", 2, 3));
            
            assertEquals("(abc,1,def,2,ghi)", t.toString());

            assertEquals(5, t.getCount());
            assertEquals("abc", t.get1());
            assertEquals(1, t.get2());
            assertEquals("def", t.get3());
            assertEquals(2, t.get4());
            assertEquals("ghi", t.get5());
            assertNull(t.y);

            final UpToSix<Integer, Integer, Integer, Integer, Integer, Integer> tt = Tuple.upToSixNullable(null, 1, 2, 3, 4);
            assertTrue(UpToSix.class == tt.getClass());
            assertEquals("(null,1,2,3,4)", tt.toString());
        }
        
        {
            final UpToSix<String, Integer, String, Integer, String, Integer> t = Tuple.upToSix("abc", 1, "def", 2, "ghi", 3);
            
            assertEquals(Objects.hash("abc", 1, "def", 2, "ghi", 3), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of("abc", 1, "def", 2, "ghi", 3));
            assertNotEquals(t, Tuple.of(1, "abc", "def", 2, "ghi", 3));
            assertNotEquals(t, Tuple.of("abc", 1, "def", 2, "ghi", 4));
            
            assertEquals("(abc,1,def,2,ghi,3)", t.toString());

            assertEquals(6, t.getCount());
            assertEquals("abc", t.get1());
            assertEquals(1, t.get2());
            assertEquals("def", t.get3());
            assertEquals(2, t.get4());
            assertEquals("ghi", t.get5());
            assertEquals(3, t.get6());

            final UpToSix<Integer, Integer, Integer, Integer, Integer, Integer> tt = Tuple.upToSixNullable(null, 1, 2, 3, 4, 5);
            assertTrue(UpToSix.class == tt.getClass());
            assertEquals("(null,1,2,3,4,5)", tt.toString());
        }
    }
    
    @Test
    public void upToSixOf() {
        {
            final UpToSixOf<Integer> t = Tuple.upToSixOf();
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.upToTwo());
            assertNotEquals(t, Tuple.upToTwo(1));
            
            assertEquals("()", t.toString());

            assertEquals(0, t.getCount());
            assertNull(t.t);
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
        }
        
        {
            final UpToSixOf<Integer> t = Tuple.upToSixOf(1);
            
            assertEquals(Objects.hash(1), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.upToTwo(1));
            assertNotEquals(t, Tuple.upToTwo(2));
            
            assertEquals("(1)", t.toString());

            assertEquals(1, t.getCount());
            assertEquals(1, t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            assertEquals("(null)", Tuple.upToSixOfNullable(null).toString());
        }
        
        {
            final UpToSixOf<Integer> t = Tuple.upToSixOf(1, 2);
            
            assertEquals(Objects.hash(1, 2), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of(1, 2));
            assertNotEquals(t, Tuple.of(2, 1));
            assertNotEquals(t, Tuple.of(1, 3));
            
            assertEquals("(1,2)", t.toString());

            assertEquals(2, t.getCount());
            assertEquals(1, t.get1());
            assertEquals(2, t.get2());
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            assertEquals("(null,1)", Tuple.upToSixOfNullable(null, 1).toString());
        }
        
        {
            final UpToSixOf<Integer> t = Tuple.upToSixOf(1, 2, 3);
            
            assertEquals(Objects.hash(1, 2, 3), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of(1, 2, 3));
            assertNotEquals(t, Tuple.of(2, 1, 3));
            assertNotEquals(t, Tuple.of(1, 2, 4));
            
            assertEquals("(1,2,3)", t.toString());

            assertEquals(3, t.getCount());
            assertEquals(1, t.get1());
            assertEquals(2, t.get2());
            assertEquals(3, t.get3());
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            assertEquals("(null,1,2)", Tuple.upToSixOfNullable(null, 1, 2).toString());
        }
        
        {
            final UpToSixOf<Integer> t = Tuple.upToSixOf(1, 2, 3, 4);
            
            assertEquals(Objects.hash(1, 2, 3, 4), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of(1, 2, 3, 4));
            assertNotEquals(t, Tuple.of(2, 1, 3, 4));
            assertNotEquals(t, Tuple.of(1, 2, 3, 5));
            
            assertEquals("(1,2,3,4)", t.toString());

            assertEquals(4, t.getCount());
            assertEquals(1, t.get1());
            assertEquals(2, t.get2());
            assertEquals(3, t.get3());
            assertEquals(4, t.get4());
            assertNull(t.x);
            assertNull(t.y);
            
            assertEquals("(null,1,2,3)", Tuple.upToSixOfNullable(null, 1, 2, 3).toString());
        }
        
        {
            final UpToSixOf<Integer> t = Tuple.upToSixOf(1, 2, 3, 4, 5);
            
            assertEquals(Objects.hash(1, 2, 3, 4, 5), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of(1, 2, 3, 4, 5));
            assertNotEquals(t, Tuple.of(2, 1, 3, 4, 5));
            assertNotEquals(t, Tuple.of(1, 2, 3, 4, 6));
            
            assertEquals("(1,2,3,4,5)", t.toString());

            assertEquals(5, t.getCount());
            assertEquals(1, t.get1());
            assertEquals(2, t.get2());
            assertEquals(3, t.get3());
            assertEquals(4, t.get4());
            assertEquals(5, t.get5());
            assertNull(t.y);
            
            assertEquals("(null,1,2,3,4)", Tuple.upToSixOfNullable(null, 1, 2, 3, 4).toString());
        }
        
        {
            final UpToSixOf<Integer> t = Tuple.upToSixOf(1, 2, 3, 4, 5, 6);
            
            assertEquals(Objects.hash(1, 2, 3, 4, 5, 6), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.of(1, 2, 3, 4, 5, 6));
            assertNotEquals(t, Tuple.of(2, 1, 3, 4, 5, 6));
            assertNotEquals(t, Tuple.of(1, 2, 3, 4, 5, 7));
            
            assertEquals("(1,2,3,4,5,6)", t.toString());

            assertEquals(6, t.getCount());
            assertEquals(1, t.get1());
            assertEquals(2, t.get2());
            assertEquals(3, t.get3());
            assertEquals(4, t.get4());
            assertEquals(5, t.get5());
            assertEquals(6, t.get6());
            
            assertEquals("(null,1,2,3,4,5)", Tuple.upToSixOfNullable(null, 1, 2, 3, 4, 5).toString());
        }
    }
}
