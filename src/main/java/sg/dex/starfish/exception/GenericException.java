package sg.dex.starfish.exception;

@SuppressWarnings("serial")
public class GenericException extends RuntimeException {

    public GenericException(String message, Exception e) {
        super(message,e);
    }

    public GenericException(String msg){
        super(msg);
    }

}
