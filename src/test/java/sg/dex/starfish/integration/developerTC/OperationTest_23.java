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

    /**
     * For this operation the input must be type JSON and the response will also be type asset
     */
    @Test
    public void testPrimeSync_1() {

        // input to the operation
        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("first-n", "11");

        // getting the prime operation based on asset id
        Operation remoteOperation = RemoteOperation.materialize(remoteAgentSurfer, "8d658b5b09ade5526aecf669e4291c07d88e9791420c09c51d2f922f721858d1", remoteAgentInvoke);

        // response will have asset id as value which has the result of the operation
        Map<String, Object> response = remoteOperation.invokeResult(metaMap);

        //getting the Asset which has the result
        RemoteAsset resultAsset = (RemoteAsset) remoteAgentSurfer.getAsset(response.get("did").toString());

        assertNotNull(resultAsset);


    }

    /**
     * For this operation the input must be type JSON and the response will also be type asset
     */
    @Test
    public void testPrimeAsync_1() {

        // input to the operation
        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("first-n", "20");

        // getting the prime operation based on asset id
        Operation remoteOperation = RemoteOperation.materialize(remoteAgentSurfer, "8d658b5b09ade5526aecf669e4291c07d88e9791420c09c51d2f922f721858d1", remoteAgentInvoke);

        // invoking the prime operation and will get the job associated
        Job job = remoteOperation.invokeAsync(metaMap);

        // waiting for job to get completed
        Asset remoteAsset = (Asset) job.awaitResult(10000);

        // getting the metadata of the new Asset which has the operation result
        Map<String, Object> metaData = remoteAsset.getMetadata();
        String did = metaData.get("did").toString();

        //getting the Asset which has result
        RemoteAsset resultAsset = (RemoteAsset) remoteAgentSurfer.getAsset(did);

        assertNotNull(resultAsset);

    }

    /**
     * For this operation the input must be type asset and the response will also be type asset
     */
    @Test
    public void testHashAsync_1() {

        // asset must be uploaded as invoke will work only on RemoteAsset
        Asset a = MemoryAsset.create("this is a asset to test Async data operation");
        // uploading the asset, it will do the registration and upload both
        RemoteAsset remoteAsset1 = (RemoteAsset) remoteAgentSurfer.uploadAsset(a);

        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("to-hash", remoteAsset1);

        // getting the prime operation based on assed id
        Operation remoteOperation = RemoteOperation.materialize(remoteAgentSurfer, "3099ae4f493d72777e4b57db43226456d67867728c0695d1eaf51f3035b20e07", remoteAgentInvoke);

        System.out.println(remoteOperation.getAssetID());

        // invoking the prime operation and will get the job associated
        Job job = remoteOperation.invokeAsync(metaMap);

        // waiting for job to get completed
        Asset remoteAsset = (Asset) job.awaitResult(10000);

        // getting the metadata of the new Asset which has the operation result
        Map<String, Object> metaData = remoteAsset.getMetadata();
        String did = metaData.get("did").toString();

        //getting the Asset which has result
        RemoteAsset resultAsset = (RemoteAsset) remoteAgentSurfer.getAsset(did);
        assertNotNull(resultAsset);

    }

    /**
     * For this operation the input must be type asset and the response will also be type asset
     */
    @Test
    public void testHashSync_1() {

        // asset must be uploaded as invoke will work only on RemoteAsset
        Asset a = MemoryAsset.create("this is a asset to test Async data operation");
        // uploading the asset, it will do the registration and upload both
        RemoteAsset remoteAsset1 = (RemoteAsset) remoteAgentSurfer.uploadAsset(a);

        // input to the operation
        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("to-hash", remoteAsset1);
        // getting the prime operation based on assed id
        Operation remoteOperation = RemoteOperation.materialize(remoteAgentSurfer, "3099ae4f493d72777e4b57db43226456d67867728c0695d1eaf51f3035b20e07", remoteAgentInvoke);

        // response will have asset id as value which has the result of the operation
        Map<String, Object> response = remoteOperation.invokeResult(metaMap);

        //getting the Asset which has the result
        RemoteAsset resultAsset = (RemoteAsset) remoteAgentSurfer.getAsset(response.get("did").toString());

        assertNotNull(resultAsset);

    }

//registering primes, hashing, asset ids as  [:8d658b5b09ade5526aecf669e4291c07d88e9791420c09c51d2f922f721858d1
// :3099ae4f493d72777e4b57db43226456d67867728c0695d1eaf51f3035b20e07
// :8ade9c7505bcadaab8dacf6848e88ddb4aa6a295612eb01759e35aeb65daeac2]
}
