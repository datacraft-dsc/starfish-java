package sg.dex.starfish;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Test;

import sg.dex.starfish.util.DID;

public class TestDDO {

	@Test public void testLocalDDO() {
		Ocean ocean=Ocean.connect();
		String ddoString="{}";
		DID did=DID.createRandom();
		ocean.installLocalDDO(did, ddoString);
		
		Map<String,Object> ddo=ocean.getDDO(did);
		assertEquals(0,ddo.size());
		
		String ddos=ocean.getDDOString(did);
		assertEquals("{}",ddos);
	}
	
	@Test public void testMissingDDO() {
		Ocean ocean=Ocean.connect();
		String ddo=ocean.getDDOString("did:op:missing");
		assertNull(ddo);
	}
	
	@Test public void testDIDwithoutPath() {
		DID did1=DID.createRandom();
		DID did2=did1.withPath("foo");
		assertNotEquals(did1,did2);
		
		DID did3=did2.withoutPath();
		assertEquals(did1,did3);
	}
}
