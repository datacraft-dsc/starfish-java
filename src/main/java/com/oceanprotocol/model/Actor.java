package com.oceanprotocol.model;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

/**
 * + * Represents an Actor on the Ocean Network. + * +
 */
public class Actor {
	/**
	 * {@link Map} to save the response from the ocean network as a key value
	 * pair
	 */
	private Map<String, JSONObject> oceanResponse;

	public Actor() {
		this.oceanResponse = new HashMap<>();
	}

	public Map<String, JSONObject> getOceanResponse() {
		return oceanResponse;
	}

	public void setOceanResponse(Map<String, JSONObject> oceanResponse) {
		this.oceanResponse = oceanResponse;
	}

	@Override
	public String toString() {
		return "Actor [oceanResponse=" + oceanResponse + "]";
	}

}