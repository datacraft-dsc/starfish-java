package sg.dex.starfish.impl.file;

import org.junit.Test;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

@SuppressWarnings("javadoc")
public class TestFileAsset {

    @Test
    public void testFileAsset() {
        String name = Utils.createRandomHexString(16);

        File f;
        try {
            f = File.createTempFile(name, ".tmp");
        } catch (IOException e) {
            throw new Error(e);
        }
        f.deleteOnExit();

        FileAsset fa = FileAsset.create(f);
        Map<String, Object> md = fa.getMetadata();
        assertNotNull(fa.getMetadata());
        // validate by dafault hashcontent is not included in metadata
        assertNull(fa.getMetadata().get(Constant.CONTENT_HASH));

        // include hash content
        fa =(FileAsset)fa.includeContentHash();
        // now content hash will be included
        assertNotNull(fa.getMetadata().get(Constant.CONTENT_HASH));

        // verify the content
        assertEquals(fa.validateContentHash(),true);






    }

    @Test
    public void testFileAssetWithContentHAsh() {
        String name = Utils.createRandomHexString(16);

        File f;
        try {
            f = File.createTempFile(name, ".tmp");
        } catch (IOException e) {
            throw new Error(e);
        }
        f.deleteOnExit();

        FileAsset fa = FileAsset.create(f);
        Map<String, Object> md = fa.getMetadata();
        assertNotNull(fa.getMetadata());
        assertNull(fa.getMetadata().get(Constant.CONTENT_HASH));
        FileAsset newFa = (FileAsset) fa.includeContentHash();
        assertNotNull(newFa.getMetadata().get(Constant.CONTENT_HASH));


    }

    @Test
    public void testValidateContentHash() {
        String name = Utils.createRandomHexString(16);

        File f;
        try {
            f = File.createTempFile(name, ".tmp");
        } catch (IOException e) {
            throw new Error(e);
        }
        f.deleteOnExit();

        FileAsset fa = FileAsset.create(f);
        Map<String, Object> md = fa.getMetadata();

        assertNotNull(fa.getMetadata());
        FileAsset newFa = (FileAsset) fa.includeContentHash();
        assertEquals(newFa.validateContentHash(),true);

    }
}
