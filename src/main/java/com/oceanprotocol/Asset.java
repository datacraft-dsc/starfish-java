/**
  * Represents an Asset on the Ocean Network.
  * 
  * Assets are defined by JSON metadata, and the Asset ID is the keccak256 hash of the metadata
  * as encoded in UTF-8.
  */
package com.oceanprotocol;

import java.util.Map;

import org.json.simple.JSONObject;

public class Asset {
	/**
	 * {@link Map} to save the response from the ocean network as a key value
	 * pair
	 */

	private JSONObject metadata=null;
	private final String metadataString;
	private final String id;
	
	private Asset(String meta){
		this.metadataString=meta;
		this.id="TODO-Keccak-Hash:"+super.toString();
	}
	

	public Map<String, JSONObject> getMetadata() {
		return metadata;
	}

	@Override
	public String toString() {
		return getID();
	}

	private String getID() {
		return id;
	}


	/**
	 * Gets the metadata for this asset as a String
	 * @return
	 */
	public String getMetadataString() {
		return metadataString;
	}

}