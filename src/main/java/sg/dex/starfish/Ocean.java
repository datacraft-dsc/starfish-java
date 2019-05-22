package sg.dex.starfish;

import com.oceanprotocol.squid.api.AccountsAPI;
import com.oceanprotocol.squid.api.AssetsAPI;
import com.oceanprotocol.squid.api.OceanAPI;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.squid.SquidAsset;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSONObjectCache;

import java.util.HashMap;
import java.util.Map;

/**
 * Main entry point for Ocean ecosystem.
 *
 * An instance of the Ocean class is used to represent a connection to the Ocean network
 * and supports functionality related to accessing the on-chain state of the Ocean network.
 *
 * @author Mike
 * @version 0.5
 */
public class Ocean {
	private static final Ocean DEFAULT_OCEAN=new Ocean(null);
	
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
	 * @param oceanAPI OceanAPI instance to create a connection with
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
	 * @param ddo associated with the DID
	 */
	public DID registerDDO(String ddo) {
		DID did=DID.createRandom();
		registerLocalDID(did,ddo);
		return did;
	}
 
	/**
	 * Gets a DDO for a specified DID via the Universal Resolver.
	 * Returns null if the DDO cannot be found.
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
		// TODO universal resolver
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	/**
	 * Gets the Squid OceanAPI for this Ocean connection
	 * @return OceanAPI instance
	 */
	public OceanAPI getOceanAPI() {
		return oceanAPI;
	}
	
	/**
	 * Gets the Squid AssetsAPI for this Ocean connection
	 * @return AssetsAPI instance
	 */
	public AssetsAPI getAssetsAPI() {
		return oceanAPI.getAssetsAPI();
	}
	
	/**
	 * Gets the Squid AccountsAPI for this Ocean connection
	 * @return AccountsAPI instance
	 */
	public AccountsAPI getAccountsAPI() {
		return oceanAPI.getAccountsAPI();
	}

	/**
	 * Gets the agent for a given DID
	 * @param did The DID for the agent to resolve
	 * @return Agent instance, or null if not able to resolve the DID
	 */
	public Agent getAgent(DID did) {
		// TODO: resolve DDO for squid
		return RemoteAgent.create(this, did);
	}

	/**
	 * Attempts to resolve an asset for a given DID
	 * 
	 * @param did The DID
	 * @return The Asset for the given DID, or null if not found
	 */
	public Asset getAsset(DID did) {
		if (did.getPath()==null) {
			// resolve using Squid
			return SquidAsset.create(this,did);
		} else {
			// resolve using DEP protocol
			Agent ag=getAgent(did);
			return ag.getAsset(did);
		}
	}



}
