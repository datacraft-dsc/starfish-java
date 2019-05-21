package sg.dex.starfish.impl;

import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.url.ResourceAsset;

import java.nio.charset.StandardCharsets;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class TestResources {

	@Test public void testResourceAsset() {
		Asset ua=ResourceAsset.create("{}","assets/hello.txt");
		byte[] bs=ua.getContent();
		String s=new String(bs,StandardCharsets.UTF_8);
		assertEquals("Hello Starfish",s);

		assertNotNull(ua.getMetadataString());
		assertNotNull(((ResourceAsset) ua).getContentSize());
	}
}
