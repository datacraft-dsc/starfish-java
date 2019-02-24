package sg.dex.starfish.util;

import java.util.Map;
import java.util.WeakHashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
	
	@SuppressWarnings("unchecked")
	public synchronized static Map<String,Object> parse(String s) {
		JSONObject cached=cache.get(s);
		if (cached!=null) return new JSONObject(cached); // deep clone
		JSONParser parser=new JSONParser();
		try {
			JSONObject result=(JSONObject) parser.parse(s);
			cache.put(s, result);
			return new JSONObject(result);
		} catch (ParseException e) {
			throw new Error("Error in JSON parsing: "+e.getMessage(),e);
		}
	}
}
