package sg.dex.starfish;

import org.json.simple.JSONObject;

import sg.dex.crypto.Hash;
import sg.dex.crypto.Hex;
import sg.dex.starfish.util.JSONObjectCache;

/**
 * Abstract base class for immutable asset implementations
 * 
 * Includes default handing of metadata
 * 
 * @author Mike
 *
 */
public abstract class AAsset implements Asset {
	protected final String metadataString;
	protected final String id;

	protected AAsset(String meta) {
		this.metadataString=meta;
		this.id=Hex.toString(Hash.keccak256(meta));
	}

	@Override
	public String toString() {
		return getAssetID();
	}

	@Override
	public String getAssetID() {
		return id;
	}
	@Override
	public JSONObject getMetadata() {
		return JSONObjectCache.parse(metadataString);
	}

	/**
	 * Gets the metadata for this asset as a String
	 * @return String
	 */
	@Override
	public String getMetadataString() {
		return metadataString;
	}

	@Override
	public abstract boolean isDataAsset();
	

}
