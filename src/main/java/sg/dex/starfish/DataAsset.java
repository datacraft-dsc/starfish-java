package sg.dex.starfish;

import java.io.InputStream;

/**
 * Interface representing a data asset.
 * 
 * A data asset is any asset that can be represented as an immutable sequence of bytes.
 * As such, data assets offer the following properties:
 * - They can be validated with a hash of the byte content
 * - The byte representation of the data can be obtained (subject to appropriate permissions)
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
	 * @return An input stream allowing consumption of the asset data
	 */
	public InputStream getInputStream();

	@Override
	public byte[] getBytes();
}
