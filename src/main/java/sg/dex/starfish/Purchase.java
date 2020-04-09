package sg.dex.starfish;

import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StorageException;

import java.util.Map;

/**
 * Class representing a purchase order for an asset
 *
 * @author Mike
 * @version 0.5
 */
public interface Purchase {

    /**
     * Returns the Listing associated with this Purchase.
     * <p>
     * The listing may not be available in some circumstances (e.g. lack of
     * access permission)
     * in which case an exception will be thrown.
     *
     * @return The listing for this Purchase
     * @throws AuthorizationException if requester does not have access
     *                                permission
     * @throws StorageException       if there is an error in retrieving the
     *                                Asset
     */
    Listing getListing();


    /**
     * API to get the status of Purchase.
     * Possible status can be :
     * "wishlist", "ordered", "delivered"
     *
     * @return The status of the purchase
     * @throws AuthorizationException if requester does not have access
     *                                permission
     * @throws StorageException       if there is an error in retrieving the
     *                                Asset
     */
    String status();

    /**
     * Get the metadata for this Purchase as a nested Map
     *
     * @return The metadata for this purchase
     */
    Map<String, Object> getMetaData();
}
