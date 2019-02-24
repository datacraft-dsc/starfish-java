package sg.dex.starfish;

import java.util.Map;

import sg.dex.starfish.util.DID;

/**
 * Interface representing an asset
 *
 * @author Mike
 */
public interface Asset {

	/**
	 * Gets the ID of the asset as a 64 character hex string
	 * 
	 * @return The ID of the asset
	 */
	public String getAssetID();
	
	/**
	 * Gets the Ocean DID for this asset. The DID may include a DID path to specify
	 * the precise asset if the DID refers to an agent managing the asset.
	 * 
	 * Throws an exception if a DID is not available or cannot be constructed.
	 * 
	 * @return The global DID for this asset.
	 */
	public DID getAssetDID();

	/**
	 * Gets a copy of the JSON metadata for this asset.
	 * 
	 * @return New clone of the parsed JSON metadata for this asset
	 */
	public Map<String, Object> getMetadata();

	/**
	 * Returns true if this asset is a data asset, i.e. the asset represents an immutable
	 * data object.
	 *
	 * @return true if the asset is a data asset, false otherwise
	 */
	public boolean isDataAsset();
	
	/**
	 * Returns this asset as a DataAsset.
	 * 
	 * Throws an exception if this asset is not a valid data asset
	 * @return This asset cast to a DataAsset
	 */
	public default DataAsset asDataAsset() {
		return (DataAsset)this;
	}

	/**
	 * Returns the metadata for this asset.
	 * 
	 * @return The metadata of this asset as a String
	 */
	public String getMetadataString();

	/**
	 * Gets a copy of byte contents for this data asset
	 * 
	 * @throws UnsupportedOperationException If this asset does not support getting byte data
	 * @return The byte contents of this asset.
	 */
	public default byte[] getBytes() {
		throw new UnsupportedOperationException("Cannot get bytes for asset of class: "+this.getClass().getCanonicalName());
	}

	/**
	 * Returns true if this asset is an operation, i.e. can be invoked on an
	 * appropriate agent
	 * 
	 * @return true if this asset is an operation, false otherwise
	 */
	public boolean isOperation();

	/**
	 * Gets the representation of this asset as required to pass to a remote invokable 
	 * service.
	 * @return A map representing this asset
	 */
	public Map<String, Object> getParamValue();

}
