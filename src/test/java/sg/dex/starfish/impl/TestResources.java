package sg.dex.starfish.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.resource.ResourceAsset;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

@SuppressWarnings("javadoc")
public class TestResources {

    @Test
    public void testResourceAssetWithContentHash() {
        DataAsset dataAsset = ResourceAsset.create("assets/hello.txt");
        byte[] bs = dataAsset.getContent();
        String s = new String(bs, StandardCharsets.UTF_8);
        Assertions.assertEquals("Hello Starfish", s);

        Assertions.assertNotNull(dataAsset.getMetadataString());
        Assertions.assertEquals(14, dataAsset.getContentSize());

        assertFalse(dataAsset.getMetadata().containsKey(Constant.CONTENT_HASH));
        dataAsset = dataAsset.includeContentHash();
        Assertions.assertEquals("e4be09f07f5665ecacc078223e86c1dba18b38a3e07a3d575167b5ba7a1821d1", dataAsset.getMetadata().get(Constant.CONTENT_HASH));
        Assertions.assertEquals(dataAsset.validateContentHash(), true);
    }

    @Test
    public void testResourceAssetWithoutContentHash() {
        DataAsset dataAsset = ResourceAsset.create("assets/hello.txt");
        byte[] bs = dataAsset.getContent();
        String s = new String(bs, StandardCharsets.UTF_8);

        // ensure the hash of content is not present in the metadata by default
        assertFalse(dataAsset.getMetadata().containsKey(Constant.CONTENT_HASH));
        assertNull(dataAsset.getMetadata().get(Constant.CONTENT_HASH));

    }
}
