package sg.dex.starfish.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;

import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.util.DID;

public class TestMemoryAsset {
	@Test public void testCreation() {
		byte[] data=new byte[10];
		
		MemoryAsset a=MemoryAsset.create(data);
		assertEquals(10,a.getSize());
		Map<String,Object> meta=a.getMetadata();
		assertEquals("10",meta.get("size"));
		assertEquals(Hash.keccak256String(data),meta.get("contentHash"));
	}
	
	@Test public void testDID() {
		byte[] data=new byte[] {1,2,3};
		Asset a=MemoryAsset.create(data);
		try {
			DID did=a.getAssetDID(); // in-memory assets don't have a DID, so this should fail
			fail("Should not succeed! Got: "+did);
		} catch (UnsupportedOperationException ex) {
			/* OK */
		}
	}
}
