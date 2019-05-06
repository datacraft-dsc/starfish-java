
package sg.dex.starfish.impl.file;

import static sg.dex.starfish.constant.Constant.CONTENT_TYPE;
import static sg.dex.starfish.constant.Constant.DATA_SET;
import static sg.dex.starfish.constant.Constant.FILE_NAME;
import static sg.dex.starfish.constant.Constant.SIZE;
import static sg.dex.starfish.constant.Constant.TYPE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import sg.dex.starfish.Asset;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.exception.TODOException;
import sg.dex.starfish.impl.AAsset;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

/**
 * Class exposing a file on the local file system as an Ocean asset
 *
 * @author Mike
 *
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
	 * @param f
	 * @return FileAsset
	 */
	public static FileAsset create(File f) {
		String metaString = JSON.toString(buildMetadata(f));
		return new FileAsset(metaString, f);
	}

	/**
	 * Create a new FileAsset at the given file location, using the specified asset
	 * as a source.
	 * 
	 * @param f
	 * @param source
	 * @return FileAsset
	 */
	public static FileAsset create(File f, Asset source) {
		throw new TODOException("Create file asset on disk");
	}

	/**
	 * Build default metadata for a file asset
	 * 
	 * @param f The file to use for this file asset
	 * @return The default metadata as a String
	 */
	protected static Map<String, Object> buildMetadata(File f) {

		Map<String, Object> ob = Utils.createDefaultMetadata();
		ob.put(TYPE, DATA_SET);
		ob.put(SIZE, f.length());
		ob.put(FILE_NAME, f.getName());
		ob.put(CONTENT_TYPE, "application/octet-stream");

		return ob;
	}

	/**
	 * Gets an input stream that can be used to consume the content of this asset.
	 *
	 * Will throw an exception if consumption of the asset data in not possible
	 * locally.
	 * 
	 * @throws AuthorizationException if requestor does not have access permission
	 * @throws StorageException if unable to load the Asset
	 * @return An input stream allowing consumption of the asset data
	 */
	@Override
	public InputStream getContentStream() {
		try {
			return new FileInputStream(file);
		}
		catch (FileNotFoundException e) {
			throw new StorageException("File not found", e);
		}
	}

	@Override
	public long getContentSize() {
		return file.length();
	}

	/**
	 * Gets an input stream that can be used to consume the content of this asset.
	 *
	 * Will throw an exception if consumption of the asset data in not possible
	 * locally.
	 * 
	 * @throws AuthorizationException if requestor does not have access permission
	 * @throws StorageException if unable to load the Asset
	 * @return An input stream allowing consumption of the asset data
	 */
	public File getFile() {
		return file;
	}

}
