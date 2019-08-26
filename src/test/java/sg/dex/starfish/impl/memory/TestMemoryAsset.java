package sg.dex.starfish.impl.memory;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import sg.dex.starfish.Asset;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Utils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static sg.dex.starfish.constant.Constant.ID;

@SuppressWarnings("javadoc")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
        try {
            DID did = a.getAssetDID(); // in-memory assets don't have a DID, so this should fail
            fail("Should not succeed! Got: " + did);
        } catch (UnsupportedOperationException ex) {
            /* OK */
        }
    }

    /**
     * TEst case for null Data
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullData() {
        byte[] data = null;
        MemoryAsset.create(data);

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

    /**
     *
     */
    @Test
    public void testCreateAsset() {
        MemoryAsset memoryAsset =MemoryAsset.create(new byte[]{1, 2, 3});
        MemoryAsset.create(memoryAsset);
        assertEquals(memoryAsset.getContentSize(),new byte[]{1, 2, 3}.length);
        assertArrayEquals(memoryAsset.getContent(), new byte[]{1, 2, 3});
    }

    /**
     * Test Stream Content
     */
    @Test
    public void testGetInputStream() {
        MemoryAsset memoryAsset = MemoryAsset.create("Test Stream".getBytes());
        assertEquals(memoryAsset.getContent().length,"Test Stream".length());
        String expected = Utils.stringFromStream(memoryAsset.getContentStream());
        assertEquals(expected, "Test Stream");
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
        assertEquals("Test Content Size".getBytes().length,memoryAsset.getContentSize());
    }

    /**
     * Test content Size
     */
    @Test
    public void testgetParamValue() {

        MemoryAsset memoryAsset = MemoryAsset.create("Test Content Size".getBytes());
        String expected =memoryAsset.getParamValue().get(ID).toString();
        String actual =memoryAsset.getAssetID();
        assertEquals(expected,actual);

    }
}
