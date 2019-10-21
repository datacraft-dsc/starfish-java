package developerTC;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteDataAsset;

import static junit.framework.TestCase.assertEquals;

/**
 * As a developer working with Ocean,
 * I need a way to download the data for a data asset that I have purchased
 */
public class AssetDownload_19 {

    private RemoteAgent remoteAgent;

    @Before
    public void setUp() {
        // create remote Agent
        remoteAgent = AgentService.getRemoteAgent();
        Assume.assumeNotNull(remoteAgent);
        // create remote Asset
    }

    @Test
    public void testDownloadAsset() {

        Asset asset = MemoryAsset.createFromString("test upload of asset");
        // upload will register and upload the asset
        RemoteDataAsset ra = remoteAgent.uploadAsset(asset);

        //ra.getContentStream()
        assertEquals(asset.getAssetID(), ra.getAssetID());

    }
}
