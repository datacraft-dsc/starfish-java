package sg.dex.starfish.util;

import static org.junit.Assert.*;

import org.junit.*;

import sg.dex.starfish.util.Hex;

public class TestHex {

	@Test public void testHexVal() {
		for (int i=0; i<=15; i++) {
			assertEquals(i,Hex.val("0123456789abcdef".charAt(i)));
		}
		
		for (int i=0; i<=15; i++) {
			assertEquals(i,Hex.val("0123456789ABCDEF".charAt(i)));
		}
	}
	
	@Test public void testHexChars() {
		for (int i=0; i<=15; i++) {
			assertEquals("0123456789abcdef".charAt(i),Hex.toChar(i));
		}
	}
}
