package sg.dex.starfish.impl.remote;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.*;
import org.apache.http.auth.AUTH;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.json.simple.JSONArray;
import sg.dex.crypto.Hash;
import sg.dex.starfish.*;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.dexchain.DexResolver;
import sg.dex.starfish.exception.*;
import sg.dex.starfish.impl.AAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static sg.dex.starfish.constant.Constant.*;

/**
 * This class represent the Remote Agent.
 * <p>
 * The class {@code RemoteAgent} includes methods for registering and asset,
 * upload an asset, find and asset ,get the metadata Endpoint , get the Storage
 * Endpoint,get the market Endpoint. This class also provide method for the
 * invokable service .
 * </p>
 *
 * @author Mike
 * @version 0.5
 */
public class RemoteAgent extends AAgent implements Invokable, MarketAgent {


    private final RemoteAccount account;

    /**
     * Creates a RemoteAgent with the specified Resolver and DID
     *
     * @param resolver Resolver
     * @param did      DID for this agent
     * @param account  account
     */
    private RemoteAgent(Resolver resolver, DID did, RemoteAccount account) {
        super(resolver, did);
        this.account = account;
    }

    /**
     * Creates a RemoteAgent with the specified URL of Agent adn user Account
     * detial.
     * This method is used to get an instance of an Remote Agent based on Agent
     * URL.
     * IT will connect to an Agent and get the DDO and did from the Agent using
     * the
     * Status Endpoint and create respective  Agent Instance
     *
     * @param account RemoteAccount for this agent
     * @return RemoteAgent
     */
    private static RemoteAgent create(String url, RemoteAccount account)
            throws URISyntaxException {

        Map<String, Object> result = getDDOByURL(url, account);
        Map<String, Object> serviceMap = new HashMap<>();

        serviceMap.put(SERVICE, result.get(SERVICE));
        String didString = result.get(ID).toString();

        DID did = sg.dex.starfish.util.DID.parse(didString);

        Resolver resolver = DexResolver.create();

        resolver.registerDID(did, JSON.toPrettyString(serviceMap));

        return new RemoteAgent(resolver, did, account);
    }


