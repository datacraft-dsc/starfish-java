package sg.dex.starfish.impl.memory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Invokable;
import sg.dex.starfish.Job;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.Operation;
import sg.dex.starfish.impl.AAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Utils;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StorageException;

// TODO: should implement MarketAgent, StorageAgent etc.

public class MemoryAgent extends AAgent implements Invokable {
	/**
	 * The singleton default memory agent instance
	 */
	public static final MemoryAgent DEFAULT = new MemoryAgent(Ocean.connect(),DID.createRandomString());

	/**
	 * A cached thread pool for jobs executed in memory
	 */
	public static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

	private HashMap<String,MemoryAsset> assetStore=new HashMap<String,MemoryAsset>();

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
		return new MemoryAgent(Ocean.connect(),DID.createRandomString());
	}

	public static MemoryAgent create(String did) {
		return new MemoryAgent(Ocean.connect(),did);
	}

	/**
	 * Registers an Asset with this Agent
	 *
	 * @param a The Asset to register
	 * @throws AuthorizationException if requestor does not have register permission
	 * @throws StorageException if there is an error in storing the Asset
	 * @return Asset The asset found, or null if the agent does not have the asset available
	 */
	@Override
	public Asset registerAsset(Asset a) {
		MemoryAsset ma = MemoryAsset.create(a);
		// TODO: consider removing because ID collision is impossible?
		//if(null != assetStore.get(a.getAssetID())){
		//		throw new IllegalStateException("Asset with id "+ a.getAssetID() +"is already register");
		//}
		assetStore.put(ma.getAssetID(),ma);
		return ma;
	}

	/**
	 * Registers an Asset with this Agent
	 *
	 * @param a The Asset to register
	 * @throws AuthorizationException if requestor does not have register permission
	 * @throws StorageException if there is an error in storing the Asset
	 * @return Asset The asset uploaded
	 */
	@Override
	public Asset uploadAsset(Asset a) {
		MemoryAsset ma=MemoryAsset.create(a);
		registerAsset(ma);
		return ma;
	}

	/**
	 * Get Asset
	 *
	 * @param id The Asset to get
	 * @throws AuthorizationException if requestor does not have register permission
	 * @throws StorageException if there is an error in loading the Asset
	 * @return Asset The asset found
	 */
	@Override
	public Asset getAsset(String id) {
		return assetStore.get(id);
	}

	/**
	 * Invokes the specified operation on this agent. If the invoke is successfully launched,
	 * will return a Job instance that can be used to access the result, otherwise throws an
	 * exception.
	 *
	 * @param operation The operation to invoke on this agent
	 * @param params Positional parameters for the invoke operation
	 * @throws IllegalArgumentException if required parameters are not available.
	 * @return A Job instance allowing access to the invoke job status and result
	 */
	@Override
	public Job invoke(Operation operation, Asset... params) {
		if (!(operation instanceof AMemoryOperation)) {
			throw new IllegalArgumentException("Operation must be a MemoryOperation but got: "+Utils.getClass(operation));
		}
		return operation.invoke(params);
	}

	/**
	 * Invokes the specified operation on this agent. If the invoke is successfully launched,
	 * will return a Job instance that can be used to access the result, otherwise throws an
	 * exception.
	 *
	 * @param operation The operation to invoke on this agent
	 * @param params named parameters for the invoke operation
	 * @throws IllegalArgumentException if operation not a AMemoryOperation
	 * @return A Job instance allowing access to the invoke job status and result
	 */
	@Override
	public Job invoke(Operation operation, Map<String, Asset> params) {
		if (!(operation instanceof AMemoryOperation)) {
			throw new IllegalArgumentException("Operation must be a MemoryOperation but got: "+Utils.getClass(operation));
		}
		return operation.invoke(params);
	}

}
