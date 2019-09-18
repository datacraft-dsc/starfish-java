package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Job;
import sg.dex.starfish.exception.JobFailedException;
import sg.dex.starfish.exception.RemoteException;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
        return pollResult() != null;
    }

    /**
     * Polls the invokable service job for a complete asset.
     *
     * @return The Map of <String,Object> where key will be the result and value will be the payload
     * @throws JobFailedException If the job has failed
     */
    @Override
    @SuppressWarnings("unchecked")
    public synchronized Map<String, Object> pollResult() {

    	// Get JSON response map
        Map<String, Object> response = (Map<String, Object>) agent.pollJob(jobID);

        String status = (String) response.get(STATUS);
        this.status = status;

        if (status == null) throw new RemoteException("No status in job id " + jobID + " result: " + response);

        // needs to match statuses in DEP6
        if (status.equals(IN_PROGRESS)
                || status.equals(SCHEDULED)) {

            return null;
        }
        
        if (status.equals(FAILED) || status.equals(SUCCEEDED)||status.equals(CANCELLED)) {
        	Map<String, Object> result=(Map<String, Object>) response.get("result");
        	if (result == null) throw new RemoteException("No result map in job id " + jobID + " result: " + response);
            return result;
        }
        return response;
    }

    @Override
    public Map<String, Object> get(long timeout, TimeUnit timeUnit) throws TimeoutException {
        long timeoutMillis = TimeUnit.MILLISECONDS.convert(timeout, timeUnit);
        long start = System.currentTimeMillis();
        int initialSleep = 100;
        while (System.currentTimeMillis() < start + timeoutMillis) {
            Map<String, Object> a = pollResult();
            if (a != null) {
                return a ;
            }
            try {
                Thread.sleep(initialSleep);
            } catch (InterruptedException e) {
                throw new JobFailedException("Job failed with exception: " + e.getCause(), e);
            }
            initialSleep *= 2;
        }
        throw new TimeoutException("Timeout in remote Job get");
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
