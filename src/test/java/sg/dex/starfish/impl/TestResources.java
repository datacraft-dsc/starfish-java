package sg.dex.starfish.impl;

import org.junit.Test;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.url.ResourceAsset;

import java.nio.charset.StandardCharsets;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SuppressWarnings("javadoc")
public class TestResources {

	@Test public void testResourceAsset() {
		DataAsset dataAsset=ResourceAsset.create("assets/hello.txt");
		byte[] bs=dataAsset.getContent();
		String s=new String(bs,StandardCharsets.UTF_8);
		assertEquals("Hello Starfish",s);

		assertNotNull(dataAsset.getMetadataString());
		assertNotNull(((ResourceAsset) dataAsset).getContentSize());
		assertNull(dataAsset.getMetadata().get(Constant.CONTENT_HASH));
		dataAsset =dataAsset.includeContentHash();
		assertNotNull(dataAsset.getMetadata().get(Constant.CONTENT_HASH));
		assertEquals(dataAsset.validateContentHash(),true);

	}

	@Test public void testResourceAssetWithContentHash() {
		DataAsset dataAsset=ResourceAsset.create("assets/hello.txt");
		byte[] bs=dataAsset.getContent();
		String s=new String(bs,StandardCharsets.UTF_8);
		assertEquals("Hello Starfish",s);
//		assertNotNull(ua.getMetadata().get(Constant.CONTENT_HASH));

		assertNotNull(dataAsset.getMetadataString());
		assertNotNull(((ResourceAsset) dataAsset).getContentSize());

		assertNull(dataAsset.getMetadata().get(Constant.CONTENT_HASH));
		dataAsset =dataAsset.includeContentHash();
		assertNotNull(dataAsset.getMetadata().get(Constant.CONTENT_HASH));
		assertEquals(dataAsset.validateContentHash(),true);
	}

	@Test public void testValidateHash() {
		DataAsset dataAsset=ResourceAsset.create("assets/hello.txt");
		byte[] bs=dataAsset.getContent();
		String s=new String(bs,StandardCharsets.UTF_8);
		assertEquals("Hello Starfish",s);

		assertNull(dataAsset.getMetadata().get(Constant.CONTENT_HASH));
		dataAsset =dataAsset.includeContentHash();
		assertNotNull(dataAsset.getMetadata().get(Constant.CONTENT_HASH));

		assertNotNull(dataAsset.getMetadataString());
		assertEquals(dataAsset.validateContentHash(),true);
	}
}
