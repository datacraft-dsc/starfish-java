package sg.dex.starfish.samples;

import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.url.ResourceAsset;
import sg.dex.starfish.util.JSON;

public class IrisSample {

	public static void main(String... args) {
		RemoteAgent surfer = SurferConfig.getSurfer("http://localhost:8080");

		ResourceAsset iris=ResourceAsset.create("assets/iris.csv");
		
		// System.out.println(Utils.stringFromStream(iris.getInputStream()));
		System.out.println(JSON.toPrettyString(iris.getAssetID()));
		System.out.println(JSON.toPrettyString(iris.getMetadata()));
		
		surfer.registerAsset(iris);
	}


}
