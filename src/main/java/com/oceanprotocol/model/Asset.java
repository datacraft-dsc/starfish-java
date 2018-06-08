/**
  * Represents an asset on the Ocean Network.
  */
package com.oceanprotocol.model;

import java.util.Map;

import org.json.simple.JSONObject;

public class Asset {
	/**
	 * {@link Map} to save the response from the ocean network as a key value
	 * pair
	 */
	private Map<String, JSONObject> oceanResponse;
	
	public Asset(){}

	/**
	 * Construct the oceanResponse with Hash map for further use.
	 */
	public Asset(Map<String, JSONObject> result){
		this.oceanResponse =result;
	}

	

	public Map<String, JSONObject> getOceanResponse() {
		return oceanResponse;
	}

	public void setOceanResponse(Map<String, JSONObject> oceanResponse) {
		this.oceanResponse = oceanResponse;
	}

	@Override
	public String toString() {
		return "Asset [oceanResponse=" + oceanResponse + "]";
	}

}