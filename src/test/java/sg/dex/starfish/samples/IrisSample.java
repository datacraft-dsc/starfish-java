package sg.dex.starfish.samples;

import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.url.ResourceAsset;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

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
		RemoteAgent surfer = SurferConfig.getSurfer("http://localhost:8080");

		ResourceAsset iris=ResourceAsset.create("assets/iris.csv");

		String prettyJSON = JSON.toPrettyString(iris.getMetadata());
		System.out.println(Utils.stringFromStream(iris.getContentStream()));
		System.out.println("Asset ID = "+iris.getAssetID());
		System.out.println(prettyJSON);

		surfer.registerAsset(iris);
	}


}
