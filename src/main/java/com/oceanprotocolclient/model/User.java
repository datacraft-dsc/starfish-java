package com.oceanprotocolclient.model;

import java.util.HashMap;
import java.util.Map;

public class User {
	String id;
	String actorId;
	String actorName;
	String walletId;
	/**
	 * {@link Map} to save the response from the ocean network as a key value pair
	 */
	private Map<String, String> oceanResponse;	

	/**
	 * Construct the oceanResponse  with Hash map for further use.
	 */
	public User()
	{
		this.oceanResponse= new HashMap<>();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "User [id=" + id + ", actorId=" + actorId + ", actorName=" + actorName + ", walletId=" + walletId
				+ ", oceanResponse=" + oceanResponse + "]";
	}

	public Map<String, String> getOceanResponse() {
		return oceanResponse;
	}

	public void setOceanResponse(Map<String, String> oceanResponse) {
		this.oceanResponse = oceanResponse;
	}
	

}