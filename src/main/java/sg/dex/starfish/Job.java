package sg.dex.starfish;

/**
 * Interface representing a job executed via the Invoke API
 * 
 * @author Mike
 *
 */
public interface Job {

	/**
	 * Returns true if the Job has been completed
	 * @return
	 */
	public boolean isComplete();
	
	/**
	 * Gets the result of the job as an Ocean asset
	 * @return
	 */
	public Asset getResult();
}
