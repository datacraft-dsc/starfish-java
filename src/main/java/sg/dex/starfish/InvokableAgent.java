package sg.dex.starfish;

/**
 * Interface representing an invokable agent.
 * 
 * @author Mike
 *
 */
public interface InvokableAgent {

	/**
	 * Invokes the specified operation on this agent
	 * @param operation
	 * @param params
	 * @return A Job instance allowing access to the invoke job status and result
	 */
	public Job invoke(Operation operation, Asset... params);
}
