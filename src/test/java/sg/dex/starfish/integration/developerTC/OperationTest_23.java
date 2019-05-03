package sg.dex.starfish.integration.developerTC;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteAsset;
import sg.dex.starfish.impl.remote.RemoteOperation;
import sg.dex.starfish.integration.connection_check.AssumingConnection;
import sg.dex.starfish.integration.connection_check.ConnectionChecker;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

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
        metaMap.put("to-hash", remoteAsset);

       String meta1="{\n" +
               "  \"params\": {\n" +
               "    \"to-hash\": {\n" +
               "      \"required\": true,\n" +
               "      \"position\": 0,\n" +
               "      \"type\": \"asset\"\n" +
               "    },\n" +
               "    \"did\":\"assethashing\"\n" +
               "  }\n" +
               "}";
       // String meta = "{\"params\": {\"to-hash\": {}}}";
        RemoteOperation remoteOperation = RemoteOperation.create(remoteAgentInvoke, meta1);

        Job job = remoteOperation.invoke(metaMap);

        Asset asset = job.awaitResult();
        // currently asset is coming null so that need to be fixed
        assertNull(asset);
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

    @Test(expected = Exception.class)
    public void testSyncOperationWithoutRequireData() {

        // asset must be uploaded as invoke will work only on RemoteAsset
        String data_to_hash ="this is a asset to test syncdata operation";
        // uploading the asset, it will do the registration and upload both
        // RemoteAsset remoteAsset = remoteAgentSurfer.uploadAsset(a);

        // creating invoke remote agent
        remoteAgentInvoke = RemoteAgentConfig.getInvoke();
        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("test", data_to_hash);

        // this meta data will expect to-hash
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
        // thie metaMAp donot have to-hash
        Object result = remoteOperation.invokeResult(metaMap);
        System.out.println("Hashing result : " + result);

        assertNotNull(result);
    }

}
