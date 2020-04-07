package sg.dex.starfish.impl.resource;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import sg.dex.starfish.DataAsset;
import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.exception.AuthorizationException;
import sg.dex.starfish.exception.StorageException;
import sg.dex.starfish.impl.AAsset;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.HTTP;
import sg.dex.starfish.util.JSON;

import java.io.InputStream;
import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static sg.dex.starfish.constant.Constant.CONTENT_LENGTH;
import static sg.dex.starfish.constant.Constant.CONTENT_TYPE;

/**
 * A specialised asset class that references data at a given URI.
 * <p>
 * It is assumed that asset content can be accessed with a HTTP GET to the given URI.
 */
public class URIAsset extends AAsset implements DataAsset {

    private final URI uri;
    private final CloseableHttpResponse response;

    protected URIAsset(URI uri, CloseableHttpResponse response, String meta) {
        super(meta);
        this.uri = uri;
        this.response = response;

    }

    /**
     * Creates a HTTP asset using the given URI and metadata
     *
     * @param uri of the resource
     * @return RemoteHttpAsset instance created using given params with default metadata this include DATE_CREATED,TYPE,CONTENT_TYPE
     */
    public static URIAsset create(URI uri, String metaString) {
        return new URIAsset(uri, getResponse(uri, null), metaString);
    }

    /**
     * Creates a HTTP asset using the given URI.
     *
     * @param uri of the resource
     * @return RemoteHttpAsset instance created using given params with default metadata this include DATE_CREATED,TYPE,CONTENT_TYPE
     */
    public static URIAsset create(URI uri) {
        return create(uri, JSON.toPrettyString(buildMetaData(uri, null, null)));
    }

    /**
     * Creates a HTTP asset using the given URI.
     *
     * @param uri of the resource
     * @return RemoteHttpAsset instance created using given params with default metadata this include DATE_CREATED,TYPE,CONTENT_TYPE
     */
    public static URIAsset create(URI uri, Map<String, String> auth, Map<String, Object> metaData) {
        return create(uri, JSON.toPrettyString(buildMetaData(uri, auth, metaData)));
    }

    /**
     * Creates a HTTP asset using the given URI string.
     *
     * @param uri      of the resource
     * @param metaData metadata associated with the asset.This metadata will be be added in addition to default
     *                 metadata i.e DATE_CREATED,TYPE,CONTENT_TYPE.If same key,value is provided then the
     *                 default value will be overridden.
     * @return RemoteHttpAsset instance created using given params with given metadata.
     */
    public static URIAsset create(URI uri, Map<String, Object> metaData) {
        return new URIAsset(uri, getResponse(uri, null), JSON.toPrettyString(buildMetaData(uri, null, metaData)));
    }

    /**
     * This method is to build the metadata of the Resource Asset
     *
     * @param metaData metadata associated with the asset.This metadata will be be added in addition to default
     *                 metadata i.e DATE_CREATED,TYPE,CONTENT_TYPE.If same key,value is provided then the
     *                 default value will be overridden.
     * @return String buildMetadata
     */
    private static Map<String, Object> buildMetaData(URI uri, Map<String, String> auth, Map<String, Object> metaData) {

        Map<String, Object> ob = new HashMap<>();
        ob.put(Constant.DATE_CREATED, Instant.now().toString());
        ob.put(Constant.TYPE, Constant.DATA_SET);
        CloseableHttpResponse response = getResponse(uri, auth);
        Header[] headers = response.getAllHeaders();

        ob.put(CONTENT_TYPE, getHeaderValue(CONTENT_TYPE, headers));
        ob.put(CONTENT_LENGTH, getHeaderValue(CONTENT_LENGTH, headers));

        if (metaData != null) {

            for (Map.Entry<String, Object> me : metaData.entrySet()) {
                ob.put(me.getKey(), me.getValue());
            }
        }
        return ob;
    }

    private static CloseableHttpResponse getResponse(URI uri, Map<String, String> header) {
        HttpGet httpget = new HttpGet(uri);
        if (header != null) {
            addHeader(httpget, header);
        }
        return HTTP.execute(httpget);

    }

    private static void addHeader(HttpGet httpget, Map<String, String> header) {
        for (Map.Entry<String, String> entry : header.entrySet()) {
            httpget.addHeader(entry.getKey(), entry.getValue());
        }

    }

    private static String getHeaderValue(String headerName, Header[] headers) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].getName().equalsIgnoreCase(headerName)) {
                return headers[i].getValue();
            }
        }
        return "";
    }


    /**
     * Gets raw data corresponding to this Asset
     *
     * @return An input stream allowing consumption of the asset data
     * @throws AuthorizationException if requestor does not have access permission
     * @throws StorageException       if unable to load the Asset
     */
    @Override
    public InputStream getContentStream() {
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (statusCode == 200) {
            InputStream inputStream = HTTP.getContent(response);
            return inputStream;
        }
        return null;

    }

    /**
     * Gets RemoteAsset size
     *
     * @return size of the RemoteAsset
     */
    @Override
    public long getContentSize() {
        return (Long) this.getMetadata().get(CONTENT_LENGTH);
    }

    @Override
    public DID getDID() {
        throw new UnsupportedOperationException("Can't get DID for asset of type " + this.getClass());
    }

    @Override
    public DataAsset updateMeta(String newMeta) {
        return create(uri, newMeta);
    }

}
