package sg.dex.starfish.impl.remote;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

import sg.dex.starfish.Asset;
import sg.dex.starfish.Invokable;
import sg.dex.starfish.Job;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.Operation;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.RemoteException;
import sg.dex.starfish.util.TODOException;
import sg.dex.starfish.util.Utils;

public class InvokeAgent extends RemoteAgent implements Invokable {

	protected InvokeAgent(Ocean ocean, DID did) {
		super(ocean, did);
		// TODO Auto-generated constructor stub
	}
	
	public URI getInvokeURI() {
		try {
			return new URI("http://10.0.1.164:3000/invokesync");
		}
		catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	public URI getJobURI(String jobID) {
		try {
			return new URI("http://10.0.1.164:3000/jobs/"+jobID);
		}
		catch (URISyntaxException e) {
			throw new IllegalArgumentException("Can't create valid URI for job: "+jobID,e);
		}
	}
	
	public HttpResponse invokeRemote() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(getInvokeURI());
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(httpget);
			try {
			    return response;
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
	
	public static void main(String... args) {
		InvokeAgent ag= new InvokeAgent(Ocean.connect(),Utils.createRandomDID());
		System.out.println(ag.invokeRemote());
	}

	@Override
	public void registerAsset(Asset a) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Asset getAsset(String id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Asset uploadAsset(Asset a) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Job invoke(Operation operation, Asset... params) {
		Map<String,Object> request=new HashMap<String,Object>(2);
		request.put("operation",operation.getAssetID());
		request.put("params",formatParams(operation,params));
		return invoke(request);
	}
	
	@Override
	public Job invoke(Operation operation, Map<String,Asset> params) {
		Map<String,Object> request=new HashMap<String,Object>(2);
		request.put("operation",operation.getAssetID());
		request.put("params",formatParams(operation,params));
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
				return InvokeAgent.create(this,response);
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

	/**
	 * Creates the "params" part of the invoke payload using the spec in the operation metadata 
	 * and the passed positional arguments
	 * @param operation The operation to format the parameters for
	 * @param params A map of parameter names to assets
	 * @return The "params" portion of the invoke payload as a Map
	 */
	@SuppressWarnings("unchecked")
	protected Map<String,Object> formatParams(Operation operation, Map<String,Asset> params) {
		HashMap<String,Object> result=new HashMap<>(params.size());
		Map<String,Object> paramSpec=operation.getParamSpec();
		for (Map.Entry<String,Object> me:paramSpec.entrySet()) {
			String paramName=me.getKey();
			Map<String,Object> spec=(Map<String,Object>)me.getValue();
			// String type=(String) spec.get("type");
			boolean required=Utils.coerceBoolean(spec.get("required"));
			if (params.containsKey(paramName)) {
				Asset a=params.get(paramName);
				Map<String,Object> value=a.getParamValue();
				result.put(paramName,value);
			}
			if (required) {
				throw new IllegalArgumentException("Paramter "+paramName+" is required but not supplied");
			}
		}
		return result;
	}
	
	/**
	 * Creates the "params" part of the invoke payload using the spec in the operation metadata 
	 * and the passed positional arguments
	 * @param operation The operation to format the parameters for
	 * @param params An array of assets to be provided as positional parameters
	 * @return The "params" portion of the invoke payload as a JSONObject
	 */
	@SuppressWarnings("unchecked")
	protected Map<String,Object> formatParams(Operation operation, Asset... params) {
		HashMap<String,Object> result=new HashMap<>(params.length);
		Map<String,Object> paramSpec=operation.getParamSpec();
		for (Map.Entry<String,Object> me:paramSpec.entrySet()) {
			String paramName=me.getKey();
			Map<String,Object> spec=(Map<String,Object>)me.getValue();
			// String type=(String) spec.get("type");
			Object positionObj=spec.get("position");
			int pos=(positionObj!=null)?Utils.coerceInt(positionObj):-1;
			boolean required=Utils.coerceBoolean(spec.get("required"));
			if ((pos>=0)&&(pos<params.length)) {
				Asset a=params[pos];
				Map<String,Object> value=a.getParamValue();
				result.put(paramName,value);
			}
			if (required) {
				throw new IllegalArgumentException("Paramter "+paramName+" is required but not supplied");
			}
		}
		return result;
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

	static Job createWith200(InvokeAgent agent, HttpResponse response) {
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
	public static Job create(InvokeAgent agent, HttpResponse response) {
		StatusLine statusLine=response.getStatusLine();
		int statusCode=statusLine.getStatusCode();
	    if (statusCode==200) {
	    	return InvokeAgent.createWith200(agent, response);
	    }			    
	    String reason=statusLine.getReasonPhrase();
	    if ((statusCode)==400) {
	    	throw new IllegalArgumentException("Bad invoke request: "+reason);
	    }
	    throw new RuntimeException("Invoke request failed with code "+statusCode+": "+reason);
	}

}
