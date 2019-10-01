package sg.dex.starfish.impl.memory;

import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.ProvUtil;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Class representing a local in-memory operation asset. Operations are executed in
 * a thread pool managed by a MemoryAgent.
 * <p>
 * Intended for use in testing or local development situations.
 * <p>
 * This is a abstract base class that implements common functionality required for
 * different memory operations. Subclasses only need to override the `compute` method
 * to provide an alternative operation implementation.
 */
public abstract class AMemoryOperation extends AMemoryAsset implements Operation {

    protected AMemoryOperation(String metaString, MemoryAgent memoryAgent) {
        super(getMetaWithProv(metaString,memoryAgent), memoryAgent);
    }

    /**
     * Method to add provenace in the metadata if not present for all Memory Operation
     * @param metaString Metadata as string
     * @param memoryAgent agent
     * @return metadata with PROVENANCE
     */
    private static String getMetaWithProv(String metaString,MemoryAgent memoryAgent) {

        Map<String,Object> meta = JSON.toMap(metaString);
        //check if already provenance is present

        if(meta.get(Constant.PROVENANCE)== null) {
            // adding default provenance
            String actId = UUID.randomUUID().toString();
            // check if DID is present in agent then agent id will be did
            // else will create a random UUID
            String agentId = memoryAgent.getDID()!=null?
                    memoryAgent.getDID().toString():
                    UUID.randomUUID().toString();

            Map<String, Object> provenanceData = ProvUtil.createPublishProvenance(actId, agentId);
            meta.put(Constant.PROVENANCE, provenanceData);
        }
        return JSON.toPrettyString(meta);
    }

    @Override
    public Job invokeAsync(Map<String, Object> params) {
        // default implementation for an asynchronous invoke job in memory, using a
        // Future<Asset>.
        // Implementations may override this for custom behaviour (e.g. a custom thread
        // pool)
        // But this should be sufficient for most cases.
        final CompletableFuture<Map<String, Object>> future = new CompletableFuture<>();
        MemoryJob memoryJob = MemoryJob.create(future);
        MemoryAgent.THREAD_POOL.submit(() -> {
            try {
                Map<String, Object> result = compute(params);
                if (future.complete(result)) {
                }
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });

        return memoryJob;
    }

    @Override
    public final Map<String, Object> invokeResult(Map<String, Object> params) {
        return compute(params);
    }

    @Override
    public Job invoke(Map<String, Object> params) {
        return invokeAsync(params);
    }

    /**
     * Method for computation of the memory operation.
     * <p>
     * Subclass implementations should override this method to provide their own
     * compute functionality.
     *
     * @param params The input map of strings to input values
     * @return The result map of Strings to output values
     */
    protected abstract Map<String, Object> compute(Map<String, Object> params);

}
