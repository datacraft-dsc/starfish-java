package sg.dex.starfish.impl.memory;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Bundle;
import sg.dex.starfish.util.JSON;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static sg.dex.starfish.constant.Constant.*;

/**
 * Class representing a local in-memory bundle asset.
 * <p>
 * Intended for use in testing or local development situations.
 * <p>
 * This class is to create a memory bundle asset
 * Intended for use in testing or local development situations.
 *
 * @author Ayush
 * @version 0.5
 */
public class MemoryBundle extends AMemoryAsset implements Bundle {

    private Map<String, Asset> assetMap;

    /**
     * Constructor to create the instance of memory bundle
     *
     * @param metaData    metaDAta
     * @param memoryAgent memoryAgent
     * @param assetMap    asset map
     */
    private MemoryBundle(String metaData, Map<String, Asset> assetMap, MemoryAgent memoryAgent) {
        super(metaData, memoryAgent);
        this.assetMap = assetMap == null ? new HashMap<>() : assetMap;

    }


    /**
     * Create a memory bundle asset.
     * This method will create a bundle asset which may contain zero or more sub-asset
     * based on assetMap  passed in the argument.
     * Default memory Agent will be passed to create bundle
     *
     * @param assetMap map of all asset with name and Asset
     * @param meta     additional meta data need for creating bundle asset
     * @return the newly created instance of Memory Bundle
     */
    public static Bundle create(Map<String, Asset> assetMap, Map<String, Object> meta) {

        return new MemoryBundle(buildMetaData(assetMap, meta), assetMap, MemoryAgent.create());
    }

    /**
     * Create a memory bundle asset.
     * This method will create a bundle asset which may contain zero or more sub-asset
     * based on assetMap  passed in the argument.
     *
     * @param assetMap    map of all asset with name and Asset
     * @param memoryAgent memoryAgent instance
     * @param meta        additional meta data need for creating bundle asset
     * @return the newly created instance of Memory Bundle
     */
    public static Bundle create(Map<String, Asset> assetMap, Map<String, Object> meta, MemoryAgent memoryAgent) {

        return new MemoryBundle(buildMetaData(assetMap, meta), assetMap, memoryAgent);
    }

    /**
     * Create a memory bundle asset.
     * This method will create a bundle asset which may contain zero or more sub-asset
     * based on assetMap map passed in the argument.
     *
     * @param assetMap map of all asset with name and Asset
     * @return the newly created instance of Memory Bundle
     */
    public static Bundle create(Map<String, Asset> assetMap) {
        return create(assetMap, null);
    }

    /**
     * API to build the metadata for bundle Asset
     *
     * @param meta     metaData
     * @param assetMap assetMap
     * @return String
     */
    private static String buildMetaData(Map<String, Asset> assetMap, Map<String, Object> meta) {

        Map<String, Object> ob = new HashMap<>();
        ob.put(DATE_CREATED, Instant.now().toString());
        ob.put(TYPE, BUNDLE);
        // adding the meta data provide
        if (meta != null) {
            for (Map.Entry<String, Object> me : meta.entrySet()) {
                ob.put(me.getKey(), me.getValue());
            }
        }
        Map<String, Map<String, String>> subAssetIdMap = buildContent(assetMap);

        ob.put(CONTENTS, subAssetIdMap);
        return JSON.toPrettyString(ob);
    }

    private static Map<String, Map<String, String>> buildContent(Map<String, Asset> assetMap) {
        //build meta data
        Map<String, Map<String, String>> subAssetIdMap = new HashMap<>();
        assetMap = assetMap == null ? new HashMap<>() : assetMap;
        Asset asset;
        for (String name : assetMap.keySet()) {

            asset = assetMap.get(name);
            subAssetIdMap.put(name, getAssetIdMap(asset.getAssetID()));

        }
        return subAssetIdMap;
    }

    /**
     * API to get the map of AssetID based on AssetId
     *
     * @param assetId assetId
     * @return Map
     */
    private static Map<String, String> getAssetIdMap(String assetId) {
        Map<String, String> assetIDMap = new HashMap<>();
        assetIDMap.put(ASSET_ID, assetId);
        return assetIDMap;

    }

    /**
     * API to get the AssetMap.Asset map will have all asset belong to given bundle.
     *
     * @return Map
     */
    private Map<String, Asset> getAssetMap() {
        return assetMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue));

    }

    @Override
    public Bundle add(String name, Asset asset) {
        Map<String, Asset> copyMap = getAssetMap();
        copyMap.put(name, asset);
        return create(copyMap, null);
    }

    @Override
    public Bundle addAll(Map<String, Asset> assetMapp) {
        Map<String, Asset> copyMap = getAssetMap();
        copyMap.putAll(assetMapp);
        return create(copyMap, this.getMetadata());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends Asset> R get(String name) {
        return (R) getAssetMap().get(name);
    }

    @Override
    public Map<String, Asset> getAll() {

        return assetMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue));

    }
}




