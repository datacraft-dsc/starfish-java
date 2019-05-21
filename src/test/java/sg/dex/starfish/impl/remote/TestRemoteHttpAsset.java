package sg.dex.starfish.impl.remote;

import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.url.RemoteHttpAsset;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;

import static org.junit.Assert.assertNotNull;

public class TestRemoteHttpAsset {

    @Test
    public void testURLConnection(){
        RemoteAgent remoteAgent = RemoteAgentConfig.getRemoteAgent();

        Asset asset =RemoteHttpAsset.create("url-test",remoteAgent,"http://httpbin.org/ip");

        assertNotNull(asset);
        assertNotNull(asset.getContent());
        assertNotNull(asset.getAssetID());
    }

}
