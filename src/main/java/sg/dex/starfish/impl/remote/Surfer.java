package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Ocean;
import sg.dex.starfish.util.DID;
import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Surfer {

	public static RemoteAgent getSurfer(String host) {
		Map<String, Object> ddo = new HashMap<>();
		List<Map<String, Object>> services = new ArrayList<>();
		services.add(Utils.mapOf(
					 "type", "Ocean.Meta.v1",
					 "serviceEndpoint", host + "/api/v1/meta"));
		services.add(Utils.mapOf(
					 "type", "Ocean.Storage.v1",
					 "serviceEndpoint", host + "/api/v1/assets"));
		services.add(Utils.mapOf(
					 "type", "Ocean.Invoke.v1",
					 "serviceEndpoint", host + "/api/v1/invoke"));
		services.add(Utils.mapOf(
					 "type", "Ocean.Market.v1",
					 "serviceEndpoint", host + "/api/v1/market"));
		services.add(Utils.mapOf(
					 "type", "Ocean.Auth.v1",
					 "serviceEndpoint", host + "/api/v1/auth"));
		ddo.put("service", services);
		String ddoString = JSON.toPrettyString(ddo);
		// System.out.println(ddoString);

		Ocean ocean = Ocean.connect();
		DID surferDID = DID.createRandom();
		ocean.registerLocalDID(surferDID, ddoString);

		RemoteAgent surfer = RemoteAgent.create(ocean, surferDID);

		return surfer;
	}

}
