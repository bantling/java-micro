package me.bantling.micro.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
import me.bantling.micro.util.Tuple.UnionFive;
import me.bantling.micro.util.Tuple.UnionFiveOf;
import me.bantling.micro.util.Tuple.UnionFour;
import me.bantling.micro.util.Tuple.UnionFourOf;
import me.bantling.micro.util.Tuple.UnionSix;
import me.bantling.micro.util.Tuple.UnionSixOf;
import me.bantling.micro.util.Tuple.UnionThree;
import me.bantling.micro.util.Tuple.UnionThreeOf;
import me.bantling.micro.util.Tuple.UnionTwo;
import me.bantling.micro.util.Tuple.UnionTwoOf;
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

@SuppressWarnings({ "static-method", "boxing", "static-access"})
public class TestTuple {
    
    @Test
    public void two() {
        final Tuple tpl = new Tuple();
        final Two<String, Integer> t = tpl.of("abc", 1);

        assertTrue(Two.class == t.getClass());
        assertEquals(Objects.hash("abc", 1), t.hashCode());
        
        assertEquals(t, t);
        assertEquals(t, tpl.of("abc", 1));
        assertNotEquals(t, tpl.of(1, "abc"));
        assertNotEquals(t, tpl.of("abc", 2));
        assertNotEquals(t, "");
        
        assertEquals("(abc,1)", t.toString());
        
        assertEquals(2, t.size);
        assertEquals("abc", t.get1());
        assertEquals(1, t.get2());
        assertNull(t.v);
        assertNull(t.w);
        assertNull(t.x);
        assertNull(t.y);

        final Two<Integer, Integer> tt = tpl.ofNullable(null, 1);
        assertTrue(Two.class == tt.getClass());
        assertEquals("(null,1)", tt.toString());
    }
    
    @Test
    public void twoOf() {
        final Tuple.Same same = new Tuple.Same();
        final TwoOf<Integer> t = same.of(1, 2);
        
        assertEquals(Objects.hash(1, 2), t.hashCode());
        
        assertEquals(t, t);
        assertEquals(t, Tuple.of(1, 2));
        assertNotEquals(t, Tuple.of(2, 1));
        assertNotEquals(t, Tuple.of(1, 3));
        assertNotEquals(t, "");
        
        assertEquals("(1,2)", t.toString());
        
        assertEquals(2, t.size);
        assertEquals(1, t.get1());
        assertEquals(2, t.get2());
        assertNull(t.v);
        assertNull(t.w);
        assertNull(t.x);
        assertNull(t.y);

        final TwoOf<Integer> tt = same.ofNullable(null, 1);
        assertEquals("(null,1)", tt.toString());
    }
    
    @Test
    public void upToTwo() {
        final Tuple.UpTo upto = new Tuple.UpTo();
        
        {
            final UpToTwo<String, Integer> t = upto.two();
            
            assertEquals(Objects.hash(), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, upto.two());
            assertNotEquals(t, upto.two(1));
            assertNotEquals(t, "");
            
            assertEquals("()", t.toString());
            
            assertEquals(2, t.size);
            assertEquals(0, t.count);
            assertNull(t.t);
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
        }
        
        {
            final UpToTwo<String, Integer> t = upto.two("abc");
            
            assertEquals(Objects.hash("abc"), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, upto.two("abc"));
            assertNotEquals(t, upto.two(1));
            assertNotEquals(t, "");
            
            assertEquals("(abc)", t.toString());
            
            assertEquals(1, t.getCount());
            assertEquals("abc", t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UpToTwo<Integer, Integer> tt = upto.twoNullable(null);
            assertTrue(UpToTwo.class == tt.getClass());
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UpToTwo<String, Integer> t = upto.two("abc", 1);
            
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

            final UpToTwo<Integer, Integer> tt = upto.twoNullable(null, 1);
            assertTrue(UpToTwo.class == tt.getClass());
            assertEquals("(null,1)", tt.toString());
        }
    }
    
    @Test
    public void upToTwoOf() {
        final Tuple.UpTo upto = new Tuple.UpTo();
        final Tuple.UpToSame uptoSame = new Tuple.UpToSame();
        {
            final UpToTwoOf<Integer> t = uptoSame.two();
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, upto.two());
            assertNotEquals(t, upto.two(1));
            
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
            final UpToTwoOf<Integer> t = uptoSame.two(1);
            
            assertEquals(Objects.hash(1), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, upto.two(1));
            assertNotEquals(t, upto.two(2));
            
            assertEquals("(1)", t.toString());

            assertEquals(1, t.getCount());
            assertEquals(1, t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UpToTwoOf<Integer> tt = uptoSame.twoNullable(null);
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UpToTwoOf<Integer> t = uptoSame.two(1, 2);
            
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

            final UpToTwoOf<Integer> tt = uptoSame.twoNullable(null, 1);
            assertEquals("(null,1)", tt.toString());
        }
    }
    
    @Test
    public void unionTwo() {
        final Tuple.Union union = new Tuple.Union();
        
        {
            final UnionTwo<String, Integer> t = union.twoFirst("abc");
            
            assertEquals(Objects.hashCode("abc"), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, union.twoFirst("abc"));
            assertNotEquals(t, union.twoFirst(1));
            assertNotEquals(t, "");
            
            assertEquals("(abc)", t.toString());
            
            assertEquals(1, t.getPosition());
            assertEquals("abc", t.get1());
            
            try {
                t.get2();
                fail();
            } catch (final Throwable th) {
                assertEquals(
                    String.format(Tuple.UNION_ERROR_FMT, 1),
                    th.getMessage()
                );
            }
            
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UnionTwo<Integer, Integer> tt = union.twoFirstNullable(null);
            assertTrue(UnionTwo.class == tt.getClass());
            assertEquals(0, tt.hashCode());
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UnionTwo<String, Integer> t = union.twoSecond(1);
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.UpTo.twoNullable(null, 1));
            assertNotEquals(t, Tuple.of("abc", 1));
            assertNotEquals(t, "");
            
            assertEquals("(1)", t.toString());

            assertEquals(2, t.getPosition());
            assertEquals(1, t.get2());
            
            try {
                t.get1();
                fail();
            } catch (final Throwable th) {
                assertEquals(
                    String.format(Tuple.UNION_ERROR_FMT, 2),
                    th.getMessage()
                );
            }

            assertNull(t.t);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UnionTwo<Integer, Integer> tt = union.twoSecondNullable(null);
            assertTrue(UnionTwo.class == tt.getClass());
            assertEquals(0, tt.hashCode());
            assertEquals("(null)", tt.toString());
        }
    }
    
