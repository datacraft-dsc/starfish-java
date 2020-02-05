package developerTC;


import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import sg.dex.starfish.*;
import sg.dex.starfish.exception.RemoteException;
import sg.dex.starfish.impl.memory.LocalResolverImpl;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAccount;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteDataAsset;
import sg.dex.starfish.impl.resource.ResourceAsset;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * As a developer working with Ocean,
 * I wish to invoke a free service available on the Ocean ecosystem
 * and obtain the results as a new Ocean Asset
 */
@Disabled("Disabled until get updated the asset id ")
public class TestInvokeServiceFree_20 {


    private DID didSurfer;
    private DID didInvoke;
    private Resolver resolver;
    private RemoteAccount remoteAccount;


    @BeforeEach
    public void setUp() throws IOException {
        // resolver=DexResolver.create("application_resolver.properties");
        resolver = new LocalResolverImpl();
        // surfer should be running
        didSurfer = getSurferDid();
        didInvoke = getInvokeDid();
        remoteAccount = getRemoteAccount("Aladdin", "OpenSesame");
        resolver.registerDID(didSurfer, getDdo());
        resolver.registerDID(didInvoke, getDDOForInvokeAgent());


    }

    private RemoteAccount getRemoteAccount(String userName, String password) {
        //Creating remote Account
        Map<String, Object> credentialMap = new HashMap<>();
        credentialMap.put("username", userName);
        credentialMap.put("password", password);
        return RemoteAccount.create(Utils.createRandomHexString(32), credentialMap);

    }

    private String getDdo() {
        String surferURL = AgentService.getSurferUrl();
        Map<String, Object> ddo = new HashMap<>();
        List<Map<String, Object>> services = new ArrayList<>();


        services.add(Utils.mapOf(
                "type", "DEP.Meta.v1",
                "serviceEndpoint", surferURL + "/api/v1/meta"));
        services.add(Utils.mapOf(
                "type", "DEP.Storage.v1",
                "serviceEndpoint", surferURL + "/api/v1/assets"));
        services.add(Utils.mapOf(
                "type", "DEP.Auth.v1",
                "serviceEndpoint", surferURL + "/api/v1/auth"));
        ddo.put("service", services);
        return JSON.toPrettyString(ddo);

    }

    private String getDDOForInvokeAgent() {
        String invokeURL = "http://52.230.52.223:8191";
        Map<String, Object> ddo = new HashMap<>();
        List<Map<String, Object>> services = new ArrayList<>();
        String surferURL = AgentService.getSurferUrl();
        services.add(Utils.mapOf(
                "type", "DEP.Invoke.v1",
                "serviceEndpoint", invokeURL + "/api/v1/invoke"));
        services.add(Utils.mapOf(
                "type", "DEP.Meta.v1",
                "serviceEndpoint", invokeURL + "/api/v1/meta"));
        services.add(Utils.mapOf(
                "type", "DEP.Auth.v1",
                "serviceEndpoint", surferURL + "/api/v1/auth"));

        ddo.put("service", services);
        return JSON.toPrettyString(ddo);

    }

    private DID getInvokeDid() {
        DID did = DID.parse("did:op:a1d2dbf875ad06ea96432ca4d091e23c26f211b7caedba1e0b71121b2d88e1fd");
        return did;

    }

    private DID getSurferDid() {
        DID did = DID.parse("did:dex:1acd41655b2d8ea3f3513cc847965e72c31bbc9bfc38e7e7ec901852bd3c457c");
        return did;

    }

    /**
     * TEST PRIME ::For this operation the input must be type JSON and the response will  be type asset.
     * it support both SYNC and ASYNC
     * this test case is for testing Sync behaviour SYNC
     */
    @Test
    public void testPrimeSync_1() throws IOException {

        // input to the operation
        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("first-n", "11");

        RemoteAgent agentI = RemoteAgent.connect(resolver, didSurfer, remoteAccount);
        RemoteAgent agentIKoi = RemoteAgent.connect(resolver, didInvoke, remoteAccount);

        // get asset form asset id of remote operation asset
        Operation remoteOperation = agentIKoi.getAsset("1c9796e94bc2d19f6f2f5d95724f4ad63ea6aa36b31227bf19b99cb4ab09eda3");

        // response will have asset id as value which has the result of the operation
        Map<String, Object> response = remoteOperation.invokeResult(metaMap);
        for (Map.Entry<String, Object> res : response.entrySet()) {
            Object r = res.getValue();
            if (r instanceof Asset) {

                Asset resultAsset = (Asset) r;
                String actual = Utils.stringFromStream(resultAsset.getContentStream());
                String expected = "2" + System.lineSeparator() + "3" + System.lineSeparator() + "5" + System.lineSeparator() + "7";
                assertTrue(expected.trim().equals(actual.trim()));

            }

        }


    }

