package sg.dex.starfish;



/**
 * Interface representing an invokable agent.
 *
 * @author Mike
 *
 */
public interface InvokableAgent {

	/**
	 * Invokes the specified operation on this agent. If the invoke is successfully launched,
	 * will return a Job instance that can be used to access the result, otherwise throws an
	 * exception.
	 *
	 * @param operation The operation to invoke on this agent
	 * @param params Positional parameters for the invoke operation
	 * @throws IllegalArgumentException if required parameters are not available.
	 * @return A Job instance allowing access to the invoke job status and result
	 */
	public Job invoke(Operation operation, Asset... params);
}
