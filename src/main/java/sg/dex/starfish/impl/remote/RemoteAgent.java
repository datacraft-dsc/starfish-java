package sg.dex.starfish.impl.remote;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import sg.dex.starfish.AAgent;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Invokable;
import sg.dex.starfish.Job;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.Operation;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Params;
import sg.dex.starfish.util.RemoteException;
import sg.dex.starfish.util.TODOException;
import sg.dex.starfish.util.Utils;

/**
 * Class implementing a remote storage agent using the Storage API
 * @author Mike
 *
 */
public class RemoteAgent extends AAgent implements Invokable {

	/**
	 * Creates a RemoteAgent with the specified Ocean connection and DID
	 * @param ocean Ocean connection to use
	 * @param did DID for this agent
	 */
	protected RemoteAgent(Ocean ocean, DID did) {
		super(ocean, did);
	}

	@Override
	public void registerAsset(Asset a) {
		throw new TODOException();
	}

	@Override
	public Asset getAsset(String id) {
		throw new TODOException();
	}

	@Override
	public Asset uploadAsset(Asset a) {
		throw new TODOException();
	}

	/**
	 * Gets a URL string for accessing the specified asset ID
	 * @param id The asset ID to address 
	 * @return The URL for the asset as a String
	 */
	public String getAssetURL(String id) {
		throw new TODOException();
	}
	
	public URI getInvokeURI() {
		try {
			return new URI(getInvokeEndpoint()+"/invokesync");
		}
		catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	public URI getJobURI(String jobID) {
		try {
			return new URI(getInvokeEndpoint()+"/jobs/"+jobID);
		}
		catch (URISyntaxException e) {
			throw new IllegalArgumentException("Can't create valid URI for job: "+jobID,e);
		}
	}

	public URL getURL(RemoteAsset remoteAsset) {
		String storageEndpoint=getStorageEndpoint();
		if (storageEndpoint==null) throw new IllegalStateException("No storage endpoint available for agent");
		try {
			return new URL(storageEndpoint+"/"+remoteAsset.getAssetID());
		}
		catch (MalformedURLException e) {
			throw new Error("Failed to get asset URL",e);
		}
	}

	/**
	 * Gets the storage endpoint for this agent
	 * @return The storage endpoint for this agent e.g. "https://www.myagent.com/api/v1/storage"
	 */
	public String getStorageEndpoint() {
		return getEndpoint("Ocean.Storage.v1");
	}
	
	/**
	 * Gets the Invoke API endpoint for this agent
	 * @return The invoke endpoint for this agent e.g. "https://www.myagent.com/api/v1/invoke"
	 */
	public String getInvokeEndpoint() {
		return getEndpoint("Ocean.Invoke.v1");
	}
	
	/**
	 * Gets the Meta API endpoint for this agent
	 * @return The Meta API endpoint for this agent e.g. "https://www.myagent.com/api/v1/meta"
	 */
	public String getMetaEndpoint() {
		return getEndpoint("Ocean.Meta.v1");
	}
	
	@Override
	public Job invoke(Operation operation, Asset... params) {
		Map<String,Object> request=new HashMap<String,Object>(2);
		request.put("operation",operation.getAssetID());
		request.put("params",Params.formatParams(operation,params));
		return invoke(request);
	}
	
	/**
	 * Polls this agent for the Asset resulting from the given job ID
	 * @throws IllegalArgumentException If the job ID is invalid
	 * @param jobID
	 * @return The asset resulting from this job ID if available, null otherwise.
	 */
	public Asset pollJob(String jobID) {
		URI uri=getJobURI(jobID);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(uri);
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(httpget);
			try {
			    StatusLine statusLine=response.getStatusLine();
			    int statusCode = statusLine.getStatusCode();
			    if (statusCode==404) {
			    	throw new RemoteException("Job ID not found for invoke at: "+uri);
			    }
			    if (statusCode==200) {
			    	String body=Utils.stringFromStream(response.getEntity().getContent());
			    	Map<String,Object> result=JSON.toMap(body);
			    	String status=(String) result.get("status");
			    	if (status==null) throw new RemoteException("No status in job result: "+body);
			    	if (status.equals("started")||status.equals("inprogress")) {
			    		return null; // no result yet
			    	}
			    	if (status.equals("started")||status.equals("inprogress")) {
			    		return null; // no result yet
			    	}
			    	if (status.equals("complete")) {
			    		String assetID = (String) result.get("result");
			    		if (assetID==null) throw new RemoteException("No asset in completed job result: "+body);
			    		return RemoteAsset.create(assetID,this);
			    	}
			    }
		    	throw new TODOException("status code not handled: "+statusCode);
			} finally {
			    response.close();
			}
		}
		catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Job invoke(Operation operation, Map<String,Asset> params) {
		Map<String,Object> request=new HashMap<String,Object>(2);
		request.put("operation",operation.getAssetID());
		request.put("params",Params.formatParams(operation,params));
		return invoke(request);
	}

	private Job invoke(Map<String,Object> request) {
		String req=JSON.toString(request);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(getInvokeURI());
		StringEntity entity=new StringEntity(req,StandardCharsets.UTF_8);
		httppost.setEntity(entity);
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(httppost);
			try {
				return RemoteAgent.create(this,response);
			} finally {
			    response.close();
			}
		}
		catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	static Job createWith200(RemoteAgent agent, HttpResponse response) {
		HttpEntity entity=response.getEntity();
		if (entity==null) throw new RuntimeException("Invoke failed: no response body");
		try {
			String body=Utils.stringFromStream(entity.getContent());
			return RemoteJob.create(agent,body);
		}
		catch (Exception e) {
			throw new RuntimeException("Invoke failed: "+e.getMessage(),e);
		}
	}

	/**
	 * Creates a remote invoke Job using the given HTTP response.
	 * @param response A valid successful response from the remote Invoke API
	 * @return A job representing the remote invocation
	 */
	public static Job create(RemoteAgent agent, HttpResponse response) {
		StatusLine statusLine=response.getStatusLine();
		int statusCode=statusLine.getStatusCode();
	    if (statusCode==200) {
	    	return RemoteAgent.createWith200(agent, response);
	    }			    
	    String reason=statusLine.getReasonPhrase();
	    if ((statusCode)==400) {
	    	throw new IllegalArgumentException("Bad invoke request: "+reason);
	    }
	    throw new RuntimeException("Invoke request failed with code "+statusCode+": "+reason);
	}


}
