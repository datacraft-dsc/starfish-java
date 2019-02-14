package sg.dex.starfish;

/**
 * Abstract base class for asset implementations
 * @author Mike
 *
 */
public abstract class AAsset implements Asset {

	@Override
	public String toString() {
		return getAssetID();
	}
}
