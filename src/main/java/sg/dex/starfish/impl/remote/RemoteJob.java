package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.util.JobFailedException;

public class RemoteJob implements Job {
	private InvokeAgent agent;
	private String jobID;
	private Asset asset;

	public RemoteJob(InvokeAgent agent, String jobID) {
		this.agent=agent;
		this.jobID=jobID;
	}

	public static Job create(InvokeAgent agent, String jobID) {
		return new RemoteJob(agent,jobID);
	}

	@Override
	public boolean isComplete() {
		return asset!=null;
	}

	@Override
	public Asset getResult() {
		return pollResult();
	}

	/**
	 * Polls the invokable service job for a complete asset. 
	 * @throws JobFailedException If the job has failed
	 * @return The resulting asset, or null if not yet available
	 */
	private synchronized Asset pollResult() {
		if (asset!=null) return asset;
		asset= agent.pollJob(jobID);
		return asset;
	}

	@Override
	public Asset awaitResult(long timeoutMillis) {
		long start=System.currentTimeMillis();
		int initialSleep=100;
		while (System.currentTimeMillis()<start+timeoutMillis) {
			Asset a=pollResult();
			if (a!=null) return a;
			try {
				Thread.sleep(initialSleep);
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			initialSleep*=2;
		};
		return pollResult();
	}

	@Override
	public String getJobID() {
		return jobID;
	}
	
	


}
