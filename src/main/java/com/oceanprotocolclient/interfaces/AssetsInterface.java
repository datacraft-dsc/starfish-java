package com.oceanprotocolclient.interfaces;

import org.json.simple.JSONObject;

public interface AssetsInterface {

	JSONObject assetsRegistration(String assetId);
	JSONObject getAssests(String assetId);
	JSONObject updateAssets();
	
}
