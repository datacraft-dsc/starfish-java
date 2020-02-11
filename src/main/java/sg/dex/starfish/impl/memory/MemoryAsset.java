/**
 * Represents an Asset stored in Memory.
 */
package sg.dex.starfish.impl.memory;

import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static sg.dex.starfish.constant.Constant.*;

/**
 * Class representing a local in-memory data asset.
 * <p>
 * Intended for use in testing or local development situations.
 *
 * @author Mike
 */
public class MemoryAsset extends AMemoryAsset implements DataAsset {

    private final byte[] data;


    private MemoryAsset(byte[] data, String metaData, MemoryAgent memoryAgent) {
        super(metaData, memoryAgent);
        this.data = data;
    }

    /**
     * Builds default metadata for a MemoryAsset.
     *
     * @param data
     * @param additionalMeta
     * @return
     */
    private static Map<String, Object> buildMetaData(byte[] data, Map<String, Object> additionalMeta) {
        Map<String, Object> meta = new HashMap<>();
        meta.put(DATE_CREATED, Instant.now().toString());
        meta.put(TYPE, DATA_SET);
        meta.put(SIZE, Integer.toString(data.length));
        meta.put(CONTENT_TYPE, OCTET_STREAM);

        if (additionalMeta != null) {
            for (Map.Entry<String, Object> me : additionalMeta.entrySet()) {
                meta.put(me.getKey(), me.getValue());
            }
        }

        return meta;
    }

    /**
     * Gets a MemoryAsset using the content and metadata from the provided asset
     *
     * @param asset The asset to use to construct this MemoryAsset
     * @return A new MemoryAsset containing the data from the passed asset argument
     */
    public static MemoryAsset create(Asset asset) {
        if (asset instanceof MemoryAsset) {
            return (MemoryAsset) asset;
        } else if (asset.isDataAsset()) {
            byte[] data = asset.getContent();
            return create(data, buildMetaData(data, null));
        } else {
            throw new IllegalArgumentException("Asset must be a data asset");
        }
    }

    /**
     * Gets a MemoryAsset using the content and metadata from the provided asset
     *
     * @param asset       The asset to use to construct this MemoryAsset
     * @param memoryAgent memoryAgent
     * @return A new MemoryAsset containing the data from the passed asset argument
     */
    public static MemoryAsset create(Asset asset, MemoryAgent memoryAgent) {
        if (asset instanceof MemoryAsset) {
            return (MemoryAsset) asset;
        } else if (asset.isDataAsset()) {
            byte[] data = asset.getContent();
            return create(data, buildMetaData(data, null), memoryAgent);
        } else {
            throw new IllegalArgumentException("Asset must be a data asset");
        }
    }

    /**
     * Creates a MemoryAsset with the provided data. Default metadata will be
     * generated.
     *
     * @param data Byte array containing the data for this asset
     * @return The newly created in-memory asset
     */
    public static MemoryAsset create(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException("Missing data,data cannot be null");
        }
        return create(data, buildMetaData(data, null), MemoryAgent.create());
    }

    /**
     * Creates a MemoryAsset with the provided data. Default metadata will be
     * generated.
     *
     * @param data        Byte array containing the data for this asset
     * @param memoryAgent memoryAgent
     * @return The newly created in-memory asset
     */
    public static MemoryAsset create(byte[] data, MemoryAgent memoryAgent) {
        if (data == null) {
            throw new IllegalArgumentException("Missing data,data cannot be null");
        }
        return create(data, buildMetaData(data, null), memoryAgent);
    }

    /**
     * Creates a MemoryAsset with the provided string data, encoded in UTF_8 Default
     * metadata will be generated.
     *
     * @param string String containing the data for this asset
     * @return The newly created in-memory asset
     */
    public static Asset createFromString(String string) {
        byte[] data = string.getBytes(StandardCharsets.UTF_8);

        // set up default content type
        Map<String, Object> meta = Utils.mapOf(Constant.CONTENT_TYPE, "text/plain");

        return create(data, meta);
    }

    /**
     * Creates a MemoryAsset with given byte[] content.
     * <p>
     * Creates default metadata, and merges in and additional metadata provided
     *
     * @param additionalMeta A map containing additional metadata for this asset
     * @param data           Byte array containing the data for this asset
     * @return The newly created in-memory asset
     */
    public static MemoryAsset create(byte[] data, Map<String, Object> additionalMeta) {
        return create(data, additionalMeta, MemoryAgent.create());
    }

    /**
     * Creates a MemoryAsset with given byte[] content, using the specified MemoryAgent
     * <p>
     * Creates default metadata, and merges in and additional metadata provided
     *
     * @param additionalMeta A map containing the metadata for this asset
     * @param data           Byte array containing the data for this asset
     * @param memoryAgent    memoryAgent
     * @return The newly created in-memory asset
     */
    public static MemoryAsset create(byte[] data, Map<String, Object> additionalMeta, MemoryAgent memoryAgent) {
        Map<String, Object> meta = buildMetaData(data, additionalMeta);
        if (!meta.containsKey(Constant.CONTENT_HASH)) {
            String hash = Hash.sha3_256String(data);
            meta.put(Constant.CONTENT_HASH, hash);
        }
        return new MemoryAsset(data, JSON.toPrettyString(meta), memoryAgent);
    }

    /**
     * Creates a MemoryAsset with the provided metadata and content
     *
     * @param metaString The metadata to use for this asset
     * @param data       Byte array containing the data for this asset
     * @return The newly created in-memory asset
     */
    public static MemoryAsset create(byte[] data, String metaString) {
        return create(data, JSON.toMap(metaString), MemoryAgent.create());
    }


    /**
     * Gets InputStream corresponding to this Asset
     *
     * @return An input stream allowing consumption of the asset data
     * @throws AuthorizationException if requestor does not have access permission
     * @throws StorageException       if unable to load the Asset
     */
    @Override
    public InputStream getContentStream() {
        if (data == null) throw new Error("MemoryAsset has not been initialised with data");
        return new ByteArrayInputStream(data);
    }

    /**
     * Gets raw data corresponding to this Asset
     *
     * @return An input stream allowing consumption of the asset data
     * @throws AuthorizationException if requestor does not have access permission
     * @throws StorageException       if unable to load the Asset
     */
    @Override
    public byte[] getContent() {
        // we take a copy of data to protected immutability of MemoryAsset instance
        return null != data ? data.clone() : null;
    }


    @Override
    public Map<String, Object> getParamValue() {
        Map<String, Object> o = new HashMap<>();
        // pass the asset ID, i.e. hash of content
        o.put(ID, getAssetID());
        return o;
    }

    @Override
    public long getContentSize() {
        return this.data != null ? this.data.length : -1;
    }


    public byte[] getSource() {
        return data;
    }

    @Override
    public DataAsset updateMeta(String newMeta) {
        return new MemoryAsset(this.getSource(), newMeta, MemoryAgent.create());
    }
}
