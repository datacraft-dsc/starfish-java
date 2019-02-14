package sg.dex.starfish;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import sg.dex.starfish.impl.MemoryAgent;

public class TestMemoryAgent {
	@Test 
	public void testAssetStorage() {
		DID did=DID.parse(Utils.createRandomDIDString());
		MemoryAgent ma=MemoryAgent.create(did);
		assertEquals(did,ma.getDID());
	}
}
