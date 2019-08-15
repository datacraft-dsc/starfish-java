package sg.dex.starfish.impl.operations;

import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
import sg.dex.starfish.impl.memory.AMemoryOperation;
import sg.dex.starfish.impl.memory.MemoryAgent;
import sg.dex.starfish.impl.memory.MemoryJob;
import sg.dex.starfish.util.Hex;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class CalculateHash_AssetI_JsonO extends AMemoryOperation implements Operation {

    protected CalculateHash_AssetI_JsonO(String meta, MemoryAgent memoryAgent) {
        super(meta, memoryAgent);
    }

    public static CalculateHash_AssetI_JsonO create(String meta, MemoryAgent memoryAgent) {


        return new CalculateHash_AssetI_JsonO(meta, memoryAgent);
    }

    @Override
    public String getAssetID() {
        return "JSON-test";
    }

    @Override
    public Job<Map<String,Object>> invokeAsync(Map<String, Object> params) {
        // default implementation for an asynchronous invoke job in memory, using a Future<Asset>.
        // Implementations may override this for custom behaviour (e.g. a custom thread pool)
        // But this should be sufficient for most cases.
        final CompletableFuture<Map<String,Object>> future = new CompletableFuture<>();

        MemoryAgent.THREAD_POOL.submit(() -> {
            try {
            	Map<String,Object> result = compute(params);
                future.complete(result); // success
            } catch (Throwable t) {
                future.completeExceptionally(t); // failure
            }
            assert (future.isDone());
        });

        MemoryJob<Map<String,Object>> memoryJob = MemoryJob.create(future);
        return memoryJob;
    }

    @Override
    public Map<String, Object> invokeResult(Map<String, Object> params) {
        return syncCallToReverse(params);
    }


    @Override
    public Job invoke(Map<String, Object> params) {
        // default implementation for an asynchronous invoke job in memory, using a Future<Asset>.
        // Implementations may override this for custom behaviour (e.g. a custom thread pool)
        // But this should be sufficient for most cases.
        final CompletableFuture<Object> future = new CompletableFuture<>();

        MemoryAgent.THREAD_POOL.submit(() -> {
            try {
                Object result = compute(params);
                future.complete(result); // success
            } catch (Throwable t) {
                future.completeExceptionally(t); // failure
            }
            assert (future.isDone());
        });

        return MemoryJob.create(future);
    }


    private Map<String,Object> doCompute(Object input) {
        Asset a= (Asset)input;
        String hash =Hex.toString(Hash.sha3_256(a.getContent()));

        Map<String,Object> result = new HashMap<>();
        result.put("output", hash);
        return result;
    }


    protected Map<String,Object> compute(Map<String, Object> params) {
        if (params == null || params.get("input") == null)
            throw new IllegalArgumentException("Missing parameter 'input'");
        return doCompute(params.get("input"));
    }

    public Map<String, Object> syncCallToReverse(Map<String, Object> params) {

        return(Map<String, Object> ) doCompute(params.get("input"));

    }

}
