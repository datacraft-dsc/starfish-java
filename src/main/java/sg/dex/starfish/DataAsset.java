package sg.dex.starfish;

/**
 * Interface representing a data asset
 * 
 * @author Mike
 *
 */
public interface DataAsset extends Asset {
	
	@Override
	public default boolean isDataAsset() {
		return true;
	}
}
