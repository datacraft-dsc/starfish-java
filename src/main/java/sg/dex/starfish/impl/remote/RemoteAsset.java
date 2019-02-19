package sg.dex.starfish.impl.remote;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import sg.dex.starfish.ADataAsset;
import sg.dex.starfish.Asset;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.util.TODOException;

/**
 * Class representing a data asset referenced by a URL.
 * 
 * @author Mike
 *
 */
public class RemoteAsset extends ADataAsset implements DataAsset {

	private final URL url;
	
	protected RemoteAsset(String meta, URL url) {
		super(meta);
		this.url=url;
	}
	
	public static Asset create(String meta, String urlString) {
		try {
			return new RemoteAsset(meta,new URL(urlString));
		}
		catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static Asset create(String meta, URL url) {
		return new RemoteAsset(meta,url);
	}

	@Override
	public boolean isDataAsset() {
		return true;
	}

	@Override
	public InputStream getInputStream() {
		URL url=getURL();
		try {
			return url.openStream();
		}
		catch (IOException e) {
			throw new Error("Cannot open input stream for URL: "+url,e);
		}
	}

	@Override
	public long getSize() {
		throw new TODOException();
	}
	
	public String getURLString() {
		return url.toString();
	}

	public URL getURL() {
		return url;
	}


}
