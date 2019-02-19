package sg.dex.starfish.impl;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import sg.dex.starfish.impl.file.FileAsset;
import sg.dex.starfish.util.Utils;

public class TestFiles {

	@Test public void testTempFile() {
		String name=Utils.createRandomHexString(16);
		
		File f;
		try {
			f = File.createTempFile(name, "tmp");
		}
		catch (IOException e) {
			throw new Error(e);
		} 
		f.deleteOnExit();
		
		FileAsset fa=FileAsset.create(f);
		assertEquals(0,fa.getBytes().length); // should be empty file

		
	}
}
