package sg.dex.starfish.impl.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sg.dex.starfish.Account;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Listing;
import sg.dex.starfish.MarketAgent;
import sg.dex.starfish.exception.JobFailedException;
import sg.dex.starfish.exception.RemoteException;
import sg.dex.starfish.exception.TODOException;
import sg.dex.starfish.impl.AListing;
import sg.dex.starfish.util.HTTP;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is responsible for creating the listing instance.
 * To create and instance user just need to pass the agent and the data(metadata)
 * for which the listing instance should be created.
 * It also sever getting the meta data of existing listing ,updating the existing listing.
 */

public class RemoteListing extends AListing {

    // local map to cache the listing data
    private Map<String, Object> metaDataCache = null;
    
    // remote agent reference
    private RemoteAgent remoteAgent;
    
    // listing id
    private String id;

    /**
     * To get the reference of existing listing user need to pass the remote Agent and the existing listing id.
     *
     * @param remoteAgent
     * @param id
     */
    private RemoteListing(RemoteAgent remoteAgent, String id) {
        this.remoteAgent = remoteAgent;
        this.id = id;
    }

    /**
     * Create New Listing based on the meta data passed in an argument and the remote Agent
     *
     * @param remoteAgent
     * @param data
     */
    private RemoteListing(RemoteAgent remoteAgent, Map<String, Object> data) {
        this.remoteAgent = remoteAgent;

    }

    /**
     * To get the Reference of Existing Listing
     *
     * @param agent
     * @param id
     * @return
     */
    public static RemoteListing create(RemoteAgent agent, String id) {
        RemoteListing remoteListing = new RemoteListing(agent, id);
        return remoteListing;
    }

    /**
     * TO create new Listing based on the meta data passed and the Remote Agent reference
     *
     * @param agent
     * @return
     */
    public static RemoteListing create(RemoteAgent agent, Map<String, Object> data) {
        RemoteListing remoteListing = new RemoteListing(agent, data);
        return remoteListing.createListing(data);
    }


    @Override
    public Asset getAsset() {
        return null;
    }

    @Override
    public Object getAgreement() {
        throw new TODOException();
    }

    @Override
    public Asset purchase(Account account) {
        throw new TODOException();
    }

    @Override
    public Listing refresh() {
        metaDataCache.put(id, getListingMetadata());
        return this;
    }

    public List<RemoteListing> getAllListing() {

        URI uri = getMarketLURI();
        HttpGet httpget = new HttpGet(uri);
        remoteAgent.addAuthHeaders(httpget);
        CloseableHttpResponse response = HTTP.execute(httpget);
        List<Map<String, Object>> result;

        try {
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 404) {
                throw new RemoteException("Listing ID not found for at: " + id);
            } else if (statusCode == 200) {
                String body = Utils.stringFromStream(HTTP.getContent(response));
                try {
                    result =
                            new ObjectMapper().readValue(body, List.class);

                    return result.stream()
                            .map(p -> new RemoteListing(remoteAgent, p.get("id").toString()))
                            .collect(Collectors.toList());

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

    public Map<String, Object> getListingMetaData() {

        return metaDataCache == null ? getListingMetadata() : metaDataCache;

    }

    private Map<String,Object> getListingMetadata(){
        URI uri = getMarketLURIByID();
        HttpGet httpget = new HttpGet(uri);
        remoteAgent.addAuthHeaders(httpget);
        CloseableHttpResponse response = HTTP.execute(httpget);
        try {
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 404) {
                throw new RemoteException("Asset ID not found for at: " + uri);
            } else if (statusCode == 200) {
                String body = Utils.stringFromStream(HTTP.getContent(response));
                metaDataCache.put(id, JSON.toMap(body));
                return JSON.toMap(body);
            } else {
                throw new TODOException("status code not handled: " + statusCode);
            }
        } finally {
            HTTP.close(response);
        }
    }


    public RemoteListing updateListing(Map<String, Object> newValue) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPut put = new HttpPut(getMarketLURIByID());

        remoteAgent.addAuthHeaders(put);
        put.addHeader("Accept", "application/json");

        put.setEntity(new StringEntity(JSON.toPrettyString(newValue), ContentType.APPLICATION_JSON));

        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(put);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                String body = Utils.stringFromStream(HTTP.getContent(response));
                metaDataCache.put(id, JSON.toMap(body));
                return this;
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

    public RemoteListing createListing(Map<String, Object> metaData) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(getMarketLURI());

        remoteAgent.addAuthHeaders(httpPost);
        httpPost.addHeader("Accept", "application/json");

        httpPost.setEntity(new StringEntity(JSON.toPrettyString(metaData), ContentType.APPLICATION_JSON));

        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                String body = Utils.stringFromStream(HTTP.getContent(response));
                String id =JSON.toMap(body).get("id").toString();
                return create(remoteAgent,id);
                //return getListObject(JSON.toMap(body));
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
    private URI getMarketLURI() {
        String marketEndpoint = remoteAgent.getMarketEndpoint();
        if (marketEndpoint == null)
            throw new UnsupportedOperationException("This agent does not support the Market API (no endpoint defined)");
        try {
            return new URI(marketEndpoint + "/listings");
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Can't create valid URI for asset metadata", e);
        }
    }

    /**
     * Gets market URI for this agent
     *
     * @return The URI for listing metadata
     * @throws UnsupportedOperationException if the agent does not support the Meta API (no endpoint defined)
     * @throws IllegalArgumentException      on invalid URI for asset metadata
     */
    private URI getMarketLURIByID() {
        String marketEndpoint = remoteAgent.getMarketEndpoint();
        if (marketEndpoint == null)
            throw new UnsupportedOperationException("This agent does not support the Market API (no endpoint defined)");
        try {
            return new URI(marketEndpoint + "/listings" + "/" + id);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Can't create valid URI for Market metadata", e);
        }
    }

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getInfo() {
		return (Map<String, Object>) getListingMetaData().get("info");
	}

}
