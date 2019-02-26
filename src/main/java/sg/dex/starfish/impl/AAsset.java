package sg.dex.starfish.impl;

import java.util.HashMap;
import java.util.Map;

import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
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
	public Map<String,Object> getMetadata() {
		return JSONObjectCache.parse(metadataString);
	}
	
	/**
	 * Gets the DID for this Asset
	 *
	 * @throws UnsupportedOperationException if unable to obtain DID
	 * @return DID
	 */
	@Override
	public DID getAssetDID() {
		throw new UnsupportedOperationException("Unable to obtain DID for asset of class: "+getClass()); 
	}
	
	@Override
	public Map<String,Object> getParamValue() {
		 Map<String,Object>  o=new HashMap<>();
		// default is to pass the asset ID
		o.put("did", getAssetDID());
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
