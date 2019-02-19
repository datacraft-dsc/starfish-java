package sg.dex.starfish.impl.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import sg.dex.starfish.ADataAsset;
import sg.dex.starfish.Agent;

public class FileAsset extends ADataAsset {
	private final File file;
	
	protected FileAsset(Agent agent, String meta, File file) {
		super(agent, meta);
		this.file=file;
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
