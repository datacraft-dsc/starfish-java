package sg.dex.starfish.impl;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.url.ResourceAsset;

public class TestResources {

	@Test public void testResourceAsset() {
		Asset ua=ResourceAsset.create("{}","assets/hello.txt");
		byte[] bs=ua.getBytes();
		String s=new String(bs,StandardCharsets.UTF_8);
		assertEquals("Hello Starfish",s);
		
		assertEquals("{}",ua.getMetadataString());
	}
}
