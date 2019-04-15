package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Listing;
import sg.dex.starfish.Purchase;
import sg.dex.starfish.exception.TODOException;

import java.util.HashMap;
import java.util.Map;

public class RemotePurchase implements Purchase {


    // local map to cache the listing data
    private static Map<String, Object> metaDataCache = null;
    // remote agent reference
    private RemoteAgent remoteAgent;
    // listing id
    private String purchase_id;


    /**
     * To get the reference of existing listing user need to pass the remote Agent and the existing listing id.
     *
     * @param remoteAgent
     * @param id
     */
    private RemotePurchase(RemoteAgent remoteAgent, String id) {
        this.remoteAgent = remoteAgent;
        this.purchase_id = id;
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

    /**
     * API to create the local cache when the Purchase instance is create
     */
    private static void initializeCache() {
        if (null == metaDataCache) {
            metaDataCache = new HashMap<>();
        }

    }

    @Override
    public Listing getListing() {
        // get the Listing from Listing id
        Listing listing;
        try {
            listing = remoteAgent.getListing(purchase_id);
        } catch (Exception e) {
            throw new TODOException("Exception occurred :" + e.getMessage());
        }
        return listing;
    }

    @Override
    public Map<String, Object> getInfo() {


        return (Map<String, Object>) getMetaData().get("info");
    }


    @Override
    public Purchase refresh() {
        metaDataCache.put(purchase_id, remoteAgent.getPurchaseMetaData(purchase_id));
        return this;
    }

    @Override
    public String status() {
        return getMetaData().get("status").toString();
    }

    @Override
    public Map<String, Object> getMetaData() {
        Map<String, Object> metaData = metaDataCache.get(purchase_id) == null ?
                remoteAgent.getPurchaseMetaData(purchase_id) : (Map<String, Object>) metaDataCache.get(purchase_id);
        return metaData;
    }

    private String getListingId() {
        return getMetaData().get("listingid").toString();
    }

    private String getAgreemnt() {
        return getMetaData().get("agreement").toString();
    }

    private String getUserId() {
        return getMetaData().get("userid").toString();
    }

    private String getPurchaseId() {
        return getMetaData().get("id").toString();
    }
}
