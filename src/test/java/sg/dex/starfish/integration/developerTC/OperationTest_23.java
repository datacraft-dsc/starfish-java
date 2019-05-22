package sg.dex.starfish.integration.developerTC;

import org.junit.Before;
import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.Operation;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAccount;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteAsset;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Params;
import sg.dex.starfish.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;

public class OperationTest_23 {

    private DID did;
    private Ocean ocean=Ocean.connect();
    private RemoteAccount remoteAccount;


    @Before
    public void setUp() {
        // surfer should be running
        did=getInvokeDid();
        remoteAccount = getRemoteAccount("Aladdin","OpenSesame");
        ocean.registerLocalDID(did,getDdo());

    }

    private RemoteAccount getRemoteAccount(String userName,String password){
        //Creating remote Account
        Map<String,Object> credentialMap = new HashMap<>();
        credentialMap.put("username",userName);
        credentialMap.put("password",password);
        return RemoteAccount.create(Utils.createRandomHexString(32), credentialMap);

    }

    private String getDdo(){
        Map<String, Object> ddo = new HashMap<>();
        List<Map<String, Object>> services = new ArrayList<>();

        services.add(Utils.mapOf(
                "type", "Ocean.Invoke.v1",
                "serviceEndpoint", "http://localhost:3000" ));
        services.add(Utils.mapOf(
                "type", "Ocean.Meta.v1",
                "serviceEndpoint", "http://localhost:8082" + "/api/v1/meta"));
        services.add(Utils.mapOf(
                "type", "Ocean.Storage.v1",
                "serviceEndpoint", "http://localhost:8082" + "/api/v1/assets"));
        services.add(Utils.mapOf(
                "type", "Ocean.Auth.v1",
                "serviceEndpoint", "http://localhost:8082" + "/api/v1/auth"));
        ddo.put("service", services);
        return JSON.toPrettyString(ddo);

    }

    private DID getInvokeDid(){
        DID did = DID.createRandom();
        return did;

    }
    /**
     * TEST PRIME ::For this operation the input must be type JSON and the response will  be type asset.
     * it support both SYNC and ASYNC
     * this test case is for testing Sync behaviour SYNC
     */
    @Test
    public void testPrimeSync_1() {

        // input to the operation
        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("first-n", "11");

        RemoteAgent agentI =RemoteAgent.create(ocean,did,remoteAccount);

        // get asset form asset id
        Operation remoteOperation =(Operation)agentI.getAsset("8d658b5b09ade5526aecf669e4291c07d88e9791420c09c51d2f922f721858d1");

//        // response will have asset id as value which has the result of the operation
        Map<String, Object> response = remoteOperation.invokeResult(metaMap);
//
        Map<String, Object> result = (Map<String, Object>) remoteOperation.getOperationSpec().get("results");
        for (Map.Entry<String, Object> me : result.entrySet()) {
            Map<String, Object> spec = (Map<String, Object>) me.getValue();
            String type = (String) spec.get("type");
            if (type.equals("asset")) {
                Asset a = (Asset) response.get(me.getKey());
                assertNotNull(a);
            } else if (type.equals("json")) {
                Object a = response.get(me.getKey());
                assertNotNull(a);

            }
        }

    }

    /**
     * TEST PRIME ::For this operation the input must be type JSON and the response will  be type asset.
     * it support both SYNC and ASYNC
     * this test case is for testing ASYNC behaviour SYNC
     */
    @Test
    public void testPrimeAsync_1() {

        // input to the operation
        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("first-n", "20");

        // RemoteAgent agentS =RemoteAgent.create(ocean,didSurfer,remoteAccount);
        RemoteAgent agentI =RemoteAgent.create(ocean,did,remoteAccount);

        // get asset form asset id
        Operation remoteOperation =(Operation)agentI.getAsset("8d658b5b09ade5526aecf669e4291c07d88e9791420c09c51d2f922f721858d1");
        // invoking the prime operation and will get the job associated
        Job job = remoteOperation.invokeAsync(metaMap);

        // waiting for job to get completed
        Object remoteAsset = job.awaitResult(10000);

        Map<String, Object> response = Params.formatResponse(remoteOperation, JSON.toMap(remoteAsset.toString()), agentI);


        Map<String, Object> result = (Map<String, Object>) remoteOperation.getOperationSpec().get("results");

        for (Map.Entry<String, Object> me : result.entrySet()) {
            Map<String, Object> spec = (Map<String, Object>) me.getValue();
            String type = (String) spec.get("type");
            if (type.equals("asset")) {
                RemoteAsset a = (RemoteAsset) response.get(me.getKey());
                assertNotNull(a);
            } else if (type.equals("json")) {
                Object a = response.get(me.getKey());
                assertNotNull(a);

            }
        }


    }

