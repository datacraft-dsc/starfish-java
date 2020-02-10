package developerTC;

import org.junit.jupiter.api.Test;
import sg.dex.starfish.Resolver;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.squid.DexResolver;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * As a developer building or managing an Ocean Agent,
 * I need to be able to register my Agent on the network and obtain an Agent ID
 */
public class TestAgentRegistration_IT {
    @Test
    public void testRegistration() throws IOException {
        Map<String, Object> ddo = new HashMap<>();
        List<Map<String, Object>> services = new ArrayList<>();
        services.add(Utils.mapOf(
                "type", "DEP.Meta.v1",
                "serviceEndpoint", "/api/v1/meta"));
        services.add(Utils.mapOf(
                "type", "DEP.Storage.v1",
                "serviceEndpoint", "/api/v1/assets"));
        services.add(Utils.mapOf(
                "type", "DEP.Invoke.v1",
                "serviceEndpoint", "/api/v1/invoke"));
        services.add(Utils.mapOf(
                "type", "DEP.Auth.v1",
                "serviceEndpoint", "/api/v1/auth"));
        services.add(Utils.mapOf(
                "type", "DEP.Market.v1",
                "serviceEndpoint", "/api/v1/market"));
        ddo.put("service", services);
        String ddoString = JSON.toPrettyString(ddo);

        Resolver resolver = DexResolver.create();
        // creating unique DID
        DID surferDID = DID.createRandom();
        //registering the  DID and DDO
        resolver.registerDID(surferDID, ddoString);

        // creating a Remote agent instance for given Ocean and DID
        RemoteAgent remoteAgent = RemoteAgent.connect(resolver, surferDID, AgentService.getRemoteAccount());
        assumeTrue(null != remoteAgent);
        assertEquals(remoteAgent.getDID(), surferDID);
        // verify the DID format
        assertEquals(remoteAgent.getDID().getMethod(), "dep");
        assertEquals(remoteAgent.getDID().getScheme(), "did");
        assumeTrue(null != remoteAgent.getDDO());
    }

    @Test
    public void testRegistrationForException() {


        assertThrows(IllegalArgumentException.class, () -> {
            RemoteAgent.connect(DID.createRandom(), null);
        });


    }

}
