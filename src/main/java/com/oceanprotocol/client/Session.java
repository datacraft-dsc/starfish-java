/**
 * Classname  - Session
 * 
 * Version information - version 1
 *
 * Date - 07 june 2018
 * 
 * Copyright notice - Uvionics Tech
 */

/*****************************************************************************************************************************
 * ***************************************************************************************************************************
 * Ocean protocol client API used for connecting to ocean protocol using Java and Spring Boot.
 *      - Actor Registration
 * 	- Get Actor
 * 	- Update Actor
 *	- Disable Actor
 * Actor Registration - This method registers an actor with the Ocean network. 
 * 					   POST "/api/v1/keeper/actors/actor/" 
 * 					   Parameter : actorId
 * Get Actor		 - This method used to fetch the actor information from ocean network
 * 			   		   GET "/api/v1/keeper/actors/actor/<actor_id>"
 * 					   This should take actorId along with url
 * Update Actor 	 - This method used to update the actor details
 * 				 	   PUT "/api/v1/keeper/actors/actor/<actor_id>"
 * 					   Parameter : Actor Name
 *					   This should take actorId along with url
 * Disable Actor	 - This method used to disable the actor.
 *					   DELETE "/api/v1/keeper/actors/actor/<actor_id>"
 *					   This should take actorId along with url
 *****************************************************************************************************************************
 *	 - Asset Registration
 * 	 - Get Asset
 * 	 - Get Assets
 * 	 - Update Asset
 * 	 - Upload Asset
 * 	 - Download Asset
 *  	 - Disable Asset
 *       - Add Asset Provider
 *       - Add Contract
 *       - Get Contract
 * 	 - SignContract
 * 	 - Authorize Contract
 * 	 - Revoke Contract Authorization
 * 	 - Access Contract Asset
 * 	 - Settle Contract
 * 	 - Add Asset Listing
 *
 * assetRegistration - This method registers an asset with the Ocean network. 
 *					   POST "/api/v1/keeper/assets/metadata"
 * 					   Parameter : url , actorId
 * getAsset		 	 - This method used to fetch an asset from the Ocean network. 
 * 					   GET "/api/v1/keeper/assets/metadata/{asset_id}"
 * 					   parameter : url , assetId
 * getAssets		 - This method used to fetch all assets from the Ocean network. 
 * 					   GET "/api/v1/keeper/assets/metadata"
 * 					   parameter : url , assetId
 * updateAsset		 - This method used to update an asset in the the Ocean network. 
 * 					   PUT "/api/v1/keeper/assets/metadata/{asset_id}"
 * 					   parameter : url , assetId,assetName
 * uploadAsset		 - This method used to upload an asset in the the Ocean network. 
 * 					   POST "/api/v1/provider/assets/asset/
 * 					   parameter : url , assetId ,file
 * downloadAsset 	 - This method used to download asset from Ocean network. 
 * 					   GET "/api/v1/provider/assets/asset/"
 * 					   parameter : url , assetId
 * disableAssets	 - This method used to disable asset from Ocean network. 
 * 					   DELETE "/api/v1/keeper/assets/metadata/{asset_id}"
 * 					   parameter : url , assetId ,assetName,actorId
 * addAssetProvider	 - This method used to add asset provider from Ocean network. 
 * 					   POST "/api/v1/keeper/assets/provider"
 * 					   parameter : url , actorId ,assetId
 * addContract		 - This method used to add contract from Ocean network. 
 * 					   POST "/api/v1/keeper/contracts/contract"
 * 					   parameter : url ,assetId
 * getContract		 - This method used to get contract from Ocean network.
 * 					   GET "/api/v1/keeper/contracts/contract/<contract_id>"
 * 					   parameter : url ,contractId
 * ***************************************************************************************************************************
 ** ***************************************************************************************************************************/

package com.oceanprotocol.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.oceanprotocol.model.Actor;
import com.oceanprotocol.model.Asset;

@SuppressWarnings("deprecation")
public class Session {
	// actor url
	public static final String actorURL = "/api/v1/keeper/actors/actor/";
	// keeper url
	public static final String keeperURL = "/api/v1/keeper";
	// provider url
	public static final String providerURL = "/api/v1/provider";
	private String baseurl;

