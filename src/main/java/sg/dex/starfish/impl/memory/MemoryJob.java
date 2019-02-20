package sg.dex.starfish.impl.memory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;

/**
 * Class representing a job being conducted in the local JVM
 * 
 * @author Mike
 *
 */
public class MemoryJob implements Job {

	private final Future<Asset> future;
	
	public MemoryJob(Future<Asset> future) {
		this.future=future;
	}

	@Override
	public boolean isComplete() {
		return future.isDone();
	}
	
	public MemoryJob create(Future<Asset> future) {
		return new MemoryJob(future);
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
			throw new Error("Job failed with exception: "+cause,e);
		}
	}

}
