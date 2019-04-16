package sg.dex.starfish.developer_usecase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteAsset;
import sg.dex.starfish.util.DID;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static sg.dex.starfish.constant.Constant.CONTENT_HASH;

/**
 * As a developer working with Ocean,
 * I need a stable identifier (Asset ID) for an arbitrary asset in the Ocean ecosystem
 */
@RunWith(JUnit4.class)
public class AssetIdentity_02 {
    RemoteAgent remoteAgent;

    @Before
    public void setup() {
        // Initialize Remote Agent
        remoteAgent = RemoteAgentConfig.getRemoteAgent();
    }

    @Test
    public void testByteContent() {
        if (remoteAgent==null) return;

        // create a memory asset
        byte[] data = new byte[]{1, 2, 3};
        Asset asset1 = MemoryAsset.create(data);

        //2. registration : it will just reg the asset and upload its metadata content  and will return a Remote Agent
        RemoteAsset remoteAsset = remoteAgent.registerAsset(asset1);
        // register to the remote agent
        // get the asset ID
        DID did = remoteAsset.getAssetDID();

        String assetID = remoteAsset.getAssetID();
        assertEquals(assetID, asset1.getAssetID());
    }


    // try to register again with same content and check the Hash
    @Test
    public void updateMetaData() {
        if (remoteAgent==null) return;
        
        byte[] data = new byte[]{1, 2, 3};
        // update the metadata
        Map<String, Object> metaDataAsset = new HashMap<>();
        metaDataAsset.put("test", "test");
        Asset asset2 = MemoryAsset.create(metaDataAsset, data);
        assertEquals(asset2.getMetadata().get("test").toString(), "test");

    }

    @Test
    public void testWithStringContent() {
        if (remoteAgent==null) return;
    	
        // create Asset using String data
        Asset asset3 = MemoryAsset.create("Testing using String");
        RemoteAsset remoteAsset3 = remoteAgent.registerAsset(asset3);
        assertEquals(remoteAsset3.getAssetID(), asset3.getAssetID());
    }

    @Test
    public void testSameAssetContent() {
        if (remoteAgent==null) return;

        Asset asset3 = MemoryAsset.create("Testing using String");
        RemoteAsset remoteAsset3 = remoteAgent.registerAsset(asset3);

        Asset asset4 = MemoryAsset.create("Testing using String");
        RemoteAsset remoteAsset4 = remoteAgent.registerAsset(asset4);
        //  assertEquals(remoteAsset4.getAssetID(),asset4.getAssetID());
        assertEquals(remoteAsset4.getMetadata().get(CONTENT_HASH), remoteAsset3.getMetadata().get(CONTENT_HASH));
    }

    @After
    public void clear() {
        remoteAgent = null;
    }
}
