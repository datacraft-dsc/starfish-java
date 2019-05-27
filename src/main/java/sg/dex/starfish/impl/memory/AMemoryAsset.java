package sg.dex.starfish.impl.memory;

import sg.dex.starfish.impl.AAsset;

/**
 * Abstract base class for in-memory assets
 * 
 * Contains common code required for
 * MemoryAsset/MemoryBundle/MemoryOperation classes.
 *
 */
public abstract class AMemoryAsset extends AAsset {

	protected MemoryAgent memoryAgent;

	/**
	 *
	 * @param metaData
	 * @param aAgent
	 */
	protected AMemoryAsset(String metaData, MemoryAgent aAgent) {
		super(metaData);
		memoryAgent = aAgent;
	}

	/**
	 *
	 * @param metaData
	 */
	protected AMemoryAsset(String metaData) {
		super(metaData);
	}



}
