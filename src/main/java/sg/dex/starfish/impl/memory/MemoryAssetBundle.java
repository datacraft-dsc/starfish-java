package sg.dex.starfish.impl.memory;

import sg.dex.starfish.exception.TODOException;
import sg.dex.starfish.impl.AAsset;
import sg.dex.starfish.util.JSON;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
     * API to create a memory bundle asset asset with given Bundle name
     *
     * @param bundleName Name of the bundle
     * @param assetMap   map of all asset with name and assetID
     * @return
     */
    public static MemoryAssetBundle create(String bundleName, Map<String, String> assetMap) {

        //build meta data
        Map<String, Map<String, String>> subAssetIdMap = new HashMap<>();
        for (String name : assetMap.keySet()) {

            subAssetIdMap.put(name, getAssetIdMap(assetMap.get(name)));
        }
        return new MemoryAssetBundle(buildMetaData(bundleName, subAssetIdMap));


    }

    /**
     * API to create a Bundle Asset with default bundle name
     * IF no bundle name is passed then ,creation Timestamp will be used as bundle name.
     *
     * @param assetMap map of all asset with name and assetID
     * @return
     */
    public static MemoryAssetBundle create(Map<String, String> assetMap) {

        return create(Instant.now().toString(), assetMap);


    }

    /**
     * API to create the Meta data of the Asset bundle
     *
     * @param bundleName
     * @param content
     * @return
     */
    private static String buildMetaData(String bundleName, Map<String, Map<String, String>> content) {
        if (null == content) {
            content = new HashMap<>();
        }

        Map<String, Object> ob = new HashMap<>();
        ob.put("name", bundleName);
        ob.put(TYPE, "bundle");
        ob.put("contents", content);
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
        assetIDMap.put("assetID", assetId);
        return assetIDMap;

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
     * API to get the list of all AssetID present in Asset Bundle
     *
     * @return
     */

    public List<String> getAllSubAssetIDs() {
        if (isBundle()) {

            Map<String, Object> metadata = getMetadata();
            Map<String, Map<String, String>> contents = (Map<String, Map<String, String>>) metadata.get("contents");

            List<String> allSubAssetIdLst = new ArrayList<>();
            for (String data : contents.keySet()) {
                allSubAssetIdLst.add((contents.get(data)).get("assetID"));
            }
            return allSubAssetIdLst;

        }
        throw new TODOException(" Not an Asset Bundle");

    }


}
