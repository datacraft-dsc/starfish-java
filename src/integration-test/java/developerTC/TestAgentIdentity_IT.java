package developerTC;

import org.junit.jupiter.api.*;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertThat;
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

        List<Map<String, Object>> actualServiceList = new ArrayList<>();
        String host = AgentService.getSurferUrl();

        actualServiceList.add(Utils.mapOf(
                "type", "DEP.Meta.v1",
                "serviceEndpoint", host + "/api/v1/meta"));
        actualServiceList.add(Utils.mapOf(
                "type", "DEP.Storage.v1",
                "serviceEndpoint", host + "/api/v1/assets"));
        actualServiceList.add(Utils.mapOf(
                "type", "DEP.Invoke.v1",
                "serviceEndpoint", host + "/api/v1/invoke"));
        actualServiceList.add(Utils.mapOf(
                "type", "DEP.Auth.v1",
                "serviceEndpoint", host + "/api/v1/auth"));
        actualServiceList.add(Utils.mapOf(
                "type", "DEP.Market.v1",
                "serviceEndpoint", host + "/api/v1/market"));
        actualServiceList.add(Utils.mapOf(
                "type", "DEP.Status",
                "serviceEndpoint", host + "/api/status"));
        actualServiceList.add(Utils.mapOf(
                "type", "DEP.DDO",
                "serviceEndpoint", host + "/api/ddo"));


        // getting the DDo of the Agent
        Map<String, Object> ddo = remoteAgent.getDDO();

        List<Map<String, Object>> expectedServiceList = (List<Map<String, Object>>) ddo.get("service");

        Assertions.assertEquals(actualServiceList, expectedServiceList);


    }
}
