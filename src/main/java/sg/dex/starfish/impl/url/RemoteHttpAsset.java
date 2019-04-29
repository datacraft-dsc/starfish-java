package sg.dex.starfish.impl.url;

import static sg.dex.starfish.constant.Constant.DATE_CREATED;
import static sg.dex.starfish.constant.Constant.TYPE;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;

import sg.dex.starfish.Asset;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.RemoteException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.exception.TODOException;
import sg.dex.starfish.impl.AAsset;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.HTTP;
import sg.dex.starfish.util.JSON;

/**
 * This class is for reading the data from a given URL
 */
public class RemoteHttpAsset extends AAsset implements DataAsset {

    private final URI uri;

    protected RemoteHttpAsset(String meta, URI uri) {
        super(meta);
        this.uri=uri;
    }
    
    private static String buildMetadata(URI uri) {
        Map<String, Object> ob = new HashMap<>();
        ob.put(DATE_CREATED, Instant.now().toString());
        ob.put(TYPE, Constant.BUNDLE);

        return JSON.toPrettyString(ob);	
    }
    
	public static Asset create(String urlString) {
		URI uri;
		try {
			uri = new URI(urlString);
		}
		catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
		return create(buildMetadata(uri),uri);
	}

    public static RemoteHttpAsset create(String meta, URI uri) {
        return new RemoteHttpAsset(meta,uri);
    }

    /**
     * Gets raw data corresponding to this Asset
     *
     * @throws AuthorizationException if requestor does not have access permission
     * @throws StorageException if unable to load the Asset
     * @return An input stream allowing consumption of the asset data
     */
    @Override
    public InputStream getContentStream() {
        HttpGet httpget = new HttpGet(uri);
        HttpResponse response = HTTP.execute(httpget);
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (statusCode == 404) {
            throw new RemoteException("Asset ID not found at: " + uri);
        }
        if (statusCode == 200) {
            return HTTP.getContent(response);
        }
        throw new TODOException("status code not handled: " + statusCode);
    }

    /**
     * Gets RemoteAsset size
     *
     * @throws TODOException not implemented yet
     * @return size of the RemoteAsset
     */
    @Override
    public long getContentSize() {
        throw new TODOException();
    }

    @Override
    public Map<String,Object> getParamValue() {
        Map<String,Object> o=new HashMap<>();
        // pass the asset ID, i.e. hash of content
        o.put("did", getAssetID());
        return o;
    }

    @Override
    public DID getAssetDID() {
        throw new UnsupportedOperationException("Can't get DID for asset of type "+this.getClass());
    }


    
 
}
