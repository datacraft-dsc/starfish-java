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

	protected RemoteOperation(RemoteAgent remoteAgent, String meta) {
		super(meta,remoteAgent);
	}

    /**
     * API to create the remote operation instance by passing the remote agent and the metadata
     * @param a
     * @param meta
     * @return
     */
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
	public Map<String, Object> invokeResult(Map<String, Object> params) {
		return remoteAgent.invokeResult(this,params);
	}

	@Override
	public Job invoke(Map<String, Asset> params) {
		return remoteAgent.invoke(this,params);
	}


}
