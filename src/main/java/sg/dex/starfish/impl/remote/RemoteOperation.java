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
     * @param a
     * @param meta
     * @return
     */
	public static RemoteOperation create(RemoteAgent a, String meta) {

		return new RemoteOperation(a,meta,null);
	}

    /**
     * API to materialize the operation which is available on remote server
     * @param agent
     * @param assetId
     * @param invokeAgent
     * @return
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
	public Job invoke(Map<String, Asset> params) {
		return remoteAgent.invoke(this,params);
	}
//	@Override
//	public String getAssetID() {
//		return "464d5b1ec3018f95edb9a3359245ff4590ed391d1c9f5020c8ec8c0159fb4e6e";
//	}
//    @Override
//    public Map<String, Object> getParamValue(){
//	    return this.getParamSpec().get("params");
//
//    }

}
