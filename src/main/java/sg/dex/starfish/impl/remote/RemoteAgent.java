package sg.dex.starfish.impl.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AUTH;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharArrayBuffer;
import org.json.simple.JSONArray;
import sg.dex.starfish.*;
import sg.dex.starfish.exception.*;
import sg.dex.starfish.impl.AAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static sg.dex.starfish.constant.Constant.*;

/**
 * Class implementing a remote storage agent using the Storage API
 *
 * @author Mike
 */
public class RemoteAgent extends AAgent implements Invokable<Asset>, MarketAgent {


	private final RemoteAccount account;

	/**
	 * Creates a RemoteAgent with the specified Ocean connection and DID
	 *
	 * @param ocean Ocean connection to use
	 * @param did   DID for this agent
	 */
	protected RemoteAgent(Ocean ocean, DID did, RemoteAccount account) {
		super(ocean, did);
		this.account =  account;
	}

	/**
	 * Creates a RemoteAgent with the specified Ocean connection, DID
	 * and RemoteAccount
	 *
	 * @param ocean Ocean connection to use
	 * @param did   DID for this agent
	 * @param account RemoteAccount for this agent
	 * @return RemoteAgent
	 */
	public static RemoteAgent create(Ocean ocean, DID did, RemoteAccount account) {
		if (ocean==null) throw new IllegalArgumentException("Ocean connection cannot be null for remote agent");
		if (did==null) throw new IllegalArgumentException("DID cannot be null for remote agent");
		return new RemoteAgent(ocean, did, account);
	}

	/**
	 * Creates a RemoteAgent with the specified Ocean connection and DID
	 *
	 * @param ocean Ocean connection to use
	 * @param did   DID for this agent
	 * @return RemoteAgent
	 */
	public static RemoteAgent create(Ocean ocean, DID did) {
		return new RemoteAgent(ocean, did, null);
	}

	/**
	 * Invokes request on this RemoteAgent
	 *
	 * @param agent    the remote
	 * @param response
	 * @return Job for this request
	 * @throws RuntimeException for protocol errors
	 */
	private static Job createSuccessJob(RemoteAgent agent, HttpResponse response) {
		HttpEntity entity = response.getEntity();
		if (entity == null) throw new RuntimeException("Invoke failed: no response body");
		try {
			String body = Utils.stringFromStream(entity.getContent());
			return RemoteJob.create(agent, body);
		} catch (Exception e) {
			throw new GenericException("Internal Server Error");
		}
	}

	/**
	 * Creates a remote invoke Job using the given HTTP response.
	 *
	 * @param agent    RemoteAgent on which to create the Job
	 * @param response A valid successful response from the remote Invoke API
	 * @return A job representing the remote invocation
	 * @throws IllegalArgumentException for a bad invoke request
	 * @throws RuntimeException         for protocol errors
	 */
	public static Job createJob(RemoteAgent agent, HttpResponse response) {
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (statusCode == 201 ) {
			return RemoteAgent.createSuccessJob(agent, response);
		}
		String reason = statusLine.getReasonPhrase();
		if ((statusCode) == 400) {
			throw new IllegalArgumentException("Bad invoke request: " + reason);
		}
		throw new GenericException("Internal Server Error");
	}

	public RemoteAgent connect(RemoteAccount acc) {
		// TODO: get user token and store this in account
		return new RemoteAgent(ocean, did, acc);
	}

