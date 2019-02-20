package sg.dex.starfish.impl.memory;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sg.dex.starfish.AAgent;
import sg.dex.starfish.Asset;
import sg.dex.starfish.InvokableAgent;
import sg.dex.starfish.Job;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.Operation;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Utils;

public class MemoryAgent extends AAgent implements InvokableAgent {
	/**
	 * The singleton default memory agent instance
	 */
	public static final MemoryAgent DEFAULT = new MemoryAgent(Ocean.connect(),Utils.createRandomDIDString());

	/**
	 * A cached thread pool for jobs executed in memory
	 */
	public static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
	
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

	@Override
	public Job invoke(Operation operation, Asset... params) {
		if (!(operation instanceof AMemoryOperation)) {
			throw new IllegalArgumentException("Operation must be a MemoryOperation but got: "+Utils.getClass(operation));
		}
		return null;
	}

}
