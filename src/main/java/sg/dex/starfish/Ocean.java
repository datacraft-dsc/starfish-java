package sg.dex.starfish;

import java.util.HashMap;
import java.util.Map;

import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSONObjectCache;

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
	 *
	 * @return An Ocean instance with default configuration
	 */
	public static Ocean connect() {
		return DEFAULT_OCEAN;
	}

	private Map<DID, String> ddoCache = new HashMap<DID,String>();

	/**
	 * Gets a DDO for a specified DID via the Universal resolver
	 * @param did DID to resolve
	 * @return The DDO as a JSON map
	 */
	public Map<String,Object> getDDO(String did) {
		return getDDO(DID.parse(did));
	}

	/**
	 * Registers a DID within the context of this Ocean connection.
	 * @param did A did to register
	 * @param ddo A string containing a valid Ocean DDO
	 */
	public void registerLocalDID(DID did, String ddo) {
		ddoCache.put(did,ddo);
	}

	/**
	 * Gets a DDO for a specified DID via the Universal Resolver
	 *
	 * @param did DID to resolve
	 * @throws UnsupportedOperationException not yet implemented
	 * @return The DDO as a JSON map
	 */
	public Map<String,Object> getDDO(DID did) {
		String localDDO=ddoCache.get(did);
		if (localDDO!=null) {
			return JSONObjectCache.parse(localDDO);
		}
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
