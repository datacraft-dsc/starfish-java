package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Account;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Listing;
import sg.dex.starfish.exception.TODOException;
import sg.dex.starfish.impl.AListing;
import sg.dex.starfish.util.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class is responsible for creating the listing instance.
 * To create and instance user just need to pass the agent and the data(metadata)
 * for which the listing instance should be created.
 * It also sever getting the meta data of existing listing ,updating the existing listing.
 */

public class RemoteListing extends AListing {

    private static final String LISTING_URL = "/listings";
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
    private RemoteListing(RemoteAgent remoteAgent, String id) {
        this.remoteAgent = remoteAgent;
        this.id = id;
    }

    /**
     * Create New Listing based on the meta data passed in an argument and the remote Agent
     *
     * @param remoteAgent
     * @param data
     */
    private RemoteListing(RemoteAgent remoteAgent, Map<String, Object> data) {
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
    public static RemoteListing create(RemoteAgent agent, String id) {
        RemoteListing remoteListing = new RemoteListing(agent, id);
        initializeCache();
        return remoteListing;
    }

    private static void initializeCache() {
        if (null == metaDataCache) {
            metaDataCache = new HashMap<>();
        }

    }

    /**
     * TO create new Listing based on the meta data passed and the Remote Agent reference
     *
     * @param agent
     * @return
     */
    public static RemoteListing create(RemoteAgent agent, Map<String, Object> data) {
        RemoteListing remoteListing = new RemoteListing(agent, data);
        return remoteListing.createListing(data);
    }


    @Override
    public Asset getAsset() {
        return null;
    }

    @Override
    public Object getAgreement() {
        throw new TODOException();
    }

    @Override
    public Asset purchase(Account account) {
        throw new TODOException();
    }

    @Override
    public Listing refresh() {
        // metaDataCache.put(id, getListingMetadata());
        return this;
    }

    public List<RemoteListing> getAllListing() {

        List<Map<String, Object>> result = remoteAgent.getAllInstance(LISTING_URL);

        return result.stream()
                .map(p -> new RemoteListing(remoteAgent, p.get("id").toString()))
                .collect(Collectors.toList());

    }


    public Map<String, Object> getListingMetaData() {

        String metaData = metaDataCache.get(id) == null ?
                remoteAgent.getInstanceMetaData(LISTING_URL + "/" + id) : metaDataCache.get(id).toString();
        metaDataCache.put(id, JSON.toMap(metaData));
        return JSON.toMap(metaData);

    }


    public RemoteListing updateListing(Map<String, Object> newValue) {

        String body = remoteAgent.updateInstance(newValue, LISTING_URL + "/" + id);
        metaDataCache.put(id, JSON.toMap(body));
        return this;

    }

    public RemoteListing createListing(Map<String, Object> metaData) {

        String body = remoteAgent.createInstance(metaData, LISTING_URL);
        String id = JSON.toMap(body).get("id").toString();
        return create(remoteAgent, id);

    }

    @Override
    public Map<String, Object> getInfo() {
        return (Map<String, Object>) getListingMetaData().get("info");
    }

}
