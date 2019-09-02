package sg.dex.starfish;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import sg.dex.starfish.util.DID;

public class TestDDO {

	@Test public void testLocalDDO() {
		Ocean ocean=Ocean.connect();
		String ddoString="{}";
		DID did=DID.createRandom();
		ocean.registerLocalDID(did, ddoString);
		
		Map<String,Object> ddo=ocean.getDDO(did);
		assertEquals(0,ddo.size());
	}
}
