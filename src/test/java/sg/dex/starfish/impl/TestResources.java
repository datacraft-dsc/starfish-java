package sg.dex.starfish.impl;

import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.url.ResourceAsset;

import java.nio.charset.StandardCharsets;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("javadoc")
public class TestResources {

	@Test public void testResourceAsset() {
		Asset ua=ResourceAsset.create("assets/hello.txt","{}",false);
		byte[] bs=ua.getContent();
		String s=new String(bs,StandardCharsets.UTF_8);
		assertEquals("Hello Starfish",s);

		assertNotNull(ua.getMetadataString());
		assertNotNull(((ResourceAsset) ua).getContentSize());
	}

	@Test public void testResourceAssetWithContetHash() {
		Asset ua=ResourceAsset.create("assets/hello.txt","{}",true);
		byte[] bs=ua.getContent();
		String s=new String(bs,StandardCharsets.UTF_8);
		assertEquals("Hello Starfish",s);
		assertNotNull(ua.getMetadata().get(Constant.CONTENT_HASH));

		assertNotNull(ua.getMetadataString());
		assertNotNull(((ResourceAsset) ua).getContentSize());
	}
	@Test public void testResourceAssetWithoutContetHash() {
		Asset ua=ResourceAsset.create("assets/hello.txt","{}",true);
		byte[] bs=ua.getContent();
		String s=new String(bs,StandardCharsets.UTF_8);
		assertEquals("Hello Starfish",s);
		assertNotNull(ua.getMetadata().get(Constant.CONTENT_HASH));

		assertNotNull(ua.getMetadataString());
	}
}
