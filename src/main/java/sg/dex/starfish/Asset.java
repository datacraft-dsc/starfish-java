package sg.dex.starfish;

import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.util.DID;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Interface representing an Asset. An Asset in Starfish is an entity in the
 * decentralised data ecosystem
 * that is:
 * A) Described by metadata
 * B) Able to be used in Digital Supply Lines
 *
 * @author Mike
 * @version 0.5
 */
public interface Asset {

    /**
     * Gets the ID for this Asset.
     * <p>
     * The format of the asset ID is determined by the implementation.
     *
     * @return the assetID
     * @throws UnsupportedOperationException if unable to obtain ID
     */
    String getAssetID();

    /**
     * Gets the  DID for this asset. The DID may include a DID path to specify
     * the precise asset if the DID refers to an agent managing the asset.
     * Throws an exception if a DID is not available or cannot be constructed.
     *
     * @return The global DID for this asset.
     * @see <a href="https://w3c-ccg.github.io/did-spec">W3C DID spec</a>
     */
    DID getDID();

    /**
     * Gets a copy of the JSON metadata for this asset, as a map of strings to
     * values.
     * <p>
     * Asset metadata will differ as per type of asset: (e.g. dataset,
     * operation, bundle)
     *
     * @return New clone of the parsed JSON metadata for this asset
     */
    Map<String, Object> getMetadata();

    /**
     * Returns true if this asset is a data asset, i.e. the asset represents an
     * immutable
     * data object.
     *
     * @return true if the asset is a data asset, false otherwise
     */
    default boolean isDataAsset() {
        return Constant.DATA_SET.equals(getMetadata().get(Constant.TYPE));
    }

    /**
     * Returns true if this asset is an operation, i.e. can be invoked on an
     * appropriate agent
     *
     * @return true if this asset is an operation, false otherwise
     */
    default boolean isOperation() {
        return Constant.OPERATION.equals(getMetadata().get(Constant.TYPE));
    }

    /**
     * Returns the metadata for this asset as a String. Assets should store
     * their metadata by default
     * as a valid JSON string.
     * <p>
     * Warning: Some implementations may not validate the JSON on asset creation
     * and it is possible
     * for the metadata String to contain invalid JSON.
     *
     * @return The metadata of this asset as a String
     */
    String getMetadataString();

    /**
     * Gets the contents of this data asset as a byte[] array.
     *
     * @return The byte contents of this asset.
     * @throws UnsupportedOperationException If this asset does not support
     *                                       getting byte data
     * @throws AuthorizationException        if requester does not have access
     *                                       permission
     * @throws StorageException              if unable to load the Asset
     */
    default byte[] getContent() {
        InputStream is = getContentStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        byte[] buf = new byte[16384];

        int bytesRead;
        try {
            while ((bytesRead = is.read(buf, 0, buf.length)) != -1) {
                buffer.write(buf, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new StorageException("Unable to get Asset content", e);
        }
        return buffer.toByteArray();
    }

    /**
     * Gets the contents of this data asset as a input stream.
     *
     * @return The byte contents of this asset.
     * @throws UnsupportedOperationException If this asset does not support
     *                                       getting byte data
     * @throws AuthorizationException        if requester does not have access
     *                                       permission
     * @throws StorageException              if unable to load the Asset
     */
    default InputStream getContentStream() {
        throw new UnsupportedOperationException("Cannot get InputStream for asset of class: " + this.getClass().getCanonicalName());
    }

    /**
     * Gets the representation of this asset as required to pass to a remote
     * invokable
     * service.
     *
     * @return A map representing this asset
     */
    Map<String, Object> getParamValue();

    /**
     * Tests if this asset is an bundle, i.e. can contain sub-assets.
     *
     * @return true if this asset is an bundle, false otherwise
     */
    default boolean isBundle() {
        return Constant.BUNDLE.equals(getMetadata().get(Constant.TYPE));
    }
}
