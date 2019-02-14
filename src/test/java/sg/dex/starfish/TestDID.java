package sg.dex.starfish;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TestDID {
	@Test public void testParse() {
		DID d1=DID.parse("did:ocn:1234");
		assertEquals("did",d1.getScheme());
		assertEquals("ocn",d1.getMethod());
		assertEquals("1234",d1.getID());
		assertNull(d1.getPath());
		assertNull(d1.getFragment());
	}
	
	@Test public void testParsePath() {
		DID d1=DID.parse("did:ocn:1234/foo");
		assertEquals("did",d1.getScheme());
		assertEquals("ocn",d1.getMethod());
		assertEquals("1234",d1.getID());
		assertEquals("foo",d1.getPath());
		assertNull(d1.getFragment());
	}
	
	@Test public void testParseFragment() {
		DID d1=DID.parse("did:ocn:1234#bar");
		assertEquals("did",d1.getScheme());
		assertEquals("ocn",d1.getMethod());
		assertEquals("1234",d1.getID());
		assertEquals("bar",d1.getFragment());
		assertNull(d1.getPath());
	}
}
