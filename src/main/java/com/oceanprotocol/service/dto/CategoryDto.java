package com.oceanprotocol.service.dto;

import java.util.List;

import org.json.simple.JSONObject;

import com.oceanprotocol.model.Assets;

public class CategoryDto {
	
	private String name;
	private List<JSONObject> assets;
	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		return "CategoryDto [subCategory=" + name + ", assets=" + assets + "]";
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<JSONObject> getAssets() {
		return assets;
	}
	public void setAssets(List<JSONObject> assetsWithImageUrl) {
		this.assets = assetsWithImageUrl;
	}  

}
