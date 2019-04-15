package sg.dex.starfish.exception;

/**
 * Class representing an unexpected failure in a remote operation
 * @author Mike
 *
 */
@SuppressWarnings("serial")
public class RemoteException extends RuntimeException {

	public RemoteException(String message) {
		super(message);
	}
	public RemoteException(String message,Throwable e) {
		super(message,e);
	}

}
