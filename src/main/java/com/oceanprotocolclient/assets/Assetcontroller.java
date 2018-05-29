/**
 *  
 * To handle API Calls from User Side 
 * This should take publisherId,name,assetId,file... as parameters
 * This data should be analyzed and returned to appropriate function call
 * For registering an user.
 * url to register an asset   :  http://host:8000/api/v1/keeper/assets/metadata/
 * url to download/upload an asset   :  http://host:8000/api/v1/provider/assets/asset/
 * paramter :assetId
 * Author : Aleena,Athul,Arun (Uvionics Tec)
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
	 * This method used to register an asset Json-encoded payload containing the
	 * Asset schema with the assetId, creationDatetime and contentState filled
	 * in.
	 * 
	 * Minimum required: name, publisherId * @return java object asset
	 */

	@Override
	public Asset assetsRegistration(String publisherId, String name, String targetUrl) {
		Asset assets = new Asset(); // Asset object creation
		String postResp = null; // Initialize the varibale
		// set parameters to PostMethod
		PostMethod post = new PostMethod(targetUrl);
		// set the parametre publisherId
		post.setParameter("publisherId", publisherId);
		// set the parametre name
		post.setParameter("name", name);
		HttpClient httpclient = new HttpClient();
		try {
			httpclient.executeMethod(post);// post data to a url
			postResp = post.getResponseBodyAsString(); // got response here
			// check wether the ocean network response is empty or not
			if (postResp == null) {
				return assets;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Convert the string into jsonobject
		String prepostToJson = postResp.substring(1, postResp.length() - 1);
		// Remove "\\" from the json string from ocean network
		String postToJson = prepostToJson.replaceAll("\\\\", "");
		// create a json parser
		JSONParser parser = new JSONParser();
		// Initialise the json variable
		JSONObject json = null;
		try {
			// parse the json string into json object
			json = (JSONObject) parser.parse(postToJson);
			// Set asset id from json object into asset
			assets.setAssetId((String) json.get("assetId"));
			// Set marketplaceId from json object into asset
			assets.setMarketplaceId((String) json.get("marketplaceId"));
			// Set publisherId from json object into asset
			assets.setPublisherId((String) json.get("publisherId"));
			// Set updateDatetime from json object into asset
			assets.setUpdateDatetime(json.get("updateDatetime").toString());
			// Set contentState from json object into asset
			assets.setContentState((String) json.get("contentState"));
			// Set name from json object into asset
			assets.setAssetname((String) json.get("name"));
			// Set creationDatetime from json object into asset
			assets.setCreationDatetime(json.get("creationDatetime").toString());

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return assets;
	}

	/**
	 * It is used to get the asset response from ocean network Paramete
	 * targetUrl
	 * 
	 * @return java object asset GET "/api/v1/keeper/assets/metadata/{asset_id}"
	 */
	@Override
	public Asset getAnAssests(String targetUrl) {
		Asset assets = new Asset(); // asset object creation
		JSONObject json = null; // initialize the json object into null
		try {

			GetMethod get = new GetMethod(targetUrl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(get);
			// used to get response from ocean server
			String getResp = get.getResponseBodyAsString();
			// check the response from ocean network
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
			// Set asset id into asset
			assets.setAssetId((String) json.get("assetId"));
			// Set publisherId into asset
			assets.setPublisherId((String) json.get("publisherId"));
			// Set name into asset
			assets.setAssetname((String) json.get("name"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return assets;

	}

	/**
	 * Update the asset by using the given details PUT
	 * "/api/v1/keeper/assets/metadata/{asset_id}" parametes targetUrl,asset
	 * 
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
			// insert asset name to the json object
			assetName.put("name", asset.getAssetname());
			// create and http entity to attach with the rest url
			org.springframework.http.HttpEntity<JSONObject> entity = new org.springframework.http.HttpEntity<>(
					assetName, headers);
			// sent data request fro update data to ocean network
			ResponseEntity<Object> response = restTemplate.exchange(targetUrl, HttpMethod.PUT, entity, Object.class);
			return response;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * parametes targetUrl,file
	 * 
	 * @return JSONObject
	 * 
	 * Allow uploading a file for an already registered asset. The
	 * upload is submitted to the provider. POST
	 * "/api/v1/provider/assets/asset/"
	 */

	@SuppressWarnings({ "resource", "unchecked" })
	@Override
	public JSONObject uploadAssest(File file, String targetUrl) {
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
	/**
	 * Allow downloading the asset file from the provider. GET "/api/v1/provider/assets/asset/"
	 */

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

	/**
	 * Method used to Delete the asset 
	 * "/api/v1/keeper/assets/metadata/{asset_id}" 
	 * parametes targetUrl,asset
	 */
	@Override
	public ResponseEntity<Object> disableAssets(String targetUrl, Asset asset) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			// setting the headers for the url
			HttpHeaders headers = new HttpHeaders();
			// content-type setting
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			// create a json object to accept the asset name
			JSONObject assetName = new JSONObject();
			// insert asset name to the json object
			assetName.put("name", asset.getAssetname());
			// create and http entity to attach with the rest url
			org.springframework.http.HttpEntity<JSONObject> entity = new org.springframework.http.HttpEntity<>(
					assetName, headers);
			// sent data request fro delete asset from ocean network
			ResponseEntity<Object> response = restTemplate.exchange(targetUrl, HttpMethod.DELETE, entity, Object.class);
			return response;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
