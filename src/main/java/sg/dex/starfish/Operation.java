package sg.dex.starfish;

import java.util.Map;

import org.json.simple.JSONObject;

/**
 * Interface representing an invokable operation
 * @author Mike
 *
 */
public interface Operation extends Asset {

	@Override 
	public default boolean isOperation() {
		return true;
	}
	
	/**
	 * Invokes this operation with the given positional parameters.
	 * 
	 * @param params Positional parameters for this invoke job
	 * @throws IllegalArgumentException if required parameters are not available.
	 * @return
	 */
	public Job invoke(Asset... params);

	public Map<String, JSONObject> getParamSpec();
}
