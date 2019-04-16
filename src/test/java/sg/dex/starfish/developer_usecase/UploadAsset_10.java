package sg.dex.starfish.developer_usecase;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sg.dex.starfish.Asset;
import sg.dex.starfish.connection_check.AssumingConnection;
import sg.dex.starfish.connection_check.ConnectionChecker;
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
    @ClassRule
    public static AssumingConnection assumingConnection =
            new AssumingConnection(new ConnectionChecker(RemoteAgentConfig.getSurferUrl()));

    private RemoteAsset remoteAsset;
    private  RemoteAgent remoteAgent;

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

}
