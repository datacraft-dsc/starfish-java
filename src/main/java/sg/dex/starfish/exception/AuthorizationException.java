package sg.dex.starfish.exception;

/**
 * Exception class representing invalid access permission
 *
 * @author Tom
 *
 */
@SuppressWarnings("serial")
public class AuthorizationException extends RuntimeException {
	/**
	 * API to create Authorization Exception instance
	 * @param message message tht need to pass.
	 * @param e Exception
	 */
	public AuthorizationException(String message, Exception e) {
		super(message,e);
	}

	/**
	 * API to create Authorization Exception instance
	 * @param message message need to pass for the exception
	 */
	public AuthorizationException(String message) {
		super(message);
	}

}
