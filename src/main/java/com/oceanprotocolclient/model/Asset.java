package com.oceanprotocolclient.model;

import java.util.HashMap;
import java.util.Map;

public class Asset {
	private String id;
	private String publisherId;
	private String assetId;
	private String assetName;
	/**
	 * {@link Map} to save the response from the ocean network as a key value pair
	 */
	private Map<String, String> oceanResponse;	
	private String contractId;
	private String fileContent;
	private String message;
	
	/**
	 * Construct the oceanResponse  with Hash map for further use.
	 */
	public Asset()
	{
		this.oceanResponse= new HashMap<>();
	}
	
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
				+ ", oceanResponse=" + oceanResponse + ", contractId=" + contractId + ", fileContent=" + fileContent
				+ ", message=" + message + "]";
	}
	public Map<String, String> getOceanResponse() {
		return oceanResponse;
	}
	public void setOceanResponse(Map<String, String> oceanResponse) {
		this.oceanResponse = oceanResponse;
	}
	
}
