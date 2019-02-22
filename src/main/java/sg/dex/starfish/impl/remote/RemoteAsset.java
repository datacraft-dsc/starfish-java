package sg.dex.starfish.impl.remote;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.json.simple.JSONObject;

import sg.dex.starfish.ADataAsset;
import sg.dex.starfish.Asset;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.TODOException;
import sg.dex.starfish.util.AuthorizationException;
import sg.dex.starfish.util.StorageException;

/**
 * Class representing a data asset referenced by a URL.
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

	public static Asset create(String meta, RemoteAgent agent) {
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
	public InputStream getInputStream() {
		URL url=agent.getURL(this);
		try {
			return url.openStream();
		}
		catch (IOException e) {
			throw new Error("Cannot open input stream for URL: "+url,e);
		}
	}

	@Override
	public long getSize() {
		throw new TODOException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getParamValue() {
		JSONObject o=new JSONObject();
		// pass the asset ID, i.e. hash of content
		o.put("did", getAssetDID());
		return o;
	}

	/**
	 * Gets DID for this Asset
	 *
	 * @throws UnsupportedOperationException if unable to obtain DID
	 * @param a The asset to register
	 */
	@Override
	public DID getAssetDID() {
		DID agentDID=agent.getDID();
		return agentDID.withPath(getAssetID());
	}


}
