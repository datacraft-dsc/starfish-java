package sg.dex.starfish.impl.remote;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import sg.dex.starfish.ADataAsset;
import sg.dex.starfish.Agent;
import sg.dex.starfish.Asset;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.util.TODOException;

public class RemoteAsset extends ADataAsset implements DataAsset {

	protected RemoteAsset(Agent agent, String meta) {
		super(agent, meta);
	}

	@Override
	public boolean isDataAsset() {
		return true;
	}

	@Override
	public InputStream getInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getSize() {
		throw new TODOException();
	}
	
	private String getURLString() {
		return getRemoteAgent().getAssetURL(id);
	}
	
	public RemoteAgent getRemoteAgent() {
		return (RemoteAgent)agent;
	}

	public URL getURL() {
		try {
			return new URL(getURLString());
		}
		catch (MalformedURLException e) {
			throw new Error(e);
		}
	}

	public static Asset create(RemoteAgent remoteAgent, String id) {
		return new RemoteAsset(remoteAgent,id);
	}

}
