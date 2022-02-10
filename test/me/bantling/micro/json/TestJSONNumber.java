package me.bantling.micro.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

@SuppressWarnings({ "static-method", "boxing", "unused" })
public class TestJSONNumber {
    @Test
    public void number() {
        {
            final Object[][] goodCases = {
                { new JSONNumber("12",   true, "12", null, true, null), "12",   true,  "12", "",   true, ""},
                { JSONNumber.of(12),                                    "12",   true,  "12", "",   true, ""}, 
                { JSONNumber.of(Integer.valueOf(12)),                   "12",   true,  "12", "",   true, ""},
                { JSONNumber.of(12L),                                   "12",   true,  "12", "",   true, ""},
                { JSONNumber.of(Long.valueOf(12L)),                     "12",   true,  "12", "",   true, ""},
                { JSONNumber.of(12.0f),                                 "12.0", true,  "12", "0",  true, ""},
                { JSONNumber.of(Float.valueOf(12.0f)),                  "12.0", true,  "12", "0",  true, ""},
                { JSONNumber.of(12.0),                                  "12.0", true,  "12", "0",  true, ""},
                { JSONNumber.of(Double.valueOf(12.0)),                  "12.0", true,  "12", "0",  true, ""},
                { JSONNumber.of(new BigInteger("12")),                  "12",   true,  "12", "",   true, ""},
                { JSONNumber.of(new BigInteger("-12")),                 "-12",  false, "12", "",   true, ""},
                { JSONNumber.of(new BigDecimal("12")),                  "12",   true,  "12", "",   true, ""},
                { JSONNumber.of(new BigDecimal("-12")),                 "-12",  false, "12", "",   true, ""},
                { JSONNumber.of(new BigDecimal("12e+10")),              "1.2E+11",  true, "1", "2",   true, "11"},
                { JSONNumber.of(new BigDecimal("12e-10")),              "1.2E-9",  true, "1", "2",   false, "9"},
            };
            
            for (Object[] goodCase : goodCases) {
                int index                      = 0;
                final JSONNumber num           = (JSONNumber)(goodCase[index++]);
                final String str               = (String)(goodCase[index++]);
                final boolean positive         = ((Boolean)(goodCase[index++])).booleanValue();
                final String integer           = (String)(goodCase[index++]);
                final String fractional        = (String)(goodCase[index++]);
                final boolean positiveExponent = ((Boolean)(goodCase[index++])).booleanValue();
                final String exponent          = (String)(goodCase[index++]);
                
                assertEquals(str,              num.toString());
                assertTrue(positive ==         num.isPositive());
                assertEquals(integer,          num.getInteger());
                assertEquals(fractional,       num.getFractional());
                assertTrue(positiveExponent == num.isPositiveExponent());
                assertEquals(exponent,         num.getExponent());
                
                assertEquals(
                    (31 * (31 * (31 * (31 * 
                      Boolean.hashCode(positive))
                    + integer.hashCode())
                    + fractional.hashCode())
                    + Boolean.hashCode(positiveExponent))
                    + exponent.hashCode(),
                    num.hashCode()
                );
    
                assertTrue(num.equals(new JSONNumber(str, positive, integer, fractional, positiveExponent, exponent)));
                assertTrue(num.equals(new JSONNumber("22", positive, integer, fractional, positiveExponent, exponent)));
                assertFalse(num.equals(new JSONNumber(str, !positive, integer, fractional, positiveExponent, exponent)));
                assertFalse(num.equals(new JSONNumber(str, positive, "22", fractional, positiveExponent, exponent)));
                assertFalse(num.equals(new JSONNumber(str, positive, integer, "22", positiveExponent, exponent)));
                assertFalse(num.equals(new JSONNumber(str, positive, integer, fractional, !positiveExponent, exponent)));
                assertFalse(num.equals(new JSONNumber(str, positive, integer, fractional, positiveExponent, "22")));
            }
        }
        
        {
            final JSONNumber num = JSONNumber.of(5);
            
            assertTrue(num.equals(num));
            assertFalse(num.equals(new Object()));
            
            assertEquals(5, num.asInt());
            assertEquals(0, num.fractionalAsInt());
            assertEquals(0, num.exponentAsInt());
            
            assertEquals(5L, num.asLong());
            assertEquals(0L, num.fractionalAsLong());
            assertEquals(0L, num.exponentAsLong());
            
            assertEquals(new BigInteger("5"), num.asBigInteger());
            assertEquals(BigInteger.ZERO, num.fractionalAsBigInteger());
            assertEquals(BigInteger.ZERO, num.exponentAsBigInteger());
            
            assertEquals(5.0f, num.asFloat(), 0.0f);
            assertEquals(5.0, num.asDouble(), 0.0);
            assertEquals(new BigDecimal("5"),num.asBigDecimal());
        }
        
        {
            final JSONNumber num = JSONNumber.of(5.25e30);
            
            assertEquals(5, num.asInt());
            assertEquals(25, num.fractionalAsInt());
            assertEquals(30, num.exponentAsInt());
            
            assertEquals(5L, num.asLong());
            assertEquals(25L, num.fractionalAsLong());
            assertEquals(30L, num.exponentAsLong());
            
            assertEquals(new BigInteger("5"), num.asBigInteger());
            assertEquals(new BigInteger("25"), num.fractionalAsBigInteger());
            assertEquals(new BigInteger("30"), num.exponentAsBigInteger());
            
            assertEquals(5.25e30f, num.asFloat(), 0.0f);
            assertEquals(5.25e30, num.asDouble(), 0.0);
            assertEquals(new BigDecimal("5.25e30"),num.asBigDecimal());
        }
        
        try {
            // Force impossible situations to complete coverage
            JSONNumber.ofIntLongFloatDoubleBigDecimal("x");
            fail("Must die");
        } catch (final Throwable t) {
            assertTrue(t instanceof IllegalArgumentException);
            assertEquals("x is not a correctly formatted JSON number", t.getMessage());
        }
    }
}
