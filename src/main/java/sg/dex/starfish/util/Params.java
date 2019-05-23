package sg.dex.starfish.util;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Operation;
import sg.dex.starfish.exception.StarfishValidationException;
import sg.dex.starfish.impl.remote.RemoteAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for handling invoke parameters
 *This class includes the methods for formatting the input passed, preparing the result ,
 * formatting the response
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
	 * API to prepare required result from a raw result based on metadata of the asset
	 * @param params
	 * @param result
	 * @param paramName
	 * @param type
	 */
	private static void prepareResult(Map<String, Object> params, HashMap<String, Object> result, String paramName, String type) {
		if (type.equals("asset")) {
			// validate if input is type asset or not
			Asset a = (Asset) params.get(paramName);
			Map<String, Object> value = a.getParamValue();
			result.put(paramName, value);
		} else if (type.equals("json")) {
			JSON.validateJson(JSON.toPrettyString(params));
			result.put(paramName, params.get(paramName));
		} else {
			throw new StarfishValidationException("Invalid type of Input .Accepted Input: " +
					"Asset or Json. Input given: " + type);
		}
	}

    /**
     * This method is used to format result of the response form Invoke calls.
     * It will map the result data of the response to result defined in the metadata
     * Eg: if the result of the response is type Json ,then it type caste the response to Json
     *     if the result of the response is type asset, then it ype caste the response map to asset.
     * @param operation instance reference
     * @param res response received from the Invoke call
     * @param remoteAgent agent on which this operation has done.
     * @return formatted map of the response received
     */
	public static Map<String, Object> formatResponse(Operation operation, Map<String, Object> res, RemoteAgent remoteAgent) {

		Map<String,Object> response = JSON.toMap(res.get("results").toString());
		HashMap<String,Object> result=new HashMap<>(response.size());
		Map<String,Object> paramSpec= (Map<String,Object>)operation.getOperationSpec().get("results");

		for (Map.Entry<String,Object> me:paramSpec.entrySet()) {
			String paramName=me.getKey();
			Map<String, Object> spec = (Map<String, Object>) me.getValue();
			String type=(String) spec.get("type");
			if (response.containsKey(paramName)) {
				if (type.equals("asset")) {
					// get the did of the asset
					Map<String,Object> didMap= (Map<String,Object>)response.get(paramName);
					Asset a = (remoteAgent.getAsset(didMap.get("did").toString()));
					result.put(paramName, a);
				} else if (type.equals("json")) {
					JSON.validateJson(JSON.toPrettyString(response));
					result.put(paramName, response.get(paramName));
				} else {
					throw new StarfishValidationException("Invalid type of Input .Accepted Input: " +
							"Asset or Json. Input given: " + type);
				}

			}

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
