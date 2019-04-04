package sg.dex.starfish.impl.memory;

import org.junit.Test;
import sg.dex.starfish.Asset;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestMemoryAssetBundle {

    @Test
    public void testAddAssetInNewBundle() {
        byte data[] = {2, 3, 4};
        Asset a = MemoryAsset.create(data);
        MemoryAssetBundle memoryAssetBundle = MemoryAssetBundle.create(a);

        Asset newAsset = memoryAssetBundle.getSubAssetById(a.getAssetID());
        assertEquals(newAsset.getAssetID(), a.getAssetID());

    }

    @Test
    public void testAddAssetInExistingBundle() {
        byte data1[] = {2, 3, 4};
        Asset a1 = MemoryAsset.create(data1);
        MemoryAssetBundle memoryAssetBundle1 = MemoryAssetBundle.create(a1);
        Asset newAsset = memoryAssetBundle1.getSubAssetById(a1.getAssetID());
        assertEquals(newAsset.getAssetID(), a1.getAssetID());


        byte data2[] = {2, 3, 4, 6};
        Asset a2 = MemoryAsset.create(data2);

        MemoryAssetBundle memoryAssetBundle2 = memoryAssetBundle1.addSubAsset(a2);

        Asset a1_new = memoryAssetBundle2.getSubAssetById(a1.getAssetID());
        Asset a2_new = memoryAssetBundle2.getSubAssetById(a2.getAssetID());

        assertEquals(a1_new.getAssetID(), a1.getAssetID());
        assertEquals(a2_new.getAssetID(), a2.getAssetID());


    }

    @Test
    public void testisBundleAsset() {
        byte data[] = {2, 3, 4};
        Asset a = MemoryAsset.create(data);
        MemoryAssetBundle memoryAssetBundle = MemoryAssetBundle.create(a);
        assertEquals(true, memoryAssetBundle.isBundle());

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
        assertEquals(memoryAssetBundle.getAllSubAsset().size(), 2);
        assertEquals(memoryAssetBundle.getSubAssetById(a.getAssetID()).getAssetID(), a.getAssetID());
        assertEquals(memoryAssetBundle.getSubAssetById(a1.getAssetID()).getAssetID(), a1.getAssetID());


    }
    @Test
    public void addAllSubAsset(){
        byte data1[] = {2, 3, 4};
        Asset a1 = MemoryAsset.create(data1);
        MemoryAssetBundle memoryAssetBundle = MemoryAssetBundle.create(a1);

        byte data2[] = {2, 3, 4,5,6,7};
        Asset a2 = MemoryAsset.create(data2);

        byte data3[] = {2, 3, 4, 6,2,3,4,};
        Asset a3 = MemoryAsset.create(data3);

        List<Asset> assetList = new ArrayList<>();
        assetList.add(a2);
        assetList.add(a3);
        MemoryAssetBundle updatedMBA =memoryAssetBundle.addAllSubAsset(assetList);
        assertEquals(updatedMBA.getSubAssetById(a1.getAssetID()).getAssetID(),a1.getAssetID());
        assertEquals(updatedMBA.getSubAssetById(a2.getAssetID()).getAssetID(),a2.getAssetID());
        assertEquals(updatedMBA.getSubAssetById(a3.getAssetID()).getAssetID(),a3.getAssetID());


    }
}
