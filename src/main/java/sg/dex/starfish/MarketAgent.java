package sg.dex.starfish;

import java.util.Map;

/**
 *
 * This interface is for all the basis functionality that Market provide.
 * It include the getting the listing ,updating and creating the listing.
 */

public interface MarketAgent {
    /**
     * API to get all the Listing.
     * This will return all the listing that belong specific agent
     *
     * @return list of listing instacne if no listing found then return an empty list.
     */
    Listing getListing(String id);

    /**
     * API to get one  Listing.
     * This will return  the listing that belong specific agent
     *
     * @return list of listing instacne if no listing found then return an empty list.
     */
    Purchase getPurchasing(String id);

    /**
     * API used to create a  new listing instance
     * it will have map of all metadata that need to be passed while creating the listing instance
     *
     * @param listingData
     * @return
     */
    Listing createListing(Map<String, Object> listingData);




}
