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
    private RemoteAgent remoteAgent;

	protected RemoteOperation(RemoteAgent remoteAgent, String meta) {
		super(meta);
		this.remoteAgent=remoteAgent;
	}
	
	public static RemoteOperation create(RemoteAgent a, String meta) {
		return new RemoteOperation(a,meta);
	}

	@Override
	public Job invoke(Asset... params) {
		return remoteAgent.invoke(this,params);
	}

	@Override
	public Job invokeAsync(Map<String, Asset> params) {
		return remoteAgent.invoke(this,params);
	}

	@Override
	public Map<String, Object> invokeResult(Map<String, Asset> params) {
		return remoteAgent.invokeResult(params);
	}

	@Override
	public Job invoke(Map<String, Asset> params) {
		return remoteAgent.invoke(this,params);
	}


}
