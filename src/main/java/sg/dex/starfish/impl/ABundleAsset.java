package sg.dex.starfish.impl;

import sg.dex.crypto.Hash;
import sg.dex.starfish.Bundle;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.JSONObjectCache;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static sg.dex.starfish.constant.Constant.CONTENTS;

public abstract class ABundleAsset implements Bundle {

    protected final String metadataString;
    protected final Map<String, Object> assetMap;
    protected final String id;

    protected ABundleAsset(String meta) {
        this.metadataString = meta;
        this.assetMap = getAssetMap(metadataString);
        this.id = Hex.toString(Hash.keccak256(meta));
    }

    private static Map<String, Object> getAssetMap(String metadataString) {

        return (Map<String, Object>) JSON.toMap(metadataString).get(CONTENTS);
    }

    @Override
    public void add(String name, Object asset) {
        assetMap.put(name, asset);
    }

    @Override
    public void addAll(Map<String, Object> assetMap) {
        assetMap.putAll(assetMap);
    }

    @Override
    public Object get(String name) {
        return assetMap.get(name);
    }

    @Override
    public Map<String, Object> getAll() {

        // cloning the map

        return assetMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue));

    }

    @Override
    public String getAssetID() {
        return id;
    }


    @Override
    public Map<String, Object> getMetadata() {
        return JSONObjectCache.parse(metadataString);
    }

    @Override
    public String getMetadataString() {
        return metadataString;
    }

    @Override
    public Map<String, Object> getParamValue() {
        Map<String, Object> o = new HashMap<>();
        // default is to pass the asset ID
        o.put("did", getAssetDID());
        return o;
    }

    @Override
    public String toString() {
        return getAssetID();
    }
}
