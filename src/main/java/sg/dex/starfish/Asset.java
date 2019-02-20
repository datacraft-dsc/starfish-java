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
	 * Returns this asset as a DataAsset.
	 *
	 * Throws an exception if this asset is not a valid data asset
	 * @return DataAsset
	 */
	public default DataAsset asDataAsset() {
		return (DataAsset)this;
	}

	/**
	 * Returns the metadata for this asset as a String
	 * @return String metadata
	 */
	public String getMetadataString();

	/**
	 * Gets a copy of byte contents for this asset
	 * @return byte[] contents for this asset
	 */
	public default byte[] getBytes() {
		throw new UnsupportedOperationException("Cannot get bytes for asset of class: "+this.getClass().getCanonicalName());
	}

	/**
	 * Returns true if this asset is an operation, i.e. can be invoked on an
	 * appropriate agent
	 *
	 * @return boolean true if this asset is an operation
	 */
	public boolean isOperation();
}
