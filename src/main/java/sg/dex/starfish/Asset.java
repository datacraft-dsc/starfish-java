package sg.dex.starfish;

import org.json.simple.JSONObject;

/**
 * Interface representing an asset
 * 
 * @author Mike
 */
public interface Asset {

	/**
	 * Gets the ID of the asset as a 64 character hex string
	 */
	public String getAssetID();
	
	/**
	 * Gets the JSON metadata for this asset
	 * @return
	 */
	public JSONObject getMetadata();

	/**
	 * Returns true if this asset is a data asset, i.e. the asset represents an immutable
	 * data object.
	 * 
	 * @return
	 */
	public boolean isDataAsset();
}
