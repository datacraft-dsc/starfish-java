package sg.dex.starfish.exception;

/**
 * Exception class representing a failure of Asset storage
 *
 * @author Tom
 */
@SuppressWarnings("serial")
public class StorageException extends RuntimeException {

    public StorageException(String message, Exception e) {
        super(message, e);
    }

}