    /**
     * TEST HASHING ::For this operation the input must be type JSON and the response will  be type asset.
     * it support both SYNC and ASYNC
     * this test case is for testing ASync behaviour of HASHING
     */
    @Test
    public void testHashingAsync_1() {

        // input to the operation
        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("to-hash", "test_Async");

        // RemoteAgent agentS =RemoteAgent.create(ocean,didSurfer,remoteAccount);
        RemoteAgent agentI =RemoteAgent.create(ocean,did,remoteAccount);

        // get asset form asset id
        Operation remoteOperation =(Operation)agentI.getAsset("8ade9c7505bcadaab8dacf6848e88ddb4aa6a295612eb01759e35aeb65daeac2");

        // invoking the prime operation and will get the job associated
        Job job = remoteOperation.invokeAsync(metaMap);

        // waiting for job to get completed
        Object remoteAsset = job.awaitResult(10000);

        Map<String, Object> response = Params.formatResponse(remoteOperation, JSON.toMap(remoteAsset.toString()), agentI);


        Map<String, Object> result = (Map<String, Object>) remoteOperation.getOperationSpec().get("results");

        for (Map.Entry<String, Object> me : result.entrySet()) {
            Map<String, Object> spec = (Map<String, Object>) me.getValue();
            String type = (String) spec.get("type");
            if (type.equals("asset")) {
                Asset a = (Asset) response.get(me.getKey());
                assertNotNull(a);
            } else if (type.equals("json")) {
                Object a = response.get(me.getKey());
                assertNotNull(a);

            }
        }


    }

    /**
     * TEST HASHING ::For this operation the input must be type JSON and the response will  be type asset.
     * it support both SYNC and ASYNC
     * this test case is for testing SYNC behaviour of HASHING
     */
    @Test
    public void testHashingSync_1() {

        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("to-hash", "test");

        // RemoteAgent agentS =RemoteAgent.create(ocean,didSurfer,remoteAccount);
        RemoteAgent agentI =RemoteAgent.create(ocean,did,remoteAccount);

        // get asset form asset id
        Operation remoteOperation =(Operation)agentI.getAsset("8ade9c7505bcadaab8dacf6848e88ddb4aa6a295612eb01759e35aeb65daeac2");

        // invoking the prime operation and will get the job associated
        Map<String, Object> response = remoteOperation.invokeResult(metaMap);
        Map<String, Object> result = (Map<String, Object>) remoteOperation.getOperationSpec().get("results");
        for (Map.Entry<String, Object> me : result.entrySet()) {
            Map<String, Object> spec = (Map<String, Object>) me.getValue();
            String type = (String) spec.get("type");
            if (type.equals("asset")) {
                RemoteAsset a = (RemoteAsset) response.get(me.getKey());
                assertNotNull(a);
            } else if (type.equals("json")) {
                Object a = response.get(me.getKey());
                assertNotNull(a);

            }
        }

    }

    /**
     * TEST ASSET_HASHING ::For this operation the input must be type JSON and the response will  be type asset.
     * it support both SYNC and ASYNC
     * this test case is for testing Sync behaviour of ASSET_HASHING
     */
    //Sync is not working : issue reported , Kiran is working on this..
//    @Test
    public void testAssetHashingSync_1() {

        // RemoteAgent agentS =RemoteAgent.create(ocean,didSurfer,remoteAccount);
        RemoteAgent agentI =RemoteAgent.create(ocean,did,remoteAccount);
        // asset must be uploaded as invoke will work only on RemoteAsset
        Asset a = MemoryAsset.create("this is a asset to test Async data operation");
        // uploading the asset, it will do the registration and upload both
        RemoteAsset remoteAsset1 = (RemoteAsset) agentI.uploadAsset(a);

        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("to-hash", remoteAsset1);

        // get asset form asset id
        Operation remoteOperation =(Operation)agentI.getAsset("3099ae4f493d72777e4b57db43226456d67867728c0695d1eaf51f3035b20e07");

        // invoking the prime operation and will get the job associated
        Map<String, Object> metaData = remoteOperation.invokeResult(metaMap);



    }



    /**
     * TEST ASSET_HASHING ::For this operation the input must be type JSON and the response will  be type asset.
     * it support both SYNC and ASYNC
     * this test case is for testing ASync behaviour of ASSET_HASHING
     */
    @Test
    public void testAssetHashingAsync_1() {


        // RemoteAgent agentS =RemoteAgent.create(ocean,didSurfer,remoteAccount);
        RemoteAgent agentI =RemoteAgent.create(ocean,did,remoteAccount);


        // asset must be uploaded as invoke will work only on RemoteAsset
        Asset a1 = MemoryAsset.create("this is a asset to test Async data operation");
        // uploading the asset, it will do the registration and upload both
        RemoteAsset remoteAsset1 = (RemoteAsset) agentI.uploadAsset(a1);

        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("to-hash", remoteAsset1);
        // get asset form asset id
        Operation remoteOperation =(Operation)agentI.getAsset("3099ae4f493d72777e4b57db43226456d67867728c0695d1eaf51f3035b20e07");


        // invoking the prime operation and will get the job associated
        Job job = remoteOperation.invokeAsync(metaMap);

        // waiting for job to get completed
        Object remoteAsset = job.awaitResult(10000);

        Map<String, Object> response = Params.formatResponse(remoteOperation, JSON.toMap(remoteAsset.toString()), agentI);


        Map<String, Object> result = (Map<String, Object>) remoteOperation.getOperationSpec().get("results");

        for (Map.Entry<String, Object> me : result.entrySet()) {
            Map<String, Object> spec = (Map<String, Object>) me.getValue();
            String type = (String) spec.get("type");
            if (type.equals("asset")) {
                RemoteAsset a = (RemoteAsset) response.get(me.getKey());
                assertNotNull(a);
            } else if (type.equals("json")) {
                Object a = response.get(me.getKey());
                assertNotNull(a);

            }
        }
    }

}
