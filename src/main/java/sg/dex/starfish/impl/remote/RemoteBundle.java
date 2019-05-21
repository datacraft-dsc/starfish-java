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
     * @param remoteAgent agent on which the  bundle need to create
     * @param assetMap map of all asset with name and assetID
     * @param meta additional meta data needs to be added while creating bundle
     * @return RemoteBundle instance
     */
    public static RemoteBundle create(RemoteAgent remoteAgent, Map<String, Asset> assetMap, Map<String, Object> meta) {


        return new RemoteBundle(buildMetaData(assetMap, meta), remoteAgent, assetMap);


    }

    /**
     * API to create a Remote bundle asset asset with given Bundle name
     *
     * @param assetMap map of all asset with name and assetID
     * @param remoteAgent agent on which this bundle need to be created
     * @return RemoteBundle new instance
     */
    public static RemoteBundle create(RemoteAgent remoteAgent, Map<String, Asset> assetMap) {
        //build meta data
        return create(remoteAgent,assetMap,null);

    }

    /**
     * API to create a Remote bundle asset  with given Asset Map and agent
     * This API will be called from agent .
     * it will create bundle asset based on the Asset passed as map.
     * For each asset data passed in map it will first get the asset id then will create
     * a Remote Asset respectively.
     *
     * @param remoteAgent agent on which this bundle need to be created
     * @param responseMap response map
     * @return RemoteBundle new instance
     */
    public static RemoteBundle createBundle(RemoteAgent remoteAgent, Map<String, Object> responseMap) {

        // get the contents
        Map<String, Map<String, String>> allSubAsset=(Map<String, Map<String, String>>)responseMap.get("contents");
        //build meta data
        Map<String,Asset> assetMap = new HashMap<>();
        for (String name : allSubAsset.keySet()) {
            String assetId = JSON.toMap(allSubAsset.get(name).toString()).get(ASSET_ID).toString();
            ARemoteAsset aRemoteAsset =remoteAgent.getAsset(assetId);
            assetMap.put(name,aRemoteAsset);

        }
        return new RemoteBundle(buildMetaData(assetMap, responseMap), remoteAgent, assetMap);


    }

    /**
     * API to build the metadata for the bundle
     * @param meta additional metadata
     * @return metadata as string
     */
    private static String buildMetaData(Map<String, Asset> assetMap, Map<String, Object> meta) {
        Map<String, Object> ob = new HashMap<>();
        ob.put(DATE_CREATED, Instant.now().toString());
        ob.put(TYPE, BUNDLE);

        if (meta != null) {
            for (Map.Entry<String, Object> me : meta.entrySet()) {
                ob.put(me.getKey(), me.getValue());
            }
        }

        //build meta data
        Map<String, Map<String, String>> subAssetIdMap = new HashMap<>();

        Asset asset;
        for (String name : assetMap.keySet()) {
            asset = assetMap.get(name);
            subAssetIdMap.put(name, getAssetIdMap(asset.getAssetID()));
        }

        ob.put(CONTENTS, subAssetIdMap);
        return JSON.toPrettyString(ob);
    }

    /**
     * API to get the map of AssetID based on AssetId
     *
     * @param assetId asset id
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

        return assetMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue));
    }

    @Override
    public Bundle add(String name, Asset asset) {
        Map<String,Asset> copyMap =getAssetMap();
        copyMap.put(name, asset);
        return create(remoteAgent, copyMap, getMetadata());
    }

    @Override
    public Bundle addAll(Map<String, Asset> assetMapp) {

        Map<String,Asset> copyMap =getAssetMap();
        copyMap.putAll(assetMapp);

        return create(remoteAgent, copyMap, getMetadata());
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
