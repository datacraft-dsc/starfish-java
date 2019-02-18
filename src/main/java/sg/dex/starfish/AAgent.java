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

	protected final DID did;

	private JSONObject ddo;

	protected AAgent(String did) {
		this.did=DID.parse(did);
	}

	public AAgent(DID did) {
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
		return Ocean.getDDO(did);
	}
}
