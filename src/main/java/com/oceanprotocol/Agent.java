package com.oceanprotocol;

import org.json.simple.JSONObject;

/**
 * Class representing an Agent in the Ocean Ecosystem
 * 
 * Agents are addressed with a W3C DID
 * 
 * @author Mike
 *
 */
public class Agent {

	private final String did;
	
	private JSONObject ddo;

	private Agent(String did) {
		this.did=did;
	}
	
	/**
	 * Gets the cached DDO for the agent
	 * Fetches the latest DDO from Universal Resolver if not cached
	 * @return
	 */
	public JSONObject getDDO() {
		if (ddo==null) {
			ddo=refreshDDO();
		}
		return ddo;
	}
	
	/**
	 * Fetches the latest DDO from Universal Resolver if not cached
	 * @return
	 */
	public JSONObject refreshDDO() {
		return Ocean.getDDO(did);
	}
}
