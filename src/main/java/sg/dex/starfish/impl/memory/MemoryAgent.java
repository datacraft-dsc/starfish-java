package sg.dex.starfish.impl.memory;

import sg.dex.starfish.*;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.impl.AAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Utils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// TODO: should implement MarketAgent, StorageAgent etc.

/**
 * An in-memory agent implementation
 *
 * @author Mike
 */
public class MemoryAgent extends AAgent implements Invokable, MarketAgent {
    /**
     * The singleton default memory agent instance
     */
    /**
     * A cached thread pool for jobs executed in memory
     */
    public static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    private HashMap<String, MemoryAsset> assetStore = new HashMap<String, MemoryAsset>();
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
    public Job invoke(Operation operation, Map<String, Asset> params) {
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
        return null;
    }


    /**
     * Create a Listing with the given data
     *
     * @param listingData
     * @return A listing for this agent
     */
    @Override
    public Listing createListing(Map<String, Object> listingData) {
        if (listingData.get("assetid") == null) {
            throw new IllegalArgumentException("Assset Id is mandatory");
        }

        Map<String, Object> responseMetaData = getResponseMetaDataListing(listingData);
        //String data =JSON.toPrettyString(responseMetaData);
        listingStore.put(responseMetaData.get("id").toString(), MemoryListing.create(this, responseMetaData));
        return listingStore.get(responseMetaData.get("id").toString());
    }

    /**
     * API to create a response similar to Remote Agents responses.
     *
     * @param meta
     * @return
     */
    private Map<String, Object> getResponseMetaDataListing(Map<String, Object> meta) {
        Map<String, Object> responseMetadata = new HashMap<>();

        responseMetadata.putAll(meta);
        // default status
        responseMetadata.put("status", "unpublished");

        responseMetadata.put("id", DID.createRandom());

        responseMetadata.put("trust_level", meta.get("trust_level") == null ? 0 : meta.get("trust_level"));
        responseMetadata.put("userid", meta.get("userid") == null ? 1234 : meta.get("userid"));
        responseMetadata.put("agreement", meta.get("agreement") == null ? 0 : meta.get("agreement"));
        responseMetadata.put("info", meta.get("info") == null ? 0 : meta.get("info"));
        responseMetadata.put("utime", meta.get("utime") == null ? Instant.now() : meta.get("utime"));
        responseMetadata.put("ctime", meta.get("ctime") == null ? Instant.now() : meta.get("ctime"));

        return responseMetadata;


    }

    /**
     * API to get the Purchase instance
     *
     * @param purchaseData
     * @return MemoryPurchase
     */
    public MemoryPurchase createPurchase(Map<String, Object> purchaseData) {
        if (purchaseData.get("listingid") == null) {
            throw new IllegalArgumentException("Listing Id is mandatory");
        }

        Map<String, Object> responseMetaData = getResponseMetaDataPurchase(purchaseData);

        purchaseStore.put(responseMetaData.get("id").toString(), MemoryPurchase.create(this, responseMetaData));
        return purchaseStore.get(responseMetaData.get("id").toString());
    }

    /**
     * API to create a response similar to Remote Agents responses.
     *
     * @param purchaseData
     * @return Map<String, Object> responseMetaDataPurchase
     */
    private Map<String, Object> getResponseMetaDataPurchase(Map<String, Object> purchaseData) {


        Map<String, Object> responseMetadata = new HashMap<>();
        responseMetadata.putAll(purchaseData);
        responseMetadata.put("status", "wishlist");
        responseMetadata.put("id", DID.createRandomString());

        responseMetadata.put("userid", purchaseData.get("userid") == null ? 1234 : purchaseData.get("userid"));
        responseMetadata.put("info", purchaseData.get("info") == null ? 0 : purchaseData.get("info"));
        responseMetadata.put("agreement", purchaseData.get("agreement") == null ? Instant.now() : purchaseData.get("agreement"));
        responseMetadata.put("ctime", purchaseData.get("ctime") == null ? Instant.now() : purchaseData.get("agreement"));
        responseMetadata.put("utime", purchaseData.get("utime") == null ? Instant.now() : purchaseData.get("agreement"));

        return responseMetadata;

    }
}
