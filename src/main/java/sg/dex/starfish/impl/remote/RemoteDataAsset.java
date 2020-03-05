package sg.dex.starfish.impl.remote;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.GenericException;
import sg.dex.starfish.exception.RemoteException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.util.HTTP;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a data asset managed via a remote agent.
 * *
 *
 * @author Mike
 * @version 0.5
 */
public class RemoteDataAsset extends ARemoteAsset implements DataAsset {

    private RemoteAgent remoteAgent;

    protected RemoteDataAsset(String meta, RemoteAgent remoteAgent) {
        super(meta, remoteAgent);
        this.remoteAgent = remoteAgent;
    }

    /**
     * Creates a RemoteAsset with the given metadata on the specified remote agent
     *
     * @param agent RemoteAgent on which to create the RemoteAsset
     * @param meta  Asset metadata which must be a valid JSON string
     * @return RemoteAsset
     */
    public static RemoteDataAsset create(RemoteAgent agent, String meta) {
        return new RemoteDataAsset(meta, agent);
    }

    @Override
    public boolean isDataAsset() {
        return true;
    }

    /**
     * Gets raw data corresponding to this Asset
     *
     * @return An input stream allowing consumption of the asset data
     * @throws AuthorizationException if requestor does not have access permission
     * @throws StorageException       if unable to load the Asset
     */
    @Override
    public InputStream getContentStream() {
        URI uri = remoteAgent.getStorageURI(getAssetID());
        HttpGet httpget = new HttpGet(uri);
        remoteAgent.addAuthHeaders(httpget);
        HttpResponse response = HTTP.execute(httpget);
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (statusCode == 404) {
            throw new RemoteException("Asset with asset ID " + this.getAssetID() + "is not uploaded on  agent :DID " + remoteAgent.getDID() + "URL failed " + uri);
        }
        if (statusCode == 200) {
            return HTTP.getContent(response);
        }
        throw new RemoteException("Asset ID not found at for Asset : " + getAssetID() + " URI: " + uri);

    }


    @Override
    public long getContentSize() {
        try {
            return remoteAgent.getContentStream(getAssetID()).available();
        } catch (IOException e) {
            throw new GenericException("Exception occurred  for asset id :" + getAssetID() + " while finding getting the Content size :", e);
        }
    }

    @Override
    public Map<String, Object> getParamValue() {
        Map<String, Object> o = new HashMap<>();

        // pass the asset ID, i.e. hash of content
        o.put(Constant.DID, this.getDID().toString());
        Map<String, Object> authMap = new HashMap<>();
        //authMap.put("token","dec75dc8008fd851f16d740aa57bdd5d18ae95ef8aea76b5003c70d51879dac1");
        authMap.put("token",remoteAgent.getAccount().getUserDataMap().get("token"));
        o.put("auth",authMap);
        return o;
    }

}
