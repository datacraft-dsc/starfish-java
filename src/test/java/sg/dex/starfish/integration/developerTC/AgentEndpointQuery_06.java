package sg.dex.starfish.integration.developerTC;

import org.junit.Test;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;


/**
 * As a developer working with Ocean,
 * I need to obtain service endpoints for an arbitrary Agent in the Ocean ecosystem
 */
public class AgentEndpointQuery_06 {


    @Test
    public  void testServiceEndPoint(){
        RemoteAgent remoteAgent =createRemoteAgent("localhost:8080");
        // getting the URL for storage service
        String storage =remoteAgent.getStorageEndpoint();

        // getting the URL for invoke service
        String invoke =remoteAgent.getInvokeEndpoint();

        // getting the URL for metaData service
        String meta =remoteAgent.getMetaEndpoint();

        // getting the URL for market service
        String market =remoteAgent.getMarketEndpoint();

        // since we created the only the storage and meta endpoint
        // so other endpoint will be null
        assertNotNull(storage);
        assertNotNull(meta);

        // will be null
        assertNull(market);
        assertNull(invoke);


    }

    private  RemoteAgent createRemoteAgent(String host) {

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

        // getting the default Ocean instance
        Ocean ocean = Ocean.connect();
        // creating unique DID
        DID surferDID = DID.createRandom();

        // registering the DID and DDO
        ocean.registerLocalDID(surferDID, ddoString);

        // creating a Remote agent instance for given Ocean and DID
        return  RemoteAgent.create(ocean, surferDID);

    }
}
