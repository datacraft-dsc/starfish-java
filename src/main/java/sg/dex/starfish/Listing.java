package sg.dex.starfish;

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
	 * @return
	 */
	public Asset getAsset();
}
