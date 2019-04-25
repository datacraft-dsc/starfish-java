package sg.dex.starfish.integration.developerTC;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteAsset;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

/**
 * As a developer working with Ocean,
 * I need a stable identifier (Asset ID) for an arbitrary asset in the Ocean ecosystem
 */
@RunWith(JUnit4.class)

public class AssetIdentity_02 {

    private RemoteAgent remoteAgent;

    @Before
    public void setup() {
        // Initialize Remote Agent
        remoteAgent = RemoteAgentConfig.getRemoteAgent();
    }

    @Test
    public void testByteContent() {

        // create a memory asset
        byte[] data = new byte[]{1, 2, 3};
        Asset asset1 = MemoryAsset.create(data);

        //2. registration : it will just reg the asset and upload its metadata content  and will return a Remote Agent
        RemoteAsset remoteAsset = remoteAgent.registerAsset(asset1);

        // register to the remote agent
        // get the Remote asset ID which has been register using remote Agent

        String assetID = remoteAsset.getAssetID();
        // compare both the assetID, It must be equal
        assertEquals(assetID, asset1.getAssetID());
    }


    // try to register again with same content and check the Hash
    @Test
    public void createAssetWithMetaData() {

        byte[] data = new byte[]{1, 2, 3};
        // update the metadata
        Map<String, Object> metaDataAsset = new HashMap<>();
        metaDataAsset.put("id", "123");
        metaDataAsset.put("name", "Fig");
        metaDataAsset.put("location", "Singapore");

        // creating asset with MetaData
        Asset asset2 = MemoryAsset.create( data,metaDataAsset);


        //2. registration : it will just reg the asset and upload its metadata content  and will return a Remote Agent
        RemoteAsset remoteAsset = remoteAgent.registerAsset(asset2);

        // uploading the Asset this remote Agent
        remoteAgent.uploadAsset(asset2);

        // get the Remote asset ID which has been register using remote Agent
        String assetID = remoteAsset.getAssetID();

        //Getting the content of the Asset
        byte[] result = remoteAsset.getContent();

        // compare both the assetID, It must be equal
        assertEquals(assetID, asset2.getAssetID());

        // verifying the Content is same or not
        assertEquals(result.length, data.length);


        // verify the Asset metaDAta must be equal to Registered asset MetaData
        assertEquals(remoteAsset.getMetadata().get("id").toString(), "123");
        assertEquals(remoteAsset.getMetadata().get("name").toString(), "Fig");
        assertEquals(remoteAsset.getMetadata().get("location").toString(), "Singapore");

    }

    @Test
    public void testWithStringContent() {

        // create Asset using String data
        Asset asset3 = MemoryAsset.create("Testing using String");

        //Registering the Asset
        RemoteAsset remoteAsset3 = remoteAgent.registerAsset(asset3);
        // uploading the Asset, it will upload the content of an asset
        remoteAgent.uploadAsset(asset3);

        assertEquals(remoteAsset3.getAssetID(), asset3.getAssetID());
        // verify the content
        assertEquals(RemoteAgentConfig.getDataAsStirngFromInputStream(remoteAsset3.getContentStream()),
                "Testing using String");
    }

    @Test
    public void testSameAssetContent() {
        if (remoteAgent == null) return;

        Asset asset3 = MemoryAsset.create("Testing using String");
        RemoteAsset remoteAsset3 = remoteAgent.registerAsset(asset3);

        Asset asset4 = MemoryAsset.create("Testing using String");
        RemoteAsset remoteAsset4 = remoteAgent.registerAsset(asset4);

        // uploading both the Asset
        remoteAgent.uploadAsset(asset3);
        remoteAgent.uploadAsset(asset4);

        assertEquals(RemoteAgentConfig.getDataAsStirngFromInputStream(remoteAsset3.getContentStream()),
                RemoteAgentConfig.getDataAsStirngFromInputStream(remoteAsset4.getContentStream()));
    }

    @Test(expected = NullPointerException.class)
    public void testForNullAsset(){
        // Null check should be there ?
        remoteAgent.registerAsset(null);
    }

}