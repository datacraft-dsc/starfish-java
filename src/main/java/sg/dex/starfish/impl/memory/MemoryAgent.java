package sg.dex.starfish.impl.memory;

import sg.dex.starfish.*;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StarfishValidationException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.impl.AAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/**
 * An in-memory agent implementation
 * This class methods include creation of memory agent,
 * get asset based on id, create listing ,create purchase,invoke service.
 *
 * @author Mike
 */
public class MemoryAgent extends AAgent implements Invokable<Asset>, MarketAgent {
    /**
     * The singleton default memory agent instance
     */
    /**
     * A cached thread pool for jobs executed in memory
     */
    public static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    private HashMap<String, MemoryAsset> assetStore = new HashMap<>();
    private HashMap<String, MemoryListing> listingStore = new HashMap<String, MemoryListing>();
    private HashMap<String, MemoryPurchase> purchaseStore = new HashMap<String, MemoryPurchase>();

    private MemoryAgent(Ocean ocean, DID did) {
        super(ocean, did);
    }

    /**
     * Creates a new MemoryAgent with the given DID
     *
     * @param did DID for this agent
     * @return A MemoryAgent with the given DID
     */
    public static MemoryAgent create(DID did) {
        return new MemoryAgent(Ocean.connect(), did);
    }

    /**
     * Creates a new MemoryAgent with a randomised DID
     *
     * @return A MemoryAgent with the given DID
     */
    public static MemoryAgent create() {
        return new MemoryAgent(Ocean.connect(), DID.parse(DID.createRandomString()));
    }

    /**
     * Creates a new MemoryAgent with the given DID
     *
     * @param did DID for this agent
     * @return A MemoryAgent with the given DID
     */
    public static MemoryAgent create(String did) {
        return create(DID.parse(did));
    }

    /**
     * Registers an Asset with this Agent
     *
     * @param a The Asset to register
     * @return Asset The asset found, or null if the agent does not have the asset available
     * @throws AuthorizationException if requestor does not have register permission
     * @throws StorageException       if there is an error in storing the Asset
     */
    @Override
    public Asset registerAsset(Asset a) {
        MemoryAsset ma = MemoryAsset.create(a);
        assetStore.put(ma.getAssetID(), ma);
        return ma;
    }

    /**
     * Registers an Asset with this Agent
     *
     * @param a The Asset to register
     * @return Asset The asset uploaded
     * @throws AuthorizationException if requestor does not have register permission
     * @throws StorageException       if there is an error in storing the Asset
     */
    @Override
    public Asset uploadAsset(Asset a) {
        MemoryAsset ma = MemoryAsset.create(a);
        registerAsset(ma);
        return ma;
    }

    /**
     * Get Asset
     *
     * @param id The Asset to get
     * @return Asset The asset found
     * @throws AuthorizationException if requestor does not have register permission
     * @throws StorageException       if there is an error in loading the Asset
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
     * @param params    Positional parameters for the invoke operation
     * @return A Job instance allowing access to the invoke job status and result
     * @throws IllegalArgumentException if required parameters are not available.
     */
    @Override
    public Job invoke(Operation operation, Asset... params) {
        if (!(operation instanceof AMemoryOperation)) {
            throw new IllegalArgumentException("Operation must be a MemoryOperation but got: " + Utils.getClass(operation));
        }
        return operation.invoke(params);
    }


    /**
     * Invokes the specified operation on this agent. If the invoke is successfully launched,
     * will return a Job instance that can be used to access the result, otherwise throws an
     * exception.
     *
     * @param operation The operation to invoke on this agent
     * @param params    named parameters for the invoke operation
     * @return A Job instance allowing access to the invoke job status and result
     * @throws IllegalArgumentException if operation not a AMemoryOperation
     */
    @Override
    public Job invoke(Operation operation, Map<String, Object> params) {
        if (!(operation instanceof AMemoryOperation)) {
            throw new IllegalArgumentException("Operation must be a MemoryOperation but got: " + Utils.getClass(operation));
        }
        return operation.invoke(params);
    }
    /**
     * Invokes the specified operation on this agent. If the invoke is successfully launched,
     * will return a Job instance that can be used to access the result, otherwise throws an
     * exception.
     *
     * @param operation The operation to invoke on this agent
     * @param params    named parameters for the invoke operation
     * @return A Job instance allowing access to the invoke job status and result
     * @throws IllegalArgumentException if operation not a AMemoryOperation
     */
    @Override
    public Job invokeAsync(Operation operation, Map<String, Object> params) {

        // check the mode if sync then throw exception
        if(isSyncMode(operation)){
            throw new StarfishValidationException("Mode must be Async for this operation");
        }
        if (!(operation instanceof AMemoryOperation)) {
            throw new IllegalArgumentException("Operation must be a MemoryOperation but got: " + Utils.getClass(operation));
        }
       return operation.invoke(params);
    }

    @Override
	public Asset getAsset(DID did) {
		return getAsset(did.getID());
	}

    @Override
    public Listing getListing(String id) {
        return listingStore.get(id);
    }

    @Override
    public Purchase getPurchase(String id) {
        return purchaseStore.get(id);
    }


    /**
     * Create a Listing with the given data
     *
     * @param listingData map of listing data
     * @return A listing for this agent
     */
    @Override
    public Listing createListing(Map<String, Object> listingData) {
        if (listingData.get("assetid") == null) {
            throw new IllegalArgumentException("Asset Id is mandatory, cannot be null");
        }

        listingStore.put(listingData.get("id").toString(), MemoryListing.create(this, listingData));
        return listingStore.get(listingData.get("id").toString());
    }

    /**
     * API to get the Purchase instance
     *
     * @param purchaseData map of purchased data based on listing id
     * @return MemoryPurchase instance of new memory purchase
     */
    public MemoryPurchase createPurchase(Map<String, Object> purchaseData) {
        if (purchaseData.get("listingid") == null) {
            throw new IllegalArgumentException("Listing Id is mandatory, cannot be null");
        }

        purchaseStore.put(purchaseData.get("id").toString(), MemoryPurchase.create(this, purchaseData));
        return purchaseStore.get(purchaseData.get("id").toString());
    }


    /**
     * API to check if the operation mode is sync or Async
     * @param operation operation of which mode need to be checked
     * @return true if mode is sync else false
     */
    private boolean isSyncMode(Operation operation) {
        Map<String,Object> metaData = operation.getMetadata();
        Object mode = metaData.get("modes");
        if(mode!=null && mode.toString().equals("sync")){
            return true;
        }
        return false;
    }
}
