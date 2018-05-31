package com.oceanprotocolclient.model;

import java.util.Date;

public class Asset {
	private String id;
	private String publisherId;
	private String assetId;
	private String assetname;
	private String imageName;
	private String files;
	private String country;
	private Date dateTime;
	private String publisherName;
	private String userEmail;
	private String category;
	private String description;
	private String type;
	private String access;
	private boolean sampleenabled;
	private int samples;
	private long filesize = 0;
	private int publishStatus = 0;
	private double price;
	private Integer status = 0;
	/***********************************/	
	private String marketplaceId;
	private String updateDatetime;
	private String contentState;
	private String creationDatetime;
	
	private String contractId;
	private String fileContent;
	private String message;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPublisherId() {
		return publisherId;
	}
	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public String getAssetname() {
		return assetname;
	}
	public void setAssetname(String assetname) {
		this.assetname = assetname;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getFiles() {
		return files;
	}
	public void setFiles(String files) {
		this.files = files;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getPublisherName() {
		return publisherName;
	}
	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAccess() {
		return access;
	}
	public void setAccess(String access) {
		this.access = access;
	}
	public boolean isSampleenabled() {
		return sampleenabled;
	}
	public void setSampleenabled(boolean sampleenabled) {
		this.sampleenabled = sampleenabled;
	}
	public int getSamples() {
		return samples;
	}
	public void setSamples(int samples) {
		this.samples = samples;
	}
	public long getFilesize() {
		return filesize;
	}
	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}
	public int getPublishStatus() {
		return publishStatus;
	}
	public void setPublishStatus(int publishStatus) {
		this.publishStatus = publishStatus;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMarketplaceId() {
		return marketplaceId;
	}
	public void setMarketplaceId(String marketplaceId) {
		this.marketplaceId = marketplaceId;
	}
	public String getUpdateDatetime() {
		return updateDatetime;
	}
	public void setUpdateDatetime(String updateDatetime) {
		this.updateDatetime = updateDatetime;
	}
	public String getContentState() {
		return contentState;
	}
	public void setContentState(String contentState) {
		this.contentState = contentState;
	}
	public String getCreationDatetime() {
		return creationDatetime;
	}
	public void setCreationDatetime(String creationDatetime) {
		this.creationDatetime = creationDatetime;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getFileContent() {
		return fileContent;
	}
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "Asset [id=" + id + ", publisherId=" + publisherId + ", assetId=" + assetId + ", assetname=" + assetname
				+ ", imageName=" + imageName + ", files=" + files + ", country=" + country + ", dateTime=" + dateTime
				+ ", publisherName=" + publisherName + ", userEmail=" + userEmail + ", category=" + category
				+ ", description=" + description + ", type=" + type + ", access=" + access + ", sampleenabled="
				+ sampleenabled + ", samples=" + samples + ", filesize=" + filesize + ", publishStatus=" + publishStatus
				+ ", price=" + price + ", status=" + status + ", marketplaceId=" + marketplaceId + ", updateDatetime="
				+ updateDatetime + ", contentState=" + contentState + ", creationDatetime=" + creationDatetime
				+ ", contractId=" + contractId + ", fileContent=" + fileContent + ", message=" + message + "]";
	}
	

	
	
}
