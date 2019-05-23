package sg.dex.starfish;

import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StorageException;

import java.util.Map;

/**
 * Interface representing a listing of an asset on the Ocean Network
 * <p>
 * Listings can be used to query and purchase Assets
 * @version 0.5
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
     Asset getAsset();

    /**
     * Returns the service agreement associated with this listing.
     * TODO create service agreement abstraction
     *
     * @return The Agreement associated with this Listing
     */
     Object getAgreement();
    
    /**
     * Gets the listing information for this listing.
     * 
     * FIXME: what is the difference between "Info" and "Metadata"??
     * 
     * Listing information is defined by the marketplace on which the Listing is present.
     * @return The information associated with this Listing
     */
     Map<String,Object> getInfo();

    /**
     * Purchases this listing using the given account
     * FIXME: should this return an asset or a purchase?
     *
     * @param account The account to use for the purchase
     * @return The purchased asset
     */
     Asset purchase(Account account);

    /**
     * Refreshes the Listing data from the agent where it is stored, returning a new listing.
	 *
     * @return The latest version of the Listing
     */
     Listing refresh();

    /**
     * API to get the meta data of this Listing
     * @return A map of listing metadata
     */
     Map<String, Object> getMetaData() ;

    /**
     * API to get the Listing ID
     * @return Listing ID
     */
    public String getId();
    
    /**
     * Gets the Asset ID of this listing
     * @return The Asset ID
     */
    public String getAssetID();

}
