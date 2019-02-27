package sg.dex.starfish.samples;

import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.impl.url.ResourceAsset;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

public class IrisSample {

	public static void main(String... args) {
		RemoteAgent surfer = SurferConfig.getSurfer("http://localhost:8080");

		ResourceAsset iris=ResourceAsset.create("assets/iris.csv");
		
		String prettyJSON = JSON.toPrettyString(iris.getMetadata());
		System.out.println(Utils.stringFromStream(iris.getInputStream()));
		System.out.println("ID = "+iris.getAssetID());
		System.out.println(prettyJSON);
		
		surfer.registerAsset(iris);
	}


}