    @Test
    public void unionTwoOf() {
        final Tuple.Union union = new Tuple.Union();
        final Tuple.UnionSame unionSame = new Tuple.UnionSame();
        
        {
            final UnionTwoOf<Integer> t = unionSame.twoFirst(1);
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, union.twoFirst(1));
            assertNotEquals(t, union.twoSecond(1));
            
            assertEquals("(1)", t.toString());

            assertEquals(1, t.getPosition());
            assertEquals(1, t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UnionTwoOf<Integer> tt = unionSame.twoFirstNullable(null);
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UnionTwoOf<Integer> t = unionSame.twoSecond(2);
            
            assertEquals(2, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.ofNullable(null, 2));
            assertNotEquals(t, Tuple.of(1, 2));
            
            assertEquals("(2)", t.toString());

            assertEquals(2, t.getPosition());
            assertEquals(2, t.get2());
            assertNull(t.t);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UnionTwoOf<Integer> tt = unionSame.twoSecondNullable(null);
            assertEquals(0, tt.hashCode());
            assertEquals("(null)", tt.toString());
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

        assertEquals(3, t.size);
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
        final Tuple.Same same = new Tuple.Same();
        final ThreeOf<Integer> t = same.of(1, 2, 3);
        
        assertEquals(Objects.hash(1, 2, 3), t.hashCode());
        
        assertEquals(t, t);
        assertEquals(t, Tuple.of(1, 2, 3));
        assertNotEquals(t, Tuple.of(2, 1, 3));
        assertNotEquals(t, Tuple.of(1, 2, 4));
        
        assertEquals("(1,2,3)", t.toString());
        
        assertEquals(3, t.size);
        assertEquals(1, t.get1());
        assertEquals(2, t.get2());
        assertEquals(3, t.get3());
        assertNull(t.w);
        assertNull(t.x);
        assertNull(t.y);
        
        final ThreeOf<Integer> tt = same.ofNullable(null, 1, 2);
        assertEquals("(null,1,2)", tt.toString());
    }
    
    @Test
    public void upToThree() {
        final Tuple.UpTo upto = new Tuple.UpTo();
        {
            final UpToThree<String, Integer, String> t = upto.three();
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, upto.three());
            assertNotEquals(t, upto.three("abc"));
            
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
            final UpToThree<String, Integer, String> t = upto.three("abc");
            assertEquals(Objects.hash("abc"), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, upto.three("abc"));
            assertNotEquals(t, upto.three(1));
            
            assertEquals("(abc)", t.toString());

            assertEquals(1, t.getCount());
            assertEquals("abc", t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            final UpToThree<Integer, Integer, Integer> tt = upto.threeNullable(null);
            assertTrue(UpToThree.class == tt.getClass());
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UpToThree<String, Integer, String> t = upto.three("abc", 1);
            
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
            
            final UpToThree<Integer, Integer, Integer> tt = upto.threeNullable(null, 1);
            assertTrue(UpToThree.class == tt.getClass());
            assertEquals("(null,1)", tt.toString());
        }
        
        {
            final UpToThree<String, Integer, String> t = upto.three("abc", 1, "def");
            
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
            
            final UpToThree<Integer, Integer, Integer> tt = upto.threeNullable(null, 1, 2);
            assertTrue(UpToThree.class == tt.getClass());
            assertEquals("(null,1,2)", tt.toString());
        }
    }
    
    @Test
    public void upToThreeOf() {
        final Tuple.UpTo upto = new Tuple.UpTo();
        final Tuple.UpToSame uptoSame = new Tuple.UpToSame();
        {
            final UpToThreeOf<Integer> t = uptoSame.three();
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, upto.three());
            assertNotEquals(t, upto.three(1));
            
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
            final UpToThreeOf<Integer> t = uptoSame.three(1);
            
            assertEquals(Objects.hash(1), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, upto.three(1));
            assertNotEquals(t, upto.three(2));
            
            assertEquals("(1)", t.toString());

            assertEquals(1, t.getCount());
            assertEquals(1, t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            final UpToThreeOf<Integer> tt = uptoSame.threeNullable(null);
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UpToThreeOf<Integer> t = uptoSame.three(1, 2);
            
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
            
            assertEquals("(null,1)", uptoSame.threeNullable(null, 1).toString());
        }
        
        {
            final UpToThreeOf<Integer> t = uptoSame.three(1, 2, 3);
            
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
            
            assertEquals("(null,1,2)", uptoSame.threeNullable(null, 1, 2).toString());
        }
    }
    
    @Test
    public void unionThree() {
        final Tuple.Union union = new Tuple.Union();
        
        {
            final UnionThree<String, Integer, String> t = union.threeFirst("abc");
            
            assertEquals(Objects.hashCode("abc"), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, union.twoFirst("abc"));
            assertNotEquals(t, union.twoFirst(1));
            assertNotEquals(t, "");
            
            assertEquals("(abc)", t.toString());
            
            assertEquals(1, t.getPosition());
            assertEquals("abc", t.get1());
            
            try {
                t.get2();
                fail();
            } catch (final Throwable th) {
                assertEquals(
                    String.format(Tuple.UNION_ERROR_FMT, 1),
                    th.getMessage()
                );
            }
            
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UnionThree<Integer, Integer, Integer> tt = union.threeFirstNullable(null);
            assertTrue(UnionThree.class == tt.getClass());
            assertEquals(0, tt.hashCode());
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UnionThree<String, Integer, String> t = union.threeThird("abc");
            
            assertEquals("abc".hashCode(), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.UpTo.threeNullable(null, null, "abc"));
            assertNotEquals(t, Tuple.of("abc", 1));
            assertNotEquals(t, "");
            
            assertEquals("(abc)", t.toString());

            assertEquals(3, t.getPosition());
            assertEquals("abc", t.get3());
            
            try {
                t.get1();
                fail();
            } catch (final Throwable th) {
                assertEquals(
                    String.format(Tuple.UNION_ERROR_FMT, 3),
                    th.getMessage()
                );
            }

            assertNull(t.t);
            assertNull(t.u);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UnionThree<Integer, Integer, Integer> tt = union.threeSecondNullable(null);
            assertTrue(UnionThree.class == tt.getClass());
            assertEquals(0, tt.hashCode());
            assertEquals("(null)", tt.toString());
        }
    }
    
    @Test
    public void unionThreeOf() {
        final Tuple.Union union = new Tuple.Union();
        final Tuple.UnionSame unionSame = new Tuple.UnionSame();
        
        {
            final UnionThreeOf<Integer> t = unionSame.threeFirst(1);
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, union.twoFirst(1));
            assertNotEquals(t, union.twoSecond(1));
            
            assertEquals("(1)", t.toString());

            assertEquals(1, t.getPosition());
            assertEquals(1, t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UnionThreeOf<Integer> tt = unionSame.threeFirstNullable(null);
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UnionThreeOf<Integer> t = unionSame.threeThird(2);
            
            assertEquals(2, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.ofNullable(null, null, 2));
            assertNotEquals(t, Tuple.of(1, 2));
            
            assertEquals("(2)", t.toString());

            assertEquals(3, t.getPosition());
            assertEquals(2, t.get3());
            assertNull(t.t);
            assertNull(t.u);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UnionThreeOf<Integer> tt = unionSame.threeThirdNullable(null);
            assertEquals(0, tt.hashCode());
            assertEquals("(null)", tt.toString());
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

        assertEquals(4, t.size);
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
        final Tuple.Same same = new Tuple.Same();
        final FourOf<Integer> t = same.of(1, 2, 3, 4);
        
        assertEquals(Objects.hash(1, 2, 3, 4), t.hashCode());
        
        assertEquals(t, t);
        assertEquals(t, Tuple.of(1, 2, 3, 4));
        assertNotEquals(t, Tuple.of(2, 1, 3, 4));
        assertNotEquals(t, Tuple.of(1, 2, 3, 5));
        
        assertEquals("(1,2,3,4)", t.toString());
        
        assertEquals(4, t.size);
        assertEquals(1, t.get1());
        assertEquals(2, t.get2());
        assertEquals(3, t.get3());
        assertEquals(4, t.get4());
        assertNull(t.x);
        assertNull(t.y);
        
        assertEquals("(null,1,2,3)", same.ofNullable(null,1,2,3).toString());
    }
    
    @Test
    public void upToFour() {
        final Tuple.UpTo upto = new Tuple.UpTo();
        {
            final UpToFour<String, Integer, String, Integer> t = upto.four();
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, upto.four());
            assertNotEquals(t, upto.four("abc"));
            
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
            final UpToFour<String, Integer, String, Integer> t = upto.four("abc");
            
            assertEquals(Objects.hash("abc"), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, upto.four("abc"));
            assertNotEquals(t, upto.four(1));
            
            assertEquals("(abc)", t.toString());

            assertEquals(1, t.getCount());
            assertEquals("abc", t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UpToFour<Integer, Integer, Integer, Integer> tt = upto.fourNullable(null);
            assertTrue(UpToFour.class == tt.getClass());
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UpToFour<String, Integer, String, Integer> t = upto.four("abc", 1);
            
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

            final UpToFour<Integer, Integer, Integer, Integer> tt = upto.fourNullable(null, 1);
            assertTrue(UpToFour.class == tt.getClass());
            assertEquals("(null,1)", tt.toString());
        }
        
        {
            final UpToFour<String, Integer, String, Integer> t = upto.four("abc", 1, "def");
            
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

            final UpToFour<Integer, Integer, Integer, Integer> tt = upto.fourNullable(null, 1, 2);
            assertTrue(UpToFour.class == tt.getClass());
            assertEquals("(null,1,2)", tt.toString());
        }
        
        {
            final UpToFour<String, Integer, String, Integer> t = upto.four("abc", 1, "def", 2);
            
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

            final UpToFour<Integer, Integer, Integer, Integer> tt = upto.fourNullable(null, 1, 2, 3);
            assertTrue(UpToFour.class == tt.getClass());
            assertEquals("(null,1,2,3)", tt.toString());
        }
    }
    
    @Test
    public void upToFourOf() {
        final Tuple.UpTo upto = new Tuple.UpTo();
        final Tuple.UpToSame uptoSame = new Tuple.UpToSame();
        {
            final UpToFourOf<Integer> t = uptoSame.four();
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, upto.four());
            assertNotEquals(t, upto.four(1));
            
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
            final UpToFourOf<Integer> t = uptoSame.four(1);
            
            assertEquals(Objects.hash(1), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, upto.four(1));
            assertNotEquals(t, upto.four(2));
            
            assertEquals("(1)", t.toString());

            assertEquals(1, t.getCount());
            assertEquals(1, t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            assertEquals("(null)", uptoSame.fourNullable(null).toString());
        }
        
        {
            final UpToFourOf<Integer> t = uptoSame.four(1, 2);
            
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
            
            assertEquals("(null,1)", uptoSame.fourNullable(null, 1).toString());
        }
        
        {
            final UpToFourOf<Integer> t = uptoSame.four(1, 2, 3);
            
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
            
            assertEquals("(null,1,2)", uptoSame.fourNullable(null, 1, 2).toString());
        }
        
        {
            final UpToFourOf<Integer> t = uptoSame.four(1, 2, 3, 4);
            
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
            
            assertEquals("(null,1,2,3)", uptoSame.fourNullable(null, 1, 2, 3).toString());
        }
    }
    
    @Test
    public void unionFour() {
        final Tuple.Union union = new Tuple.Union();
        
        {
            final UnionFour<String, Integer, String, Integer> t = union.fourFirst("abc");
            
            assertEquals(Objects.hashCode("abc"), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, union.twoFirst("abc"));
            assertNotEquals(t, union.twoFirst(1));
            assertNotEquals(t, "");
            
            assertEquals("(abc)", t.toString());
            
            assertEquals(1, t.getPosition());
            assertEquals("abc", t.get1());
            
            try {
                t.get2();
                fail();
            } catch (final Throwable th) {
                assertEquals(
                    String.format(Tuple.UNION_ERROR_FMT, 1),
                    th.getMessage()
                );
            }
            
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UnionFour<Integer, Integer, Integer, Integer> tt = union.fourFirstNullable(null);
            assertTrue(UnionFour.class == tt.getClass());
            assertEquals(0, tt.hashCode());
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UnionFour<String, Integer, String, Integer> t = union.fourFourth(1);
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.UpTo.fourNullable(null, null, null, 1));
            assertNotEquals(t, Tuple.of("abc", 1));
            assertNotEquals(t, "");
            
            assertEquals("(1)", t.toString());

            assertEquals(4, t.getPosition());
            assertEquals(1, t.get4());
            
            try {
                t.get1();
                fail();
            } catch (final Throwable th) {
                assertEquals(
                    String.format(Tuple.UNION_ERROR_FMT, 4),
                    th.getMessage()
                );
            }

            assertNull(t.t);
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.x);
            assertNull(t.y);

            final UnionFour<Integer, Integer, Integer, Integer> tt = union.fourSecondNullable(null);
            assertTrue(UnionFour.class == tt.getClass());
            assertEquals(0, tt.hashCode());
            assertEquals("(null)", tt.toString());
        }
    }
    
