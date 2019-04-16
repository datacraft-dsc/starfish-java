package sg.dex.starfish.developer_usecase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteAsset;

import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static sg.dex.starfish.constant.Constant.SIZE;

/**
 * As a developer working with Ocean, I need a way to register a new asset with Ocean
 */
@RunWith(JUnit4.class)
public class AssetRegistration_08 {

    RemoteAgent remoteAgent;
    Asset asset;

    @Before
    public void setup() {
        remoteAgent = RemoteAgentConfig.getRemoteAgent();
        String data = "Simple memory Asset";
        asset = MemoryAsset.create(data);
    }

    @Test
    public void testRegister() {
        if (remoteAgent==null) return;
    	
        RemoteAsset remoteAsset = remoteAgent.registerAsset(asset);
        assertEquals(asset.getAssetID(), remoteAsset.getAssetID());
        // get registered Asset by ID
        RemoteAsset remoteAsset1 = remoteAgent.getAsset(asset.getAssetID());
        assertEquals(remoteAsset.getAssetID(), remoteAsset1.getAssetID());
        assertEquals(remoteAsset.isDataAsset(), remoteAsset1.isDataAsset());
        assertEquals(remoteAsset.getMetadataString(), remoteAsset1.getMetadataString());
        System.out.println(remoteAsset.getAssetDID());
        System.out.println(remoteAsset.getAssetID());
    }

    @Test
    public void testToModifyMetadata() {    	
        if (remoteAgent==null) return;

        RemoteAsset remoteAsset = remoteAgent.registerAsset(asset);

        Map<String, Object> metaData = asset.getMetadata();
        String size = asset.getMetadata().get(SIZE).toString();


        // trying to update the data
        metaData.put(SIZE, 100000000);
        String size1 = asset.getMetadata().get(SIZE).toString();
        assertEquals(size, size1);

    }

}
