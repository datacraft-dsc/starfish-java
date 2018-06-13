/**
  * Represents an asset on the Ocean Network.
  */
package com.oceanprotocol.model;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

public class Asset {
	/**
	 * {@link Map} to save the response from the ocean network as a key value
	 * pair
	 */
	private Map<String, String> oceanResponse;

	/**
	 * Construct the oceanResponse with Hash map for further use.
	 */
	public Asset(Map<String, String> result){
		this.oceanResponse =result;
	}

	public Asset() {}

	

	public Map<String, String> getOceanResponse() {
		return oceanResponse;
	}

	public void setOceanResponse(Map<String, String> oceanResponse) {
		this.oceanResponse = oceanResponse;
	}

	@Override
	public String toString() {
		return "Asset [oceanResponse=" + oceanResponse + "]";
	}

}