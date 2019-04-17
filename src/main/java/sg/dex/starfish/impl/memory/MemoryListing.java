package sg.dex.starfish.impl.memory;

import sg.dex.starfish.Account;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Listing;
import sg.dex.starfish.impl.AListing;
import sg.dex.starfish.util.JSON;

import java.util.Map;

public class MemoryListing extends AListing {
	private MemoryAgent agent;
	
    private final Map<String,Object> meta;
	private final String id;

    private MemoryListing(MemoryAgent agent, String listingID, Map<String,Object> metaMap) {
    	this.agent=agent;
        this.meta=metaMap;
        this.id=listingID;
    }

    /**
     * API will create the listing instance based on the metaMap data passed.
     * @param agent
     * @param metaMap
     * @return
     */
    public static MemoryListing create(MemoryAgent agent, Map<String,Object> metaMap) {

    	String listingID= metaMap.get("id").toString();

        return  new MemoryListing(agent,listingID,metaMap);
    }

    public String getAssetID() {
        return (String) meta.get("assetid");
    }

    @Override
    public Asset getAsset() {
        return agent.getAsset(getAssetID());
    }

    @Override
    public Object getAgreement() {
        return null;
    }

    @Override
    public Map<String, Object> getInfo() {
        return null;
    }

    @Override
    public Asset purchase(Account account) {
        return null;
    }

    @Override
    public Listing refresh() {
    	return this;
    }

    @Override
    public Map<String, Object> getMetaData() {
        return meta;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "MemoryListing: " + JSON.toString(meta);
    }
}
