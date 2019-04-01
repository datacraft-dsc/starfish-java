package sg.dex.starfish.developer_usecase;

import org.junit.Before;
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

import static junit.framework.TestCase.assertEquals;

/**
 * As a developer managing a Ocean Agent,
 * I need to be able to update service endpoints for my Agent
 */
@RunWith(JUnit4.class)
public class AgentEndpointUpdate_05 {
    RemoteAgent remoteAgent;

    @Before
    public void setup() {
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
        remoteAgent = RemoteAgent.create(ocean, surferDID);
    }

    @Test
    public void testServiceEndPoint() {

        assertEquals(remoteAgent.getStorageEndpoint(), "/api/v1/assets");
        assertEquals(remoteAgent.getInvokeEndpoint(), "/api/v1/invoke");
        assertEquals(remoteAgent.getMetaEndpoint(), "/api/v1/meta");
        assertEquals(remoteAgent.getMarketEndpoint(), "/api/v1/market");
    }

    @Test(expected = Error.class)
    public void testForNull() {
        Ocean ocean = Ocean.connect();
        // creating unique DID
        DID surferDID = DID.createRandom();
        //registering the  DID and DDO
        // adding invalid json data
        ocean.registerLocalDID(surferDID, "Invalid json data");
        // will throw JSON  parsing Error
        RemoteAgent remoteAgent2 = RemoteAgent.create(ocean, surferDID);

        System.out.println(remoteAgent2.getStorageEndpoint());

    }
}
