package sg.dex.starfish.impl.remote;

import sg.dex.starfish.AAgent;
import sg.dex.starfish.Asset;
import sg.dex.starfish.DID;
import sg.dex.starfish.Ocean;

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
		throw new UnsupportedOperationException();
	}

	@Override
	public Asset getAsset(String id) {
		return RemoteAsset.create(this,id);
	}

	@Override
	public Asset uploadAsset(Asset a) {
		// TODO Auto-generated method stub
		return null;
	}

}