	// constructor to recieve url from user as URL
	public Session(URL baseUrl) {
		this.baseurl = baseUrl.toString();
	}

	// constructor to recieve url from user as String
	public Session(String baseurl) {
		this.baseurl = baseurl;
	}

	/**
	 * This method registers an actor with the Ocean network. POST
	 * "/api/v1/keeper/actors/actor/"
	 * 
	 * @Param actorId
	 * @return actor object
	 * @throws IOException
	 * @throws HttpException
	 * @throws ParseException
	 */

	@SuppressWarnings("unchecked")
	public Actor registerActor(String actorId) throws HttpException, IOException, ParseException {
		JSONObject json = null;
		// Create object for actor class..it include all actor details
		Actor actor = null;
		// Checks the argument values is present or not
		if (baseurl == null) {
			throw new NullPointerException("baseurl is not found");
		}
		if (actorId == null) {
			throw new NullPointerException("actorId is not found");
		}
		String oceanurl = baseurl + actorURL;
		String postActorResp = null;
		/**
		 * Used for posting the data to ocean network
		 */
		PostMethod postActor = new PostMethod(oceanurl);
		// set Parameter actorId
		postActor.setParameter("actorId", actorId);
		HttpClient httpclient = new HttpClient();

		// sent the parameters to ocean network
		httpclient.executeMethod(postActor);
		// Response from ocean network
		postActorResp = postActor.getResponseBodyAsString();
		int statuscode = postActor.getStatusCode();
		if (statuscode == 201) {
			String prepostToJson = postActorResp.substring(1, postActorResp.length() - 1);
			// Data coming from ocean network is a json string..
			// This line remove the "\\" from the response
			String postactorResponseToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			json = (JSONObject) parser.parse(postactorResponseToJson);
			// set the result json to the actor object
			actor = new Actor(json);

		} else {
			String prepostToJson = postActorResp.substring(1, postActorResp.length() - 1);
			json = new JSONObject();
			json.put("response", prepostToJson);
			actor = new Actor(json);
		}
		return actor;
	}

	/**
	 * This method used to fetch the actor information from ocean network GET
	 * "/api/v1/keeper/actors/actor/<actor_id>" This should take actorId
	 * 
	 * @Param actorId
	 * @return actor object
	 * @throws IOException
	 * @throws HttpException
	 * @throws ParseException
	 */

