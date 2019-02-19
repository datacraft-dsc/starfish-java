package sg.dex.starfish;

/**
 * Interface representing an invokable agent.
 * 
 * @author Mike
 *
 */
public interface InvokableAgent {

	public Job invoke(Operation o, Asset... params);
}
