package sg.dex.starfish.impl.memory;

import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.AAsset;
import sg.dex.starfish.util.JSON;

import java.util.HashMap;
import java.util.Map;

import static sg.dex.starfish.constant.Constant.TYPE;

/**
 * This class is to create a memory bundle asset
 */
public class MemoryAssetBundle extends AAsset {

    private MemoryAssetBundle(String metaData) {

        super(metaData);

    }

    /**
     * Gets a BundleAsset using the content and metadata from the provided asset
     *
     * @param name
     * @param subAssetId
     * @return
     */

    public static MemoryAssetBundle create(String bundleName, String name, String subAssetId) {
        //build meta data
        Map<String, String> subAssetIdMap = new HashMap<>();
        subAssetIdMap.put(name, "AssetId : " + subAssetId);

        return new MemoryAssetBundle(buildMetaData(bundleName, subAssetIdMap));

    }

    /**
     * Gets a BundleAsset using the content and metadata from the provided asset
     *
     * @param name
     * @param
     * @return
     */
    public static MemoryAssetBundle create(String bundleName, String name, Asset subAsset) {
        //build meta data
        Map<String, String> subAssetIdMap = new HashMap<>();
        subAssetIdMap.put(name, "AssetId : " + subAsset.getAssetID());
        return new MemoryAssetBundle(buildMetaData(bundleName, subAssetIdMap));
    }

    /**
     * Gets a BundleAreturn new MemoryAssetBundle(buildMetaData(bundleName, subAssetIdMap));   sset using the content and metadata from the provided asset
     *
     * @param
     * @param
     * @return
     */
    public static MemoryAssetBundle create(String bundleName, Map<String, String> assetMap) {

        //build meta data
        Map<String, String> subAssetIdMap = new HashMap<>();
        for (String name : assetMap.keySet()) {

            subAssetIdMap.put(name, "AssetId: " + assetMap.get(name));
        }
        return new MemoryAssetBundle(buildMetaData(bundleName, subAssetIdMap));


    }


    /**
     * Build default metadata for an in-bundle asset
     *
     * @return The default metadata as a String
     */
    private static String buildMetaData(String bundleName, Map<String, String> content) {
        if (null == content) {
            content = new HashMap<>();
        }

        Map<String, Object> ob = new HashMap<>();
        ob.put("name", bundleName);
        ob.put(TYPE, "bundle");
        ob.put("contents", content);
        return JSON.toPrettyString(ob);
    }

    @Override
    public boolean isDataAsset() {
        return false;
    }

    @Override
    public boolean isOperation() {
        return false;
    }

    /**
     * API to get all the Sub Asset
     *
     * @return it will return a map of all Sub asset present in the Asset Bundle
     */
    public Map<String, String> getAllSubAsset() {
        return (Map<String, String>) getMetadata().get("contents");

    }

    /**
     * API to get the sub Asset based on sub Asset ID
     *
     * @param name sub Asset ID
     * @return
     */
    public String getSubAssetById(String name) {
        Map<String, String> metaData = (Map<String, String>) getMetadata().get("contents");
        return metaData.get(name);
    }


    /**
     * API to add subAsset .
     * It will create a new instance of Assetbundle instance and all the give sub-Asset
     *
     * @param
     * @return
     */
    public MemoryAssetBundle addSubAsset(String name, String subAssetId) {
        Map<String, Object> metaData = getMetadata();
        Map<String, String> content = (Map<String, String>) metaData.get("contents");
        String bundleName = metaData.get("name").toString();
        content.put(name, "AssetId: "+ subAssetId);
        return create(bundleName, content);

    }

    /**
     * API to add subAsset .
     * It will create a new instance of Assetbundle instance and all the give sub-Asset
     *
     * @param subAsset
     * @return
     */
    public MemoryAssetBundle addSubAsset(String name, Asset subAsset) {
        Map<String, Object> metaData = getMetadata();
        Map<String, String> content = (Map<String, String>) metaData.get("contents");
        String bundleName = metaData.get("name").toString();
        content.put(name, "AssetId: "+subAsset.getAssetID());
        return create(bundleName, content);
    }


}
