package sg.dex.starfish.impl.resource;

import sg.dex.starfish.DataAsset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.GenericException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.impl.AAsset;
import sg.dex.starfish.util.JSON;

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

    protected ResourceAsset(String metaData, String resourcePath) {
        super(metaData);
        this.resourcePath = resourcePath;
    }
    
    /**
     * This method is to create Resource Asset with specific resource path, metadata  and isHashOfContentRequired
     *
     * @param resourcePath path of the resource
     * @param metaData     metadata associated with the asset.This metadata will be be added in addition to default
     *                     metadata i.e DATE_CREATED,TYPE,CONTENT_TYPE.If same key,value is provided then the
     *                     default value will be overridden.
     * @return ResourceAsset instance created using given resource path and metadata
     */

    public static ResourceAsset create(String resourcePath, String metaString) {
        return new ResourceAsset(metaString, resourcePath);
    }

    /**
     * This method is to create Resource Asset with specific resource path, metadata  and isHashOfContentRequired
     *
     * @param resourcePath path of the resource
     * @param metaData     metadata associated with the asset.This metadata will be be added in addition to default
     *                     metadata i.e DATE_CREATED,TYPE,CONTENT_TYPE.If same key,value is provided then the
     *                     default value will be overridden.
     * @return ResourceAsset instance created using given resource path and metadata
     */

    public static ResourceAsset create(String resourcePath, Map <String,Object> metaData) {
        return create(resourcePath,JSON.toPrettyString(buildMetaData( metaData)));
    }

    /**
     * This method is to crete Resource Asset with specific resource path, metadata  and isHashOfContentRequired
     *
     * @param resourcePath path of the resource
     * @return ResourceAsset instance created using given params with default metadata this include DATE_CREATED,TYPE,CONTENT_TYPE
     */
    public static ResourceAsset create(String resourcePath) {
        return create(resourcePath,(Map<String,Object>)null);
    }

    /**
     * This method is to build the metadata of the Resource Asset
     *
     * @param metaData     metadata associated with the asset.This metadata will be be added in addition to default
     *                     metadata i.e DATE_CREATED,TYPE,CONTENT_TYPE.If same key,value is provided then the
     *                     default value will be overridden.
     * @return String buildMetadata
     */

    private static Map<String,Object> buildMetaData( Map<String,Object> metaData) {


        Map<String, Object> ob = new HashMap<>();
        ob.put(Constant.DATE_CREATED, Instant.now().toString());
        ob.put(Constant.TYPE, Constant.DATA_SET);
        ob.put(Constant.CONTENT_TYPE, "application/octet-stream");


        if (metaData != null) {

            for (Map.Entry<String, Object> me : metaData.entrySet()) {
                ob.put(me.getKey(), me.getValue());
            }
        }

        return ob;
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
    public String getSource() {
        return resourcePath;
    }

    @Override
    public DataAsset updateMeta(String newMeta) {
        return create(this.getSource(),newMeta);
    }

}
