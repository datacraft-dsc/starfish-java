package sg.dex.starfish.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Utility class for handling JSON objects
 * @author Mike
 *
 */
public class JSON {
	public static Map<String,Object> toMap(JSONObject value) {
		throw new TODOException();
	}

	public static String toString(Object value) {
		// TODO refactor to remove json-simple?
		StringWriter sw=new StringWriter();
		try {
			JSONValue.writeJSONString(value,sw);
		}
		catch (IOException e) {
			throw new RuntimeException("Can't create JSON string from object",e);
		}
		return sw.toString();
	}

	/**
	 * Converts a string assumed to contain a valid JSON object to a (possibly nested) Map
	 * @param s A string containing valid JSON
	 * @return A map representing the JSON object
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(String s) {
		JSONParser parser=new JSONParser();
		try {
			JSONObject result=(JSONObject) parser.parse(s);
			return new JSONObject(result);
		} catch (ParseException e) {
			throw new Error("Error in JSON parsing: "+e.getMessage(),e);
		}
	}
	
	/**
	 * Converts a string assumed to contain valid JSON object to an Object
	 * @param s A string containing valid JSON
	 * @return A Java object represnting the JSON provided
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parse(String s) {
		JSONParser parser=new JSONParser();
		try {
			Object result=parser.parse(s);
			return (T) result;
		} catch (ParseException e) {
			throw new Error("Error in JSON parsing: "+e.getMessage(),e);
		}
	}
}
