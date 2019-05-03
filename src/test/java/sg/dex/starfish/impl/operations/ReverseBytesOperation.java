package sg.dex.starfish.impl.operations;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
import sg.dex.starfish.impl.memory.AMemoryOperation;
import sg.dex.starfish.impl.memory.MemoryAgent;

import java.util.Map;

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
	public Map<String, java.lang.Object> invokeResult(Map<String, java.lang.Object> params) {
		return  memoryAgent.syncCallToReverse(this,params);
	}

	@Override
	public Job invoke(Map<String, Asset> params) {
		return null;
	}




}
