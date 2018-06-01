package com.oceanprotocolclient.model;

import java.util.HashMap;
import java.util.Map;
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
	private Map<String, String> oceanResponse;

	/**
	 * Construct the oceanResponse with Hash map for further use.
	 */
	Response response;

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

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public Map<String, String> getOceanResponse() {
		return oceanResponse;
	}

	public void setOceanResponse(Map<String, String> oceanResponse) {
		this.oceanResponse = oceanResponse;
	}

	@Override
	public String toString() {
		return "User [actorId=" + actorId + ", actorName=" + actorName + ", walletId=" + walletId
				+ ", oceanResponse=" + oceanResponse + "]";
	}

}