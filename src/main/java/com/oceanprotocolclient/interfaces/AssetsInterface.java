package com.oceanprotocolclient.interfaces;

import java.io.File;

import org.json.simple.JSONObject;

public interface AssetsInterface {

	JSONObject assetsRegistration(String publisherId,String name);
	JSONObject getAnAssests(String assetId);
	JSONObject updateAssets();
	JSONObject uploadAssest(String assetId,File file);
	JSONObject downloadAssest(String assetId);
	
	
	
}
