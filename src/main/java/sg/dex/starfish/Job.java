package sg.dex.starfish;

import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.JobFailedException;
import sg.dex.starfish.exception.StorageException;

/**
 * Interface representing a job executed via the Invoke API
 *
 * @author Mike
 * @version 0.5
 */
public interface Job<T> {

	/**
	 * Gets the Job ID associated with this Job. Job IDs are allocated by the agent implementation
	 * responsible for completing the job.
	 *
	 * @return jobID
	 */
	public String getJobID();

	/**
	 * Returns true if the Job has been completed. If the job is complete, the result
	 * may be obtained via getResult()
	 *
	 * @return boolean true if the job is complete, false otherwise.
	 */
	public boolean isComplete();

	/**
	 * Gets the result of the job as an Ocean asset
	 *
	 * @throws AuthorizationException if requestor does not have load permission
	 * @throws StorageException if unable to load the Asset
	 * @throws JobFailedException if the job failed during execution
	 * @return The Asset resulting from the job, or null if not yet available
	 */
	public T getResult();


	/**
	 * Waits for the result of the Operation and returns the result Asset
	 * WARNING: may never return if the job does not complete
	 *
	 * @throws AuthorizationException if requestor does not have load permission
	 * @throws StorageException if unable to load the Asset
	 * @throws JobFailedException if Job fails
	 * @return The Asset resulting from the job
	 */
	public default T awaitResult() {
		return awaitResult(Long.MAX_VALUE);
	}

	/**
	 * Waits for the result of the Operation and returns the result Asset
	 * or returns null if the timeout in milliseconds expires before the
	 * asset is available.
	 *
	 * Implementations may throw these exceptions:
	 * throws TimeoutException if result is not available in timeoutMillis
	 * throws AuthorizationException if requestor does not have load permission
	 * throws StorageException if unable to load the Asset
	 * throws JobFailedException if Job fails
	 * @param timeoutMillis The number of milliseconds to wait for a result before returning null
	 * @return The Asset resulting from the job, or null if the timeout expires before the  job completes
	 */
	public T awaitResult(long timeoutMillis);
}
