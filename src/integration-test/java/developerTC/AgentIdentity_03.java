package developerTC;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.DID;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * As a developer working with Ocean,
 * I need a stable identifier (Agent ID) for an arbitrary Agent in the Ocean ecosystem
 * This test class with validate the DID and DDO format and and their data
 */
public class AgentIdentity_03 {

    private RemoteAgent remoteAgent;

    @Before
    public void setup() {

        remoteAgent = AgentService.getRemoteAgent();
        Assume.assumeNotNull(remoteAgent);
    }

    @Test
    public void testDidFormat() {
        // getting the did of the agent
        DID did = remoteAgent.getDID();
        assertEquals(did.getMethod(), "op");
        assertEquals(did.getScheme(), "did");

    }

    @Test
    public void testDDOFormat() {
        // getting the DDo of the Agent
        Map<String, Object> ddo = remoteAgent.getDDO();

        List<Map<String, Object>> services = (List<Map<String, Object>>) ddo.get("service");


        Assume.assumeNotNull(services);
        assertTrue(remoteAgent.getMetaEndpoint().contains("/api/v1/meta"));
        assertTrue(remoteAgent.getMarketEndpoint().contains("/api/v1/market"));

    }

    @After
    public void clear() {
        remoteAgent = null;
    }
}