    /**
     * TEST PRIME ::For this operation the input must be type JSON and the response will  be type asset.
     * it support both SYNC and ASYNC
     * this test case is for testing ASYNC behaviour SYNC
     */
    @Test
    public void testPrimeAsync_1() throws IOException {

        // input to the operation
        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("first-n", "11");

        RemoteAgent agentData = RemoteAgent.connect(didSurfer, remoteAccount);
        RemoteAgent agentOperation = RemoteAgent.connect(didInvoke, remoteAccount);

        // get asset form asset id of remote operation asset
        Operation operation = agentOperation.getAsset("1c9796e94bc2d19f6f2f5d95724f4ad63ea6aa36b31227bf19b99cb4ab09eda3");

        // invoking the prime operation and will get the job associated
        Job job = operation.invokeAsync(metaMap);

        // waiting for job to get completed
        Map<String, Object> remoteAsset = job.getResult();
        // Map<String, Object> response = Params.formatResponse(operation, remoteAsset);


        for (Map.Entry<String, Object> res : remoteAsset.entrySet()) {
            Object r = res.getValue();
            if (r instanceof Asset) {
                Asset resultAsset = (Asset) r;//agentData.getAsset(did.getID());
                String actual = Utils.stringFromStream(resultAsset.getContentStream());
                // used line separator ans result was returned haveing each digit in new line.
                String expected = "2" + System.lineSeparator() + "3" + System.lineSeparator() + "5" + System.lineSeparator() + "7";
                assertTrue(expected.trim().equals(actual.trim()));

            }

        }


    }

    /**
     * Validating wrong Job ID message.
     */

    /**
     * TEST PRIME ::Testing for invalid job id.
     * it support both SYNC and ASYNC
     * this test case is for testing ASYNC behaviour SYNC
     */
    @Test
    public void testPrimeAsync_WrongJobID() {

        // input to the operation
        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("first-n", "20");

        RemoteAgent agentI = RemoteAgent.connect(resolver, didSurfer, remoteAccount);


        assertThrows(RemoteException.class, () -> {
            agentI.getAsset("0e48ad0c07f6fe87762e24cba3e013a029b7cd734310bface8b3218280366791");
        });

    }

    /**
     * TEST HASHING ::For this operation the input must be type ASSET and the response will  be type asset.
     * this is to test the  ASYNC
     * this test case is for testing ASync behaviour of HASHING
     */
    @Test
    public void testHashingAsync_1() throws IOException {

        RemoteAgent agentI = RemoteAgent.connect(didSurfer, remoteAccount);
        RemoteAgent agentIKoi = RemoteAgent.connect(didInvoke, remoteAccount);


        // input to the operation
        Map<String, Object> metaMap = new HashMap<>();
        MemoryAsset memoryAsset = MemoryAsset.create("test_Async".getBytes());
        RemoteDataAsset remoteDataAsset = agentI.uploadAsset(memoryAsset);
        metaMap.put("to-hash", remoteDataAsset);

        Operation remoteOperation = agentIKoi.getAsset("e5574e68df2dd9b1ebe277687c7e5cdd0051f4a70d0069647da948e60da47b59");

//        // invoking the prime operation and will get the job associated
        Job job = remoteOperation.invokeAsync(metaMap);

        // waiting for job to get completed
        Map<String, Object> remoteAsset = job.getResult(10000);

        // Map<String, Object> response = Params.formatResponse(remoteOperation, remoteAsset);


        for (Map.Entry<String, Object> res : remoteAsset.entrySet()) {
            Object r = res.getValue();
            if (r instanceof Asset) {
                Asset resultAsset = (Asset) r;
                String actual = Utils.stringFromStream(resultAsset.getContentStream());
                String expected = "b35af2607950b7c071fd220006f120dbe7af8944c5a91611a633173823574fe9";
                assertTrue(expected.equals(actual));

            }

        }


    }

