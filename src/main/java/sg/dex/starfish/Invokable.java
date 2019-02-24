package sg.dex.starfish;

import java.util.Map;

/**
 * Interface representing an invokable service.
 * 
 * @author Mike
 *
 */
public interface Invokable {

	/**
	 * Invokes the specified operation on this agent. If the invoke is successfully launched,
	 * will return a Job instance that can be used to access the result, otherwise throws an
	 * exception.
	 * 
	 * @param operation The operation to invoke on this agent
	 * @param params Positional parameters for the invoke operation
	 * @return A Job instance allowing access to the invoke job status and result
	 */
	public Job invoke(Operation operation, Asset... params);
	
	/**
	 * Gets the parameter specification for this Invokable service given the specified operation
	 *
	 * @throws UnsupportedOperationException if this service cannot support the given operation
	 * @param op
	 * @return A map of parameter names to parameter specs
	 */
	public default Map<String,Object> getParamSpec(Operation op) {
		return op.getParamSpec();
	}
}
