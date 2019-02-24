package sg.dex.starfish.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Test;

public class TestJSON {

	@Test public void testLongInMap() {
		Map<String,Object> json=JSON.toMap("{\"foo\": 1}");
		assertEquals(Long.valueOf(1),json.get("foo"));
	}
	
	@Test public void testString() {
		Object json=JSON.parse("\"foo\"");
		assertEquals("foo",json);
	}

	@Test public void testLong() {
		Object json=JSON.parse("1");
		assertEquals(Long.valueOf(1),json);
	}
	
	@Test public void testBoolean() {
		assertEquals(Boolean.valueOf(true),JSON.parse("true"));
		assertEquals(Boolean.valueOf(false),JSON.parse("false"));
	}
	
	@Test public void testSlashes() {
		String s="\"\\/foo\\/bar\"";
		assertEquals("/foo/bar",JSON.parse(s));
		assertEquals (s,JSON.toString("/foo/bar"));
	}
	
	@Test public void testNull() {
		assertNull(JSON.parse("null"));
	}
	
	@Test public void testRoundTrip() {
		Map<String,Object> json=JSON.toMap("{\"foo\": 1}");
		assertEquals(Long.valueOf(1),json.get("foo"));
	}
}
