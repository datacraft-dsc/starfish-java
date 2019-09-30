package sg.dex.starfish.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestHex {

    @Test
    public void testHexVal() {
        for (int i = 0; i <= 15; i++) {
            assertEquals(i, Hex.val("0123456789abcdef".charAt(i)));
        }

        for (int i = 0; i <= 15; i++) {
            assertEquals(i, Hex.val("0123456789ABCDEF".charAt(i)));
        }
    }

    @Test
    public void testHexChars() {
        for (int i = 0; i <= 15; i++) {
            assertEquals("0123456789abcdef".charAt(i), Hex.toChar(i));
        }
    }

    @Test
    public void testHexExceptionToChar() {
        try {
            char c = Hex.toChar(16);
            fail("Hex.toChar should throw an IllegalArgumentException on: " + c);
        } catch (IllegalArgumentException e) { // expected
            assertEquals(e.getMessage(), "Invalid value for hex char: 16");
        }
    }

    @SuppressWarnings("unused")
    @Test
    public void testHexExceptionToBytes() {
        try {
            byte[] result = Hex.toBytes("A");
            fail("Hex.toBytes should throw an Error on 'A'");
        } catch (Error e) { // expected
            assertEquals(e.getMessage(), "Hex string must have even length: 1");
        }
    }

    @SuppressWarnings("unused")
    @Test
    public void testHexExceptionVal() {
        try {
            int val = Hex.val('?');
            fail("Hex.val should throw an Error on '?'");
        } catch (Error e) { // expected
            assertEquals(e.getMessage(), "Invalid hex char [?] = 63");
        }
    }
}
