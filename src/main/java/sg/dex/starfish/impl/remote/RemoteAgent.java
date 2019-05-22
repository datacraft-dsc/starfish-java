package sg.dex.starfish.impl.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AUTH;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
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
 * This class represent the Remote Agent.
 * <p>
 * The class {@code RemoteAgent} includes methods for registering and asset,
 * upload an asset, find and asset ,get the metadata Endpoint ,
 * get the Storage Endpoint,get the market Endpoint.
 * This class also provide method for the invokable service .
 * </p>
 *
 * @author Mike
 * @version 0.5
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
        this.account = account;
    }

    /**
     * Creates a RemoteAgent with the specified Ocean connection, DID
     * and RemoteAccount
     *
     * @param ocean   Ocean connection to use
     * @param did     DID for this agent
     * @param account RemoteAccount for this agent
     * @return RemoteAgent
     */
    public static RemoteAgent create(Ocean ocean, DID did, RemoteAccount account) {
        if (ocean == null) throw new IllegalArgumentException("Ocean connection cannot be null for remote agent");
        if (did == null) throw new IllegalArgumentException("DID cannot be null for remote agent");
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
     * @param response A valid successful response from the remote Invoke API
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
        if (statusCode == 201) {
            return RemoteAgent.createSuccessJob(agent, response);
        }
        String reason = statusLine.getReasonPhrase();
        if ((statusCode) == 400) {
            throw new IllegalArgumentException("Bad invoke request: " + reason);
        }
        throw new GenericException("Internal Server Error");
    }

    /**
     * This method is to add the remote account details to RemoteAgent.
     * This will return a new RemoteAgent with have the Remote Account details passed as an argument
     *
     * @param acc remote account that need to be updated in remote Agent
     * @return New Remote Agent with has the Remote Account details
     */
    public RemoteAgent connect(RemoteAccount acc) {
        // TODO: get user token and store this in account
        return new RemoteAgent(ocean, did, acc);
    }

    @Override
    public ARemoteAsset registerAsset(Asset a) {
        URI uri = getMetaURI();
        CloseableHttpClient httpclient = HttpClients.createDefault();

        //TODO need to update this logic for bundle
        if (a.getMetadata().get(TYPE).equals(BUNDLE)) {
            // register all sub asset
            RemoteBundle remoteBundle = (RemoteBundle) a;
            Map<String, Object> allSubAsset = remoteBundle.getAll();
            for (String name : allSubAsset.keySet()) {
                Asset subAsset = (Asset) allSubAsset.get(name);
                return registerAsset(subAsset);

            }
        } else {
            HttpPost httpPost = new HttpPost(uri);
            addAuthHeaders(httpPost);
            httpPost.setEntity(HTTP.textEntity(a.getMetadataString()));
            return createRemoteAsset(uri, httpclient, httpPost);
        }

        throw new StarfishValidationException("issue while registering the Asset");
    }

    /**
     * This method  to get the Remote Asset
     *
     * @param uri
     * @param httpclient
     * @param httpPost
     * @return
     */
    private ARemoteAsset createRemoteAsset(URI uri, CloseableHttpClient httpclient, HttpPost httpPost) {
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
                throw new HttpResponseException(statusCode, statusLine.getReasonPhrase());
            } finally {
                response.close();
            }
        } catch (IOException e) {
            throw new RemoteException("Getting remote asset failed :", e);
        }
    }

    /**
     * This method  to add header to the HTTP request.
     *
     * @param request HttpRequest on which the header will be updated
     */
    private void addAuthHeaders(HttpRequest request) {
        if (account == null) {
            throw new AuthorizationException("User don`t have account credentials");
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
            if ((token == null) && (username == null) && (password == null)) {
                throw new AuthorizationException("Username or Token or Password is not available for given account :" + account);
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
     * @throws StorageException       if there is an error in retrieving the Asset
     * @throws JobFailedException     if there is a failure in a remote operation
     */
    @Override
    public ARemoteAsset getAsset(String id) {
        URI uri = getMetaURI(id);
        HttpGet httpget = new HttpGet(uri);
        addAuthHeaders(httpget);
        CloseableHttpResponse response;
        try {
            response = HTTP.execute(httpget);
            try {
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 404) {
                    throw new RemoteException("Asset ID not found for at: " + uri);
                } else if (statusCode == 200) {
                    String body = Utils.stringFromStream(HTTP.getContent(response));
                    Map<String, Object> metaMap = JSON.toMap(body);
                    return getRemoteAsset(body, metaMap);
                } else {

                    throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            throw new JobFailedException(" Getting asset by ID failed  :", e);
        }
    }

    /**
     * This method is used for getting the remote asset instance based on metaString passed
     *
     * @param metaString meta data of the asset
     * @param metaMap    map of the asset
     * @return the new remote asset created
     */
    private ARemoteAsset getRemoteAsset(String metaString, Map<String, Object> metaMap) {
        if (metaMap.get(TYPE).equals(OPERATION)) {
            return RemoteOperation.create(this, metaString);
        } else if (metaMap.get(TYPE).equals(DATA_SET)) {
            return RemoteAsset.create(this, metaString);
        } else if (metaMap.get(TYPE).equals(BUNDLE)) {
            return RemoteBundle.createBundle(this, metaMap);
        } else {
            throw new StarfishValidationException("Invalid Asset Type :" + metaMap.get(TYPE));
        }
    }

    @Override
    public ARemoteAsset getAsset(DID did) {
        return getAsset(did.getID());
    }

    /**
     * This method is to check if the Asset is already registered based on asset ID passed as parameter.
     * if the asset is already registered it will return true else false.
     *
     * @param assetId id of an asset to check if the aset is register
     * @return boolean true if asset is registered else false
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

        // asset already registered then only upload
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
     * This method is to upload the content of an data asset to this agent.
     * if the asset type is operation it will do nothing
     *
     * @param asset
     * @throws UnsupportedOperationException if the asset type is bundle
     */
    private void uploadAssetContent(Asset asset) {

        // check if the asset is operation or bundle it will not have content, so return without upload
        if (asset.getMetadata().get(TYPE).equals(BUNDLE)) {
            throw new UnsupportedOperationException("Unable to obtain DID for asset of class: " + getClass());
        } else if (asset.getMetadata().get(TYPE).equals(OPERATION)) {
            return;
        }

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
                    throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            throw new RemoteException(" Upload asset Content failed:  :", e);
        }
    }


    /**
     * Gets URI for this agent's invoke endpoint
     *
     * @param did did
     * @return The URI for this agent's invoke endpoint
     * @throws RuntimeException on URI syntax errors
     */
    private URI getInvokeSyncURI(Object did) {
        try {
            if (did == null) {
                return new URI(getInvokeEndpoint());
            } else {
                return new URI(getInvokeEndpoint() + INVOKE_SYNC + "/" + did.toString());
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets URI for this agent's invoke endpoint
     *
     * @param did did
     * @return The URI for this agent's invoke endpoint
     * @throws RuntimeException on URI syntax errors
     */
    private URI getInvokeAsyncURI(Object did) {
        try {
            if (did == null) {
                return new URI(getInvokeEndpoint());
            } else {
                return new URI(getInvokeEndpoint() + INVOKE_ASYNC + "/" + did.toString());
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
            String jobId = JSON.toMap(response).get(JOB_ID).toString();
            return new URI(getInvokeEndpoint() + JOBS + "/" + jobId);
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
            return new URI(metaEndpoint + DATA + "/" + assetID);
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
    private URI getStorageURI(String assetID) {
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
    private URL getURL(RemoteAsset remoteAsset) {
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
        return invoke(request, operation.getAssetID());
    }

    /**
     * Polls this agent for the Asset resulting from the given job ID
     *
     * @param jobID ID for the Job to poll
     * @return The asset resulting from this job ID if available, null otherwise.
     * @throws IllegalArgumentException If the job ID is invalid
     * @throws RemoteException          if there is a failure in a remote operation
     * @throws RuntimeException         for protocol errors
     */
    public Object pollJob(String jobID) {
        URI uri = getJobURI(jobID);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(uri);
        CloseableHttpResponse response;
        try {
            response = httpclient.execute(httpget);
            try {
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();

                if (statusCode == 200) {
                    String body = Utils.stringFromStream(response.getEntity().getContent());
                    Map<String, Object> result = JSON.toMap(body);
                    String status = (String) result.get(STATUS);
                    if (status == null) throw new RemoteException("No status in job result: " + body);
                    if (status.equals(STARTED) || status.equals(IN_PROGRESS) || status.equals(ACCEPTED) || status.equals(SCHEDULED)) {
                        return null; // no result yet
                    }
                    if (status.equals(COMPLETED) || status.equals(SUCCEEDED)) {

                        return result;
                    } else if (status.equals("error")) {
                        throw new JobFailedException("Error code: " + result.get("errorcode") +
                                "description is : " + result.get("description"));
                    } else {

                        throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                    }
                } else {
                    throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            throw new JobFailedException(" Job invocation failed:  :", e);
        }
    }

    @Override
    public Job invoke(Operation operation, Map<String, Object> params) {
        Map<String, Object> request = new HashMap<String, Object>(2);
        request.put(OPERATION, operation.getAssetID());
        request.put(PARAMS, Params.formatParams(operation, params));
        return invoke(request, operation.getAssetID());
    }

    /**
     * Invokes request on this RemoteAgent
     *
     * @param request Invoke request
     * @return Job for this request
     * @throws RuntimeException for protocol errors
     */
    private Job invoke(Map<String, Object> request, String assetID) {
        Map<String, Object> req = (Map<String, Object>) request.get("params");
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
    public Job invokeAsync(Operation operation, Map<String, Object> params) {

        Map<String, Object> paramValueMap = Params.formatParams(operation, params);

        // check if the mode is sync else throw exception
        if (!isModeSupported(operation, ASYNC)) {
            throw new StarfishValidationException("Mode must be Async for this operation");
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
     * API to invoke a sync operation
     *
     * @param operation operation asset
     * @param params    params contain int the input data need for the operation
     * @return Map of operation result
     */
    public Map<String, Object> invokeResult(Operation operation, Map<String, Object> params) {

        // this will validate if the required input is provided or not
        Map<String, Object> paramValueMap = Params.formatParams(operation, params);

        // check if the mode is sync else throw exception
        if (!isModeSupported(operation, SYNC)) {
            throw new StarfishValidationException("Mode must be sync for this operation");
        }

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(getInvokeSyncURI(operation.getAssetID()));
//        addAuthHeaders(httpget);
        // TODO if params is a map of asset then form proper entity
        StringEntity entity = new StringEntity(JSON.toPrettyString(paramValueMap), ContentType.APPLICATION_JSON);
        httppost.setEntity(entity);
        CloseableHttpResponse response;
        try {
            response = httpclient.execute(httppost);
            try {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == 200) {
                    String body = Utils.stringFromStream(response.getEntity().getContent());
                    return JSON.toMap(body);
                } else {
                    throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            throw new JobFailedException(" Job invocation failed:  :", e);
        }
    }


    private boolean isModeSupported(Operation operation, String mode) {
        Map<String, Object> metaData = operation.getOperationSpec();
        JSONArray modes = (JSONArray) metaData.get(MODE);
        return modes.contains(mode);


    }

    /**
     * API to get List of map of all Metadata
     *
     * @param marketAgentUrl
     * @return List<Map < String, Object>> marketMetaData
     */
    private List<Map<String, Object>> getAllMarketMetaData(String marketAgentUrl) {
        URI uri = getMarketLURI(marketAgentUrl);
        HttpGet httpget = new HttpGet(uri);
        addAuthHeaders(httpget);
        CloseableHttpResponse response;
        List<Map<String, Object>> result;

        try {
            response = HTTP.execute(httpget);

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
                    throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            throw new RemoteException(" Getting market data failed  :", e);
        }
        return Collections.emptyList();
    }

    /**
     * API to get Market agent instance from the Agent
     *
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
            try {


                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    String body = Utils.stringFromStream(HTTP.getContent(response));
                    return body;
                } else {
                    throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            throw new RemoteException(" Create Market Instance failed:  :", e);
        }
    }

    /**
     * API to get the Market metadata from the agent by providing market URL.
     *
     * @param marketAgentUrl market url
     * @return
     */
    private String getMarketMetaData(String marketAgentUrl) {
        HttpGet httpget = new HttpGet(getMarketLURI(marketAgentUrl));
        addAuthHeaders(httpget);
        try {
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
                    throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            throw new RemoteException(" Getting market Data failed:  :", e);
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
            try {
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    String body = Utils.stringFromStream(HTTP.getContent(response));
                    return body;
                } else {
                    throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            throw new RemoteException(" Updating market data failed:  :", e);
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
        String response = getMarketMetaData(PURCHASE_URL + "/" + id);
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
     *
     * @param path auth path
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

        CloseableHttpResponse response;
        try {
            response = HTTP.execute(httpget);
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
                    throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            throw new RemoteException(" Getting user details failed:  :", e);
        }

    }

    /**
     * The will create the token base on user name and password configured
     *
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
                } else {
                    throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            throw new RemoteException(" Create token failed:  :", e);
        }

    }

    /**
     * This method is  to get the content of an Asset based on Asset id passed.
     *
     * @param assetId of the asset
     * @return Stream of asset content
     */
    public InputStream getContentStream(String assetId) {
        URI uri = getStorageURI(assetId);
        HttpGet httpget = new HttpGet(uri);
        addAuthHeaders(httpget);
        CloseableHttpResponse response;
        try {
            response = HTTP.execute(httpget);
            try {
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 404) {
                    throw new RemoteException("Asset ID not found at: " + uri);
                }
                if (statusCode == 200) {
                    InputStream inputStream = HTTP.getContent(response);
                    return inputStream;
                } else {
                    throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            throw new RemoteException(" Getting Remote Asset content failed: ", e);
        }

    }

    /**
     * This method is  to update the token for this Agent
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
