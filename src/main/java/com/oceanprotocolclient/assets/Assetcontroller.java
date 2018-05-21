/**
 *  
 * To handle API Calls from User Side 
 * This should take publisherId,name,assetId,file... as parameters
 * This data should be analyzed and returned to appropriate function call
 * For registering an user.
 * url to register an asset   :  http://host:8000/api/v1/keeper/assets/metadata/
 * url to download/upload an asset   :  http://host:8000/api/v1/provider/assets/asset/
 * paramter :assetId
 * Author : Athul (Uvionics Tec)
 */

package com.oceanprotocolclient.assets;

import java.io.File;
import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import com.oceanprotocolclient.interfaces.AssetsInterface;

@SuppressWarnings("deprecation")
public class Assetcontroller implements AssetsInterface {

	@Value("${targethost}")
	private String targetHost = "http://20.188.98.205:8000";

	@Value("${assetUrl}")
	private String assetUrl = "/api/v1/keeper/assets/metadata/";

	@Value("${assetupdownUrl}")
	private String assetupdownUrl = "/api/v1/provider/assets/asset/";

	/**
	 * 
	 * @param publisherId,name
	 * @return JSONObject
	 * 
	 *         Posting a url and return result into Asset Controller
	 */

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject assetsRegistration(String publisherId, String name) {
		JSONObject resultObject = new JSONObject();
		// combine targetHost and asssetupdownurl with aseetid
		String targetUrl = targetHost + assetUrl;
		String postResp = null;
		// set parameters to PostMethod
		PostMethod post = new PostMethod(targetUrl);
		post.setParameter("publisherId", publisherId);
		post.setParameter("name", name);

		HttpClient httpclient = new HttpClient();
		try {
			httpclient.executeMethod(post);// post data to a url
			postResp = post.getResponseBodyAsString(); // got response here

			// Used for return a Json Object with failed result and status

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
			e.printStackTrace();
		}

		// Used for adding response and status to Json Object

		resultObject.put("result", json);
		resultObject.put("status", 1);
		return resultObject;
	}

	/**
	 * 
	 * @param assetId
	 * @return JSONObject
	 * 
	 *         Posting a url and return result into Asset Controller
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getAnAssests(String assetId) {
		JSONObject resultObject = new JSONObject();
		// combine targetHost and asssetupdownurl with aseetid
		String targetUrl = targetHost + assetUrl + assetId;

		JSONObject json = null;
		try {

			GetMethod get = new GetMethod(targetUrl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(get);
			// used to get respose from ocean server
			String getResp = get.getResponseBodyAsString();
			// Used for return a Json Object with failed result and status
			if (getResp == null) {
				resultObject.put("failedresult", "Get Response is not Present");
				resultObject.put("status", 0);
				return resultObject;
			}
			// Convert the string into jsonobject
			String prepostToJson = getResp.substring(1, getResp.length() - 1);
			// repalcing '\' with space
			String postToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			json = (JSONObject) parser.parse(postToJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Used for adding response and status to Json Object
		resultObject.put("result", json);
		resultObject.put("status", 1);
		return resultObject;

	}

	@Override
	public JSONObject updateAssets() {
		return null;
	}

	/**
	 * 
	 * @param assetId,file
	 * @return JSONObject
	 * 
	 *         Posting a url and return result into Asset Controller
	 */

	@SuppressWarnings({ "resource", "unchecked" })
	@Override
	public JSONObject uploadAssest(String assetId, File file) {
		JSONObject resultObject = new JSONObject();
		// combine targetHost and asssetupdownurl with aseetid
		String targetUrl = targetHost + assetupdownUrl + assetId;
		// set parameters to PostMethod
		org.apache.http.client.HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(targetUrl);
		MultipartEntity entity = new MultipartEntity();
		entity.addPart("file", new FileBody(file));
		post.setEntity(entity);
		String string = null;
		// used to get respose from ocean server
		try {
			HttpResponse response = client.execute(post);
			HttpEntity entity2 = response.getEntity();
			string = EntityUtils.toString(entity2);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Used for return a Json Object with failed result and status
		if (string == null) {
			resultObject.put("status", 0);
			resultObject.put("failedResult", "Get Response is not Present");
			return resultObject;

		}
		// Used for adding response and status to Json Object
		else {
			resultObject.put("status", 1);
			resultObject.put("result", file.getName());
			return resultObject;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject downloadAssest(String assetId) {
		JSONObject resultObject = new JSONObject();
		String getResp = null;
		// combine targetHost and asssetupdownurl with aseetid
		String targetUrl = targetHost + assetupdownUrl + assetId;
		// Execute a get Method and get respose from ocean server
		try {

			GetMethod get = new GetMethod(targetUrl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(get);
			getResp = get.getResponseBodyAsString();
			// Used for return a Json Object with failed result and status
			if (getResp == null) {
				resultObject.put("status", 0);
				resultObject.put("failedResult", "Get Response is not Present");
				return resultObject;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// Used for adding response and status to Json Object
		resultObject.put("result", getResp);
		resultObject.put("status", 1);

		return resultObject;
	}

}
