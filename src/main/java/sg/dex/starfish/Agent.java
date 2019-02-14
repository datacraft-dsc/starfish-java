package sg.dex.starfish;

import org.json.simple.JSONObject;

/**
 * Interface representing an Agent in the Ocean ecosystem
 * @author Mike
 *
 */
public interface Agent {

	/**
	 * Gets the DDO for the agent
	 * @return
	 */
	public JSONObject getDDO();

	/**
	 * Gets the DID for an Agent
	 */
	public DID getDID();
	
	/**
	 * Registers asset with this agent.
	 * The agent must support metadata storage.
	 */
	public void registerAsset(Asset a);
	
	/**
	 * Gets an asset for the given asset ID from this agent.
	 * Returns null if the asset ID does not exist.
	 */
	public Asset getAsset(String id);
}
