package sg.dex.starfish;

import java.util.Map;

import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StorageException;

/**
 * Interface representing a listing of an asset on the Ocean Network
 * <p>
 * Listings can be used to query and purchase Assets
 *
 * @author Mike
 */
public interface Listing {

    /**
     * Returns the asset associated with this listing.
     * <p>
     * The asset may not be available in some circumstances (e.g. lack of access permission)
     * in which case an exception will be thrown.
     *
     * @return The asset for this listing
     * @throws AuthorizationException if requester does not have access permission
     * @throws StorageException       if there is an error in retrieving the Asset
     */
    public Asset getAsset();


    /**
     * Returns the service agreement associated with this listing.
     * TODO create service agreement abstraction
     *
     * @return The Agreement associated with this Listing
     */
    public Object getAgreement();


    /**
     * Purchases this listing using the given account
     *
     * @param account The account to use for the purchase
     * @return
     */
    public Asset purchase(Account account);

    /**
     * This method can be used to refresh the cached listing data, if the implementation supports
     * mutable listings with in-memory local caches. Implementations may wish to do this to 
     * avoid calling the server for every time listing details are requested.
     * 
     * @throws UnsupportedOperationException If the implementation does not support listing refresh
     */
    public Listing refresh();

}
