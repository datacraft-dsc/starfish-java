package com.oceanprotocol.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "assets")
public class Assets {
	private String id;
	private String publisherId;
	private String assetId;
	private String assetname;
	private String imageName;
	private String files;
	private String country;
	private Date dateTime;
	public String getFiles() {
		return files;
	}

	public void setFiles(String files) {
		this.files = files;
	}

	private String publisherName;
	private String userEmail;
	private String category;
	private String description;
	private String type;
	private String access;
	private boolean sampleenabled;
	private int samples;
	private long filesize=0;
	
	public long getFilesize() {
		return filesize;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
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

	private double price;
	private Integer status = 0;

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
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
	

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	@Override
	public String toString() {
		return "Assets [id=" + id + ", publisherId=" + publisherId + ", assetId=" + assetId + ", assetname=" + assetname
				+ ", imageName=" + imageName + ", files=" + files + ", country=" + country + ", dateTime=" + dateTime
				+ ", publisherName=" + publisherName + ", userEmail=" + userEmail + ", category=" + category
				+ ", description=" + description + ", type=" + type + ", price=" + price + ", status=" + status + "]";
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
}
