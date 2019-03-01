package sg.dex.starfish.samples;

import static org.junit.Assert.assertEquals;

import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.JSON;

public class RegisterSample {

	public static String test(String... args) {
		RemoteAgent surfer = SurferConfig.getSurfer("http://localhost:8080");

		// a new memory asset
		Asset a=MemoryAsset.create("Hello World");
		String assetID = a.getAssetID();
		System.out.println("Asset ID: "+assetID);
		String prettyJSON = JSON.toPrettyString(a.getMetadata());
		System.out.println(prettyJSON);

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
