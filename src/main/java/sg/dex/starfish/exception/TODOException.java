package sg.dex.starfish.exception;

@SuppressWarnings("serial")
public class TODOException extends RuntimeException {

	public TODOException() {
		super("Not yet implemented");
	}
	
	public TODOException(String message) {
		super(message);
	}
	
}
