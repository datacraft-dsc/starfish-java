package sg.dex.starfish.impl.memory;

import org.junit.Test;
import sg.dex.starfish.Asset;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestMemoryAssetBundle {

    @Test
    public void testCreateWithAsset() {
        byte data[] = {2, 3, 4};
        Asset a = MemoryAsset.create(data);
        MemoryAssetBundle memoryAssetBundle = MemoryAssetBundle.create(a);
        System.out.println(memoryAssetBundle.getAllSubAsset());
        System.out.println(memoryAssetBundle.getSubAssetById(a.getAssetID()));
        byte data1[] = {2, 3, 4, 6};
        Asset a1 = MemoryAsset.create(data1);
        memoryAssetBundle.addSubAsset(a1);
        System.out.println(memoryAssetBundle.getAllSubAsset());
        System.out.println(memoryAssetBundle.getSubAssetById(a.getAssetID()));
        memoryAssetBundle.getContent();
    }

    @Test
    public void testAddAssetAfterCreate() {
        byte data[] = {2, 3, 4};
        Asset a = MemoryAsset.create(data);
        MemoryAssetBundle memoryAssetBundle = MemoryAssetBundle.create(a);
        System.out.println(memoryAssetBundle.getAllSubAsset());
        System.out.println(memoryAssetBundle.getSubAssetById(a.getAssetID()));
        byte data1[] = {2, 3, 4, 6};
        Asset a1 = MemoryAsset.create(data1);
        memoryAssetBundle.addSubAsset(a1);
        System.out.println(memoryAssetBundle.getAllSubAsset());
        System.out.println(memoryAssetBundle.getSubAssetById(a.getAssetID()));
        memoryAssetBundle.getContent();
    }

    @Test
    public void testisBundleAsset() {
        byte data[] = {2, 3, 4};
        Asset a = MemoryAsset.create(data);
        MemoryAssetBundle memoryAssetBundle = MemoryAssetBundle.create(a);
        assertEquals(true, memoryAssetBundle.isBundledAsset());

    }

    @Test(expected = Exception.class)
    public void testisBundleAssetContent() {
        byte data[] = {2, 3, 4};
        Asset a = MemoryAsset.create(data);
        MemoryAssetBundle memoryAssetBundle = MemoryAssetBundle.create(a);
        memoryAssetBundle.getContent();

    }

    @Test
    public void testisBundleAssetID() {
        byte data[] = {2, 3, 4};
        Asset a = MemoryAsset.create(data);
        MemoryAssetBundle memoryAssetBundle = MemoryAssetBundle.create(a);
        assertNotNull(memoryAssetBundle.getAssetID());

    }

    @Test
    public void testForAssetList() {

        byte data[] = {2, 3, 4};
        Asset a = MemoryAsset.create(data);

        byte data1[] = {2, 3, 4, 6};
        Asset a1 = MemoryAsset.create(data1);

        List<Asset> assetList = new ArrayList<>();
        assetList.add(a);
        assetList.add(a1);
        MemoryAssetBundle memoryAssetBundle = MemoryAssetBundle.create(assetList);
        assertEquals(memoryAssetBundle.getAllSubAsset().size(),2);
        assertEquals(memoryAssetBundle.getSubAssetById(a.getAssetID()).getAssetID(),a.getAssetID());
        assertEquals(memoryAssetBundle.getSubAssetById(a1.getAssetID()).getAssetID(),a1.getAssetID());


    }
}
