package sg.dex.starfish.impl.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import sg.dex.starfish.Asset;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.exception.TODOException;
import sg.dex.starfish.impl.ADataAsset;
import sg.dex.starfish.util.JSON;

import static sg.dex.starfish.constant.Constant.*;

/**
 * Class exposing a file on the local file system as an Ocean asset
 *
 * @author Mike
 *
 */
public class FileAsset extends ADataAsset {
	private final File file;

	protected FileAsset(String meta, File file) {
		super(meta);
		this.file=file;
	}

	/**
	 * Create a FileAsset to read from an existing file
	 * @param f File from which to read the Asset
	 * @return FileAsset
	 */
	public static FileAsset create(File f) {
		return new FileAsset(buildMetadata(f,null),f);
	}

	/**
	 * Create a new FileAsset at the given file location, using the specified asset as a source.
	 * @param f File from which to read the Asset
	 * @param source Asset
	 * @return FileAsset
	 */
	public static FileAsset create(File f, Asset source) {
		throw new TODOException("Create file asset on disk");
	}

	/**
	 * Build default metadata for a file asset
	 * @param f The file to use for this file asset
	 * @return The default metadata as a String
	 */
	private static String buildMetadata(File f,Map<String,Object> meta) {

		Map<String,Object> ob=new HashMap<>();
		ob.put(DATE_CREATED, Instant.now().toString());
		ob.put(TYPE, "dataset");
		ob.put(SIZE, f.length());
		ob.put(FILE_NAME, f.getName());
		ob.put(CONTENT_TYPE,"application/octet-stream");

		if (meta!=null) {
			for (Map.Entry<String,Object> me:meta.entrySet()) {
				ob.put(me.getKey(), me.getValue());
			}
		}

		return JSON.toString(ob);
	}


	/**
	 * Gets an input stream that can be used to consume the content of this asset.
	 *
	 * Will throw an exception if consumption of the asset data in not possible locally.
	 * @throws AuthorizationException if requestor does not have access permission
	 * @throws StorageException if unable to load the Asset
	 * @return An input stream allowing consumption of the asset data
	 */
	@Override
	public InputStream getInputStream() {
		try {
			return new FileInputStream(file);
		}
		catch (FileNotFoundException e) {
			throw new StorageException("File not found",e);
		}
	}

	@Override
	public long getContentSize() {
		return file.length();
	}

	/**
	 * Gets an input stream that can be used to consume the content of this asset.
	 *
	 * Will throw an exception if consumption of the asset data in not possible locally.
	 * @throws AuthorizationException if requestor does not have access permission
	 * @throws StorageException if unable to load the Asset
	 * @return An input stream allowing consumption of the asset data
	 */
	public File getFile() {
		return file;
	}



}
