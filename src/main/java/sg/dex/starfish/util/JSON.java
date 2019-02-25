package sg.dex.starfish.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
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
	/**
	 * Converts an object to an efficient JSON string representation
	 * @param value Object to represent as a JSON String
	 * @return JSON string representing the value
	 */
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
	 * Converts an object to a pretty-printed JSON string representation suitable for human consumption
	 * @param value Object to represent as a JSON String
	 * @return JSON string representing the value
	 */
	public static String toPrettyString(Object value) {
		StringBuilder sb=new StringBuilder();
		sb=appendPrettyString(sb,value,0);
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	private static StringBuilder appendPrettyString(StringBuilder sb,Object o,int indent) {
		String indentString=createIndentString(indent);
		if (o instanceof Map) {
			int entryIndent=indent+2;
			String internalIndent=createIndentString(entryIndent);
			sb.append("{\n");
			Map<String,Object> m=((Map<String,Object>)o);
			int size=m.size();
			int pos=0;
			for (Map.Entry<String,Object> me:m.entrySet()) {
				String k=me.getKey();
				sb.append(internalIndent);
				sb.append(toString(k));
				sb.append(": ");
				int vIndent=entryIndent+k.length()+4; // indent for value
				Object v=me.getValue();
				appendPrettyString(sb,v,vIndent);
				pos++;
				if (pos==size) {
					sb.append('\n'); // final entry
				} else {
					sb.append(",\n"); // comma for next entry
				}
			}
			sb.append(indentString);
			sb.append("}");
		} else if (o instanceof List) {
			List<Object> list=(List<Object>)o;
			int size=list.size();
			int entryIndent=indent+1;
			String internalIndent=createIndentString(entryIndent);
			sb.append("[");
			for (int i=0; i<size; i++) {
				if (i>0) {
					sb.append(",\n");
					sb.append(internalIndent);
				}
				Object v=list.get(i);
				sb=appendPrettyString(sb,v,entryIndent);
			}
			sb.append("]");
		} else {
			sb.append(toString(o));
		}
		return sb;
	}

	private static String WHITESPACE="                                                             ";
	private static int WHITESPACE_LENGTH=WHITESPACE.length();
	
	/**
	 * 
	 * @param indent Number of whitespace characters
	 * @return String containing the number of whitespace characters specified
	 */
	private static String createIndentString(int indent) {
		String s="";
		while (indent>WHITESPACE_LENGTH) {
			s=s+WHITESPACE;
			indent-=WHITESPACE_LENGTH;
		}
		s=s+WHITESPACE.substring(0,indent);
		return s;
	}

	/**
	 * Converts a string assumed to contain a valid JSON object to a (possibly nested) Map
	 * @param jsonString A string containing a valid JSON object
	 * @return A map representing the JSON object
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(String jsonString) {
		JSONParser parser=new JSONParser();
		try {
			JSONObject result=(JSONObject) parser.parse(jsonString);
			return new JSONObject(result);
		} catch (ParseException e) {
			throw new Error("Error in JSON parsing: "+e.getMessage(),e);
		}
	}
	
	/**
	 * Converts a string assumed to contain valid JSON object to an Object
	 * @param jsonString A string containing valid JSON
	 * @return A Java object represnting the JSON provided
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parse(String jsonString) {
		JSONParser parser=new JSONParser();
		try {
			Object result=parser.parse(jsonString);
			return (T) result;
		} catch (ParseException e) {
			throw new Error("Error in JSON parsing: "+e.getMessage(),e);
		}
	}
}
