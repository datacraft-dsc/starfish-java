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
    private final String resourceName;

    protected ResourceAsset(String meta, String resourceName) {

        super(meta);
        this.resourceName = resourceName;
    }

    /**
     * This method is to crete resource name wth meta data and the resource path
     *
     * @param meta metadata
     * @param resourcePath path of the resource
     * @return ResourceAsset
     */
    public static ResourceAsset create(String meta, String resourcePath) {
        return new ResourceAsset(buildMetaData(resourcePath,JSON.toMap(meta)), resourcePath);
    }

    /**
     * This method is to crete a Resource Asset with resource Name
     *
     * @param resourceName Resource name
     * @return ResourceAsset
     */
    public static ResourceAsset create(String resourceName) {
        return create(buildMetaData(resourceName, null), resourceName);
    }

    /**
     * This method is to build the metadata of the Resource Asset
     *
     * @param resourcePath resourcePath
     * @param meta meta
     * @return String buildMetadata
     */
    private static String buildMetaData(String resourcePath, Map<String, Object> meta) {

        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        if(null ==inputStream){
            throw new StarfishValidationException("Given Resource path " +resourcePath +" is not valid");
        }
        String hash = Hex.toString(Hash.keccak256(Utils.stringFromStream(inputStream)));

        Map<String, Object> ob = new HashMap<>();
        ob.put(Constant.NAME, resourcePath);
        ob.put(Constant.DATE_CREATED, Instant.now().toString());
        ob.put(Constant.CONTENT_HASH, hash);
        ob.put(Constant.TYPE, Constant.DATA_SET);
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
        try {
            return getContentStream().available();
        } catch (IOException e) {
            throw  new GenericException("Exception occurred  for asset id :"+getAssetID()+" while finding getting the Content size :",e);
        }
    }


}
