package sg.dex.starfish.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.dex.starfish.constant.Constant;

/**
 * Utility functions for handling and managing W3C DDOs
 * 
 * @author Mike
 *
 */
public class DDO {

	/**
	 * Creates a basic DDO Map containing the base DID and context defaults.
	 * 
	 * @param did DID to include within this DDO
	 * @return
	 */
	public static Map<String, Object> createBasicDDO(DID did) {
		HashMap<String, Object> ddo = new HashMap<>();

		ddo.put("id", did.withoutPath().toString());
		ddo.put("@context", "https://www.w3.org/2019/did/v1");
		return ddo;
	}

	/**
	 * Gets an endpoint for the given service type, or null if not found
	 * @param ddo
	 * @param type
	 * @return
	 */
	public static String getEndpoint(Map<String, Object> ddo, String type) {
		List<Map<String, Object>> services = getServices(ddo);
		// 
		if (services == null) return null;
		for (Map<String, Object> service : services) {
			if (type.equals(service.get("type"))) return (String) service.get(Constant.SERVICE_ENDPOINT);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static List<Map<String, Object>> getServices(Map<String, Object> ddo) {
		return (List<Map<String, Object>>) ddo.get(Constant.SERVICE);
	}
}
