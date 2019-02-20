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
	 * @return boolean true if the Job has been completed
	 */
	public boolean isComplete();

	/**
	 * Gets the result of the job as an Ocean asset
	 *
	 * @return The Asset resulting from the job, or null if not available
	 */
	public Asset getResult();


	/**
	 * Waits for the result of the Operation and returns the result Asset
	 * WARNING: may never return if the job does not complete
	 *
	 * @return The Asset resulting from the job
	 */
	public Asset awaitResult();

	/**
	 * Waits for the result of the Operation and returns the result Asset
	 * or returns null if the timeout in milliseconds expires before the
	 * asset is available.
	 *
	 * @param timeoutMillis The number of milliseconds to wait for a result before returning null
	 * @return The Asset resulting from the job, or null if the timeout expires before the  job completes
	 */
	public Asset awaitResult(long timeoutMillis);
}
