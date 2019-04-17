package sg.dex.starfish.developer_usecase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteAsset;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * As a developer working with Ocean, I need a way to register a new asset with Ocean
 */
@RunWith(JUnit4.class)
public class AssetRegistration_08 {

    private RemoteAgent remoteAgent;


    @Before
    public void setup() {
        remoteAgent = RemoteAgentConfig.getRemoteAgent();

    }

    @Test
    public void testRegister() {
        String data = "Simple memory Asset";
        Asset asset = MemoryAsset.create(data);

        RemoteAsset remoteAsset = remoteAgent.registerAsset(asset);

        assertEquals(asset.getAssetID(), remoteAsset.getAssetID());

        // get registered Asset by ID
        assertEquals(remoteAsset.isDataAsset(), remoteAsset.isDataAsset());
        assertEquals(remoteAsset.getMetadataString(), remoteAsset.getMetadataString());
    }

    @Test
    public void testRegisterWithTwoAssetSameContent() {
        String data = "Simple Test two  Asset with same content";

        RemoteAsset remoteAsset = remoteAgent.registerAsset(MemoryAsset.create(data));
        RemoteAsset remoteAsset1 = remoteAgent.registerAsset(MemoryAsset.create(data));

        assertNotEquals(remoteAsset1.getAssetID(), remoteAsset.getAssetID());


    }

}
