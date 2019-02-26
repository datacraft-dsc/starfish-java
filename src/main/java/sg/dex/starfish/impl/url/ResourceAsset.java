package sg.dex.starfish.impl.url;

import java.io.InputStream;

import sg.dex.starfish.ADataAsset;
import sg.dex.starfish.Asset;
import sg.dex.starfish.util.TODOException;

import sg.dex.starfish.util.AuthorizationException;
import sg.dex.starfish.util.StorageException;

/**
 * Class exposing a Java resource referenced by a URL as an Ocean asset
 *
 * @author Mike
 *
 */
public class ResourceAsset extends ADataAsset {
	private final String name;

	protected ResourceAsset(String meta, String name) {
		super(meta);
		this.name=name;
	}

	public static Asset create(String meta, String resourcePath) {
		return new ResourceAsset(meta,resourcePath);
	}

	/**
	 * Gets InputStream corresponding to this Asset
	 *
	 * @throws AuthorizationException if requestor does not have access permission
	 * @throws StorageException if unable to load the Asset
	 * @return An input stream allowing consumption of the asset data
	 */
	@Override
	public InputStream getInputStream() {
		InputStream istream= Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
		if (istream==null) throw new IllegalStateException("Resource does not exist on classpath: "+name);
		return istream;
	}

	@Override
	public long getSize() {
		throw new TODOException();
	}

	public String getName() {
		return name;
	}



}
