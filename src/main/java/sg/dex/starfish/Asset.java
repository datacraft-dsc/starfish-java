package sg.dex.starfish;

import org.json.simple.JSONObject;

/**
 * Interface representing an asset
 * 
 * @author Mike
 */
public interface Asset {

	/**
	 * Gets the JSON metadata for this asset
	 * @return
	 */
	public JSONObject getMetadata();

	public boolean isDataAsset();
}
