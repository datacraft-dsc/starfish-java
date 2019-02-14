package sg.dex.starfish;

import org.json.simple.JSONObject;

/**
 * Class representing an Agent in the Ocean Ecosystem
 * 
 * Agents are addressed with a W3C DID
 * 
 * @author Mike
 *
 */
public abstract class AAgent implements Agent {

	private final String did;
	
	private JSONObject ddo;

	private AAgent(String did) {
		this.did=did;
	}
	
	@Override public String getDID() {
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
	 * @return
	 */
	public JSONObject refreshDDO() {
		return Ocean.getDDO(did);
	}
}
