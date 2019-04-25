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
 *Class representing a local in-memory bundle asset.
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
        this.assetMap = assetMap;

    }

    /**
     * API to create a memory bundle asset asset with given given Asset named map and metadata
     *
     * @param assetMap map of all asset with name and assetID
     * @param meta     meta data
     * @return it will return the instance of Memory Bundle
     */
    public static MemoryBundle create(MemoryAgent memoryAgent, Map<String, Asset> assetMap, Map<String, Object> meta) {


        //build meta data
        Map<String, Map<String, String>> subAssetIdMap = new HashMap<>();
        Asset asset;
        for (String name : assetMap.keySet()) {
            asset = assetMap.get(name);
            subAssetIdMap.put(name, getAssetIdMap(asset.getAssetID()));
            // TODO: why is this here?
            // memoryAgent.uploadAsset(asset);

        }
        return new MemoryBundle(buildMetaData(subAssetIdMap, meta), assetMap, memoryAgent);


    }

    /**
     * API to create a memory bundle asset asset with given given Asset named map and memory Agent
     *
     * @param assetMap map of all asset with name and assetID
     * @return it will return the instance of Memory Bundle
     */
    public static MemoryBundle create(MemoryAgent memoryAgent, Map<String, Asset> assetMap) {

        //build meta data
        Map<String, Map<String, String>> subAssetIdMap = new HashMap<>();
        Asset asset;
        for (String name : assetMap.keySet()) {
            asset = assetMap.get(name);
            subAssetIdMap.put(name, getAssetIdMap(asset.getAssetID()));
            // TODO: why is this here?
            // memoryAgent.uploadAsset(asset);

        }
        return new MemoryBundle(buildMetaData(subAssetIdMap, null), assetMap, memoryAgent);


    }

    /**
     * API to create a memory bundle asset asset with given given Asset named map
     *
     * @param assetMap map of all asset with name and assetID
     * @return it will return the instance of Memory Bundle
     */
    public static MemoryBundle create(Map<String, Asset> assetMap) {

        //build meta data
        Map<String, Map<String, String>> subAssetIdMap = new HashMap<>();
        Asset asset;
        for (String name : assetMap.keySet()) {
            asset = assetMap.get(name);
            subAssetIdMap.put(name, getAssetIdMap(asset.getAssetID()));
            // TODO: why is this here?
            // memoryAgent.uploadAsset(asset);

        }
        return new MemoryBundle(buildMetaData(subAssetIdMap, null), assetMap, null);


    }

    /**
     * API to create a memory bundle asset asset with given Asset named map and its respective metadata name
     *
     * @param assetMap map of all asset with name and assetID
     * @param meta     meta data for the bundle
     * @return it will return the instance of Memory Bundle
     */
    public static MemoryBundle create(Map<String, Asset> assetMap, Map<String, Object> meta) {

        //build meta data
        Map<String, Map<String, String>> subAssetIdMap = new HashMap<>();
        Asset asset;
        for (String name : assetMap.keySet()) {
            asset = assetMap.get(name);
            subAssetIdMap.put(name, getAssetIdMap(asset.getAssetID()));
            // TODO: why is this here?
            // memoryAgent.uploadAsset(asset);

        }
        return new MemoryBundle(buildMetaData(subAssetIdMap, meta), assetMap, null);


    }

    private static String buildMetaData(Map<String, Map<String, String>> contents, Map<String, Object> meta) {

        Map<String, Object> ob = new HashMap<>();
        ob.put(DATE_CREATED, Instant.now().toString());
        ob.put(TYPE, "bundle");
        ob.put(CONTENTS, contents);

        if (meta != null) {
            for (Map.Entry<String, Object> me : meta.entrySet()) {
                ob.put(me.getKey(), me.getValue());
            }
        }

        return JSON.toString(ob);
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


    private Map<String, Asset> getAssetMap() {
    	/// FIXME metadata shouldn't contain map of Strings to Assets!
        Map<String, Asset> assetMap = (Map<String, Asset>) getMetadata().get(CONTENTS);
        return assetMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue));

    }


    @Override
    public Bundle add(String name, Asset asset) {
        assetMap.put(name, asset);
        return create(getMemoryAgent(), assetMap, getMetadata());
    }

    @Override
    public Bundle addAll(Map<String, Asset> assetMapp) {
        assetMap.putAll(assetMapp);
        return create(getMemoryAgent(), assetMap, getMetadata());
    }

    @Override
    public Asset get(String name) {
        return getAssetMap().get(name);
    }

    @Override
    public Map<String, Object> getAll() {

        return getAssetMap().entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue));

    }
}




