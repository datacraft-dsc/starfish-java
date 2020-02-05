package developerTC;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteDataAsset;
import sg.dex.starfish.util.Utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * As a developer working with Ocean,
 * I need a way to download the data for a data asset that I have purchased
 */
public class TestAssetDownload_19 {

    private RemoteAgent remoteAgent;

    @BeforeEach
    public void setUp() {
        // create remote Agent
        remoteAgent = AgentService.getRemoteAgent();
        assumeTrue(null != remoteAgent);
    }

    @Test
    public void testDownloadAsset() {

        String data = "test upload of asset";
        Asset asset = MemoryAsset.createFromString(data);
        // upload will register and upload the asset
        RemoteDataAsset ra = remoteAgent.uploadAsset(asset);

        // verify asset ID of both
        assertEquals(asset.getAssetID(), ra.getAssetID());


        // check if the asset is registered
        DataAsset remoteAsset = remoteAgent.getAsset(asset.getAssetID());
        assertEquals(asset.getAssetID(), remoteAsset.getAssetID());

        // verify the content of both asset
        String content = Utils.stringFromStream(ra.getContentStream());
        assertEquals(content, data);

    }
}
