package sg.dex.starfish.impl.memory;

import sg.dex.starfish.Job;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.JobFailedException;
import sg.dex.starfish.exception.RemoteException;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.Utils;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static sg.dex.starfish.constant.Constant.*;

/**
 * Class representing a Job being conducted asynchronously in the local JVM, which wraps
 * an arbitrary Future.
 *
 * @author Mike
 */
public class MemoryJob implements Job {

    private final Future<Map<String, Object>> future;
    private String status;

    private MemoryJob(Future<Map<String, Object>> future) {
        status = Constant.RUNNING;
        this.future = future;
    }

    /**
     * Create a MemoryJob instance using the provided Future.
     *
     * @param future A Future to be used to complete this job
     * @return A MemoryJob instance encapsulation the provided future
     */
    public static MemoryJob create(Future<Map<String, Object>> future) {
        return new MemoryJob(future);
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
    public Map<String, Object> pollResult() {
        if (!future.isDone()) return null;
        try {
			return future.get();
		}
		catch (Throwable t) {
			status=Constant.FAILED;
			throw Utils.sneakyThrow(t);
		}
    }

    /**
     * Waits for the result of the Operation and returns the result Asset
     * or throws an exception if the timeout in milliseconds expires before the
     * asset is available.
     *
     * @return The Asset resulting from the job, or null if the timeout expires before the  job completes
     * @throws JobFailedException if Job fails
     */
    @Override
    public Map<String, Object> get(long timeout, TimeUnit timeUnit) {
        long timeoutMillis = TimeUnit.MILLISECONDS.convert(timeout, timeUnit);
        long start = System.currentTimeMillis();
        int initialSleep = 100;
        while (System.currentTimeMillis() < start + timeoutMillis) {
            Map<String, Object> a = pollResult();
            if (a != null) {
            	status=Constant.SUCCEEDED;
                return a;
            }
            try {
                Thread.sleep(initialSleep);
            } catch (InterruptedException e) {
            	status=Constant.FAILED;
                throw new JobFailedException("Job interrupted with exception: " + e.getCause(), e);
            }
            initialSleep *= 2;
        }
        throw Utils.sneakyThrow(new TimeoutException("Timeout in MemoryJob.get(...)"));
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public String getJobID() {
        return "MemoryJob:" + Hex.toString(System.identityHashCode(this));
    }


}
