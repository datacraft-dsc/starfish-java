package sg.dex.starfish;

import org.json.simple.JSONObject;

import sg.dex.starfish.util.DID;

/**
 * Main entry point for Ocean ecosystem.
 *
 * An instance of the Ocean class is used to represent a connection to the Ocean network
 * and supports functionality related to accessing the on-chain state of the Ocean network.
 *
 * @author Mike
 *
 */
public class Ocean {

	public static final Ocean DEFAULT_OCEAN=new Ocean();

	/**
	 * Gets an instance of an Ocean object with the default configuration
	 * @return Ocean
	 */
	public static Ocean connect() {
		return DEFAULT_OCEAN;
	}

	/**
	 * Gets a DDO for a specified DID via the Universal resolver
	 * @param did DID to resolve
	 * @return JSONObject
	 */
	public JSONObject getDDO(String did) {
		return getDDO(DID.parse(did));
	}

	/**
	 * Gets a DDO for a specified DID via the Universal resolver
	 * @param did DID to resolve
	 * @return JSONObject
	 */
	public JSONObject getDDO(DID did) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
