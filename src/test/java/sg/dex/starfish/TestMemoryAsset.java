package sg.dex.starfish;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestMemoryAsset {
	@Test public void testCreation() {
		byte[] data=new byte[10];
		
		MemoryAsset a=MemoryAsset.create(data);
		assertEquals(10,a.getSize());
	}
}
