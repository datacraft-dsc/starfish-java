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

	private JSONObject ddo;

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
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
