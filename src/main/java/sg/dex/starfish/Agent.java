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
	public String getDID();
	
	/**
	 * Registers asset metadata with this agent.
	 * The agent must support metadata storage.
	 */
	public Asset registerAsset(String metadata);
}
