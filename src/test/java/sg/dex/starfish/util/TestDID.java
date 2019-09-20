package sg.dex.starfish.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestDID {
	
	private void testRoundTrip(String s) {
		assertEquals(s, DID.parse(s).toString());
	}
	
	@Test 
	public void testRoundTrips() {
		testRoundTrip("did:ocn:foo");
		testRoundTrip("did:op:1234/5678#foo");
		testRoundTrip("did:op:1234#foo");
	}
	
    @Test
    public void testParse() {
        DID d1 = DID.parse("did:ocn:1234");
        assertEquals("did", d1.getScheme());
        assertEquals("ocn", d1.getMethod());
        assertEquals("1234", d1.getID());
        assertNull(d1.getPath());
        assertNull(d1.getFragment());
    }

    @Test
    public void testParsePath() {
        DID d1 = DID.parse("did:ocn:1234/foo");
        assertEquals("did", d1.getScheme());
        assertEquals("ocn", d1.getMethod());
        assertEquals("1234", d1.getID());
        assertEquals("foo", d1.getPath());
        assertNull(d1.getFragment());
    }

    @Test
    public void testRandomDID() {
        DID.parse(DID.createRandomString());
    }

    @Test
    public void testFullDID() {
        DID d1 = DID.parse("did:ocn:1234/foo/bar#fragment");
        assertEquals("did", d1.getScheme());
        assertEquals("ocn", d1.getMethod());
        assertEquals("1234", d1.getID());
        assertEquals("foo/bar", d1.getPath());
        assertEquals("fragment", d1.getFragment());
    }

    @Test
    public void testValidDID() {
        assertTrue(DID.isValidDID("did:ocn:1234/foo/bar"));
        assertFalse(DID.isValidDID("nonsense:ocn:1234"));
        assertFalse(DID.isValidDID("did:OCN:1234"));
    }

    @Test
    public void testParseFragment() {
        DID d1 = DID.parse("did:ocn:1234#bar");
        assertEquals("did", d1.getScheme());
        assertEquals("ocn", d1.getMethod());
        assertEquals("1234", d1.getID());
        assertEquals("bar", d1.getFragment());
        assertNull(d1.getPath());
    }
}
