package sg.dex.starfish.impl.operations;

import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
import sg.dex.starfish.impl.memory.AMemoryOperation;
import sg.dex.starfish.impl.memory.MemoryAgent;
import sg.dex.starfish.impl.memory.MemoryJob;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class FindPrime_JsonI_JsonO extends AMemoryOperation implements Operation {

    protected FindPrime_JsonI_JsonO(String meta, MemoryAgent memoryAgent) {
        super(meta, memoryAgent);
    }

    public static FindPrime_JsonI_JsonO create(String meta, MemoryAgent memoryAgent) {


        return new FindPrime_JsonI_JsonO(meta, memoryAgent);
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
        Integer num = (Integer.parseInt( input.toString()));

        StringBuilder res = new StringBuilder();

        for (int i = 2; i <= num; i++) {
            if (isPrime(i))
                res.append(i + "  ");
        }
        Map<String, Object> response = new HashMap<>();
        response.put("output", res);
        return response;
    }


    protected Map<String,Object> compute(Map<String, Object> params) {
        if (params == null || params.get("input") == null)
            throw new IllegalArgumentException("Missing parameter 'input'");
        return doCompute(params.get("input"));
    }

    public Map<String, Object> syncCallToReverse(Map<String, Object> params) {
        return (Map<String, Object>) doCompute(params.get("input"));

    }

    private boolean isPrime(int n) {
        if (n <= 1)
            return false;

        for (int i = 2; i < n; i++)
            if (n % i == 0)
                return false;

        return true;
    }

}
