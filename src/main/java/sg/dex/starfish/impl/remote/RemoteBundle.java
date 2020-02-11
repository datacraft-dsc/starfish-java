package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Bundle;
import sg.dex.starfish.impl.memory.MemoryBundle;
import sg.dex.starfish.util.JSON;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static sg.dex.starfish.constant.Constant.ASSET_ID;
import static sg.dex.starfish.constant.Constant.CONTENTS;


/**
 * Class representing an Bundle Asset managed via a remote agent.
 */
public class RemoteBundle extends ARemoteAsset implements Bundle {
    protected RemoteBundle(String meta, RemoteAgent remoteAgent) {
        super(meta, remoteAgent);
    }


    /**
     * Creates a RemoteAsset with the given metadata on the specified remote agent
     *
     * @param agent RemoteAgent on which to create the RemoteAsset
     * @param meta  Asset metadata which must be a valid JSON string
     * @return RemoteAsset
     */
    public static RemoteBundle create(RemoteAgent agent, String meta) {
        return new RemoteBundle(meta, agent);
    }

    /**
     * This method is to get the AssetMap
     *
     * @return assetMap that belong to bundle
     */
    private Map<String, Asset> getAssetMap() {
        Map<String, Asset> subAssetMap = new HashMap<>();

        @SuppressWarnings("unchecked")
        Map<String, Map<String, String>> subAssetIdMap =
                (Map<String, Map<String, String>>) JSON.toMap(metadataString).get(CONTENTS);

        for (String name : subAssetIdMap.keySet()) {
            Asset asset = aAgent.getAsset(subAssetIdMap.get(name).get(ASSET_ID));
            subAssetMap.put(name, asset);
        }
        return subAssetMap;
    }

    @Override
    public Bundle add(String name, Asset asset) {
        Map<String, Asset> copyMap = getAssetMap();
        copyMap.put(name, asset);
        Bundle newBundle = MemoryBundle.create(copyMap, this.getMetadata());
        return aAgent.registerAsset(newBundle);
    }

    @Override
    public Bundle addAll(Map<String, Asset> assetMapp) {
        Map<String, Asset> copyMap = getAssetMap();
        copyMap.putAll(assetMapp);
        Bundle newBundle = MemoryBundle.create(copyMap, this.getMetadata());
        return aAgent.registerAsset(newBundle);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends Asset> R get(String name) {
        return (R) getAssetMap().get(name);
    }

    @Override
    public Map<String, Asset> getAll() {
        return getAssetMap().entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue));
    }
}
