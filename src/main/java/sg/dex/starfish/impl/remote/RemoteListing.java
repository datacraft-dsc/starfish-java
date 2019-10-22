package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Account;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Listing;
import sg.dex.starfish.impl.AListing;

import java.util.HashMap;
import java.util.Map;

import static sg.dex.starfish.constant.Constant.ID;

/**
 * Class representing an Listing managed via a remote agent.
 * <p>
 * This Listing will be present in Ocean ecosystem and be referred by using the Listing ID.
 *
 * @author Ayush
 * @version 0.5
 */

public class RemoteListing extends AListing {

    // local map to cache the listing data
    private static Map<String, Object> metaDataCache = null;
    // remote agent reference
    private RemoteAgent remoteAgent;
    // listing id
    private String listing_id;


    /**
     * To get the reference of existing listing user need to pass the remote Agent and the existing listing id.
     *
     * @param remoteAgent remote agent
     * @param id          id
     */
    private RemoteListing(RemoteAgent remoteAgent, String id) {
        this.remoteAgent = remoteAgent;
        this.listing_id = id;
    }

    /**
     * To get the Reference of Existing Listing
     *
     * @param agent agent on which this listing needs to be created
     * @param id    listing id
     * @return RemoteListing
     */
    public static RemoteListing create(RemoteAgent agent, String id) {
        RemoteListing remoteListing = new RemoteListing(agent, id);
        initializeCache();
        return remoteListing;
    }

    /**
     * This method is to create the local cache instance
     */
    private static void initializeCache() {
        if (null == metaDataCache) {
            metaDataCache = new HashMap<>();
        }

    }

    @Override
    public Asset getAsset() {

        return remoteAgent.getAsset(getAssetID());
    }

    @Override
    public Object getAgreement() {
        return getMetaData().get("agreement");
    }

    @Override
    public Asset purchase() {
        // Todo get Purchase based on account
        return null;
    }

    @Override
    public Listing refresh() {
        metaDataCache.put(listing_id, remoteAgent.getListingMetaData(listing_id));
        return this;
    }


    @Override
    public Map<String, Object> getMetaData() {
        @SuppressWarnings("unchecked")
        Map<String, Object> metaData = metaDataCache.get(listing_id) == null ?
                remoteAgent.getListingMetaData(listing_id) : (Map<String, Object>) metaDataCache.get(listing_id);
        return metaData;
    }

    /**
     * This method is to get the AssetID
     *
     * @return String assetId
     */
    @Override
    public String getAssetID() {
        return getMetaData().get("assetid").toString();
    }

    /**
     * This method is to get the Listing ID
     *
     * @return String listingId
     */
    @Override
    public String getId() {
        return getMetaData().get(ID).toString();
    }

}
