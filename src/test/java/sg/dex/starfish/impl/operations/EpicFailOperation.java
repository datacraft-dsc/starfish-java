package sg.dex.starfish.impl.operations;

import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.AMemoryOperation;

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
	 * Returns the Asset result of computation
	 *
	 * @return Asset result of computation
	 */
	@Override
	public Asset compute(Asset... params) {
		throw new RuntimeException("EPIC FAIL");
	}

}
