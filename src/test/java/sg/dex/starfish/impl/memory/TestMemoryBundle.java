package sg.dex.starfish.impl.memory;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Bundle;
import sg.dex.starfish.util.JSON;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMemoryBundle {

    /**
     * Test bundle creation with bundle name, so name will be null
     */
    @Test
    public void testCreationWithoutBundleName() {

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

        // create asset bundle without any custom metadata // so passing null
        Bundle memoryAssetBundle = MemoryBundle.create(memoryAgent, assetBundle, null);

        // getting the ceated asset bundle metadata
        Map<String, Object> metadata = memoryAssetBundle.getMetadata();

        // checking default name
        assertEquals(metadata.get("name"), null);

        // getting the contents of asset bundle through metadata
        Map<String, Map<String, String>> contents = (Map<String, Map<String, String>>) metadata.get("contents");

        //comparing id
        assertEquals(contents.get("two").get("assetID"), a2.getAssetID());
        assertEquals(contents.get("one").get("assetID"), a1.getAssetID());
        assertEquals(contents.get("three").get("assetID"), a3.getAssetID());
        assertEquals(contents.get("four").get("assetID"), a4.getAssetID());

         System.out.println(JSON.toPrettyString(contents));

        // getting the contents of asset bundle through API
        Map<String, Object> allAssetMap = memoryAssetBundle.getAll();


        assertEquals(( allAssetMap.get("three").toString()), a3.getAssetID());
        assertEquals(( allAssetMap.get("one").toString()), a1.getAssetID());
        assertEquals(( allAssetMap.get("four").toString()), a4.getAssetID());

    }

    /**
     * Test bundle creation wth name given
     */

    @Test
    public void testCreationWithCustomMetadata() {

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

        // creating additional metada
        Map<String, Object> additionalMetaData = new HashMap<>();
        additionalMetaData.put("name", "Bundle1-Test");

        // creating a memory Agent
        MemoryAgent memoryAgent = MemoryAgent.create();

        // create asset bundle without any custom metadata // so passing null
        Bundle memoryAssetBundle = MemoryBundle.create(memoryAgent, assetBundle, additionalMetaData);

        // getting the created asset bundle metadata
        Map<String, Object> metadata = memoryAssetBundle.getMetadata();

        // checking  name
        assertEquals(metadata.get("name"), "Bundle1-Test");

        // getting the contents of asset bundle from metadata
        Map<String, Map<String, String>> contents = (Map<String, Map<String, String>>) metadata.get("contents");

        //comparing id
        assertEquals(contents.get("two").get("assetID"), a2.getAssetID());
        assertEquals(contents.get("one").get("assetID"), a1.getAssetID());
        assertEquals(contents.get("three").get("assetID"), a3.getAssetID());
        assertEquals(contents.get("four").get("assetID"), a4.getAssetID());

        //System.out.println(contents);

        // getting the contents of asset bundle through API
        Map<String, Object> allAssetMap = memoryAssetBundle.getAll();


        assertEquals((allAssetMap.get("two").toString()), a2.getAssetID());
        assertEquals((allAssetMap.get("three").toString()), a3.getAssetID());
        assertEquals((allAssetMap.get("one").toString()), a1.getAssetID());
        assertEquals((allAssetMap.get("four").toString()), a4.getAssetID());

    }

    /**
     * Test bundle creation with name and custom meta data
     */

    @Test
    public void testCreationWithBundleName() {

        // creating  assets

        byte[] data1 = {2, 3, 4};
        Asset a1 = MemoryAsset.create(data1);

        byte[] data2 = {5, 6, 7};
        Asset a2 = MemoryAsset.create(data2);


        //assigning each asset with name and adding to map
        Map<String, Asset> assetBundle = new HashMap<>();
        assetBundle.put("one", a1);
        assetBundle.put("two", a2);


        //Custom metadata
        Map<String, Object> customeData = new HashMap<>();
        customeData.put("name", "My First Bundle");

        // creating a memory Agent
        MemoryAgent memoryAgent = MemoryAgent.create();

        // create asset bundle without any custom metadata // so passing null
        Bundle memoryAssetBundle = MemoryBundle.create(memoryAgent, assetBundle, customeData);

        // getting the ceated asset bundle metadata
        Map<String, Object> metadata = memoryAssetBundle.getMetadata();

        assertEquals(metadata.get("name"), "My First Bundle");

    }

    /**
     * Add sub-Asset map in empty bundle
     */
    @Test
    public void testAddAssetMapInEmptyBundle() {

        // creating  assets

        byte[] data1 = {2, 3, 4};
        Asset a1 = MemoryAsset.create(data1);

        byte[] data2 = {5, 6, 7};
        Asset a2 = MemoryAsset.create(data2);


        //assigning each asset with name and adding to map
        Map<String, Asset> assetBundle = new HashMap<>();
        assetBundle.put("one", a1);
        assetBundle.put("two", a2);

        // creating a memory Agent
        MemoryAgent memoryAgent = MemoryAgent.create();

        // create asset bundle without any custom metadata // so passing null
        // if assetMap is passed as null it will create an empty map for that
        Bundle memoryAssetBundle = MemoryBundle.create(memoryAgent, assetBundle, null);

        Map<String, Asset> assetBundleNEW = new HashMap<>();
        byte[] data3 = {4, 16, 7};
        Asset a3 = MemoryAsset.create(data3);

        byte[] data4 = {15, 16, 17};
        Asset a4 = MemoryAsset.create(data4);

        assetBundle.put("three", a3);
        assetBundle.put("four", a4);
        // adding add asset now after bundle gets created
        memoryAssetBundle = (MemoryBundle) memoryAssetBundle.addAll(assetBundle);

        // getting the created asset bundle metadata
        Map<String, Object> metadata = memoryAssetBundle.getMetadata();

        // checking default name
        assertEquals(metadata.get("name"), null);

        // getting the contents of asset bundle from metadata
        Map<String, Map<String, String>> contents = (Map<String, Map<String, String>>) metadata.get("contents");

        //comparing id
        assertEquals(contents.get("two").get("assetID"), a2.getAssetID());
        assertEquals(contents.get("one").get("assetID"), a1.getAssetID());
        assertEquals(contents.get("four").get("assetID"), a4.getAssetID());
        assertEquals(contents.get("three").get("assetID"), a3.getAssetID());

        // getting the contents of asset bundle through API
        Map<String, Object> allAssetMap = memoryAssetBundle.getAll();


        assertEquals((allAssetMap.get("two").toString()), a2.getAssetID());
        assertEquals((allAssetMap.get("one").toString()), a1.getAssetID());


    }

    /**
     * Add sub-asset map in Existing bundle which has asset
     */
    @Test
    public void testAddAssetInExistingBundle() {

        // creating  assets

        byte[] data1 = {2, 3, 4};
        Asset a1 = MemoryAsset.create(data1);

        byte[] data2 = {5, 6, 7};
        Asset a2 = MemoryAsset.create(data2);

        //assigning each asset with name and adding to map
        Map<String, Asset> assetBundle = new HashMap<>();
        assetBundle.put("one", a1);

        // creating a memory Agent
        MemoryAgent memoryAgent = MemoryAgent.create();

        // create asset bundle without any custom metadata // so passing null
        Bundle memoryAssetBundle = MemoryBundle.create(memoryAgent, assetBundle, null);

        // getting the created asset bundle metadata
        Map<String, Object> metadata = memoryAssetBundle.getMetadata();

        // checking default name
        assertEquals(metadata.get("name"), null);

        // getting the contents of asset bundle through metadata
        Map<String, Map<String, String>> contents = (Map<String, Map<String, String>>) metadata.get("contents");

        //comparing id
        assertEquals(contents.get("two"), null);
        assertEquals(contents.get("one").get("assetID"), a1.getAssetID());

        // adding two asset

        memoryAssetBundle = (MemoryBundle) memoryAssetBundle.add("two", a2);

        // getting the contents of asset bundle through metadata
        contents = (Map<String, Map<String, String>>) memoryAssetBundle.getMetadata().get("contents");
        assertEquals(contents.get("two").get("assetID"), a2.getAssetID());

    }

    /**
     * APi to test get sub-asset bundle by name
     */
    @Test
    public void getBundleBySubAssetName(){
       Asset subAsset = MemoryAsset.create("Test bundle asset by sub asset name");
        Bundle bundle =MemoryBundle.create();

        Bundle newBundle =bundle.add("test",subAsset);
        Asset expected =newBundle.get("test");
        assertEquals(subAsset.getAssetID(),expected.getAssetID());

    }

}
