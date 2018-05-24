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
import java.util.Arrays;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.oceanprotocolclient.interfaces.AssetsInterface;
import com.oceanprotocolclient.model.Asset;

@SuppressWarnings("deprecation")
public class Assetcontroller implements AssetsInterface {

	/**
	 * 
	 * @param publisherId,name
	 * @return JSONObject
	 * 
	 *         Posting a url and return result into Asset Controller
	 */

	@Override
	public Asset assetsRegistration(String publisherId, String name,String targetUrl) {
		Asset assets = new Asset();
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
				return assets;
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
			assets.setAssetId((String) json.get("assetId"));
			assets.setMarketplaceId((String) json.get("marketplaceId"));
			assets.setPublisherId((String) json.get("publisherId"));
			assets.setUpdateDatetime( json.get("updateDatetime").toString());
			assets.setContentState((String) json.get("contentState"));
			assets.setAssetname((String) json.get("name"));
			assets.setCreationDatetime(json.get("creationDatetime").toString());
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// Used for adding response and status to Json Object
		
		return assets;
	}

	/**
	 * 
	 * @param assetId
	 * @return JSONObject
	 * 
	 *         Posting a url and return result into Asset Controller
	 */
	@Override
	public Asset getAnAssests(String targetUrl) {
		Asset assets = new Asset();
		JSONObject json = null;
		try {

			GetMethod get = new GetMethod(targetUrl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(get);
			// used to get response from ocean server
			String getResp = get.getResponseBodyAsString();
			// Used for return a Json Object with failed result and status
			if (getResp == null) {
				return assets;
			}
			// Convert the string into jsonobject
			String prepostToJson = getResp.substring(1, getResp.length() - 1);
			// repalcing '\' with space
			String postToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			json = (JSONObject) parser.parse(postToJson);
			assets.setAssetId((String) json.get("assetId"));
			assets.setPublisherId((String) json.get("publisherId"));
			assets.setAssetname((String) json.get("name"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Used for adding response and status to Json Object
		return assets;

	}

	/**
	 * Update the asset by using the given details
	 * 
	 * @param assetId
	 * @param asset
	 * @return
	 *
	 */
	@Override
	public ResponseEntity<Object> updateAssets(String targetUrl, Asset asset) {

		try {
			RestTemplate restTemplate = new RestTemplate();
			// setting the headers for the url
			HttpHeaders headers = new HttpHeaders();
			// content-type setting
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			
			// create a json object to accept the asset name
			JSONObject assetName = new JSONObject();
			//insert asset name to the json object
			assetName.put("name", asset.getAssetname());

			// create and http entity to attach with the rest url
			org.springframework.http.HttpEntity<JSONObject> entity = new org.springframework.http.HttpEntity<>(assetName, headers);
			ResponseEntity<Object> response = restTemplate.exchange(targetUrl, HttpMethod.PUT, entity, Object.class);
				
				return response;

			

		} catch (Exception ex) {
			ex.printStackTrace();

		}

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
	public JSONObject uploadAssest(File file,String targetUrl) {
		JSONObject resultObject = new JSONObject();
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
	public JSONObject downloadAssest(String targetUrl) {
		JSONObject resultObject = new JSONObject();
		String getResp = null;
		// Execute a get Method and get respose from ocean server
		try {
			GetMethod get = new GetMethod(targetUrl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(get);
			getResp = get.getResponseBodyAsString();
			System.out.println(getResp);
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
