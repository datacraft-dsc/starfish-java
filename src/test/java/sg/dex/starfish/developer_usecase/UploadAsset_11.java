package sg.dex.starfish.developer_usecase;

import org.junit.Before;
import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteAsset;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * As a developer wishing to make an asset available for download,
 * I need a way to upload the asset data to an appropriate service provider
 */
public class UploadAsset_11 {
    RemoteAsset remoteAsset;
    RemoteAgent remoteAgent;

    @Before
    public void setUp() {
        // create remote Agent
        remoteAgent = RemoteAgentConfig.getRemoteAgent();
    	if (remoteAgent==null) return;

        // create remote Asset
        remoteAsset = RemoteAsset.create(remoteAgent, "Test Asset publish");
        // register Remote asset
        remoteAgent.registerAsset(remoteAsset);


    }

    @Test
    public void testUploadAsset() {
    	if (remoteAgent==null) return;
    	
        Asset a = MemoryAsset.create("Testing to upload of asset");
        RemoteAsset remoteAssetUpload = remoteAgent.uploadAsset(a);
        String actual = RemoteAgentConfig.getDataAsStirngFromInputStream(remoteAssetUpload.getContentStream());

        assertEquals(actual, "Testing to upload of asset");
        assertEquals(a.getAssetID(), remoteAssetUpload.getAssetID());
        assertNotNull(a.getMetadata());

    }

}


