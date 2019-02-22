package sg.dex.starfish.impl.invoke;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;

import sg.dex.starfish.AAgent;
import sg.dex.starfish.Asset;
import sg.dex.starfish.InvokableAgent;
import sg.dex.starfish.Job;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.Operation;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.Utils;

public class InvokeAgent extends AAgent implements InvokableAgent {

	protected InvokeAgent(Ocean ocean, DID did) {
		super(ocean, did);
		// TODO Auto-generated constructor stub
	}

	public URI getInvokeURI() {
		try {
			return new URI("http://10.0.1.164:3000/invokesync");
		}
		catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public HttpResponse invokeRemote() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(getInvokeURI());
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(httpget);
			try {
			    return response;
			} finally {
			    response.close();
			}
		}
		catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String... args) {
		InvokeAgent ag= new InvokeAgent(Ocean.connect(),Utils.createRandomDID());
		System.out.println(ag.invokeRemote());
	}

	/**
	 * Registers an Asset with this Agent
	 *
	 * @param a The Asset to register
	 * @throws AuthorizationExceptionn if requestor does not have register permission
	 * @throws StorageException if there is an error in storing the Asset
	 * @return Asset The asset found, or null if the agent does not have the asset available
	 */
	@Override
	public void registerAsset(Asset a) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Gets an asset for the given asset ID from this agent.
	 * Returns null if the asset ID does not exist.
	 *
	 * @param id The ID of the asset to get from this agent
	 * @throws AuthorizationException if requestor does not have access permission
	 * @throws StorageException if there is an error in retreiving the Asset
	 * @return Asset The asset found
	 */
	@Override
	public Asset getAsset(String id) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Uploads an asset to this agent. Registers the asset with the agent if required.
	 *
	 * Throws an exception if upload is not possible, with the following likely causes:
	 * - The agent does not support uploads of this asset type / size
	 * - The data for the asset cannot be accessed by the agent
	 *
	 * @param a Asset to upload
	 * @throws AuthorizationException if requestor does not have upload permission
	 * @throws StorageException if there is an error in uploading the Asset
	 * @return Asset An asset stored on the agent if the upload is successful
	 */
	@Override
	public Asset uploadAsset(Asset a) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Invokes this operation with the given positional parameters.
	 *
	 * @param params Positional parameters for this invoke job
	 * @throws IllegalArgumentException if required parameters are not available.
	 * @return The Job for this invoked operation
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Job invoke(Operation operation, Asset... params) {
		JSONObject request=new JSONObject();
		request.put("operation",operation.getAssetID());
		request.put("params",formatParams(operation,params));
		return null;
	}

	/**
	 * Creates the "params" part of the invoke payload using the spec in the operation metadata
	 * and the passed arguments
	 * @param operation
	 * @param params
	 * @return The "params" portion of the invoke payload as a JSONObject
	 */
	@SuppressWarnings("unchecked")
	private JSONObject formatParams(Operation operation, Asset... params) {
		JSONObject result=new JSONObject();
		Map<String,JSONObject> paramSpec=operation.getParamSpec();
		for (Map.Entry<String,JSONObject> me:paramSpec.entrySet()) {
			String paramName=me.getKey();
			JSONObject spec=me.getValue();
			// String type=(String) spec.get("type");
			Object positionObj=spec.get("position");
			int pos=(positionObj!=null)?Utils.coerceInt(positionObj):-1;
			boolean required=Utils.coerceBoolean(spec.get("required"));
			if ((pos>=0)&&(pos<params.length)) {
				Asset a=params[pos];
				JSONObject value=a.getParamValue();
				result.put(paramName,value);
			}
			if (required) {
				throw new IllegalArgumentException("Paramter "+paramName+" is required but not supplied");
			}
		}
		return result;
	}

}
