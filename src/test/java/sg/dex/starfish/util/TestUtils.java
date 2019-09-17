package sg.dex.starfish.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestUtils {

	@Test public void testCoerceInt() {
		assertEquals(1,Utils.coerceInt("1"));
		assertEquals(1,Utils.coerceInt(1L));
	}
}
