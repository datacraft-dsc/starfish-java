package sg.dex.starfish.impl.memory;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Utils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static sg.dex.starfish.constant.Constant.ID;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMemoryAsset {

    MemoryAsset memoryAsset;

    @Before
    public void setup() {

        memoryAsset = MemoryAsset.create(new byte[]{1, 2, 3});

    }

    @Test
    public void testCreation() {
        int SIZE = 10;

        byte[] data = new byte[SIZE];

        MemoryAsset a = MemoryAsset.create(data);
        assertEquals(SIZE, a.getContentSize());
        Map<String, Object> meta = a.getMetadata();
        assertEquals(Integer.toString(SIZE), meta.get("size"));
        assertEquals(Hash.keccak256String(data), meta.get("contentHash"));
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
        MemoryAsset.create(memoryAsset);
        assertNotNull(memoryAsset.getAssetID());
        assertNotNull(memoryAsset.getContentSize());
        assertNotNull(memoryAsset.getContent());
        assertArrayEquals(memoryAsset.getContent(), new byte[]{1, 2, 3});
    }

    /**
     * Test Stream Content
     */
    @Test
    public void testGetInputStream() {
        MemoryAsset memoryAsset = MemoryAsset.create("Test Stream".getBytes());
        assertNotNull(memoryAsset.getContentStream());
        String expected = Utils.stringFromStream(memoryAsset.getContentStream());
        //String actual =new String(byte[]{1,2,3}.);
        assertEquals(expected, "Test Stream");
    }

    /**
     * Test get Content
     */
    @Test
    public void testgetContent() {
        MemoryAsset memoryAsset = MemoryAsset.create("Test get Content".getBytes());
        assertNotNull(memoryAsset.getContent());
        assertArrayEquals("Test get Content".getBytes(), memoryAsset.getContent());
    }

    /**
     * Test content Size
     */
    @Test
    public void testgetContentSize() {

        MemoryAsset memoryAsset = MemoryAsset.create("Test Content Size".getBytes());
        assertNotNull(memoryAsset.getContent());
        assertNotNull(memoryAsset.getContentSize());
        assertEquals(memoryAsset.getContentSize(), "Test Content Size".length());
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
