package sg.dex.starfish.developer_usecase;

import sg.dex.starfish.Agent;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * This class is used to get the Remote Agent based on the host.
 * Currently it will connect with default OCEAN.
 */
public class RemoteAgentConfig {

    private  static RemoteAgent getSurfer(String host) {
        Map<String, Object> ddo = new HashMap<>();
        List<Map<String, Object>> services = new ArrayList<>();
        services.add(Utils.mapOf(
                "type", "Ocean.Meta.v1",
                "serviceEndpoint", host + "/api/v1/meta"));
        services.add(Utils.mapOf(
                "type", "Ocean.Storage.v1",
                "serviceEndpoint", host + "/api/v1/assets"));
        services.add(Utils.mapOf(
                "type", "Ocean.Invoke.v1",
                "serviceEndpoint", host + "/api/v1/invoke"));services.add(Utils.mapOf(
                "type", "Ocean.Market.v1",
                "serviceEndpoint", host + "/api/v1/market"));
        ddo.put("service", services);
        String ddoString = JSON.toPrettyString(ddo);

        //getting the default Ocean instance
        Ocean ocean = Ocean.connect();
        // creating unique DID
        DID surferDID = DID.createRandom();
        //registering the  DID and DDO
        ocean.registerLocalDID(surferDID, ddoString);

        // creating a Remote agent instance for given Ocean and DID
        RemoteAgent surfer = RemoteAgent.create(ocean, surferDID);

        return surfer;
    }

    private static Properties getProperties(){
        Properties properties = new Properties();
        try {
            try (InputStream is = RemoteAgentConfig.class.getClassLoader().getResourceAsStream("application_test.properties")) {
                properties.load(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  properties;
    }

    /**
     *
     * @return
     */
    public static RemoteAgent getRemoteAgent(){
        Properties properties = getProperties();
        String ip = properties.getProperty("surfer.host");
        String port = properties.getProperty("surfer.port");
        return getSurfer(ip + ":" + port);

    }

}
