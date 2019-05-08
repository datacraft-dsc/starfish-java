package sg.dex.starfish.integration.developerTC;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteAsset;

import static junit.framework.TestCase.assertEquals;

/**
 * As a developer working with an Ocean marketplace,
 * I need a way to upload my asset with a service agreement
 */
@RunWith(JUnit4.class)
public class UploadAsset_10 {


    private  RemoteAgent remoteAgent;

    @Before
    public void setUp() {
        // create remote Agent
        remoteAgent = RemoteAgentConfig.getRemoteAgent();

    }

    @Test
    public void testUploadDownloadAsset() {


        Asset asset = MemoryAsset.create("test upload of asset");

        // upload will do the registration and upload the content
        RemoteAsset ra = (RemoteAsset)remoteAgent.uploadAsset(asset);
        // getting the content form Remote Agent (Downloading the content)

        String downloadData = RemoteAgentConfig.getDataAsStringFromInputStream(ra.getContentStream());


        assertEquals(asset.getAssetID(), ra.getAssetID());
        // comparing both the content
        assertEquals(downloadData, "test upload of asset");

    }

}
