package sg.dex.starfish.developer_usecase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import sg.dex.starfish.Ocean;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

/**
 * This class is used to get the Remote Agent based on the host. Currently it
 * will connect with default OCEAN.
 */
public class RemoteAgentConfig {

	private static RemoteAgent surfer ;

	static {
		 surfer = getSurfer(getSurferUrl());
	}

	private static RemoteAgent getSurfer(String host) {
		Map<String, Object> ddo = new HashMap<>();
		List<Map<String, Object>> services = new ArrayList<>();
		services.add(Utils.mapOf(
				"type", "Ocean.Meta.v1",
				"serviceEndpoint", host + "/api/v1/meta"));
		services.add(Utils.mapOf(
				"type", "Ocean.Storage.v1",
				"serviceEndpoint", host + "/api/v1/assets"));
		services.add(Utils.mapOf(
				"type", "Ocean.Invoke.v1",
				"serviceEndpoint", host + "/api/v1/invoke"));
		services.add(Utils.mapOf(
				"type", "Ocean.Auth.v1",
				"serviceEndpoint", host + "/api/v1/auth"));
		services.add(Utils.mapOf(
				"type", "Ocean.Market.v1",
				"serviceEndpoint", host + "/api/v1/market"));
		ddo.put("service", services);
		String ddoString = JSON.toPrettyString(ddo);

		// getting the default Ocean instance
		Ocean ocean = Ocean.connect();
		// creating unique DID
		DID surferDID = DID.createRandom();
		// registering the DID and DDO
		ocean.registerLocalDID(surferDID, ddoString);

		// creating a Remote agent instance for given Ocean and DID
		RemoteAgent surfer = RemoteAgent.create(ocean, surferDID);

		return surfer;
	}

	public static String getSurferUrl(){
		Properties properties = getProperties();
		String ip = properties.getProperty("surfer.host");
		String port = properties.getProperty("surfer.port");
		String surferURL = ip + ":" + port;
		return surferURL;
	}
	public static int getSocketTimeout(){
		Properties properties = getProperties();
		String timeOutValue = properties.getProperty("socket.timeout");
		int timeOut =Integer.parseInt(timeOutValue);

		return timeOut;
	}
	private static Properties getProperties() {
		Properties properties = new Properties();
		try {
			try (InputStream is = RemoteAgentConfig.class.getClassLoader()
					.getResourceAsStream("application_test.properties")) {
				properties.load(is);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * Gets the surfer remote agent for testing purposes
	 * 
	 * @return The RemoteAgent, or null if not up
	 */
	public static RemoteAgent getRemoteAgent() {
		return surfer;

	}

	public static String getDataAsStirngFromInputStream(InputStream is) {

		BufferedReader br = null;
		String result = "";

		try {

			// is = System.in;
			br = new BufferedReader(new InputStreamReader(is));

			String line = null;

			while ((line = br.readLine()) != null) {

				result = result + line;
			}

		}
		catch (IOException ioe) {
			System.out.println("Exception while reading input " + ioe);
		}
		finally {
			// close the streams using close method
			try {
				if (br != null) {
					br.close();
				}
			}
			catch (IOException ioe) {
				System.out.println("Error while closing stream: " + ioe);
			}

		}

		return result;
	}

}
