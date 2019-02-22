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

	/**
	 * Invokes this operation on this agent. If the invoke is successfully launched,
	 * will return a Job instance that can be used to access the result, otherwise throws an
	 * exception.
	 *
	 * @param params Positional parameters for the invoke operation
	 * @throws IllegalArgumentException if required parameters are not available.
	 * @return A Job instance allowing access to the invoke job status and result
	 */
	@Override
	public Job invoke(Asset... params) {
		// TODO Auto-generated method stub
		return null;
	}


}
