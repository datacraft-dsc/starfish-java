package sg.dex.starfish.impl.operations;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Operation;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.memory.AMemoryOperation;
import sg.dex.starfish.impl.memory.MemoryAgent;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.util.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic implementation of an operation which reverses the byte sequence of a
 * data asset
 *
 * @author Mike
 *
 */
public class ReverseByte_AssetI_AssetO extends AMemoryOperation implements Operation {

	protected ReverseByte_AssetI_AssetO(String meta, MemoryAgent memoryAgent) {
		super(meta, memoryAgent);
	}

	/**
	 * Creates a new instance of ReverseByte_AssetI_AssetO
	 * @param memoryAgent memory agent
	 * @param meta meta
	 * @return new instance of ReverseByte_AssetI_AssetO
	 */
	public static ReverseByte_AssetI_AssetO create(String meta, MemoryAgent memoryAgent) {

		return new ReverseByte_AssetI_AssetO(meta, memoryAgent);
	}

	/**
	 * API to reverse the byte array
	 * 
	 * @param asset
	 * @return
	 */
	private Map<String, Object> doCompute(Asset asset) {

		byte[] bytes = asset.getContent();
		int length = bytes.length;
		for (int i = 0; i < (length / 2); i++) {
			byte temp = bytes[i];
			bytes[i] = bytes[length - i - 1];
			bytes[length - i - 1] = temp;
		}
		Map<String, Object> result = new HashMap<>();
		Asset res = MemoryAsset.create(bytes);
		result.put("status", Constant.SUCCEEDED);
		memoryAgent.registerAsset(res);
		result.put("did", res.getAssetID());

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result",result);
		return resultMap;
	}

	@Override
	protected Map<String, Object> compute(Map<String, Object> params) {
		Object o=params.get("input");
		if (o == null) throw new IllegalArgumentException("Missing parameter 'input'");
		return doCompute((Asset)o);
	}
}
