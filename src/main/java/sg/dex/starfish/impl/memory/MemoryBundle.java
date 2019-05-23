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
 *
 * Intended for use in testing or local development situations.
 *
 * This class is to create a memory bundle asset
 * Intended for use in testing or local development situations.
 *
 * @author Ayush
 */
public class MemoryBundle extends AMemoryAsset implements Bundle {

    private Map<String, Asset> assetMap;

    /**
     * Constructor to create the instance of memory bundle
     *
     * @param metaData
     * @param assetMap
     * @param memoryAgent
     */
    private MemoryBundle(String metaData, Map<String, Asset> assetMap, MemoryAgent memoryAgent) {
        super(metaData, memoryAgent);
        this.assetMap = assetMap == null ? new HashMap<>() : assetMap;

    }


    /**
     * Create a memory bundle asset asset with given given Asset named map and metadata
     *
     * @param memoryAgent MemoryAgent to associated with this bundle
     * @param assetMap    map of all asset with name and assetID
     * @param meta        meta data
     * @return it will return the instance of Memory Bundle
     */
    public static Bundle create(MemoryAgent memoryAgent, Map<String, Asset> assetMap, Map<String, Object> meta) {

        //build meta data

        return new MemoryBundle(buildMetaData(assetMap, meta), assetMap, memoryAgent);


    }

    /**
     * API to create a memory bundle asset asset with given given Asset named map and memory Agent
     *
     * @param memoryAgent MemoryAgent to associate with this asset
     * @param assetMap    map of all asset with name and assetID
     * @return it will return the instance of Memory Bundle
     */
    public static Bundle create(MemoryAgent memoryAgent, Map<String, Asset> assetMap) {

        //build meta data

        return create(memoryAgent,assetMap,null);


    }

    /**
     * API to build the metadata for bundle Asset
     *
     * @param meta
     * @return
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
     * @param assetId
     * @return
     */
    private static Map<String, String> getAssetIdMap(String assetId) {
        Map<String, String> assetIDMap = new HashMap<>();
        assetIDMap.put(ASSET_ID, assetId);
        return assetIDMap;

    }

    /**
     * API to get the AssetMap.Asset map will have all asset belong to given bundle.
     *
     * @return
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
        return create(memoryAgent, copyMap, null);
    }

    @Override
    public Bundle addAll(Map<String, Asset> assetMapp) {
        Map<String, Asset> copyMap = getAssetMap();
        copyMap.putAll(assetMapp);
        return create(memoryAgent, copyMap, this.getMetadata());
    }

    @Override
    public Asset get(String name) {
        return  getAssetMap().get(name);
    }

    @Override
    public Map<String, Object> getAll() {

        return assetMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue));

    }
}




