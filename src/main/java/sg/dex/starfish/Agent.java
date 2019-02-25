package sg.dex.starfish;

import java.util.Map;

import sg.dex.starfish.util.DID;

/**
 * Interface representing an Agent in the Ocean ecosystem
 * @author Mike
 *
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
	 * @throws UnsupportedOperationException if the agent does not support metadata storage
	 * @param asset The asset to register
	 * @return 
	 */
	public Asset registerAsset(Asset asset);

	/**
	 * Gets an asset for the given asset ID from this agent.
	 * Returns null if the asset ID does not exist.
	 * 
	 * @param id The ID of the asset to get from this agent
	 * @return Asset The asset found, or null if the agent does not have the asset available
	 */
	public Asset getAsset(String id);

	/**
	 * Uploads an asset to this agent. Registers the asset with the agent if required.
	 * 
	 * Throws an exception if upload is not possible, with the following likely causes:
	 * - The agent does not support uploads of this asset type / size
	 * - The data for the asset cannot be accessed by the agent
	 * 
	 * @param a Asset to upload
	 * @return Asset An asset stored on the agent if the upload is successful
	 */
	public Asset uploadAsset(Asset a);
}
