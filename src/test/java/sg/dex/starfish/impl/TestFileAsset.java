package sg.dex.starfish.impl;

import org.junit.Test;
import sg.dex.starfish.impl.file.FileAsset;
import sg.dex.starfish.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestFileAsset {

	@Test public void testTempFile() {
		String name=Utils.createRandomHexString(16);
		
		File f;
		try {
			f = File.createTempFile(name, ".tmp");
		}
		catch (IOException e) {
			throw new Error(e);
		} 
		f.deleteOnExit();
		
		FileAsset fa=FileAsset.create(f);
		Map<String,Object> md=fa.getMetadata();
		assertEquals(0,fa.getContentSize()); // should be empty file
		assertEquals(f.getName(),md.get("fileName"));
		assertEquals(0,Utils.coerceInt(md.get("size")));

		
	}
}
