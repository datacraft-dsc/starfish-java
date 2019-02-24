package sg.dex.starfish.impl.remote;

import java.util.Map;

import sg.dex.starfish.AOperation;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;

/**
 * Class representing a remote operation callable via the Invoke API
 * 
 * @author Mike
 *
 */
public class RemoteOperation extends AOperation {

	private InvokeAgent agent;

	protected RemoteOperation(InvokeAgent agent, String meta) {
		super(meta);
		this.agent=agent;
	}
	
	public static Asset create(InvokeAgent a, String meta) {
		return new RemoteOperation(a,meta);
	}

	@Override
	public Job invoke(Asset... params) {
		return agent.invoke(this,params);
	}

	@Override
	public Job invoke(Map<String, Asset> params) {
		return agent.invoke(this,params);
	}


}
