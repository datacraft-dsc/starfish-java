package sg.dex.starfish;

import org.json.simple.JSONObject;

import sg.dex.crypto.Hash;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Hex;
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
	 * Gets DID for this Asset
	 *
	 * @throws UnsupportedOperationException if unable to obtain DID
	 * @param a The asset to register
	 */
	@Override
	public DID getAssetDID() {
		throw new UnsupportedOperationException("Unable to obtain DID for asset of class: "+getClass());
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getParamValue() {
		JSONObject o=new JSONObject();
		// default is to pass the asset ID
		o.put("id", getAssetID());
		return o;
	}

	/**
	 * Gets the metadata for this asset as a String
	 * @return The metadata as a String
	 */
	@Override
	public String getMetadataString() {
		return metadataString;
	}

	@Override
	public abstract boolean isDataAsset();


}
