package com.oceanprotocol;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.oceanprotocol.client.SessionController;
import com.oceanprotocol.model.Actor;
import com.oceanprotocol.model.Asset;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SessionController.class)
public class SessionControllerTest {
	/**
	 * create a mock object
	 */
	@InjectMocks
	SessionController controller;
	/**
	 * a mock object of the {@link PostMethod}. While executing a method this mock
	 * object will replace if any post method object used in that method
	 */
	@Mock
	private PostMethod mockPostMethod;
	/**
	 * a mock object of the {@link HttpClient}. While executing a method this mock
	 * object will replace if any post method object used in that method
	 */
	@Mock
	private HttpClient mockHttpClient;
	/**
	 * a mock object of the {@link GetMethod}. While executing a method this mock
	 * object will replace if any post method object used in that method
	 */
	@Mock
	private GetMethod mockGetMethod;

	/**
	 * Before executing a test method this method will be activated.
	 * it instantiate all the mock objects in given above to use.
	 * @throws IOException
	 *
	 */
	@Before
	public void before() throws IOException {

		MockitoAnnotations.initMocks(this);

	}

	/**
	 * A test method to test the Actor registration in the session class first
	 * creates a sample url and result to return.Then mocks httpClient and post
	 * method for testing purpose
	 * 
	 * @throws Exception
	 *
	 */
	@Test
	public void actorRegistrationTest() throws Exception {
		// sample test URL
		URL url = new URL("http://localhost:8000/api/v1/keeper/assets/metadata");
		// sample data to return
		String result = "[{\"state\": \"CREATED\",\"actorName\":\"ARUN\", \"\": \"\", \"actorId\": \"0x80e36ac92168301f9b12588ba3b28c259c088063d520b422c1108ad96921be6db5717cd8f684984d6a67d38ed3bff0db727144d716730a3124a35c8be9017ca0\", \"attributes\": [], \"updateDatetime\": 1528088768.2945037, \"privateKey\": \"0xf10f009b4def2638784e27c0fda3d5ec4b80d5c9e6412ffdaffd3a266a39be6e\", \"creationDatetime\": 1528088768.2945037, \"defaultWalletAddress\": \"0xe644c5dd5d812c3b02645514bf0048e6c8b20e4eae4d311de074ea615b26279a4f8c3b269e0688d180a4ae79dd0e6489f8a92893330fa3906eefc208225a4c3f\"}]";
		/**
		 * when a new instance of post method created at any time do return
		 * mockPostMethod object instead of new object
		 */
		PowerMockito.whenNew(PostMethod.class).withAnyArguments().thenReturn(mockPostMethod);
		/**
		 * when a new instance of HttpClient created at any time do return
		 * mockHttpClient object instead of new object
		 */
		PowerMockito.whenNew(HttpClient.class).withAnyArguments().thenReturn(mockHttpClient);
		/**
		 * mocking the execute method of HTTP CLIENT and get the result as specified in
		 * the thenReturn method
		 */
		Mockito.when(mockHttpClient.executeMethod(Mockito.any(PostMethod.class))).thenReturn(1);
		/**
		 * mocking the set parameter function of the POST METHOD object
		 */
		Mockito.doNothing().when(mockPostMethod).setParameter(Mockito.anyString(), Mockito.anyString());
		/**
		 * mocking the getResponseBodyAsString of the POST METHOD and return the above
		 * sample data as result
		 */
		Mockito.when(mockPostMethod.getResponseBodyAsString()).thenReturn(result);
		/**
		 * calling the registration method to test based on these mock objects
		 */
		Actor actor = controller.actorRegistration(url, "");
		/**
		 * checking the test is success or not with a expected result and an actual
		 * value
		 */
		assertEquals("ARUN", actor.getOceanResponse().get("result").get("actorName"));

	}