	/**
	 * Registers Asset a with the RemoteAgent
	 *
	 * @param a Asset to register
	 * @return RemoteAsset corresponding to a
	 * @throws RemoteException  if a is not found
	 * @throws TODOException    for unhandled results
	 * @throws RuntimeException for protocol errors
	 */
	@Override
	public ARemoteAsset registerAsset(Asset a) {
		URI uri = getMetaURI();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(uri);
		addAuthHeaders(httpPost);
		httpPost.setEntity(HTTP.textEntity(a.getMetadataString()));
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(httpPost);
			try {
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 404) {
					throw new RemoteException("Asset ID not found for at: " + uri);
				}
				if (statusCode == 200) {
					String body = Utils.stringFromStream(response.getEntity().getContent());
					String id = JSON.parse(body);
					return getAsset(id);
				}
				throw new TODOException("Result not handled: " + statusLine);
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void addAuthHeaders(HttpRequest request) {
		if (account == null) {
			throw new AuthorizationException("User don`t have account credentials" );
		} else {
			String token = null;
			String username = null;
			String password = null;

			if (account.getUserDataMap().get("token") != null) {
				token = account.getUserDataMap().get("token").toString();
			}
			if (account.getCredentials().get("username").toString() != null) {
				username = account.getCredentials().get("username").toString();
			}
			if (account.getCredentials().get("password").toString() != null) {
				password = account.getCredentials().get("password").toString();
			}
			if ((token == null) && (username == null)&&(password == null)) {
				throw new AuthorizationException("Username or Token or Password is not available for given account :"+account );
			} else {
				final CharArrayBuffer buffer = new CharArrayBuffer(32);
				if (token != null) {
					buffer.append("token ");
					buffer.append(token);
				}
				// will create toke for given account
				else {
//					final StringBuilder tmp = new StringBuilder();
//					tmp.append(username);
//					tmp.append(":");
//					tmp.append((password == null) ? "null" : password);
//					final Base64 base64codec = new Base64(0);
//					final byte[] base64password = base64codec.encode(EncodingUtils.getBytes(tmp.toString(), Consts.UTF_8.name()));
//					buffer.append("Basic ");
//					buffer.append(base64password, 0, base64password.length);
					createToken(account);
					buffer.append("token ");
					buffer.append(account.getUserDataMap().get("token").toString());
				}
				String header = AUTH.WWW_AUTH_RESP;
				String value = buffer.toString();
				request.setHeader(header, value);
			}
		}
	}


	/**
	 * Gets an asset for the given asset ID from this agent.
	 * Returns Runtime Exception  if the asset ID does not exist.
	 *
	 * @param id The ID of the asset to get from this agent
	 * @return Asset The asset found
	 * @throws AuthorizationException if requestor does not have access permission
	 * @throws StorageException       if there is an error in retreiving the Asset
	 * @throws RemoteException        if there is a failure in a remote operation
	 * @throws TODOException          for unhandled status codes
	 */
	@Override
	public ARemoteAsset getAsset(String id) {
		URI uri = getMetaURI(id);
		HttpGet httpget = new HttpGet(uri);
		addAuthHeaders(httpget);
		CloseableHttpResponse response = HTTP.execute(httpget);
		try {
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 404) {
				throw new RemoteException("Asset ID not found for at: " + uri);
			} else if (statusCode == 200) {
				String body = Utils.stringFromStream(HTTP.getContent(response));
				Map<String,Object> metaMap=JSON.toMap(body);
				return getRemoteAsset(body, metaMap);
			} else {
				throw new TODOException("status code not handled: " + statusCode);
			}
		} finally {
			HTTP.close(response);
		}
	}

	private ARemoteAsset getRemoteAsset(String body, Map<String, Object> metaMap) {
		if(metaMap.get(TYPE).equals(OPERATION)){
			 return RemoteOperation.create(this, body);
		}
		else if(metaMap.get(TYPE).equals(DATA_SET)){
			return RemoteAsset.create(this, body);
		}
		else {
			throw new TODOException("Invalid Type :" + metaMap.get("type"));
		}
	}

	@Override
	public ARemoteAsset getAsset(DID did) {
		return getAsset(did.getID());
	}

	/**
	 * API to check if the Asset is present if present it will return true else false.
	 *
	 * @param assetId
	 * @return boolean
	 */
	private boolean isAssetRegistered(String assetId) {
		URI uri = getMetaURI(assetId);
		HttpGet httpget = new HttpGet(uri);
		addAuthHeaders(httpget);
		CloseableHttpResponse response = HTTP.execute(httpget);
		try {
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			return statusCode == 200;
		} finally {
			HTTP.close(response);
		}
	}

