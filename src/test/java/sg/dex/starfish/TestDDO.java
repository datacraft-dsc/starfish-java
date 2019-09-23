package sg.dex.starfish;

import org.junit.Test;

import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.DID;

import java.util.Map;

import static org.junit.Assert.*;

public class TestDDO {

    @Test
    public void testLocalDDO() {
        Ocean ocean = Ocean.connect();
        String endpoint="http://localhost:8080/api/v1/meta";
        String ddoString = "{\"service\" [{\"type\" : \"Ocean.Meta.v1\" , \"serviceEndpoint\": \""+endpoint+"\"}]}";
        DID did = DID.createRandom();
        ocean.installLocalDDO(did, ddoString);

        String ddos = ocean.getDDOString(did);
        assertEquals(ddoString, ddos);

        Map<String, Object> ddo = ocean.getDDO(did);
        assertEquals(1, ddo.size());
        
        RemoteAgent agent=ocean.getAgent(did);
        assertEquals(endpoint,agent.getMetaEndpoint());
        assertEquals(endpoint,agent.getEndpoint(Constant.ENDPOINT_META));
    }

    @Test
    public void testMissingDDO() {
        Ocean ocean = Ocean.connect();
        String ddo = ocean.getDDOString("did:op:missing");
        assertNull(ddo);
    }

    @Test
    public void testDIDwithoutPath() {
        DID did1 = DID.createRandom();
        DID did2 = did1.withPath("foo");
        assertNotEquals(did1, did2);

        DID did3 = did2.withoutPath();
        assertEquals(did1, did3);
    }
}
