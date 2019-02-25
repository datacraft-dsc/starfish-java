package sg.dex.starfish.samples;

import static org.junit.Assert.assertEquals;

import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.remote.RemoteAgent;

public class MetadataSample {

	public static void main(String... args) {
		RemoteAgent surfer = SurferConfig.getSurfer("http://localhost:8080");
		Asset a=surfer.getAsset("69ea9dcb1f9fdbdb803e91d735bdc56b76f7a0130d4a9615955438afda5eb393");
		assertEquals("{\"name\":\"My Test Asset\"}",a.getMetadataString());
	}


}
