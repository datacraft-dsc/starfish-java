package sg.dex.starfish.exception;

/**
 * Class representing a failure in a remote operation
 * @author Mike
 *
 */
public class RemoteException extends RuntimeException {

	public RemoteException(String message) {
		super(message);
	}
	public RemoteException(String message,Throwable e) {
		super(message,e);
	}

}
