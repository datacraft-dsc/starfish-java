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

	@Override
	public String getAssetID() {
		return "epic-fail";
	}

	/**
	 * Creates a new instance of ReverseBytesOperation
	 * 
	 * @return
	 */
	public static EpicFailOperation create() {
		String meta = "{}";
		return new EpicFailOperation(meta);
	}

	@Override
	public Asset compute(Asset... params) {
		throw new RuntimeException("EPIC FAIL");
	}

}
