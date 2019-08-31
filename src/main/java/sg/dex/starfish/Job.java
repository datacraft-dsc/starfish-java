package sg.dex.starfish;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.JobFailedException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.util.Utils;

/**
 * Interface representing an asynchronous Job execution.
 * 
 * Job extends the Future interface, but adds additional functionality relevant
 * to invokable services in the data ecosystem.
 * 
 * Jobs are typically executed via the Invoke API
 *
 * @author Mike
 * @version 0.5
 */
public interface Job extends Future<Map<String, Object>> {

	// maximum timeout kept sufficiently below Long.MAXVALUE to avoid risk of overflow
	long MAX_TIMEOUT_MILLIS = Long.MAX_VALUE/4;

	/**
	 * Gets the Job ID associated with this Job. Job IDs are allocated by the agent
	 * implementation responsible for completing the job, and may be used to refer
	 * to the Job via other mechanisms.
	 *
	 * @return jobID
	 */
	public String getJobID();

	/**
	 * Returns true if the Job has been completed. If the job is complete, the
	 * result may be obtained via get() or getResult()
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
	public Map<String, Object> pollResult();

	/**
	 * Waits for the result of the operation and returns the result WARNING: may
	 * never return if the Job does not complete
	 *
	 * @throws AuthorizationException if requestor does not have load permission
	 * @throws StorageException if unable to load the Asset
	 * @throws JobFailedException if Job fails
	 * @return The Asset resulting from the job
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@Override
	public default Map<String, Object> get() throws InterruptedException, ExecutionException {
		try {
			return get(MAX_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
		}
		catch (TimeoutException t) {
			throw Utils.sneakyThrow(t);
		}
	}
	
	/**
	 * Convenience function to get a value from the Job result map
	 * 
	 * @param <R> The expected return type from the Job result
	 * @param key The key to look up in the Job map
	 * @return The value from the Job result map, or null if not present.
	 */
	@SuppressWarnings("unchecked")
	public default <R> R get(String key) {
		try {
			return (R) (get().get(key));
		}
		catch (Throwable e) {
			throw Utils.sneakyThrow(e);
		}
	}

	@Override
	public Map<String, Object> get(long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException;

	/**
	 * Waits for the result of the Operation and returns the result or returns null
	 * if the timeout in milliseconds expires before the asset is available.
	 *
	 * @param timeoutMillis The number of milliseconds to wait for a result before
	 *            returning null
	 * @return The result from the Job
	 * @throws ExecutionException
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */
	public default Map<String, Object> get(long timeoutMillis)
			throws InterruptedException, TimeoutException, ExecutionException {
		return get(timeoutMillis, TimeUnit.MILLISECONDS);
	}

	/**
	 * Convenience method to get the result of the Job without checked exceptions.
	 * 
	 * @param timeoutMillis Timeout to wait for the Jo result in milliseconds
	 * @return The result of the Job
	 */
	public default Map<String, Object> getResult(long timeoutMillis) {
		try {
			return get(timeoutMillis, TimeUnit.MILLISECONDS);
		}
		catch (InterruptedException | TimeoutException | ExecutionException e) {
			throw new Error(e);
		}
	}

	/**
	 * Convenience method to get the Job result without checked exceptions.
	 * 
	 * @return The result of the Job
	 */
	public default Map<String, Object> getResult() {
		try {
			return get();
		}
		catch (InterruptedException | ExecutionException e) {
			throw Utils.sneakyThrow(e);
		}
	}

	/**
	 * Attempts to cancel execution of this Job. This attempt will fail if the Job
	 * has already completed, has already been cancelled, or could not be cancelled
	 * for some other reason. If successful, and this task has not started when
	 * {@code cancel} is called, this Job should never run.
	 * 
	 * @return {@code false} if the Job could not be cancelled, typically because it
	 *         has already completed normally; {@code true} otherwise
	 */
	public default boolean cancel() {
		return cancel(true);
	}

	/**
	 * Attempts to cancel execution of this Job. This attempt will fail if the Job
	 * has already completed, has already been cancelled, or could not be cancelled
	 * for some other reason. If successful, and this task has not started when
	 * {@code cancel} is called, this Job should never run.
	 * 
	 * If the Job has already started, then the {@code mayInterruptIfRunning}
	 * parameter determines whether the an attempt should be made to stop the Job
	 * (e.g. via interrupting a running thread, or by sending a message to cancel to
	 * a remote agent)
	 * 
	 * @param mayInterruptIfRunning {@code true} if an attempt should be made to
	 *            interrupt and already-running Job; otherwise, in-progress tasks
	 *            are allowed to complete
	 * @return {@code false} if the Job could not be cancelled, typically because it
	 *         has already completed normally; {@code true} otherwise
	 */
	@Override
	public default boolean cancel(boolean mayInterruptIfRunning) {
		// note: classes implementing Job should override this if they support Job
		// cancellation
		return false;
	}

}
