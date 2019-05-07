package sg.dex.starfish.integration.developerTC;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteAsset;
import sg.dex.starfish.impl.remote.RemoteOperation;
import sg.dex.starfish.integration.connection_check.AssumingConnection;
import sg.dex.starfish.integration.connection_check.ConnectionChecker;
import sg.dex.starfish.integration.developerTC.remoteoperation.HashingRemoteOperation;
import sg.dex.starfish.integration.developerTC.remoteoperation.ToHashRemoteOperation;
import sg.dex.starfish.util.JSON;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;

public class OperationTest_23 {

    @ClassRule
    public static AssumingConnection assumingInvokeConnection =
            new AssumingConnection(new ConnectionChecker(RemoteAgentConfig.getInvokeUrl()));

    private RemoteAgent remoteAgentSurfer;
    private RemoteAgent remoteAgentInvoke;


    @Before
    public void setUp() {
        // surfer should be running
        remoteAgentSurfer = RemoteAgentConfig.getRemoteAgent();
        remoteAgentInvoke = RemoteAgentConfig.getInvoke();

    }

    @Test
    public void testAsyncOperation() {

        // asset must be uploaded as invoke will work only on RemoteAsset
        Asset a = MemoryAsset.create("this is a asset to test Async data operation");
        // uploading the asset, it will do the registration and upload both
        RemoteAsset remoteAsset = remoteAgentSurfer.uploadAsset(a);


        //
        Map<String, Asset> metaMap = new HashMap<>();
        metaMap.put("to-hash",remoteAsset);

       String meta1="{\n" +
               "  \"name\": \"hashing\",\n" +
               "  \"description\": \"hashes the input\",\n" +
               "  \"type\": \"operation\",\n" +
               "  \"operation\": {\n" +
               "    \"modes\": [\n" +
               "      \"sync\",\n" +
               "      \"async\"\n" +
               "    ],\n" +
               "    \"params\": {\n" +
               "      \"to-hash\": {\n" +
               "        \"type\": \"asset\",\n" +
               "        \"position\": 0,\n" +
               "        \"required\": true\n" +
               "      }\n" +
               "    },\n" +
               "    \"results\": {\n" +
               "      \"hash_value\": {\n" +
               "        \"type\": \"asset\"\n" +
               "      }\n" +
               "    }\n" +
               "  }\n" +
               "}";
       // String meta = "{\"params\": {\"to-hash\": {}}}";
        HashingRemoteOperation hashingRemoteOperation = HashingRemoteOperation.create(remoteAgentInvoke, meta1);

        Job job = hashingRemoteOperation.invokeAsync(metaMap);

        Asset asset = job.awaitResult();
        // currently asset is coming null so that need to be fixed
        assertNotNull(asset);
    }


    @Test
    public void testGetJobStatus() {

        //
        Map<String, Asset> metaMap = new HashMap<>();
        // create a remoteAsset

        Asset a = MemoryAsset.create("this is a asset data to check the job status ");
        // uploading the asset, it will do the registration and upload both
        RemoteAsset remoteA = remoteAgentSurfer.uploadAsset(a);
        metaMap.put("to-hash", remoteA);

        String meta="{\n" +
                "  \"params\": {\n" +
                "    \"to-hash\": {\n" +
                "      \"required\": true,\n" +
                "      \"position\": 0,\n" +
                "      \"type\": \"asset\"\n" +
                "    },\n" +
                "    \"did\":\"assethashing\"\n" +
                "  }\n" +
                "}";
        RemoteOperation remoteOperation = RemoteOperation.create(remoteAgentInvoke, meta);

        Job job = remoteOperation.invoke(metaMap);

        assertNotNull(job.getJobID());
        assertNotNull(job.isComplete());
        //Still not implemented, code change is required for this ,
        // job.getResult() will return an Asset
//        // To be
//        assertNull(job);
    }
//
    @Test
    public void testSyncOperation() {

        // asset must be uploaded as invoke will work only on RemoteAsset
        String data_to_hash ="this is a asset to test syncdata operation";
        // uploading the asset, it will do the registration and upload both
       // RemoteAsset remoteAsset = remoteAgentSurfer.uploadAsset(a);

        // creating invoke remote agent
        remoteAgentInvoke = RemoteAgentConfig.getInvoke();
        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("to-hash", data_to_hash);

        String meta="{\n" +
                "  \"params\": {\n" +
                "    \"to-hash\": {\n" +
                "      \"required\": \"true\",\n" +
                "      \"position\": 0,\n" +
                "      \"type\": \"asset\"\n" +
                "      \n" +
                "    },\n" +
                "    \"did\": \"hashing\"\n" +
                "  },\n" +
                "  \"mode\":\"sync\",\n" +
                "  \"result\": {\n" +
                "        \"hash-value\": {\n" +
                "           \"type\": \"asset\"\n" +
                "      \n" +
                "    }\n" +
                "  }\n" +
                "}";


        // creating an instance of Remote operation based on remote agent and metaData
        RemoteOperation remoteOperation = RemoteOperation.create(remoteAgentInvoke, meta);

        // call Sync Operation
        Object result = remoteOperation.invokeResult(metaMap);
        System.out.println("Hashing result : " + result);

        assertNotNull(result);
    }

