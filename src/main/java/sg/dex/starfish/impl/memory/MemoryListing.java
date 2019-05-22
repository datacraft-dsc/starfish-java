package sg.dex.starfish.impl.memory;

import sg.dex.starfish.Account;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Listing;
import sg.dex.starfish.util.JSON;

import java.util.Map;

import static sg.dex.starfish.constant.Constant.*;

/**
 *Class representing a local in-memory Listing instance.
 *
 * Intended for use in testing or local development situations.
 */
public class MemoryListing implements Listing {
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
     * @param agent Agent on which the listing has to be created
     * @param metaMap Map of metadata that need to create listing
     * @return The new Listing
     */
    public static MemoryListing create(MemoryAgent agent, Map<String,Object> metaMap) {

    	String listingID= metaMap.get(ID).toString();

        return  new MemoryListing(agent,listingID,metaMap);
    }


    @Override
    public Asset getAsset() {
        return agent.getAsset(getAssetID());
    }

    @Override
    public Map<String,Object> getAgreement() {
       return getMetaData().get(AGREEMENT)!= null ?(Map<String, Object>)getMetaData().get(AGREEMENT): null;
    }

    @Override
    public Map<String, Object> getInfo() {
        return  getMetaData().get(INFO)!= null ?(Map<String, Object>)getMetaData().get("info"): null;
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

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "MemoryListing: " + JSON.toString(meta);
    }

    @Override
    public String getAssetID() {
        return getMetaData().get("assetid").toString();
    }
}
