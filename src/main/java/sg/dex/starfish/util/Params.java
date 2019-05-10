package sg.dex.starfish.util;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Operation;
import sg.dex.starfish.exception.TODOException;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for handling invoke parameters
 *
 * @author Mike
 *
 */
public class Params {

	/**
	 * Creates the "params" part of the invoke payload using the spec in the operation metadata
	 * and the passed positional arguments
	 * @param operation The operation to format the parameters for
	 * @param params A map of parameter names to assets
	 * @throws IllegalArgumentException if missing required parameter
	 * @return The "params" portion of the invoke payload as a Map
	 */
	@SuppressWarnings("unchecked")
	public
	static Map<String,Object> formatParams(Operation operation, Map<String,Object> params) {
		HashMap<String,Object> result=new HashMap<>(params.size());
		Map<String,Object> paramSpec= operation.getParamsSpec();
		for (Map.Entry<String,Object> me:paramSpec.entrySet()) {
			String paramName=me.getKey();
				Map<String, Object> spec = (Map<String, Object>) me.getValue();
				String type=(String) spec.get("type");
				boolean required = Utils.coerceBoolean(spec.get("required"));
				if (params.containsKey(paramName)) {
					prepareResult(params, result, paramName, type);

				}
				// added additional check so ,if the param is required then
				// it must be present in the params map
				// if required is false then it may or may not present
				if (required && !result.containsKey(paramName)) {
					throw new IllegalArgumentException("Parameter " + paramName + " is required but not supplied");
				}
		}
		return result;
	}

	/**
	 * API to prepare result for invoke call
	 * @param params
	 * @param result
	 * @param paramName
	 * @param type
	 */
	private static void prepareResult(Map<String, Object> params, HashMap<String, Object> result, String paramName, String type) {
		if (type.equals("asset")) {
			// validate if input is type asset or not
			//Utils.validateIfAssetType(params.get(paramName));
			Asset a = (Asset) params.get(paramName);
			Map<String, Object> value = a.getParamValue();
			result.put(paramName, value);
		} else if (type.equals("json")) {
			JSON.validateJson(JSON.toPrettyString(params));
			result.put(paramName, params.get(paramName));
		} else {
			throw new TODOException("Invalid type of Input.It must be either Asset or Json , type is : " + type);
		}
	}


	public
	static Map<String,Object> formatResult( Map<String,Object> response) {
		Map<String,Object> result=new HashMap<>(response.size());
		Map<String,Object> paramSpec=(Map<String,Object>)response.get("results");

		//TODO need to check the result type is json or asset then form the response accordingly
		for (Map.Entry<String,Object> me:paramSpec.entrySet()) {
			String paramName = me.getKey();

			Map<String, Object> res = JSON.toMap(response.get("results").toString());
			result = JSON.toMap(res.get(paramName).toString());
		}


		return result;
	}
	/**
	 * Creates the "params" part of the invoke payload using the spec in the operation metadata
	 * and the passed positional arguments
	 * @param operation The operation to format the parameters for
	 * @param params An array of assets to be provided as positional parameters
	 * @throws IllegalArgumentException if missing required parameter
	 * @return The "params" portion of the invoke payload as a JSONObject
	 */
	@SuppressWarnings("unchecked")
	public
	static Map<String,Object> formatParams(Operation operation, Asset... params) {
		HashMap<String,Object> result=new HashMap<>(params.length);
		Map<String,Object> paramSpec=operation.getParamsSpec();
		for (Map.Entry<String,Object> me:paramSpec.entrySet()) {
			String paramName=me.getKey();
			Map<String,Object> spec=(Map<String,Object>)me.getValue();
			// String type=(String) spec.get("type");
			Object positionObj=spec.get("position");
			int pos=(positionObj!=null)?Utils.coerceInt(positionObj):-1;
			boolean required=Utils.coerceBoolean(spec.get("required"));
			if ((pos>=0)&&(pos<params.length)) {
				Asset a=params[pos];
				Map<String,Object> value=a.getParamValue();
				result.put(paramName,value);
			}
			if (required) {
				throw new IllegalArgumentException("Parameter "+paramName+" is required but not supplied");
			}
		}
		return result;
	}
	
	/**
	 * Converts an array of positional parameters to a map of named parameters, according to the
	 * parameter spec of the given operation.
	 * 
	 * @param operation need to get the meta data
	 * @param params all asset on which the validation need to be done
	 * @return A new map containing the named parameters
	 * @throws IllegalArgumentException if a require dparameter is not provided
	 */
	@SuppressWarnings("unchecked")
	public
	static Map<String,Object> namedParams(Operation operation, Asset... params) {
		HashMap<String,Object> result=new HashMap<>(params.length);
		Map<String,Object> paramSpec=operation.getParamsSpec();
		for (Map.Entry<String,Object> me:paramSpec.entrySet()) {
			String paramName=me.getKey();
			Map<String,Object> spec=(Map<String,Object>)me.getValue();
			// String type=(String) spec.get("type");
			Object positionObj=spec.get("position");
			int pos=(positionObj!=null)?Utils.coerceInt(positionObj):-1;
			boolean required=Utils.coerceBoolean(spec.get("required"));
			if ((pos>=0)&&(pos<params.length)) {
				Asset a=params[pos];
				result.put(paramName,a);
			} else if (required) {
				throw new IllegalArgumentException("Parameter "+paramName+" is required but not supplied");
			}
		}
		return result;
	}
	

}