    @Test
    public void unionFourOf() {
        final Tuple.Union union = new Tuple.Union();
        final Tuple.UnionSame unionSame = new Tuple.UnionSame();
        
        {
            final UnionFourOf<Integer> t = unionSame.fourFirst(1);
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, union.twoFirst(1));
            assertNotEquals(t, union.twoSecond(1));
            
            assertEquals("(1)", t.toString());

            assertEquals(1, t.getPosition());
            assertEquals(1, t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UnionFourOf<Integer> tt = unionSame.fourFirstNullable(null);
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UnionFourOf<Integer> t = unionSame.fourFourth(2);
            
            assertEquals(2, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.ofNullable(null, null, null, 2));
            assertNotEquals(t, Tuple.of(1, 2));
            
            assertEquals("(2)", t.toString());

            assertEquals(4, t.getPosition());
            assertEquals(2, t.get4());
            assertNull(t.t);
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.x);
            assertNull(t.y);

            final UnionFourOf<Integer> tt = unionSame.fourFourthNullable(null);
            assertEquals(0, tt.hashCode());
            assertEquals("(null)", tt.toString());
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

        assertEquals(5, t.size);
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
        final Tuple.Same same = new Tuple.Same();
        final FiveOf<Integer> t = same.of(1, 2, 3, 4, 5);
        
        assertEquals(Objects.hash(1, 2, 3, 4, 5), t.hashCode());
        
        assertEquals(t, t);
        assertEquals(t, Tuple.of(1, 2, 3, 4, 5));
        assertNotEquals(t, Tuple.of(2, 1, 3, 4, 5));
        assertNotEquals(t, Tuple.of(1, 2, 3, 4, 6));
        
        assertEquals("(1,2,3,4,5)", t.toString());
        
        assertEquals(5, t.size);
        assertEquals(1, t.get1());
        assertEquals(2, t.get2());
        assertEquals(3, t.get3());
        assertEquals(4, t.get4());
        assertEquals(5, t.get5());
        assertNull(t.y);
        
        assertEquals("(null,1,2,3,4)", same.ofNullable(null, 1, 2, 3, 4).toString());
    }
    
