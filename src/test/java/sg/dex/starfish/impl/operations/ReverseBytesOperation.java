package sg.dex.starfish.impl.operations;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
import sg.dex.starfish.impl.memory.AMemoryOperation;
import sg.dex.starfish.impl.memory.MemoryAgent;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.memory.MemoryJob;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Basic implementation of an operation which reverses the byte sequence of a
 * data asset
 *
 * @author Mike
 *
 */
public class ReverseBytesOperation extends AMemoryOperation implements Operation {

	protected ReverseBytesOperation(String meta, MemoryAgent memoryAgent) {
		super(meta,memoryAgent);
	}

	/**
	 * Returns the AssetID for this ReverseBytesOperation
	 *
	 * @return AssetID for this ReverseBytesOperation
	 */
	@Override
	public String getAssetID() {
		return "reverse-bytes";
	}

	/**
	 * Creates a new instance of ReverseBytesOperation
	 *
	 * @return new instance of ReverseBytesOperation
	 */
	public static ReverseBytesOperation create(String meta,MemoryAgent memoryAgent) {


		return new ReverseBytesOperation(meta,memoryAgent);
	}




	@Override
	public Job invokeAsync(Map<String, Asset> params) {
		return memoryAgent.invokeAsync(this,params);
	}

	@Override
	public Map<String, Object> invokeResult(Map<String, Object> params) {
		return syncCallToReverse(params);
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
	 * API to reverse the byte array
	 * @param input
	 * @return
	 */
	private Asset doCompute(Asset input) {
		byte[] bytes = input.getContent();
		int length = bytes.length;
		for (int i = 0; i < (length / 2); i++) {
			byte temp = bytes[i];
			bytes[i] = bytes[length - i - 1];
			bytes[length - i - 1] = temp;
		}
		Asset result = MemoryAsset.create(bytes);
		return result;
	}

	/**
	 * API that implement the compute logic that will reverse the content of an Asset.
	 * @param params
	 * @return
	 */
	protected Asset compute(Map<String, Asset> params) {
		if (params==null ||params.get("input")==null) throw new IllegalArgumentException("Missing parameter 'input'");
		return doCompute(params.get("input"));
	}
	/**
	 * API to test the Sync call execution
	 * @param params
	 * @return
	 */
	public  Map<String, Object> syncCallToReverse(Map<String, Object> params){


		Map<String,Object> result = new HashMap<>();

		String str =new StringBuilder(params.get("to-reverse").toString()).reverse().toString();
		result.put("hash-value",str);

		return result;

	}

}
