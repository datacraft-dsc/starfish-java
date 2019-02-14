package sg.dex.starfish;

/**
 * Class representing an asset bundle
 * @author Mike
 *
 */
public class AssetBundle extends AAsset {

	protected AssetBundle(String meta) {
		super(meta);
	}

	@Override
	public boolean isDataAsset() {
		return false;
	}

}
