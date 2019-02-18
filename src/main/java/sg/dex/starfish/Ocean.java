package sg.dex.starfish;

import org.json.simple.JSONObject;

/**
 * Main entry point for Ocean ecosystem
 *
 * @author Mike
 *
 */
public class Ocean {
	/**
	 * Create an instance of an Ocean object with the provided configuration
	 */
	public static Ocean connect() {
		return new Ocean();
	}

	/**
	 * Gets a DDO for a specified DID via the Universal resolver
	 * @param did DID to resolve
	 * @return JSONObject
	 */
	public static JSONObject getDDO(String did) {
		return getDDO(DID.parse(did));
	}

	/**
	 * Gets a DDO for a specified DID via the Universal resolver
	 * @param did DID to resolve
	 * @return JSONObject
	 */
	public static JSONObject getDDO(DID did) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
