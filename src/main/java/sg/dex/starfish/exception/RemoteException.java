package sg.dex.starfish.exception;

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
