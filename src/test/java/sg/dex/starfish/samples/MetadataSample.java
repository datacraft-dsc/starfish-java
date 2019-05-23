package sg.dex.starfish.samples;

import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.integration.developerTC.RemoteAgentConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This will be a sample steps to get the Asset instance base on Asset id from the RemoteAgent.
 * Here in Example we have used Surfer as the Remote Agent
 */
public class MetadataSample {

	public static void main(String... args) {
		String assetID = null;
		String defaultAssetID = "8d658b5b09ade5526aecf669e4291c07d88e9791420c09c51d2f922f721858d1";

		if (args.length > 0) {
			assetID = args[0];
		}
		if (assetID == null) {
			assetID = defaultAssetID;
		}
		RemoteAgent surfer  = RemoteAgentConfig.getRemoteAgent();

		Asset a=surfer.getAsset(assetID);
		if (assetID == defaultAssetID) {
			assertEquals("{\"name\":\"My Test Asset\"}",a.getMetadataString());
		} else {
			assertTrue(a.getMetadataString().length() > 11);
		}
	}


}
