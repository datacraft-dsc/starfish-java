package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Job;
import sg.dex.starfish.exception.JobFailedException;

/**
 * This class is for creating the job and also can be used
 * to know the status of each job
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
    public boolean isComplete() {
        return response != null;
    }

    @Override
    public T getResult() {
        return pollResult();
    }



    /**
     * Polls the invokable service job for a complete asset.
     *
     * @return The resulting asset, or null if not yet available
     * @throws JobFailedException If the job has failed
     */
    @SuppressWarnings("unchecked")
	private synchronized T pollResult() {
        if (response != null) return response;
        response = (T)agent.pollJob(jobID);
        return response;
    }

    @Override
    public T awaitResult(long timeoutMillis) {
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


}
