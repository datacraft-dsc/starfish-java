package sg.dex.starfish.impl.memory;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import sg.dex.starfish.Asset;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMemoryBundleAsset {

    @Test
    public void testAssetBundleCreationWithoutBundleName() {

        // creating  assets

        byte[] data1 = {2, 3, 4};
        Asset a1 = MemoryAsset.create(data1);

        byte[] data2 = {5, 6, 7};
        Asset a2 = MemoryAsset.create(data2);

        byte[] data3 = {2, 3, 4};
        Asset a3 = MemoryAsset.create(data3);

        byte[] data4 = {2, 3, 4};
        Asset a4 = MemoryAsset.create(data4);

        //assigning each asset with name and adding to map
        Map<String, Asset> assetBundle = new HashMap<>();
        assetBundle.put("one", a1);
        assetBundle.put("two", a2);
        assetBundle.put("three", a3);
        assetBundle.put("four", a4);

        // creating a memory Agent
        MemoryAgent memoryAgent = MemoryAgent.create();

        // create assetbundle without any custom metadata // so passing null
        MemoryBundleAsset memoryAssetBundle = MemoryBundleAsset.create(memoryAgent, assetBundle, null);

        // getting the ceated assetbundle metadata
        Map<String, Object> metadata = memoryAssetBundle.getMetadata();

        // checking default name
        assertEquals(metadata.get("name"), null);

        // getting the contents of asset bundle throug metadata
        Map<String, Map<String, String>> contents = (Map<String, Map<String, String>>) metadata.get("contents");

        //comparing id
        assertEquals(contents.get("two").get("assetID"), a2.getAssetID());
        assertEquals(contents.get("one").get("assetID"), a1.getAssetID());
        assertEquals(contents.get("three").get("assetID"), a3.getAssetID());
        assertEquals(contents.get("four").get("assetID"), a4.getAssetID());


        // getting the contents of asset bundle through API
        Map<String, Object> allAssetMap = memoryAssetBundle.getAll();


        assertEquals(((Map<String, Asset>) allAssetMap.get("two")).get("assetID"), a2.getAssetID());
        assertEquals(((Map<String, Asset>) allAssetMap.get("three")).get("assetID"), a3.getAssetID());
        assertEquals(((Map<String, Asset>) allAssetMap.get("one")).get("assetID"), a1.getAssetID());
        assertEquals(((Map<String, Asset>) allAssetMap.get("four")).get("assetID"), a4.getAssetID());

        // validating the singe asset
        Map<String, String> assetIdOfOne = (Map<String, String>) memoryAssetBundle.get("one");

        Asset oneAsset = memoryAgent.getAsset(assetIdOfOne.get("assetID"));
        assertEquals(oneAsset.getAssetID(), a1.getAssetID());


    }

    @Test
    public void testAssetBundleCreationWithBundleName() {

        // creating  assets

        byte[] data1 = {2, 3, 4};
        Asset a1 = MemoryAsset.create(data1);

        byte[] data2 = {5, 6, 7};
        Asset a2 = MemoryAsset.create(data2);


        //assigning each asset with name and adding to map
        Map<String, Asset> assetBundle = new HashMap<>();
        assetBundle.put("one", a1);
        assetBundle.put("two", a2);


        //Custome metadata
        Map<String, Object> customeData = new HashMap<>();
        customeData.put("name", "My First Bundle");

        // creating a memory Agent
        MemoryAgent memoryAgent = MemoryAgent.create();

        // create assetbundle without any custom metadata // so passing null
        MemoryBundleAsset memoryAssetBundle = MemoryBundleAsset.create(memoryAgent, assetBundle, customeData);

        // getting the ceated asset bundle metadata
        Map<String, Object> metadata = memoryAssetBundle.getMetadata();

        assertEquals(metadata.get("name"), "My First Bundle");

    }

}
