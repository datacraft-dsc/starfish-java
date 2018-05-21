package com.oceanprotocolclient.interfaces;

import org.json.simple.JSONObject;

public interface AssetsInterface {

	JSONObject assetsRegistration(String publisherId,String name);
	JSONObject getAnAssests(String assetId);
	JSONObject updateAssets();
	JSONObject uploadAssest();
	JSONObject downloadAssest(String assetId);
	
	
	
}
