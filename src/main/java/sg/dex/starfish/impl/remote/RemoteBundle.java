package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Bundle;
import sg.dex.starfish.util.JSON;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static sg.dex.starfish.constant.Constant.*;


/**
 * Class representing an bundle asset managed via a remote agent.
 *
 * This bundle asset will be present in Ocean ecosystem and be referred by using the asset ID.
 *
 * @author Ayush
 */
public class RemoteBundle extends ARemoteAsset implements Bundle {
    private Map<String, Asset> assetMap;

    private RemoteBundle(String metaData, RemoteAgent remoteAgent, Map<String, Asset> assetMap) {
        super(metaData,remoteAgent);
        this.assetMap = assetMap;

    }

    /**
     * API to create a Remote bundle asset asset with given Bundle name
     *
     * @param assetMap map of all asset with name and assetID
     * @return RemoteBundle
     */
    public static RemoteBundle create(RemoteAgent remoteAgent, Map<String, Asset> assetMap, Map<String, Object> meta) {

        //build meta data
        Map<String, Map<String, String>> subAssetIdMap = new HashMap<>();

        Asset asset;
        for (String name : assetMap.keySet()) {
            asset = assetMap.get(name);
            subAssetIdMap.put(name, getAssetIdMap(asset.getAssetID()));
            // TODO: why is this here?
            // memoryAgent.uploadAsset(asset);

        }
        return new RemoteBundle(buildMetaData(subAssetIdMap, meta), remoteAgent, assetMap);


    }

    /**
     * API to create a Remote bundle asset asset with given Bundle name
     *
     * @param assetMap map of all asset with name and assetID
     * @return RemoteBundle
     */
    public static RemoteBundle create(RemoteAgent remoteAgent, Map<String, Asset> assetMap) {

        //build meta data
        Map<String, Map<String, String>> subAssetIdMap = new HashMap<>();
        Asset asset;
        for (String name : assetMap.keySet()) {
            asset = assetMap.get(name);
            subAssetIdMap.put(name, getAssetIdMap(asset.getAssetID()));
            // TODO: why is this here?
            // memoryAgent.uploadAsset(asset);

        }
        return new RemoteBundle(buildMetaData(subAssetIdMap, null), remoteAgent, assetMap);


    }

    /**
     * API to create a Remote bundle asset asset with given Bundle name
     *
     * @param assetMap map of all asset with name and assetID
     * @return RemoteBundle
     */
    public static RemoteBundle create(Map<String, Asset> assetMap) {

        //build meta data
        Map<String, Map<String, String>> subAssetIdMap = new HashMap<>();
        Asset asset;
        for (String name : assetMap.keySet()) {
            asset = assetMap.get(name);
            subAssetIdMap.put(name, getAssetIdMap(asset.getAssetID()));
            // TODO: why is this here?
            // memoryAgent.uploadAsset(asset);

        }
        return new RemoteBundle(buildMetaData(subAssetIdMap, null), null, assetMap);


    }

    /**
     * API to create a Remote bundle asset asset with given Bundle name
     *
     * @param assetMap map of all asset with name and assetID
     * @return RemoteBundle
     */
    public static RemoteBundle create(Map<String, Asset> assetMap, Map<String, Object> meta) {

        //build meta data
        Map<String, Map<String, String>> subAssetIdMap = new HashMap<>();
        Asset asset;
        for (String name : assetMap.keySet()) {
            asset = assetMap.get(name);
            subAssetIdMap.put(name, getAssetIdMap(asset.getAssetID()));
            // TODO: why is this here?
            // memoryAgent.uploadAsset(asset);

        }
        return new RemoteBundle(buildMetaData(subAssetIdMap, meta), null, assetMap);


    }

    /**
     * API to build the metadata for the bundle
     * @param contents
     * @param meta
     * @return
     */
    private static String buildMetaData(Map<String, Map<String, String>> contents, Map<String, Object> meta) {
        //String hash = Hex.toString(Hash.keccak256(data));

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
     * @return Map<String, String> assetIdMap
     */
    private static Map<String, String> getAssetIdMap(String assetId) {
        Map<String, String> assetIDMap = new HashMap<>();
        assetIDMap.put(ASSET_ID, assetId);
        return assetIDMap;

    }

    /**
     * API to get the AssetMap
     * @return assetMap that belong to bundle
     */
    private Map<String, Asset> getAssetMap() {

        return (Map<String, Asset>) getMetadata().get(CONTENTS);
    }

    @Override
    public Bundle add(String name, Asset asset) {
        assetMap.put(name, asset);
        return create(remoteAgent, assetMap, getMetadata());
    }

    @Override
    public Bundle addAll(Map<String, Asset> assetMapp) {
        assetMap.putAll(assetMapp);
        return create(remoteAgent, assetMap, getMetadata());
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
