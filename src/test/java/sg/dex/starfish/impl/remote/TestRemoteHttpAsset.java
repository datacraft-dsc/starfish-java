package sg.dex.starfish.impl.remote;

import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.url.RemoteHttpAsset;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static sg.dex.starfish.constant.Constant.DATA_SET;
import static sg.dex.starfish.constant.Constant.TYPE;

public class TestRemoteHttpAsset {

    @Test
    public void testURLConnection(){
        RemoteAgent remoteAgent = RemoteAgentConfig.getRemoteAgent();

        Map<String,Object> metaMap = new HashMap<>();
        metaMap.put(TYPE,DATA_SET);

        Asset asset =RemoteHttpAsset.create(metaMap.toString(),remoteAgent,"http://httpbin.org/ip");

        assertNotNull(asset);
        assertNotNull(asset.getContent());
        assertNotNull(asset.getAssetID());
    }

}
