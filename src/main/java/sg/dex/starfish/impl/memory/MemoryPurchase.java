package sg.dex.starfish.impl.memory;

import sg.dex.starfish.util.JSON;

import java.util.Map;

public class MemoryPurchase {

    private final Map<String, Object> meta;
    private final String id;
    private MemoryAgent agent;

    private MemoryPurchase(MemoryAgent agent, String listingID, Map<String, Object> metaMap) {
        this.agent = agent;
        this.meta = metaMap;
        this.id = listingID;
    }

    public static MemoryPurchase create(MemoryAgent agent, Map<String, Object> metaMap) {

        return new MemoryPurchase(agent, metaMap.get("id").toString(), metaMap);
    }

    public String getListingId() {
        return meta.get("listingid").toString();
    }


    public String getId() {
        return id;
    }

    public String getStatus() {
        return meta.get("status") == null ? null : meta.get("status").toString();
    }

    public String getInfo() {
        return meta.get("info") == null ? null : meta.get("info").toString();
    }

    public Map<String, Object> getMetaData() {
        return meta;
    }

    @Override
    public String toString() {
        return "Purchase: " + JSON.toString(meta);
    }

}
