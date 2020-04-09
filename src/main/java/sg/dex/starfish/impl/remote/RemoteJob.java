package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Job;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.JobFailedException;
import sg.dex.starfish.exception.RemoteException;
import sg.dex.starfish.util.Params;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static sg.dex.starfish.constant.Constant.*;

/**
 * This class represents a remote Job executed via the Invoke API on a remote agent.
 */
public class RemoteJob implements Job {
    private final RemoteAgent agent;
    private final String jobID;
    private String status = SCHEDULED;
    private Map<String, Object> result = null;
    private Map<String, Object> paramSpec = null;


    private RemoteJob(RemoteAgent agent, Map<String, Object> paramSpec, String jobID) {
        this.agent = agent;
        this.jobID = jobID;
        this.paramSpec = paramSpec;
    }


    /**
     * Creates a RemoteJob representing a Job on a given RemoteAgent.
     * <p>
     * Does not perform any validation on the existence of the Job
     *
     * @param agent2
     * @param jobID
     * @return
     */
    public static RemoteJob create(RemoteAgent agent2, Map<String, Object> paramSpec, String jobID) {
        return new RemoteJob(agent2, paramSpec, jobID);
    }

    @Override
    public boolean isDone() {
        return status.equals(SUCCEEDED) || status.equals(FAILED) || (status.equals(CANCELLED));
    }

    /**
     * Polls the invokable service job for a complete result.
     * <p>
     * Returns null if the Job has not completed, or if the remote service cannot be accessed
     *
     * @return The Map of <String,Object> where key will be the result and value will be the payload
     * @throws JobFailedException If the job has failed
     */
    @Override
    @SuppressWarnings("unchecked")
    public synchronized Map<String, Object> pollResult() {
        // quick check to see if we already have a terminal result - avoids extra requests
        if (isDone()) {
            if (status.equals(SUCCEEDED))
                return result; // we should have a valid result
            return getResult(); // should throw an error
        }

        // Get JSON response map
        Map<String, Object> response = (Map<String, Object>) agent.pollJob(jobID);
        if (response == null) return null; // unable to read from server?

        String newStatus = (String) response.get(STATUS);
        if (newStatus == null)
            throw new RemoteException("No status in job id " + jobID + " result: " + response);

        // FIXME: needs to match statuses in DEP6
        if (newStatus.equals(RUNNING) || newStatus.equals(SCHEDULED)) {
            this.status = newStatus;
            return null;
        }

        if (newStatus.equals(SUCCEEDED)) {

            Map<String, Object> res = (Map<String, Object>) response.get(Constant.OUTPUTS);
            if (res == null)
                throw new RemoteException("No result map in job id " + jobID + " result: " + response);
            // store result and success status
            result = res;
            this.status = SUCCEEDED;
            return res;
        }

        if (newStatus.equals(CANCELLED) || newStatus.equals(FAILED)) {
            this.status = newStatus;
            throw new RemoteException("Job failed to complete with status: [" + newStatus + "]");
        }

        throw new RemoteException("Unexpected Job status: [" + newStatus + "]");
    }

    @Override
    public Map<String, Object> get(long timeout, TimeUnit timeUnit) throws TimeoutException {

        long timeoutMillis = TimeUnit.MILLISECONDS.convert(timeout, timeUnit);
        long start = System.currentTimeMillis();
        int sleepTime = 100;
        while (System.currentTimeMillis() < start + timeoutMillis) {
            Map<String, Object> a = pollResult();
            if (a != null) {
                return Params.formatResponse(paramSpec, a);
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new JobFailedException("Job failed with exception: " + e.getCause(), e);
            }
            sleepTime *= 2;
        }
        Map<String, Object> a = pollResult();
        if (a != null) {
            return a;
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
}
