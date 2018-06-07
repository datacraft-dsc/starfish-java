package com.oceanprotocol;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
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

import com.oceanprotocol.client.Session;
import com.oceanprotocol.model.Actor;
import com.oceanprotocol.model.Asset;

/**
 * A Test class to test all the methods in {@link SessionController} to make
 * sure all the units in that class are working properly. Junit test is used to
 * write test cases of the session class with the support of the mocking tool.
 * For local variable mocking power mockito is used in test methods
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Session.class)
public class SessionTest {
	/**
	 * create an object
	 */

	Session controller;
	/**
	 * a mock object of the {@link PostMethod}. While executing a method this mock
	 * object will replace if any post method object used in that method
	 */
	@Mock
	private PostMethod mockPostMethod;

	/**
	 * a mock object of the {@link HttpPost}. While executing a method this mock
	 * object will replace if any post method object used in that method
	 */
	@Mock
	private HttpPost mockHttpPost;
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

	@Mock
	private HttpResponse httpResponse;
	@Mock
	HttpURLConnection connection;

	/**
	 * Before executing a test method this method will be activated. it instantiate
	 * all the mock objects in given above to use.
	 * 
	 * @throws IOException
	 *
	 */

	@Before
	public void before() throws IOException {
		controller = new Session("http://testurl");
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
		URL url = new URL("http://localhost:8000/api/v1/keeper/actors/actor/");
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
		Actor actor = controller.registerActor("");
		/**
		 * checking the test is success or not with a expected result and an actual
		 * value
		 */
		assertEquals("ARUN", actor.getOceanResponse().get("result").get("actorName"));

	}

	/**
	 * stub to test the asset registration method in the {@link Session} uses sample
	 * url and a test result to return mocks the http connections and calls the
	 * asset registration
	 * 
	 * @throws Exception
	 *
	 */
	@Test
	public void assetRegistrationTest() throws Exception {
		URL url = new URL("http://localhost:8000/api/v1/keeper/assets/metadata");
		String result = "[{\"name\": \"data from the cosmos\", \"updateDatetime\": 1528264925.8759427, \"parameters\": [], \"creationDatetime\": 1528264925.8759427, \"publisherId\": \"107\", \"links\": [], \"assetId\": \"1602501922914712807\", \"contentState\": \"UNPUBLISHED\", \"marketplaceId\": null}]";
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
		Asset asset = controller.assetRegistration("", result);
		/**
		 * checking the test is success or not with a expected result and an actual
		 * value
		 */
		assertEquals("1602501922914712807", asset.getOceanResponse().get("result").get("assetId"));

	}

	/**
	 * used to test the get actor method in {@link SessionController} ensure the
	 * procedures are correct in the method uses sample url,result and mock object
	 * for the http connections.
	 * 
	 * @throws Exception
	 *
	 */
	@Test
	public void getActorTest() throws Exception {
		String result = "[{\"actorId\": \"0xcaed2b054e1d2fc1441bae4c28e449846590f3dd49ad5281ff8ae945af4db941a5432b794b67df40f70acd6fa92f6ef15892070f02eedd6d47e3427e3c179fc6\", \"creationDatetime\": 1524783502.1750574, \"updateDatetime\": 1524783502.1750574, \"state\": \"CREATED\", \"name\": \"Actor-three\"}]";
		URL url = new URL("http://localhost:8000/api/v1/keeper/assets/metadata");

		/**
		 * when a new instance of get method created at any time do return
		 * mockGettMethod object instead of new object
		 */
		PowerMockito.whenNew(GetMethod.class).withAnyArguments().thenReturn(mockGetMethod);
		/**
		 * mocking the execute method of HTTP CLIENT and get the result as specified in
		 * the thenReturn method
		 */
		PowerMockito.whenNew(HttpClient.class).withAnyArguments().thenReturn(mockHttpClient);
		/**
		 * mocking the execute method of HTTP CLIENT and get the result as specified in
		 * the thenReturn method
		 */
		Mockito.when(mockHttpClient.executeMethod(Mockito.any(GetMethod.class))).thenReturn(1);
		/**
		 * mocking the getResponseBodyAsString of the POST METHOD and return the above
		 * sample data as result
		 */
		Mockito.when(mockGetMethod.getResponseBodyAsString()).thenReturn(result);

		Actor actor = controller.getActor("");
		/**
		 * checking the test is success or not with a expected result and an actual
		 * value
		 */
		assertEquals("Actor-three", actor.getOceanResponse().get("result").get("name"));

	}

	/**
	 * used to test the get Asset method in {@link SessionController} ensure the
	 * procedures are correct in the method uses sample url,result and mock object
	 * for the http connections.
	 * 
	 * @throws Exception
	 *
	 */
	@Test
	public void getAssetTest() throws Exception {
		String result = "[{\"assetId\": \"1598819930732334101\", \"creationDatetime\": 1524953502.1750574, \"updateDatetime\": 1524953502.1750574, \"contentState\": \"PUBLISHED\", \"name\": \"Asset-20\", \"publisherId\": \"0x9f2489d408aeaf7d09e10fd3bf46ca6c3fc1c3054bb878294536af05aaf58180f29cd9bf1bf2c7113c17f264b165c45e875cb48467bfacea85c4ff470514df13\"}]";
		URL url = new URL("http://localhost:8000/api/v1/keeper/assets/metadata");

		/**
		 * when a new instance of get method created at any time do return
		 * mockGettMethod object instead of new object
		 */
		PowerMockito.whenNew(GetMethod.class).withAnyArguments().thenReturn(mockGetMethod);
		/**
		 * mocking the execute method of HTTP CLIENT and get the result as specified in
		 * the thenReturn method
		 */
		PowerMockito.whenNew(HttpClient.class).withAnyArguments().thenReturn(mockHttpClient);
		/**
		 * mocking the execute method of HTTP CLIENT and get the result as specified in
		 * the thenReturn method
		 */
		Mockito.when(mockHttpClient.executeMethod(Mockito.any(GetMethod.class))).thenReturn(1);
		/**
		 * mocking the getResponseBodyAsString of the POST METHOD and return the above
		 * sample data as result
		 */
		Mockito.when(mockGetMethod.getResponseBodyAsString()).thenReturn(result);

		Asset asset = controller.getAsset("");
		/**
		 * checking the test is success or not with a expected result and an actual
		 * value
		 */
		assertEquals("1598819930732334101", asset.getOceanResponse().get("result").get("assetId"));

	}

	// @Test
	// public void uploadassetTest() throws Exception {
	// org.apache.http.client.HttpClient httpclient=mock(DefaultHttpClient.class);
	// File file = mock(File.class);
	// String result = "[{\"name\": \"data from the cosmos\", \"updateDatetime\":
	// 1528264925.8759427, \"parameters\": [], \"creationDatetime\":
	// 1528264925.8759427, \"publisherId\": \"107\", \"links\": [], \"assetId\":
	// \"1602501922914712807\", \"contentState\": \"UNPUBLISHED\",
	// \"marketplaceId\": null}]";
	// /**
	// * when a new instance of HttpPost created at any time do return
	// * mockHttpPost object instead of new object
	// */
	// PowerMockito.whenNew(HttpPost.class).withAnyArguments().thenReturn(mockHttpPost);
	// /**
	// * when a new instance of HttpClient created at any time do return
	// * mockHttpClient object instead of new object
	// */
	// PowerMockito.whenNew(org.apache.http.client.HttpClient.class).withAnyArguments().thenReturn(httpclient);
	// /**
	// * mocking the execute method of HTTP CLIENT and get the result as
	// * specified in the thenReturn method
	// */
	// Mockito.when(httpclient.execute(Mockito.any(HttpPost.class))).thenReturn(httpResponse);
	// /**
	// * mocking the set entity function of the Httppost object
	// */
	// Mockito.doNothing().when(mockHttpPost).setEntity(Mockito.any(MultipartEntity.class));
	// /**
	// * mocking the getResponseBodyAsString of the POST METHOD and return the
	// * above sample data as result
	// */
	// Mockito.when(mockPostMethod.getResponseBodyAsString()).thenReturn(result);
	// /**
	// * calling the registration method to test based on these mock objects
	// */
	// Asset asset = controller.uploadAsset("", file);
	// /**
	// * checking the test is success or not with a expected result and an
	// * actual value
	// */
	// // assertEquals("1602501922914712807",
	// asset.getOceanResponse().get("result").get("assetId"));
	//
	// }

	/**
	 * stub to test the asset upload method in the {@link Session} uses sample url
	 * and a test result to return mocks the http connections and calls the asset
	 * registration
	 * 
	 * @throws MalformedURLException
	 * 
	 * @throws Exception
	 *
	 */
	@Test
	public void updateAssetTest() throws MalformedURLException {
		URL url =new URL("http://localhost:8000/api/v1/keeper/assets/metadata");
	//	controller.updateAsset("", url.toString());
	}

}