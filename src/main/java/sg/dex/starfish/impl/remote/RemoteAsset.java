package sg.dex.starfish.impl.remote;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import sg.dex.starfish.ADataAsset;
import sg.dex.starfish.Asset;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.util.TODOException;

public class RemoteAsset extends ADataAsset implements DataAsset {

	private final String urlString;
	
	protected RemoteAsset(String meta, String urlString) {
		super(meta);
		this.urlString=urlString;
	}
	
	public static Asset create(String meta, String url) {
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
	
	private String getURLString() {
		return urlString;
	}

	public URL getURL() {
		try {
			return new URL(getURLString());
		}
		catch (MalformedURLException e) {
			throw new Error(e);
		}
	}


}
