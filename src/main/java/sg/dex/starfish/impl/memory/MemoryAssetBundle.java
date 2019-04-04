package sg.dex.starfish.impl.memory;

import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.AAsset;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.JSON;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sg.dex.starfish.constant.Constant.*;

/**
 * This class is to create a memory bundle asset
 */
public class MemoryAssetBundle extends AAsset {
    private final Map<String, Asset> subAssetMap = new HashMap<>();

    /**
     * Constructor to create an instance of MemoryAssetBundle
     *
     * @param meta     meta data of memory asset bundle
     * @param subAsset sub asset
     */
    private MemoryAssetBundle(String meta, Asset subAsset) {
        super(meta);
        subAssetMap.put(subAsset.getAssetID(), subAsset);
    }

    /**
     * API to create the instance of memory bundle asset from list of subAsset
     *
     * @param meta
     * @param subAssetList
     */
    private MemoryAssetBundle(String meta, List<Asset> subAssetList) {
        super(meta);

        for (Asset asset : subAssetList)
            subAssetMap.put(asset.getAssetID(), asset);
    }

    /**
     * Create a BundleAsset with given subAsset and the meta data
     *
     * @param subAsset sub asset for this BundleAsset
     * @return A new BundleAsset with sub Asset passed
     */
    public static MemoryAssetBundle create(Map<String, Object> meta, Asset subAsset) {
        return new MemoryAssetBundle(buildMetaData(meta, new String(subAsset.getContent())), subAsset);

    }

    /**
     * Gets a BundleAsset using the content and metadata from the provided asset
     *
     * @param asset The asset to use to construct this BundleAsset
     * @return A new BundleAsset containing the data from the passed asset argument
     */
    public static MemoryAssetBundle create(Asset asset) {

        return new MemoryAssetBundle(buildMetaData(null, new String(asset.getContent())), asset);

    }

    /**
     * API to create Bundle Asset from list of subAsset
     *
     * @param assetList
     * @return
     */
    public static MemoryAssetBundle create(List<Asset> assetList) {

        return new MemoryAssetBundle(buildMetaData(null, getContent(assetList)), assetList);
    }

    /**
     * API to create bundle asset from List of sub-Asset and the metaData
     *
     * @param meta
     * @param assetList
     * @return
     */
    public static MemoryAssetBundle create(Map<String, Object> meta, List<Asset> assetList) {

        return new MemoryAssetBundle(buildMetaData(meta, getContent(assetList)), assetList);
    }

    private static String getContent(List<Asset> assetList) {
        StringBuilder allContent = new StringBuilder();
        for (Asset asset : assetList)
            allContent.append(asset.getContent());
        return allContent.toString();
    }

    /**
     * Build default metadata for an in-bundle asset
     *
     * @param meta BundleAsset data
     * @return The default metadata as a String
     */
    private static String buildMetaData(Map<String, Object> meta, String content) {
        // ToDO : need to discuss
        String hash = Hex.toString(Hash.keccak256(content));

        Map<String, Object> ob = new HashMap<>();
        ob.put(DATE_CREATED, Instant.now().toString());
        ob.put(CONTENT_HASH, hash);
        ob.put(TYPE, "bundle");
        ob.put(CONTENT_TYPE, "application/octet-stream");

        if (meta != null) {
            for (Map.Entry<String, Object> me : meta.entrySet()) {
                ob.put(me.getKey(), me.getValue());
            }
        }

        return JSON.toString(ob);
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
    public Map<String, Asset> getAllSubAsset() {
        return subAssetMap;

    }

    /**
     * API to get the sub Asset based on sub Asset ID
     *
     * @param id sub Asset ID
     * @return
     */
    public Asset getSubAssetById(String id) {
        return subAssetMap.get(id);
    }

    /**
     * API to add subAsset .
     * It will create a new instance of Assetbundle instance and all the give sub-Asset
     *
     * @param subAsset
     * @return
     */
    public MemoryAssetBundle addSubAsset(Asset subAsset) {
        if (subAssetMap.size() < 1) {
            return create(subAsset);
        }
        List<Asset> allAsset = new ArrayList(subAssetMap.values());
        allAsset.add(subAsset);
        return create(allAsset);
    }

    /**
     * API to add list of sub-asset in give bundleAsset
     *
     * @param subAssetList
     * @return
     */
    public MemoryAssetBundle addAllSubAsset(List<Asset> subAssetList) {
        List<Asset> allAsset = new ArrayList(subAssetMap.values());
        allAsset.addAll(subAssetList);
        return create(allAsset);
    }


}
