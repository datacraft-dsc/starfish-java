package sg.dex.starfish.impl;

import sg.dex.starfish.Agent;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.util.DID;

import java.util.List;
import java.util.Map;

/**
 * Class representing an Agent in the Ocean Ecosystem
 *
 * Agents are addressed with a W3C DID
 *
 * @author Mike
 * @version 0.5
 *
 */
public abstract class AAgent implements Agent {

	protected final DID did;

	private Map<String,Object> ddo;

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
	public Map<String,Object> getDDO() {
		if (ddo==null) {
			ddo=refreshDDO();
		}
		return ddo;
	}

	/**
	 * Fetches the latest DDO from Universal Resolver if not cached
	 * @return JSONObject
	 */
	public Map<String,Object> refreshDDO() {
		return ocean.getDDO(did);
	}
	
	/**
	 * Returns the serviceEndpoint for the specified service type.
	 * Searched the agent's DDO for the appropriate service.
	 * 
	 * @param type The type of the service to find
	 * @return The service endpoint, or null if not found
	 */
	@SuppressWarnings("unchecked")
	public String getEndpoint(String type) {
		Map<String,Object> ddo=getDDO();
		List<Object> services = (List<Object>) ddo.get("service");
		if (services==null) return null;
		for (Object o: services) {
			Map<String,Object> service=(Map<String,Object>)o;
			if (type.equals(service.get("type"))) return (String) service.get("serviceEndpoint");
		}
		return null;
	}


}
