package sg.dex.starfish.util;

/**
 * Class representing a failure in a remote operation
 * @author Mike
 *
 */
@SuppressWarnings("serial")
public class RemoteException extends RuntimeException {

	public RemoteException(String message) {
		super(message);
	}

}
