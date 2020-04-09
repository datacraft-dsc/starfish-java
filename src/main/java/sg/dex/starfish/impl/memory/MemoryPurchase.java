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
     * Create method is to create a instance of Memory Purchase
     * This method will create a new instance of Purchase based on metaMap
     * passed as an argument.
     *
     * @param agent   Agent on which the listing has to be created
     * @param metaMap Map of metadata that need to create listing
     * @return Return the new Memory Purchase instance
     */
    public static MemoryPurchase create(MemoryAgent agent, Map<String, Object> metaMap) {

        return new MemoryPurchase(agent, metaMap.get(ID).toString(), metaMap);
    }

    /**
     * This method to get the listing id associated with the this purchase
     * This method will return the listing id
     *
     * @return Listing id
     */
    public String getListingId() {
        return meta.get(LISTING_ID).toString();
    }

    /**
     * This method is  to get the Purchase ID
     * This method will return the purchase id
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
