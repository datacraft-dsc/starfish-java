package sg.dex.starfish.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sg.dex.crypto.Hash;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.resource.ResourceAsset;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.DigestException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

@SuppressWarnings("javadoc")
public class TestResources {

    @Test
    public void testResourceAssetWithContentHash() throws  IOException {
        DataAsset dataAsset = ResourceAsset.create("assets/hello.txt");
        byte[] bs = dataAsset.getContent();
        String expected =Hash.sha3_256String(bs);

        String s = new String(bs, StandardCharsets.UTF_8);
        Assertions.assertEquals("Hello Starfish", s);

        assertFalse(dataAsset.getMetadata().containsKey(Constant.CONTENT_HASH));
        dataAsset = dataAsset.includeContentHash();

        Assertions.assertEquals(Hash.computeHashWithSHA3(dataAsset.getContentStream()), dataAsset.getMetadata().get(Constant.CONTENT_HASH));
        Assertions.assertEquals(expected, dataAsset.getMetadata().get(Constant.CONTENT_HASH));

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
