package sg.dex.starfish.util;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Operation;
import sg.dex.starfish.exception.StarfishValidationException;
import sg.dex.starfish.impl.remote.RemoteAccount;
import sg.dex.starfish.impl.remote.RemoteAgent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for marshalling invokable operation parameters and return values
 */
public class Params {

    /**
     * Creates the "params" part of the invoke payload using the spec in the operation metadata
     * and the passed positional arguments
     *
     * @param operation The operation to format the parameters for
     * @param params    A map of parameter names to assets
     * @return The "params" portion of the invoke payload as a Map
     * @throws IllegalArgumentException if missing required parameter
     */
    @SuppressWarnings("unchecked")
    public
    static Map<String, Object> formatParams(Operation operation, Map<String, Object> params) {
        if (null == params) {
            throw new IllegalArgumentException("Input parameter cannot be null");
        }
        HashMap<String, Object> result = new HashMap<>(params.size());
        Map<String, Object> paramSpec = operation.getParamsSpec();
        for (Map.Entry<String, Object> me : paramSpec.entrySet()) {
            String paramName = me.getKey();
            Map<String, Object> spec = (Map<String, Object>) me.getValue();
            String type = (String) spec.get("type");
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
     *
     * @param params    params
     * @param result    result
     * @param paramName param Name
     * @param type      type
     */
    private static void prepareResult(Map<String, Object> params, HashMap<String, Object> result, String paramName, String type) {
        try {
            if (type.equalsIgnoreCase("asset")) {
                // validate if input is type asset or not
                Asset a = (Asset) params.get(paramName);
                Map<String, Object> value = a.getParamValue();
                result.put(paramName, value);

            } else if (type.equalsIgnoreCase("json")) {

                if (null != params) {
                    //JSON.parse(params.toString());
                    result.put(paramName, params.get(paramName));
                }
            } else {
                throw new StarfishValidationException("Invalid type of Input .Accepted Input: " +
                        "Asset or Json. Input given: " + type);
            }
        } catch (Exception e) {
            throw new StarfishValidationException("Invalid type of Input .Accepted Input: " +
                    "Asset or Json. Input given: " + type);
        }
    }

    /**
     * This method is used to format result of the response form Invoke calls.
     * It will map the result data of the response to result defined in the metadata
     * Eg: if the result of the response is type Json ,then it type caste the response to Json
     * if the result of the response is type asset, then it ype caste the response map to asset.
     *
     * @param operationSpecs instance reference
     * @param response       response received from the Invoke call
     * @return formatted map of the response received
     */
    public static Map<String, Object> formatResponse(Map<String, Object> operationSpecs, Map<String, Object> response, RemoteAccount remoteAccount) throws IOException {

        HashMap<String, Object> result = new HashMap<>(response.size());
        Map<String, Object> outputsSpecs = (Map<String, Object>) operationSpecs.get("outputs");

        for (Map.Entry<String, Object> me : outputsSpecs.entrySet()) {
            String paramName = me.getKey();
            @SuppressWarnings("unchecked")
            Map<String, Object> spec = (Map<String, Object>) me.getValue();
            String type = (String) spec.get("type");
            if (response.containsKey(paramName)) {
                if (type.equals("asset")) {

                    Map<String, Object> didMap = (Map<String, Object>) response.get(paramName);
                    // DID did = DID.create("op", (String)didMap.get("did"), null, null);
                    result.put(paramName, getAsset(didMap.get("did"), remoteAccount));
                } else if (type.equals("json")) {
                    result.put(paramName, response.get(paramName));
                } else {
                    throw new StarfishValidationException("Invalid type of Input. Accepted Input: " +
                            "Asset or Json. Input given: " + type);
                }

            }

        }
        return result;

    }

    public static Map<String, Object> formatResponse(Map<String, Object> operationSpecs, Map<String, Object> response) {

        HashMap<String, Object> result = new HashMap<>(response.size());
        Map<String, Object> outputsSpecs = (Map<String, Object>) operationSpecs.get("outputs");

        for (Map.Entry<String, Object> me : outputsSpecs.entrySet()) {
            String paramName = me.getKey();
            Map<String, Object> spec = (Map<String, Object>) me.getValue();
            String type = (String) spec.get("type");
            if (response.containsKey(paramName)) {
                if (type.equals("asset")) {

                    Map<String, Object> didMap = (Map<String, Object>) response.get(paramName);
                    String userName = ((Map<String, Object>) didMap.get("auth")).get("username").toString();
                    String password = ((Map<String, Object>) didMap.get("auth")).get("password").toString();

                    result.put(paramName, getAsset(didMap.get("did"), RemoteAccount.create(userName, password)));
                } else if (type.equals("json")) {
                    result.put(paramName, response.get(paramName));
                } else {
                    throw new StarfishValidationException("Invalid type of Input. Accepted Input: " +
                            "Asset or Json. Input given: " + type);
                }

            }

        }
        return result;

    }

    private static Object getAsset(Object di, RemoteAccount remoteAccount) {
        if (di == null) {
            throw new StarfishValidationException("DID is null");
        }
        String didS = (String) di;
        DID did = DID.parse(didS);
        return getAssetByDID(did, remoteAccount);

    }

    /**
     * this methid is used to ge the Asset based on DID and the account.
     *
     * @param did           did of the asset
     * @param remoteAccount user credential
     * @return Asset
     * @throws IOException
     */
    public static Asset getAssetByDID(DID did, RemoteAccount remoteAccount) {
        DID agentDID = did.withoutPath();
        String path = did.getPath();
        RemoteAgent remoteAgent = RemoteAgent.connect(agentDID, remoteAccount);
        Asset asset = remoteAgent.getAsset(path);
        return asset;
    }

    /**
     * Creates the "params" part of the invoke payload using the spec in the operation metadata
     * and the passed positional arguments
     *
     * @param operation The operation to format the parameters for
     * @param params    An array of assets to be provided as positional parameters
     * @return The "params" portion of the invoke payload as a JSONObject
     * @throws IllegalArgumentException if missing required parameter
     */
    @SuppressWarnings("unchecked")
    public
    static Map<String, Object> formatParams(Operation operation, Object... params) {
        HashMap<String, Object> result = new HashMap<>(params.length);
        Map<String, Object> paramSpec = operation.getParamsSpec();
        for (Map.Entry<String, Object> me : paramSpec.entrySet()) {
            String paramName = me.getKey();
            Map<String, Object> spec = (Map<String, Object>) me.getValue();
            // String type=(String) spec.get("type");
            Object positionObj = spec.get("position");
            int pos = (positionObj != null) ? Utils.coerceInt(positionObj) : -1;
            boolean required = Utils.coerceBoolean(spec.get("required"));
            if ((pos >= 0) && (pos < params.length)) {
                // TODO: Handle non-asset parameters?
                Asset a = (Asset) params[pos];
                Map<String, Object> value = a.getParamValue();
                result.put(paramName, value);
            }
            if (required) {
                throw new IllegalArgumentException("Parameter " + paramName + " is required but not supplied");
            }
        }
        return result;
    }

    /**
     * Converts an array of positional parameters to a map of named parameters, according to the
     * parameter spec of the given operation.
     *
     * @param operation need to get the meta data
     * @param params    all asset on which the validation need to be done
     * @return A new map containing the named parameters
     * @throws IllegalArgumentException if a require dparameter is not provided
     */
    @SuppressWarnings("unchecked")
    public
    static Map<String, Object> namedParams(Operation operation, Object... params) {
        HashMap<String, Object> result = new HashMap<>(params.length);
        Map<String, Object> paramSpec = operation.getParamsSpec();
        for (Map.Entry<String, Object> me : paramSpec.entrySet()) {
            String paramName = me.getKey();
            Map<String, Object> spec = (Map<String, Object>) me.getValue();
            Object positionObj = spec.get("position");
            int pos = (positionObj != null) ? Utils.coerceInt(positionObj) : -1;
            boolean required = Utils.coerceBoolean(spec.get("required"));
            if ((pos >= 0) && (pos < params.length)) {
                // TODO: handle non-asset parameters>
                Asset a = (Asset) params[pos];
                result.put(paramName, a);
            } else if (required) {
                throw new IllegalArgumentException("Parameter " + paramName + " is required but not supplied");
            }
        }
        return result;
    }


}
