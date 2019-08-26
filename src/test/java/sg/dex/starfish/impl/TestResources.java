package sg.dex.starfish.impl;

import org.junit.Test;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.StarfishValidationException;
import sg.dex.starfish.impl.url.ResourceAsset;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestResources {

	@Test public void testResourceAsset() {
		DataAsset dataAsset=ResourceAsset.create("assets/hello.txt",null);
		byte[] bs=dataAsset.getContent();
		String s=new String(bs,StandardCharsets.UTF_8);
		assertEquals("Hello Starfish",s);

		// testing by default the hash content is not calculated and included in metadata
		assertNull(dataAsset.getMetadata().get(Constant.CONTENT_HASH));
		// including the content hash
		dataAsset =dataAsset.includeContentHash();
		// validating the content hash
		assertEquals(dataAsset.validateContentHash(),true);

	}

	@Test(expected = StarfishValidationException.class)
	public void testInvalidResource() {
		DataAsset dataAsset = ResourceAsset.create("assets/NotValid.txt", null);
	}


		@Test
		public void testValidateHash() {
		DataAsset dataAsset=ResourceAsset.create("assets/hello.txt",null);
		byte[] bs=dataAsset.getContent();
		String s=new String(bs,StandardCharsets.UTF_8);
		assertEquals("Hello Starfish",s);

		assertNull(dataAsset.getMetadata().get(Constant.CONTENT_HASH));
		dataAsset =dataAsset.includeContentHash();

		assertEquals(dataAsset.validateContentHash(),true);
	}
}