    /**
     * Invokes request on this RemoteAgent
     *
     * @param agent    the remote
     * @param response A valid successful response from the remote Invoke API
     * @return Job for this request
     * @throws RuntimeException for protocol errors
     */
    private static Job createSuccessJob(RemoteAgent agent, Map<String, Object> paramsSpecs,
                                        HttpResponse response) {
        HttpEntity entity = response.getEntity();
        if (entity == null)
            throw new RemoteException("Invoke failed: no response body");
        try {
            String body = Utils.stringFromStream(entity.getContent()).trim();
            String jobID;

            if (body.isEmpty()) {
                throw new RemoteException("Invoke failed: empty body returned");
            }

            // TODO: Fix according to DEP once reference implementations are stable
            if (body.startsWith("\"")) {
                // interpret as a JOB ID JSON String
                jobID = JSON.parse(body);
            } else {
                if (body.startsWith("{")) {
                    // interpret as a JSON map, should contain jobid
                    Map<String, Object> json = JSON.parse(body);

                    jobID = json.get("job-id") != null ? json.get("job-id").toString() : null;
                    if (jobID == null) throw
                            new RemoteException("Invoke failed: no job-id in body: " + body);
                } else {
                    // interpret as a raw job ID
                    jobID = body;
                }
            }
            return RemoteJob.create(agent, paramsSpecs, jobID);
        } catch (Exception e) {
            throw Utils.sneakyThrow(e);
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
    private static Job createJob(RemoteAgent agent, Map<String, Object> paramsSpecs,
                                 HttpResponse response) {

        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if ((statusCode == 201) || (statusCode == 200)) {
            return RemoteAgent.createSuccessJob(agent, paramsSpecs, response);
        }

        throw new RemoteException("Job creation failed" + statusLine.getReasonPhrase());

    }

    /**
     * This method is to create a Remote Agent Instance based on Resolver,
     * DID and RemoteAccount
     *
     * @param resolver it will be used to resolver the DID and get DDO
     * @param did      did of the agent
     * @param acc      remote account
     * @return New Remote Agent Instance
     */
    public static RemoteAgent connect(Resolver resolver, DID did, RemoteAccount acc) {
        return new RemoteAgent(resolver, did, acc);
    }

    /**
     * This method is to create a Remote Agent Instance based on Resolver,
     * DID and RemoteAccount
     *
     * @param did did of the agent
     * @param acc remote account that need to be updated in remote Agent
     * @return New Remote Agent Instance
     */
    public static RemoteAgent connect(DID did, RemoteAccount acc) {
        if (null == did || null == acc) {
            throw new IllegalArgumentException("param DID/RemoteAccount cannot be null. ");
        }
        return new RemoteAgent(DexResolver.create(), did, acc);
    }

    /**
     * This method is to create a Remote Agent Instance based on agent url and
     * user Account
     * this method will connect with Agent through REST endpoint and get the DDO
     * based on
     * give DID.
     *
     * @param url url of the Agent
     * @param acc remote account
     * @return New Remote Agent Instance
     */
    public static RemoteAgent connect(String url, RemoteAccount acc)
            throws URISyntaxException {
        if (url == null || null == acc)
            throw new IllegalArgumentException("params URL or acc  cannot be null ");
        return create(url, acc);
    }

    /**
     * This method is to get the DDO from and agent with agent URL and
     * UserAccount.
     * This will make an HTTP call to remote agent whose and will parse the
     * response .
     * The DDO response include the DID, all services Endpoints.
     *
     * @return JSON
     */
    private static Map<String, Object> getDDOByURL(String url, Account account)
            throws URISyntaxException {
        URI uri = (url.contains(Constant.DDO_PATH)) ?
                new URI(url) :
                new URI(url + Constant.DDO_PATH);


        HttpGet httpget = new HttpGet(uri);

        String username = account.getCredentials().get(USER_NAME).toString();
        String password = account.getCredentials().get(PASSWORD).toString();
        try {
            CloseableHttpResponse response;

            response = HTTP.executeWithAuth(httpget, username, password);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() != 200) {
                throw new HttpResponseException(statusLine.getStatusCode(),
                        statusLine.getReasonPhrase());
            }
            String body = Utils.stringFromStream(response.getEntity().getContent());
            if (body.isEmpty()) {
                throw new RemoteException("No Content in the response for :"
                        + uri.getPath());
            }
            return JSON.toMap(body);
        } catch (IOException e) {
            throw new RemoteException("Fatal transport error: ", e);
        } finally {
            // Release the connection.
            httpget.releaseConnection();
        }
    }

    /**
     * Creates a RemoteAgent with the specified Resolver , DID and RemoteAccount
     *
     * @param resolver Resolver connection to use
     * @param did      DID for this agent
     * @param account  RemoteAccount for this agent
     * @return RemoteAgent
     */
    protected RemoteAgent create(Resolver resolver, DID did, RemoteAccount account) {
        if (resolver == null || null == did)
            throw new IllegalArgumentException("params Resolver/DID  cannot be null . ");
        return new RemoteAgent(resolver, did, account);
    }

    /**
     * Creates a RemoteAgent with the specified Resolver and DID This method
     * will create a new instance of Remote agent based on the Resolver and DID
     * reference
     * passed as an argument
     *
     * @param resolver Resolver
     * @param did      DID for this agent
     * @return RemoteAgent new instance of remote Agent
     */
    protected RemoteAgent create(Resolver resolver, DID did) {
        return new RemoteAgent(resolver, did, null);
    }

    private <R extends Asset> R registerBundle(Bundle bundle) {

        Map<String, Asset> resultAsset = new HashMap<>();
        // getting all sub asset
        Map<String, Asset> allSubAsset = bundle.getAll();
        for (String name : allSubAsset.keySet()) {
            Asset subAsset = allSubAsset.get(name);
            // registering each sub asset
            resultAsset.put(name, registerAsset(subAsset));
        }
        // registering bundle itself

        return registerAsset(bundle.getMetadataString());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends Asset> R registerAsset(Asset asset) {
        if (null == asset) {
            throw new IllegalArgumentException("params Asset cannot be null. ");
        } else if (asset.getMetadata().get(TYPE).equals(BUNDLE)) {
            return (R) registerBundle((Bundle) asset);
        }
        return registerAsset(asset.getMetadataString());
    }

    @Override
    public <R extends Asset> R registerAsset(String metaString) {
        if (null == metaString) {
            throw new StarfishValidationException("params metaString cannot be null");
        }

        URI uri = getMetaURI();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(HTTP.textEntity(metaString));
        String remoteAssetID = Utils.stringFromStream(getHTTPResponseAsStream(httpPost));
        String assetID = Hash.sha3_256String(metaString);
        if (!assetID.equals(assetID)) {
            throw new StarfishValidationException("Remote Asset ID " + remoteAssetID +
                    "is not match with given Asset ID" + assetID);
        }

        return (R) getRemoteAsset(metaString, JSON.toMap(metaString));
    }


    /**
     * This method to add header to the HTTP request.
     */
    protected String getSignInToken() {

        if (account == null) {
            throw new AuthorizationException("User don`t have account to access this agent");
        }
        if (account.getUserDataMap().get(TOKEN) != null) {
            return account.getUserDataMap().get(TOKEN).toString();
        }
        validateAccountData(account);
        try {
            createToken(account);
        } catch (RemoteException e) {
            return null;
        }

        return account.getUserDataMap().get(TOKEN).toString();
    }

    private void validateAccountData(RemoteAccount account) {
        String token = null;
        String username = null;
        String password = null;

        if (account.getUserDataMap().get(TOKEN) != null) {
            token = account.getUserDataMap().get(TOKEN).toString();
        }
        if (account.getCredentials().get(USER_NAME) != null) {
            username = account.getCredentials().get(USER_NAME).toString();
        }
        if (account.getCredentials().get(PASSWORD) != null) {
            password = account.getCredentials().get(PASSWORD).toString();
        }
        if ((token == null) && (username == null) && (password == null)) {
            throw new AuthorizationException(
                    "Username or Token or Password is not available for given account :"
                            + account);
        }
    }

    /**
     * Gets an asset for the given asset ID from this agent. Throws an
     * exeception if
     * the asset ID does not exist, or if the returned metadata is not valid.
     *
     * @param id The ID of the asset to get from this agent
     * @return Asset The asset found
     * @throws AuthorizationException if requestor does not have access
     *                                permission
     * @throws StorageException       if there is an error in retrieving the
     *                                Asset
     * @throws JobFailedException     if there is a failure in a remote
     *                                operation
     */
    @Override
    public <R extends Asset> R getAsset(String id) {
        URI uri = getMetaURI(id);
        HttpGet httpget = new HttpGet(uri);
        String body = Utils.stringFromStream(getHTTPResponseAsStream(httpget));
        Map<String, Object> metaMap = JSON.toMap(body);
        R a = getRemoteAsset(body, metaMap);
        String rid = a.getAssetID();
        if (!rid.equals(id)) {
            throw new StarfishValidationException(
                    "Expected asset ID: " + id + " but got metadata with hash: " + rid);
        }
        return a;

    }

    /**
     * This method is used for getting the remote Asset instance based on
     * metadata
     * passed
     *
     * @param metaMap map of the asset
     * @return the new remote asset created
     */
    @SuppressWarnings("unchecked")
    private <R extends Asset> R getRemoteAsset(String meta, Map<String, Object> metaMap) {
        if (metaMap.get(TYPE).equals(OPERATION)) {
            return (R) RemoteOperation.create(this, meta);
        } else if (metaMap.get(TYPE).equals(DATA_SET)) {
            return (R) RemoteDataAsset.create(this, meta);
        } else if (metaMap.get(TYPE).equals(BUNDLE)) {
            return (R) RemoteBundle.create(this, meta);
        } else {
            throw new StarfishValidationException("Invalid Asset Type :" + metaMap.get(TYPE));
        }
    }

    /**
     * This method is to check if the Asset is already registered based on asset
     * ID
     * passed as parameter. if the asset is already registered it will return
     * true
     * else false.
     *
     * @param assetId id of an asset to check if the aset is register
     * @return boolean true if asset is registered else false
     */
    private boolean isAssetRegistered(String assetId) {
        URI uri = getMetaURI(assetId);
        HttpGet httpget = new HttpGet(uri);

        try {
            HttpResponse httpResponse = getHttpResponse(httpget);
            if (200 == httpResponse.getStatusLine().getStatusCode()) {
                return true;
            }
        } catch (IOException e) {
            throw new RemoteException("Fatal transport error: ", e);
        } finally {
            // Release the connection.
            httpget.releaseConnection();
        }
        return false;
    }

    /**
     * Uploads an asset to this agent. Registers the asset with the agent if
     * required.
     * <p>
     * Throws an exception if upload is not possible, with the following likely
     * causes: - The agent does not support uploads of this asset type / size -
     * The
     * data for the asset cannot be accessed by the agent
     *
     * @param asset Asset to upload
     * @return Asset An asset stored on the agent if the upload is successful
     * @throws AuthorizationException if requestor does not have upload
     *                                permission
     * @throws StorageException       if there is an error in uploading the
     *                                Asset
     * @throws RemoteException        if there is an problem executing the task
     *                                on remote
     *                                Server.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <R extends Asset> R uploadAsset(Asset asset) {
        if (null == asset) {
            throw new IllegalArgumentException("params Asset cannot be null");
        }
        // asset already registered then only upload

        if (isAssetRegistered(asset.getAssetID())) {
            uploadAssetContent(asset);
        }
        // if asset is not registered then registered and upload
        else {
            registerAsset(asset);
            uploadAssetContent(asset);
        }

        return (R) getAsset(asset.getAssetID());
    }

    /**
     * This method is to upload the content of an data asset to this agent. if
     * the
     * asset type is operation it will do nothing
     *
     * @param asset Asset
     * @throws UnsupportedOperationException if the asset type is bundle
     */
    private void uploadAssetContent(Asset asset) {

        // check if the asset is operation or bundle it will not have content, so return
        // without upload
        if (asset.getMetadata().get(TYPE).equals(BUNDLE)) {
            throw new UnsupportedOperationException(
                    "Bundle don`t have content,so cannot be upload " + asset.getMetadata());
        } else if (asset.getMetadata().get(TYPE).equals(OPERATION)
                && !asset.getMetadata().get(CLASS).equals(ORCHESTRATION)) {

            return;
        }

        // get the storage API to upload the Asset content
        URI uri = getStorageURI(asset.getAssetID());
        HttpPost post = new HttpPost(uri);

        InputStream assetContentAsStream = asset.getContentStream();
        HttpEntity entity = HTTP.createMultiPart(FILE,
                new InputStreamBody(assetContentAsStream,
                        Utils.createRandomHexString(16) + ".tmp"));

        post.setEntity(entity);


        try {
            HttpResponse httpResponse = getHttpResponse(post);
            if (httpResponse.getStatusLine().getStatusCode() == 201) {
                return;
            }
        } catch (IOException e) {
            throw new RemoteException(" Upload asset Content failed for asset id : "
                    + asset.getAssetID(), e);
        }
        throw new RemoteException(" Upload asset Content failed for asset id : "
                + asset
                .getAssetID());
    }

    /**
     * Gets URI for this agent's invoke endpoint
     *
     * @param operationID did
     * @return The URI for this agent's invoke endpoint
     * @throws RuntimeException on URI syntax errors
     */
    private URI getInvokeSyncURI(String operationID) {
        try {
            String endPoint = getInvokeEndpoint();
            if (endPoint == null) throw
                    new UnsupportedOperationException(
                            "This agent does not support the Invoke API " +
                                    "(no endpoint defined)");


            return new URI(endPoint + INVOKE_SYNC + "/" + operationID);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Can't create valid URI for async: "
                    + operationID, e);
        }
    }

    /**
     * Gets URI for invoking a specific operation asynchronously
     *
     * @param operationID Asset ID of the operation to invoke
     * @return The URI for this the async invoke endpoint for the specified
     *         operation
     * @throws RuntimeException on URI syntax errors
     */
    private URI getInvokeAsyncURI(String operationID) {
        try {
            String endPoint = getInvokeEndpoint();
            if (endPoint == null) throw
                    new UnsupportedOperationException(
                            "This agent does not support the Invoke API " +
                                    "(no endpoint defined)");
            return new URI(endPoint + INVOKE_ASYNC + "/" + operationID);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Can't create valid URI for async: "
                    + operationID, e);
        }
    }

