package sg.dex.starfish;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import sg.dex.crypto.Hash;
import sg.dex.crypto.Hex;

/**
 * Abstract base class for asset implementations
 * @author Mike
 *
 */
public abstract class AAsset implements Asset {
	protected final String metadataString;
	protected final String id;
	protected final Agent agent;

	protected AAsset(Agent agent, String meta) {
		this.agent=agent;
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
		JSONParser parser=new JSONParser();
		try {
			return (JSONObject) parser.parse(metadataString);
		} catch (ParseException e) {
			throw new Error("Error in JSON parson",e);
		}
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
