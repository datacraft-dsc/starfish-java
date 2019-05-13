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

    private RemoteAgent invokeAgent;
	protected RemoteOperation(RemoteAgent remoteAgent, String meta,RemoteAgent invokeAgent) {

		super(meta,remoteAgent);
        this.invokeAgent=invokeAgent;
	}

    /**
     * API to create the remote operation instance by passing the remote agent and the metadata
     * @param a agent on which this operation instance needs to be created
     * @param meta meta data for creating remote operation instance
     * @return new Remote operation instance
     */
	public static RemoteOperation create(RemoteAgent a, String meta) {

		return new RemoteOperation(a,meta,null);
	}

    /**
     * API to materialize the operation which is available on remote server
     * @param agent remote agent where asset is register
     * @param assetId asset id of the operation
     * @param invokeAgent remote agent where the operation need to be invoke
     * @return new Remote operation instance
     */
	public static RemoteOperation materialize(RemoteAgent agent,String assetId,RemoteAgent invokeAgent) {
		Asset a = agent.getAsset(assetId);

		return new RemoteOperation(agent,a.getMetadataString(),invokeAgent);
	}

	@Override
	public Job invoke(Asset... params) {
		return remoteAgent.invoke(this,params);
	}

	@Override
	public Job invokeAsync(Map<String, Object> params) {
		return invokeAgent.invokeAsync(this,params);
	}

	@Override
	public Map<String, Object> invokeResult(Map<String, Object> params) {
		return invokeAgent.invokeResult(this,params);
	}

	@Override
	public Job invoke(Map<String, Object> params) {
		return remoteAgent.invoke(this,params);
	}

}
