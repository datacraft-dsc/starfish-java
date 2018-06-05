package com.oceanprotocol.model;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
/**
+ * Represents an Actor on the Ocean Network.
+ *
+ */
public class Actor {
	String actorId;
	String actorName;
	String walletId;
	/**
	 * {@link Map} to save the response from the ocean network as a key value
	 * pair
	 */
	private Map<String, JSONObject> oceanResponse;


	public Actor() {
		this.oceanResponse = new HashMap<>();
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public String getActorName() {
		return actorName;
	}

	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}


	public Map<String, JSONObject> getOceanResponse() {
		return oceanResponse;
	}

	public void setOceanResponse(Map<String, JSONObject> oceanResponse) {
		this.oceanResponse = oceanResponse;
	}

	@Override
	public String toString() {
		return "Actor [actorId=" + actorId + ", actorName=" + actorName + ", walletId=" + walletId + ", oceanResponse="
				+ oceanResponse + "]";
	}



}