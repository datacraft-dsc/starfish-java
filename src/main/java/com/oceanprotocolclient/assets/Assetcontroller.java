package com.oceanprotocolclient.assets;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.oceanprotocolclient.interfaces.AssetsInterface;

public class Assetcontroller implements AssetsInterface {

	@Value("${targethost}")
	private String targetHost = "http://20.188.98.205:8000";

	@Value("${assetUrl}")
	private String assetUrl = "/api/v1/keeper/assets/metadata/";

	@Override
	public JSONObject assetsRegistration(String publisherId, String name) {
		JSONObject resultObject = new JSONObject();
		String targetUrl = targetHost + assetUrl;
		String postResp = null;
		PostMethod post = new PostMethod(targetUrl);
		post.setParameter("publisherId", publisherId);
		post.setParameter("name", name);

		HttpClient httpclient = new HttpClient();
		try {
			httpclient.executeMethod(post);// post data to a url
			postResp = post.getResponseBodyAsString(); // got response here
			if (postResp == null) {
				resultObject.put("result", "Post Response is not Present");
				return resultObject;
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		// Convert the string into jsonobject
		String prepostToJson = postResp.substring(1, postResp.length() - 1);
		String postToJson = prepostToJson.replaceAll("\\\\", "");
		JSONParser parser = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject) parser.parse(postToJson);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}

	@Override
	public JSONObject getAssests(String assetId) {
		return null;
	}

	@Override
	public JSONObject updateAssets() {
		return null;
	}

}
