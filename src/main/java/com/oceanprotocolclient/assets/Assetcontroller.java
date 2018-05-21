package com.oceanprotocolclient.assets;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
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

	@Value("${assetupdownUrl}")
	private String assetupdownUrl = "/api/v1/provider/assets/asset/";

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
				resultObject.put("failedResult", "Post Response is not Present");
				resultObject.put("status", 0);
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
		resultObject.put("result", json);
		resultObject.put("status", 1);
		return json;
	}

	@Override
	public JSONObject getAnAssests(String assetId) {
		JSONObject resultObject = new JSONObject();
		String targetUrl = targetHost + assetUrl + assetId;// combine target url
															// asssetupdownur
															// with aseetid

		JSONObject json = null;
		try {

			GetMethod get = new GetMethod(targetUrl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(get);
			String getResp = get.getResponseBodyAsString(); // respose from
															// ocean server
			if (getResp == null) {
				resultObject.put("failedresult", "Get Response is not Present");
				resultObject.put("status", 0);
				return resultObject;
			}
			// Convert the string into jsonobject
			String prepostToJson = getResp.substring(1, getResp.length() - 1);
			String postToJson = prepostToJson.replaceAll("\\\\", ""); // repalcing
																		// '\\'
																		// with
																		// space
			JSONParser parser = new JSONParser();

			json = (JSONObject) parser.parse(postToJson); // parse string to
															// json object
															// json object
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultObject.put("result", json);
		resultObject.put("status", 1);
		return resultObject;

	}

	@Override
	public JSONObject updateAssets() {
		return null;
	}

	@Override
	public JSONObject uploadAssest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject downloadAssest(String assetId) {
		JSONObject resultObject = new JSONObject();
		String getResp = null;
		String targetUrl = targetHost + assetupdownUrl + assetId;// combine
																	// target
																	// url
																	// asssetupdownur
																	// with
																	// aseetid
		try {

			GetMethod get = new GetMethod(targetUrl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(get);
			getResp = get.getResponseBodyAsString();

			if (getResp == null) {
				resultObject.put("status", 1);
				resultObject.put("result", "Get Response is not Present");
				return resultObject;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		resultObject.put("result", getResp);

		return resultObject;
	}

}
