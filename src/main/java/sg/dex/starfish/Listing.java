package sg.dex.starfish;

import org.json.simple.JSONObject;

/**
 * Interface representing a listing of an asset on a data marketplace
 *
 * Listings can be used to query and purchase Assets
 *
 * @author Mike
 *
 */
public interface Listing {

	/**
	 * Returns the asset associated with this listing.
	 *
	 * The asset may not be available in some circumstances (e.g. lack of access permission)
	 * in which case an exception will be thrown.
	 *
	 * @return The asset for this listing
	 */
	public Asset getAsset();
	
	/**
	 * Returns the metadata associated with this listing.
	 *
	 * @return A copy of the JSON metadata
	 */
	public JSONObject getMetadata();
	
	/**
	 * Returns the service agreement associated with this listing.
	 * TODO create service agreement abstraction
	 *
	 * @return The Agreement associated with this Listing
	 */
	public Object getAgreement();
}
