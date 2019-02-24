package sg.dex.starfish.impl.operations;

import java.util.Map;

import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.AMemoryOperation;
import sg.dex.starfish.impl.memory.MemoryAsset;

/**
 * Basic implementation of an operation which reverses the byte sequence of a
 * data asset
 *
 * @author Mike
 *
 */
public class ReverseBytesOperation extends AMemoryOperation {

	protected ReverseBytesOperation(String meta) {
		super(meta);
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
	public static ReverseBytesOperation create() {
		String meta = "{}";
		return new ReverseBytesOperation(meta);
	}

	/**
	 * Returns the Asset result of computation
	 *
	 * @return Asset result of computation
	 */
	@Override
	public Asset compute(Asset... params) {
		if (params.length != 1) throw new IllegalArgumentException("Wrong arity, exactly 1 parameter required");
		return doCompute( params[0]);
	}
	
	private Asset doCompute(Asset input) {
		byte[] bytes = input.getBytes();
		int length = bytes.length;
		for (int i = 0; i < (length / 2); i++) {
			byte temp = bytes[i];
			bytes[i] = bytes[length - i - 1];
			bytes[length - i - 1] = temp;
		}
		Asset result = MemoryAsset.create(bytes);
		return result;
	}

	@Override
	protected Asset compute(Map<String, Asset> params) {
		Asset input=params.get("input");
		return doCompute(input);
	}

}
