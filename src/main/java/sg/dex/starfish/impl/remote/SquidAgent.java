package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Ocean;
import sg.dex.starfish.util.DID;

import com.oceanprotocol.squid.api.OceanAPI;

/**
 * Class implementing a remote storage agent using Squid
 *
 * @author Tom
 *
 */
public class SquidAgent extends RemoteAgent {

	private OceanAPI oceanAPI;

	/**
	 * Creates a SquidAgent with the specified OceanAPI, Ocean connection and DID
	 *
	 * @param oceanapi Squid OceanAPI to use
	 * @param ocean Ocean connection to use
	 * @param did DID for this agent
	 */
	protected SquidAgent(OceanAPI oceanAPI, Ocean ocean, DID did) {
		super(ocean, did);
		this.oceanAPI = oceanAPI;
	}

	/**
	 * Creates a RemoteAgent with the specified OceanAPI, Ocean connection and DID
	 *
	 * @param oceanapi Squid OceanAPI to use
	 * @param ocean Ocean connection to use
	 * @param did DID for this agent
	 * @return RemoteAgent
	 */
	public static SquidAgent create(OceanAPI oceanAPI, Ocean ocean, DID did) {
		return new SquidAgent(oceanAPI, ocean, did);
	}

}
