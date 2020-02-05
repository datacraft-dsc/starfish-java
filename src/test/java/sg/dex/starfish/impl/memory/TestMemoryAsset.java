package sg.dex.starfish.impl.memory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Utils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static sg.dex.starfish.constant.Constant.ID;

@SuppressWarnings("javadoc")
public class TestMemoryAsset {

    @Test
    public void testCreation() {
        int SIZE = 10;

        byte[] data = new byte[SIZE];

        MemoryAsset a = MemoryAsset.create(data);
        assertEquals(SIZE, a.getContentSize());
        Map<String, Object> meta = a.getMetadata();
        assertEquals(Integer.toString(SIZE), meta.get("size"));
    }

    @Test
    public void testDID() {
        byte[] data = new byte[]{1, 2, 3};
        Asset a = MemoryAsset.create(data);
        DID did = a.getDID(); // in-memory assets don't have a DID, so this should fail
        assertTrue(did.toString().contains(a.getAssetID()));
        assertEquals(did.getPath(), a.getAssetID());
    }

    /**
     * Test case for null data
     */
    @Test
    public void testNullData() {
        byte[] data = null;


        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            MemoryAsset.create(data);
        });

    }

    /**
     * Test case to build meta data
     */
    @Test
    public void testBuildMetaData() {
        byte[] data = new byte[]{1, 2, 3};
        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("test1", "success");
        Asset a = MemoryAsset.create(data, metaMap);
        assertEquals(a.getMetadata().get("test1"), "success");
    }

    @Test
    public void testCreateFromString() {
        String s = "SomeString";
        Asset a = MemoryAsset.createFromString(s);
        assertEquals(s, Utils.stringFromStream(a.getContentStream()));
        assertEquals("text/plain", a.getMetadata().get(Constant.CONTENT_TYPE));
        assertTrue(a.isDataAsset());
    }

    /**
     *
     */
    @Test
    public void testCreateAsset() {
        MemoryAsset memoryAsset = MemoryAsset.create(new byte[]{1, 2, 3});
        MemoryAsset.create(memoryAsset);
        assertEquals(3, memoryAsset.getContentSize());
        assertArrayEquals(memoryAsset.getContent(), new byte[]{1, 2, 3});
    }

    /**
     * Test Stream Content
     */
    @Test
    public void testGetInputStream() {
        MemoryAsset memoryAsset = MemoryAsset.create("Test Stream".getBytes());
        assertEquals(11, memoryAsset.getContent().length);
        assertEquals("Test Stream", Utils.stringFromStream(memoryAsset.getContentStream()));
    }

    /**
     * Test get Content
     */
    @Test
    public void testgetContent() {
        MemoryAsset memoryAsset = MemoryAsset.create("Test get Content".getBytes());
        assertArrayEquals("Test get Content".getBytes(), memoryAsset.getContent());
    }

    /**
     * Test content Size
     */
    @Test
    public void testgetContentSize() {
        MemoryAsset memoryAsset = MemoryAsset.create("Test Content Size".getBytes());
        assertEquals(17, memoryAsset.getContentSize());
    }

    /**
     * Test content Size
     */
    @Test
    public void testGetParamValue() {
        MemoryAsset memoryAsset = MemoryAsset.create("Test Content Size".getBytes());
        String actual = memoryAsset.getParamValue().get(ID).toString();
        String expected = memoryAsset.getAssetID();
        assertEquals(expected, actual);
    }

    @Test
    public void testRegisterAsset() {
        byte[] data = {3, 4, 5, 6, 7};

        DID did = DID.createRandom();
        MemoryAgent memoryAgent = MemoryAgent.create(did);
        MemoryAsset memoryAsset = MemoryAsset.create(data, memoryAgent);

        DID assetDID = memoryAsset.getDID();
        assertTrue(assetDID.toString().contains(did.toString()));
        assertTrue(assetDID.toString().contains(memoryAsset.getAssetID()));
        assertArrayEquals(data, memoryAsset.getContent());
    }

    @Test
    public void testRegisterAssetWithDefaultAgent() {

        byte[] data = {3, 4, 5, 6, 7};

        MemoryAsset memoryAsset = MemoryAsset.create(data);

        DID assetDID = memoryAsset.getDID();
        assertTrue(assetDID.toString().contains(memoryAsset.getAssetID()));
        assertArrayEquals(data, memoryAsset.getContent());
    }
}
