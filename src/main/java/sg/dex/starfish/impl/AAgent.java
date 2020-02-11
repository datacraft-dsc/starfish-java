package sg.dex.starfish.impl;

import sg.dex.starfish.Agent;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Resolver;
import sg.dex.starfish.util.DDO;
import sg.dex.starfish.util.DID;

import java.util.Map;

/**
 * Class representing an Agent in the Ecosystem
 * <p>
 * Agents are addressed with a W3C DID
 *
 * @author Mike
 * @version 0.5
 */
public abstract class AAgent implements Agent {

    protected final DID did;
    protected final Resolver resolver;
    private Map<String, Object> ddo;

    /**
     * Create an agent with the provided Resolver and DID
     *
     * @param resolver The Resolver to use for this agent
     * @param did      The DID for this agent
     */
    protected AAgent(Resolver resolver, DID did) {
        this.resolver = resolver;
        this.did = did;
    }


    @Override
    public DID getDID() {
        return did;
    }

    @Override
    public Map<String, Object> getDDO() {
        if (ddo == null) {
            ddo = resolver.getDDO(did);
        }
        return ddo;
    }

    @Override
    public <R extends Asset> R getAsset(DID did) {
        String assetID = did.getPath();
        if (assetID == null) throw new IllegalArgumentException("Expected Asset ID in DID path");
        return getAsset(assetID);
    }

    /**
     * Returns the serviceEndpoint for the specified service type.
     * Searches the agent's DDO for the appropriate service.
     *
     * @param type The type of the service to find
     * @return The service endpoint, or null if not found
     */
    public String getEndpoint(String type) {
        Map<String, Object> ddo = getDDO();
        if (ddo == null) return null;
        return DDO.getEndpoint(ddo, type);
    }

    @Override
    public String toString() {
        return "#starfish/agent \"" + getDID().toString() + "\"";
    }
}
