package sg.dex.starfish.impl.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import sg.dex.starfish.ADataAsset;

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
	
	public static FileAsset create(File f) {
		return new FileAsset("{}",f);
	}

	@Override
	public InputStream getInputStream() {
		try {
			return new FileInputStream(file);
		}
		catch (FileNotFoundException e) {
			throw new IllegalStateException("File not found",e);
		}
	}

	@Override
	public long getSize() {
		return file.length();
	}
	
	public File getFile() {
		return file;
	}



}
