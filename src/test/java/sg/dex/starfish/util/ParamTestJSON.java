package sg.dex.starfish.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ParamTestJSON {
	private String data;

	public ParamTestJSON(String label,String data) {
		this.data = data;
	}

	@Parameterized.Parameters(name = "{index}: {0}")
	public static Collection<String[]> dataExamples() {
		return Arrays.asList(new String[][] { 
			{ "Number","1" }, 
			{ "Number with decimal","1.0" }, 
			{ "Null","null" }, 
			{ "String","\"foobar\"" }, 
			{ "Array of numbers","[1,2,3]" }, 
			{ "Array of stuff","[1,{},true,\"bar\"]" }, 
			{ "Empty map","{}" }, 
			{ "Nested maps", "{\"foo\": {}}" } 
		});
	}
	
	@Test public void testJSONRoundTrip() {
		Object o1=JSON.parse(data);
		String s1=JSON.toString(o1);
		Object o2=JSON.parse(s1);
		String s2=JSON.toString(o2);
		assertEquals(s1,s2);
		assertEquals(o1,o2);
	}
	
	@Test public void testJSONRoundTripPretty() {
		Object o1=JSON.parse(data);
		String ps1=JSON.toPrettyString(o1);
		Object o2=JSON.parse(ps1);
		String ps2=JSON.toPrettyString(o2);
		assertEquals(ps1,ps2);
		assertEquals(o1,o2);
	}
}
