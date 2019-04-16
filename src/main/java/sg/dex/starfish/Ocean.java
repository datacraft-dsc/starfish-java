package sg.dex.starfish;

import java.util.HashMap;
import java.util.Map;

import com.oceanprotocol.squid.api.OceanAPI;

import sg.dex.starfish.exception.TODOException;
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
	public static final Ocean DEFAULT_OCEAN=new Ocean(null);
	
	private final Map<DID, String> ddoCache = new HashMap<DID,String>();

	private final OceanAPI oceanAPI;

	private Ocean(OceanAPI oceanAPI) {
		this.oceanAPI=oceanAPI;
	}
	
	/**
	 * Gets an instance of an Ocean object with the default configuration
	 *
	 * @return An Ocean instance with default configuration
	 */
	public static Ocean connect() {
		return DEFAULT_OCEAN;
	}
	
	/**
	 * Gets an instance of an Ocean object with the given OceanAPI instance.
	 *
	 * @return An Ocean instance with default configuration
	 */
	public static Ocean connect(OceanAPI oceanAPI) {
		return new Ocean(oceanAPI);
	}

	/**
	 * Gets a DDO for a specified DID via the Universal resolver
	 * 
	 * @param did DID to resolve
	 * @return The DDO as a JSON map
	 */
	public Map<String,Object> getDDO(String did) {
		return getDDO(DID.parse(did));
	}

	/**
	 * Registers a DID with a DDO in the context of this Ocean connection on the local machine.
	 * 
	 * This registration is intended for testing purposes.
	 * 
	 * @param did A did to register
	 * @param ddo A string containing a valid Ocean DDO
	 */
	public void registerLocalDID(DID did, String ddo) {
		ddoCache.put(did,ddo);
	}
	
	/**
	 * Registers an agent DDO with this Ocean connection,
	 * @return did The newly created DID for the agent
	 * @param ddo
	 */
	public DID registerDDO(String ddo) {
		DID did=DID.createRandom();
		registerLocalDID(did,ddo);
		return did;
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

	public Agent getAgent(DID did) {
		// TODO create an aggent instance according to the given DID
		throw new TODOException();
	}

}
