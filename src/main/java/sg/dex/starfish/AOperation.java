package sg.dex.starfish;

import java.util.Map;

import org.json.simple.JSONObject;

/**
 * Abstract base class representing invokable operation sin th eOCean ecosystem
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
	public boolean isDataAsset() {
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getParamSpec() {
		JSONObject meta=getMetadata();
		return (Map<String, Object>) meta.get("params");
	}
	
}