    @Test
    public void upToFive() {
        final Tuple.UpTo upto = new Tuple.UpTo();
        {
            final UpToFive<String, Integer, String, Integer, String> t = upto.five();
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, upto.five());
            assertNotEquals(t, upto.five("abc"));
            
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
            final UpToFive<String, Integer, String, Integer, String> t = upto.five("abc");
            
            assertEquals(Objects.hash("abc"), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, upto.five("abc"));
            assertNotEquals(t, upto.five(1));
            
            assertEquals("(abc)", t.toString());

            assertEquals(1, t.getCount());
            assertEquals("abc", t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UpToFive<Integer, Integer, Integer, Integer, Integer> tt = upto.fiveNullable(null);
            assertTrue(UpToFive.class == tt.getClass());
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UpToFive<String, Integer, String, Integer, String> t = upto.five("abc", 1);
            
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

            final UpToFive<Integer, Integer, Integer, Integer, Integer> tt = upto.fiveNullable(null, 1);
            assertTrue(UpToFive.class == tt.getClass());
            assertEquals("(null,1)", tt.toString());
        }
        
        {
            final UpToFive<String, Integer, String, Integer, String> t = upto.five("abc", 1, "def");
            
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

            final UpToFive<Integer, Integer, Integer, Integer, Integer> tt = upto.fiveNullable(null, 1, 2);
            assertTrue(UpToFive.class == tt.getClass());
            assertEquals("(null,1,2)", tt.toString());
        }
        
        {
            final UpToFive<String, Integer, String, Integer, String> t = upto.five("abc", 1, "def", 2);
            
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

            final UpToFive<Integer, Integer, Integer, Integer, Integer> tt = upto.fiveNullable(null, 1, 2, 3);
            assertTrue(UpToFive.class == tt.getClass());
            assertEquals("(null,1,2,3)", tt.toString());
        }
        
        {
            final UpToFive<String, Integer, String, Integer, String> t = upto.five("abc", 1, "def", 2, "ghi");
            
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

            final UpToFive<Integer, Integer, Integer, Integer, Integer> tt = upto.fiveNullable(null, 1, 2, 3, 4);
            assertTrue(UpToFive.class == tt.getClass());
            assertEquals("(null,1,2,3,4)", tt.toString());
        }
    }
    
