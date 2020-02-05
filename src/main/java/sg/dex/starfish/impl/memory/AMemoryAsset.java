package sg.dex.starfish.impl.memory;

import sg.dex.starfish.impl.AAsset;
import sg.dex.starfish.util.DID;

/**
 * Abstract base class for in-memory assets
 * <p>
 * Contains common code required for
 * MemoryAsset/MemoryBundle/MemoryOperation classes.
 */
public abstract class AMemoryAsset extends AAsset {

    protected MemoryAgent memoryAgent;

    /**
     * Constructor for Memory Asset
     *
     * @param metaData metaData
     * @param aAgent   memory Agent
     */
    protected AMemoryAsset(String metaData, MemoryAgent aAgent) {
        super(metaData);
        memoryAgent = aAgent;
    }


    @Override
    public DID getDID() {
        // DID of a memory asset is the DID of the appropriate agent with the asset ID as a resource path
        DID agentDID = memoryAgent.getDID();
        return agentDID.withPath(getAssetID());
    }

    /**
     * Method to get the Memory Agent reference
     *
     * @return memory agent reference
     */
    public MemoryAgent getMemoryAgent() {
        return memoryAgent;
    }

}
