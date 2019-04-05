package sg.dex.starfish.impl.memory;

import org.junit.Test;
import sg.dex.starfish.Asset;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestMemoryAssetBundle {

    @Test
    public void testAddAssetInNewBundle() {
        byte data[] = {2, 3, 4};
        Asset a = MemoryAsset.create(data);
        MemoryAssetBundle memoryAssetBundle = MemoryAssetBundle.create("test","abc",a);

        System.out.println(memoryAssetBundle.getAllSubAsset());
        System.out.println(memoryAssetBundle.getMetadata());
        System.out.println(memoryAssetBundle.getAssetID());
        System.out.println(memoryAssetBundle.getSubAssetById("abc"));




    }

    @Test
    public void testAddAssetInNewBundle2() {
        byte data[] = {2, 3, 4};
        Asset a = MemoryAsset.create(data);

        Map<String,String> map1 = new HashMap<>();
        map1.put("abc","1234");
        map1.put("def","567");
        MemoryAssetBundle memoryAssetBundle = MemoryAssetBundle.create("test",map1);

        assertEquals(memoryAssetBundle.getAllSubAsset().size(),2);

        System.out.println(memoryAssetBundle.getMetadata().get("contents"));
        System.out.println(memoryAssetBundle.getMetadata());
        assertNotNull(memoryAssetBundle.getMetadata().get("contents"));

    }
    @Test
    public void testAddSubAsset() {
        byte data[] = {2, 3, 4};
        Asset a = MemoryAsset.create(data);


        MemoryAssetBundle memoryAssetBundle = MemoryAssetBundle.create("TestBundle","first Asset",a.getAssetID());

        byte data2[] = {12, 13, 14};
        Asset a2 = MemoryAsset.create(data);
        memoryAssetBundle.addSubAsset("Second Asset",a2.getAssetID());

        assertEquals(memoryAssetBundle.getAllSubAsset().size(),2);
        System.out.println(memoryAssetBundle.getMetadata());
        assertNotNull(memoryAssetBundle.getMetadata().get("contents"));

    }


}
