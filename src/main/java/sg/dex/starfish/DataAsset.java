package sg.dex.starfish;

import java.io.InputStream;

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
	
	/**
	 * Gets an input stream that can be used to consume the content of this asset.
	 * 
	 * Will throw an exception if consumption of the asset data in not possible locally.
	 * @return
	 */
	public InputStream getInputStream();
}
