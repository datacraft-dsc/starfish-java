package sg.dex.starfish.demo;

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
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;

public class Hashing_test {

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
    public void testHashAsync_1() {

        // asset must be uploaded as invoke will work only on RemoteAsset
        Asset a = MemoryAsset.create("this is a asset to test Async data operation");
        // uploading the asset, it will do the registration and upload both
        RemoteAsset remoteAsset1 = (RemoteAsset)remoteAgentSurfer.uploadAsset(a);

        System.out.println(JSON.toPrettyString(a.getMetadata()));


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
        String hashResult=Utils.stringFromStream(resultAsset.getContentStream());

        // printing the result
        System.out.println("Reasult:"+hashResult);
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
