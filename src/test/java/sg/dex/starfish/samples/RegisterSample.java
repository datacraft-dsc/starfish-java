package sg.dex.starfish.samples;

import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;
import sg.dex.starfish.util.JSON;

import static org.junit.Assert.assertEquals;

/**
 * This class describe a dummy implementation(Sample) how to register any local asset to the Ocean echosystem
 * using Remote Agent.
 * In this eg : local asset is a any String data
 * In this eg remote agent is used Surfer.it wil do two steps:
 *		1. create ResouceAseet instance based string data passed
 * 		2. Register the  asset to any RemoteAgent .
 * After successsful registration of the asset to Ocean EchoSystem and it will return a reference of Remote Asset .
 */

public class RegisterSample {

	public static String test(String... args) {
		RemoteAgent surfer = RemoteAgentConfig.getRemoteAgent();

		// a new memory asset
		Asset a=MemoryAsset.create("Hello World");
		String assetID = a.getAssetID();
		String prettyJSON = JSON.toPrettyString(a.getMetadata());

		Asset ra=surfer.registerAsset(a);

		// check the metadata is correct
		assertEquals(a.getMetadata(),ra.getMetadata());


		// download the asset
		// Asset dl=MemoryAsset.create(ra);

		return assetID;
	}

	public static void main(String... args) {
		test(args);
	}

}
