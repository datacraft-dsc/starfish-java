package sg.dex.starfish;

import sg.dex.starfish.impl.remote.RemoteListing;

import java.util.List;
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
    List<RemoteListing> getAllListing();

    /**
     * API to get the meta data of the  listing
     * it will return the Map of meta data of the listing
     *
     * @return Map which has the Meta data of the Listing
     */
    Map<String, Object> getListing();

    /**
     * API to update the existing listing
     * This api will be used to update one or multiple filed of listing metadata.
     *
     * @param metaDaa it will have all fields that need to be updated for the Listing
     * @return It will return the updated listing instance
     */
    RemoteListing updateListing(Map<String, Object> metaDaa);

    /**
     * API used to create a  new listing instance
     * it will have map of all metadata that need to be passed while creating the listing instance
     *
     * @param listingData
     * @return
     */
    RemoteListing createListing(Map<String, Object> listingData);

}