	@Test
	public void assetRegistrationTest() throws Exception {
		URL url = new URL("http://localhost:8000/api/v1/keeper/assets/metadata");
		String result = "[{\"state\": \"CREATED\", \"\": \"\", \"actorId\": \"0x80e36ac92168301f9b12588ba3b28c259c088063d520b422c1108ad96921be6db5717cd8f684984d6a67d38ed3bff0db727144d716730a3124a35c8be9017ca0\", \"attributes\": [], \"updateDatetime\": 1528088768.2945037, \"privateKey\": \"0xf10f009b4def2638784e27c0fda3d5ec4b80d5c9e6412ffdaffd3a266a39be6e\", \"creationDatetime\": 1528088768.2945037, \"defaultWalletAddress\": \"0xe644c5dd5d812c3b02645514bf0048e6c8b20e4eae4d311de074ea615b26279a4f8c3b269e0688d180a4ae79dd0e6489f8a92893330fa3906eefc208225a4c3f\"}]";
		/**
		 * when a new instance of post method created at any time do return
		 * mockPostMethod object instead of new object
		 */
		PowerMockito.whenNew(PostMethod.class).withAnyArguments().thenReturn(mockPostMethod);
		/**
		 * when a new instance of HttpClient created at any time do return
		 * mockHttpClient object instead of new object
		 */
		PowerMockito.whenNew(HttpClient.class).withAnyArguments().thenReturn(mockHttpClient);
		/**
		 * mocking the execute method of HTTP CLIENT and get the result as specified in
		 * the thenReturn method
		 */
		Mockito.when(mockHttpClient.executeMethod(Mockito.any(PostMethod.class))).thenReturn(1);
		/**
		 * mocking the set parameter function of the POST METHOD object
		 */
		Mockito.doNothing().when(mockPostMethod).setParameter(Mockito.anyString(), Mockito.anyString());
		/**
		 * mocking the getResponseBodyAsString of the POST METHOD and return the above
		 * sample data as result
		 */
		Mockito.when(mockPostMethod.getResponseBodyAsString()).thenReturn(result);
		/**
		 * calling the registration method to test based on these mock objects
		 */

		Asset registeredAsset = controller.assetRegistration(url, "", "");
		Actor actor = controller.actorRegistration(url, "");
		/**
		 * checking the test is success or not with a expected result and an actual
		 * value
		 */
		//PowerMockito.doThrow(new NullPointerException()).when(controller.assetRegistration(url, "", ""));

	}

	@Test(expected = NullPointerException.class)
	public void downloadAssetTest() throws Exception {
		URL url = new URL("http://localhost:8000/api/v1/keeper/assets/metadata");
		String result = "[{\"state\": \"CREATED\", \"actorName\": \"ARUN\", \"actorId\": \"0x80e36ac92168301f9b12588ba3b28c259c088063d520b422c1108ad96921be6db5717cd8f684984d6a67d38ed3bff0db727144d716730a3124a35c8be9017ca0\", \"attributes\": [], \"updateDatetime\": 1528088768.2945037, \"privateKey\": \"0xf10f009b4def2638784e27c0fda3d5ec4b80d5c9e6412ffdaffd3a266a39be6e\", \"creationDatetime\": 1528088768.2945037, \"defaultWalletAddress\": \"0xe644c5dd5d812c3b02645514bf0048e6c8b20e4eae4d311de074ea615b26279a4f8c3b269e0688d180a4ae79dd0e6489f8a92893330fa3906eefc208225a4c3f\"}]";

		GetMethod post = mock(GetMethod.class);
		HttpClient client = mock(HttpClient.class);

		PowerMockito.whenNew(GetMethod.class).withAnyArguments().thenReturn(post);
		PowerMockito.whenNew(HttpClient.class).withAnyArguments().thenReturn(client);
		Mockito.when(client.executeMethod(Mockito.any(GetMethod.class))).thenReturn(1);
		
		Mockito.when(post.getResponseBodyAsString()).thenReturn(result);
		assertEquals(new NullPointerException(), controller.downloadAsset(null, ""));
		Asset downloadedAsset = controller.downloadAsset(url, "");
		System.out.println("DOWNLOAD : " + downloadedAsset);

	}
}
