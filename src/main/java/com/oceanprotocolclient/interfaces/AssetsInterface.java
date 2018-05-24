package com.oceanprotocolclient.interfaces;

import java.io.File;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

import com.oceanprotocolclient.model.Asset;

public interface AssetsInterface {
	Asset assetsRegistration(String publisherId,String name,String targetUrl);
	Asset getAnAssests(String targetUrl);
	ResponseEntity<Object> updateAssets(String targetUrl,Asset asset);
	JSONObject uploadAssest(File file,String targetUrl);
	JSONObject downloadAssest(String targetUrl);
}
