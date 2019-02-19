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
	 * @return JSONObject
	 */
	public JSONObject getMetadata();

	/**
	 * Returns true if this asset is a data asset, i.e. the asset represents an immutable
	 * data object.
	 *
	 * @return boolean
	 */
	public boolean isDataAsset();

	/**
	 * Returns the metadata for this asset as a String
	 * @return
	 */
	public String getMetadataString();

	/**
	 * Gets a copy of byte contents for this asset
	 * @return
	 */
	public default byte[] getBytes() {
		throw new UnsupportedOperationException("Cannot get bytes for asset of class: "+this.getClass().getCanonicalName());
	}
}
