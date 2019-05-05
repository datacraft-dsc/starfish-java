package sg.dex.starfish.impl.memory;

import sg.dex.starfish.impl.AAsset;

/**
 * This is an abstract class which have common code required for
 * MemoryAsset/MemoryBundle/MemoryOperation class.
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

	@Override
	public String toString() {
		return getAssetID();
	}

}
