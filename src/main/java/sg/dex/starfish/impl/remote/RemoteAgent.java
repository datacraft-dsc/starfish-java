package sg.dex.starfish.impl.remote;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import sg.dex.starfish.AAgent;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.TODOException;
import sg.dex.starfish.util.AuthorizationException;
import sg.dex.starfish.util.StorageException;

/**
 * Class implementing a remote storage agent using the Storage API
 * @author Mike
 *
 */
public class RemoteAgent extends AAgent {

	/**
	 * Creates a RemoteAgent with the specified Ocean connection and DID
	 * @param ocean Ocean connection to use
	 * @param did DID for this agent
	 */
	protected RemoteAgent(Ocean ocean, DID did) {
		super(ocean, did);
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
		throw new TODOException();
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
		throw new TODOException();
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
		throw new TODOException();
	}

	/**
	 * Gets a URL string for accessing the specified asset ID
	 * @param id The asset ID to address
	 * @return The URL for the asset as a String
	 */
	public String getAssetURL(String id) {
		throw new TODOException();
	}

	public URL getURL(RemoteAsset remoteAsset) {
		String storageEndpoint=getStorageEndpoint();
		if (storageEndpoint==null) throw new IllegalStateException("No storage endpoint available for agent");
		try {
			return new URL(storageEndpoint+"/"+remoteAsset.getAssetID());
		}
		catch (MalformedURLException e) {
			throw new Error("Failed to get asset URL",e);
		}
	}

	/**
	 * Gets the storage endpoint for this agent
	 * @return The storage endpoint for this agent e.g. "https://www.myagent.com/api/v1/storage"
	 */
	public String getStorageEndpoint() {
		return getEndpoint("Ocean.Storage");
	}

	/**
	 * Returns the serviceEndpoint for the specified service type.
	 * Searched the agent's DDO for the appropriate service.
	 *
	 * @param type The type of the service to find
	 * @return The service endpoint, or null if not found
	 */
	public String getEndpoint(String type) {
		JSONObject ddo=getDDO();
		JSONArray services = (JSONArray) ddo.get("service");
		if (services==null) return null;
		for (Object o: services) {
			JSONObject service=(JSONObject)o;
			if (type.equals(service.get("type"))) return (String) service.get("serviceEndpoint");
		}
		return null;
	}

}
