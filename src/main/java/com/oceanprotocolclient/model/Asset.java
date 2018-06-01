/**
 * Asset class
 */

package com.oceanprotocolclient.model;

import java.util.HashMap;
import java.util.Map;

public class Asset {
	private String publisherId;
	private String assetId;
	private String assetName;
	/**
	 * {@link Map} to save the response from the ocean network as a key value
	 * pair
	 */
	private Map<String, String> oceanResponse;
	private String contractId;
	private Response response;

	/**
	 * Construct the oceanResponse with Hash map for further use.
	 */
	public Asset() {
		this.oceanResponse = new HashMap<>();
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

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "Asset [publisherId=" + publisherId + ", assetId=" + assetId + ", assetName=" + assetName
				+ ", oceanResponse=" + oceanResponse + ", contractId=" + contractId + "]";
	}

	public Map<String, String> getOceanResponse() {
		return oceanResponse;
	}

	public void setOceanResponse(Map<String, String> oceanResponse) {
		this.oceanResponse = oceanResponse;
	}

}
