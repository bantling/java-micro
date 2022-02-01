package me.bantling.micro.util;

import static org.junit.Assert.assertEquals;

import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public class TestUnicode {
    @Test
    public void nextCodePoint() {
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
}
