package sg.dex.starfish;

import org.json.simple.JSONObject;

import sg.dex.starfish.util.DID;

/**
 * Class representing an Agent in the Ocean Ecosystem
 *
 * Agents are addressed with a W3C DID
 *
 * @author Mike
 *
 */
public abstract class AAgent implements Agent {

	protected final DID did;

	private JSONObject ddo;

	protected final Ocean ocean;

	/**
	 * Create an agent with the provided Ocean connection and DID
	 * 
	 * @param ocean The ocean connection to use for this agent
	 * @param did The DID for this agent
	 */
	protected AAgent(Ocean ocean, DID did) {
		this.ocean=ocean;
		this.did=did;
	}

	/**
	 * Create an agent with the provided Ocean connection and DID
	 * @param did The DID for this agent
	 */
	protected AAgent(DID did) {
		this.ocean=Ocean.connect();
		this.did=did;
	}

	@Override public DID getDID() {
		return did;
	}

	@Override
	public JSONObject getDDO() {
		if (ddo==null) {
			ddo=refreshDDO();
		}
		return ddo;
	}

	/**
	 * Fetches the latest DDO from Universal Resolver if not cached
	 * @return JSONObject
	 */
	public JSONObject refreshDDO() {
		return ocean.getDDO(did);
	}
}
