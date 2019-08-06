package sg.dex.starfish.impl.url;

import sg.dex.crypto.Hash;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.GenericException;
import sg.dex.starfish.exception.StarfishValidationException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.impl.AAsset;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.io.IOException;
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
    private final String resourcePath;

    protected ResourceAsset(String resourceName, String meta, boolean isHashOfContentRequired) {
        super(buildMetaData(resourceName, meta, isHashOfContentRequired));
        this.resourcePath = resourceName;
    }

    /**
     * This method is to crete Resource Asset with specific resource path, metadata  and isHashOfContentRequired
     *
     * @param resourcePath            path of the resource
     * @param meta                    metadata associated with the asset
     * @param isHashOfContentRequired if true then hash of content is calculated and included in metadata.
     *                                Hash of content will be calculated using Keccak256 hash function
     *                                if false then content hash is not included in metadata
     * @return ResourceAsset instance created using given params
     */

    public static ResourceAsset create(String resourcePath, String meta, boolean isHashOfContentRequired) {
        return new ResourceAsset(resourcePath, meta, isHashOfContentRequired);
    }

    /**
     * This method is to crete Resource Asset with specific resource path, metadata  and isHashOfContentRequired
     *
     * @param resourcePath            path of the resource
     * @param isHashOfContentRequired if true then hash of content is calculated and included in metadata.
     *                                Hash of content will be calculated using Keccak256 hash function
     *                                if false then content hash is not included in metadata
     * @return ResourceAsset instance created using given params with default metadata this include DATE_CREATED,TYPE,CONTENT_TYPE
     */
    public static ResourceAsset create(String resourcePath, boolean isHashOfContentRequired) {
        return new ResourceAsset(resourcePath, null, isHashOfContentRequired);
    }

    /**
     * This method is to build the metadata of the Resource Asset
     *
     * @param resourcePath resourcePath
     * @param meta         meta
     * @return String buildMetadata
     */
    private static String buildMetaData(String resourcePath, String meta, boolean isHashOfContentRequired) {


        Map<String, Object> ob = new HashMap<>();
        ob.put(Constant.DATE_CREATED, Instant.now().toString());
        ob.put(Constant.TYPE, Constant.DATA_SET);
        ob.put(Constant.CONTENT_TYPE, "application/octet-stream");


        if (meta != null) {

            for (Map.Entry<String, Object> me : JSON.toMap(meta).entrySet()) {
                ob.put(me.getKey(), me.getValue());
            }
        }

        if (isHashOfContentRequired) {
            ob.put(Constant.CONTENT_HASH, getHashOfContent(resourcePath));
        }
        return JSON.toString(ob);
    }

    private static String getHashOfContent(String resourcePath) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        if (null == inputStream) {
            throw new StarfishValidationException("Given Resource path " + resourcePath + " is not valid");
        }
        return Hex.toString(Hash.keccak256(Utils.stringFromStream(inputStream)));
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
        InputStream istream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        if (istream == null) throw new IllegalStateException("Resource does not exist on classpath: " + resourcePath);
        return istream;
    }

    @Override
    public long getContentSize() {
        try {
            return getContentStream().available();
        } catch (IOException e) {
            throw new GenericException("Exception occurred  for asset id :" + getAssetID() + " while finding getting the Content size :", e);
        }
    }


}
