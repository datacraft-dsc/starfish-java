package sg.dex.starfish.integration.developerTC;

import org.junit.Before;
import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteAsset;

import static junit.framework.TestCase.assertEquals;

/**
 * As a developer working with Ocean,
 * I need a way to download the data for a data asset that I have purchased
 */
public class AssetDownload_19 {

    private RemoteAsset remoteAsset;
    private RemoteAgent remoteAgent;

    @Before
    public void setUp() {
        // create remote Agent
        remoteAgent = RemoteAgentConfig.getRemoteAgent();
        // create remote Asset
        remoteAsset = RemoteAsset.create(remoteAgent, "Test Asset publish");


    }

    @Test
    public void testDownloadAsset() {

        Asset asset = MemoryAsset.create("test upload of asset");
        RemoteAsset ra = remoteAgent.uploadAsset(asset);

        //ra.getContentStream()
        assertEquals(asset.getAssetID(), ra.getAssetID());

    }
}
