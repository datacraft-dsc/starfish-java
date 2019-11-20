package sg.dex.starfish.impl.remote;

import org.junit.jupiter.api.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.resource.URIAsset;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SuppressWarnings("javadoc")
public class TestRemoteHttpAsset {

    @Test
    public void testURLConnection() throws URISyntaxException {

        Asset asset = URIAsset.create(new URI("http://httpbin.org/ip"));
        assertEquals(Constant.DATA_SET, asset.getMetadata().get(Constant.TYPE));
        assertNotNull(asset);
        assertNotNull(asset.getContent());
        assertNotNull(asset.getAssetID());
    }

}
