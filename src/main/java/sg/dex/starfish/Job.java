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
	
	
	/**
	 * Waits for the result of the Operation and returns the result Asset
	 * WARNING: may never return if the job does not complete 
	 */
	public Asset awaitResult();
	
	/**
	 * Waits for the result of the Operation and returns the result Asset
	 * or returns null if the timeout in milliseconds expires before the
	 * asset is available.
	 */
	public Asset awaitResult(long timeoutMillis);
}
