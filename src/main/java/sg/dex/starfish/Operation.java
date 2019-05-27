package sg.dex.starfish;

import sg.dex.starfish.util.Params;

import java.util.Map;

/**
 * Interface representing an invokable operation
 * 
 * @author Mike
 * @version 0.5
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
	public default Job invoke(Asset... params) {
		return invoke(Params.namedParams(this, params));
	}

	/**
	 * Invokes this operation with the given named parameters. Operations should
	 * override this method to provide an implementation of asynchronous invocation
	 * via the Job interface
	 * 
	 * @param params Positional parameters for this invoke job
	 * @throws IllegalArgumentException if required parameters are not available, or
	 *             of incorrect type
	 * @return The Job for this invoked operation
	 */
	public Job invokeAsync(Map<String, Object> params);

	/**
	 * Invokes this operation with the given named parameters. Operations should
	 * override this method to provide an implementation of asynchronous invocation
	 * via the Job interface
	 *
	 * @param params Positional parameters for this invoke job
	 * @throws IllegalArgumentException if required parameters are not available, or
	 *             of incorrect type
	 * @return The Job for this invoked operation
	 */
	public Map<String, Object> invokeResult(Map<String, Object> params);

	/**
	 * Invokes this operation with the given named parameters. Operations should
	 * override this method to provide an implementation of asynchronous invocation
	 * via the Job interface
	 *
	 * @param params Positional parameters for this invoke job
	 * @throws IllegalArgumentException if required parameters are not available, or
	 *             of incorrect type
	 * @return The Job for this invoked operation
	 */
	public Job invoke(Map<String, Object> params);

	/**
	 * Returns the operation specification for this operation. Operations must
	 * override this method to define what operation input and result  they accept.
	 * 
	 * TODO: add brief description of format and link to DEP6
	 *
	 * @return A map of parameter names to specifications
	 */
	@SuppressWarnings("unchecked")
	public default Map<String, Object> getOperationSpec() {
		return  (Map<String, Object>) getMetadata().get("operation");
	}
	/**
	 * Returns the parameter specification for this operation. Operations must
	 * override this method to define what parameters they accept.
	 *
	 * TODO: add brief description of format and link to DEP6
	 *
	 * @return A map of parameter names to specifications
	 */
	@SuppressWarnings("unchecked")
	public default Map<String, Object> getParamsSpec() {
		return (Map<String, Object> ) getOperationSpec().get("params");
	}

}
