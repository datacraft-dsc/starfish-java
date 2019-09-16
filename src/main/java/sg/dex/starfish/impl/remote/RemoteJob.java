package sg.dex.starfish.impl.remote;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import sg.dex.starfish.Job;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.JobFailedException;

/**
 * This class represents a remote Job executed via the Invoke API on a remote agent.
 */
public class RemoteJob implements Job {
    private RemoteAgent agent;
    private String jobID;
    private Map<String,Object> response;
    private String status;
    private RemoteJob(RemoteAgent agent, String jobID) {
        this.agent = agent;
        this.jobID = jobID;
        status= Constant.RUNNING;
    }

    public static RemoteJob create(RemoteAgent agent2, String jobID) {

        return new RemoteJob(agent2, jobID);
    }

    @Override
    public boolean isDone() {
        return response != null;
    }

    /**
     * Polls the invokable service job for a complete asset.
     *
     * @return The resulting asset, or null if not yet available
     * @throws JobFailedException If the job has failed
     */
    @Override
	@SuppressWarnings("unchecked")
	public synchronized Map<String,Object> pollResult() {
        if (response != null) {
            return response;
        }
        response = (Map<String,Object>)agent.pollJob(jobID);
        return response;
    }

    @Override
    public Map<String,Object> get(long timeout, TimeUnit timeUnit) {
    	long timeoutMillis=TimeUnit.MILLISECONDS.convert(timeout,timeUnit);
        long start = System.currentTimeMillis();
        int initialSleep = 100;
        while (System.currentTimeMillis() < start + timeoutMillis) {
        	Map<String,Object> a = pollResult();
            if (a != null){
                return a;
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
        return status;
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
