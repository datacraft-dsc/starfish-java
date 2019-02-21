package sg.dex.starfish.impl.remote;

import sg.dex.starfish.AAgent;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.TODOException;

/**
 * Class implementing a remote storage agent using the Storage API
 * @author Mike
 *
 */
public class RemoteAgent extends AAgent {

	protected RemoteAgent(Ocean ocean, DID did) {
		super(ocean, did);
	}

	@Override
	public void registerAsset(Asset a) {
		throw new TODOException();
	}

	@Override
	public Asset getAsset(String id) {
		throw new TODOException();
	}

	@Override
	public Asset uploadAsset(Asset a) {
		throw new TODOException();
	}

	/**
	 * Gets a URL string for accessing the specified asset ID
	 * @param id for Asset
	 * @return String URL for the Asset
	 */
	public String getAssetURL(String id) {
		throw new TODOException();
	}

}
