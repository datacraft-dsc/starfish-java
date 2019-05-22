package sg.dex.starfish;

import java.util.Map;

/**
 * Interface representing an invokable service.
 * 
 * @author Mike
 * @version 0.5
 */
public interface Invokable<T> {

	/**
	 * Invokes the specified operation on this agent. If the invoke is successfully launched,
	 * will return a Job instance that can be used to access the result, otherwise throws an
	 * exception.
	 * 
	 * @param operation The operation to invoke on this agent
	 * @param params Positional parameters for the invoke operation
	 * @return A Job instance allowing access to the invoke job status and result
	 */
	public Job invoke(Operation operation, T... params);
	
	/**
	 * Invokes the specified operation on this agent. If the invoke is successfully launched,
	 * will return a Job instance that can be used to access the result, otherwise throws an
	 * exception.
	 * 
	 * @param operation The operation to invoke on this agent
	 * @param params Named parameters for the invoke operation
	 * @return A Job instance allowing access to the invoke job status and result
	 */
	public Job invoke(Operation operation, Map<String,Object> params);
	
	/**
	 * Gets the parameter specification for this Invokable service given the specified operation
	 * 
	 * @param op The operation for which to obtain the parameter specification
	 *
	 * @throws UnsupportedOperationException if this service cannot support the given operation
	 * @return A map of parameter names to parameter specs
	 */
	public default Map<String,Object> getParamSpec(Operation op) {
		return op.getParamsSpec();
	}
	/**
	 * Invokes this operation with the given named parameters. Operations should override
	 * this method to provide an implementation of asynchronous invocation via the
	 * Job interface
	 *
	 * @param params Positional parameters for this invoke job
	 * @param operation The operation for which to obtain the parameter specification
	 * @throws IllegalArgumentException if required parameters are not available, or of incorrect type
	 * @return The Job for this invoked operation
	 */
	public Job invokeAsync(Operation operation,Map<String,Object> params);
}
