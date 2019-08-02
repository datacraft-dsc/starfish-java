package sg.dex.starfish.impl.file;

import org.junit.Test;
import sg.dex.crypto.ComputeHashFactory;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@SuppressWarnings("javadoc")
public class TestFileAsset {

    @Test
    public void testTempFile() {
        String name = Utils.createRandomHexString(16);

        File f;
        try {
            f = File.createTempFile(name, ".tmp");
        } catch (IOException e) {
            throw new Error(e);
        }

        FileAsset fa = FileAsset.create(f);
        f.deleteOnExit();


    }

    @Test
    public void testFileAssetWithMetadata() throws IOException {

        // read metadata
        String asset_metaData = new String(Files.readAllBytes(Paths.get("src/test/resources/assets/SJR8961K_metadata.json")));

        Path path = Paths.get("src/test/resources/assets/SJR8961K_content.json");

        FileAsset fileAsset = FileAsset.create(path.toFile(), asset_metaData);
        assertNotNull(fileAsset.getMetadata());
    }

    @Test
    public void testFileAsset() {

        Path path = Paths.get("src/test/resources/assets/SJR8961K_content.json");
        // create asset using metadata and given content
        FileAsset fileAsset = FileAsset.create(path.toFile());
        assertNotNull(fileAsset.getMetadata());
    }

    @Test
    public void testHashForFileAsset() throws IOException {

        // read metadata
        String asset_metaData = new String(Files.readAllBytes(Paths.get("src/test/resources/assets/SJR8961K_metadata.json")));

        Path path = Paths.get("src/test/resources/assets/SJR8961K_content.json");
        // create asset using metadata and given content

        FileAsset fileAsset = FileAsset.createWithContenthash(path.toFile(), asset_metaData, null);

        String content = Utils.stringFromStream(fileAsset.getContentStream());
        String expected = Hex.toString(new ComputeHashFactory().getHashfunction(Constant.Keccak256).compute(content));

        String actual = fileAsset.getMetadata().get(Constant.CONTENT_HASH).toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testHashForFileAssetWithDefaultMetadata() {

        // read metadata

        Path path = Paths.get("src/test/resources/assets/SJR8961K_content.json");
        // create asset using metadata and given content
        FileAsset fileAsset = FileAsset.createWithContenthash(path.toFile(), null);
        String content = Utils.stringFromStream(fileAsset.getContentStream());
        String expected = Hex.toString(new ComputeHashFactory().getHashfunction(Constant.Keccak256).compute(content));
        String actual = fileAsset.getMetadata().get(Constant.CONTENT_HASH).toString();

        assertEquals(expected, actual);
    }


}
