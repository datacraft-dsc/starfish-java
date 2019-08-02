package sg.dex.starfish.impl.file;

import sg.dex.crypto.ComputeHash;
import sg.dex.crypto.ComputeHashFactory;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.impl.AAsset;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
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

    /**
     * Create a FileAsset to read from an existing file
     *
     * @param f file handle one which the File Asset will be created
     * @return FileAsset
     */
    public static FileAsset create(File f) {
        String metaString = buildMetadata(f, null);
        return new FileAsset(metaString, f);
    }

    /**
     * Create a FileAsset to read from an existing file
     *
     * @param f file handle one which the File Asset will be created
     * @return FileAsset
     */
    public static FileAsset create(File f, String metaData) {

        Map<String, Object> metaMap = metaData != null ? JSON.toMap(metaData) : null;

        String metaString = buildMetadata(f, metaMap);
        return new FileAsset(metaString, f);
    }

    /**
     * Create a FileAsset to read from an existing file
     *
     * @param f file handle one which the File Asset will be created
     * @return FileAsset
     */
    public static FileAsset createWithContenthash(File f, ComputeHash computeHash) {
        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put(CONTENT_HASH, getContentAfterHash(f, computeHash));
        String metaString =buildMetadata(f, metaMap);
        return new FileAsset(metaString, f);
    }

    /**
     * Create a FileAsset to read from an existing file
     *
     * @param f file handle one which the File Asset will be created
     * @return FileAsset
     */
    public static FileAsset createWithContenthash(File f, String metaData, ComputeHash computeHash) {

        Map<String, Object> metaMap = metaData != null ? JSON.toMap(metaData) : new HashMap<>();
        metaMap.put(CONTENT_HASH, getContentAfterHash(f, computeHash));
        String metaString = buildMetadata(f, metaMap);
        return new FileAsset(metaString, f);
    }

    private static String getContentAfterHash(File f, ComputeHash computeHash) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            throw new StorageException("File not found ,file : " + f, e);
        }
        if (null == computeHash) {
            computeHash = new ComputeHashFactory().getHashfunction(Keccak256);
        }
        return Hex.toString(computeHash.compute(Utils.stringFromStream(fileInputStream)));
    }

    /**
     * Build default metadata for a file asset
     *
     * @param f The file to use for this file asset
     * @return The default metadata as a String
     */
    protected static String buildMetadata(File f, Map<String, Object> metaMap) {

        Map<String, Object> ob = Utils.createDefaultMetadata();
        ob.put(TYPE, DATA_SET);
        ob.put(SIZE, f.length());
        ob.put(FILE_NAME, f.getName());
        ob.put(CONTENT_TYPE, OCTET_STREAM);

        if (metaMap != null) {
            for (Map.Entry<String, Object> me : metaMap.entrySet()) {
                ob.put(me.getKey(), me.getValue());
            }
        }

        return JSON.toString(ob);

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


}
