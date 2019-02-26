package sg.dex.starfish.impl.remote;

import java.util.Map;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.impl.AOperation;

/**
 * Class representing a remote operation callable via the Invoke API
 * 
 * @author Mike
 *
 */
public class RemoteOperation extends AOperation {

	private RemoteAgent agent;

	protected RemoteOperation(RemoteAgent agent, String meta) {
		super(meta);
		this.agent=agent;
	}
	
	public static Asset create(RemoteAgent a, String meta) {
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
