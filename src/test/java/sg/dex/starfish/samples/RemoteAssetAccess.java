package sg.dex.starfish.samples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.dex.starfish.util.JSON;
import sg.dex.starfish.util.Utils;

public class RemoteAssetAccess {

	public static void main(String... args) {
		String host="https://13.67.33.157";
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
		String ddoString=JSON.toString(ddo);
		System.out.println(ddoString);
	}
}
