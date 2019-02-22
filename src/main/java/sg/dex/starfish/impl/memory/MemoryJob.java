package sg.dex.starfish.impl.memory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.util.JobFailedException;
import sg.dex.starfish.util.AuthorizationException;
import sg.dex.starfish.util.StorageException;

/**
 * Class representing a job being conducted asynchronously in the local JVM.
 *
 * A memory job will either:
 * - Be in progress (getResult return null)
 * - Complete normally (getResult returns an Asset)
 * - Fail with some exception (getResult throws an exception)
 *
 * It is possible that a memory job will never complete.
 *
 * @author Mike
 *
 */
public class MemoryJob implements Job {

	private final Future<Asset> future;

	private MemoryJob(Future<Asset> future) {
		this.future=future;
	}

	/**
	 * Create a MemoryJob instance using the provided Future.
	 *
	 * @param future A Future to be used to complete this job
	 * @return A MemoryJob instance encapsulation the provided future
	 */
	public static MemoryJob create(Future<Asset> future) {
		return new MemoryJob(future);
	}

	@Override
	public boolean isComplete() {
		return future.isDone();
	}

	/**
	 * Gets the result of the job as an Ocean asset
	 *
	 * @throws AuthorizationException if requestor does not have load permission
	 * @throws StorageException if unable to load the Asset
	 * @throws JobFailedException if Job fails
	 * @return The Asset resulting from the job, or null if not available
	 */
	@Override
	public Asset getResult() {
		try {
			return future.isDone()?future.get():null;
		}
		catch (InterruptedException e) {
			throw new Error("Job interruped",e);
		}
		catch (ExecutionException e) {
			throw new Error("Job failed with exception: ",e.getCause());
		}
	}

	/**
	 * Waits for the result of the Operation and returns the result Asset
	 * WARNING: may never return if the job does not complete
	 *
	 * @throws AuthorizationException if requestor does not have load permission
	 * @throws StorageException if unable to load the Asset
	 * @throws JobFailedException if Job fails
	 * @return The Asset resulting from the job
	 */
	@Override
	public Asset awaitResult() {
		try {
			return future.get();
		}
		catch (InterruptedException e) {
			throw new Error("Job interruped",e);
		}
		catch (ExecutionException e) {
			Throwable cause=e.getCause();
			throw new Error("Job failed with exception: "+cause,e);
		}
	}

	/**
	 * Waits for the result of the Operation and returns the result Asset
	 * or returns null if the timeout in milliseconds expires before the
	 * asset is available.
	 *
	 * @param timeoutMillis The number of milliseconds to wait for a result before returning null
	 * @throws TimeoutException if result is not available in timeoutMillis
	 * @throws AuthorizationException if requestor does not have load permission
	 * @throws StorageException if unable to load the Asset
	 * @throws JobFailedException if Job fails
	 * @return The Asset resulting from the job, or null if the timeout expires before the  job completes
	 */
	@Override
	public Asset awaitResult(long timeoutMillis) {
		try {
			return future.get(timeoutMillis, TimeUnit.MILLISECONDS);
		}
		catch (InterruptedException | ExecutionException | TimeoutException e) {
			Throwable cause=e.getCause();
			throw new JobFailedException("Job failed with exception: "+cause,e);
		}
	}

}
