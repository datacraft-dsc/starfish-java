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
import sg.dex.starfish.util.Utils;

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
        RemoteAsset remoteAsset = (RemoteAsset)remoteAgentSurfer.uploadAsset(a);


        //
        Map<String, Object> metaMap = new HashMap<>();
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
        RemoteAsset remoteA = (RemoteAsset)remoteAgentSurfer.uploadAsset(a);
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
    public void testPrimeSync_1() {

        // input to the operation
        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("first-n", "10");

        // getting the prime operation based on assed id
        Operation remoteOperation = RemoteOperation.materialize(remoteAgentSurfer, "8d658b5b09ade5526aecf669e4291c07d88e9791420c09c51d2f922f721858d1",remoteAgentInvoke);


        // response will have asset id as value which has the result of the operation
        Map<String,Object> response =remoteOperation.invokeResult(metaMap);

        //getting the Asset which has the result
        RemoteAsset resultAsset=(RemoteAsset)remoteAgentSurfer.getAsset(response.get("did").toString());

        // reading the content fo the asset
        String allPrimes=Utils.stringFromStream(resultAsset.getContentStream());

        // printing result
        System.out.println(allPrimes);
        assertNotNull(resultAsset);


    }

    @Test
    public void testPrimeAsync_1() {

        // input to the operation
        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("first-n", "20");

        // getting the prime operation based on assed id
        Operation remoteOperation = RemoteOperation.materialize(remoteAgentSurfer, "8d658b5b09ade5526aecf669e4291c07d88e9791420c09c51d2f922f721858d1",remoteAgentInvoke);

        System.out.println(remoteOperation.getAssetID());

        // invoking the prime operation and will get the job associated
        Job job =remoteOperation.invokeAsync(metaMap);

        // waiting for job to get completed
       Asset remoteAsset = job.awaitResult(10000);

       // getting the metadata of the new Asset which has the operation result
       Map<String,Object> metaData = remoteAsset.getMetadata();
       String did = metaData.get("did").toString();

       //getting the Asset which has result
        RemoteAsset resultAsset=(RemoteAsset)remoteAgentSurfer.getAsset(did);
        // getting the result form the asset content
        String allPrimes=Utils.stringFromStream(resultAsset.getContentStream());

        // printing the result
        System.out.println(allPrimes);
        assertNotNull(remoteAsset);

    }

    @Test
    public void testHashAsync_1() {

        // asset must be uploaded as invoke will work only on RemoteAsset
        Asset a = MemoryAsset.create("this is a asset to test Async data operation");
        // uploading the asset, it will do the registration and upload both
        RemoteAsset remoteAsset1 = (RemoteAsset)remoteAgentSurfer.uploadAsset(a);


        //
        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("to-hash",remoteAsset1);

        // getting the prime operation based on assed id
        Operation remoteOperation = RemoteOperation.materialize(remoteAgentSurfer, "37ace3efb694bff7a25f657920ecac27d6799f8096ec5f5bd3ddba270b4d228b",remoteAgentInvoke);

        System.out.println(remoteOperation.getAssetID());

        // invoking the prime operation and will get the job associated
        Job job =remoteOperation.invokeAsync(metaMap);

        // waiting for job to get completed
        Asset remoteAsset = job.awaitResult(10000);

        // getting the metadata of the new Asset which has the operation result
        Map<String,Object> metaData = remoteAsset.getMetadata();
        String did = metaData.get("did").toString();

        //getting the Asset which has result
        RemoteAsset resultAsset=(RemoteAsset)remoteAgentSurfer.getAsset(did);
        // getting the result form the asset content
        String allPrimes=Utils.stringFromStream(resultAsset.getContentStream());

        // printing the result
        System.out.println(allPrimes);
        assertNotNull(remoteAsset);

    }
//    @Test
//    public void testHashSync_1() {
//
//        // input to the operation
//        Map<String, Object> metaMap = new HashMap<>();
//        metaMap.put("to-hash", "test HAsing");
//
//        // getting the prime operation based on assed id
//        Operation remoteOperation = RemoteOperation.materialize(remoteAgentSurfer, "37ace3efb694bff7a25f657920ecac27d6799f8096ec5f5bd3ddba270b4d228b",remoteAgentInvoke);
//
//        // response will have asset id as value which has the result of the operation
//        Map<String,Object> response =remoteOperation.invokeResult(metaMap);
//
//        //getting the Asset which has the result
//        RemoteAsset resultAsset=(RemoteAsset)remoteAgentSurfer.getAsset(response.get("did").toString());
//
//        // reading the content fo the asset
//        String allPrimes=Utils.stringFromStream(resultAsset.getContentStream());
//
//        // printing result
//        System.out.println(allPrimes);
//        assertNotNull(resultAsset);
//
//
//
//    }
}
