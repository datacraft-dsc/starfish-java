package sg.dex.starfish.impl.file;

import sg.dex.starfish.DataAsset;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StarfishValidationException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.impl.AAsset;
import sg.dex.starfish.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import static sg.dex.starfish.constant.Constant.*;

/**
 * Class exposing a file on the local file system as an Ocean asset
 *
 * @author Mike
 */
public class FileAsset extends AAsset implements DataAsset {
    private final File file;

    protected FileAsset(String meta, File file) {
        super(meta);
        this.file = file;
    }

    protected FileAsset(Map<String,Object> meta, File file) {
        super(meta);
        this.file = file;
    }
    /**
     * This method is to crete File Asset with specific given path.
     *
     * @param f path of the file
     * @return FileAsset instance created using given params
     */
    public static FileAsset create(File f) {
        if(f== null || !f.exists()){
            throw new StarfishValidationException("Give file :"+f +" is null or may not exist");
        }
        return new FileAsset(buildMetadata( null), f);
    }

    /**
     * This method is to crete Resource Asset with specific given path, metadata
     *
     * @param f        path of the file
     * @param metaData metadata associated with the asset.This metadata will be be added in addition to default
     *                 metadata i.e DATE_CREATED,TYPE,CONTENT_TYPE.If same key,value is provided then the
     *                 default value will be overridden.
     * @return FileAsset instance created using given params
     */
    public static FileAsset create(File f, Map <String,Object> metaData) {
        if(f== null || !f.exists()){
            throw new StarfishValidationException("Give file is null or may not exist");
        }
        return new FileAsset(buildMetadata( metaData), f);
    }


    /**
     * Build default metadata for a file asset
     *
     * @param metaMap metadata associated with the asset.This metadata will be be added in addition to default
     *             metadata i.e DATE_CREATED,TYPE,CONTENT_TYPE.If same key,value is provided then the
     *             default value will be overridden.
     * @return The default metadata as a String
     */
    protected static Map<String,Object> buildMetadata(Map<String,Object> metaMap) {

        Map<String, Object> ob = Utils.createDefaultMetadata();
        ob.put(TYPE, DATA_SET);
        ob.put(CONTENT_TYPE, OCTET_STREAM);

        if (metaMap != null) {
            for (Map.Entry<String, Object> me : metaMap.entrySet()) {
                ob.put(me.getKey(), me.getValue());
            }
        }

        return ob;

    }

    /**
     * Gets an input stream that can be used to consume the content of this asset.
     * <p>
     * Will throw an exception if consumption of the asset data in not possible
     * locally.
     *
     * @return An input stream allowing consumption of the asset data
     * @throws AuthorizationException if requestor does not have access permission
     * @throws StorageException       if unable to load the Asset
     */
    @Override
    public InputStream getContentStream() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new StorageException("File not found ,file : " + file, e);
        }
    }

    @Override
    public long getContentSize() {
        return null != file ? file.length() : -1;
    }
    public File getSource() {
        return file;
    }

    @Override
    public DataAsset updateMeta(Map<String, Object> newMeta) {
        return create(this.getSource(),newMeta);
    }
}
