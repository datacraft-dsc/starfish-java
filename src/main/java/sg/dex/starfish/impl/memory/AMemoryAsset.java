package sg.dex.starfish.impl.memory;

import sg.dex.starfish.impl.AAsset;

/**
 * This is an abstract class which have common code required for
 * MemoryAsset/MemoryBundle/MemoryOperation class.
 *
 */
public abstract class AMemoryAsset extends AAsset {

    private  MemoryAgent memoryAgent;

    protected AMemoryAsset(String metaData,MemoryAgent aAgent) {
        super(metaData);
        memoryAgent = aAgent;

    }

    /**
     * Constructor
     * @param metaData
     */
    public AMemoryAsset(String metaData) {
        super(metaData);
        memoryAgent = MemoryAgent.create();

    }

    /**
     * API to get the memory agent reference
     * @return The MemoryAgent associated with this asset
     */
    public MemoryAgent getMemoryAgent() {
        return memoryAgent;
    }
    
    @Override
    public String toString() {
        return getAssetID();
    }

}
