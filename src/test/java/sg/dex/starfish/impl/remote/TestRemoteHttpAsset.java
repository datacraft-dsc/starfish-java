package sg.dex.starfish.impl.remote;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.url.RemoteHttpAsset;

public class TestRemoteHttpAsset {

    @Test
    public void testURLConnection(){

        Asset asset =RemoteHttpAsset.create("http://httpbin.org/ip");

        assertNotNull(asset);
        assertNotNull(asset.getContent());
        assertNotNull(asset.getAssetID());
    }

}
