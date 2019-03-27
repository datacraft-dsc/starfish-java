package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Purchasing;
import sg.dex.starfish.util.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RemotePurchase implements Purchasing {


    private static final String PURCHAISNG_URL = "/purchases";
    // local map to cache the listing data
    private static Map<String, Object> metaDataCache = null;
    // remote agent reference
    private RemoteAgent remoteAgent;
    // listing id
    private String id;

    /**
     * To get the reference of existing listing user need to pass the remote Agent and the existing listing id.
     *
     * @param remoteAgent
     * @param id
     */
    private RemotePurchase(RemoteAgent remoteAgent, String id) {
        this.remoteAgent = remoteAgent;
        this.id = id;
    }

    /**
     * Create New Purchasing based on the meta data passed in an argument and the remote Agent
     *
     * @param remoteAgent
     * @param data
     */
    private RemotePurchase(RemoteAgent remoteAgent, Map<String, Object> data) {
        this.remoteAgent = remoteAgent;
        initializeCache();

    }

    /**
     * To get the Reference of Existing Listing
     *
     * @param agent
     * @param id
     * @return
     */
    public static RemotePurchase create(RemoteAgent agent, String id) {
        RemotePurchase remotePurchase = new RemotePurchase(agent, id);
        initializeCache();
        return remotePurchase;
    }

    private static void initializeCache() {
        if (null == metaDataCache) {
            metaDataCache = new HashMap<>();
        }

    }

    /**
     * TO create new Purchasing based on the meta data passed and the Remote Agent reference
     *
     * @param agent
     * @return
     */
    public static RemotePurchase create(RemoteAgent agent, Map<String, Object> data) {
        RemotePurchase remotePurchasing = new RemotePurchase(agent, data);
        return remotePurchasing.createPurchase(data);
    }


    public List<RemotePurchase> getAllPurchasing() {

        List<Map<String, Object>> result = remoteAgent.getAllInstance(PURCHAISNG_URL);

        return result.stream()
                .map(p -> new RemotePurchase(remoteAgent, p.get("id").toString()))
                .collect(Collectors.toList());

    }

    public Map<String, Object> getPurchasingMetaData() {

        String metaData = metaDataCache.get(id) == null ?
                remoteAgent.getInstanceMetaData(PURCHAISNG_URL + "/" + id) : metaDataCache.get(id).toString();
        metaDataCache.put(id, JSON.toMap(metaData));
        return JSON.toMap(metaData);

    }


    public RemotePurchase updatePurchase(Map<String, Object> newValue) {

        String body = remoteAgent.updateInstance(newValue, PURCHAISNG_URL + "/" + id);
        metaDataCache.put(id, JSON.toMap(body));
        return this;

    }

    public RemotePurchase createPurchase(Map<String, Object> metaData) {

        String body = remoteAgent.createInstance(metaData, PURCHAISNG_URL);
        String id = JSON.toMap(body).get("id").toString();
        return create(remoteAgent, id);

    }

}
