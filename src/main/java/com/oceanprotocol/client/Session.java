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

import java.io.File;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.oceanprotocol.model.Actor;
import com.oceanprotocol.model.Asset;

@SuppressWarnings("deprecation")
public class Session {

	public static final String actorURL = "/api/v1/keeper/actors/actor";
	public static final String keeperURL = "/api/v1/keeper";
	public static final String providerURL = "/api/v1/provider";

	
	/**
	 * This method registers an actor with the Ocean network. POST
	 * "/api/v1/keeper/actors/actor/"
	 * 
	 * @Param actorId
	 * @Param url
	 * @return actor object
	 */

	public Actor actorRegistration(URL url, String actorId) {
		// Create object for actor class..it include all actor details
		Actor actor = new Actor();
		// Checks the argument values is present or not
		if (url == null || actorId == null) {
			throw new NullPointerException();
		}
		String oceanurl = actorURL;
		String postActorResp = null;
		// Initialize postResp - response from ocean network is given to this
		// variable
		try {
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
			String prepostToJson = postActorResp.substring(1, postActorResp.length() - 1);
			// Data coming from ocean network is a json string..
			// This line remove the "\\" from the response
			String postactorResponseToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser(); // create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(postactorResponseToJson);
			// set the result json to the actor object
			actor.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actor;
	}

	/**
	 * This method used to fetch the actor information from ocean network GET
	 * "/api/v1/keeper/actors/actor/<actor_id>" This should take actorId along
	 * with url
	 * 
	 * @Param actorId
	 * @Param url
	 * @return actor object
	 */

	public Actor getActor(URL url, String actorId) {
		// Create object for actor class..it include all actor details
		Actor actor = new Actor();
		// Checks the argument values is present or not
		if (url == null || actorId == null) {
			throw new NullPointerException();
		}
		String oceanurl = actorURL + actorId;
		String getActorResp = null;
		/**
		 * Used for getting the data to ocean network
		 */
		try {
			GetMethod getActor = new GetMethod(oceanurl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(getActor);
			// Response from ocean network
			getActorResp = getActor.getResponseBodyAsString();
			String prepostToJson = getActorResp.substring(1, getActorResp.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String getActorToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(getActorToJson);
			// set the result json to the actor object
			actor.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actor;
	}

	/**
	 * JSON-encoded key-value pairs from the Actor schema that are allowed to be
	 * updated (only 'name' and 'attributes')
	 * 
	 * @param targetUrl
	 * @param name
	 * @return updatedresponse
	 *
	 */
	public Actor updateActor(URL url, String actorId, String actorName) {
		// Create object for actor class..it include all actor details
		Actor actor = new Actor();
		// Checks the argument values is present or not
		if (url == null || actorId == null || actorName == null) {
			throw new NullPointerException();
		}
		String oceanurl = actorURL + actorId;
		String updatedresponse = null;
		try {
			PutMethod put = new PutMethod(oceanurl);
			HttpMethodParams httpmethod = new HttpMethodParams();
			httpmethod.setParameter("name", actorName);
			put.setParams(httpmethod);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(put);
			// got response from ocean network
			updatedresponse = put.getResponseBodyAsString();
			String prepostToJson = updatedresponse.substring(1, updatedresponse.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String getActorToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(getActorToJson);
			// set the result json to the actor object
			actor.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actor;
	}

	/**
	 * This method is used to disable the actor.
	 * 
	 * @param targetUrl
	 * @param name
	 * @return response
	 */
	public Actor disableActor(URL url, String actorId) {
		// Create object for actor class..it include all actor details
		Actor actor = new Actor();
		// Checks the argument values is present or not
		if (url == null || actorId == null) {
			throw new NullPointerException();
		}
		String oceanurl = actorURL + actorId;
		String deletedresponse = null;
		try {
			DeleteMethod delete = new DeleteMethod(oceanurl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(delete);
			// got response from ocean network
			deletedresponse = delete.getResponseBodyAsString();
			String predeleteToJson = deletedresponse.substring(1, deletedresponse.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String getActorToJson = predeleteToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(getActorToJson);
			// set the result json to the actor object
			actor.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
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
	 * @param url
	 * @param publisherId
	 *            - publisher Id
	 * @param assetName
	 *            - assetName
	 * @return java object asset
	 */

	public Asset assetRegistration(URL url, String publisherId, String assetName) {
		// Asset object creation
		Asset asset = new Asset();
		 
		// Checks the argument values is present or not
		if (url == null) {
			throw new NullPointerException();
		}
		if (publisherId == null || assetName == null) {
			throw new NullPointerException();
		}
		String oceanUrl = url + keeperURL + "/assets/metadata";
		// Initialize the variable to null
		String postAssetResp = null;
		// set parameters to PostMethod
		PostMethod postasset = new PostMethod(oceanUrl);
		// set the parameter publisherId
		postasset.setParameter("publisherId", publisherId);
		// set the parameter name
		postasset.setParameter("name", assetName);
		HttpClient httpclient = new HttpClient();
		try {
			// post data to a url
			httpclient.executeMethod(postasset);
			// Response from ocean network
			postAssetResp = postasset.getResponseBodyAsString();
			// Convert the string into jsonobject
			String prepostToJson = postAssetResp.substring(1, postAssetResp.length() - 1);
			// Remove "\\" from the json string from ocean network
			String postAssetToJson = prepostToJson.replaceAll("\\\\", "");
			// create a json parser
			JSONParser parser = new JSONParser();
			// parse the json string into json object
			JSONObject json = (JSONObject) parser.parse(postAssetToJson);
			// set the result json to the asset object
			asset.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	/**
	 * It is used to get the asset response from ocean network
	 * 
	 * @param url
	 * @param assetId
	 * @return asset
	 */
	public Asset getAsset(URL url, String assetId) {
		Asset asset = new Asset(); // asset object creation
		 
		// Checks the argument values is present or not
		if (assetId == null) {
			throw new NullPointerException();
		}
		String oceanUrl = url + keeperURL + "/assets/metadata/" + assetId;
		String getResp = null;
		try {
			// used for executing the server call
			GetMethod get = new GetMethod(oceanUrl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(get);
			// used to get response from ocean server
			getResp = get.getResponseBodyAsString();
			// Convert the string into jsonobject
			String prepostToJson = getResp.substring(1, getResp.length() - 1);
			// Replacing '\' with space
			String postToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			JSONObject json = (JSONObject) parser.parse(postToJson);
			// set the result json to the asset object
			asset.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	/**
	 * Update the asset by using the given details PUT parametes targetUrl,asset
	 * 
	 * @return assets
	 *
	 */
	public Asset updateAsset(URL url, String assetId, String assetName) {
		Asset asset = new Asset();// asset Object Creation
		 
		// Checks the argument values is present or not
		if (url == null) {
			throw new NullPointerException();
		}
		if (assetId == null || assetName == null) {
			throw new NullPointerException();
		}
		String oceanUrl = url + keeperURL + "/assets/metadata/" + assetId;
		String updatedresponse = null;
		try {
			PutMethod put = new PutMethod(oceanUrl);
			HttpClient httpclient = new HttpClient();
			HttpMethodParams httpmethod = new HttpMethodParams();
			// setting the parameter name to update from ocean network
			httpmethod.setParameter("name", assetName);
			put.setParams(httpmethod);
			httpclient.executeMethod(put);
			// got response from ocean network
			updatedresponse = put.getResponseBodyAsString();
			String prepostToJson = updatedresponse.substring(1, updatedresponse.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String updateAssetToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(updateAssetToJson);
			// set the result json to the asset object
			asset.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	/**
	 * 
	 * parameters targetUrl,file
	 * 
	 * @param url
	 * @param assetId
	 * @param file
	 * @return asset
	 * 
	 *         Allow uploading a file for an already registered asset. The
	 *         upload is submitted to the provider.
	 */

	@SuppressWarnings({ "resource" })
	public Asset uploadAsset(URL url, String assetId, File file) {
		String uploadassetResp = null;
		// asset Object Creation
		Asset asset = new Asset();
		 
		// Checks the argument values is present or not
		if (url == null) {
			throw new NullPointerException();
		}
		if (assetId == null || file == null) {
			throw new NullPointerException();
		}
		String oceanUrl = url + providerURL + "/assets/asset/" + assetId;
		// set parameters to PostMethod
		org.apache.http.client.HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(oceanUrl);
		// used for setting the parameters to post and executing the server call
		MultipartEntity entity = new MultipartEntity();
		entity.addPart("file", new FileBody(file));
		post.setEntity(entity);
		// used to get respose from ocean server
		try {
			HttpResponse response = client.execute(post);
			HttpEntity entity2 = response.getEntity();
			uploadassetResp = EntityUtils.toString(entity2);
			String prepostToJson = uploadassetResp.substring(1, uploadassetResp.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String updateAssetToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(updateAssetToJson);
			// set the result json to the asset object
			asset.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	/**
	 * Allow downloading the asset file from the provider. GET
	 * "/api/v1/provider/assets/asset/"
	 * 
	 * @param url
	 * @param assetId
	 */

	public Asset downloadAsset(URL url, String assetId) {
		Asset asset = new Asset();// asset Object Creation
		 
		// Checks the argument values is present or not
		if (url == null) {
			throw new NullPointerException();
		}
		if (assetId == null) {
			throw new NullPointerException();
		}
		String oceanUrl = url + providerURL + "/assets/asset/" + assetId;

		String getResp = null;
		// Execute a get Method and get response from ocean server
		try {
			GetMethod get = new GetMethod(oceanUrl.toString());
			// setting the headers for the url
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
			asset.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	/**
	 * Method used to Delete the asset
	 * "/api/v1/keeper/assets/metadata/{asset_id}" parametes targetUrl,asset
	 * 
	 * @param assetId
	 * @param assetName
	 * @param actorId
	 */

	public Asset disableAsset(URL url, String assetId, String assetName, String actorId) {
		Asset asset = new Asset();// asset Object Creation
		 
		// Checks the argument values is present or not
		if (url == null) {
			throw new NullPointerException();
		}
		if (assetId == null || assetName == null || actorId == null) {
			throw new NullPointerException();
		}
		String oceanUrl = url + keeperURL + "/metadata/" + assetId;
		String disableAssetMessageHandler = null;
		try {
			// used for executing the server call
			DeleteMethod delete = new DeleteMethod(oceanUrl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(delete);
			// got response from ocean network
			disableAssetMessageHandler = delete.getResponseBodyAsString();
			String predeleteToJson = disableAssetMessageHandler.substring(1, disableAssetMessageHandler.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String diabledAssetToJson = predeleteToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(diabledAssetToJson);
			// set the result json to the asset object
			asset.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	/**
	 * This method used to get all assets from ocean network
	 * 
	 * @param url
	 * @param assetId
	 */

	public Asset getAssets(URL url, String assetId) {
		Asset asset = new Asset();// asset Object Creation
		 
		// Checks the argument values is present or not
		if (url == null) {
			throw new NullPointerException();
		}
		if (assetId == null) {
			throw new NullPointerException();
		}
		String oceanUrl = url + keeperURL + "/metadata/";
		String getAssetResp = null;
		try {
			// used executing the server call
			GetMethod get = new GetMethod(oceanUrl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(get);
			// used to get response from ocean server
			getAssetResp = get.getResponseBodyAsString();
			// Convert the string into jsonobject
			String prepostToJson = getAssetResp.substring(1, getAssetResp.length() - 1);
			// replacing '\' with space
			String getAssetToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(getAssetToJson);
			// set the result json to the asset object
			asset.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	/**
	 * This is used to add asset provider
	 * 
	 * @param url
	 * @param actorId
	 * @param assetId
	 */

	public Asset addAssetProvider(URL url, String actorId, String assetId) {
		Asset asset = new Asset();// asset Object Creation
		 
		// Checks the argument values is present or not
		if (url == null) {
			throw new NullPointerException();
		}
		if (assetId == null || actorId == null) {
			throw new NullPointerException();
		}
		String oceanUrl = url + keeperURL + "/assets/provider/";
		String getAssetProviderResp = null;
		try {
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
			// Convert the string into jsonobject
			String prepostToJson = getAssetProviderResp.substring(1, getAssetProviderResp.length() - 1);
			// replacing '\' with space
			String postAssetProviderToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			JSONObject json = (JSONObject) parser.parse(postAssetProviderToJson);
			// set the result json to the asset object
			asset.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	/**
	 * This is used to create a contract
	 */

	public Asset addContract(URL url, String assetId) {
		Asset asset = new Asset();// asset Object Creation
		 
		// Checks the argument values is present or not
		if (url == null) {
			throw new NullPointerException();
		}
		if (assetId == null) {
			throw new NullPointerException();
		}
		String oceanUrl = url + keeperURL + "/contracts/contract/";
		String postcontractResp = null;
		try {
			PostMethod postcontract = new PostMethod(oceanUrl);
			// set the assetId
			postcontract.setParameter("assetId", assetId);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(postcontract);
			// used to get response from ocean server
			postcontractResp = postcontract.getResponseBodyAsString();
			// Convert the string into jsonobject
			String prepostToJson = postcontractResp.substring(1, postcontractResp.length() - 1);
			// replacing '\' with space
			String postcontactToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			JSONObject json = (JSONObject) parser.parse(postcontactToJson);
			// set the result json to the asset object
			asset.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	/**
	 * This method is used to get contract from alredy contracted stackholders
	 */

	public Asset getContract(URL url, String contractId) {
		Asset asset = new Asset();// asset Object Creation
		 
		// Checks the argument values is present or not
		if (url == null) {
			throw new NullPointerException();
		}
		if (contractId == null) {
			throw new NullPointerException();
		}
		String oceanUrl = url + keeperURL + "/contracts/contract/" + contractId;
		String getContractResp = null;
		try {
			// used for executing the server call
			GetMethod getContract = new GetMethod(oceanUrl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(getContract);
			// used to get response from ocean server
			getContractResp = getContract.getResponseBodyAsString();
			// Convert the string into jsonobject
			String prepostToJson = getContractResp.substring(1, getContractResp.length() - 1);
			// replacing '\' with space
			String postcontractToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			JSONObject json = (JSONObject) parser.parse(postcontractToJson);
			// set the result json to the asset object
			asset.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	/**
	 * This method is used to sign the contract.
	 */
	public Asset signContract(URL url, String contractId, String signingActorId) {
		Asset asset = new Asset();// asset Object Creation
		 
		// Checks the argument values is present or not
		if (url == null) {
			throw new NullPointerException();
		}
		if (contractId == null || signingActorId == null) {
			throw new NullPointerException();
		}
		String oceanUrl = url + keeperURL + "/contracts/contract/" + contractId;
		String postcontractResp = null;
		try {
			// used for setting the parameters to post and executing the server
			// call
			PostMethod postcontract = new PostMethod(oceanUrl);
			postcontract.setParameter("actorId", signingActorId);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(postcontract);
			// used to get response from ocean server
			postcontractResp = postcontract.getResponseBodyAsString();
			// Convert the string into jsonobject
			String prepostToJson = postcontractResp.substring(1, postcontractResp.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String signedContractToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(signedContractToJson);
			// set the result json to the asset object
			asset.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	/**
	 * This method is used to authorize the contract.
	 */
	public Asset authorizeContract(URL url, String contractId, String assetId) {
		Asset asset = new Asset();// asset Object Creation
		 
		// Checks the argument values is present or not
		if (url == null) {
			throw new NullPointerException();
		}
		if (contractId == null || assetId == null) {
			throw new NullPointerException();
		}
		String oceanUrl = url + keeperURL + "/contracts/contract/" + contractId + "/auth";
		String updatedresponse = null;
		try {
			PutMethod put = new PutMethod(oceanUrl);
			HttpMethodParams httpmethod = new HttpMethodParams();
			// setting the parameter assetId to update from ocean network
			httpmethod.setParameter("assetId", assetId);
			put.setParams(httpmethod);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(put);
			// got response from ocean network
			updatedresponse = put.getResponseBodyAsString();
			String prepostToJson = updatedresponse.substring(1, updatedresponse.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String authorizeContractToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(authorizeContractToJson);
			// set the result json to the asset object
			asset.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	public Asset revokeContractAuthorization(URL url, Asset asset) {
		return null;
	}

	/**
	 * This method is used to access Contract Asset.
	 */
	public Asset accessContractAsset(URL url, String contractId) {
		Asset asset = new Asset();// asset Object Creation
		 
		// Checks the argument values is present or not
		if (url == null) {
			throw new NullPointerException();
		}
		if (contractId == null) {
			throw new NullPointerException();
		}
		String oceanUrl = url + keeperURL + "/contracts/contract/" + contractId + "/access";
		String getContractResp = null;
		try {
			// used for executing the server call
			GetMethod getContract = new GetMethod(oceanUrl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(getContract);
			// used to get response from ocean server
			getContractResp = getContract.getResponseBodyAsString();
			// Convert the string into jsonobject
			String prepostToJson = getContractResp.substring(1, getContractResp.length() - 1);
			// replacing '\' with space
			String accessContractToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			JSONObject json = (JSONObject) parser.parse(accessContractToJson);
			// set the result json to the asset object
			asset.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	/**
	 * This method is used to settle Contract Asset.
	 */

	public Asset settleContract(URL url, String actorId, String contractId) {
		Asset asset = new Asset();// asset Object Creation
		 
		// Checks the argument values is present or not
		if (url == null) {
			throw new NullPointerException();
		}
		if (actorId == null || contractId == null) {
			throw new NullPointerException();
		}
		String oceanUrl = url + keeperURL + "/contracts/contract/" + contractId + "/settlement";
		String updatedresponse = null;
		try {
			PutMethod put = new PutMethod(oceanUrl);
			HttpMethodParams httpmethod = new HttpMethodParams();
			// setting the parameter actorId to update from ocean network
			httpmethod.setParameter("actorId", actorId);
			put.setParams(httpmethod);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(put);
			// got response from ocean network
			updatedresponse = put.getResponseBodyAsString();
			String prepostToJson = updatedresponse.substring(1, updatedresponse.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String settleContractContractToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(settleContractContractToJson);
			// set the result json to the asset object
			asset.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	/**
	 * This method is used to add asset listing (Market asset end point)
	 */

	public Asset addAssetListing(URL url, String assetId, String publisherId) {
		Asset asset = new Asset();// asset Object Creation
		 
		// Checks the argument values is present or not
		if (url == null) {
			throw new NullPointerException();
		}
		if (assetId == null || publisherId == null) {
			throw new NullPointerException();
		}
		String oceanUrl = url + keeperURL + "/market/asset/" + publisherId;
		String postcontractResp = null;
		try {
			PostMethod postcontract = new PostMethod(oceanUrl);
			// insert asset publisherId to the json object
			postcontract.setParameter("publisherId", publisherId);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(postcontract);
			// used to get response from ocean server
			postcontractResp = postcontract.getResponseBodyAsString();
			// Convert the string into jsonobject
			String prepostToJson = postcontractResp.substring(1, postcontractResp.length() - 1);
			// replacing '\' with space
			String postcontactToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			JSONObject json = (JSONObject) parser.parse(postcontactToJson);
			// set the result json to the asset object
						asset.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}
}
