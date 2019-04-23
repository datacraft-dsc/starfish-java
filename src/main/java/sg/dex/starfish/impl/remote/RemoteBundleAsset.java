package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Asset;
import sg.dex.starfish.BundleAsset;
import sg.dex.starfish.impl.AAgent;
import sg.dex.starfish.impl.ABundleAsset;
import sg.dex.starfish.util.JSON;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static sg.dex.starfish.constant.Constant.*;

public class RemoteBundleAsset extends ABundleAsset  {
    private static AAgent remoteAgent;

    protected RemoteBundleAsset(String meta) {
        super(meta);
    }

    /**
     * API to create a memory bundle asset asset with given Bundle name
     *
     * @param assetMap map of all asset with name and assetID
     * @return
     */
    public static RemoteBundleAsset create(AAgent aAgent, Map<String, Asset> assetMap, Map<String, Object> meta) {

        //build meta data
        Map<String, Map<String, String>> subAssetIdMap = new HashMap<>();
        remoteAgent = aAgent;
        Asset asset;
        for (String name : assetMap.keySet()) {
            asset = assetMap.get(name);
            subAssetIdMap.put(name, getAssetIdMap(asset.getAssetID()));
            remoteAgent.uploadAsset(asset);

        }
        return new RemoteBundleAsset(buildMetaData(subAssetIdMap, meta));


    }


    /**
     * API to create the Meta data of the Asset bundle
     *
     * @param content
     * @return
     */
    private static String buildMetaData(Map<String, Map<String, String>> content, Map<String, Object> meta) {
        if (null == content) {
            content = new HashMap<>();
        }
        Map<String, Object> ob = new HashMap<>();
        ob.put("name", null);
        ob.put(DATE_CREATED, Instant.now().toString());
        ob.put(TYPE, "bundle");
        ob.put("contents", content);
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
        assetIDMap.put("assetID", assetId);
        return assetIDMap;

    }
}
