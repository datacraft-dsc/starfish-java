package sg.dex.starfish.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAgent;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Hex;

public class TestMemoryAgent {
	private static final byte[] BYTE_DATA = Hex.toBytes("0123456789");

	@Test 
	public void testAgentID() {
		DID did=DID.parse(DID.createRandomString());
		MemoryAgent ma=MemoryAgent.create(did);
		assertEquals(did,ma.getDID());
	}
	
	@Test public void testTransfer() {
		MemoryAgent agent1=MemoryAgent.create();
		MemoryAgent agent2=MemoryAgent.create();
		
		MemoryAsset a=MemoryAsset.create(BYTE_DATA);
		String id=a.getAssetID();
		assertNull(agent1.getAsset(id));
		
		Asset a1=agent1.uploadAsset(a);
		assertEquals(a1,agent1.getAsset(id));
		
		assertNull(agent2.getAsset(id));
		Asset a2=agent2.uploadAsset(a1);
		assertNotNull(agent2.getAsset(id));
		
		assertEquals(a1.getMetadataString(),a2.getMetadataString());
		
		assertTrue(Arrays.equals(BYTE_DATA, a2.getBytes()));
	}
}
