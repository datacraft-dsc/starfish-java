package sg.dex.starfish.developer_usecase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.RemoteAsset;

import java.nio.charset.StandardCharsets;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * As a developer working with an Ocean marketplace,
 * I need a way to upload my asset with a service agreement
 */
@RunWith(JUnit4.class)
public class UploadAsset_10 {

    RemoteAsset remoteAsset;
    RemoteAgent remoteAgent;

    @Before
    public void setUp() {
        // create remote Agent
        remoteAgent = RemoteAgentConfig.getRemoteAgent();
        // create remote Asset
        remoteAsset = RemoteAsset.create(remoteAgent, "Test Asset publish");

    }

    @Test
    public void testUploadDownloadAsset() {

        Asset asset = MemoryAsset.create("test upload of asset");
        RemoteAsset ra = remoteAgent.uploadAsset(asset);
        String downloadData = RemoteAgentConfig.getDataAsStirngFromInputStream(ra.getContentStream());

        assertEquals(asset.getAssetID(), ra.getAssetID());
        assertEquals(downloadData, "test upload of asset");

    }
    @Test
    public void testDownloadAssetWithoutUpload() {

        String data ="test upload of asset";
        Asset asset = MemoryAsset.create(data);
        byte[] bytes=data.getBytes(StandardCharsets.UTF_8);
        assertEquals(bytes.length,asset.getContent().length);
        assertNotNull(data);

    }

}
