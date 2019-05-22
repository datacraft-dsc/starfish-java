package sg.dex.starfish.impl.memory;

import sg.dex.starfish.Listing;
import sg.dex.starfish.Purchase;
import sg.dex.starfish.util.JSON;

import java.util.Map;

import static sg.dex.starfish.constant.Constant.*;

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
     *
     * @param agent   Agent on which the listing has to be created
     * @param metaMap Map of metadata that need to create listing
     * @return Return the new Memory Purchase instance
     */
    public static MemoryPurchase create(MemoryAgent agent, Map<String, Object> metaMap) {

        return new MemoryPurchase(agent, metaMap.get(ID).toString(), metaMap);
    }

    /**
     * API to get the listing id associated with the this purchase
     *
     * @return Listing id
     */
    public String getListingId() {
        return meta.get(LISTING_ID).toString();
    }

    /**
     * API to get the Purchase ID
     *
     * @return purchase id
     */
    public String getId() {
        return id;
    }


    @Override
    public Listing getListing() {
        return agent.getListing(meta.get(LISTING_ID).toString());
    }


    @Override
    public Map<String, Object> getInfo() {
        return meta.get(INFO) == null ? null : (Map<String, Object>) meta.get(INFO);
    }

    @Override
    public String status() {
        return meta.get(STATUS) == null ? null : meta.get(STATUS).toString();
    }

    @Override
    public Map<String, Object> getMetaData() {
        return meta;
    }

    @Override
    public String toString() {
        return "Purchase: " + JSON.toString(meta);
    }

}
