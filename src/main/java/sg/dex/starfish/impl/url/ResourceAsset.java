package sg.dex.starfish.impl.url;

import sg.dex.crypto.Hash;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.exception.TODOException;
import sg.dex.starfish.impl.AAsset;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.JSON;

import java.io.InputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Class exposing a Java classpath resource as an Ocean asset.
 * <p>
 * Mainly useful for testing, or for applications that wish to embed some default assets such as
 * scripts as fixed resources.
 *
 * @author Mike
 */
public class ResourceAsset extends AAsset implements DataAsset {
    private final String resourceName;

    protected ResourceAsset(String meta, String resourceName) {
        super(meta);
        this.resourceName = resourceName;
    }

    /**
     * API to crete resurce name wth meta data and the resource path
     *
     * @param meta
     * @param resourcePath
     * @return ResourceAsset
     */
    public static ResourceAsset create(String meta, String resourcePath) {
        return new ResourceAsset(meta, resourcePath);
    }

    /**
     * API to crete a Resource Asset with resource Name
     *
     * @param resourceName
     * @return ResourceAsset
     */
    public static ResourceAsset create(String resourceName) {
        return create(buildMetaData(resourceName, null), resourceName);
    }

    /**
     * API to build the metadata of the Resource Asset
     *
     * @param resourcePath
     * @param meta
     * @return String buildMetadata
     */
    private static String buildMetaData(String resourcePath, Map<String, Object> meta) {
        String hash = Hex.toString(Hash.keccak256(resourcePath));

        Map<String, Object> ob = new HashMap<>();
        ob.put(Constant.NAME, resourcePath);
        ob.put(Constant.DATE_CREATED, Instant.now().toString());
        ob.put(Constant.CONTENT_HASH, hash);
        ob.put(Constant.TYPE, Constant.DATASET);
        ob.put(Constant.CONTENT_TYPE, "application/octet-stream");

        if (meta != null) {
            for (Map.Entry<String, Object> me : meta.entrySet()) {
                ob.put(me.getKey(), me.getValue());
            }
        }

        return JSON.toString(ob);
    }

    /**
     * Gets InputStream corresponding to this Asset
     *
     * @return An input stream allowing consumption of the asset data
     * @throws AuthorizationException if requester does not have access permission
     * @throws StorageException       if unable to load the Asset
     */
    @Override
	public InputStream getContentStream() {
        InputStream istream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
        if (istream == null) throw new IllegalStateException("Resource does not exist on classpath: " + resourceName);
        return istream;
    }

    @Override
    public long getContentSize() {
        throw new TODOException();
    }

    /**
     * API to get the name of the resource
     *
     * @return String name
     */
    public String getName() {
        return resourceName;
    }


}
