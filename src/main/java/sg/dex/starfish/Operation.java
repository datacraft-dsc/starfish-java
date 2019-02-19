package sg.dex.starfish;

/**
 * Interface representing an invokable operation
 * @author Mike
 *
 */
public interface Operation extends Asset {

	@Override 
	public default boolean isOperation() {
		return true;
	}
}
