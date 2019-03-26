package sg.dex.starfish.impl.memory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.impl.AOperation;

/**
 * Abstract base class for operations executed in-memory.
 * 
 * Subclasses should override compute(....) to implement the in-memory computation of results.
 * Invocations to this operation will invoke compute(....) in a separate threat, using a 
 * future to return the result of the memory Job.
 *
 * Memory operations do not require a specific agent to operate. 
 *
 * @author Mike
 *
 */
public abstract class AMemoryOperation extends AOperation {

	/**
	 * Creates a memory operation with the provided metadata.
	 *
	 * @param meta The metadata string describing this operation
	 */
	protected AMemoryOperation(String meta) {
		super(meta);
	}

	@Override
	public Job invoke(Map<String, Asset> params) {
		// default implementation for an asynchronous invoke job in memory, using a Future<Asset>.
		// Implementations may override this for custom behaviour (e.g. a custom thread pool)
		// But this should be sufficient for most cases.
		final CompletableFuture<Asset> future=new CompletableFuture<Asset>();

	    MemoryAgent.THREAD_POOL.submit(() -> {
	        try {
	        	Asset result=compute(params);
	        	future.complete(result); // success
	        } catch (Throwable t) {
	        	future.completeExceptionally(t); // failure
	        }
	        assert(future.isDone());
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
	protected abstract Asset compute(Map<String,Asset> params);
}
