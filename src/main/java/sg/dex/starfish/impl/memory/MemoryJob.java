package sg.dex.starfish.impl.memory;

import sg.dex.starfish.Job;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.JobFailedException;
import sg.dex.starfish.exception.RemoteException;
import sg.dex.starfish.util.Hex;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static sg.dex.starfish.constant.Constant.*;

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
	private String status ;

	private MemoryJob(Future<Map<String,Object>> future) {
		status=Constant.RUNNING;
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
    public Map<String, Object> pollResult() {
        Map<String, Object> res = null;
		try {
			res = future.get();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Map<String, Object> result = (Map<String, Object> )res.get("result");
		String status = (String) result.get(STATUS);
            this.status = status;

            if (status == null) throw new RemoteException("No status result: " + result);
            if (status.equals(STARTED) || status.equals(IN_PROGRESS) || status.equals(ACCEPTED)
                    || status.equals(SCHEDULED)) {

                return null;
            }
            if (status.equals(FAILED) || status.equals(SUCCEEDED)) {
                return res;
            } else if (status.equals(Constant.UNKNOWN)) {
                throw new JobFailedException("Error code: " + result.get("errorcode") +
                        "description is : " + result.get("description"));
            }
        return result;
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
	public Map<String,Object> get(long timeout, TimeUnit timeUnit) {
        long timeoutMillis=TimeUnit.MILLISECONDS.convert(timeout,timeUnit);
        long start = System.currentTimeMillis();
        int initialSleep = 100;
        while (System.currentTimeMillis() < start + timeoutMillis) {
            Map<String,Object> a = pollResult();
            if (a != null){
                return (Map<String,Object>)a.get("result");
            }
            try {
                Thread.sleep(initialSleep);
            } catch (InterruptedException e) {
                throw new JobFailedException("Job failed with exception: "+e.getCause(),e);
            }
            initialSleep *= 2;
        }
        return pollResult();
	}

	@Override
	public String getStatus() {
		return this.status;
	}
	@Override
	public String getJobID() {
		return "MemoryJob:"+Hex.toString(System.identityHashCode(this));
	}


}
