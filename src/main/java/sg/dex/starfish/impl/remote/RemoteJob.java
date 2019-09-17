package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Job;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.JobFailedException;
import sg.dex.starfish.exception.RemoteException;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static sg.dex.starfish.constant.Constant.*;

/**
 * This class represents a remote Job executed via the Invoke API on a remote agent.
 */
public class RemoteJob implements Job {
    private RemoteAgent agent;
    private String jobID;
    private String status;
    private RemoteJob(RemoteAgent agent, String jobID) {
        this.agent = agent;
        this.jobID = jobID;

    }

    public static RemoteJob create(RemoteAgent agent2, String jobID) {

        return new RemoteJob(agent2, jobID);
    }

    @Override
    public boolean isDone() {
        return pollResult()== null? false:true;
    }

    /**
     * Polls the invokable service job for a complete asset.
     *
     * @return The Map of <String,Object> where key will be the result and value will be the payload
     * @throws JobFailedException If the job has failed
     */
    @Override
	@SuppressWarnings("unchecked")
	public synchronized Map<String,Object> pollResult() {

        Map<String,Object> result = (Map<String,Object>)agent.pollJob(jobID);

        String status = (String) result.get(STATUS);
        this.status=status;

        if (status == null) throw new RemoteException("No status in job id " + jobID + " result: " + result);
        if (status.equals(STARTED) || status.equals(IN_PROGRESS) || status.equals(ACCEPTED)
                || status.equals(SCHEDULED)) {

            return null;
        }
        if (status.equals(COMPLETED) || status.equals(SUCCEEDED)) {
            return result;
        } else if (status.equals(Constant.UNKNOWN)) {
            throw new JobFailedException("Error code: " + result.get("errorcode") +
                    "description is : " + result.get("description"));
        }
        return result;
    }

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
