package sg.dex.starfish.impl.remote;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
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
import sg.dex.starfish.util.AuthorizationException;
import sg.dex.starfish.util.StorageException;
import sg.dex.starfish.util.Utils;
import sg.dex.starfish.utils.HTTP;

/**
 * Class implementing a remote storage agent using the Storage API
 *
 * @author Mike
 *
 */
public class RemoteAgent extends AAgent implements Invokable {

	/**
	 * Creates a RemoteAgent with the specified Ocean connection and DID
	 *
	 * @param ocean Ocean connection to use
	 * @param did DID for this agent
	 */
	protected RemoteAgent(Ocean ocean, DID did) {
		super(ocean, did);
	}

	/**
	 * Creates a RemoteAgent with the specified Ocean connection and DID
	 *
	 * @param ocean Ocean connection to use
	 * @param did DID for this agent
	 */
	public static RemoteAgent create(Ocean ocean, DID did) {
		return new RemoteAgent(ocean, did);
	}

	@Override
	public RemoteAsset registerAsset(Asset a) {
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
			}
			finally {
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

	public InputStream getDownloadStream(RemoteAsset a) {
		URI uri = getStorageURI(a.getAssetID());
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

	private void addAuthHeaders(HttpRequest request) {
		request.setHeader("Authorization", "Basic QWxhZGRpbjpPcGVuU2VzYW1l");
	}

	/**
	 * Gets an asset for the given asset ID from this agent.
	 * Returns null if the asset ID does not exist.
	 *
	 * @param id The ID of the asset to get from this agent
	 * @throws AuthorizationException if requestor does not have access permission
	 * @throws StorageException if there is an error in retreiving the Asset
	 * @throws RemoteException if there is a failure in a remote operation
	 * @throws TODOException for unhandled status codes
	 * @return Asset The asset found
	 */
	@Override
	public RemoteAsset getAsset(String id) {
		URI uri = getMetaURI(id);
		HttpGet httpget = new HttpGet(uri);
		addAuthHeaders(httpget);
		CloseableHttpResponse response = HTTP.execute(httpget);
		try {
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 404) {
				throw new RemoteException("Asset ID not found for at: " + uri);
			}
			if (statusCode == 200) {
				String body = Utils.stringFromStream(HTTP.getContent(response));
				return RemoteAsset.create(this, body);
			}
			throw new TODOException("status code not handled: " + statusCode);
		}
		finally {
			HTTP.close(response);
		}
	}

	/**
	 * Uploads an asset to this agent. Registers the asset with the agent if required.
	 *
	 * Throws an exception if upload is not possible, with the following likely causes:
	 * - The agent does not support uploads of this asset type / size
	 * - The data for the asset cannot be accessed by the agent
	 *
	 * @param a Asset to upload
	 * @throws AuthorizationException if requestor does not have upload permission
	 * @throws StorageException if there is an error in uploading the Asset
	 * @throws TODOException when not implemented yet
	 * @return Asset An asset stored on the agent if the upload is successful
	 */
	@Override
	public Asset uploadAsset(Asset a) {
		throw new TODOException();
	}

	/**
	 * Gets a URL string for accessing the specified asset ID
	 *
	 * @param id The asset ID to address
	 * @throws TODOException when not implemented yet
	 * @return The URL for the asset as a String
	 */
	public String getAssetURL(String id) {
		throw new TODOException();
	}

	public URI getInvokeURI() {
		try {
			return new URI(getInvokeEndpoint() + "/invokesync");
		}
		catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private URI getJobURI(String jobID) {
		try {
			return new URI(getInvokeEndpoint() + "/jobs/" + jobID);
		}
		catch (URISyntaxException e) {
			throw new IllegalArgumentException("Can't create valid URI for job: " + jobID, e);
		}
	}

	private URI getMetaURI(String assetID) {
		String metaEndpoint = getMetaEndpoint();
		if (metaEndpoint == null)
			throw new UnsupportedOperationException("This agent does not support the Meta API (no endpoint defined)");
		try {
			return new URI(metaEndpoint + "/data/" + assetID);
		}
		catch (URISyntaxException e) {
			throw new IllegalArgumentException("Can't create valid URI for asset metadata with ID: " + assetID, e);
		}
	}

	public URI getStorageURI(String assetID) {
		String storageEndpoint = getStorageEndpoint();
		if (storageEndpoint == null) throw new UnsupportedOperationException(
				"This agent does not support the Storage API (no endpoint defined)");
		try {
			return new URI(storageEndpoint + "/" + assetID);
		}
		catch (URISyntaxException e) {
			throw new IllegalArgumentException("Can't create valid URI for asset storage with ID: " + assetID, e);
		}
	}

	private URI getMetaURI() {
		String metaEndpoint = getMetaEndpoint();
		if (metaEndpoint == null)
			throw new UnsupportedOperationException("This agent does not support the Meta API (no endpoint defined)");
		try {
			return new URI(metaEndpoint + "/data");
		}
		catch (URISyntaxException e) {
			throw new IllegalArgumentException("Can't create valid URI for asset metadata", e);
		}
	}

	public URL getURL(RemoteAsset remoteAsset) {
		String storageEndpoint = getStorageEndpoint();
		if (storageEndpoint == null) throw new IllegalStateException("No storage endpoint available for agent");
		try {
			return new URL(storageEndpoint + "/" + remoteAsset.getAssetID());
		}
		catch (MalformedURLException e) {
			throw new Error("Failed to get asset URL", e);
		}
	}

	/**
	 * Gets the storage endpoint for this agent
	 *
	 * @return The storage endpoint for this agent e.g.
	 *         "https://www.myagent.com/api/v1/storage"
	 */
	public String getStorageEndpoint() {
		return getEndpoint("Ocean.Storage.v1");
	}

	/**
	 * Gets the Invoke API endpoint for this agent
	 *
	 * @return The invoke endpoint for this agent e.g.
	 *         "https://www.myagent.com/api/v1/invoke"
	 */
	public String getInvokeEndpoint() {
		return getEndpoint("Ocean.Invoke.v1");
	}

	/**
	 * Gets the Meta API endpoint for this agent, or null if this does not exist
	 *
	 * @return The Meta API endpoint for this agent e.g.
	 *         "https://www.myagent.com/api/v1/meta"
	 */
	public String getMetaEndpoint() {
		return getEndpoint("Ocean.Meta.v1");
	}

	@Override
	public Job invoke(Operation operation, Asset... params) {
		Map<String, Object> request = new HashMap<String, Object>(2);
		request.put("operation", operation.getAssetID());
		request.put("params", Params.formatParams(operation, params));
		return invoke(request);
	}

	/**
	 * Polls this agent for the Asset resulting from the given job ID
	 *
	 * @param jobID
	 * @throws IllegalArgumentException If the job ID is invalid
	 * @throws RemoteException if there is a failure in a remote operation
	 * @throws TODOException for unhandled status codes
	 * @throws RuntimeException for protocol errors
	 * @return The asset resulting from this job ID if available, null otherwise.
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
					String status = (String) result.get("status");
					if (status == null) throw new RemoteException("No status in job result: " + body);
					if (status.equals("started") || status.equals("inprogress")) {
						return null; // no result yet
					}
					if (status.equals("started") || status.equals("inprogress")) {
						return null; // no result yet
					}
					if (status.equals("complete")) {
						String assetID = (String) result.get("result");
						if (assetID == null) throw new RemoteException("No asset in completed job result: " + body);
						return RemoteAsset.create(this, assetID);
					}
				}
				throw new TODOException("status code not handled: " + statusCode);
			}
			finally {
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
	public Job invoke(Operation operation, Map<String, Asset> params) {
		Map<String, Object> request = new HashMap<String, Object>(2);
		request.put("operation", operation.getAssetID());
		request.put("params", Params.formatParams(operation, params));
		return invoke(request);
	}

	private Job invoke(Map<String, Object> request) {
		String req = JSON.toString(request);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(getInvokeURI());
		StringEntity entity = new StringEntity(req, StandardCharsets.UTF_8);
		httppost.setEntity(entity);
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(httppost);
			try {
				return RemoteAgent.createJob(this, response);
			}
			finally {
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

	private static Job createJobWith200(RemoteAgent agent, HttpResponse response) {
		HttpEntity entity = response.getEntity();
		if (entity == null) throw new RuntimeException("Invoke failed: no response body");
		try {
			String body = Utils.stringFromStream(entity.getContent());
			return RemoteJob.create(agent, body);
		}
		catch (Exception e) {
			throw new RuntimeException("Invoke failed: " + e.getMessage(), e);
		}
	}

	/**
	 * Creates a remote invoke Job using the given HTTP response.
	 *
	 * @param response A valid successful response from the remote Invoke API
	 * @throws IllegalArgumentException for a bad invoke request
	 * @throws RuntimeException for protocol errors
	 * @return A job representing the remote invocation
	 */
	public static Job createJob(RemoteAgent agent, HttpResponse response) {
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (statusCode == 200) {
			return RemoteAgent.createJobWith200(agent, response);
		}
		String reason = statusLine.getReasonPhrase();
		if ((statusCode) == 400) {
			throw new IllegalArgumentException("Bad invoke request: " + reason);
		}
		throw new RuntimeException("Invoke request failed with code " + statusCode + ": " + reason);
	}

}
