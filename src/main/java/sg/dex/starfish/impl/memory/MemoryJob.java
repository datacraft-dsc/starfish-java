package sg.dex.starfish.impl.memory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.utils.JobFailedException;

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
	
	public MemoryJob(Future<Asset> future) {
		this.future=future;
	}
	
	/**
	 * Create a MemoryJob instance using the provided Future.
	 * 
	 * @param future
	 * @return A MemoryJob instance encapsulation the provided future
	 */
	public static MemoryJob create(Future<Asset> future) {
		return new MemoryJob(future);
	}

	@Override
	public boolean isComplete() {
		return future.isDone();
	}

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
