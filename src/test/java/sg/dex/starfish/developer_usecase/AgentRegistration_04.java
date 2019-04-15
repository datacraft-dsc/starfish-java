package sg.dex.starfish.developer_usecase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * As a developer building or managing an Ocean Agent,
 * I need to be able to register my Agent on the network and obtain an Agent ID
 */
@RunWith(JUnit4.class)
public class AgentRegistration_04 {
    @Test
    public void testRegistration() {
        Map<String, Object> ddo = new HashMap<>();
        List<Map<String, Object>> services = new ArrayList<>();
        services.add(Utils.mapOf(
                "type", "Ocean.Meta.v1",
                "serviceEndpoint", "/api/v1/meta"));
        services.add(Utils.mapOf(
                "type", "Ocean.Storage.v1",
                "serviceEndpoint", "/api/v1/assets"));
        services.add(Utils.mapOf(
                "type", "Ocean.Invoke.v1",
                "serviceEndpoint", "/api/v1/invoke"));
        services.add(Utils.mapOf(
                "type", "Ocean.Auth.v1",
                "serviceEndpoint", "/api/v1/auth"));
         services.add(Utils.mapOf(
                "type", "Ocean.Market.v1",
                "serviceEndpoint", "/api/v1/market"));
        ddo.put("service", services);
        String ddoString = JSON.toPrettyString(ddo);

        //getting the default Ocean instance
        Ocean ocean = Ocean.connect();
        // creating unique DID
        DID surferDID = DID.createRandom();
        //registering the  DID and DDO
        ocean.registerLocalDID(surferDID, ddoString);

        // creating a Remote agent instance for given Ocean and DID
        RemoteAgent remoteAgent = RemoteAgent.create(ocean, surferDID);
        assertNotNull(remoteAgent);
        assertEquals(remoteAgent.getDID(), surferDID);
    }
}
