package sg.dex.starfish;

import java.util.Map;

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

	/**
	 * Invokes this operation with the given positional parameters.
	 *
	 * @param params Positional parameters for this invoke job
	 * @throws IllegalArgumentException if required parameters are not available.
	 * @return The Job for this invoked operation
	 */
	public Job invoke(Asset... params);
	
	/**
	 * Invokes this operation with the given named parameters. Operations should override
	 * this method to provide an implementation of asynchronous invocation via the
	 * Job interface
	 * 
	 * @param params Positional parameters for this invoke job
	 * @throws IllegalArgumentException if required parameters are not available, or of incorrect type
	 * @return The Job for this invoked operation
	 */
	public Job invoke(Map<String,Asset> params);

	/**
	 * Returns the parameter specification for this operation. Operations must
	 * override this method to define what parameters they accept.
	 * 
	 * TODO: add brief description of format and link to DEP6
	 *
	 * @return A map of parameter names to specifications
	 */
	public Map<String, Object> getParamSpec();

}