    @Test
    public void upToFiveOf() {
        final Tuple.UpTo upto = new Tuple.UpTo();
        final Tuple.UpToSame uptoSame = new Tuple.UpToSame();
        {
            final UpToFiveOf<Integer> t = uptoSame.five();
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, upto.five());
            assertNotEquals(t, upto.five(1));
            
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
            final UpToFiveOf<Integer> t = uptoSame.five(1);
            
            assertEquals(Objects.hash(1), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, upto.five(1));
            assertNotEquals(t, upto.five(2));
            
            assertEquals("(1)", t.toString());

            assertEquals(1, t.getCount());
            assertEquals(1, t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            assertEquals("(null)", uptoSame.fiveNullable(null).toString());
        }
        
        {
            final UpToFiveOf<Integer> t = uptoSame.five(1, 2);
            
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
            
            assertEquals("(null,1)", uptoSame.fiveNullable(null, 1).toString());
        }
        
        {
            final UpToFiveOf<Integer> t = uptoSame.five(1, 2, 3);
            
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
            
            assertEquals("(null,1,2)", uptoSame.fiveNullable(null, 1, 2).toString());
        }
        
        {
            final UpToFiveOf<Integer> t = uptoSame.five(1, 2, 3, 4);
            
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
            
            assertEquals("(null,1,2,3)", uptoSame.fiveNullable(null, 1, 2, 3).toString());
        }
        
        {
            final UpToFiveOf<Integer> t = uptoSame.five(1, 2, 3, 4, 5);
            
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
            
            assertEquals("(null,1,2,3,4)", uptoSame.fiveNullable(null, 1, 2, 3, 4).toString());
        }
    }
    
