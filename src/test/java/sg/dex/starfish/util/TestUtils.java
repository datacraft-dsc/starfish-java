package sg.dex.starfish.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestUtils {

	@Test public void testCoerceInt() {
		assertEquals(1,Utils.coerceInt("1"));
		assertEquals(1,Utils.coerceInt(1L));
	}
}
