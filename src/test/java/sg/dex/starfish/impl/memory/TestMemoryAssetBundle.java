package sg.dex.starfish.impl.memory;

import org.junit.Test;
import sg.dex.starfish.Asset;
import sg.dex.starfish.util.JSON;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class TestMemoryAssetBundle {

    @Test
    public void testAssetBundleCreationWithoutBundleName() {
        byte data1[] = {2, 3, 4};
        Asset a1 = MemoryAsset.create(data1);

        byte data2[] = {5, 6, 7};
        Asset a2 = MemoryAsset.create(data2);

        byte data3[] = {2, 3, 4};
        Asset a3 = MemoryAsset.create(data3);

        byte data4[] = {2, 3, 4};
        Asset a4 = MemoryAsset.create(data4);

        Map<String, String> assetBundle = new HashMap<>();
        assetBundle.put("one", a1.getAssetID());
        assetBundle.put("two", a2.getAssetID());
        assetBundle.put("three", a3.getAssetID());
        assetBundle.put("four", a4.getAssetID());

        MemoryAssetBundle memoryAssetBundle = MemoryAssetBundle.create(assetBundle);
        assertEquals(memoryAssetBundle.getMetadata().size(), 3);
        Map<String, Object> metadata = memoryAssetBundle.getMetadata();
        Map<String, Map<String, String>> contents = (Map<String, Map<String, String>>) metadata.get("contents");


        // Map<String,String> data  =contents.get("two");
        assertEquals(contents.get("two").get("assetID"), a2.getAssetID());
        assertEquals(contents.get("one").get("assetID"), a1.getAssetID());
        assertEquals(contents.get("three").get("assetID"), a3.getAssetID());
        assertEquals(contents.get("four").get("assetID"), a4.getAssetID());


    }

    @Test
    public void testAssetBundleCreationWithBundleName() {
        byte data1[] = {2, 3, 4};
        Asset a1 = MemoryAsset.create(data1);

        byte data2[] = {5, 6, 7};
        Asset a2 = MemoryAsset.create(data2);

        byte data3[] = {2, 3, 4};
        Asset a3 = MemoryAsset.create(data3);

        byte data4[] = {2, 3, 4};
        Asset a4 = MemoryAsset.create(data4);

        Map<String, String> assetBundle = new HashMap<>();
        assetBundle.put("one", a1.getAssetID());
        assetBundle.put("two", a2.getAssetID());
        assetBundle.put("three", a3.getAssetID());
        assetBundle.put("four", a4.getAssetID());

        MemoryAssetBundle memoryAssetBundle = MemoryAssetBundle.create("Test Bundle", assetBundle);
        System.out.println(JSON.toPrettyString(memoryAssetBundle.getMetadata()));
        System.out.println(JSON.toPrettyString(memoryAssetBundle.getAllSubAssetIDs()));
        assertEquals(memoryAssetBundle.getAllSubAssetIDs().size(), 4);
        Map<String, Object> metadata = memoryAssetBundle.getMetadata();
        Map<String, Map<String, String>> contents = (Map<String, Map<String, String>>) metadata.get("contents");
        assertEquals(metadata.get("name"), "Test Bundle");

        assertEquals(contents.get("two").get("assetID"), a2.getAssetID());
        assertEquals(contents.get("one").get("assetID"), a1.getAssetID());
        assertEquals(contents.get("three").get("assetID"), a3.getAssetID());
        assertEquals(contents.get("four").get("assetID"), a4.getAssetID());

    }

}
