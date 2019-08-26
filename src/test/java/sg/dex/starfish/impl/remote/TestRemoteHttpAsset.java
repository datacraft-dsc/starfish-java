package sg.dex.starfish.impl.remote;

import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.url.RemoteHttpAsset;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("javadoc")
public class TestRemoteHttpAsset {

    @Test
    public void testURLConnection(){

        Asset asset =RemoteHttpAsset.create("http://httpbin.org/ip");
        assertEquals(Constant.DATA_SET,asset.getMetadata().get(Constant.TYPE));

    }

}
