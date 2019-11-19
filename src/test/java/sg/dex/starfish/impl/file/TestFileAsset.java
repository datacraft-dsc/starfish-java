package sg.dex.starfish.impl.file;

import org.junit.jupiter.api.Test;
import sg.dex.crypto.Hash;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

@SuppressWarnings("javadoc")
public class TestFileAsset {

    @Test
    public void testFileAssetWithNewFile() {

        // file creation
        String name = Utils.createRandomHexString(16);

        File f;
        try {
            f = File.createTempFile(name, ".tmp");
        } catch (IOException e) {
            throw new Error(e);
        }

        f.deleteOnExit();

        //File asset creation
        FileAsset fa = FileAsset.create(f);

        Map<String, Object> md = fa.getMetadata();
        // hash content is optional so  default hash content is not included in metadata
        assertNull(md.get(Constant.CONTENT_HASH));

        // verify the default metadata
        assertNotNull(md.get(Constant.DATE_CREATED));
        assertEquals(Constant.DATA_SET, md.get(Constant.TYPE));
        assertNotNull(md.get(Constant.CONTENT_TYPE));

        // include hash content in metadata explicitly
        fa = (FileAsset) fa.includeContentHash();

        // now content hash will be included, should be hash of empty byte array since file is empty
        assertTrue(Arrays.equals(Hash.EMPTY_BYTES_SHA3, Hex.toBytes((String) fa.getMetadata().get(Constant.CONTENT_HASH))));

        // validate the hash content
        assertEquals(fa.validateContentHash(), true);

    }

    @Test
    public void testFileAssetWithExistingFile() throws IOException {
        String asset_metaData = new String(Files.readAllBytes(Paths.get("src/test/resources/assets/test_metadata.json")));

        Path path = Paths.get("src/test/resources/assets/test_content.json");
        // create asset using metadata and given content
        FileAsset fa = FileAsset.create(path.toFile(), JSON.toMap(asset_metaData));

        Map<String, Object> md = fa.getMetadata();
        // hash content is optional so  default hash content is not included in metadata
        assertNull(md.get(Constant.CONTENT_HASH));

        // verify the default metadata
        assertNotNull(md.get(Constant.DATE_CREATED));
        assertEquals(Constant.DATA_SET, md.get(Constant.TYPE));
        assertNotNull(md.get(Constant.CONTENT_TYPE));

        // include hash content in metadata explicitly
        fa = (FileAsset) fa.includeContentHash();

        // now content hash will be included
        assertNotNull(fa.getMetadata().get(Constant.CONTENT_HASH));

        // validate the hash content
        assertEquals(fa.validateContentHash(), true);
    }
//
//    @Test(expected = StorageException.class)
//    public void testMissingFileAsset() {
//        FileAsset fa = FileAsset.create(new File("NoFile"));
//        fa.includeContentHash(); // should fail since unable to read asset content
//    }

    public void testFileAssetWithNoFile() {
        FileAsset fa = FileAsset.create(new File("NoFile"));
        assertNotNull(fa.getAssetID());
    }
}
