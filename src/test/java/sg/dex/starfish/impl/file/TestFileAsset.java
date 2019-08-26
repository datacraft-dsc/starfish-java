package sg.dex.starfish.impl.file;

import org.junit.Test;
import sg.dex.crypto.Hash;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.StarfishValidationException;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.*;
import static sg.dex.starfish.constant.Constant.OCTET_STREAM;

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
        assertNull(fa.getMetadata().get(Constant.CONTENT_HASH));

        // verify the default metadata
        assertEquals(fa.getMetadata().get(Constant.CONTENT_TYPE),OCTET_STREAM);


        // include hash content in metadata explicitly
        fa = (FileAsset) fa.includeContentHash();

        // now content hash will be included
        assertEquals(fa.getMetadata().get(Constant.CONTENT_HASH), Hex.toString(Hash.sha3_256(fa.getContent())));

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
        assertNull(fa.getMetadata().get(Constant.CONTENT_HASH));

        // verify the default metadata
        assertEquals(fa.getMetadata().get(Constant.CONTENT_TYPE),OCTET_STREAM);

        // verify additional metadata
        assertEquals(fa.getMetadata().get("copyrightHolder"),"Vehicle Owner ");


        // include hash content in metadata explicitly
        fa = (FileAsset) fa.includeContentHash();

        // now content hash will be included
        assertEquals(fa.getMetadata().get(Constant.CONTENT_HASH), Hex.toString(Hash.sha3_256(fa.getContent())));

        // validate the hash content
        assertEquals(fa.validateContentHash(), true);


    }

    @Test(expected = StarfishValidationException.class)
    public void testFileAssetWithN0File() {

        FileAsset fa = FileAsset.create(new File("NoFile"));
        Map<String, Object> md = fa.getMetadata();


    }
}
