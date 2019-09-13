package sg.dex.starfish.impl.memory;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sg.dex.starfish.Job;
import sg.dex.starfish.exception.JobFailedException;
import sg.dex.starfish.util.Hex;

/**
 * Class representing a Job being conducted asynchronously in the local JVM, which wraps
 * an arbitrary Future.
 *
 *
 * @author Mike
 *
 */
public class MemoryJob implements Job {

	private final Future<Map<String,Object>> future;



	private Job.Status status ;

	private MemoryJob(Future<Map<String,Object>> future) {
		this.future=future;
	}

	/**
	 * Create a MemoryJob instance using the provided Future.
	 *
	 * @param future A Future to be used to complete this job
	 * @return A MemoryJob instance encapsulation the provided future
	 */
	public static MemoryJob create(Future<Map<String,Object>> future) {
		return new MemoryJob (future);
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
	public Map<String,Object> pollResult() {
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
	public Map<String,Object> get(long timeoutMillis, TimeUnit timeUnit) {
		try {
			this.setStatus(Status.succeeded);
			return future.get(timeoutMillis, timeUnit);
		}
		catch (InterruptedException | ExecutionException | TimeoutException e) {
			Throwable cause=e.getCause();
			this.setStatus(Status.failed);
			throw new JobFailedException("Job failed with exception: "+cause,e);
		}
	}

	@Override
	public Job.Status getStatus() {
		return this.status;
	}
	public void setStatus( Job.Status status) {
		this.status = status;
	}
	@Override
	public String getJobID() {
		return "MemoryJob:"+Hex.toString(System.identityHashCode(this));
	}


}
