package sg.dex.starfish.impl.memory;

import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.AAsset;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.JSON;

import java.time.Instant;
import java.util.*;

import static sg.dex.starfish.constant.Constant.*;

public class MemoryAssetBundle extends AAsset {
    private Map<String, Asset> subAssetMap;

    private MemoryAssetBundle(String meta) {
        super(meta);
        subAssetMap = new HashMap<>();

    }

    private MemoryAssetBundle(String meta, Asset asset) {
        super(meta);
        subAssetMap = new HashMap<>();
        addSubAsset(asset);
    }

    private MemoryAssetBundle(String meta, List<Asset> assetList) {
        super(meta);
        subAssetMap = new HashMap<>();
        addAllSubAsset(assetList);
    }

    /**
     * Gets a BundleAsset using the content and metadata from the provided asset
     *
     * @param asset The asset to use to construct this BundleAsset
     * @return A new BundleAsset containing the data from the passed asset argument
     */
    public static MemoryAssetBundle create(Map<String, Object> meta, Asset asset) {
        return new MemoryAssetBundle(buildMetaData(meta),asset);

    }

    /**
     * Gets a BundleAsset using the content and metadata from the provided asset
     *
     * @param asset The asset to use to construct this BundleAsset
     * @return A new BundleAsset containing the data from the passed asset argument
     */
    public static MemoryAssetBundle create(Asset asset) {

        return new MemoryAssetBundle(buildMetaData(null), asset);

    }

    public static MemoryAssetBundle create(List<Asset> assetList) {

        return new MemoryAssetBundle(buildMetaData(null), assetList);
    }

    /**
     * Build default metadata for an in-bundle asset
     *
     * @param meta BundleAsset data
     * @return The default metadata as a String
     */
    private static String buildMetaData(Map<String, Object> meta) {
        // ToDO
        String hash = Hex.toString(Hash.keccak256(new Random().toString()));

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

    public List<Asset> getAllSubAsset() {
        return new ArrayList<>(subAssetMap.values());

    }

    public Asset getSubAssetById(String id) {
        return subAssetMap.get(id);
    }

    public void addSubAsset(Asset asset) {
        subAssetMap.putIfAbsent(asset.getAssetID(), asset);
    }

    public void addAllSubAsset(List<Asset> assetLst) {
        for (Asset asset : assetLst)
            subAssetMap.put(asset.getAssetID(), asset);
    }


}
