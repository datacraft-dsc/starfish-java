package sg.dex.starfish.demo;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
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

public class Prime_Test {

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
    public void testPrimeSync_1() {

        // input to the operation
        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("first-n", "10000");

        // getting the calculate primes operation  asset based on given asset id
        Operation remoteOperation = RemoteOperation.materialize(
                remoteAgentSurfer, "8d658b5b09ade5526aecf669e4291c07d88e9791420c09c51d2f922f721858d1",
                remoteAgentInvoke);

     System.out.println(JSON.toPrettyString(remoteOperation.getMetadata()));
        // response will have asset id as value which has the result of the operation
        Map<String,Object> results =remoteOperation.invokeResult(metaMap);

        //getting the Asset which has the result
        RemoteAsset resultAsset=(RemoteAsset)remoteAgentSurfer.getAsset(results.get("did").toString());

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
        metaMap.put("first-n", "2000");

        // getting the prime operation based on assed id
        Operation remoteOperation = RemoteOperation.materialize(remoteAgentSurfer,
                "8d658b5b09ade5526aecf669e4291c07d88e9791420c09c51d2f922f721858d1",remoteAgentInvoke);

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

}
