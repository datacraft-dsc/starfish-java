package sg.dex.starfish.impl.file;

import java.io.InputStream;

import sg.dex.starfish.ADataAsset;
import sg.dex.starfish.Agent;
import sg.dex.starfish.util.TODOException;

public class ResourceAsset extends ADataAsset {
	private final String name;
	
	protected ResourceAsset(Agent agent, String meta, String name) {
		super(agent, meta);
		this.name=name;
	}

	@Override
	public InputStream getInputStream() {
		ClassLoader cl = this.getClass().getClassLoader();
		return cl.getResourceAsStream(name);
	}

	@Override
	public long getSize() {
		throw new TODOException();
	}
	
	public String getName() {
		return name;
	}

}