	@SuppressWarnings("unchecked")
	public Actor getActor(String actorId) throws HttpException, IOException, ParseException {
		// Create object for actor class..it include all actor details
		Actor actor = null;
		// Checks the argument values is present or not
		if (baseurl == null) {
			throw new NullPointerException("baseurl is not found");
		}
		if (actorId == null) {
			throw new NullPointerException("actorId is not found");
		}
		String oceanurl = baseurl + actorURL + actorId;
		JSONObject json = null;
		/**
		 * Used for getting the data to ocean network
		 */
		GetMethod getActor = new GetMethod(oceanurl);
		HttpClient httpclient = new HttpClient();
		httpclient.executeMethod(getActor);
		// Response from ocean network
		String getActorResp = getActor.getResponseBodyAsString();
		int statuscode = getActor.getStatusCode();
		if (statuscode == 200) {
			String prepostToJson = getActorResp.substring(1, getActorResp.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String getActorToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			json = (JSONObject) parser.parse(getActorToJson);
			// set the result json to the actor object
			actor = new Actor(json);
		} else {
			String prepostToJson = getActorResp.substring(1, getActorResp.length() - 1);
			json = new JSONObject();
			json.put("response", prepostToJson);
			actor = new Actor(json);
		}
		return actor;
	}

	/**
	 * JSON-encoded key-value pairs from the Actor schema that are allowed to be
	 * updated (only 'name' and 'attributes')
	 * 
	 * @param name
	 * @return updatedresponse
	 * @throws IOException
	 * @throws ParseException
	 *
	 */
	@SuppressWarnings("unchecked")
	public Actor updateActor(String actorId, String actorName) throws IOException, ParseException {
		// Create object for actor class..it include all actor details
		Actor actor = null;
		// Checks the argument values is present or not
		// Checks the argument values is present or not
		if (baseurl == null) {
			throw new NullPointerException("baseurl is not found");
		}
		if (actorId == null || actorName == null) {
			throw new NullPointerException("actorId or actorName not found");
		}
		URL oceanurl = new URL(baseurl + actorURL + actorId);
		JSONObject json = null;
		JSONObject obj = new JSONObject();
		obj.put("name", actorName);
		String updatedresponse = modify(oceanurl, obj, "PUT");
		if (!updatedresponse.equalsIgnoreCase("Not Found")) {
			String prepostToJson = updatedresponse.substring(1, updatedresponse.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String updateActorToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			json = (JSONObject) parser.parse(updateActorToJson);
			// set the result json to the actor object
			actor = new Actor(json);
		} else {
			json = new JSONObject();
			json.put("response", updatedresponse);
			actor = new Actor(json);
		}
		return actor;
	}

	/**
	 * This method is used to disable the actor.
	 * 
	 * @param name
	 * @return response
	 * @throws IOException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public Actor disableActor(String actorId) throws IOException, ParseException {
		// Create object for actor class..it include all actor details
		Actor actor = null;
		// Checks the argument values is present or not
		if (baseurl == null) {
			throw new NullPointerException("baseurl is not found");
		}
		if (actorId == null) {
			throw new NullPointerException("actorId not found");
		}
		URL oceanurl = new URL(baseurl + actorURL + actorId);
		JSONObject json = null;
		JSONObject obj = new JSONObject();
		obj.put("requestor_actor_id", actorId);
		String deletedresponse = modify(oceanurl, obj, "DELETE");
		// got response from ocean network
		if (!deletedresponse.equals("Not Found")) {
			String predeleteToJson = deletedresponse.substring(1, deletedresponse.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String disableActorToJson = predeleteToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			json = (JSONObject) parser.parse(disableActorToJson);
			// set the result json to the actor object
			actor = new Actor(json);
		} else {
			json = new JSONObject();
			json.put("response", deletedresponse);
			actor = new Actor(json);
		}
		return actor;
	}

	/**
	 * This method used to register an asset Json-encoded payload containing the
	 * Asset schema with the assetId, creationDatetime and contentState filled
	 * in.
	 * 
	 * Minimum required: name, publisherId
	 * 
	 * @param publisherId
	 *            - publisher Id
	 * @param assetName
	 *            - assetName
	 * @return java object asset
	 * @throws IOException
	 * @throws HttpException
	 * @throws ParseException
	 */

	public Asset assetRegistration(String publisherId, String assetName)
			throws HttpException, IOException, ParseException {
		// Asset object creation
		Asset asset = null;
		// Checks the argument values is present or not
		if (baseurl == null) {
			throw new NullPointerException("baseurl is not found");
		}
		if (publisherId == null || assetName == null) {
			throw new NullPointerException("publisherId or assetName not found");
		}
		JSONObject json = null;
		String oceanUrl = baseurl + keeperURL + "/assets/metadata";
		// Initialize the variable to null
		String postAssetResp = null;
		// set parameters to PostMethod
		PostMethod postasset = new PostMethod(oceanUrl);
		// set the parameter publisherId
		postasset.setParameter("publisherId", publisherId);
		// set the parameter name
		postasset.setParameter("name", assetName);
		HttpClient httpclient = new HttpClient();
		// post data to a url
		httpclient.executeMethod(postasset);
		// Response from ocean network
		postAssetResp = postasset.getResponseBodyAsString();
		byte[] responseBody = postasset.getResponseBody();
	
		int statuscode = postasset.getStatusCode();
		if (statuscode == 201) {
			// Convert the string into jsonobject
			String prepostToJson = postAssetResp.substring(1, postAssetResp.length() - 1);
			// Remove "\\" from the json string from ocean network
			String postAssetToJson = prepostToJson.replaceAll("\\\\", "");
			// create json parser
			JSONParser parser = new JSONParser();
			// parse the data to json object
			json = (JSONObject) parser.parse(postAssetToJson);
			
			
			asset = new Asset(json);
		} else {
			String prepostToJson = postAssetResp.substring(1, postAssetResp.length() - 1);
			json = new JSONObject();
			json.put("response", prepostToJson);
			asset = new Asset(json);
		}

		return asset;
	}

	/**
	 * It is used to get the asset response from ocean network
	 * 
	 * @param assetId
	 * @return asset
	 * @throws HttpException 
	 * @throws ParseException
	 * @throws IOException
	 */

	@SuppressWarnings("unchecked")
	public Asset getAsset(String assetId) throws HttpException, IOException, ParseException {
		Asset asset = null; // asset object creation
		// Checks the argument values is present or not
		if (baseurl == null) {
			throw new NullPointerException("baseurl is not found");
		}
		if (assetId == null) {
			throw new NullPointerException("assetId not found");
		}
		JSONObject json = null;
		String oceanUrl = baseurl + keeperURL + "/assets/metadata/" + assetId;
		// used for executing the server call
		GetMethod get = new GetMethod(oceanUrl);
		HttpClient httpclient = new HttpClient();
		httpclient.executeMethod(get);
		// used to get response from ocean server
		String getResp = get.getResponseBodyAsString();
		int statuscode = get.getStatusCode();
		if (statuscode == 201) {
			// Convert the string into jsonobject
			String prepostToJson = getResp.substring(1, getResp.length() - 1);
			// Replacing '\' with space
			String postToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			json = (JSONObject) parser.parse(postToJson);
			// set the result json to the asset object
			asset = new Asset(json);
		} else {
			String prepostToJson = getResp.substring(1, getResp.length() - 1);
			json = new JSONObject();
			json.put("response", prepostToJson);
			asset = new Asset(json);
		}
		return asset;
	}

	/**
	 * Update the asset by using the given details PUT parametes url,asset
	 * 
	 * @return assets
	 * @throws IOException
	 * @throws ParseException
	 *
	 */
	@SuppressWarnings("unchecked")
	public Asset updateAsset(String assetId, String assetName) throws IOException, ParseException {
		Asset asset = new Asset();// asset Object Creation
		// Checks the argument values is present or not
		if (baseurl == null) {
			throw new NullPointerException("baseurl is not found");
		}
		if (assetId == null || assetName == null) {
			throw new NullPointerException("assetId or assetName not found");
		}
		URL oceanUrl = new URL(baseurl + keeperURL + "/assets/metadata/" + assetId);
		String updatedresponse = null;
		JSONObject json = null;
		JSONObject obj = new JSONObject();
		obj.put("name", assetName);
		updatedresponse = modify(oceanUrl, obj, "PUT");
		if (!updatedresponse.equals("Not Found")) {
			String prepostToJson = updatedresponse.substring(1, updatedresponse.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String updateAssetToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			json = (JSONObject) parser.parse(updateAssetToJson);
			// set the result json to the asset object
			asset = new Asset(json);
		} else {
			json = new JSONObject();
			json.put("response", updatedresponse);
			asset = new Asset(json);
		}
		return asset;
	}

	/**
	 * 
	 * parameters url,file
	 * 
	 * @param assetId
	 * @param file
	 * @return asset
	 * 
	 *         Allow uploading a file for an already registered asset. The
	 *         upload is submitted to the provider.
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws ParseException
	 */

	@SuppressWarnings({ "resource" })
	public Asset uploadAsset(String assetId, File file) throws ClientProtocolException, IOException, ParseException {
		String uploadassetResp = null;
		// asset Object Creation
		Asset asset = null;

		// Checks the argument values is present or not
		if (baseurl == null) {
			throw new NullPointerException("baseurl is not found");
		}
		if (assetId == null || file == null) {
			throw new NullPointerException("assetId or file not found");
		}
		String oceanUrl = baseurl + providerURL + "/assets/asset/" + assetId;
		// set parameters to PostMethod
		org.apache.http.client.HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(oceanUrl);
		// used for setting the parameters to post and executing the server call
		MultipartEntity entity = new MultipartEntity();
		entity.addPart("file", new FileBody(file));
		post.setEntity(entity);
		// used to get respose from ocean server
		HttpResponse response = client.execute(post);
		HttpEntity entity2 = response.getEntity();
		uploadassetResp = EntityUtils.toString(entity2);
		StatusLine responseLine = response.getStatusLine();
		String prepostToJson = uploadassetResp.substring(1, uploadassetResp.length() - 1);
		// Data coming from ocean network is a json string..This line remove
		// the "\\" from the response
		String updateAssetToJson = prepostToJson.replaceAll("\\\\", "");
		JSONParser parser = new JSONParser();// create json parser
		// parse the data to json object
		JSONObject json = (JSONObject) parser.parse(updateAssetToJson);
		// set the result json to the asset object
		asset = new Asset(json);
		return asset;
	}

	/**
	 * Allow downloading the asset file from the provider. GET
	 * "/api/v1/provider/assets/asset/"
	 * 
	 * @param assetId
	 */

	public Asset downloadAsset(String assetId) {
		Asset asset = null;// asset Object Creation

		// Checks the argument values is present or not
		if (baseurl == null) {
			throw new NullPointerException("baseurl is not found");
		}
		if (assetId == null) {
			throw new NullPointerException("assetId is not found");
		}
		String oceanUrl = baseurl + providerURL + "/assets/asset/" + assetId;

		String getResp = null;
		// Execute a get Method and get response from ocean server
		try {
			GetMethod get = new GetMethod(oceanUrl.toString());
			// setting the headers for the baseurl
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(get);
			// got response from ocean network
			getResp = get.getResponseBodyAsString();
			String prepostToJson = getResp.substring(1, getResp.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String updateAssetToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(updateAssetToJson);
			// set the result json to the asset object
			asset = new Asset(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	/**
	 * Method used to Delete the asset
	 * "/api/v1/keeper/assets/metadata/{asset_id}"
	 * 
	 * @param assetId
	 * @param assetName
	 * @param actorId
	 * @throws ParseException
	 * @throws IOException
	 */

	@SuppressWarnings("unchecked")
	public Asset disableAsset(String assetId, String assetName, String actorId) throws ParseException, IOException {
		Asset asset = new Asset();// asset Object Creation

		// Checks the argument values is present or not
		if (baseurl == null) {
			throw new NullPointerException("baseurl is not found");
		}
		if (assetId == null || assetName == null || actorId == null) {
			throw new NullPointerException("assetId or assetName or actorId not found");
		}
		URL oceanurl = new URL(baseurl + keeperURL + "/metadata/" + assetId);
		String disableAsset = null;
		JSONObject json = null;
		// used for executing the server call
		JSONObject obj = new JSONObject();
		obj.put("requestor_actor_id", actorId);
		// got response from ocean network
		disableAsset = modify(oceanurl, obj, "DELETE");
		if (!disableAsset.equals("Not Found")) {
			String predeleteToJson = disableAsset.substring(1, disableAsset.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String diabledAssetToJson = predeleteToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			json = (JSONObject) parser.parse(diabledAssetToJson);
			// set the result json to the asset object
			asset = new Asset(json);
		} else {
			json = new JSONObject();
			json.put("response", disableAsset);
			asset = new Asset(json);
		}
		return asset;
	}

	/**
	 * This method used to get all assets from ocean network
	 * 
	 * @param assetId
	 */

	public Asset getAssets() {
		Asset asset = new Asset();// asset Object Creation
		// Checks the argument values is present or not
		if (baseurl == null) {
			throw new NullPointerException();
		}
		String oceanUrl = baseurl + keeperURL + "/assets/metadata/";
		String getAssetResp = null;
		try {
			// used executing the server call
			GetMethod get = new GetMethod(oceanUrl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(get);
			// used to get response from ocean server
			getAssetResp = get.getResponseBodyAsString();
			System.out.println(getAssetResp);
			// Convert the string into jsonobject
			String prepostToJson = getAssetResp.substring(1, getAssetResp.length() - 1);
			// replacing '\' with space
			String getAssetToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(getAssetToJson);
			// set the result json to the asset object
			asset = new Asset(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	/**
	 * This is used to add asset provider
	 * 
	 * @param actorId
	 * @param assetId
	 * @throws IOException
	 * @throws HttpException
	 * @throws ParseException
	 */

	public Asset addAssetProvider(String actorId, String assetId) throws HttpException, IOException, ParseException {
		Asset asset = new Asset();// asset Object Creation
		// Checks the argument values is present or not
		if (baseurl == null) {
			throw new NullPointerException();
		}
		if (assetId == null || actorId == null) {
			throw new NullPointerException();
		}
		String oceanUrl = baseurl + keeperURL + "/assets/provider/";
		String getAssetProviderResp = null;
		JSONObject json = null;
		PostMethod postassetprovider = new PostMethod(oceanUrl);
		// set the assetId
		postassetprovider.setParameter("assetId", assetId);
		// set the providerId
		postassetprovider.setParameter("providerId", actorId);
		HttpClient httpclient = new HttpClient();
		// used for executing the server call
		httpclient.executeMethod(postassetprovider);
		// used to get response from ocean server
		getAssetProviderResp = postassetprovider.getResponseBodyAsString();
		int statuscode = postassetprovider.getStatusCode();
		if (statuscode == 201) {
			// Convert the string into jsonobject
			String prepostToJson = getAssetProviderResp.substring(1, getAssetProviderResp.length() - 1);
			// replacing '\' with space
			String postAssetProviderToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			json = (JSONObject) parser.parse(postAssetProviderToJson);
			// set the result json to the asset objectasset = new Asset(json);asset.getOceanResponse().put("result", json);
		} else {
			String prepostToJson = getAssetProviderResp.substring(1, getAssetProviderResp.length() - 1);
			json = new JSONObject();
			json.put("response", prepostToJson);
			asset = new Asset(json);
		}
		return asset;
	}
	/**
	 * This is used to create a contract
	 * 
	 * @param assetId
	 * @throws IOException
	 * @throws HttpException
	 * @throws ParseException
	 */

	@SuppressWarnings("unchecked")
	public Asset addContract(String assetId) throws HttpException, IOException, ParseException {
		Asset asset = new Asset();// asset Object Creation
		// Checks the argument values is present or not
		if (baseurl == null) {
			throw new NullPointerException();
		}
		if (assetId == null) {
			throw new NullPointerException();
		}
		String oceanUrl = baseurl + keeperURL + "/contracts/contract/";
		String postcontractResp = null;
		JSONObject json = null;
		PostMethod postcontract = new PostMethod(oceanUrl);
		// set the assetId
		postcontract.setParameter("assetId", assetId);
		HttpClient httpclient = new HttpClient();
		httpclient.executeMethod(postcontract);
		// used to get response from ocean server
		postcontractResp = postcontract.getResponseBodyAsString();
		int statuscode = postcontract.getStatusCode();
		if (statuscode == 201) {
			// Convert the string into jsonobject
			String prepostToJson = postcontractResp.substring(1, postcontractResp.length() - 1);
			// replacing '\' with space
			String postcontactToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			json = (JSONObject) parser.parse(postcontactToJson);
			// set the result json to the asset object
			asset = new Asset(json);
		} else {
			String prepostToJson = postcontractResp.substring(1, postcontractResp.length() - 1);
			json = new JSONObject();
			json.put("response", prepostToJson);
			asset = new Asset(json);
		}
		return asset;
	}

	/**
	 * This method is used to get contract from alredy contracted stackholders
	 * 
	 * @param contractId
	 * @return
	 * @throws IOException
	 * @throws HttpException
	 * @throws ParseException
	 */

	public Asset getContract(String contractId) throws HttpException, IOException, ParseException {
		Asset asset = new Asset();// asset Object Creation
		// Checks the argument values is present or not
		if (baseurl == null) {
			throw new NullPointerException();
		}
		if (contractId == null) {
			throw new NullPointerException();
		}
		String oceanUrl = baseurl + keeperURL + "/contracts/contract/" + contractId;
		String getContractResp = null;
		JSONObject json = null;
		// used for executing the server call
		GetMethod getContract = new GetMethod(oceanUrl);
		HttpClient httpclient = new HttpClient();
		httpclient.executeMethod(getContract);
		// used to get response from ocean server
		getContractResp = getContract.getResponseBodyAsString();
		int statuscode = getContract.getStatusCode();
		if (statuscode == 201) {
			// Convert the string into jsonobject
			String prepostToJson = getContractResp.substring(1, getContractResp.length() - 1);
			// replacing '\' with space
			String postcontractToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			json = (JSONObject) parser.parse(postcontractToJson);
			// set the result json to the asset object
			asset = new Asset(json);
		} else {
			String prepostToJson = getContractResp.substring(1, getContractResp.length() - 1);
			json = new JSONObject();
			json.put("response", prepostToJson);
			asset = new Asset(json);
		}
		return asset;
	}

	/**
	 * This method is used to sign the contract.
	 * 
	 * @param contractId
	 * @param signingActorId
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws HttpException
	 */
	@SuppressWarnings("unchecked")
	public Asset signContract(String contractId, String signingActorId)
			throws ParseException, HttpException, IOException {
		Asset asset = new Asset();// asset Object Creation
		// Checks the argument values is present or not
		if (baseurl == null) {
			throw new NullPointerException();
		}
		if (contractId == null || signingActorId == null) {
			throw new NullPointerException();
		}
		String oceanUrl = baseurl + keeperURL + "/contracts/contract/" + contractId;
		String postcontractResp = null;
		JSONObject json = null;
		// used for setting the parameters to post and executing the server
		// call
		PostMethod postcontract = new PostMethod(oceanUrl);
		postcontract.setParameter("actorId", signingActorId);
		HttpClient httpclient = new HttpClient();
		httpclient.executeMethod(postcontract);
		// used to get response from ocean server
		postcontractResp = postcontract.getResponseBodyAsString();
		int statuscode = postcontract.getStatusCode();
		if (statuscode == 201) {
			// Convert the string into jsonobject
			String prepostToJson = postcontractResp.substring(1, postcontractResp.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String signedContractToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			json = (JSONObject) parser.parse(signedContractToJson);
			// set the result json to the asset object
			asset = new Asset(json);
		} else {
			String prepostToJson = postcontractResp.substring(1, postcontractResp.length() - 1);
			json = new JSONObject();
			json.put("response", prepostToJson);
			asset = new Asset(json);
		}
		return asset;
	}

	/**
	 * This method is used to authorize the contract.
	 * 
	 * @param contractId
	 * @param assetId
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public Asset authorizeContract(String contractId, String assetId) throws IOException, ParseException {
		Asset asset = new Asset();// asset Object Creation
		// Checks the argument values is present or not
		if (baseurl == null) {
			throw new NullPointerException();
		}
		if (contractId == null || assetId == null) {
			throw new NullPointerException();
		}
		JSONObject json = null;
		URL oceanUrl = new URL(baseurl + keeperURL + "/contracts/contract/" + contractId + "/auth");
		String updatedresponse = null;
		JSONObject obj = new JSONObject();
		obj.put("assetId", assetId);
		updatedresponse = modify(oceanUrl, obj, "PUT");
		if (!updatedresponse.equals("Not Found")) {
			String prepostToJson = updatedresponse.substring(1, updatedresponse.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String authorizeContractToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			json = (JSONObject) parser.parse(authorizeContractToJson);
			// set the result json to the asset object
			asset = new Asset(json);
		} else {
			json = new JSONObject();
			json.put("response", updatedresponse);
			asset = new Asset(json);
		}
		return asset;
	}

	public Asset revokeContractAuthorization(URL url, Asset asset) {
		return null;
	}

	/**
	 * This method is used to access Contract Asset.
	 * 
	 * @param contractId
	 * @return
	 * @throws IOException
	 * @throws HttpException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public Asset accessContractAsset(String contractId) throws HttpException, IOException, ParseException {
		Asset asset = new Asset();// asset Object Creation
		// Checks the argument values is present or not
		if (baseurl == null) {
			throw new NullPointerException();
		}
		if (contractId == null) {
			throw new NullPointerException();
		}
		String oceanUrl = baseurl + keeperURL + "/contracts/contract/" + contractId + "/access";
		String getContractResp = null;
		JSONObject json = null;
		// used for executing the server call
		GetMethod getContract = new GetMethod(oceanUrl);
		HttpClient httpclient = new HttpClient();
		httpclient.executeMethod(getContract);
		// used to get response from ocean server
		getContractResp = getContract.getResponseBodyAsString();
		int statuscode = getContract.getStatusCode();
		if (statuscode == 201) {
			// Convert the string into jsonobject
			String prepostToJson = getContractResp.substring(1, getContractResp.length() - 1);
			// replacing '\' with space
			String accessContractToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			json = (JSONObject) parser.parse(accessContractToJson);
			// set the result json to the asset object
			asset = new Asset(json);
		} else {
			String prepostToJson = getContractResp.substring(1, getContractResp.length() - 1);
			json = new JSONObject();
			json.put("response", prepostToJson);
			asset = new Asset(json);
		}
		return asset;
	}

	/**
	 * This method is used to settle Contract Asset.
	 * 
	 * @param actorId
	 * @param contractId
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */

	@SuppressWarnings("unchecked")
	public Asset settleContract(String actorId, String contractId) throws IOException, ParseException {
		Asset asset = new Asset();// asset Object Creation

		// Checks the argument values is present or not
		if (baseurl == null) {
			throw new NullPointerException();
		}
		if (actorId == null || contractId == null) {
			throw new NullPointerException();
		}
		URL oceanUrl = new URL(baseurl + keeperURL + "/contracts/contract/" + contractId + "/settlement");
		String updatedresponse = null;
		JSONObject json = null;
		JSONObject obj = new JSONObject();
		obj.put("actorId", actorId);
		updatedresponse = modify(oceanUrl, obj, "PUT");
		if (!updatedresponse.equals("Not Found")) {
			String prepostToJson = updatedresponse.substring(1, updatedresponse.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String settleContractContractToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			json = (JSONObject) parser.parse(settleContractContractToJson);
			// set the result json to the asset object
			asset = new Asset(json);
		} else {
			json = new JSONObject();
			json.put("response", updatedresponse);
			asset = new Asset(json);
		}
		return asset;
	}

	/**
	 * This method is used to add asset listing (Market asset end point)
	 * 
	 * @param assetId
	 * @param publisherId
	 * @return
	 * @throws IOException 
	 * @throws HttpException 
	 * @throws ParseException 
	 */

	@SuppressWarnings("unchecked")
	public Asset addAssetListing(String assetId, String publisherId) throws HttpException, IOException, ParseException {
		Asset asset = new Asset();// asset Object Creation

		// Checks the argument values is present or not
		if (baseurl == null) {
			throw new NullPointerException();
		}
		if (assetId == null || publisherId == null) {
			throw new NullPointerException();
		}
		String oceanUrl = baseurl + keeperURL + "/market/asset/" + publisherId;
		String postcontractResp = null;
		JSONObject json = null;
			PostMethod postcontract = new PostMethod(oceanUrl);
			// insert asset publisherId to the json object
			postcontract.setParameter("publisherId", publisherId);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(postcontract);
			// used to get response from ocean server
			postcontractResp = postcontract.getResponseBodyAsString();
			int statuscode = postcontract.getStatusCode();
			if (statuscode == 201) {
			// Convert the string into jsonobject
			String prepostToJson = postcontractResp.substring(1, postcontractResp.length() - 1);
			// replacing '\' with space
			String postcontactToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			json = (JSONObject) parser.parse(postcontactToJson);
			// set the result json to the asset object
			asset = new Asset(json);
			} else {
				String prepostToJson = postcontractResp.substring(1, postcontractResp.length() - 1);
				json = new JSONObject();
				json.put("response", prepostToJson);
				asset = new Asset(json);
			}
		return asset;
	}

	public String modify(URL oceanurl, JSONObject obj, String httpMethod) throws IOException {
		String updatedresponse = "";
		HttpURLConnection conn = (HttpURLConnection) oceanurl.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod(httpMethod);
		conn.setRequestProperty("Content-Type", "application/json");
		String input = obj.toString();
		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		String response = conn.getResponseMessage();
		if (response.equals("OK")) {
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			updatedresponse = br.readLine();
		} else {
			updatedresponse = updatedresponse + response;
		}
		conn.disconnect();
		return updatedresponse;
	}
}
