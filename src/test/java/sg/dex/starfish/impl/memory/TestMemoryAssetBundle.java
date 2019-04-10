package sg.dex.starfish.impl.memory;

import org.junit.Test;
import sg.dex.starfish.Asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class TestMemoryAssetBundle {

    @Test
    public void testAssetBundleCreationWithoutBundleName() {

        // create asset
        byte data1[] = {2, 3, 4};
        Asset a1 = MemoryAsset.create(data1);

        byte data2[] = {5, 6, 7};
        Asset a2 = MemoryAsset.create(data2);

        byte data3[] = {2, 3, 4};
        Asset a3 = MemoryAsset.create(data3);

        byte data4[] = {2, 3, 4};
        Asset a4 = MemoryAsset.create(data4);

        // adding asset to list
        List<Asset> initalAssetLst = new ArrayList<>();
        initalAssetLst.add(a1);
        initalAssetLst.add(a2);
        initalAssetLst.add(a3);
        initalAssetLst.add(a4);

        Map<String, Asset> assetBundle = new HashMap<>();
        assetBundle.put("one", a1);
        assetBundle.put("two", a2);
        assetBundle.put("three", a3);
        assetBundle.put("four", a4);

        MemoryAgent memoryAgent = MemoryAgent.create();
        // create assetbundle
        MemoryAssetBundle memoryAssetBundle = MemoryAssetBundle.create(memoryAgent,assetBundle);

        Map<String, Object> metadata = memoryAssetBundle.getMetadata();
        Map<String, Map<String, String>> contents = (Map<String, Map<String, String>>) metadata.get("contents");

        //comparing id
        assertEquals(contents.get("two").get("assetID"), a2.getAssetID());
        assertEquals(contents.get("one").get("assetID"), a1.getAssetID());
        assertEquals(contents.get("three").get("assetID"), a3.getAssetID());
        assertEquals(contents.get("four").get("assetID"), a4.getAssetID());


        // get list of all sub Asset
        List<Asset> allAssetLst = memoryAssetBundle.getAllSubAsset();

        // check if asset present
        Asset a = allAssetLst.stream()
                .filter(asset -> a1.getAssetID().equals(asset.getAssetID()))
                .findAny()
                .orElse(null);

        assertNotNull(a);

        // comparing each added asset with result asset

        List<Asset> filteredAssetList = allAssetLst.stream()
                .filter(asset -> initalAssetLst.stream()
                        .anyMatch(asset1 ->
                                asset1.getAssetID().equals(asset.getAssetID())))
                .collect(Collectors.toList());

        assertEquals(filteredAssetList.size(),
                initalAssetLst.size());

    }

    @Test
    public void testAssetBundleCreationWithBundleName() {
        // create asset
        byte data1[] = {2, 3, 4};
        Asset a1 = MemoryAsset.create(data1);

        byte data2[] = {5, 6, 7};
        Asset a2 = MemoryAsset.create(data2);

        byte data3[] = {2, 3, 4};
        Asset a3 = MemoryAsset.create(data3);

        byte data4[] = {2, 3, 4};
        Asset a4 = MemoryAsset.create(data4);

        // adding asset to list
        List<Asset> initalAssetLst = new ArrayList<>();
        initalAssetLst.add(a1);
        initalAssetLst.add(a2);
        initalAssetLst.add(a3);
        initalAssetLst.add(a4);

        Map<String, Asset> assetBundle = new HashMap<>();
        assetBundle.put("one", a1);
        assetBundle.put("two", a2);
        assetBundle.put("three", a3);
        assetBundle.put("four", a4);

        // create assetbundle
        MemoryAgent memoryAgent =MemoryAgent.create();
        MemoryAssetBundle memoryAssetBundle = MemoryAssetBundle.create(memoryAgent,"AssetBundle Test Name", assetBundle);

        Map<String, Object> metadata = memoryAssetBundle.getMetadata();

        Map<String, Map<String, String>> contents = (Map<String, Map<String, String>>) metadata.get("contents");

        //comparing id
        assertEquals(contents.get("two").get("assetID"), a2.getAssetID());
        assertEquals(contents.get("one").get("assetID"), a1.getAssetID());
        assertEquals(contents.get("three").get("assetID"), a3.getAssetID());
        assertEquals(contents.get("four").get("assetID"), a4.getAssetID());


        // get list of all sub Asset
        List<Asset> allAssetLst = memoryAssetBundle.getAllSubAsset();

        // check if asset present
        Asset a = allAssetLst.stream()
                .filter(asset -> a1.getAssetID().equals(asset.getAssetID()))
                .findAny()
                .orElse(null);

        assertNotNull(a);

        // comparing each added asset with result asset

        List<Asset> filteredAssetList = allAssetLst.stream()
                .filter(asset -> initalAssetLst.stream()
                        .anyMatch(asset1 ->
                                asset1.getAssetID().equals(asset.getAssetID())))
                .collect(Collectors.toList());

        assertEquals(filteredAssetList.size(),
                initalAssetLst.size());

        // get the asset base one asset name from the bundle

        Asset assetByName =memoryAssetBundle.getSubAsset("two");
        assertEquals(assetByName.getAssetID(),a2.getAssetID());

    }


}
