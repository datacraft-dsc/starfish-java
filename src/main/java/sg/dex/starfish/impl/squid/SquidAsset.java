package sg.dex.starfish.impl.squid;

import sg.dex.starfish.impl.AAsset;
import sg.dex.starfish.util.DID;

/**
  * Class representing a SquidAsset on the Ocean Network.
  *
  * @author Tom
  *
  */
public class SquidAsset extends AAsset {
	private DID did;

	private SquidAsset(String meta, DID did) {
		super(meta);
		this.did = did;
	}

	private SquidAsset(String meta) {
		super(meta);
		this.did = null;
	}

	/**
	 * Creates SquidAsset using the metadata and DID
	 *
	 * @param meta Asset metadata
	 * @param did DID
	 * @return A new SquidAsset
	 */
	public static SquidAsset create(String meta, DID did) {
		return new SquidAsset(meta, did);
	}

	/**
	 * Creates SquidAsset using the metadata
	 *
	 * @param meta Asset metadata
	 * @return A new SquidAsset
	 */
	public static SquidAsset create(String meta) {
		return new SquidAsset(meta);
	}

	/**
	 * Gets the DID for this SquidAsset
	 *
	 * @return DID
	 */
	@Override
	public DID getAssetDID() {
		return did;
	}

	@Override
	public boolean isDataAsset() {
		return false;
	}

	/**
	 * Returns true if this asset is an operation, i.e. can be invoked on an
	 * appropriate agent
	 *
	 * @return true if this asset is an operation, false otherwise
	 */
	@Override
	public boolean isOperation() {
		return false;
	}

}
