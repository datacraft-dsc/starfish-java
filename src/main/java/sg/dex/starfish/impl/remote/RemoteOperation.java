package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;

import java.util.Map;

/**
 * Class representing a remote operation callable via the Invoke API
 * 
 * @author Mike
 *
 */
public class RemoteOperation extends ARemoteAsset implements Operation {


	protected RemoteOperation(RemoteAgent agent, String meta) {
		super(meta,agent);
	}
	
	public static Asset create(RemoteAgent a, String meta) {
		return new RemoteOperation(a,meta);
	}

	@Override
	public Job invoke(Asset... params) {
		return getRemoteAgent().invoke(this,params);
	}

	@Override
	public Job invoke(Map<String, Asset> params) {
		return getRemoteAgent().invoke(this,params);
	}


}
