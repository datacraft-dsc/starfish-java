package sg.dex.starfish.impl;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import sg.dex.crypto.Hash;
import sg.dex.starfish.impl.memory.MemoryAsset;

public class TestMemoryAsset {
	@Test public void testCreation() {
		byte[] data=new byte[10];
		
		MemoryAsset a=MemoryAsset.create(data);
		assertEquals(10,a.getSize());
		Map<String,Object> meta=a.getMetadata();
		assertEquals("10",meta.get("size"));
		assertEquals(Hash.keccak256String(data),meta.get("contentHash"));
	}
}
