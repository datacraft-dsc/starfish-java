package sg.dex.starfish.impl.memory;

import java.util.Map;

import org.bouncycastle.util.encoders.Hex;

import sg.dex.crypto.Hash;
import sg.dex.starfish.Account;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Listing;
import sg.dex.starfish.impl.AListing;
import sg.dex.starfish.util.JSON;

public class MemoryListing extends AListing {
	private MemoryAgent agent;
	
    private final Map<String,Object> meta;
	private final String id;

    private MemoryListing(MemoryAgent agent, String listingID, Map<String,Object> metaMap) {
    	this.agent=agent;
        this.meta=metaMap;
        this.id=listingID;
    }

    public static MemoryListing create(MemoryAgent agent, String metaString) {
    	Map<String,Object> metaMap=JSON.parse(metaString);
    	String listingID=Hex.toHexString(Hash.keccak256(metaString));
        return  new MemoryListing(agent,listingID,metaMap);
    }

    public String getAssetID() {
        return (String) meta.get("assetID");
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

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "MemoryListing: " + JSON.toString(meta);
    }
}
