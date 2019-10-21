//package developerTC;
//
//import org.junit.Before;
//import org.junit.Test;
//import sg.dex.starfish.*;
//import sg.dex.starfish.exception.JobFailedException;
//import sg.dex.starfish.impl.memory.LocalResolverImpl;
//import sg.dex.starfish.impl.memory.MemoryAsset;
//import sg.dex.starfish.impl.memory.MemoryBundle;
//import sg.dex.starfish.impl.remote.*;
//import sg.dex.starfish.util.DID;
//import sg.dex.starfish.util.JSON;
//import sg.dex.starfish.util.Params;
//import sg.dex.starfish.util.Utils;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.Assert.assertArrayEquals;
//
//
///**
// * As a developer working with Ocean,
// * I wish to invoke a free service available on the Ocean ecosystem
// * and obtain the results as a new Ocean Asset
// */
//public class InvokeServiceFree_20 {
//
//
//    private DID did;
//    private Resolver resolver = new LocalResolverImpl();
//    private RemoteAccount remoteAccount;
//
//
//    @Before
//    public void setUp() {
//        // surfer should be running
//        did = getInvokeDid();
//        remoteAccount = getRemoteAccount("Aladdin", "OpenSesame");
//        resolver.registerDID(did, getDdo());
//
//
//    }
//
//    private RemoteAccount getRemoteAccount(String userName, String password) {
//        //Creating remote Account
//        Map<String, Object> credentialMap = new HashMap<>();
//        credentialMap.put("username", userName);
//        credentialMap.put("password", password);
//        return RemoteAccount.create(Utils.createRandomHexString(32), credentialMap);
//
//    }
//
//    private String getDdo() {
//        String surferURL = AgentService.getSurferUrl();
//        String invokeURL = AgentService.getInvokeUrl();
//        Map<String, Object> ddo = new HashMap<>();
//        List<Map<String, Object>> services = new ArrayList<>();
//
//        services.add(Utils.mapOf(
//                "type", "Ocean.Invoke.v1",
//                "serviceEndpoint", invokeURL + "/api/v1"));
//        services.add(Utils.mapOf(
//                "type", "Ocean.Meta.v1",
//                "serviceEndpoint", surferURL + "/api/v1/meta"));
//        services.add(Utils.mapOf(
//                "type", "Ocean.Storage.v1",
//                "serviceEndpoint", surferURL + "/api/v1/assets"));
//        services.add(Utils.mapOf(
//                "type", "Ocean.Auth.v1",
//                "serviceEndpoint", surferURL + "/api/v1/auth"));
//        ddo.put("service", services);
//        return JSON.toPrettyString(ddo);
//
//    }
//
//    private DID getInvokeDid() {
//        DID did = DID.createRandom();
//        return did;
//
//    }
//
//    /**
//     * TEST PRIME ::For this operation the input must be type JSON and the response will  be type asset.
//     * it support both SYNC and ASYNC
//     * this test case is for testing Sync behaviour SYNC
//     */
//    @Test
//    public void testPrimeSync_1() {
//
//        // input to the operation
//        Map<String, Object> metaMap = new HashMap<>();
//        metaMap.put("first-n", "11");
//
//        RemoteAgent agentI = RemoteAgent.create(resolver, did, remoteAccount);
//
//        // get asset form asset id of remote operation asset
//        Operation remoteOperation = agentI.getAsset("0e48ad0c07f6fe87762e24cba3e013a029b7cd734310bface8b3218280366791");
//
////        // response will have asset id as value which has the result of the operation
//        Map<String, Object> response = remoteOperation.invokeResult(metaMap);
////
//        Map<String, Object> result = (Map<String, Object>) remoteOperation.getOperationSpec().get("results");
//
//
//        for (Map.Entry<String, Object> me : result.entrySet()) {
//            Map<String, Object> spec = (Map<String, Object>) me.getValue();
//            String type = (String) spec.get("type");
//            if (type.equals("asset")) {
//                DataAsset dataAsset = (DataAsset) response.get(me.getKey());
//                assertArrayEquals(new byte[]{2, 3, 5, 7, 11}, dataAsset.getContent());
//
//            } else if (type.equals("json")) {
//                Object a = response.get(me.getKey());
//
//            }
//        }
//
//    }
//
//    /**
//     * TEST PRIME ::For this operation the input must be type JSON and the response will  be type asset.
//     * it support both SYNC and ASYNC
//     * this test case is for testing ASYNC behaviour SYNC
//     */
//    @Test
//    public void testPrimeAsync_1() {
//
//        // input to the operation
//        Map<String, Object> metaMap = new HashMap<>();
//        metaMap.put("first-n", "20");
//
//        // RemoteAgent agentS =RemoteAgent.create(ocean,didSurfer,remoteAccount);
//        RemoteAgent agentI = RemoteAgent.create(resolver, did, remoteAccount);
//
//        // get asset form asset id
//        Operation remoteOperation = agentI.getAsset("0e48ad0c07f6fe87762e24cba3e013a029b7cd734310bface8b3218280366791");
//        // invoking the prime operation and will get the job associated
//        Job job = remoteOperation.invokeAsync(metaMap);
//
//        // waiting for job to get completed
//        Map<String, Object> remoteAsset = job.getResult();
//
//        // Map<String, Object> response = Params.formatResponse(remoteOperation, JSON.toMap(remoteAsset.toString()), agentI);
//
//
//        Map<String, Object> result = (Map<String, Object>) remoteOperation.getOperationSpec().get("results");
//
//        for (Map.Entry<String, Object> me : result.entrySet()) {
//            Map<String, Object> spec = (Map<String, Object>) me.getValue();
//            String type = (String) spec.get("type");
//            if (type.equals("asset")) {
//                DataAsset asset = (DataAsset) remoteAsset.get(me.getKey());
//                assertArrayEquals(asset.getContent(), new byte[]{2, 3, 5, 7, 11, 13, 17, 19});
//
//            } else if (type.equals("json")) {
//                Object a = remoteAsset.get(me.getKey());
//
//            }
//        }
//
//
//    }
//
//    /**
//     * Validating wrong Job ID message.
//     */
//
//    /**
//     * TEST PRIME ::Testing for invalid job id.
//     * it support both SYNC and ASYNC
//     * this test case is for testing ASYNC behaviour SYNC
//     */
//    @Test(expected = JobFailedException.class)
//    public void testPrimeAsync_WrongJobID() {
//
//        // input to the operation
//        Map<String, Object> metaMap = new HashMap<>();
//        metaMap.put("first-n", "20");
//
//        // RemoteAgent agentS =RemoteAgent.create(ocean,didSurfer,remoteAccount);
//        RemoteAgent agentI = RemoteAgent.create(resolver, did, remoteAccount);
//
//        // get asset form asset id
//        Operation remoteOperation = agentI.getAsset("0e48ad0c07f6fe87762e24cba3e013a029b7cd734310bface8b3218280366791");
//        // invoking the prime operation and will get the job associated
//        Job job = remoteOperation.invokeAsync(metaMap);
//        Map<String, Object> jobData = new HashMap<>();
//        jobData.put("jobid", "invalid");
//        Job invalidJob = RemoteJob.create(agentI, JSON.toPrettyString(jobData));
//
//        // waiting for job to get completed
//        Object remoteAsset = invalidJob.getResult(10000);
//
//
//    }
//
//    /**
//     * TEST HASHING ::For this operation the input must be type JSON and the response will  be type asset.
//     * it support both SYNC and ASYNC
//     * this test case is for testing ASync behaviour of HASHING
//     */
//    @Test
//    public void testHashingAsync_1() {
//
//        // input to the operation
//        Map<String, Object> metaMap = new HashMap<>();
//        metaMap.put("to-hash", "test_Async");
//
//        // RemoteAgent agentS =RemoteAgent.create(ocean,didSurfer,remoteAccount);
//        RemoteAgent agentI = RemoteAgent.create(resolver, did, remoteAccount);
//
//        // get asset form asset id
//        Operation remoteOperation = agentI.getAsset("678d5e333ca9ea1a0f7939b4f1d923f73a1641dda8da0430c2b3604d3ceb5991");
//
//        // invoking the prime operation and will get the job associated
//        Job job = remoteOperation.invokeAsync(metaMap);
//
//        // waiting for job to get completed
//        Map<String, Object> remoteAsset = job.getResult(10000);
//
//        Map<String, Object> response = Params.formatResponse(remoteOperation, JSON.toMap(remoteAsset.toString()), agentI);
//
//
//        Map<String, Object> result = (Map<String, Object>) remoteOperation.getOperationSpec().get("results");
//
//        for (Map.Entry<String, Object> me : result.entrySet()) {
//            Map<String, Object> spec = (Map<String, Object>) me.getValue();
//            String type = (String) spec.get("type");
//            if (type.equals("asset")) {
//                Asset a = (Asset) response.get(me.getKey());
//            } else if (type.equals("json")) {
//                Object a = response.get(me.getKey());
//
//            }
//        }
//
//
//    }
//
//    /**
//     * TEST HASHING ::For this operation the input must be type JSON and the response will  be type asset.
//     * it support both SYNC and ASYNC
//     * this test case is for testing SYNC behaviour of HASHING
//     */
//    @Test
//    public void testHashingSync_1() {
//
//        Map<String, Object> metaMap = new HashMap<>();
//        metaMap.put("to-hash", "test");
//
//        // RemoteAgent agentS =RemoteAgent.create(ocean,didSurfer,remoteAccount);
//        RemoteAgent agentI = RemoteAgent.create(resolver, did, remoteAccount);
//
//        // get asset form asset id
//        Operation remoteOperation = agentI.getAsset("678d5e333ca9ea1a0f7939b4f1d923f73a1641dda8da0430c2b3604d3ceb5991");
//
//        // invoking the prime operation and will get the job associated
//        Map<String, Object> response = remoteOperation.invokeResult(metaMap);
//        Map<String, Object> result = (Map<String, Object>) remoteOperation.getOperationSpec().get("results");
//        for (Map.Entry<String, Object> me : result.entrySet()) {
//            Map<String, Object> spec = (Map<String, Object>) me.getValue();
//            String type = (String) spec.get("type");
//            if (type.equals("asset")) {
//                RemoteDataAsset a = (RemoteDataAsset) response.get(me.getKey());
//            } else if (type.equals("json")) {
//                Object a = response.get(me.getKey());
//
//            }
//        }
//
//    }
//
//    /**
//     * TEST ASSET_HASHING ::For this operation the input must be type JSON and the response will  be type asset.
//     * it support both SYNC and ASYNC
//     * this test case is for testing Sync behaviour of ASSET_HASHING
//     */
//    //Sync is not working : issue reported , Kiran is working on this..
//    @Test
//    public void testAssetHashingSync_1() {
//
//        // RemoteAgent agentS =RemoteAgent.create(ocean,didSurfer,remoteAccount);
//        RemoteAgent agentI = RemoteAgent.create(resolver, did, remoteAccount);
//        // asset must be uploaded as invoke will work only on RemoteAsset
//        Asset a = MemoryAsset.create(new byte[]{3, 4, 5, 6});
//        // uploading the asset, it will do the registration and upload both
//        RemoteDataAsset remoteAsset1 = agentI.uploadAsset(a);
//
//        Map<String, Object> metaMap = new HashMap<>();
//        metaMap.put("to-hash", remoteAsset1);
//
//
//        // get asset form asset id
//        // Operation remoteOperation =(Operation)agentI.getAsset("3099ae4f493d72777e4b57db43226456d67867728c0695d1eaf51f3035b20e07");
//        Operation remoteOperation = agentI.getAsset("3eea0affa77814713e5b18f22761d433162d53530e9824cd14fcca7d38b64f73");
//
//        // invoking the prime operation and will get the job associatedm
//        Map<String, Object> metaData = remoteOperation.invokeResult(metaMap);
//
//
//    }
//
//
//    /**
//     * TEST ASSET_HASHING ::For this operation the input must be type JSON and the response will  be type asset.
//     * it support both SYNC and ASYNC
//     * this test case is for testing ASync behaviour of ASSET_HASHING
//     */
//    @Test
//    public void testAssetHashingAsync_1() {
//
//
//        // RemoteAgent agentS =RemoteAgent.create(ocean,didSurfer,remoteAccount);
//        RemoteAgent agentI = RemoteAgent.create(resolver, did, remoteAccount);
//
//
//        // asset must be uploaded as invoke will work only on RemoteAsset
//        Asset a1 = MemoryAsset.create(new byte[]{3, 4, 5, 6});
//        // uploading the asset, it will do the registration and upload both
//        RemoteDataAsset remoteAsset1 = agentI.uploadAsset(a1);
//
//        Map<String, Object> metaMap = new HashMap<>();
//        metaMap.put("to-hash", remoteAsset1);
//        // get asset form asset id
//        Operation remoteOperation = agentI.getAsset("3eea0affa77814713e5b18f22761d433162d53530e9824cd14fcca7d38b64f73");
//
//
//        // invoking the prime operation and will get the job associated
//        Job job = remoteOperation.invokeAsync(metaMap);
//
//        // waiting for job to get completed
//        Map<String, Object> remoteAsset = job.getResult(10000);
//
//        Map<String, Object> response = Params.formatResponse(remoteOperation, JSON.toMap(remoteAsset.toString()), agentI);
//
//
//        Map<String, Object> result = (Map<String, Object>) remoteOperation.getOperationSpec().get("results");
//
//        for (Map.Entry<String, Object> me : result.entrySet()) {
//            Map<String, Object> spec = (Map<String, Object>) me.getValue();
//            String type = (String) spec.get("type");
//            if (type.equals("asset")) {
//                RemoteDataAsset a = (RemoteDataAsset) response.get(me.getKey());
//            } else if (type.equals("json")) {
//                Object a = response.get(me.getKey());
//
//            }
//        }
//    }
//
//    /**
//     * TEST ASSET_HASHING ::For this operation the input must be type JSON and the response will  be type asset.
//     * it support both SYNC and ASYNC
//     * this test case is for testing ASync behaviour of ASSET_HASHING
//     */
//    @Test(expected = Exception.class)
//    public void testAssetHashingAsyncForBundle_1() {
//
//
//        // RemoteAgent agentS =RemoteAgent.create(ocean,didSurfer,remoteAccount);
//        RemoteAgent agentI = RemoteAgent.create(resolver, did, remoteAccount);
//
//
//        // creating  assets
//
//        Asset a1 = MemoryAsset.create("testing bundle".getBytes());
//        //ARemoteAsset ra1 =agentI.registerAsset(a1);
//
//        //assigning each asset with name and adding to map
//        Map<String, Asset> assetBundle = new HashMap<>();
//        assetBundle.put("one", a1);
//
//        Bundle remoteBundle = MemoryBundle.create( assetBundle);
//        // uploading the asset, it will do the registration and upload both
//        RemoteBundle remoteAsset1 = agentI.registerAsset(remoteBundle);
//
//        Map<String, Object> metaMap = new HashMap<>();
//        metaMap.put("to-hash", remoteAsset1);
//        // get asset form asset id
//        Operation remoteOperation = agentI.getAsset("678d5e333ca9ea1a0f7939b4f1d923f73a1641dda8da0430c2b3604d3ceb5991");
//
//
//        // invoking the prime operation and will get the job associated
//        Job job = remoteOperation.invokeAsync(metaMap);
//
//        // waiting for job to get completed
//        Map<String, Object> remoteAsset = job.getResult(10000);
//
//        // Map<String, Object> response = Params.formatResponse(remoteOperation, JSON.toMap(remoteAsset.toString()), agentI);
//
//
//        Map<String, Object> result = (Map<String, Object>) remoteOperation.getOperationSpec().get("results");
//
//        for (Map.Entry<String, Object> me : result.entrySet()) {
//            Map<String, Object> spec = (Map<String, Object>) me.getValue();
//            String type = (String) spec.get("type");
//            if (type.equals("asset")) {
//                RemoteDataAsset a = (RemoteDataAsset) remoteAsset.get(me.getKey());
//            } else if (type.equals("json")) {
//                Object a = remoteAsset.get(me.getKey());
//
//            }
//        }
//    }
//}
