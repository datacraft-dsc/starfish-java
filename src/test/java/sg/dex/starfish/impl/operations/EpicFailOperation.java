package sg.dex.starfish.impl.operations;

import java.util.Map;

import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.AMemoryOperation;

import sg.dex.starfish.util.JobFailedException;

/**
 * Basic implementation of an operation which always fails
 * data asset
 *
 * @author Mike
 *
 */
public class EpicFailOperation extends AMemoryOperation {

	protected EpicFailOperation(String meta) {
		super(meta);
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
		String meta = "{}";
		return new EpicFailOperation(meta);
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
	protected Asset compute(Asset... params) {
		throw new JobFailedException("EPIC FAIL",
					     new Exception("Fail by design"));
	}

}
