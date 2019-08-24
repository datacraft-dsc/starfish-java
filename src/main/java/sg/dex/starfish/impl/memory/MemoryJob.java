package sg.dex.starfish.impl.memory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sg.dex.starfish.Job;
import sg.dex.starfish.exception.JobFailedException;
import sg.dex.starfish.util.Hex;

/**
 * Class representing a job being conducted asynchronously in the local JVM, which wraps
 * an arbitrary Future.
 *
 * @param <T> The type of the Job result
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
	public boolean isDone() {
		return future.isDone();
	}
	
	@Override
	public boolean isCancelled() {
		return future.isCancelled();
	}

	@Override
	public  T pollResult() {
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
	 * or returns null if the timeout in milliseconds expires before the
	 * asset is available.
	 *
	 * @throws JobFailedException if Job fails
	 * @return The Asset resulting from the job, or null if the timeout expires before the  job completes
	 */
	@Override
	public T get(long timeoutMillis, TimeUnit timeUnit) {
		try {
			return future.get(timeoutMillis, timeUnit);
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
