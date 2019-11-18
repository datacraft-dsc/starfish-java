package sg.dex.starfish;

import java.io.InputStream;
import java.util.Map;

import sg.dex.crypto.Hash;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StarfishValidationException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

/**
 * Interface representing a data asset.
 * <p>
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
    default boolean isDataAsset() {
        return true;
    }

    /**
     * Gets an input stream that can be used to consume the content of this asset.
     * <p>
     * Will throw an exception if consumption of the asset data in not possible locally.
     *
     * @return An input stream allowing consumption of the asset data
     * @throws AuthorizationException if requester does not have access permission
     * @throws StorageException       if unable to load the Asset
     */
    @Override
    InputStream getContentStream();

    /**
     * Gets the size of this data asset's content
     *
     * @return The size of the asset in bytes
     */
    long getContentSize();


    /**
     * This method is to validate the hash of the asset content .
     * it will calculate the hash of the content of an Asset
     * then compare with the hash value included in metadata,
     * if both are not the same , StarfishValidation Exception will be thrown
     *
     * @return boolean  true if the content is valid else false
     * @throws StarfishValidationException if hash content is not matched , exception will be thrown
     */
    default boolean validateContentHash() {

        Object contentHashFromMetadata = this.getMetadata().get(Constant.CONTENT_HASH);
        if (null == contentHashFromMetadata) {
            throw new StarfishValidationException("Content hash is not included in the metadata");
        }

        String contentHash = Hex.toString(Hash.sha3_256(Utils.stringFromStream(this.getContentStream())));
        // if hash of asset content is equal
        if (contentHashFromMetadata.toString().equals(contentHash)) {
            return true;

        }
        throw new StarfishValidationException("Failed to validate content hash");
    }

    /**
     * This method is used to calculate the hash of the content by using keccak256 hashing algorithm.
     *
     * @return the content of hash as string
     */

    default String getContentHash() {

        return Hex.toString(Hash.sha3_256(this.getContent()));
    }

    /**
     * This method is to include the content of hash in the asset metadata.
     * Hash of the content will be calculated based on sha3_256String hashing algo , and the hash content will
     * be included in the asset metadata.
     * This hash content will be used to validate the integrity of asset content
     * Also this operation is only applicable if the Asset is of type DataAsset , if Asset is not
     * DataAsset Unsupported Operation Exception will be thrown
     *
     * @return respective data asset sub class.
     * @throws UnsupportedOperationException if the Asset type is not DataAsset
     */
    default DataAsset includeContentHash() {

        // check if the hash content is already present also
        // validate if content is valid or not
        if (null != this.getMetadata().get(Constant.CONTENT_HASH) && validateContentHash()) {

            return this;

        } else {
            Map<String, Object> metaMap = this.getMetadata();
            metaMap.put(Constant.CONTENT_HASH, getContentHash());

            return this.updateMeta(JSON.toPrettyString(metaMap));
        }
    }

    /**
     * Get the new Data Asset based on metaData passed as n argument.
     * This method will be implemented by the sub class
     *
     * @param newMetaData new meta data that will be used to create a Data Asset.
     * @return the respective dataAsset based on sub-class
     * @throws UnsupportedOperationException if this operation  is not supported by sub-class
     */
    default DataAsset updateMeta(String newMetaData) {
        throw new UnsupportedOperationException("This Operation is not supported");
    }

}
