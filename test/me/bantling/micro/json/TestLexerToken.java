package me.bantling.micro.json;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

@SuppressWarnings("static-method")
public class TestLexerToken {
    @Test
    public void number() {
        LexerToken num = new LexerToken("1", true, "1", "", true, "");
        assertEquals(LexerToken.Type.NUMBER, num.type);
        assertEquals("1", num.token);
        assertTrue(num.positive);
        assertEquals("1", num.integer);
        assertEquals("", num.fractional);
        assertTrue(num.positiveExponent);
        assertEquals("", num.exponent);
        
        assertEquals((31 * LexerToken.Type.NUMBER.hashCode()) + "1".hashCode(), num.hashCode());
        assertEquals(num, num);
        assertEquals(num, new LexerToken("1", true, "1", "", true, ""));
        assertNotEquals(num, "1");
        assertNotEquals(num, new LexerToken(LexerToken.Type.STRING, "a"));
        assertNotEquals(num, new LexerToken("10", true, "1", "", true, ""));
        assertNotEquals(num, new LexerToken("1", false, "1", "", true, ""));
        assertNotEquals(num, new LexerToken("1", true, "10", "", true, ""));
        assertNotEquals(num, new LexerToken("1", true, "1", "2", true, ""));
        assertNotEquals(num, new LexerToken("1", true, "1", "", false, ""));
        assertNotEquals(num, new LexerToken("1", true, "1", "", true, "2"));
        
        assertEquals("LexerToken[type=NUMBER,token=1,positive=true,integer=1,fractional=,positiveExponent=true,exponent=]", num.toString());
    }
}
