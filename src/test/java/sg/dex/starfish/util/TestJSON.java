package sg.dex.starfish.util;

import static org.junit.Assert.assertEquals;

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
}
