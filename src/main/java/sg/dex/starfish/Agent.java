package sg.dex.starfish;

import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.util.DID;

import java.util.Map;

/**
 * Interface representing an Agent in the Ocean ecosystem
 * @author Mike
 * @version 0.5
 */
public interface Agent {

	/**
	 * Gets the DDO for the agent
	 * @return The DDO of the agent as a metadata Map
	 */
	public Map<String,Object> getDDO();

	/**
	 * Gets the DID for an Agent
	 *
	 * @return DID The DID that can be used to address this agent in the Ocean Ecosystem
	 */
	public DID getDID();

	/**
	 * Registers an asset with this agent.
	 * The agent must support metadata storage.
	 *
	 * @param asset The asset to register
	 * @throws AuthorizationException if requester does not have register permission
	 * @throws StorageException if unable to register the Asset
	 * @throws UnsupportedOperationException if the agent does not support metadata storage
	 * @return Asset
	 */
	public Asset registerAsset(Asset asset);

	/**
	 * Gets an asset for the given asset ID from this agent.
	 * Returns null if the asset ID does not exist.
	 *
	 * @param id The ID of the asset to get from this agent
	 * @throws AuthorizationException if requester does not have access permission
	 * @throws StorageException if there is an error in requester the Asset
	 * @return Asset The asset found
	 */
	public Asset getAsset(String id);
	
	/**
	 * Gets an asset for the given asset DID from this agent.
	 * Returns null if the asset not exist.
	 *
	 * @param did The DID of the asset to get from this agent
	 * @throws AuthorizationException if requester does not have access permission
	 * @throws StorageException if there is an error in retrieving the Asset
	 * @return Asset The asset found, or null if not found
	 */	
	public Asset getAsset(DID did);

	/**
	 * Uploads an asset to this agent. Registers the asset with the agent if required.
	 *
	 * Throws an exception if upload is not possible, with the following likely causes:
	 * - The agent does not support uploads of this asset type / size
	 * - The data for the asset cannot be accessed by the agent
	 *
	 * @param a Asset to upload
	 * @throws AuthorizationException if requester does not have upload permission
	 * @throws StorageException if there is an error in uploading the Asset
	 * @return Asset An asset stored on the agent if the upload is successful
	 */
	public Asset uploadAsset(Asset a);


}
