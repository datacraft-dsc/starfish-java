package sg.dex.starfish.exception;

/**
 * Exception class covering Resolver related exceptions
 *
 * @author Ilia
 */
public class DexChainException extends RuntimeException {
    /**
     * API to create Resolver Exception instance
     *
     * @param e Throwable
     */
    public DexChainException(Throwable e) {
        super(e);
    }

    /**
     * API to create Resolver Exception instance
     */
    public DexChainException() {
        super();
    }

    public DexChainException(String message) {
        super(message);
    }
}
