package sg.dex.starfish.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Utility class maintaining a cache of parsed JSON objects
 *
 * TODO: confirm hypothesis that a deep clone of JSONObject is cheaper than fresh parsing
 *
 * @author Mike
 *
 */
public class JSONObjectCache {

	private static final WeakHashMap<String,JSONObject> cache=new WeakHashMap<String,JSONObject>();

	/**
	 * Converts a string assumed to contain valid JSON object to an Object
	 * @param jsonString A string containing valid JSON
	 * @throws Error on JSON parsing error
	 * @return A JSONObject as parsed from jsonString
	 */
	@SuppressWarnings("unchecked")
	public synchronized static Map<String,Object> parse(String jsonString) {
		JSONObject cached=cache.get(jsonString);
		if (cached!=null) return new JSONObject(cached); // deep clone
		JSONParser parser=new JSONParser();
		try {
			JSONObject result=(JSONObject) parser.parse(jsonString);
			cache.put(jsonString, result);
			return new JSONObject(result);
		} catch (ParseException e) {
			throw new Error("Error in JSON parsing: "+e.getMessage(),e);
		}
	}
}
