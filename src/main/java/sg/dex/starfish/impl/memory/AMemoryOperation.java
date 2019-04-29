package sg.dex.starfish.impl.memory;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
import sg.dex.starfish.util.JSON;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static sg.dex.starfish.constant.Constant.DATE_CREATED;
import static sg.dex.starfish.constant.Constant.TYPE;

/**
 *Class representing a local in-memory operation asset.
 *
 * Intended for use in testing or local development situations.
 * Abstract class that have common code required for different memory operation implementation
 */
public abstract class AMemoryOperation extends AMemoryAsset implements Operation {

    protected AMemoryOperation(String metaString) {
        super(metaString);
    }

    /**
     * API to build the metadata
     * @param data
     * @param meta
     * @return
     */
    protected static String buildMetaData(String data, Map<String, Object> meta) {
        data = data == null ? "" : data;
        Map<String, Object> ob = new HashMap<>();
        ob.put(DATE_CREATED, Instant.now().toString());
        ob.put("data", data);
        ob.put(TYPE, "operation");

        if (meta != null) {
            for (Map.Entry<String, Object> me : meta.entrySet()) {
                ob.put(me.getKey(), me.getValue());
            }
        }

        return JSON.toString(ob);
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

    /**
     * Computes the result of the invoke job using the provided assets
     *
     * @param params The named parameters for this computation
     * @return Asset The result of the computation as an asset
     * @throws IllegalArgumentException is a required parameter is not present or of incorrect type
     */
    protected abstract Asset compute(Map<String, Asset> params);
}
