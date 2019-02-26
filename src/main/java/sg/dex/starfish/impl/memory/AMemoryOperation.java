package sg.dex.starfish.impl.memory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import sg.dex.starfish.AOperation;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;

import sg.dex.starfish.util.JobFailedException;

/**
 * Abstract base class for operations executed in-memory.
 *
 * Memory operations to not require a specific agent to operate.
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

	/**
	 * Invokes this operation with the given positional parameters.
	 *
	 * @param params Positional parameters for this invoke job
	 * @throws IllegalArgumentException if required parameters are not available.
	 * @return The Job for this invoked operation
	 */
	@Override
	public Job invoke(Asset... params) {
		// default implementation for an invoke job in memory, using a Future<Asset>.
		// Implementations may override this for custom behaviour
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

	@Override
	public Job invoke(Map<String, Asset> params) {
		// default implementation for an invoke job in memory, using a Future<Asset>.
		// Implementations may override this for custom behaviour
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
	 * @param params The positional parameters for this computation
	 * @throws IllegalArgumentException if required parameters are not available.
	 * @throws JobFailedException if the computation fails
	 * @return Asset The result of the computation as an asset
	 */
	protected abstract Asset compute(Asset... params);

	/**
	 * Computes the result of the invoke job using the provided assets
	 *
	 * @param params The named parameters for this computation
	 * @return Asset The result of the computation as an asset
	 */
	protected abstract Asset compute(Map<String,Asset> params);
}
