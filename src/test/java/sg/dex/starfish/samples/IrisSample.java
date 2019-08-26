package sg.dex.starfish.samples;

import sg.dex.starfish.constant.Constant;
import sg.dex.starfish.impl.remote.ARemoteAsset;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.url.ResourceAsset;

import static org.junit.Assert.assertNotNull;

/**
 * This class describe a dummy implementation(Sample) how to register any local asset to the Ocean echosystem
 * using Remote Agent.
 * In this eg : local asset is a csv file
 * In this eg remote agent is used Surfer.it wil do two steps:
 *		1. create ResouceAseet instance based on that csv file.
 * 		2. Register the  asset to any RemoteAgent .
 * After successsful registration of the asset to Ocean EchoSystem and it will return a reference of Remote Asset .
 */

public class IrisSample {

	public static void main(String... args) {
		RemoteAgent surfer = RemoteAgentConfig.getRemoteAgent();

		ResourceAsset iris=ResourceAsset.create("assets/iris.csv");
		ARemoteAsset remoteAsset =surfer.registerAsset(iris);
		iris =(ResourceAsset)iris.includeContentHash();
		iris.validateContentHash();
		assertNotNull(iris.getMetadata().get(Constant.CONTENT_HASH));
	}


}
