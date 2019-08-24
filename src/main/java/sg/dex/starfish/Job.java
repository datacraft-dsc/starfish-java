package sg.dex.starfish;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.JobFailedException;
import sg.dex.starfish.exception.StorageException;

/**
 * Interface representing a asynchronous job execution.
 * 
 * Jobs are typically executed via the Invoke API
 *
 * @author Mike
 * @version 0.5
 * @param <T> The type of result returned by the Job
 */
public interface Job<T> extends Future<T>{

	/**
	 * Gets the Job ID associated with this Job. Job IDs are allocated by the agent implementation
	 * responsible for completing the job.
	 *
	 * @return jobID
	 */
	public String getJobID();

	/**
	 * Returns true if the Job has been completed. If the job is complete, the result
	 * may be obtained via get() or getResult()
	 *
	 * @return boolean true if the job is complete, false otherwise.
	 */
	@Override
	public boolean isDone();

	/**
	 * Gets the result of the Job if available, or null if not yet available.
	 *
	 * @return The Asset resulting from the job, or null if not yet available
	 */
	public T pollResult();

	/**
	 * Waits for the result of the operation and returns the result
	 * WARNING: may never return if the Job does not complete
	 *
	 * @throws AuthorizationException if requestor does not have load permission
	 * @throws StorageException if unable to load the Asset
	 * @throws JobFailedException if Job fails
	 * @return The Asset resulting from the job
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@Override
	public default T get() throws InterruptedException, ExecutionException {
		try {
			return get(Long.MAX_VALUE,TimeUnit.MILLISECONDS);
		} catch (TimeoutException t) {
			throw new Error("Unexpected timeout!");
		}
	}
	
	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;

	/**
	 * Waits for the result of the Operation and returns the result 
	 * or returns null if the timeout in milliseconds expires before the
	 * asset is available.
	 *
	 * @param timeoutMillis The number of milliseconds to wait for a result before returning null
	 * @return The result from the Job
	 * @throws ExecutionException 
	 * @throws TimeoutException 
	 * @throws InterruptedException 
	 */
	public default T get(long timeoutMillis) throws InterruptedException, TimeoutException, ExecutionException {
		return get(timeoutMillis, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Convenience method to get the result of the Job without checked exceptions.
	 * 
	 * @param timeoutMillis Timeout to wait for the Jo result in milliseconds
	 * @return The result of the Job
	 */
	public default T getResult(long timeoutMillis) {
		try {
			return get(timeoutMillis, TimeUnit.MILLISECONDS);
		}
		catch (InterruptedException | TimeoutException | ExecutionException e) {
			throw new Error(e);
		}
	}

	
	/**
	 * Convenience method to get the result without checked exceptions.
	 * @return
	 */
	public default T getResult() {
		try {
			return get();
		}
		catch (InterruptedException | ExecutionException e) {
			throw new Error(e);
		}
	}
		
	@Override
	public default boolean cancel(boolean mayInterruptIfRunning) {
		// note: classes implementing Job should override this if they support Job cancellation
		return false;
	}

}