    @Test
    public void unionFive() {
        final Tuple.Union union = new Tuple.Union();
        
        {
            final UnionFive<String, Integer, String, Integer, String> t = union.fiveFirst("abc");
            
            assertEquals(Objects.hashCode("abc"), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, union.twoFirst("abc"));
            assertNotEquals(t, union.twoFirst(1));
            assertNotEquals(t, "");
            
            assertEquals("(abc)", t.toString());
            
            assertEquals(1, t.getPosition());
            assertEquals("abc", t.get1());
            
            try {
                t.get2();
                fail();
            } catch (final Throwable th) {
                assertEquals(
                    String.format(Tuple.UNION_ERROR_FMT, 1),
                    th.getMessage()
                );
            }
            
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UnionFive<Integer, Integer, Integer, Integer, Integer> tt = union.fiveFirstNullable(null);
            assertTrue(UnionFive.class == tt.getClass());
            assertEquals(0, tt.hashCode());
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UnionFive<String, Integer, String, Integer, String> t = union.fiveFifth("abc");
            
            assertEquals("abc".hashCode(), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.UpTo.fiveNullable(null, null, null, null, "abc"));
            assertNotEquals(t, Tuple.of("abc", 1));
            assertNotEquals(t, "");
            
            assertEquals("(abc)", t.toString());

            assertEquals(5, t.getPosition());
            assertEquals("abc", t.get5());
            
            try {
                t.get1();
                fail();
            } catch (final Throwable th) {
                assertEquals(
                    String.format(Tuple.UNION_ERROR_FMT, 5),
                    th.getMessage()
                );
            }

            assertNull(t.t);
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.y);

            final UnionFive<Integer, Integer, Integer, Integer, Integer> tt = union.fiveSecondNullable(null);
            assertTrue(UnionFive.class == tt.getClass());
            assertEquals(0, tt.hashCode());
            assertEquals("(null)", tt.toString());
        }
    }
    
    @Test
    public void unionFiveOf() {
        final Tuple.Union union = new Tuple.Union();
        final Tuple.UnionSame unionSame = new Tuple.UnionSame();
        
        {
            final UnionFiveOf<Integer> t = unionSame.fiveFirst(1);
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, union.twoFirst(1));
            assertNotEquals(t, union.twoSecond(1));
            
            assertEquals("(1)", t.toString());

            assertEquals(1, t.getPosition());
            assertEquals(1, t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UnionFiveOf<Integer> tt = unionSame.fiveFirstNullable(null);
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UnionFiveOf<Integer> t = unionSame.fiveFifth(2);
            
            assertEquals(2, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.ofNullable(null, null, null, null, 2));
            assertNotEquals(t, Tuple.of(1, 2));
            
            assertEquals("(2)", t.toString());

            assertEquals(5, t.getPosition());
            assertEquals(2, t.get5());
            assertNull(t.t);
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.y);

            final UnionFiveOf<Integer> tt = unionSame.fiveFifthNullable(null);
            assertEquals(0, tt.hashCode());
            assertEquals("(null)", tt.toString());
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

        assertEquals(6, t.size);
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
        final Tuple.Same same = new Tuple.Same();
        final SixOf<Integer> t = same.of(1, 2, 3, 4, 5, 6);
        
        assertEquals(Objects.hash(1, 2, 3, 4, 5, 6), t.hashCode());
        
        assertEquals(t, t);
        assertEquals(t, Tuple.of(1, 2, 3, 4, 5, 6));
        assertNotEquals(t, Tuple.of(2, 1, 3, 4, 5, 6));
        assertNotEquals(t, Tuple.of(1, 2, 3, 4, 5, 7));
        
        assertEquals("(1,2,3,4,5,6)", t.toString());
        
        assertEquals(6, t.size);
        assertEquals(1, t.get1());
        assertEquals(2, t.get2());
        assertEquals(3, t.get3());
        assertEquals(4, t.get4());
        assertEquals(5, t.get5());
        assertEquals(6, t.get6());
        
        assertEquals("(null,1,2,3,4,5)", same.ofNullable(null, 1, 2, 3, 4, 5).toString());
    }
    
    @Test
    public void upToSix() {
        final Tuple.UpTo upto = new Tuple.UpTo();
        {
            final UpToSix<String, Integer, String, Integer, String, Integer> t = upto.six();
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, upto.six());
            assertNotEquals(t, upto.six("abc"));
            
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
            final UpToSix<String, Integer, String, Integer, String, Integer> t = upto.six("abc");
            
            assertEquals(Objects.hash("abc"), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, upto.six("abc"));
            assertNotEquals(t, upto.six(1));
            
            assertEquals("(abc)", t.toString());

            assertEquals(1, t.getCount());
            assertEquals("abc", t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UpToSix<Integer, Integer, Integer, Integer, Integer, Integer> tt = upto.sixNullable(null);
            assertTrue(UpToSix.class == tt.getClass());
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UpToSix<String, Integer, String, Integer, String, Integer> t = upto.six("abc", 1);
            
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

            final UpToSix<Integer, Integer, Integer, Integer, Integer, Integer> tt = upto.sixNullable(null, 1);
            assertTrue(UpToSix.class == tt.getClass());
            assertEquals("(null,1)", tt.toString());
        }
        
        {
            final UpToSix<String, Integer, String, Integer, String, Integer> t = upto.six("abc", 1, "def");
            
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

            final UpToSix<Integer, Integer, Integer, Integer, Integer, Integer> tt = upto.sixNullable(null, 1, 2);
            assertTrue(UpToSix.class == tt.getClass());
            assertEquals("(null,1,2)", tt.toString());
        }
        
        {
            final UpToSix<String, Integer, String, Integer, String, Integer> t = upto.six("abc", 1, "def", 2);
            
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

            final UpToSix<Integer, Integer, Integer, Integer, Integer, Integer> tt = upto.sixNullable(null, 1, 2, 3);
            assertTrue(UpToSix.class == tt.getClass());
            assertEquals("(null,1,2,3)", tt.toString());
        }
        
        {
            final UpToSix<String, Integer, String, Integer, String, Integer> t = upto.six("abc", 1, "def", 2, "ghi");
            
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

            final UpToSix<Integer, Integer, Integer, Integer, Integer, Integer> tt = upto.sixNullable(null, 1, 2, 3, 4);
            assertTrue(UpToSix.class == tt.getClass());
            assertEquals("(null,1,2,3,4)", tt.toString());
        }
        
        {
            final UpToSix<String, Integer, String, Integer, String, Integer> t = upto.six("abc", 1, "def", 2, "ghi", 3);
            
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

            final UpToSix<Integer, Integer, Integer, Integer, Integer, Integer> tt = upto.sixNullable(null, 1, 2, 3, 4, 5);
            assertTrue(UpToSix.class == tt.getClass());
            assertEquals("(null,1,2,3,4,5)", tt.toString());
        }
    }
    
    @Test
    public void upToSixOf() {
        final Tuple.UpTo upto = new Tuple.UpTo();
        final Tuple.UpToSame uptoSame = new Tuple.UpToSame();
        {
            final UpToSixOf<Integer> t = uptoSame.six();
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, upto.six());
            assertNotEquals(t, upto.six(1));
            
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
            final UpToSixOf<Integer> t = uptoSame.six(1);
            
            assertEquals(Objects.hash(1), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, upto.six(1));
            assertNotEquals(t, upto.six(2));
            
            assertEquals("(1)", t.toString());

            assertEquals(1, t.getCount());
            assertEquals(1, t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);
            
            assertEquals("(null)", uptoSame.sixNullable(null).toString());
        }
        
        {
            final UpToSixOf<Integer> t = uptoSame.six(1, 2);
            
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
            
            assertEquals("(null,1)", uptoSame.sixNullable(null, 1).toString());
        }
        
        {
            final UpToSixOf<Integer> t = uptoSame.six(1, 2, 3);
            
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
            
            assertEquals("(null,1,2)", uptoSame.sixNullable(null, 1, 2).toString());
        }
        
        {
            final UpToSixOf<Integer> t = uptoSame.six(1, 2, 3, 4);
            
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
            
            assertEquals("(null,1,2,3)", uptoSame.sixNullable(null, 1, 2, 3).toString());
        }
        
        {
            final UpToSixOf<Integer> t = uptoSame.six(1, 2, 3, 4, 5);
            
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
            
            assertEquals("(null,1,2,3,4)", uptoSame.sixNullable(null, 1, 2, 3, 4).toString());
        }
        
        {
            final UpToSixOf<Integer> t = uptoSame.six(1, 2, 3, 4, 5, 6);
            
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
            
            assertEquals("(null,1,2,3,4,5)", uptoSame.sixNullable(null, 1, 2, 3, 4, 5).toString());
        }
    }
    
    @Test
    public void unionSix() {
        final Tuple.Union union = new Tuple.Union();
        
        {
            final UnionSix<String, Integer, String, Integer, String, Integer> t = union.sixFirst("abc");
            
            assertEquals("abc".hashCode(), t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, union.twoFirst("abc"));
            assertNotEquals(t, union.twoFirst(1));
            assertNotEquals(t, "");
            
            assertEquals("(abc)", t.toString());
            
            assertEquals(1, t.getPosition());
            assertEquals("abc", t.get1());
            
            try {
                t.get2();
                fail();
            } catch (final Throwable th) {
                assertEquals(
                    String.format(Tuple.UNION_ERROR_FMT, 1),
                    th.getMessage()
                );
            }
            
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UnionSix<Integer, Integer, Integer, Integer, Integer, Integer> tt = union.sixFirstNullable(null);
            assertTrue(UnionSix.class == tt.getClass());
            assertEquals(0, tt.hashCode());
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UnionSix<String, Integer, String, Integer, String, Integer> t = union.sixSixth(1);
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.UpTo.sixNullable(null, null, null, null, null, 1));
            assertNotEquals(t, Tuple.of("abc", 1));
            assertNotEquals(t, "");
            
            assertEquals("(1)", t.toString());

            assertEquals(6, t.getPosition());
            assertEquals(1, t.get6());
            
            try {
                t.get1();
                fail();
            } catch (final Throwable th) {
                assertEquals(
                    String.format(Tuple.UNION_ERROR_FMT, 6),
                    th.getMessage()
                );
            }

            assertNull(t.t);
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);

            final UnionSix<Integer, Integer, Integer, Integer, Integer, Integer> tt = union.sixSecondNullable(null);
            assertTrue(UnionSix.class == tt.getClass());
            assertEquals(0, tt.hashCode());
            assertEquals("(null)", tt.toString());
        }
    }
    
    @Test
    public void unionSixOf() {
        final Tuple.Union union = new Tuple.Union();
        final Tuple.UnionSame unionSame = new Tuple.UnionSame();
        
        {
            final UnionSixOf<Integer> t = unionSame.sixFirst(1);
            
            assertEquals(1, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, union.twoFirst(1));
            assertNotEquals(t, union.twoSecond(1));
            
            assertEquals("(1)", t.toString());

            assertEquals(1, t.getPosition());
            assertEquals(1, t.get1());
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);
            assertNull(t.y);

            final UnionSixOf<Integer> tt = unionSame.sixFirstNullable(null);
            assertEquals("(null)", tt.toString());
        }
        
        {
            final UnionSixOf<Integer> t = unionSame.sixSixth(2);
            
            assertEquals(2, t.hashCode());
            
            assertEquals(t, t);
            assertEquals(t, Tuple.ofNullable(null, null, null, null, null, 2));
            assertNotEquals(t, Tuple.of(1, 2));
            
            assertEquals("(2)", t.toString());

            assertEquals(6, t.getPosition());
            assertEquals(2, t.get6());
            assertNull(t.t);
            assertNull(t.u);
            assertNull(t.v);
            assertNull(t.w);
            assertNull(t.x);

            final UnionSixOf<Integer> tt = unionSame.sixSixthNullable(null);
            assertEquals(0, tt.hashCode());
            assertEquals("(null)", tt.toString());
        }
    }
}
