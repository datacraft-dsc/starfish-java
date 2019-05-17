package sg.dex.starfish.exception;

/**
 * Exception class representing a failure during invoke job execution.
 * 
 * Intended for cases where the invoke was sucessfully launched, but terminated with
 * an error during the execution of the operation.
 * 
 * @author Mike
 *
 */
@SuppressWarnings("serial")
public class JobFailedException extends RuntimeException {


	public JobFailedException(String message, Exception e) {
		super(message,e);
	}
	public JobFailedException(String message) {
		super(message);
	}

}
