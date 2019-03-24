package sg.dex.starfish.impl;

import java.util.Map;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Job;
import sg.dex.starfish.Operation;
import sg.dex.starfish.util.Params;

/**
 * Abstract base class representing invokable operations in the Ocean ecosystem
 * 
 * @author Mike
 *
 */
public abstract class AOperation extends AAsset implements Operation {

	/**
	 * Creates an operation object with the provided metadata string
	 * 
	 * @param meta Canonical metadata String to use for this operation
	 */
	protected AOperation(String meta) {
		super(meta);
	}

	@Override
	public final boolean isDataAsset() {
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getParamSpec() {
		Map<String,Object> meta=getMetadata();
		Map<String, Object> paramSpec= (Map<String, Object>) meta.get("params");
		return paramSpec;
	}
	
//	@Override
//	public Job invoke(Asset... params) {
//		return invoke(Params.formatParams(this, params));
//	}
	
}
