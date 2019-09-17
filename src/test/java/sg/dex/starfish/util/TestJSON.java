package sg.dex.starfish.util;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestJSON {

    @Test
    public void testLongInMap() {
        Map<String, Object> json = JSON.toMap("{\"foo\": 1}");
        assertEquals(Long.valueOf(1), json.get("foo"));
    }

    @Test
    public void testString() {
        Object json = JSON.parse("\"foo\"");
        assertEquals("foo", json);
    }

    @Test
    public void testLong() {
        Object json = JSON.parse("1");
        assertEquals(Long.valueOf(1), json);
    }

    @Test
    public void testBoolean() {
        assertEquals(Boolean.valueOf(true), JSON.parse("true"));
        assertEquals(Boolean.valueOf(false), JSON.parse("false"));
    }

    @Test
    public void testSlashes() {
        String s = "\"\\/foo\\/bar\"";
        assertEquals("/foo/bar", JSON.parse(s));
        assertEquals(s, JSON.toString("/foo/bar"));
    }

    @Test
    public void testarrayType() {
        List<Long> list = JSON.parse("[1 2]");
        assertEquals(2, list.size());
        assertEquals(Long.valueOf(1), list.get(0));
        assertEquals(Long.valueOf(2), list.get(1));
    }

    @Test
    public void testNull() {
        assertNull(JSON.parse("null"));
    }

    @Test
    public void testRoundTrip() {
        Map<String, Object> json = JSON.toMap("{\"foo\": 1}");
        assertEquals(Long.valueOf(1), json.get("foo"));
    }
}
