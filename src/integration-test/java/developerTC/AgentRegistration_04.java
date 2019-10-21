package developerTC;

import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
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

import static org.junit.Assert.assertEquals;

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

Resolver resolver= new LocalResolverImpl();
        // creating unique DID
        DID surferDID = DID.createRandom();
        //registering the  DID and DDO
        resolver.registerDID(surferDID, ddoString);

        // creating a Remote agent instance for given Ocean and DID
        RemoteAgent remoteAgent = RemoteAgent.create(resolver, surferDID);
        Assume.assumeNotNull(remoteAgent);
        assertEquals(remoteAgent.getDID(), surferDID);
        // verify the DID format
        assertEquals(remoteAgent.getDID().getMethod(), "op");
        assertEquals(remoteAgent.getDID().getScheme(), "did");
        Assume.assumeNotNull(remoteAgent.getDDO());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegistrationForException() {
        Map<String, Object> ddo = new HashMap<>();
        List<Map<String, Object>> services = new ArrayList<>();

        String ddoString = JSON.toPrettyString(ddo);

        //Should not allow to create the null DID ?
        //getting the default Ocean instance
        Resolver resolver=new LocalResolverImpl();
        RemoteAgent remoteAgent = RemoteAgent.create(resolver, null);
        //registering the  DID and DDO

        resolver.registerDID(null, ddoString);

        assertEquals(remoteAgent.getDID(), null);


    }

}
