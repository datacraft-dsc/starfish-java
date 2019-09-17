package sg.dex.starfish.impl.memory;

import sg.dex.starfish.impl.AAsset;

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

    /**
     * Constructor for Memory Asset
     *
     * @param metaData metadata
     */
    protected AMemoryAsset(String metaData) {
        super(metaData);
    }


}
