package sg.dex.starfish.exception;

@SuppressWarnings("serial")
public class GenericException extends RuntimeException {

    /**
     * Constructor to create the Generic Exception instance
     *
     * @param message message need to pass for generic exception
     * @param e       Exception
     */
    public GenericException(String message, Exception e) {
        super(message, e);
    }

    /**
     * Constructor to create the Generic Exception instance
     *
     * @param msg message need to pass for generic exception
     */
    public GenericException(String msg) {
        super(msg);
    }

}
