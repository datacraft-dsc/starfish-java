package sg.dex.starfish.impl.memory;

import java.util.HashMap;

import sg.dex.starfish.AAgent;
import sg.dex.starfish.Asset;
import sg.dex.starfish.DID;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.util.Utils;

public class MemoryAgent extends AAgent {
	/**
	 * The singleton default memory agent instance
	 */
	public static final MemoryAgent DEFAULT = new MemoryAgent(Ocean.connect(),Utils.createRandomDIDString());
	
	private HashMap<String,Asset> assetStore=new HashMap<String,Asset>();

	private MemoryAgent(Ocean ocean,String did) {
		this(ocean,DID.parse(did));
	}
	
	private MemoryAgent(Ocean ocean, DID did) {
		super(ocean, did);
	}
	
	public static MemoryAgent create(DID did) {
		return new MemoryAgent(Ocean.connect(),did);
	}
	
	public static MemoryAgent create() {
		return new MemoryAgent(Ocean.connect(),Utils.createRandomDIDString());
	}
	
	public static MemoryAgent create(String did) {
		return new MemoryAgent(Ocean.connect(),did);
	}

	@Override
	public void registerAsset(Asset a) {
		assetStore.put(a.getAssetID(),a);
	}
	
	@Override
	public Asset uploadAsset(Asset a) {
		MemoryAsset ma=MemoryAsset.create(a);
		registerAsset(ma);
		return ma;
	}

	@Override
	public Asset getAsset(String id) {
		return assetStore.get(id);
	}

}
