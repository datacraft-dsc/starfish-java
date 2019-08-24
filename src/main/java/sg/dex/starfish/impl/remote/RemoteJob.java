package sg.dex.starfish.impl.remote;

import java.util.concurrent.TimeUnit;

import sg.dex.starfish.Job;
import sg.dex.starfish.exception.JobFailedException;

/**
 * This class represents a remote Job executed via the Invoke API on a remote agent.
 */
public class RemoteJob<T> implements Job<T> {
    private RemoteAgent agent;
    private String jobID;
    private T response;

    private RemoteJob(RemoteAgent agent, String jobID) {
        this.agent = agent;
        this.jobID = jobID;
    }

    public static <T> Job<T> create(RemoteAgent agent2, String jobID) {
        return new RemoteJob<T>(agent2, jobID);
    }

    @Override
    public boolean isDone() {
        return response != null;
    }

    @Override
    public T get() {
        return pollResult();
    }



    /**
     * Polls the invokable service job for a complete asset.
     *
     * @return The resulting asset, or null if not yet available
     * @throws JobFailedException If the job has failed
     */
    @Override
	@SuppressWarnings("unchecked")
	public synchronized T pollResult() {
        if (response != null) return response;
        response = (T)agent.pollJob(jobID);
        return response;
    }

    @Override
    public T get(long timeout, TimeUnit timeUnit) {
    	long timeoutMillis=TimeUnit.MILLISECONDS.convert(timeout,timeUnit);
        long start = System.currentTimeMillis();
        int initialSleep = 100;
        while (System.currentTimeMillis() < start + timeoutMillis) {
            T a = pollResult();
            if (a != null) return a;
            try {
                Thread.sleep(initialSleep);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            initialSleep *= 2;
        }
        return pollResult();
    }

    @Override
    public String getJobID() {
        return jobID;
    }

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}


}
