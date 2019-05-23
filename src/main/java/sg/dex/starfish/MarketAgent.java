package sg.dex.starfish;

import java.util.Map;

/**
 *
 * This interface is for all the basis functionality that Market provide.
 * It include the getting the listing ,updating and creating the listing.
 * @version 0.5
 */

public interface MarketAgent {
    /**
     * API to get all the Listing.
     * This will return all the listing that belong specific agent
     * 
     * @param id The ID of the Listing to retrieve
     * @return Listing instance, or null if not found
     */
    Listing getListing(String id);

    /**
     * API to get one  Listing.
     * This will return  the listing that belong specific agent
     * @param id The ID 
     *
     * @return Purchase instance, or null if not found
     */
    Purchase getPurchase(String id);

    /**
     * API used to create a  new listing instance
     * it will have map of all metadata that need to be passed while creating the listing instance
     *
     * @param listingData The data to include in the listing
     * @return Listing instance created
     */
    Listing createListing(Map<String, Object> listingData);




}
