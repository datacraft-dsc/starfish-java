package sg.dex.starfish;

import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StorageException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
 * @version 0.5
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
	 * @throws AuthorizationException if requester does not have access permission
	 * @throws StorageException if unable to load the Asset
	 * @return An input stream allowing consumption of the asset data
	 */
	public InputStream getContentStream();

	/**
	 * Gets the data content of this data asset as a byte[] array.
	 *
	 * @throws UnsupportedOperationException If this asset does not support getting byte data
	 * @throws AuthorizationException if requester does not have access permission
	 * @throws StorageException if unable to load the Asset
	 * @return The byte contents of this asset.
	 */
	@Override
	public default byte[] getContent() {
		InputStream is = getContentStream();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		byte[] buf = new byte[16384];

		int bytesRead;
		try {
			while ((bytesRead = is.read(buf, 0, buf.length)) != -1) {
				buffer.write(buf, 0, bytesRead);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return buffer.toByteArray();
	}
	
	/**
	 * Gets the size of this data asset's content
	 * @return The size of the asset in bytes
	 */
	public abstract long getContentSize();

}
