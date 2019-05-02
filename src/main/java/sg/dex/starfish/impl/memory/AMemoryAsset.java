package sg.dex.starfish.impl.memory;

import sg.dex.starfish.impl.AAsset;

/**
 * This is an abstract class which have common code required for
 * MemoryAsset/MemoryBundle/MemoryOperation class.
 *
 */
public abstract class AMemoryAsset extends AAsset {

    protected   MemoryAgent memoryAgent;

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


    @Override
    public String toString() {
        return getAssetID();
    }

}
