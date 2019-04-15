package sg.dex.starfish.impl.remote;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import sg.dex.starfish.Asset;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.exception.RemoteException;
import sg.dex.starfish.impl.ADataAsset;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.exception.TODOException;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.util.HTTP;

/**
 * Class representing a data asset referenced by a URL.
 * this asset will be present in Ocean echo system and be referred by using the asset ID.
 *
 *
 * @author Mike
 *
 */
public class RemoteAsset extends ADataAsset implements DataAsset {

	private final RemoteAgent agent;

	protected RemoteAsset(String meta, RemoteAgent agent) {
		super(meta);
		this.agent=agent;
	}

	/**
	 * Creates a RemoteAsset with the given metadata on the specified remote agent
	 * @param agent RemoteAgent on which to create the RemoteAsset
	 * 
	 * @param meta Asset metadata which must be a valid JSON string
	 * @return RemoteAsset
	 */
	public static RemoteAsset create(RemoteAgent agent, String meta) {
		return new RemoteAsset(meta,agent);
	}

	@Override
	public boolean isDataAsset() {
		return true;
	}

	/**
	 * Gets raw data corresponding to this Asset
	 *
	 * @throws AuthorizationException if requestor does not have access permission
	 * @throws StorageException if unable to load the Asset
	 * @return An input stream allowing consumption of the asset data
	 */
	@Override
	public InputStream getContentStream() {
        URI uri = agent.getStorageURI(getAssetID());
        HttpGet httpget = new HttpGet(uri);
        agent.addAuthHeaders(httpget);
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
	 * Gets RemoteAsset size
	 *
	 * @throws TODOException not implemented yet
	 * @return size of the RemoteAsset
	 */
	@Override
	public long getContentSize() {
		throw new TODOException();
	}

	@Override
	public Map<String,Object> getParamValue() {
		Map<String,Object> o=new HashMap<>();
		// pass the asset ID, i.e. hash of content
		o.put("did", getAssetDID());
		return o;
	}

	/**
	 * Gets DID for this Asset
	 *
	 * @throws UnsupportedOperationException if unable to obtain DID
	 * @return DID
	 */
	@Override
	public DID getAssetDID() {
		// DID of a remote asset is the DID of the appropriate agent with the asset ID as a resource path
		DID agentDID=agent.getDID();
		return agentDID.withPath(getAssetID());
	}


}
