package sg.dex.starfish.impl.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Consts;
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
import org.apache.http.util.EncodingUtils;

import sg.dex.starfish.*;
import sg.dex.starfish.exception.*;
import sg.dex.starfish.impl.AAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.HTTP;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Params;
import sg.dex.starfish.util.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class implementing a remote storage agent using the Storage API
 *
 * @author Mike
 */
public class RemoteAgent extends AAgent implements Invokable, MarketAgent {

    private static final String LISTING_URL = "/listings";
    private static final String PURCHAISNG_URL = "/purchases";
    private final Account account;

    /**
     * Creates a RemoteAgent with the specified Ocean connection and DID
     *
     * @param ocean Ocean connection to use
     * @param did   DID for this agent
     */
    protected RemoteAgent(Ocean ocean, DID did, Account account) {
        super(ocean, did);
        this.account = (account == null) ? defaultAccount() : account;
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
     * Creates a RemoteAgent with the specified Ocean connection, DID
     * and Account
     *
     * @param ocean Ocean connection to use
     * @param did   DID for this agent
     * @param account Account for this agent
     * @return RemoteAgent
     */
    public static RemoteAgent create(Ocean ocean, DID did, Account account) {
        return new RemoteAgent(ocean, did, account);
    }

    private Account defaultAccount() {
	 RemoteAccount account = new RemoteAccount(Utils.createRandomHexString(32), null);
	account.setCredential("username", "test");
	account.setCredential("password", "foobar");
	return account;
    }

    /**
     * Invokes request on this RemoteAgent
     *
     * @param agent    the remote
     * @param response
     * @return Job for this request
     * @throws RuntimeException for protocol errors
     */
    private static Job createJobWith200(RemoteAgent agent, HttpResponse response) {
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
        if (statusCode == 200) {
            return RemoteAgent.createJobWith200(agent, response);
        }
        String reason = statusLine.getReasonPhrase();
        if ((statusCode) == 400) {
            throw new IllegalArgumentException("Bad invoke request: " + reason);
        }
        throw new GenericException("Internal Server Error");
    }

    public RemoteAgent connect(Account acc) {
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
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
     }

	void addAuthHeaders(HttpRequest request) {
		final CharArrayBuffer buffer = new CharArrayBuffer(32);
		String token = null;

		if (account.getCredentials().get("token") != null) {
			token = account.getCredentials().get("token").toString();
		}
		if (token != null) {
			buffer.append("token ");
			buffer.append(token);
		} else {
			String username = account.getCredentials().get("username").toString();
			String password = account.getCredentials().get("password").toString();
			final StringBuilder tmp = new StringBuilder();
			tmp.append(username);
			tmp.append(":");
			tmp.append((password == null) ? "null" : password);
			final Base64 base64codec = new Base64(0);
			final byte[] base64password = base64codec.encode(EncodingUtils.getBytes(tmp.toString(), Consts.UTF_8.name()));
			buffer.append("Basic ");
			buffer.append(base64password, 0, base64password.length);

		}
		String header = AUTH.WWW_AUTH_RESP;
		String value = buffer.toString();
		// FIXME: remove this debugging
		System.out.println("account = " + account);
		System.out.println("RemoteAgent.addAuthHeaders(" + header + ", " + value + ")");
		request.setHeader(header, value);
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
            } else if (statusCode == 200) {
                String body = Utils.stringFromStream(HTTP.getContent(response));
                // TODO: consider if this this an operation rather than data asset?
                return RemoteAsset.create(this, body);
            } else {
                throw new TODOException("status code not handled: " + statusCode);
            }
        } finally {
            HTTP.close(response);
        }
    }

    /**
     * API to check if the Asset is present if present it will return true else false.
     *
     * @param assetId
     * @return
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
    public RemoteAsset uploadAsset(Asset a) {
        RemoteAsset remoteAsset;

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
     * @return
     */
    private void uploadAssetContent(Asset asset) {
        // get the storage API to upload the Asset content
        URI uri = getStorageURI(asset.getAssetID());
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost post = new HttpPost(uri);
        addAuthHeaders(post);
        post.addHeader("Accept", "application/json");

        InputStream assetContentAsStream = new ByteArrayInputStream(asset.getContent());
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
     *
     * @return The URI for this agent's invoke endpoint
     * @throws RuntimeException on URI syntax errors
     */
    public URI getInvokeURI() {
        try {
            return new URI(getInvokeEndpoint() + "/invokesync");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets URI for this agent's endpoint for jobID
     *
     * @param jobID of the URI to create
     * @return The URI for this agent's invoke endpoint
     * @throws IllegalArgumentException on invalid URI for jobID
     */
    private URI getJobURI(String jobID) {
        try {
            return new URI(getInvokeEndpoint() + "/jobs/" + jobID);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Can't create valid URI for job: " + jobID, e);
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
            return new URI(metaEndpoint + "/data/" + assetID);
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
            return new URI(metaEndpoint + "/data");
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
        Map<String, Object> request = new HashMap<String, Object>(2);
        request.put("operation", operation.getAssetID());
        request.put("params", Params.formatParams(operation, params));
        return invoke(request);
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
                    String status = (String) result.get("status");
                    if (status == null) throw new RemoteException("No status in job result: " + body);
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
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            throw new JobFailedException(" Client Protocol Expectopn :", e);
        } catch (IOException e) {
            throw new JobFailedException(" IOException occured  Expectopn :", e);
        }
    }

    @Override
    public Job invoke(Operation operation, Map<String, Asset> params) {
        Map<String, Object> request = new HashMap<String, Object>(2);
        request.put("operation", operation.getAssetID());
        request.put("params", Params.formatParams(operation, params));
        return invoke(request);
    }

    /**
     * Invokes request on this RemoteAgent
     *
     * @param request Invoke request
     * @return Job for this request
     * @throws RuntimeException for protocol errors
     */
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
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            throw new JobFailedException(" Client Protocol Expectopn :", e);
        } catch (IOException e) {
            throw new JobFailedException(" IOException occured  Expectopn :", e);
        }
    }

    /**
     * API for Listing
     *
     * @param marketAgentUrl
     * @return
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
    public Purchase getPurchasing(String id) {
        return RemotePurchase.create(this, id);
    }

    @Override
    public Listing createListing(Map<String, Object> listingData) {
        String response = createMarketAgentInstance(listingData, LISTING_URL);
        String id = JSON.toMap(response).get("id").toString();
        return RemoteListing.create(this, id);
    }

    /**
     * API to update the Lising data
     *
     * @param newValue
     * @return
     */
    public Listing updateListing(Map<String, Object> newValue) {

        String id = newValue.get("id").toString();
        if (id == null) {
            throw new GenericException("Listing ID not found");
        }
        updateMarketMetaData(newValue, LISTING_URL + "/" + id);
        return RemoteListing.create(this, id);

    }

    /**
     * API to get the listing meta data
     *
     * @param id
     * @return
     */
    public Map<String, Object> getListingMetaData(String id) {
        String response = getMarketMetaData(LISTING_URL + "/" + id);
        return JSON.toMap(response);
    }

    /**
     * API to get all listing metaData.It may ab very heavy call .
     *
     * @return
     */
    public List<Listing> getAllListing() {

        List<Map<String, Object>> result = getAllMarketMetaData(LISTING_URL);

        return result.stream()
                .map(p -> RemoteListing.create(this, p.get("id").toString()))
                .collect(Collectors.toList());

    }

    /**
     * APi to get listing by userID
     *
     * @param userID
     * @return
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
     * @param data
     * @return
     */
    public Purchase createPurchase(Map<String, Object> data) {
        String response = createMarketAgentInstance(data, PURCHAISNG_URL);
        String id = JSON.toMap(response).get("id").toString();
        return RemotePurchase.create(this, id);
    }

    /**
     * API to get the Purchase MetaData
     *
     * @param id
     * @return
     */
    public Map<String, Object> getPurchaseMetaData(String id) {
        String response = getMarketMetaData(PURCHAISNG_URL + "/" + id);
        return JSON.toMap(response);
    }

    /**
     * API to update the Purchase
     *
     * @param newValue
     * @return
     */
    public Purchase updatePurchase(Map<String, Object> newValue) {

        String id = newValue.get("id").toString();
        if (id == null) {
            throw new GenericException("Listing ID not found");
        }
        updateMarketMetaData(newValue, PURCHAISNG_URL + "/" + id);
        return RemotePurchase.create(this, id);

    }

    /**
     * Gets Auth URI for this agent
     *
     * @return The URI for listing metadata
     * @throws UnsupportedOperationException if the agent does not support the Meta API (no endpoint defined)
     * @throws IllegalArgumentException      on invalid URI for asset metadata
     */
    private URI getAuthURI(String authpath) {
        String authEndpoint = getAuthEndpoint();
        if (authEndpoint == null)
            throw new UnsupportedOperationException("This agent does not support the Market API (no endpoint defined)");
        try {
            return new URI(authEndpoint + "/" + authpath);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Can't create valid URI for asset metadata", e);
        }
    }

    /**
     * API to get the logged in user details from the Agent
     *
     * @return
     */
    public Map<String, Object> getUserDetails() {

        HttpGet httpget = new HttpGet(getAuthURI("user"));
        addAuthHeaders(httpget);
        CloseableHttpResponse response = HTTP.execute(httpget);
        try {
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 404) {
                throw new RemoteException("Auth not found for at: " + statusCode);
            } else if (statusCode == 200) {
                String body = Utils.stringFromStream(HTTP.getContent(response));
                ((RemoteAccount) account).getUserDataMap().putAll(JSON.toMap(body));
                return JSON.toMap(body);
            } else {
                throw new TODOException("status code not handled: " + statusCode);
            }
        } finally {
            HTTP.close(response);
        }

    }

    /**
     * API to get the Oath Token from the Agent
     *
     * @return
     */
    public String getToken() {

        HttpGet httpget = new HttpGet(getAuthURI("token"));
        addAuthHeaders(httpget);
        CloseableHttpResponse response = HTTP.execute(httpget);
        try {
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 404) {
                throw new RemoteException("Asset ID not found for at: " + statusCode);
            } else if (statusCode == 200) {
                String body = Utils.stringFromStream(HTTP.getContent(response));
                List<String> allTokenLst = JSON.parse(body);
                ((RemoteAccount) account).getUserDataMap().put("token", allTokenLst.get(0));
                return allTokenLst.get(0);
            } else {
                throw new TODOException("status code not handled: " + statusCode);
            }
        } finally {
            HTTP.close(response);
        }


    }

    /**
     * The will create the token base on user name and password configured
     *
     * @return new token
     */
    public String createToken(Account account) {
        String url = "token";
        HttpPost httpPost = new HttpPost(getAuthURI(url));
        CloseableHttpResponse response;
        try {
            String username = account.getCredentials().get("username").toString();
            String password = account.getCredentials().get("password").toString();

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
                    return id;
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

    /**
     * API to update the Account Data based on response received from the Agent
     *
     *
     * @param token
     */
    private void updateAccountData(String token) {
        if (null == account) {
            return;
        }


        if (null != ((RemoteAccount) account).getUserDataMap().get("token")) {
            List<String> tokenLst = ((List<String>) ((RemoteAccount) account).getUserDataMap().get("token"));
            tokenLst.add(token);
        } else {
            List<String> tokenLst = new ArrayList<>();
            tokenLst.add(token);
            ((RemoteAccount) account).getUserDataMap().put("token", tokenLst);
        }

    }

}
