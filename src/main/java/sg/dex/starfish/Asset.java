package sg.dex.starfish;

import org.json.simple.JSONObject;

import sg.dex.starfish.util.DID;

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
	 * Gets the Ocean DID for this asset. The DID may include a DID path to specify
	 * the precise asset if the DID refers to an agent managing the asset.
	 * 
	 * Throws an exception if a DID is not available or cannot be constructed.
	 * 
	 * @return DID The global DID for this asset.
	 */
	public DID getAssetDID();

	/**
	 * Gets a copy of the JSON metadata for this asset.
	 * 
	 * @return JSONObject New clone of the parsed JSON metadat for this asset
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
	 * @return
	 */
	public default DataAsset asDataAsset() {
		return (DataAsset)this;
	}

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

	/**
	 * Returns true if this asset is an operation, i.e. can be invoked on an
	 * appropriate agent
	 * 
	 * @return
	 */
	public boolean isOperation();

	/**
	 * Gets the representation of this asset as required to pass to a remote invokable 
	 * service.
	 * @return
	 */
	public JSONObject getParamValue();

}
