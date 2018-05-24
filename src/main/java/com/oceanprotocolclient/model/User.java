package com.oceanprotocolclient.model;

public class User {
	String id;
	String actorId;
	String name;
	String dobInString;
	String country;
	String contact;
	String sex;
	String email;
	String userName;
	String password;
	String walletId;
	String privateKey;
	/***********************************/	
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


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getDobInString() {
		return dobInString;
	}

	public void setDobInString(String dobInString) {
		this.dobInString = dobInString;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	

	public String getContact() {
		return contact;
	}


	public void setContact(String contact) {
		this.contact = contact;
	}


	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
		return "User [id=" + id + ", actorId=" + actorId + ", name=" + name + ", dobInString=" + dobInString
				+ ", country=" + country + ", contact=" + contact + ", sex=" + sex + ", email=" + email + ", userName="
				+ userName + ", password=" + password + ", walletId=" + walletId + ", privateKey=" + privateKey + "]";
	}


}