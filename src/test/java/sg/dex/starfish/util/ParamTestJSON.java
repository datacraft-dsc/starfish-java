package sg.dex.starfish.util;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;


@Disabled
public class ParamTestJSON {
    private String data;

    public ParamTestJSON(String data) {
        this.data = data;
    }


    public static Collection<String[]> dataExamples() {
        return Arrays.asList(new String[][]{
                {"Number", "1"},
                {"Number with decimal", "1.0"},
                {"Null", "null"},
                {"String", "\"foobar\""},
                {"String with special characters", "\"foo\\nbaz\\tbar\""},
                {"String with unicode characters", "\"\u0026\uFFFF\""},
                {"String with non-escaped forward slashes", "\"foo/bar/baz\""},
                {"String with escaped forward slashes", "\"foo\\/bar\\/baz\""},
                {"Array of numbers", "[1,2,3]"},
                {"Array of stuff", "[1,{},true,\"bar\"]"},
                {"Empty map", "{}"},
                {"Nested maps", "{\"foo/bar\": {}}"}
        });
    }

    @Test
    public void testJSONRoundTrip() {
        Object o1 = JSON.parse(data);
        String s1 = JSON.toString(o1);
        Object o2 = JSON.parse(s1);
        String s2 = JSON.toString(o2);
        assertTrue(s1.equals(s2));
        assertTrue(o1.equals(o2));
    }

    @Test
    public void testJSONRoundTripPretty() {
        Object o1 = JSON.parse(data);
        String ps1 = JSON.toPrettyString(o1);
        Object o2 = JSON.parse(ps1);
        String ps2 = JSON.toPrettyString(o2);
        assertTrue(ps1.equals(ps2));
        assertTrue(o1.equals(o2));
    }
}
