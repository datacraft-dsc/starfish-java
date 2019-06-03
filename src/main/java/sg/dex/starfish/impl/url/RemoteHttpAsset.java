package sg.dex.starfish.impl.url;

import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import sg.dex.crypto.Hash;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.GenericException;
import sg.dex.starfish.exception.RemoteException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.impl.AAsset;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.HTTP;
import sg.dex.starfish.util.Hex;
import sg.dex.starfish.util.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * A specialised asset class that references data at a given URI.
 * 
 * It is assumed that asset content can be accessed with a HTTP GET to the given URI.
 *
 * 
 */
public class RemoteHttpAsset extends AAsset implements DataAsset {

	private final URI uri;

	protected RemoteHttpAsset(String meta, URI uri) {
		super(meta);
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

	/**
	 * Creates a HTTP asset using the given URI string.
	 * 
	 * @param uri of the resource
	 * @return A new HTTP asset
	 */
	public static RemoteHttpAsset create( String uri) {
		return new RemoteHttpAsset(buildMetaData(uri, null),  getUri(uri));
	}

    /**
     * Creates a HTTP asset using the given URI string.
     *
     * @param uri of the resource
     * @param meta of the resource
     * @return A new HTTP asset
     */
    public static RemoteHttpAsset create( String uri,Map<String,Object> meta) {
        return new RemoteHttpAsset(buildMetaData(uri, meta),  getUri(uri));
    }

	private static String buildMetaData(String uri, Map<String, Object> meta) {
		String hash = Hex.toString(Hash.keccak256(uri));

		Map<String, Object> ob = new HashMap<>();
		ob.put(Constant.DATE_CREATED, Instant.now().toString());
		ob.put(Constant.CONTENT_HASH, hash);
		ob.put(Constant.TYPE, Constant.DATA_SET);
		ob.put(Constant.CONTENT_TYPE, "application/octet-stream");

		if (meta != null) {
			for (Map.Entry<String, Object> me : meta.entrySet()) {
				ob.put(me.getKey(), me.getValue());
			}
		}

		return JSON.toString(ob);
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
	public DID getAssetDID() {
		throw new UnsupportedOperationException("Can't get DID for asset of type " + this.getClass());
	}

}
