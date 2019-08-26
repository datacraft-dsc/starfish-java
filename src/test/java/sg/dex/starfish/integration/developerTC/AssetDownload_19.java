package sg.dex.starfish.integration.developerTC;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteAsset;

import static org.junit.Assert.assertEquals;

/**
 * As a developer working with Ocean,
 * I need a way to download the data for a data asset that I have purchased
 */
public class AssetDownload_19 {

    private RemoteAgent remoteAgent;

    @Before
    public void setUp() {
        // create remote Agent
        remoteAgent = RemoteAgentConfig.getRemoteAgent();
        Assume.assumeNotNull(remoteAgent);
        // create remote Asset
    }

    @Test
    public void testDownloadAsset() {

        Asset asset = MemoryAsset.create("test upload of asset");
        // upload will register and upload the asset
        RemoteAsset ra = (RemoteAsset)remoteAgent.uploadAsset(asset);

        //ra.getContentStream()
        assertEquals(asset.getAssetID(), ra.getAssetID());

    }
}
