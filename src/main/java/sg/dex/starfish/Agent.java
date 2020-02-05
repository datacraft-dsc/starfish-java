package sg.dex.starfish;

import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.util.DID;

import java.util.Map;

/**
 * Interface representing an Agent in the Ocean ecosystem
 *
 * @author Mike
 * @version 0.5
 */
public interface Agent {

    /**
     * Gets the DDO for the agent
     *
     * @return The DDO of the agent as a metadata Map
     */
    Map<String, Object> getDDO();

    /**
     * Gets the DID for an Agent
     *
     * @return DID The DID that can be used to address this agent in the Ocean Ecosystem
     */
    DID getDID();

    /**
     * Registers an Asset with this Agent.
     * The Agent must support metadata storage (e.g. via the DEP15 Meta API)
     *
     * @param asset The Asset to register
     * @return Asset
     * @throws AuthorizationException        if requester does not have register permission
     * @throws UnsupportedOperationException if the agent does not support metadata storage
     */
    <R extends Asset> R registerAsset(Asset asset);

    /**
     * Registers Asset metadata with this Agent, returning an Asset of the
     * appropriate type for this Agent.
     * <p>
     * The Agent must support metadata storage (e.g. via the DEP15 Meta API)
     *
     * @param metaString The metadata of an Asset to register
     * @return Asset
     * @throws AuthorizationException        if requester does not have register permission
     * @throws UnsupportedOperationException if the agent does not support metadata storage
     */
    <R extends Asset> R registerAsset(String metaString);

    /**
     * Gets an asset for the given asset ID from this agent.
     * Returns null if the asset ID does not exist in the context of the agent
     *
     * @param id The ID of the asset to get from this agent
     * @return Asset The asset if found, or null if the agent does not have the specified asset
     */
    <R extends Asset> R getAsset(String id);

    /**
     * Gets an asset for the given asset DID from this agent.
     * Returns null if the asset not exist.
     *
     * @param did The DID of the asset to get from this agent
     * @return Asset The asset if found, or null if not found
     * @throws AuthorizationException if requester does not have access permission
     * @throws StorageException       if there is an error in retrieving the Asset
     */
    <R extends Asset> R getAsset(DID did);

    /**
     * Uploads an asset to this agent. Registers the asset with the agent if required.
     * <p>
     * Throws an exception if upload is not possible, with the following likely causes:
     * - The agent does not support uploads of this asset type / size
     * - The data for the asset cannot be accessed by the agent
     *
     * @param a Asset to upload
     * @return Asset An asset stored on the agent if the upload is successful
     * @throws AuthorizationException if requester does not have upload permission
     * @throws StorageException       if there is an error in uploading the Asset
     */
    <R extends Asset> R uploadAsset(Asset a);


}
