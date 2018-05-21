package com.oceanprotocol.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.methods.GetMethod;
import org.json.simple.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.oceanprotocol.model.Assets;
import com.oceanprotocol.service.dto.CategoryDto;

public interface AssetService {
	
	Assets save(Assets assetSave); //save assets

	Assets save(Assets assetSave, String email,String fileName); //save assets

	Assets setAssetStaus(String assetId, int status); //set asset status

	List<Assets> findByStatus(int status); //find list by status (status 1-upload,2-download

	JSONObject findByEmail(String email); //find upload and download asset by email (status 1-upload,2-download

	List<CategoryDto> findByCategory(String category,String email); // list asset by category and email

	List<CategoryDto> findByCategory(String category); // list asset according to category

	List<Assets> findByType(String subCategory); //list assets according to type

	List<CategoryDto> findByuserEmail(String email); //list category according to publisher Id
	
	JSONObject findCount(String email);//count of total asset for a particular user
	
	JSONObject findCount();//count of total asset
	
	Assets setAssetStausAndFileName(String assetId, int i, String name);

	Map<String, String> getEncodedData(GetMethod get, String assetId) throws IOException;

	String checkFile(String fileName, String assetId);

	Assets findOne(String assetId);
	
	Assets updateAsset(Assets updatedasset);

	List<JSONObject> findAllAssetswithImage(String category, String type) throws IOException;

	Assets findByCategoryAndAssetId(String category, String assetId);
	
	Assets findAsseturl(String assetId);//find assetimageurl by pass assetId

	List<Assets> findAll();
	
	List<Assets> findAllByCategory(String category); //find all assets for count according to category
	
	List<Assets> findAllByCategoryAndUserEmail(String category,String email); //find all assets for count according to category and email for signed in user
	
	Assets findByAssetId(String assetId);
	
}
