package sg.dex.starfish.samples;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.Operation;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAsset;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteOperation;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class RemoteOperationSample {

    private static Properties getProperties() {
        Properties properties = new Properties();
        try {
            try (InputStream is = RemoteOperationSample.class.getClassLoader().getResourceAsStream("application_test.properties")) {
                properties.load(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    private static RemoteAgent getSurfer(String host) {
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
                "serviceEndpoint", host + "/api/v1/invoke"));
        services.add(Utils.mapOf(
                "type", "Ocean.Auth.v1",
                "serviceEndpoint", host + "/api/v1/auth"));
        services.add(Utils.mapOf(
                "type", "Ocean.Market.v1",
                "serviceEndpoint", host + "/api/v1/market"));
        ddo.put("service", services);
        String ddoString = JSON.toPrettyString(ddo);

        // getting the default Ocean instance
        Ocean ocean = Ocean.connect();
        // creating unique DID
        DID surferDID = DID.createRandom();
        // registering the DID and DDO
        ocean.registerLocalDID(surferDID, ddoString);

        // creating a Remote agent instance for given Ocean and DID
        return RemoteAgent.create(ocean, surferDID);

    }

    public static void main(String[] arg) {

        Properties p = getProperties();
        String koiHost =p.getProperty("koi.host");
        String koiPort =p.getProperty("koi.port");

       // RemoteAgent surfer = getSurfer(koiHost+":"+koiPort);
        RemoteAgent surfer = RemoteAgentConfig.getRemoteAgent();//getSurfer(koiHost+":"+koiPort);

        String info = "{\n" +
                "    \"to-hash\": \"TestKOI \" }";
        JSONObject json=null;
        JSONParser parser = new JSONParser();
        try {
            json= (JSONObject) parser.parse(info);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String meta = "{" +
                "params: " +
                "{" +
                "input: " +
                "{" +
                "required:true, " +
                "type:asset," +
                " position:0" +
                "}" +
                "}" +
                "}";

        String tesstAsset ="i am testing invoke";
       RemoteAsset remoteAsset = surfer.registerAsset(MemoryAsset.create(tesstAsset));
        RemoteOperation remoteOperation = (RemoteOperation)RemoteOperation.create(surfer,meta);
        Job job=surfer.invoke( remoteOperation,remoteAsset);





    }
}
