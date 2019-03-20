package sg.dex.starfish.exception;

/**
 * Exception class representing invalid access permission
 *
 * @author Tom
 *
 */
@SuppressWarnings("serial")
public class AuthorizationException extends RuntimeException {

	public AuthorizationException(String message, Exception e) {
		super(message,e);
	}

}
