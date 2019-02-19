package sg.dex.starfish.impl.remote;

import java.io.InputStream;

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

	public static Asset create(RemoteAgent remoteAgent, String id) {
		return new RemoteAsset(remoteAgent,id);
	}

}
