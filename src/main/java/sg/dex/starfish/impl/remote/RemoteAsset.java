package sg.dex.starfish.impl.remote;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import sg.dex.starfish.ADataAsset;
import sg.dex.starfish.Asset;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.TODOException;

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

	@Override
	public Map<String,Object> getParamValue() {
		Map<String,Object> o=new HashMap<>();
		// pass the asset ID, i.e. hash of content
		o.put("did", getAssetDID());
		return o;
	}

	@Override
	public DID getAssetDID() {
		// DID of a remote asset is the DID of the appropriate agent with the asset ID as a resource path
		DID agentDID=agent.getDID();
		return agentDID.withPath(getAssetID());
	}


}
