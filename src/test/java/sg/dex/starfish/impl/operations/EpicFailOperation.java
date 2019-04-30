package sg.dex.starfish.impl.operations;

import sg.dex.starfish.Asset;
import sg.dex.starfish.exception.JobFailedException;
import sg.dex.starfish.impl.memory.AMemoryOperation;
import sg.dex.starfish.impl.memory.MemoryAgent;

import java.util.Map;

/**
 * Basic implementation of an operation which always fails
 * data asset
 *
 * @author Mike
 *
 */

public class EpicFailOperation extends AMemoryOperation {

	protected EpicFailOperation(String meta,MemoryAgent memoryAgent) {

		super(meta,memoryAgent);
	}

	/**
	 * Returns the AssetID for this EpicFailOperation
	 *
	 * @return AssetID for this EpicFailOperation
	 */
	@Override
	public String getAssetID() {
		return "epic-fail";
	}

	/**
	 * Creates a new instance of EpicFailOperation
	 *
	 * @return new instance of EpicFailOperation
	 */
	public static EpicFailOperation create() {
		String meta =  "{\"params\": {\"input\": {\"required\":true, \"type\":\"asset\", \"position\":0}}}";
		MemoryAgent memoryAgent = MemoryAgent.create();
		return new EpicFailOperation(meta,memoryAgent);
	}

	/**
	 * Computes the result of the invoke job using the provided assets
	 *
	 * @param params The positional parameters for this computation
	 * @throws IllegalArgumentException if required parameters are not available.
	 * @throws JobFailedException if the computation fails
	 * @return Asset The result of the computation as an asset
	 */
	@Override
	public Asset compute(Map<String, Asset> params) {
		throw new JobFailedException("EPIC FAIL",
					     new Exception("Fail by design"));
	}

	@Override
	public Map<String, Object> syncCallToReverse(Map<String, Object> params) {
		return null;
	}

	@Override
	public Map<String, Object> invokeResult(Map<String, Object> params) {
		return null;
	}
}
