package sg.dex.starfish.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HTTP {

	/**
	 * Creates a HTTP entity with the specified String as UTF_8 bytes
	 * @param s String of UTF-8 bytes to create the entity
	 * @return HttpEntity
	 */
	public static HttpEntity textEntity(String s) {
		return new ByteArrayEntity(s.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Executes an HTTP request
	 *
	 * @param httpRequest the HttpUriRequest to execute
	 * @throws RuntimeException for protocol errors
	 * @return CloseableHttpResponse
	 */
	public static CloseableHttpResponse execute(HttpUriRequest httpRequest) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(httpRequest);
			return response;
		}
		catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Closes an HTTP response
	 *
	 * @param response A valid successful response from the remote Invoke API
	 * @throws RuntimeException for protocol errors
	 */
	public static void close(CloseableHttpResponse response) {
		try {
			response.close();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static HttpEntity getEntity(HttpResponse response) {
		return response.getEntity();
	}

	/**
	 * Gets HTTP response content
	 *
	 * @param response A valid successful response from the remote Invoke API
	 * @throws RuntimeException for protocol errors
	 * @return InputStream for the content
	 */
	public static InputStream getContent(HttpResponse response) {
		try {
			return getEntity(response).getContent();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
