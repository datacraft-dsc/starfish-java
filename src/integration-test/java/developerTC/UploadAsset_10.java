package developerTC;

import org.junit.Before;
import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteDataAsset;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * As a developer working with an Ocean marketplace,
 * I need a way to upload my asset with a service agreement
 */
public class UploadAsset_10 {


    private RemoteAgent remoteAgent;

    @Before
    public void setUp() {
        // create remote Agent
        remoteAgent = AgentService.getRemoteAgent();

    }

    @Test
    public void testUploadDownloadAsset() {

        Asset asset = MemoryAsset.createFromString("test upload of asset");

        RemoteDataAsset ra = remoteAgent.uploadAsset(asset);

        assertEquals(asset.getAssetID(), ra.getAssetID());
        assertArrayEquals(asset.getContent(), ra.getContent());


    }

}