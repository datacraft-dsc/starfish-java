package sg.dex.starfish.samples;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.dex.starfish.Agent;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Ocean;
import sg.dex.starfish.impl.remote.RemoteAgent;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

/**
 * This class is used to get the Remote Agent based on the host.
 * Currently it will connect with default OCEAN.
 */
public class SurferConfig {

	static RemoteAgent getSurfer(String host) {
		Map<String,Object> ddo=new HashMap<>();
		List<Map<String,Object>> services=new ArrayList<>();
		services.add(Utils.mapOf(
				"type","Ocean.Meta.v1",
				"serviceEndpoint",host+"/api/v1/meta"));
		services.add(Utils.mapOf(
				"type","Ocean.Storage.v1",
				"serviceEndpoint",host+"/api/v1/assets"));
		services.add(Utils.mapOf(
				"type","Ocean.Invoke.v1",
				"serviceEndpoint",host+"/api/v1/invoke"));
		ddo.put("service",services);
		String ddoString=JSON.toPrettyString(ddo);
		// System.out.println(ddoString);
		Map<String,Object> surferDDO=JSON.toMap(ddoString);

		Ocean ocean=Ocean.connect();
		DID surferDID=DID.createRandom();
		ocean.registerLocalDID(surferDID,ddoString);

		RemoteAgent surfer=RemoteAgent.create(ocean,surferDID);
		assertEquals(surferDID,surfer.getDID());
		assertEquals(surferDDO,surfer.getDDO());
		return surfer;
	}
	
	public static void main(String[] args) {
		RemoteAgent surfer=getSurfer("http://13.67.33.157:8080");
		
		// Agent agent=Ocean.connect().getAgent(surfer.getDID());
		Agent agent=surfer;
		Asset a = agent.getAsset("e399c658b8b5e260e946261b6dd19299e8dda7e9f810452deb4887bd702b0c11");
		Map<String,Object> meta=a.getMetadata();
		System.out.println(JSON.toPrettyString(meta));
	}

}
