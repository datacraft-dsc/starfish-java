package sg.dex.starfish.impl.memory;

import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.AAgent;
import sg.dex.starfish.impl.ABundleAsset;
import sg.dex.starfish.util.JSON;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static sg.dex.starfish.constant.Constant.*;

/**
 * This class is to create a memory bundle asset
 */
public class MemoryBundleAsset extends ABundleAsset {

    private static AAgent memoryAgent;

    private MemoryBundleAsset(String metaData) {
        super(metaData);
    }


    /**
     * API to create a memory bundle asset asset with given Bundle name
     *
     * @param assetMap map of all asset with name and assetID
     * @return
     */
    public static MemoryBundleAsset create(AAgent aAgent, Map<String, Asset> assetMap, Map<String, Object> meta) {

        //build meta data
        Map<String, Map<String, String>> subAssetIdMap = new HashMap<>();
        memoryAgent = aAgent;
        Asset asset;
        for (String name : assetMap.keySet()) {
            asset = assetMap.get(name);
            subAssetIdMap.put(name, getAssetIdMap(asset.getAssetID()));
            memoryAgent.uploadAsset(asset);

        }
        return new MemoryBundleAsset(buildMetaData(subAssetIdMap, meta));


    }


    /**
     * API to create the Meta data of the Asset bundle
     *
     * @param contents
     * @return
     */
    private static String buildMetaData(Map<String, Map<String, String>> contents, Map<String, Object> meta) {
        if (null == contents) {
            contents = new HashMap<>();
        }
        Map<String, Object> ob = new HashMap<>();
        ob.put(BUNDLE_NAME, null);
        ob.put(DATE_CREATED, Instant.now().toString());
        ob.put(TYPE, "bundle");
        ob.put(CONTENTS, contents);
        ob.put(CONTENT_TYPE, "application/octet-stream");
        if (meta != null) {
            for (Map.Entry<String, Object> me : meta.entrySet()) {
                ob.put(me.getKey(), me.getValue());
            }
        }
        return JSON.toPrettyString(ob);
    }

    /**
     * API to get the map of AssetID based on AssetId
     *
     * @param assetId
     * @return
     */
    private static Map<String, String> getAssetIdMap(String assetId) {
        Map<String, String> assetIDMap = new HashMap<>();
        assetIDMap.put(ASSETID, assetId);
        return assetIDMap;

    }

}