	/**
	 * Uploads an asset to this agent. Registers the asset with the agent if required.
	 * <p>
	 * Throws an exception if upload is not possible, with the following likely causes:
	 * - The agent does not support uploads of this asset type / size
	 * - The data for the asset cannot be accessed by the agent
	 *
	 * @param a Asset to upload
	 * @return Asset An asset stored on the agent if the upload is successful
	 * @throws AuthorizationException if requestor does not have upload permission
	 * @throws StorageException       if there is an error in uploading the Asset
	 * @throws RemoteException        if there is an problem executing the task on remote Server.
	 */
	@Override
	public ARemoteAsset uploadAsset(Asset a) {
		ARemoteAsset remoteAsset;

		// asset alredy registered then only upload
		if (isAssetRegistered(a.getAssetID())) {
			remoteAsset = getAsset(a.getAssetID());
			uploadAssetContent(a);
		}
		// if asset is not registered then registered and upload
		else {
			remoteAsset = registerAsset(a);
			uploadAssetContent(a);
		}

		return remoteAsset;
	}

	/**
	 * API to uplaod the content of an Asset.
	 * API to upload the Asset content
	 *
	 * @param asset
	 */
	private void uploadAssetContent(Asset asset) {
		// get the storage API to upload the Asset content
		URI uri = getStorageURI(asset.getAssetID());
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpPost post = new HttpPost(uri);
		addAuthHeaders(post);
		post.addHeader("Accept", "application/json");

		InputStream assetContentAsStream = asset.asDataAsset().getContentStream();
		HttpEntity entity = HTTP.createMultiPart("file", new InputStreamBody(assetContentAsStream, Utils.createRandomHexString(16) + ".tmp"));

		post.setEntity(entity);

		CloseableHttpResponse response;
		try {
			response = httpclient.execute(post);
			try {
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 201) {
					return;
				}
				if (statusCode == 404) {
					throw new RemoteException("server could not find what was requested or it was configured not to fulfill the request." + uri);
				} else if (statusCode == 500) {
					throw new GenericException("Internal Server Error : " + statusLine);
				} else {
					throw new TODOException("Result not handled: " + statusLine);
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			throw new RemoteException("ClientProtocolException executing HTTP request ," + e.getMessage(), e);
		} catch (IOException e) {
			throw new RemoteException("IOException executing HTTP request ," + e.getMessage(), e);
		}
	}

	/**
	 * Gets a URL string for accessing the specified asset ID
	 *
	 * @param id The asset ID to address
	 * @return The URL for the asset as a String
	 * @throws TODOException when not implemented yet
	 */
	public String getAssetURL(String id) {
		throw new TODOException();
	}

	/**
	 * Gets URI for this agent's invoke endpoint
	 * @param did did
	 * @return The URI for this agent's invoke endpoint
	 * @throws RuntimeException on URI syntax errors
	 */
	public URI getInvokeSyncURI(Object did) {
		try {
			if(did==null) {
				return new URI(getInvokeEndpoint() );
			}
			else{
				return new URI(getInvokeEndpoint() +INVOKE_SYNC+"/" +did.toString());
			}
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets URI for this agent's invoke endpoint
     * @param did did
	 * @return The URI for this agent's invoke endpoint
	 * @throws RuntimeException on URI syntax errors
	 */
	public URI getInvokeAsyncURI(Object did) {
		try {
			if(did==null) {
				return new URI(getInvokeEndpoint() );
			}
			else{
				return new URI(getInvokeEndpoint() +INVOKE_ASYNC+"/" +did.toString());
			}
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets URI for this agent's endpoint for jobID
	 *
	 * @param response of the URI to create
	 * @return The URI for this agent's invoke endpoint
	 * @throws IllegalArgumentException on invalid URI for jobID
	 */
	private URI getJobURI(String response) {
		try {
			String jobId =JSON.toMap(response).get(JOB_ID).toString();
			return new URI(getInvokeEndpoint()+JOBS + "/" + jobId);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Can't create valid URI for job: " + response, e);
		}
	}

	/**
	 * Gets meta URI for this assetID
	 *
	 * @param assetID of the URI to create
	 * @return The URI for this assetID
	 * @throws UnsupportedOperationException if the agent does not support the Meta API
	 * @throws IllegalArgumentException      on invalid URI for assetID
	 */
	private URI getMetaURI(String assetID) {
		String metaEndpoint = getMetaEndpoint();
		if (metaEndpoint == null)
			throw new UnsupportedOperationException("This agent does not support the Meta API (no endpoint defined)");
		try {
			return new URI(metaEndpoint + DATA+"/" + assetID);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Can't create valid URI for asset metadata with ID: " + assetID, e);
		}
	}

	/**
	 * Gets storage URI for this assetID
	 *
	 * @param assetID of the URI to create
	 * @return The URI for this assetID
	 * @throws UnsupportedOperationException if the agent does not support the Storage API
	 * @throws IllegalArgumentException      on invalid URI for assetID
	 */
	public URI getStorageURI(String assetID) {
		String storageEndpoint = getStorageEndpoint();
		if (storageEndpoint == null) throw new UnsupportedOperationException(
										     "This agent does not support the Storage API (no endpoint defined)");
		try {
			return new URI(storageEndpoint + "/" + assetID);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Can't create valid URI for asset storage with ID: " + assetID, e);
		}
	}

	/**
	 * Gets meta URI for this agent
	 *
	 * @return The URI for asset metadata
	 * @throws UnsupportedOperationException if the agent does not support the Meta API (no endpoint defined)
	 * @throws IllegalArgumentException      on invalid URI for asset metadata
	 */
	private URI getMetaURI() {
		String metaEndpoint = getMetaEndpoint();
		if (metaEndpoint == null)
			throw new UnsupportedOperationException("This agent does not support the Meta API (no endpoint defined)");
		try {
			return new URI(metaEndpoint + DATA);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Can't create valid URI for asset metadata", e);
		}
	}

	/**
	 * Gets URL for a given remoteAsset
	 *
	 * @param remoteAsset for the URL
	 * @return The URL for the remoteAsset
	 * @throws IllegalStateException No storage endpoint available for agent
	 * @throws Error                 on failure to get asset URL
	 */
	public URL getURL(RemoteAsset remoteAsset) {
		String storageEndpoint = getStorageEndpoint();
		if (storageEndpoint == null) throw new IllegalStateException("No storage endpoint available for agent");
		try {
			return new URL(storageEndpoint + "/" + remoteAsset.getAssetID());
		} catch (MalformedURLException e) {
			throw new Error("Failed to get asset URL", e);
		}
	}

	/**
	 * Gets the storage endpoint for this agent
	 *
	 * @return The storage endpoint for this agent e.g.
	 * "https://www.myagent.com/api/v1/storage"
	 */
	public String getStorageEndpoint() {
		return getEndpoint("Ocean.Storage.v1");
	}

	/**
	 * Gets the Invoke API endpoint for this agent
	 *
	 * @return The invoke endpoint for this agent e.g.
	 * "https://www.myagent.com/api/v1/invoke"
	 */
	public String getInvokeEndpoint() {
		return getEndpoint("Ocean.Invoke.v1");
	}

	/**
	 * Gets the Meta API endpoint for this agent, or null if this does not exist
	 *
	 * @return The Meta API endpoint for this agent e.g.
	 * "https://www.myagent.com/api/v1/meta"
	 */
	public String getMetaEndpoint() {
		return getEndpoint("Ocean.Meta.v1");
	}

	/**
	 * Gets the Market API endpoint for this agent, or null if this does not exist
	 *
	 * @return The Meta API endpoint for this agent e.g.
	 * "https://www.myagent.com/api/v1/meta"
	 */
	public String getMarketEndpoint() {
		return getEndpoint("Ocean.Market.v1");
	}

	/**
	 * Gets the Auth API endpoint for this agent, or null if this does not exist
	 *
	 * @return The Meta API endpoint for this agent e.g.
	 * "https://www.myagent.com/api/v1/meta"
	 */
	public String getAuthEndpoint() {
		return getEndpoint("Ocean.Auth.v1");
	}

	@Override
	public Job invoke(Operation operation, Asset... params) {
		Map<String, Object> request = new HashMap(2);
		request.put(OPERATION, operation.getAssetID());
		request.put(PARAMS, Params.formatParams(operation, params));
		return invoke(request,operation.getAssetID());
	}

	/**
	 * Polls this agent for the Asset resulting from the given job ID
	 *
	 * @param jobID ID for the Job to poll
	 * @return The asset resulting from this job ID if available, null otherwise.
	 * @throws IllegalArgumentException If the job ID is invalid
	 * @throws RemoteException          if there is a failure in a remote operation
	 * @throws TODOException            for unhandled status codes
	 * @throws RuntimeException         for protocol errors
	 */
	public Asset pollJob(String jobID) {
		URI uri = getJobURI(jobID);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(uri);
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(httpget);
			try {
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 404) {
					throw new RemoteException("Job ID not found for invoke at: " + uri);
				}
				if (statusCode == 200) {
					String body = Utils.stringFromStream(response.getEntity().getContent());
					Map<String, Object> result = JSON.toMap(body);
					String status = (String) result.get(STATUS);
					if (status == null) throw new RemoteException("No status in job result: " + body);
					if (status.equals(STARTED) || status.equals(IN_PROGRESS)|| status.equals(ACCEPTED)||status.equals(SCHEDULED)) {
						return null; // no result yet
					}
					if (status.equals(COMPLETED)|| status.equals(SUCCEEDED)) {
                       Map<String,Object> didMap=Params.formatResult(result);
						if (didMap == null) throw new RemoteException("No asset in completed job result: " + body);
						return RemoteAsset.create(this, JSON.toPrettyString(didMap));
					}
				}
				throw new TODOException("status code not handled: " + statusCode);
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			throw new JobFailedException(" Client Protocol Exception :", e);
		} catch (IOException e) {
			throw new JobFailedException(" IOException occurred  Exception :", e);
		}
	}

	@Override
	public Job invoke(Operation operation, Map<String, Object> params) {
		Map<String, Object> request = new HashMap<String, Object>(2);
		request.put(OPERATION, operation.getAssetID());
		request.put(PARAMS, Params.formatParams(operation, params));
		return invoke(request,operation.getAssetID());
	}

	/**
	 * Invokes request on this RemoteAgent
	 *
	 * @param request Invoke request
	 * @return Job for this request
	 * @throws RuntimeException for protocol errors
	 */
	private Job invoke(Map<String, Object> request,String assetID) {
		Map<String, Object> req = (Map<String, Object>)request.get("params");
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpPost httppost = new HttpPost(getInvokeAsyncURI(assetID));
		StringEntity entity = new StringEntity(JSON.toPrettyString(req), ContentType.APPLICATION_JSON);
		httppost.setEntity(entity);
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(httppost);

			try {
				return RemoteAgent.createJob(this, response);
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			throw new JobFailedException(" Client Protocol Exception :", e);
		} catch (IOException e) {
			throw new JobFailedException(" IOException occurred  Exception :", e);
		}
	}

	@Override
	public Job invokeAsync(Operation operation,Map<String,Object> params){

		Map<String,Object> paramValueMap= Params.formatParams(operation, params);

		// check if the mode is sync else throw exception
		if(!isModeSupported(operation,ASYNC)){
			throw new TODOException("Mode must be Async for this operation");
		}

		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpPost httppost = new HttpPost(getInvokeAsyncURI(operation.getAssetID()));
		StringEntity entity = new StringEntity(JSON.toPrettyString(paramValueMap), ContentType.APPLICATION_JSON);
		httppost.setEntity(entity);
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(httppost);

			try {
				return RemoteAgent.createJob(this, response);
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			throw new JobFailedException(" Client Protocol Exception :", e);
		} catch (IOException e) {
			throw new JobFailedException(" IOException occurred  Exception :", e);
		}
	}
	/**
	 *API to invoke a sync operation
	 * @param operation operation asset
	 * @param params params contain int the input data need for the operation
	 * @return Map of operation result
	 */
	public Map<String, Object> invokeResult(Operation operation,Map<String, Object> params){

		// this will validate if the required input is provided or not
		Map<String,Object> paramValueMap= Params.formatParams(operation, params);

		// check if the mode is sync else throw exception
		if(!isModeSupported(operation,SYNC)){
			throw new TODOException("Mode must be sync for this operation");
		}

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(getInvokeSyncURI(operation.getAssetID()));
		Map<String,Object> resultMap = new HashMap<>();
//        addAuthHeaders(httpget);
		// TODO if params is a map of asset then form proper entity
			StringEntity entity = new StringEntity(JSON.toPrettyString(paramValueMap), ContentType.APPLICATION_JSON);
			httppost.setEntity(entity);
			CloseableHttpResponse response;
			try {
				response = httpclient.execute(httppost);
				if(response.getStatusLine().getStatusCode() ==200){
					//TODO return an Memory Asset if result type is an asset
					String body = Utils.stringFromStream(response.getEntity().getContent());
					return Params.formatResult(JSON.toMap(body));
				}


			} catch (ClientProtocolException e) {
				throw new JobFailedException(" Client Protocol Exception :", e);
			} catch (IOException e) {
				throw new JobFailedException(" IOException occurred  Exception :", e);
			}
		return resultMap;
	}


	private boolean isModeSupported(Operation operation,String mode) {
		Map<String, Object> metaData = operation.getOperationSpec();
		JSONArray modes = (JSONArray) metaData.get(MODE);
		 if(modes.contains(mode)){
		 	return true;
		 }
		 return false;


	}

	/**
	 * API to get List of map of all Metadata
	 *
	 * @param marketAgentUrl
	 * @return List<Map<String, Object>> marketMetaData
	 */
	private List<Map<String, Object>> getAllMarketMetaData(String marketAgentUrl) {
		URI uri = getMarketLURI(marketAgentUrl);
		HttpGet httpget = new HttpGet(uri);
		addAuthHeaders(httpget);
		CloseableHttpResponse response = HTTP.execute(httpget);
		List<Map<String, Object>> result;

		try {
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 404) {
				throw new RemoteException("Listing ID not found for at: " + statusCode);
			} else if (statusCode == 200) {
				String body = Utils.stringFromStream(HTTP.getContent(response));
				try {
					result =
						new ObjectMapper().readValue(body, List.class);

					return result;

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				throw new TODOException("status code not handled: " + statusCode);
			}
		} finally {
			HTTP.close(response);
		}
		return Collections.emptyList();
	}

	/**
	 * API to get Market agent instance from the Agent
	 * @param listingData
	 * @param marketAgentUrl
	 * @return
	 */
	private String createMarketAgentInstance(Map<String, Object> listingData, String marketAgentUrl) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(getMarketLURI(marketAgentUrl));

		addAuthHeaders(httpPost);
		httpPost.addHeader("Accept", "application/json");

		httpPost.setEntity(new StringEntity(JSON.toPrettyString(listingData), ContentType.APPLICATION_JSON));

		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				String body = Utils.stringFromStream(HTTP.getContent(response));
				return body;
			} else if (statusCode == 403) {
				throw new TODOException("Can't modify listing: only listing owner can do so" + statusCode);
			} else {
				throw new TODOException("status code not handled: " + statusCode);
			}
		} catch (ClientProtocolException e) {
			throw new JobFailedException(" Client Protocol Expectopn :", e);
		} catch (IOException e) {
			throw new JobFailedException(" IOException occured  Expectopn :", e);
		} finally {
			HTTP.close(response);
		}
	}

	/**
	 * API to get the Market metadata from the agent by providing market URL.
	 * @param marketAgentUrl market url
	 * @return
	 */
	private String getMarketMetaData(String marketAgentUrl) {
		HttpGet httpget = new HttpGet(getMarketLURI(marketAgentUrl));
		addAuthHeaders(httpget);
		CloseableHttpResponse response = HTTP.execute(httpget);
		try {
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 404) {
				throw new RemoteException("Asset ID not found for at: " + statusCode);
			} else if (statusCode == 200) {
				String body = Utils.stringFromStream(HTTP.getContent(response));
				return body;
			} else {
				throw new TODOException("status code not handled: " + statusCode);
			}
		} finally {
			HTTP.close(response);
		}
	}

