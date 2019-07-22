package sg.dex.starfish.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import sg.dex.starfish.exception.RemoteException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * This is an HTTP class for executing REST calls.
 * <p>
 * The method of this class will be used to create the the HTTP instance,
 * execute the HTTP request, get the content of the HTTP response.
 * </p>
 *
 */
public class HTTP {

	/**
	 * Creates a HTTP entity with the specified String as UTF_8 bytes
	 *
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
	 * @return CloseableHttpResponse
	 * @throws RemoteException if there is an problem executing the task on remote
	 *             Server.
	 */
	public static CloseableHttpResponse execute(HttpUriRequest httpRequest) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(httpRequest);
			return response;
		}
		catch (ClientProtocolException e) {
			throw new RemoteException("ClientProtocolException executing HTTP request ," + e.getMessage(), e);
		}
		catch (IOException e) {
			throw new RemoteException("IOException executing HTTP request ," + e.getMessage(), e);
		}
	}

	/**
	 * Execute the HTTP request based on user name ,password, and the request
	 * data.
	 * 
	 * @param httpRequest will have request data
	 * @param user user name
	 * @param password password of the user
	 * @return An HTTP Response object
	 */
	public static CloseableHttpResponse executeWithAuth(HttpUriRequest httpRequest, String user, String password) {
		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials cred = new UsernamePasswordCredentials(user, password);
		provider.setCredentials(org.apache.http.auth.AuthScope.ANY, (Credentials) cred);

		CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(httpRequest);
			return response;
		}
		catch (ClientProtocolException e) {
			throw new RemoteException("ClientProtocolException executing HTTP request ," + e.getMessage(), e);
		}
		catch (IOException e) {
			throw new RemoteException("IOException executing HTTP request ," + e.getMessage(), e);
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

	/**
	 * Gets HTTP response content
	 *
	 * @param response A valid successful response from the remote API
	 * @return InputStream for the content
	 * @throws RuntimeException for protocol errors
	 */
	public static InputStream getContent(HttpResponse response) {
		try {
			return  new ByteArrayInputStream(EntityUtils.toByteArray(response.getEntity()));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Tests is a server is listening on the given port
	 * @param host hostname
	 * @param port name
	 * @return True if the server is listening, false otherwise
	 */
	public static boolean testServerListening(String host, int port) {
		Socket s = null;
		try {
			s = new Socket(host, port);
			return true;
		}
		catch (Exception e) {
			return false;
		} finally {
			if (s!=null) try {
				s.close();
			} catch (IOException e) {
				return false;
			}
		}
	}

	/**
	 * This method is used to crate a Multipart instance with the given content.
	 * 
	 * @param partName name
	 * @param body body
	 * @return A multipart HttpEntity with the given  content
	 */
	public static HttpEntity createMultiPart(String partName, ContentBody body) {
		return MultipartEntityBuilder
				.create()
				// to upload a file
				// .addBinaryBody("file", f, ContentType.create("application/octet-stream"),
				// f.getName())
				.addPart(partName, body)
				.build();
	}
}
