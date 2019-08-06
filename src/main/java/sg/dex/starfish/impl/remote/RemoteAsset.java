package sg.dex.starfish.impl.remote;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.*;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.HTTP;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing an asset managed via a remote agent.
 * <p>
 * This asset will be present in Ocean ecosystem and be referred by using the asset ID.
 *
 * @author Mike
 * @version 0.5
 */
public class RemoteAsset extends ARemoteAsset implements DataAsset {

    protected RemoteAsset(String meta, RemoteAgent remoteAgent) {
        super(meta, remoteAgent);
    }

    /**
     * Creates a RemoteAsset with the given metadata on the specified remote agent
     *
     * @param agent RemoteAgent on which to create the RemoteAsset
     * @param meta  Asset metadata which must be a valid JSON string
     * @return RemoteAsset
     */
    public static RemoteAsset create(RemoteAgent agent, String meta) {
        return new RemoteAsset(meta, agent);
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
            throw new RemoteException("Asset ID not found at: " + uri);
        }
        if (statusCode == 200) {
            InputStream inputStream = HTTP.getContent(response);
            // validating the hash of the content
            if(null != this.getMetadata().get(Constant.CONTENT_HASH)) {
                remoteAgent.validateHashOfContent(inputStream,getAssetID());
            }
            return inputStream;
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
        o.put(Constant.DID, getAssetID());
        return o;
    }

    @Override
    public DID getAssetDID() {
        // DID of a remote asset is the DID of the appropriate agent with the asset ID as a resource path
        DID agentDID = remoteAgent.getDID();
        return agentDID.withPath(getAssetID());
    }

}
