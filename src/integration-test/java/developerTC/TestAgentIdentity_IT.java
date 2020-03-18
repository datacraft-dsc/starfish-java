package developerTC;

import org.junit.jupiter.api.*;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.DID;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * As a developer working with Data Supply line,
 * I need a stable identifier (Agent ID) for an arbitrary Agent in the Data ecosystem
 * This test class with validate the DID and DDO format and and their data
 */
public class TestAgentIdentity_IT {

    private RemoteAgent remoteAgent;

    @BeforeAll
    @DisplayName("Check if RemoteAgent is up!!")
    public static void init() {
        Assumptions.assumeTrue(ConnectionStatus.checkAgentStatus(), "Agent :" + AgentService.getSurferUrl() + "is not running. is down");
    }

    @BeforeEach
    public void setup() {

        remoteAgent = AgentService.getRemoteAgent();
        assumeTrue(null != remoteAgent);
    }

    @Test
    public void testDidFormat() {
        // getting the did of the agent
        DID did = remoteAgent.getDID();
        assertEquals(did.getMethod(), "dep");
        assertEquals(did.getScheme(), "did");

    }

    @Test
    public void testDDOFormat() {
        // getting the DDo of the Agent
        Map<String, Object> ddo = remoteAgent.getDDO();

        List<Map<String, Object>> services = (List<Map<String, Object>>) ddo.get("service");


        assumeTrue(null != services);
        assertTrue(remoteAgent.getMetaEndpoint().contains("/api/v1/meta"));
        assertTrue(remoteAgent.getMarketEndpoint().contains("/api/v1/market"));

    }


}
