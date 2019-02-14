package sg.dex.starfish.impl;

import java.util.HashMap;

import sg.dex.starfish.AAgent;
import sg.dex.starfish.Asset;

public class MemoryAgent extends AAgent {

	private HashMap<String,Asset> assetStore=new HashMap<String,Asset>();

	private MemoryAgent(String did) {
		super(did);
	}
	
	public static MemoryAgent create(String did) {
		return new MemoryAgent(did);
	}

	@Override
	public void registerAsset(Asset a) {
		assetStore.put(a.getAssetID(),a);
	}
}