	private String updateMarketMetaData(Map<String, Object> listingData, String marketAgentUrl) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPut put = new HttpPut(getMarketLURI(marketAgentUrl));

		addAuthHeaders(put);
		put.addHeader("Accept", "application/json");

		put.setEntity(new StringEntity(JSON.toPrettyString(listingData), ContentType.APPLICATION_JSON));

		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(put);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				String body = Utils.stringFromStream(HTTP.getContent(response));
				return body;
			} else if (statusCode == 403) {
				throw new TODOException("Can't modify listing: only listing owner can do so" + statusCode);
			} else {
				throw new TODOException("status code not handled: " + statusCode);
			}
		} catch (ClientProtocolException e) {
			throw new JobFailedException(" Client Protocol Expectopn :", e);
		} catch (IOException e) {
			throw new JobFailedException(" IOException occured  Expectopn :", e);
		} finally {
			HTTP.close(response);
		}
	}

	/**
	 * Gets market URI for this agent
	 *
	 * @return The URI for listing metadata
	 * @throws UnsupportedOperationException if the agent does not support the Meta API (no endpoint defined)
	 * @throws IllegalArgumentException      on invalid URI for asset metadata
	 */
	private URI getMarketLURI(String marketAgentUrl) {
		String marketEndpoint = getMarketEndpoint();
		if (marketEndpoint == null)
			throw new UnsupportedOperationException("This agent does not support the Market API (no endpoint defined)");
		try {
			return new URI(marketEndpoint + marketAgentUrl);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Can't create valid URI for asset metadata", e);
		}
	}

	@Override
	public Listing getListing(String id) {
		return RemoteListing.create(this, id);
	}

	@Override
	public Purchase getPurchase(String id) {
		return RemotePurchase.create(this, id);
	}

	@Override
	public Listing createListing(Map<String, Object> listingData) {
		String response = createMarketAgentInstance(listingData, LISTING_URL);
		String id = JSON.toMap(response).get("id").toString();
		return RemoteListing.create(this, id);
	}

	/**
	 * API to update the data of existing listing.
	 * if the listing id passed is not exist it will throw Exception
	 *
	 * @param newValue map of new value need to be updated for listing
	 * @return Listing return updated listing instance
	 */
	public Listing updateListing(Map<String, Object> newValue) {

		String id = newValue.get(ID).toString();
		if (id == null) {
			throw new GenericException("Listing ID not found");
		}
		updateMarketMetaData(newValue, LISTING_URL + "/" + id);
		return RemoteListing.create(this, id);

	}

	/**
	 * API to get the listing meta data
	 *
	 * @param id id
	 * @return metadata
	 */
	public Map<String, Object> getListingMetaData(String id) {
		String response = getMarketMetaData(LISTING_URL + "/" + id);
		return JSON.toMap(response);
	}

	/**
	 * API to get all listing metaData.It may ab very heavy call .
	 *
	 * @return List of all listing
	 */
	public List<Listing> getAllListing() {

		List<Map<String, Object>> result = getAllMarketMetaData(LISTING_URL);

		return result.stream()
			.map(p -> RemoteListing.create(this, p.get(ID).toString()))
			.collect(Collectors.toList());

	}

	/**
	 * APi to get all the listing that belong to user id passed in the argument
	 *
	 * @param userID user id for which the listing data need to be retrieved form agent
	 * @return List of all listing belong to given user id
	 */
	public List<RemoteListing> getAllListing(String userID) {

		List<Map<String, Object>> result = getAllMarketMetaData(LISTING_URL);

		return result.stream()
			.map(p -> RemoteListing.create(this, p.get("id").toString()))
			.collect(Collectors.toList());

	}

	/**
	 * API to create the Purchase object
	 *
	 * @param data map of meta data need to create purchase instance
	 * @return Purchase
	 */
	public Purchase createPurchase(Map<String, Object> data) {
		String response = createMarketAgentInstance(data, PURCHASE_URL);
		String id = JSON.toMap(response).get(ID).toString();
		return RemotePurchase.create(this, id);
	}

	/**
	 * API to get the Purchase MetaData
	 *
	 * @param id id
	 * @return purchaseMetadata
	 */
	public Map<String, Object> getPurchaseMetaData(String id) {
		String response = getMarketMetaData( PURCHASE_URL+ "/" + id);
		return JSON.toMap(response);
	}

	/**
	 * API to update the Purchase
	 *
	 * @param newValue map of new value to update purchase
	 * @return Purchase
	 */
	public Purchase updatePurchase(Map<String, Object> newValue) {

		String id = newValue.get(ID).toString();
		if (id == null) {
			throw new GenericException("Listing ID not found");
		}
		updateMarketMetaData(newValue, PURCHASE_URL + "/" + id);
		return RemotePurchase.create(this, id);

	}

	/**
	 * Gets Auth URI for this agent
	 * @param  path auth path
	 * @return The URI for listing metadata
	 * @throws UnsupportedOperationException if the agent does not support the Meta API (no endpoint defined)
	 * @throws IllegalArgumentException      on invalid URI for asset metadata
	 */
	private URI getAuthURI(String path) {
		String authEndpoint = getAuthEndpoint();
		if (authEndpoint == null)
			throw new UnsupportedOperationException("This agent does not support the Auth API (no endpoint defined)");
		try {
			return new URI(authEndpoint + "/" + path);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Can't create valid URI for asset metadata", e);
		}
	}

	/**
	 * API to get the logged in user details from the Agent
	 *
	 * @return userDetails map
	 */
	public Map<String, Object> getUserDetails() {

		HttpGet httpget = new HttpGet(getAuthURI(USER));
		addAuthHeaders(httpget);
		CloseableHttpResponse response = HTTP.execute(httpget);
		try {
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 404) {
				throw new RemoteException("Auth not found for at: " + statusCode);
			} else if (statusCode == 200) {
				String body = Utils.stringFromStream(HTTP.getContent(response));
				// updating the user details in map
				account.getUserDataMap().putAll(JSON.toMap(body));
				return JSON.toMap(body);
			} else {
				throw new TODOException("status code not handled: " + statusCode);
			}
		} finally {
			HTTP.close(response);
		}

	}

	/**
	 * The will create the token base on user name and password configured
	 * @param account remote account reference
	 * @return new token
	 */
	private void createToken(RemoteAccount account) {

		// TODO this probably needs refactoring
		HttpPost httpPost = new HttpPost(getAuthURI(TOKEN));
		CloseableHttpResponse response;
		try {
			String username = account.getCredentials().get(USER_NAME).toString();
			String password = account.getCredentials().get(PASSWORD).toString();

			response = HTTP.executeWithAuth(httpPost, username, password);
			try {
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 404) {
					throw new RemoteException("Asset ID not found for at: " + "");
				}
				if (statusCode == 200) {
					String body = Utils.stringFromStream(response.getEntity().getContent());
					String id = JSON.parse(body);
					updateAccountData(id);
				}
				else {
					throw new TODOException("Result not handled: " + statusLine);
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * API to get the content of an Asset based on Asset id
	 * @param assetId asset id
	 * @return Stream of asset content
	 */
	public InputStream getContentStream(String assetId) {
		URI uri = getStorageURI(assetId);
		HttpGet httpget = new HttpGet(uri);
		addAuthHeaders(httpget);
		HttpResponse response = HTTP.execute(httpget);
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (statusCode == 404) {
			throw new RemoteException("Asset ID not found at: " + uri);
		}
		if (statusCode == 200) {
			return HTTP.getContent(response);
		}
		throw new TODOException("status code not handled: " + statusCode);
	}

	/**
	 * API to update the Account Data based on response received from the Agent
	 *
	 *
	 * @param token auth token
	 */
	private void updateAccountData(String token) {
		if (null == account) {
			return;
		}
		account.getUserDataMap().put(TOKEN, token);

	}

}