    /**
     * Gets URI for this agent's endpoint for jobID
     *
     * @return The URI for this agent's invoke endpoint
     * @throws IllegalArgumentException on invalid URI for jobID
     */
    private URI getJobURI(String jobID) {
        try {
            String endPoint = getInvokeEndpoint();
            if (endPoint == null)
                throw new UnsupportedOperationException("This agent does not " +
                        "support the Invoke API (no endpoint defined)");
            return new URI(endPoint + JOBS + "/" + jobID);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Can't create valid URI for job: "
                    + jobID, e);
        }
    }

    /**
     * Gets meta URI for this assetID
     *
     * @param assetID of the URI to create
     * @return The URI for this assetID
     * @throws UnsupportedOperationException if the agent does not support the
     *                                       Meta
     *                                       API
     * @throws IllegalArgumentException      on invalid URI for assetID
     */
    private URI getMetaURI(String assetID) {
        String metaEndpoint = getMetaEndpoint();
        if (metaEndpoint == null)
            throw new UnsupportedOperationException("This agent does not support " +
                    "the Meta API (no endpoint defined)");
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
     * @throws UnsupportedOperationException if the agent does not support the
     *                                       Storage API
     * @throws IllegalArgumentException      on invalid URI for assetID
     */
    protected URI getStorageURI(String assetID) {
        String storageEndpoint = getStorageEndpoint();
        if (storageEndpoint == null)
            throw new UnsupportedOperationException(
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
     * @throws UnsupportedOperationException if the agent does not support the
     *                                       Meta
     *                                       API (no endpoint defined)
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
     * Gets the storage endpoint for this agent
     *
     * @return The storage endpoint for this agent e.g.
     *         "https://www.myagent.com/api/v1/storage"
     */
    public String getStorageEndpoint() {
        return getEndpoint(Constant.ENDPOINT_STORAGE);
    }

    /**
     * Gets the Invoke API endpoint for this agent
     *
     * @return The invoke endpoint for this agent e.g.
     *         "https://www.myagent.com/api/v1/invoke"
     */
    public String getInvokeEndpoint() {
        return getEndpoint(Constant.ENDPOINT_INVOKE);
    }

    public String getStatusEndpoint() {
        return getEndpoint(Constant.ENDPOINT_STATUS);
    }

    public String getDDOEndpoint() {
        return getEndpoint(Constant.ENDPOINT_DDO);
    }

    /**
     * Gets the Meta API endpoint for this agent, or null if this does not exist
     *
     * @return The Meta API endpoint for this agent e.g.
     *         "https://www.myagent.com/api/v1/meta"
     */
    public String getMetaEndpoint() {
        return getEndpoint(Constant.ENDPOINT_META);
    }

    /**
     * Gets the Market API endpoint for this agent, or null if this does not
     * exist
     *
     * @return The Meta API endpoint for this agent e.g.
     *         "https://www.myagent.com/api/v1/meta"
     */
    public String getMarketEndpoint() {
        return getEndpoint(Constant.ENDPOINT_MARKET);
    }

    /**
     * Gets the Auth API endpoint for this agent, or null if this does not exist
     *
     * @return The Auth API endpoint for this agent e.g.
     *         "https://www.myagent.com/api/v1/meta"
     */
    public String getAuthEndpoint() {

        return getEndpoint(Constant.ENDPOINT_AUTH);
    }

    @Override
    public Job invoke(Operation operation, Object... params) {
        Map<String, Object> request = new HashMap<>(2);
        request.put(OPERATION, operation.getAssetID());
        request.put(PARAMS, Params.formatParams(operation, params));
        return invoke(operation, request);
    }

    /**
     * Polls this agent for the Asset resulting from the given job ID
     *
     * @param jobID ID for the Job to poll
     * @return The JSON map representing the response from the server as per
     *         DEP6.
     * @throws IllegalArgumentException If the job ID is invalid
     * @throws RemoteException          if there is a failure in a remote
     *                                  operation
     * @throws RuntimeException         for protocol errors
     */
    public Object pollJob(String jobID) {
        URI uri = getJobURI(jobID);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(uri);

        getHTTPResponseAsStream(httpget);
        CloseableHttpResponse response;
        try {
            response = httpclient.execute(httpget);
            try {
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();

                if (statusCode == 200) {
                    String body = Utils.stringFromStream(response.getEntity().getContent());
                    if (body.isEmpty()) {
                        throw new RemoteException("Expected JSON job status but got no body for Job ID:" + jobID);
                    }
                    Map<String, Object> result = JSON.toMap(body);
                    return result;
                } else {
                    return null;
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            throw new JobFailedException(" Job invocation failed for job id : " + jobID, e);
        }
    }

    @Override
    public Job invoke(Operation operation, Map<String, Object> params) {
        return invokeImpl(operation, Params.formatParams(operation, params));
    }

    /**
     * Invokes request on this RemoteAgent
     *
     * @param operation  operation
     * @param requestMap params
     * @return Job for this request
     * @throws RuntimeException for protocol errors
     */

    private Job invokeImpl(Operation operation, Map<String, Object> requestMap) {

        String assetID = operation.getAssetID();
        HttpPost httppost = new HttpPost(getInvokeAsyncURI(assetID));
        String paramJSON = JSON.toPrettyString(requestMap);

        StringEntity entity = new StringEntity(paramJSON, ContentType.APPLICATION_JSON);
        httppost.setEntity(entity);
        try {
            CloseableHttpResponse response = getHttpResponse(httppost);
            return RemoteAgent.createJob(this, operation.getParamsSpec(), response);
        } catch (IOException e) {
            throw new JobFailedException(
                    " IOException occurred  for asset ID: " + assetID + "params :" + paramJSON, e);
        } finally {
            httppost.releaseConnection();
        }
    }

    @Override
    public Job invokeAsync(Operation operation, Map<String, Object> params) {
        if (null == operation || !(operation instanceof RemoteOperation)) {
            throw new IllegalArgumentException("Operation must be a RemoteOperation but got: " + Utils.getClass(operation));
        }

        // validate the memory operation metadata
        Utils.validateAssetMetaData(operation.getMetadataString());

        Map<String, Object> paramValueMap = Params.formatParams(operation, params);

        URI uri = getInvokeAsyncURI(operation.getAssetID());
        HttpPost httppost = new HttpPost(uri);
        StringEntity entity = new StringEntity(JSON.toPrettyString(paramValueMap), ContentType.APPLICATION_JSON);
        httppost.setEntity(entity);
        try {
            CloseableHttpResponse response = getHttpResponse(httppost);
            return RemoteAgent.createJob(this, operation.getOperationSpec(), response);


        } catch (IOException e) {
            throw new JobFailedException(" IOException occurred  operation : " + operation.toString() + "params :"
                    + JSON.toPrettyString(params), e);
        } finally {
            httppost.releaseConnection();
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

        // validate operation metadata
        Utils.validateAssetMetaData(operation.getMetadataString());

        // this will validate if the required input is provided or not
        Map<String, Object> paramValueMap = Params.formatParams(operation, params);


        URI uri = getInvokeSyncURI(operation.getAssetID());
        HttpPost httppost = new HttpPost(uri);
        // TODO if params is a map of asset then form proper entity
        StringEntity entity = new StringEntity(JSON.toPrettyString(paramValueMap), ContentType.APPLICATION_JSON);
        httppost.setEntity(entity);
        String body = Utils.stringFromStream(getHTTPResponseAsStream(httppost));
        Map<String, Object> res = (Map<String, Object>) JSON.toMap(body).get(Constant.OUTPUTS);
        return Params.formatResponse(operation.getOperationSpec(), res);

    }

    private InputStream getHTTPResponseAsStream(HttpRequestBase requestBase) {
        try {

            // make http call
            CloseableHttpResponse response = getHttpResponse(requestBase);

            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == 200) {
                return response.getEntity().getContent();
                //throw new HttpResponseException(statusLine.getStatusCode(),
                //statusLine.getReasonPhrase());
            } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                // refresh the token and try again
                requestBase.removeHeaders(HttpHeaders.AUTHORIZATION);
                if (account.getUserDataMap() != null) {
                    account.getUserDataMap().put(TOKEN, null);
                } else {
                    account.getUserDataMap().put("Basic", null);
                }
                //  resend the request
                response = getHttpResponse(requestBase);
                 statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == 200) {
                    return response.getEntity().getContent();}
                else{
                    throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                }
            }


        } catch (IOException e) {
            throw new RemoteException("Fatal transport error: " + e.getMessage(), e);
        } finally {
            // Release the connection.
            requestBase.releaseConnection();
        }
        throw new RemoteException("Authorization failed :");
    }

    /**
     * This method is used to get the response from Agent for service.
     *
     * @param requestBase
     * @return
     * @throws IOException
     */
    public CloseableHttpResponse getHttpResponse(HttpRequestBase requestBase)
            throws IOException {

        if (null != getSignInToken()) {
            requestBase.addHeader(HttpHeaders.AUTHORIZATION, "token " + getSignInToken());
        }
        // try basic token
        else {
            String value = getBasicToken();
            String header = AUTH.WWW_AUTH_RESP;
            requestBase.setHeader(header, value);
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        return httpclient.execute(requestBase);
    }

    /**
     * API to get List of map of all Metadata
     *
     * @param marketAgentUrl market url
     * @return List marketMetaData
     */
    private List<Map<String, Object>> getAllMarketMetaData(String marketAgentUrl) {
        URI uri = getMarketURI(marketAgentUrl);
        HttpGet httpget = new HttpGet(uri);
        List<Map<String, Object>> result;
        String body = Utils.stringFromStream(getHTTPResponseAsStream(httpget));
        result = JSON.parse(body);
        return result;

    }

    /**
     * API to get Market agent instance from the Agent
     *
     * @param listingData    listing data
     * @param marketAgentUrl market url
     * @return String
     */
    private String createMarketAgentInstance(Map<String, Object> listingData, String marketAgentUrl) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(getMarketURI(marketAgentUrl));

        httpPost.setEntity(new StringEntity(JSON.toPrettyString(listingData), ContentType.APPLICATION_JSON));

        return Utils.stringFromStream(getHTTPResponseAsStream(httpPost));
    }

    /**
     * This method is to get the Market metadata from the agent by providing
     * market
     * URL.
     *
     * @param marketAgentUrl market url
     * @return market metadata as String
     */
    private String getMarketMetaData(String marketAgentUrl) {
        HttpGet httpget = new HttpGet(getMarketURI(marketAgentUrl));
        return Utils.stringFromStream(getHTTPResponseAsStream(httpget));
    }

    private String updateMarketMetaData(Map<String, Object> listingData, String marketAgentUrl) {
        HttpPut put = new HttpPut(getMarketURI(marketAgentUrl));

        put.setEntity(new StringEntity(JSON.toPrettyString(listingData), ContentType.APPLICATION_JSON));

        return Utils.stringFromStream(getHTTPResponseAsStream(put));
    }

    /**
     * Gets market URI for this agent
     *
     * @param marketAgentUrl marketAgentUrl
     * @return The URI for listing metadata
     * @throws UnsupportedOperationException if the agent does not support the
     *                                       Meta
     *                                       API (no endpoint defined)
     * @throws IllegalArgumentException      on invalid URI for asset metadata
     */
    private URI getMarketURI(String marketAgentUrl) {
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
        Object assetId = listingData.get(ASSET_ID);
        if (assetId == null) {
            throw new StarfishValidationException("Asset ID not found, asset id is mandatory for creating listing");
        }

        String response = createMarketAgentInstance(listingData, LISTING_URL);
        String id = JSON.toMap(response).get(ID).toString();
        return RemoteListing.create(this, id);
    }

    /**
     * API to update the data of existing listing. if the listing id passed is
     * not
     * exist it will throw Exception
     *
     * @param metaMap map of new value need to be updated for listing
     * @return Listing return updated listing instance
     */
    public Listing updateListing(Map<String, Object> metaMap) {

        if (null == metaMap) {
            throw new IllegalArgumentException("param metaMap cannot be null.");
        }
        if (metaMap.get(ID) == null) {
            throw new StarfishValidationException(
                    "Listing ID :" + metaMap.get(ID) + " not found in the meta map passed");
        }
        String id = (String) metaMap.get(ID);
        updateMarketMetaData(metaMap, LISTING_URL + "/" + id);
        return RemoteListing.create(this, id);

    }

    /**
     * API to get the listing meta data
     *
     * @param id id
     * @return metadata
     */
    protected Map<String, Object> getListingMetaData(String id) {
        String response = getMarketMetaData(LISTING_URL + "/" + id);
        return JSON.toMap(response);
    }

    /**
     * API to get all listing metaData.It may ab very heavy call .
     *
     * @return List of all listing
     */
    private List<Listing> getAllListing() {

        List<Map<String, Object>> result = getAllMarketMetaData(LISTING_URL);

        return result.stream()
                .map(p -> RemoteListing.create(this, p.get(ID).toString()))
                .collect(Collectors.toList());

    }

    /**
     * APi to get all the listing that belong to user id passed in the argument
     *
     * @param userID user id for which the listing data need to be retrieved
     *               form
     *               agent
     * @return List of all listing belong to given user id
     */
    private List<RemoteListing> getAllListing(String userID) {

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
        if (null == data) {
            throw new IllegalArgumentException("param data cannot be null");
        }
        // check if the data is not null and it must have listing id
        if (data.get(LISTING_ID) == null) {
            throw new StarfishValidationException(" Listing ID is missing in param passed");
        }
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
    private Purchase updatePurchase(Map<String, Object> newValue) {

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
     * @throws UnsupportedOperationException if the agent does not support the
     *                                       Meta
     *                                       API (no endpoint defined)
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
     * The will create the token base on user name and password configured
     *
     * @param account remote account reference
     */
    public void createToken(RemoteAccount account) {

        if (account.getUserDataMap().get(TOKEN) != null) {
            return;
        }

        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials(account.getUserDataMap().get("username").toString(), account.getUserDataMap().get("password").toString());
        provider.setCredentials(AuthScope.ANY, credentials);

        HttpClient client = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(provider)
                .build();

        try {
            HttpResponse response = client.execute(
                    new HttpGet(getAuthURI(TOKEN)));
            int statusCode = response.getStatusLine()
                    .getStatusCode();
            if (statusCode == 200) {
                String body = Utils.stringFromStream(response.getEntity().getContent());
                JSONArray jsonArray = JSON.parse(body);
                String id = jsonArray.get(0).toString();
                updateAccountData(id);
            }
            if (statusCode == 401) {
                throw new RemoteException("Authorization failed :");
            }
        } catch (IOException e) {
            throw new RemoteException(" Create token failed for account  :" + account, e);
        }


    }

    private String getBasicToken() {

        if (null != account.getUserDataMap().get("Basic")) {
            return account.getUserDataMap().get("Basic").toString();
        }

        final CharArrayBuffer buffer = new CharArrayBuffer(32);

        String username =
                this.account.getCredentials().get("username").toString();
        String password =
                this.account.getCredentials().get("password").toString();
        final StringBuilder tmp = new StringBuilder();
        tmp.append(username);
        tmp.append(":");
        tmp.append((password == null) ? "null" : password);
        final Base64 base64codec = new Base64(0);
        final byte[] base64password = base64codec.encode(EncodingUtils.getBytes(tmp.toString(), Consts.UTF_8.name()));
        buffer.append("Basic ");
        buffer.append(base64password, 0, base64password.length);
        account.getUserDataMap().put("Basic", buffer.toString());

        return buffer.toString();
    }

    /**
     * This method is to get the content of an Asset based on Asset id passed as
     * an
     * argument.
     *
     * @param assetId of the asset
     * @return Stream of asset content
     */
    public InputStream getContentStream(String assetId) {
        URI uri = getStorageURI(assetId);
        HttpGet httpget = new HttpGet(uri);
        return getHTTPResponseAsStream(httpget);

    }

    /**
     * This method is to update the token for this Agent
     *
     * @param token auth token
     */
    private void updateAccountData(String token) {
        account.getUserDataMap().put(TOKEN, token);
    }

    /**
     * API to get the Status of an Remote Agent.
     * the status will have name , description and
     * other information about the agent.
     *
     * @return response Map
     */
    public Map<String, Object> getStatus() {
        URI uri = getStatusUri();
        HttpGet httpget = new HttpGet(uri);
        String body = Utils.stringFromStream(getHTTPResponseAsStream(httpget));
        if (body.isEmpty()) {
            throw new RemoteException("No Content in the response for :" + uri);
        }
        Map<String, Object> result = JSON.toMap(body);
        return result;

    }

    /**
     * The method used to get the Status URI of an Agent
     *
     * @return status uri
     */
    private URI getStatusUri() {
        String endPoint = getStatusEndpoint();
        try {
            if (endPoint == null)
                throw new IllegalArgumentException("This agent does not support the Status API (no endpoint defined)");
            return new URI(endPoint);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Can't create valid URI for endpoint " + endPoint, e);
        }
    }

    /**
     * The method used to get the DDO URI of an Agent
     *
     * @return ddo uri
     */
    private URI getDDOUri() {
        String endPoint = getDDOEndpoint();
        try {

            if (endPoint == null)
                throw new IllegalArgumentException("This agent does not support the DDO API (no endpoint defined)");
            return new URI(endPoint);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Can't create valid URI : " + endPoint, e);
        }
    }

    /**
     * This method is to ge the DDO of an agent.
     * The DDO response include the DID, all services Endpoints.
     *
     * @return JSON
     */
    public Map<String, Object> getAgentDDO() throws URISyntaxException {
        URI uri = getDDOUri();
        return getDDOByURL(uri.toString(), this.getAccount());
    }

    /**
     * This method is used to get the remote account associated with the Remote
     * Agent
     *
     * @return remoteAgent instance
     */
    public RemoteAccount getAccount() {
        return account;
    }

    @Override
    public Job getJob(String jobID) {
        return null;
    }
}
