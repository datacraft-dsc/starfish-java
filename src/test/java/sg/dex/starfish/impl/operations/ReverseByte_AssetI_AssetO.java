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
public class ReverseByte_AssetI_AssetO extends AMemoryOperation implements Operation {

	protected ReverseByte_AssetI_AssetO(String meta, MemoryAgent memoryAgent) {
		super(meta,memoryAgent);
	}

	/**
	 * Returns the AssetID for this ReverseByte_AssetI_AssetO
	 *
	 * @return AssetID for this ReverseByte_AssetI_AssetO
	 */
	@Override
	public String getAssetID() {
		return "reverse-bytes";
	}

	/**
	 * Creates a new instance of ReverseByte_AssetI_AssetO
	 *
	 * @return new instance of ReverseByte_AssetI_AssetO
	 */
	public static ReverseByte_AssetI_AssetO create(String meta, MemoryAgent memoryAgent) {


		return new ReverseByte_AssetI_AssetO(meta,memoryAgent);
	}

	@Override
	public Job<Asset> invokeAsync(Map<String, Object> params) {
		// default implementation for an asynchronous invoke job in memory, using a Future<Asset>.
		// Implementations may override this for custom behaviour (e.g. a custom thread pool)
		// But this should be sufficient for most cases.
		final CompletableFuture<Asset> future = new CompletableFuture<>();

		MemoryAgent.THREAD_POOL.submit(() -> {
			try {
				Asset result = compute(params);
				future.complete(result); // success
			} catch (Throwable t) {
				future.completeExceptionally(t); // failure
			}
			assert (future.isDone());
		});

		MemoryJob<Asset> memoryJob = MemoryJob.create(future);
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
	private Asset doCompute(Object input) {
//		Object a=(JSON.toMap(input.toString())).get(ID);
		 Asset asset =(Asset)input;
//
		byte[] bytes = asset.getContent();
		int length = bytes.length;
		for (int i = 0; i < (length / 2); i++) {
			byte temp = bytes[i];
			bytes[i] = bytes[length - i - 1];
			bytes[length - i - 1] = temp;
		}
		byte[] data = new byte[]{3, 2, 1};
		Asset result = MemoryAsset.create(data);
		return result;
	}

	/**
	 * API that implement the compute logic that will reverse the content of an Asset.
	 * @param params
	 * @return
	 */
	protected Asset compute(Map<String, Object> params) {
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
		Asset a=doCompute(params.get("input"));
		result.put("output",a);
		// wait for some time
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return result;

	}

}
