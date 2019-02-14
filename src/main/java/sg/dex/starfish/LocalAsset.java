/**
  * Represents an Asset on the Ocean Network.
  * 
  * Assets are defined by JSON metadata, and the Asset ID is the keccak256 hash of the metadata
  * as encoded in UTF-8.
  */
package sg.dex.starfish;

import java.io.InputStream;

import org.json.simple.JSONObject;

import sg.dex.crypto.Hash;
import sg.dex.crypto.Hex;

/**
 * Class representing a local in-memory asset.
 * 
 * Intended for use in testing or local development situations.
 * 
 * @author Mike
 *
 */
public class LocalAsset extends ADataAsset {

	private JSONObject metadata=null;
	private final String metadataString;
	private final String id;
	
	private LocalAsset(String meta){
		this.metadataString=meta;
		this.id=Hex.toString(Hash.keccak256(getMetadataString()));
	}
	
	@Override
	public JSONObject getMetadata() {
		return metadata;
	}

	@Override
	public String getAssetID() {
		return id;
	}

	/**
	 * Gets the metadata for this asset as a String
	 * @return
	 */
	public String getMetadataString() {
		return metadataString;
	}

	@Override
	public boolean isDataAsset() {
		return true;
	}

	@Override
	public InputStream getInputStream() {
		throw new UnsupportedOperationException();
	}

}