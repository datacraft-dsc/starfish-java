package com.oceanprotocolclient.model;

public class Asset {
	private String id;
	private String publisherId;
	private String assetId;
	private String assetName;
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
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
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
		return "Asset [id=" + id + ", publisherId=" + publisherId + ", assetId=" + assetId + ", assetName=" + assetName
				+ ", marketplaceId=" + marketplaceId + ", updateDatetime=" + updateDatetime + ", contentState="
				+ contentState + ", creationDatetime=" + creationDatetime + ", contractId=" + contractId
				+ ", fileContent=" + fileContent + ", message=" + message + "]";
	}
	
}
