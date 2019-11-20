package developerTC;

import org.junit.jupiter.api.Test;
import sg.dex.starfish.Resolver;
import sg.dex.starfish.impl.memory.LocalResolverImpl;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * As a developer managing a Ocean Agent,
 * I need to be able to update service endpoints for my Agent
 */
public class AgentEndpointUpdate_05 {


    @Test
    public void testServiceEndPoint() {
        // creating an instance of Remote Agent
        String surferURL = AgentService.getSurferUrl();
        RemoteAgent remoteAgent = createRemoteAgent(surferURL);

        assertTrue(remoteAgent.getStorageEndpoint().contains("/api/v1/assets"));
        assertTrue(remoteAgent.getMetaEndpoint().contains("/api/v1/meta"));

        // below endpoint wll be null and it was not initialize while creating services for Remote Agent
        assertNull(remoteAgent.getInvokeEndpoint());
        assertNull(remoteAgent.getAuthEndpoint());
    }

    private RemoteAgent createRemoteAgent(String host) {

        Map<String, Object> ddo = new HashMap<>();
        List<Map<String, Object>> services = new ArrayList<>();
        // add the respective end points
        services.add(Utils.mapOf(
                "type", "Ocean.Meta.v1",
                "serviceEndpoint", host + "/api/v1/meta"));
        services.add(Utils.mapOf(
                "type", "Ocean.Storage.v1",
                "serviceEndpoint", host + "/api/v1/assets"));

        // adding to ddo map
        ddo.put("service", services);
        // converting ddo to string
        String ddoString = JSON.toPrettyString(ddo);

        Resolver resolver = new LocalResolverImpl();
        // creating unique DID
        DID surferDID = DID.createRandom();

        // registering the DID and DDO
        resolver.registerDID(surferDID, ddoString);

        // creating a Remote agent instance for given Ocean and DID
        return RemoteAgent.create(resolver, surferDID);

    }
}
