package sg.dex.starfish.impl.memory;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 *Class representing a local in-memory operation asset.
 *
 * Intended for use in testing or local development situations.
 * Abstract class that have common code required for different memory operation implementation
 */
public abstract class AMemoryOperation extends AMemoryAsset implements Operation {

    protected MemoryAgent memoryAgent;
    protected AMemoryOperation(String metaString,MemoryAgent memoryAgent) {
        super(metaString);
        this.memoryAgent=memoryAgent;
    }

    @Override
    public Job invoke(Map<String, Asset> params) {
        // default implementation for an asynchronous invoke job in memory, using a Future<Asset>.
        // Implementations may override this for custom behaviour (e.g. a custom thread pool)
        // But this should be sufficient for most cases.
        final CompletableFuture<Asset> future = new CompletableFuture<Asset>();

        MemoryAgent.THREAD_POOL.submit(() -> {
            try {
                Asset result = compute(params);
                future.complete(result); // success
            } catch (Throwable t) {
                future.completeExceptionally(t); // failure
            }
            assert (future.isDone());
        });

        return MemoryJob.create(future);
    }

    @Override
    public Job invokeAsync(Map<String, Asset> params) {
        // default implementation for an asynchronous invoke job in memory, using a Future<Asset>.
        // Implementations may override this for custom behaviour (e.g. a custom thread pool)
        // But this should be sufficient for most cases.
        final CompletableFuture<Asset> future = new CompletableFuture<Asset>();

        MemoryAgent.THREAD_POOL.submit(() -> {
            try {
                Asset result = compute(params);
                future.complete(result); // success
            } catch (Throwable t) {
                future.completeExceptionally(t); // failure
            }
            assert (future.isDone());
        });

        return MemoryJob.create(future);
    }

    /**
     * Computes the result of the invoke job using the provided assets
     *
     * @param params The named parameters for this computation
     * @return Asset The result of the computation as an asset
     * @throws IllegalArgumentException is a required parameter is not present or of incorrect type
     */
    protected abstract Asset compute(Map<String, Asset> params);


}
