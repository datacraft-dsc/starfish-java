package sg.dex.starfish.impl.operations;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
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

public class EpicFailOperation extends AMemoryOperation implements Operation {

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
	public static EpicFailOperation create(String meta) {
		//String meta =  "{\"params\": {\"input\": {\"required\":true, \"type\":\"asset\", \"position\":0}}}";
		MemoryAgent memoryAgent = MemoryAgent.create();
		return new EpicFailOperation(meta,memoryAgent);
	}

	@Override
	public Job invokeAsync(Map<String, Asset> params) {
		return memoryAgent.invokeAsync(this,params);
	}

	@Override
	public Map<String, Object> invokeResult(Map<String, Object> params) {
		return null;
	}

	@Override
	public Job invoke(Map<String, Asset> params) {
		return memoryAgent.invoke(this,params);
	}


}
