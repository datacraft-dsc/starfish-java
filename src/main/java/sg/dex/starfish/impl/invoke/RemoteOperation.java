package sg.dex.starfish.impl.invoke;

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

	protected RemoteOperation(String meta) {
		super(meta);
	}
	
	public static Asset create(String meta) {
		return new RemoteOperation(meta);
	}

	@Override
	public Job invoke(Asset... params) {
		// TODO Auto-generated method stub
		return null;
	}


}
