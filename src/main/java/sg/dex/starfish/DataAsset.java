package sg.dex.starfish;

import sg.dex.crypto.Hash;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StarfishValidationException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

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
	 *
	 * @return The size of the asset in bytes
	 */
	public abstract long getContentSize();


	/**
	 * This method is to validate the hash of the asset content .
	 * it will calculate the hash of the content of an Asset
	 * then compare with the hash value included in metadata,
	 * if both are not the same , StarfishValidation Exception will be thrown
	 * @return boolean  true if the content is valid else false
	 *
	 * @throws StarfishValidationException if hash content is not matched , exception will be thrown
	 */
	public default boolean validateContentHash() {

		Object contentHashFromMetadata =  this.getMetadata().get(Constant.CONTENT_HASH);
		if(null == contentHashFromMetadata){
            throw new StarfishValidationException("Content hash is not included in the metadata");
        }

		String contentHash = Hex.toString(Hash.keccak256(Utils.stringFromStream(this.getContentStream())));
		if (null != contentHashFromMetadata && !contentHashFromMetadata.toString().equals(contentHash)) {
			throw new StarfishValidationException("Failed to validate content hash");
		}
		return true;
	}

	/**
	 * This method is used to calculate the hash of the content by using keccak256 hashing algorithm.
	 *
	 * @return the content of hash as string
	 */

	public default String getContentHash() {

		return Hex.toString(Hash.keccak256(this.getContent()));
	}

	/**
	 * This method is to include the content of hash in the asset metadata.
	 * Hash of the content will be calculated based on keccak256 hashing algo , and the hash content will
	 * be included in the asset metadata.
	 * This hash content will be used to validate the integrity of asset content
	 *
	 * @return respective data asset sub class.
	 */
	public default DataAsset includeContentHash() {

		// check if the hash content is already present
		if(null != this.getMetadata().get(Constant.CONTENT_HASH)){

			if(validateContentHash()){
				return this;
			}
		}

		Map<String, Object> metaMap = this.getMetadata();
		metaMap.put(Constant.CONTENT_HASH, getContentHash());

		if (this instanceof DataAsset){
			return this.updateMeta(metaMap);

		}

		throw new StarfishValidationException("Asset or its content is not Valid");
	}

	/**
	 * Get the new Data Asset base on metaData passed as n argument.
	 * This method will be implemented by the sub class
	 *
	 * @param newMetaData new meta data that will be used to create a Data Asset.
	 * @return the respective dataAsset based on sub-class
	 * @throws UnsupportedOperationException if this operation  is not supported by sub-class
	 */
	public default DataAsset updateMeta(Map<String, Object> newMetaData) {
		throw new UnsupportedOperationException("This Operation is not supported");
	}

}
