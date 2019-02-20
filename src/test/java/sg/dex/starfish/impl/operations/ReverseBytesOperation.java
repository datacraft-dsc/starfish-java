package sg.dex.starfish.impl.operations;

import sg.dex.starfish.AOperation;

/**
 * Basic implementation of an operation which reverses the byte sequence of a data asset
 * 
 * @author Mike
 *
 */
public class ReverseBytesOperation extends AOperation {

	protected ReverseBytesOperation(String meta) {
		super(meta);
	}

	@Override
	public String getAssetID() {
		return "reverse-bytes";
	}
}
