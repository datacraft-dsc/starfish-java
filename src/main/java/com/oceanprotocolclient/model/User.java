package com.oceanprotocolclient.model;

public class User {
	String id;
	String actorId;
	String actorName;
	String walletId;
	String privateKey;	
	String updateDatetime;
	String state;
	String creationDatetime;
	public User() {
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
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public String getUpdateDatetime() {
		return updateDatetime;
	}
	public void setUpdateDatetime(String updateDatetime) {
		this.updateDatetime = updateDatetime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCreationDatetime() {
		return creationDatetime;
	}
	public void setCreationDatetime(String creationDatetime) {
		this.creationDatetime = creationDatetime;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", actorId=" + actorId + ", actorName=" + actorName + ", walletId=" + walletId
				+ ", privateKey=" + privateKey + ", updateDatetime=" + updateDatetime + ", state=" + state
				+ ", creationDatetime=" + creationDatetime + "]";
	}
	

}