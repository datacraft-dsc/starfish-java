package sg.dex.starfish;

import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StorageException;

import java.util.Map;

public interface Purchase {

    /**
     * Returns the Listing associated with this Purchase.
     * <p>
     * The listing may not be available in some circumstances (e.g. lack of access permission)
     * in which case an exception will be thrown.
     *
     * @return The listing for this Purchase
     * @throws AuthorizationException if requester does not have access permission
     * @throws StorageException       if there is an error in retrieving the Asset
     */
    Listing getListing();


    /**
     * Gets the Purchase information for this Purchase.
     * <p>
     * Purchase information is defined by the marketplace on which the Purchase is present.
     */
    Map<String, Object> getInfo();


    /**
     * This method can be used to refresh the cached listing data, if the implementation supports
     * mutable purchase with in-memory local caches. Implementations may wish to do this to
     * avoid calling the server for every time purchase details are requested.
     *
     * @throws UnsupportedOperationException If the implementation does not support listing refresh
     */
    Purchase refresh();

    /**
     * API to ge the status of Purchase.
     * Possible status can be :
     *
     * @throws AuthorizationException if requester does not have access permission
     * @throws StorageException       if there is an error in retrieving the Asset
     */
    String status();

    /**
     * API to get the metadata for this Purchase
     *
     * @return
     */
    Map<String, Object> getMetaData();
}
