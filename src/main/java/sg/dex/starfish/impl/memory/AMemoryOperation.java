package sg.dex.starfish.impl.memory;

import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
import sg.dex.starfish.exception.StarfishValidationException;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static sg.dex.starfish.constant.Constant.*;

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
        super(metaString, memoryAgent);


    }


    @Override
    public Job invokeAsync(Map<String, Object> params) {
        // validate the memory operation metadata
        Utils.validateAssetMetaData(this.getMetadataString());

        if (validateOperationMode(this) != null && !validateOperationMode(this).contains(ASYNC)) {
            throw new StarfishValidationException("Mode must be Async for this operation");
        }

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

        // validate the memory operation metadata
        Utils.validateAssetMetaData(this.getMetadataString());

        if (validateOperationMode(this) != null && !validateOperationMode(this).contains(SYNC)) {
            throw new StarfishValidationException("Mode must be Sync for this operation");
        }
        return compute(params);
    }

    @Override
    public Job invoke(Map<String, Object> params) {

        return invokeAsync(params);
    }

    private List<String> validateOperationMode(Operation operation) {

        Map<String, Object> operationData = JSON.toMap(operation.getMetadata().get(OPERATION).toString());
        //1. check if mode is present

        if (operationData.get(MODES) == null) {
            return null;
        }
        List<String> modeLst = (List<String>) operationData.get(MODES);
        for (Object mode : modeLst) {
            if (mode.toString().equals(SYNC) ||
                    mode.toString().equals(ASYNC)) {

            } else {
                throw new StarfishValidationException("Invalid mode of the given operation:" + operation.toString());
            }
        }

        return modeLst;


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
