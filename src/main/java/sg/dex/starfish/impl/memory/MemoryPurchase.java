package sg.dex.starfish.impl.memory;

import sg.dex.starfish.Listing;
import sg.dex.starfish.Purchase;
import sg.dex.starfish.util.JSON;

import java.util.Map;

/**
 * Class representing a local in-memory  Purchase.
 * <p>
 * Intended for use in testing or local development situations.
 */
public class MemoryPurchase implements Purchase {

    private final Map<String, Object> meta;
    private final String id;
    private MemoryAgent agent;

    private MemoryPurchase(MemoryAgent agent, String listingID, Map<String, Object> metaMap) {
        this.agent = agent;
        this.meta = metaMap;
        this.id = listingID;
    }

    /**
     * API to create a instance of Memory Purchase
     * @param agent
     * @param metaMap
     * @return
     */
    public static MemoryPurchase create(MemoryAgent agent, Map<String, Object> metaMap) {

        return new MemoryPurchase(agent, metaMap.get("id").toString(), metaMap);
    }

    /**
     * API to get the listing id associated with the this purchase
     * @return
     */
    public String getListingId() {
        return meta.get("listingid").toString();
    }

    /**
     * API to get the Purchase ID
     *
     * @return
     */
    public String getId() {
        return id;
    }


    @Override
    public Listing getListing() {
        return null;
    }

    /**
     * API to get the information of the Purchase
     *
     * @return
     */
    @Override
    public Map<String, Object> getInfo() {
        return meta.get("info") == null ? null : (Map<String, Object>) meta.get("info");
    }

    @Override
    public Purchase refresh() {
        return null;
    }

    @Override
    public String status() {
        return meta.get("status") == null ? null : meta.get("status").toString();
    }

    /**
     * API to get the metadata of this purchase
     *
     * @return map of metadata associated with this purchase
     */
    @Override
    public Map<String, Object> getMetaData() {
        return meta;
    }

    @Override
    public String toString() {
        return "Purchase: " + JSON.toString(meta);
    }

}
