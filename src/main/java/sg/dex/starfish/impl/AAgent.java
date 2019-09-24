package sg.dex.starfish.impl;

import sg.dex.starfish.Agent;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.util.DID;

import java.util.List;
import java.util.Map;

/**
 * Class representing an Agent in the Ocean Ecosystem
 * <p>
 * Agents are addressed with a W3C DID
 *
 * @author Mike
 * @version 0.5
 */
public abstract class AAgent implements Agent {

    protected final DID did;

    private Map<String, Object> ddo;

    protected final Ocean ocean;

    /**
     * Create an agent with the provided Ocean connection and DID
     *
     * @param ocean The ocean connection to use for this agent
     * @param did   The DID for this agent
     */
    protected AAgent(Ocean ocean, DID did) {
        this.ocean = ocean;
        this.did = did;
    }

    /**
     * Create an agent with the provided Ocean connection and DID
     *
     * @param did The DID for this agent
     */
    protected AAgent(DID did) {
        this.ocean = Ocean.connect();
        this.did = did;
    }

    @Override
    public DID getDID() {
        return did;
    }

    @Override
    public Map<String, Object> getDDO() {
        if (ddo == null) {
            ddo = ocean.getDDO(did);
        }
        return ddo;
    }
    
    @Override
    public <R extends Asset> R getAsset(DID did) {
    	String assetID=did.getPath();
    	if (assetID==null) throw new IllegalArgumentException("Expected Asset ID in DID path");
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
        @SuppressWarnings("unchecked")
        List<Map<String,Object>> services = (List<Map<String,Object>>) ddo.get(Constant.SERVICE);
        if (services == null) return null;
        for (Map<String,Object> service : services) {
            if (type.equals(service.get("type"))) return (String) service.get(Constant.SERVICE_ENDPOINT);
        }
        return null;
    }

    @Override
    public String toString() {
        return "#starfish/agent \"" + getDID().toString() + "\"";
    }
}
