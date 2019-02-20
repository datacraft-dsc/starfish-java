package sg.dex.starfish.impl.operations;

import java.util.concurrent.CompletableFuture;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.impl.memory.AMemoryOperation;
import sg.dex.starfish.impl.memory.MemoryAgent;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.memory.MemoryJob;

/**
 * Basic implementation of an operation which reverses the byte sequence of a data asset
 * 
 * @author Mike
 *
 */
public class ReverseBytesOperation extends AMemoryOperation {

	protected ReverseBytesOperation(String meta) {
		super(meta);
	}

	@Override
	public String getAssetID() {
		return "reverse-bytes";
	}
	
	/**
	 * Creates a new instance of ReverseBytesOperation
	 * @return
	 */
	public static ReverseBytesOperation create() {
		String meta = "{}";
		return new ReverseBytesOperation(meta);
	}

	@Override
	public Job invoke(Asset... params) {
		if (params.length!=1) throw new IllegalArgumentException("Wrong arity, exactly 1 parameter required");
		final CompletableFuture<Asset> future=new CompletableFuture<Asset>();
		
	    MemoryAgent.THREAD_POOL.submit(() -> {
	        try {
	        	Asset a=params[0];
	        	byte[] bytes=a.getBytes();
	        	int length=bytes.length;
	        	for(int i = 0; i < (length / 2); i++)
	        	{
	        	    byte temp = bytes[i];
	        	    bytes[i] = bytes[length - i - 1];
	        	    bytes[length - i - 1] = temp;
	        	}
	        	Asset result=MemoryAsset.create(bytes);
	        	future.complete(result); // success
	        } catch (Throwable t) {
	        	future.completeExceptionally(t); // failure
	        }
	        assert(future.isDone());
	    });		
	    
		return MemoryJob.create(future);
	}


}
