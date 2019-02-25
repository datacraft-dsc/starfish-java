package sg.dex.starfish.samples;

import static org.junit.Assert.assertEquals;

import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.impl.remote.RemoteAgent;

public class RegisterSample {

	public static void main(String... args) {
		RemoteAgent surfer = SurferConfig.getSurfer("http://13.67.33.157:8080");
		Asset a=MemoryAsset.create("Hello World");
		
		System.out.println("Asset ID: "+a.getAssetID());
		System.out.println(a.getMetadataString());
		
		Asset ra=surfer.registerAsset(a);
		
		// check the metadata is correct
		assertEquals(a.getMetadata(),ra.getMetadata());
	}


}
