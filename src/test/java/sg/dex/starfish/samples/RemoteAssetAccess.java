package sg.dex.starfish.samples;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.dex.starfish.Asset;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

public class RemoteAssetAccess {

	private static RemoteAgent getSurfer(String host) {
		Map<String,Object> ddo=new HashMap<>();
		List<Map<String,Object>> services=new ArrayList<>();
		services.add(Utils.mapOf(
				"type","Ocean.Meta.v1",
				"serviceEndpoint",host+"/api/v1/meta"));
		services.add(Utils.mapOf(
				"type","Ocean.Storage.v1",
				"serviceEndpoint",host+"/api/v1/assets"));
		services.add(Utils.mapOf(
				"type","Ocean.Storage.v1",
				"serviceEndpoint",host+"/api/v1/invoke"));
		ddo.put("service",services);
		String ddoString=JSON.toPrettyString(ddo);
		System.out.println(ddoString);
		Map<String,Object> surferDDO=JSON.toMap(ddoString);
		
		Ocean ocean=Ocean.connect();
		DID surferDID=DID.createRandom();
		ocean.registerLocalDID(surferDID,ddoString);
		
		RemoteAgent surfer=RemoteAgent.create(ocean,surferDID);
		assertEquals(surferDID,surfer.getDID());
		assertEquals(surferDDO,surfer.getDDO());
		return surfer;
	}
	
	public static void main(String... args) {
		RemoteAgent surfer = getSurfer("http://localhost:8080");
		Asset a=surfer.getAsset("69ea9dcb1f9fdbdb803e91d735bdc56b76f7a0130d4a9615955438afda5eb393");
		assertEquals("{\"name\":\"My Test Asset\"}",a.getMetadataString());
	}


}
