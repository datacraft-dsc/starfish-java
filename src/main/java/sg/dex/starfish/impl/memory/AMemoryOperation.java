package sg.dex.starfish.impl.memory;

/**
 *Class representing a local in-memory operation asset.
 *
 * Intended for use in testing or local development situations.
 * Abstract class that have common code required for different memory operation implementation
 */
public abstract class AMemoryOperation extends AMemoryAsset {

    protected MemoryAgent memoryAgent;
    protected AMemoryOperation(String metaString,MemoryAgent memoryAgent) {
        super(metaString);
        this.memoryAgent=memoryAgent;
    }

}
