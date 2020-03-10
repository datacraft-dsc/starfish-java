package sg.dex.starfish.impl.remote;

import org.apache.http.auth.AUTH;
import org.junit.jupiter.api.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.resource.URIAsset;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

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
    @Test
    public void testURLConnection_1() throws URISyntaxException {

        URIAsset uriAsset = URIAsset.create(new URI("http://httpbin.org/ip"));

        String contentType =uriAsset.getMetadata().get(Constant.CONTENT_TYPE).toString();
        String contentLength =uriAsset.getMetadata().get(Constant.CONTENT_LENGTH).toString();
        assertEquals(contentType,"application/json");
        assertEquals(contentLength,"30");

    }
    @Test
    public void testURLConnection_Auth() throws URISyntaxException {

        String headerKey = AUTH.WWW_AUTH_RESP;
        String token="token 99ba561a377b909a7f3feb573c9f27288e516e8920912b54fa553d14f32c6856";
        Map<String, String> header = new HashMap<>();
        header.put(headerKey,token);

        URIAsset uriAsset = URIAsset.create(new URI("http://localhost:3030/api-docs/index.html"),header,null);

        String contentType =uriAsset.getMetadata().get(Constant.CONTENT_TYPE).toString();
        String contentLength =uriAsset.getMetadata().get(Constant.CONTENT_LENGTH).toString();
        assertEquals(contentType,"text/html");
        assertEquals(contentLength,"1496");

    }


}
