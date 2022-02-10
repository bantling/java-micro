package me.bantling.micro.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

@SuppressWarnings({ "static-method", "unused" })
public class TestUnicode {
    public static final String HIGH_SURROGATE_EOF = Unicode.HIGH_SURROGATE_EOF;
    
    @Test
    public void cons() {
        try {
            new Unicode();
            fail("Must die");
        } catch (final RuntimeException e) {
            //
        }
    }
    
    @Test
    public void nextCodePoint() throws Throwable {
        {
            final Reader testCase = new StringReader("Aß東𐐀");
            final int[] codePoints = {
                0x000041, // A
                0x0000df, // ß
                0x006771, // 東
                0x010400, // 𐐀
                -1,
                -1
            };
            
            for (final int expectedCodePoint : codePoints) {
                final int actualCodePoint = Unicode.nextCodePoint(testCase, null);
                assertEquals(expectedCodePoint, actualCodePoint);
            
            }
        }
        
        {
            final Reader testCase = new StringReader("\ud801"); // high surrogate followed by EOF

            try {
                Unicode.nextCodePoint(testCase, () -> "0:1");
                fail("Must die");
            } catch (final Throwable t) {
                assertTrue(t instanceof RuntimeException);
                assertTrue(t.getCause() instanceof IOException);
                assertEquals(String.format(Unicode.HIGH_SURROGATE_EOF, "0:1"), t.getCause().getMessage());
            }
        }
        
        {
            final Reader testCase = new StringReader("\ud801A"); // high surrogate followed by non-low surrogate

            try {
                Unicode.nextCodePoint(testCase, Unicode.NO_POSITION);
                fail("Must die");
            } catch (final Throwable t) {
                assertTrue(t instanceof RuntimeException);
                assertTrue(t.getCause() instanceof IOException);
                assertEquals(String.format(Unicode.HIGH_SURROGATE_LOW_SURROGATE, ""), t.getCause().getMessage());
            }
        }
    }
    
    @Test
    public void unreadCodePoint() {
        final PushbackReader testCase = new PushbackReader(new StringReader("𐐀東ßA"), 2);
        final int[] codePoints = {
            0x010400, // 𐐀
            0x006771, // 東
            0x0000df, // ß
            0x000041, // A
        };
        
        for (final int expectedCodePoint : codePoints) {
            int actualCodePoint;
            
            // Read and compare
            actualCodePoint = Unicode.nextCodePoint(testCase, null);
            assertEquals(expectedCodePoint, actualCodePoint);

            // Unread, reread, and recommpare
            Unicode.unreadCodePoint(testCase, actualCodePoint);
            actualCodePoint = Unicode.nextCodePoint(testCase, null);
            assertEquals(expectedCodePoint, actualCodePoint);
        }
        
        // Have EOF
        assertEquals(-1, Unicode.nextCodePoint(testCase, null));
        
        // Can unread single char code point after EOF, reread code point, then reread EOF
        Unicode.unreadCodePoint(testCase, 0x000041);
        assertEquals(0x000041, Unicode.nextCodePoint(testCase, null));
        assertEquals(-1, Unicode.nextCodePoint(testCase, null));

        // Can unread two char code point after EOF, reread code point, then reread EOF
        Unicode.unreadCodePoint(testCase, 0x010400);
        assertEquals(0x010400, Unicode.nextCodePoint(testCase, null));
        assertEquals(-1, Unicode.nextCodePoint(testCase, null));
    }
    
    @Test
    public void formatAsUnicodeEscapes() {
        /* 
            0x000041, A
            0x0000df, ß
            0x006771, 東
            0x010400, 𐐀
         */
        assertEquals("\\u0041",        Unicode.formatAsUnicodeEscapes(0x000041));
        assertEquals("\\u00df",        Unicode.formatAsUnicodeEscapes(0x0000df));
        assertEquals("\\u6771",        Unicode.formatAsUnicodeEscapes(0x006771));
        assertEquals("\\ud801\\udc00", Unicode.formatAsUnicodeEscapes(0x010400));
    }
}
