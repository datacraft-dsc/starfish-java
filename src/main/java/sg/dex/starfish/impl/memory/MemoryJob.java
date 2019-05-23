package sg.dex.starfish.impl.memory;

import sg.dex.starfish.Job;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.JobFailedException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.util.Hex;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Class representing a job being conducted asynchronously in the local JVM.
 *
 * A memory job will either:
 * - Be in progress (getResult return null)
 * - Complete normally (getResult returns an Asset)
 * - Fail with some exception (getResult throws an exception)
 * @param <T> This describes my type parameter
 * It is possible that a memory job will never complete.
 *
 * @author Mike
 *
 */
public class MemoryJob<T> implements Job<T> {

	private final Future<T> future;

	private MemoryJob(Future<T> future) {
		this.future=future;
	}

	/**
	 * Create a MemoryJob instance using the provided Future.
	 *
	 * @param future A Future to be used to complete this job
	 * @param <T> This describes my type parameter
	 * @return A MemoryJob instance encapsulation the provided future
	 */
	public static<T> MemoryJob<T> create(Future<T> future) {
		return new MemoryJob<T>(future);
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
	public  T getResult() {
		try {
			return future.isDone()?future.get():null;
		}
		catch (InterruptedException e) {
			throw new Error("Job interrupted",e);
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
	 * @throws Error if Job interrupted
	 * @return The Asset resulting from the job
	 */
	@Override
	public T awaitResult() {
		try {
			return future.get();
		}
		catch (InterruptedException e) {
			throw new Error("Job interrupted",e);
		}
		catch (ExecutionException e) {
			Throwable cause=e.getCause();
			throw new JobFailedException("Job failed with exception: "+cause,e);
		}
	}

	/**
	 * Waits for the result of the Operation and returns the result Asset
	 * or returns null if the timeout in milliseconds expires before the
	 * asset is available.
	 *
	 * @throws JobFailedException if Job fails
	 * @return The Asset resulting from the job, or null if the timeout expires before the  job completes
	 */
	@Override
	public T awaitResult(long timeoutMillis) {
		try {
			return future.get(timeoutMillis, TimeUnit.MILLISECONDS);
		}
		catch (InterruptedException | ExecutionException | TimeoutException e) {
			Throwable cause=e.getCause();
			throw new JobFailedException("Job failed with exception: "+cause,e);
		}
	}

	@Override
	public String getJobID() {
		return "MemoryJob:"+Hex.toString(System.identityHashCode(this));
	}

}
