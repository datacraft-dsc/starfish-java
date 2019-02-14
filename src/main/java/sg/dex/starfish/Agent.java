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

}