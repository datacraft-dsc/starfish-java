package sg.dex.starfish.impl.url;

import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.GenericException;
import sg.dex.starfish.exception.RemoteException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.impl.remote.ARemoteAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.HTTP;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is for reading the data from a given URL
 */
public class RemoteHttpAsset extends ARemoteAsset implements DataAsset {

	private final URI uri;

	protected RemoteHttpAsset(String meta, RemoteAgent remoteAgent, URI uri) {
		super(meta, remoteAgent);
		this.uri = uri;
	}

	private static URI getUri(String urlString) {
		URI uri;
		try {
			uri = new URI(urlString);
		}
		catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
		return uri;
	}

	public static RemoteHttpAsset create(String meta, RemoteAgent remoteAgent, String uri) {
		return new RemoteHttpAsset(meta, remoteAgent, getUri(uri));
	}

	/**
	 * Gets raw data corresponding to this Asset
	 *
	 * @return An input stream allowing consumption of the asset data
	 * @throws AuthorizationException if requestor does not have access permission
	 * @throws StorageException if unable to load the Asset
	 */
	@Override
	public InputStream getContentStream() {
		HttpGet httpget = new HttpGet(uri);
		CloseableHttpResponse response = null;
		try {
			response = HTTP.execute(httpget);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 404) {
				throw new RemoteException("Asset ID not found at: " + uri);
			}
			if (statusCode == 200) {
				InputStream inputStream = HTTP.getContent(response);
				return inputStream;
			} else {
				throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
			}
		}
		catch (IOException e) {
			throw new RemoteException(" Getting Remote Asset content failed: ", e);
		}

	}

	/**
	 * Gets RemoteAsset size
	 *
	 * @return size of the RemoteAsset
	 */
	@Override
	public long getContentSize() {
		try {
			return getContentStream().available();
		}
		catch (IOException e) {
			throw new GenericException(
					"Exception occurred  for asset id :" + getAssetID() + " while finding getting the Content size :",
					e);
		}
	}

	@Override
	public Map<String, Object> getParamValue() {
		Map<String, Object> o = new HashMap<>();
		// pass the asset ID, i.e. hash of content
		o.put(Constant.DID, getAssetID());
		return o;
	}

	@Override
	public DID getAssetDID() {
		throw new UnsupportedOperationException("Can't get DID for asset of type " + this.getClass());
	}

}