    @Test(expected = Exception.class)
    public void testSyncOperationWithDifferentMode() {

        // asset must be uploaded as invoke will work only on RemoteAsset
        String data_to_hash ="this is a asset to test sync data operation";
        // uploading the asset, it will do the registration and upload both
        // RemoteAsset remoteAsset = remoteAgentSurfer.uploadAsset(a);

        // creating invoke remote agent
        remoteAgentInvoke = RemoteAgentConfig.getInvoke();
        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("to-hash", data_to_hash);

        String meta="{\n" +
                "  \"params\": {\n" +
                "    \"to-hash\": {\n" +
                "      \"required\": \"true\",\n" +
                "      \"position\": 0,\n" +
                "      \"type\": \"asset\"\n" +
                "      \n" +
                "    },\n" +
                "    \"did\": \"hashing\"\n" +
                "  },\n" +
                "  \"mode\":\"Notsync\",\n" +
                "  \"result\": {\n" +
                "        \"hash-value\": {\n" +
                "           \"type\": \"asset\"\n" +
                "      \n" +
                "    }\n" +
                "  }\n" +
                "}";


        // creating an instance of Remote operation based on remote agent and metaData
        RemoteOperation remoteOperation = RemoteOperation.create(remoteAgentInvoke, meta);

        // call Sync Operation
        Object result = remoteOperation.invokeResult(metaMap);
        System.out.println("Hashing result : " + result);

        assertNotNull(result);
    }

    @Test
    public void testHashing() {

        // creating invoke remote agent
        remoteAgentInvoke = RemoteAgentConfig.getInvoke();


        Map<String, Object> metaMap1 = new HashMap<>();
       // metaMap1.put("to_hash", "Test123");
        metaMap1.put("to-hash","ttttt")   ;


        String meta1="{\n" +
                "  \"name\": \"hashing\",\n" +
                "  \"description\":\"hashes the input\",\n" +
                "  \"type\":\"operation\",\n" +
                "  \"operation\" : {\"modes\":\"sync\",\n" +
                "                  \"params\":{\"to_hash\": {\"type\":\"json\"}},\n" +
                "                  \"results\":{\"hash-value\": {\"type\":\"json\"}}}\n" +
                "}";

        // creating an instance of Remote operation based on remote agent and metaData
        ToHashRemoteOperation remoteOperation = ToHashRemoteOperation.create(remoteAgentInvoke, meta1);

        Map<String,Object> response =remoteOperation.invokeResult(metaMap1);
        Map<String,Object> didMap =JSON.toMap( JSON.toMap(response.get("results").toString()).get("hash_value").toString());
        assertNotNull(didMap.get("did"));
    }

    @Test
    public void testOperationPrimeSync() {

        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("first-n", "10");

        String meta1="{\n" +
                "   \"name\": \"Prime computation operation\",\n" +
                "   \"type\": \"operation\",\n" +
                "   \"description\": \"Computes prime numbers\",\n" +
                "   \"author\": \"Primely Inc\",\n" +
                "   \"license\": \"CC-BY\",\n" +
                "   \"inLanguage\": \"en\",\n" +
                "   \"tags\": [\"weather\", \"uk\", \"2011\", \"temperature\", \"humidity\"],\n" +
                "   \"operation\" : {\"modes\":[\"sync\", \"async\"],\n" +
                "                  \"params\":{\"first-n\": {\"type\":\"json\"}},\n" +
                "                  \"results\":{\"primes\": {\"type\":\"asset\"}}}\n" +
                "}";


//        PrimeRemoteOperation remoteOperation = PrimeRemoteOperation.create(remoteAgentInvoke, meta1);
        Operation remoteOperation = RemoteOperation.create(remoteAgentInvoke, meta1);
        System.out.println(remoteOperation.getAssetID());

        Map<String,Object> response =remoteOperation.invokeResult(metaMap);
        Map<String,Object> didMap =JSON.toMap( JSON.toMap(response.get("results").toString()).get("primes").toString());
        System.out.println("Did : "+ didMap.get("did"));
        assertNotNull(didMap.get("did"));

    }



}