    /**
     * TEST HASHING ::For this operation the input must be type ASSET and the response will  be type asset.
     * this is to test the sync mode
     * this test case is for testing SYNC behaviour of HASHING
     */
    @Test
    public void testHashingSync_1() {

        RemoteAgent agentI = RemoteAgent.connect(resolver, didSurfer, remoteAccount);
        RemoteAgent agentIKoi = RemoteAgent.connect(resolver, didInvoke, remoteAccount);


        // input to the operation
        Map<String, Object> metaMap = new HashMap<>();
        MemoryAsset memoryAsset = MemoryAsset.create("test_Async".getBytes());
        RemoteDataAsset remoteDataAsset = agentI.uploadAsset(memoryAsset);
        metaMap.put("to-hash", remoteDataAsset);


        Operation remoteOperation = agentIKoi.getAsset("e5574e68df2dd9b1ebe277687c7e5cdd0051f4a70d0069647da948e60da47b59");

        // response will have asset id as value which has the result of the operation
        Map<String, Object> response = remoteOperation.invokeResult(metaMap);


        for (Map.Entry<String, Object> res : response.entrySet()) {
            Object r = res.getValue();
            if (r instanceof DID) {
                DID did = (DID) r;
                Asset resultAsset = agentI.getAsset(did.getID());
                String actual = Utils.stringFromStream(resultAsset.getContentStream());

                // below has is calculated based on Hash.sha3_256String("test_Async");
                String expected = "b35af2607950b7c071fd220006f120dbe7af8944c5a91611a633173823574fe9";

                assertTrue(expected.equals(actual));

            }

        }

    }

    /**
     * This Testcase is to test sync the merge operation for two given data asset.
     * One data asset is Vehicle data and other Asset is workshop data.
     * Result Asset will be merged of both data based on some criteria
     *
     * @throws JSONException
     */
    @Test
    public void test_Vehicle_Workshop_Merge_Sync() throws JSONException {

        RemoteAgent agentI = RemoteAgent.connect(resolver, didSurfer, remoteAccount);
        RemoteAgent agentIKoi = RemoteAgent.connect(resolver, didInvoke, remoteAccount);


        // input to the operation
        Map<String, Object> metaMap = new HashMap<>();
        DataAsset workshopData = ResourceAsset.create("assets/vehicle.json");
        DataAsset vehicleData = ResourceAsset.create("assets/workshop.json");

        RemoteDataAsset remoteworkshopData = agentI.uploadAsset(workshopData);
        RemoteDataAsset remotevechileData = agentI.uploadAsset(vehicleData);

        metaMap.put("workshop-dataset", remoteworkshopData);
        metaMap.put("vehicle-dataset", remotevechileData);

        Operation remoteOperation = agentIKoi.getAsset("be2108d9e3221689482fec43c06fbf9b92d455a84548cfe9b3e7a01ea41bd115");

        // response will have asset id as value which has the result of the operation
        Map<String, Object> response = remoteOperation.invokeResult(metaMap);


        for (Map.Entry<String, Object> res : response.entrySet()) {
            Object r = res.getValue();
            if (r instanceof DID) {
                DID did = (DID) r;
                Asset resultAsset = agentI.getAsset(did.getID());
                String actual = Utils.stringFromStream(resultAsset.getContentStream());
                InputStream inputStream = Thread.currentThread().getContextClassLoader().
                        getResourceAsStream("assets/joined_output.json");
                String expected = Utils.stringFromStream(inputStream);
                JSONAssert.assertEquals(expected, actual, false);


            }

        }

    }

    /**
     * This Testcase is to test async the merge operation for two given data asset.
     * One data asset is Vehicle data and other Asset is workshop data.
     * Result Asset will be merged of both data based on some criteria
     *
     * @throws JSONException
     */
    @Test
    public void test_Vehicle_Workshop_Merge_ASync() throws JSONException {

        RemoteAgent agentI = RemoteAgent.connect(resolver, didSurfer, remoteAccount);
        RemoteAgent agentIKoi = RemoteAgent.connect(resolver, didInvoke, remoteAccount);


        // input to the operation
        Map<String, Object> metaMap = new HashMap<>();
        DataAsset workshopData = ResourceAsset.create("assets/vehicle.json");
        DataAsset vehicleData = ResourceAsset.create("assets/workshop.json");

        RemoteDataAsset remoteworkshopData = agentI.uploadAsset(workshopData);
        RemoteDataAsset remotevechileData = agentI.uploadAsset(vehicleData);

        metaMap.put("workshop-dataset", remoteworkshopData);
        metaMap.put("vehicle-dataset", remotevechileData);

        Operation remoteOperation = agentIKoi.getAsset("be2108d9e3221689482fec43c06fbf9b92d455a84548cfe9b3e7a01ea41bd115");

        //        // invoking the prime operation and will get the job associated
        Job job = remoteOperation.invokeAsync(metaMap);

        // waiting for job to get completed
        Map<String, Object> remoteAsset = job.getResult(10000);


        for (Map.Entry<String, Object> res : remoteAsset.entrySet()) {
            Object r = res.getValue();
            if (r instanceof DID) {
                Asset resultAsset = (Asset) r;
                String actual = Utils.stringFromStream(resultAsset.getContentStream());
                InputStream inputStream = Thread.currentThread().getContextClassLoader().
                        getResourceAsStream("assets/joined_output.json");
                String expected = Utils.stringFromStream(inputStream);
                JSONAssert.assertEquals(expected, actual, false);

            }

        }

    }

}
