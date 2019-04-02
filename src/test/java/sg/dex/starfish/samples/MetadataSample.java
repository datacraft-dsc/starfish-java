package sg.dex.starfish.samples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.remote.Surfer;

/**
 * This will be a sample steps to get the Asset instance base on Asset id from the RemoteAgent.
 * Here in Example we have used Surfer as the Remote Agent
 */
public class MetadataSample {

	public static void main(String... args) {
		String assetID = null;
		String defaultAssetID = "69ea9dcb1f9fdbdb803e91d735bdc56b76f7a0130d4a9615955438afda5eb393";

		if (args.length > 0) {
			assetID = args[0];
		}
		if (assetID == null) {
			assetID = defaultAssetID;
		}
		System.out.println("looking up assetID = "+assetID);
		RemoteAgent surfer = Surfer.getSurfer("http://localhost:8080");
		Asset a=surfer.getAsset(assetID);
		if (assetID == defaultAssetID) {
			assertEquals("{\"name\":\"My Test Asset\"}",a.getMetadataString());
		} else {
			assertTrue(a.getMetadataString().length() > 11);
		}
	}


}
