package sg.dex.starfish.impl;

import java.util.HashMap;

import sg.dex.starfish.AAgent;
import sg.dex.starfish.Asset;

public class MemoryAgent extends AAgent {

	private HashMap<String,Asset> assetStore=new HashMap<String,Asset>();

	protected MemoryAgent(String did) {
		super(did);
	}

	@Override
	public void registerAsset(Asset a) {
		assetStore.put(a.getAssetID(),a);
	}
}
