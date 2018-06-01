/*****************************************************************************************************************************
 * ***************************************************************************************************************************
 * Ocean protocol client API used for connecting to ocean protocol using Java and Spring Boot.
 * assetController Class includes- Asset Registration
 * 								 - Get Asset
 * 								 - Get Assets
 * 								 - Update Asset
 * 								 - Upload Asset
 * 								 - Download Asset
 *								 - Disable Asset
 *								 - Add Asset Provider
 *								 - Add Contract
 *								 - Get Contract
 *								 - SignContract
 *								 - Authorize Contract
 *								 - Revoke Contract Authorization
 *								 - Access Contract Asset
 *								 - Settle Contract
 *								 - Add Asset Listing
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
 * 
 * 
 *						
 * Author : Aleena,Athul,Arun (Uvionics Tec)
 * ***************************************************************************************************************************
 ** ***************************************************************************************************************************/

package com.oceanprotocolclient.asset;

import java.io.File;
import java.io.IOException;
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
import com.oceanprotocolclient.interfaces.AssetInterface;
import com.oceanprotocolclient.model.Asset;
import com.oceanprotocolclient.model.MessageHandler;

@SuppressWarnings("deprecation")
public class AssetController implements AssetInterface {
	public String keeperURL = "/api/v1/keeper";
	public String providerURL = "/api/v1/provider";
	/**
	 * This method used to register an asset Json-encoded payload containing the
	 * Asset schema with the assetId, creationDatetime and contentState filled
	 * in.
	 * 
	 * Minimum required: name, publisherId
	 * 
	 * @param publisherId-publisher
	 *            Id
	 * @param name
	 *            - publisher name
	 * @param targetUrltarget
	 *            URL
	 * @return java object asset
	 */

	public Asset assetRegistration(URL url, String publisherId, String assetName) {
		// Asset object creation
		Asset asset = new Asset();
		MessageHandler messagehandler = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null) {
			messagehandler.setMessage("Host url not found");
			asset.setMessageHandler(messagehandler);
			return asset;
		}
		if (publisherId == null || assetName == null) {
			messagehandler.setMessage("Publisher Id or Asset Name not found");
			asset.setMessageHandler(messagehandler);
			return asset;
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Convert the string into jsonobject
		String prepostToJson = postAssetResp.substring(1, postAssetResp.length() - 1);
		// Remove "\\" from the json string from ocean network
		String postAssetToJson = prepostToJson.replaceAll("\\\\", "");
		// create a json parser
		JSONParser parser = new JSONParser();
		try {
			// parse the json string into json object
			JSONObject json = (JSONObject) parser.parse(postAssetToJson);
			// Set asset id from json object into asset
			asset.setAssetId((String) json.get("assetId"));
			// Set marketplaceId from json object into asset
			asset.getOceanResponse().put("marketplaceId", json.get("marketplaceId").toString());
			// Set publisherId from json object into asset
			asset.setPublisherId((String) json.get("publisherId"));
			// Set updateDatetime from json object into asset
			asset.getOceanResponse().put("updateDatetime", json.get("updateDatetime").toString());
			// Set contentState from json object into asset
			asset.getOceanResponse().put("contentState", json.get("contentState").toString());
			// Set name from json object into asset
			asset.setAssetName((String) json.get("name"));
			// Set creationDatetime from json object into asset
			asset.getOceanResponse().put("creationDatetime", json.get("creationDatetime").toString());


		} catch (Exception e) {
			// returns the response if no values are present
			messagehandler.setMessage(postAssetResp);
			asset.setMessageHandler(messagehandler);
			e.printStackTrace();
			return asset;
		}
		return asset;
	}

	/**
	 * It is used to get the asset response from ocean network
	 * 
	 * @param targetUrl
	 * 
	 * @return "
	 */
	public Asset getAsset(URL url, String assetId) {
		Asset asset = new Asset(); // asset object creation
		MessageHandler messagehandler = new MessageHandler();
		// Checks the argument values is present or not
		if (assetId == null) {
			messagehandler.setMessage("Asset Id not found");
			asset.setMessageHandler(messagehandler);
			return asset;
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
			// Set asset id into asset
			asset.setAssetId((String) json.get("assetId"));
			// Set publisherId into asset
			asset.setPublisherId((String) json.get("publisherId"));
			// Set name into asset
			asset.setAssetName((String) json.get("name"));
		} catch (Exception e) {
			// returns the response if no values are present
			messagehandler.setMessage(getResp);
			asset.setMessageHandler(messagehandler);
			e.printStackTrace();
			return asset;
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
		MessageHandler messagehandler = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null) {
			messagehandler.setMessage("Host url not found");
			asset.setMessageHandler(messagehandler);
			return asset;
		}
		if (assetId == null || assetName == null) {
			messagehandler.setMessage("Asset Id or Asset Name not found");
			asset.setMessageHandler(messagehandler);
			return asset;
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
			// set the setAssetname to the user object
			asset.setAssetName(json.get("name").toString());
			// set the set Asset id to the user object
			asset.setAssetId(json.get("assetId").toString());
			// set the updateDatetime to the user object
			asset.getOceanResponse().put("updateDatetime", json.get("updateDatetime").toString());
			// set the state to the user object
			asset.getOceanResponse().put("state", json.get("state").toString());
			// set the creationDatetime to the user object
			asset.getOceanResponse().put("creationDatetime", json.get("creationDatetime").toString());
			// set the publisher Id to the user object
			asset.setPublisherId(json.get("publisherId").toString());
		} catch (Exception e) {
			// returns the response if no values are present
			messagehandler.setMessage(updatedresponse);
			asset.setMessageHandler(messagehandler);
			e.printStackTrace();
			return asset;
		}
		return asset;
	}

	/**
	 * 
	 * parametes targetUrl,file
	 * 
	 * @return JSONObject
	 * 
	 *         Allow uploading a file for an already registered asset. The
	 *         upload is submitted to the provider.
	 */

	@SuppressWarnings({ "resource" })
	public Asset uploadAsset(URL url, String assetId, File file) {
		String uploadassetResp = null;
		// asset Object Creation
		Asset asset = new Asset();
		MessageHandler messagehandler = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null) {
			messagehandler.setMessage("Host url not found");
			asset.setMessageHandler(messagehandler);
			return asset;
		}
		if (assetId == null || file == null) {
			messagehandler.setMessage("Asset Id or File not found");
			asset.setMessageHandler(messagehandler);
			return asset;
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
		} catch (Exception e) {
			// returns the response if no values are present
			messagehandler.setMessage(uploadassetResp);
			asset.setMessageHandler(messagehandler);
			e.printStackTrace();
			return asset;
		}
		messagehandler.setMessage(uploadassetResp);
		asset.setMessageHandler(messagehandler);
		return asset;
	}

	/**
	 * Allow downloading the asset file from the provider. GET
	 * "/api/v1/provider/assets/asset/"
	 */

	public Asset downloadAsset(URL url, String assetId) {
		Asset asset = new Asset();// asset Object Creation
		MessageHandler messagehandler = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null) {
			messagehandler.setMessage("Host url not found");
			asset.setMessageHandler(messagehandler);
			return asset;
		}
		if (assetId == null) {
			messagehandler.setMessage("Asset Id not found");
			asset.setMessageHandler(messagehandler);
			return asset;
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
		} catch (Exception e) {
			// returns the response if no values are present
			messagehandler.setMessage(getResp);
			asset.setMessageHandler(messagehandler);
			e.printStackTrace();
			return asset;
		}
		messagehandler.setFileContent(getResp);
		asset.setMessageHandler(messagehandler);
		return asset;
	}

	/**
	 * Method used to Delete the asset
	 * "/api/v1/keeper/assets/metadata/{asset_id}" parametes targetUrl,asset
	 */

	public Asset disableAsset(URL url, String assetId, String assetName, String actorId) {
		Asset asset = new Asset();// asset Object Creation
		MessageHandler messagehandler = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null) {
			messagehandler.setMessage("Host url not found");
			asset.setMessageHandler(messagehandler);
			return asset;
		}
		if (assetId == null || assetName == null || actorId == null) {
			messagehandler.setMessage("Asset Id or Asset Name or Actor Id  not found");
			asset.setMessageHandler(messagehandler);
			return asset;
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
			// set the setAssetname to the user object
			asset.setAssetName(json.get("name").toString());
			// set the set Asset id to the user object
			asset.setAssetId(json.get("assetId").toString());
			// set the updateDatetime to the user object
			asset.getOceanResponse().put("updateDatetime", json.get("updateDatetime").toString());
			// set the state to the user object
			asset.getOceanResponse().put("state", json.get("state").toString());
			// set the creationDatetime to the user object
			asset.getOceanResponse().put("creationDatetime", json.get("creationDatetime").toString());
			// set the publisher Id to the user object
			asset.setPublisherId(json.get("publisherId").toString());

		} catch (Exception e) {
			// returns the response if no values are present
			messagehandler.setMessage(disableAssetMessageHandler);
			asset.setMessageHandler(messagehandler);
			e.printStackTrace();
			return asset;
		}
		return asset;
	}

	/**
	 * This method used to get all assets from ocean network
	 */

	public Asset getAssets(URL url, String assetId) {
		Asset asset = new Asset();// asset Object Creation
		MessageHandler messagehandler = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null) {
			messagehandler.setMessage("Host url not found");
			asset.setMessageHandler(messagehandler);
			return asset;
		}
		if (assetId == null) {
			messagehandler.setMessage("Asset Id not found");
			asset.setMessageHandler(messagehandler);
			return asset;
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
			// set the setAssetname to the user object
			asset.setAssetName(json.get("name").toString());
			// set the set Asset id to the user object
			asset.setAssetId(json.get("assetId").toString());
			// set the updateDatetime to the user object
			asset.getOceanResponse().put("updateDatetime", json.get("updateDatetime").toString());
			// set the state to the user object
			asset.getOceanResponse().put("state", json.get("state").toString());
			// set the creationDatetime to the user object
			asset.getOceanResponse().put("creationDatetime", json.get("creationDatetime").toString());
			// set the publisher Id to the user object
			asset.setPublisherId(json.get("publisherId").toString());

		} catch (Exception e) {
			// returns the response if no values are present
			messagehandler.setMessage(getAssetResp);
			asset.setMessageHandler(messagehandler);
			e.printStackTrace();
			return asset;
		}
		return asset;
	}

	/**
	 * This is used to add asset provider
	 */

	public Asset addAssetProvider(URL url, String actorId, String assetId) {
		Asset asset = new Asset();// asset Object Creation
		MessageHandler messagehandler = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null) {
			messagehandler.setMessage("Host url not found");
			asset.setMessageHandler(messagehandler);
			return asset;
		}
		if (assetId == null || actorId == null) {
			messagehandler.setMessage("Asset Id or Actor Id  not found");
			asset.setMessageHandler(messagehandler);
			return asset;
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
			asset.setAssetId(json.get("assetId").toString());
			// Set asset id into asset
		} catch (Exception e) {
			// returns the response if no values are present
			messagehandler.setMessage(getAssetProviderResp);
			asset.setMessageHandler(messagehandler);
			e.printStackTrace();
			return asset;
		}
		return asset;
	}

	/**
	 * This is used to create a contract
	 */

	public Asset addContract(URL url, String assetId) {
		Asset asset = new Asset();// asset Object Creation
		MessageHandler messagehandler = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null) {
			messagehandler.setMessage("Host url not found");
			asset.setMessageHandler(messagehandler);
			return asset;
		}
		if (assetId == null) {
			messagehandler.setMessage("Asset Id not found");
			asset.setMessageHandler(messagehandler);
			return asset;
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
			// set the set Asset id to the user object
			asset.setAssetId(json.get("assetId").toString());
		} catch (Exception e) {
			// returns the response if no values are present
			messagehandler.setMessage(postcontractResp);
			asset.setMessageHandler(messagehandler);
			e.printStackTrace();
			return asset;
		}
		return asset;
	}

	/**
	 * This method is used to get contract from alredy contracted stackholders
	 */

	public Asset getContract(URL url, String contractId) {
		Asset asset = new Asset();// asset Object Creation
		MessageHandler messagehandler = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null) {
			messagehandler.setMessage("Host url not found");
			asset.setMessageHandler(messagehandler);
			return asset;
		}
		if (contractId == null) {
			messagehandler.setMessage("contract Id not found");
			asset.setMessageHandler(messagehandler);
			return asset;
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
			// Set asset id into asset
			asset.setAssetId(json.get("assetId").toString());
		} catch (Exception e) {
			// returns the response if no values are present
			messagehandler.setMessage(getContractResp);
			asset.setMessageHandler(messagehandler);
			e.printStackTrace();
			return asset;
		}
		return asset;
	}

	/**
	 * This method is used to sign the contract.
	 */
	public Asset signContract(URL url, String contractId, String signingActorId) {
		Asset asset = new Asset();// asset Object Creation
		MessageHandler messagehandler = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null) {
			messagehandler.setMessage("Host url not found");
			asset.setMessageHandler(messagehandler);
			return asset;
		}
		if (contractId == null || signingActorId == null) {
			messagehandler.setMessage("Contract Id or SigningActor Id not found");
			asset.setMessageHandler(messagehandler);
			return asset;
		}
		String oceanUrl = url + keeperURL + "/contracts/contract/" + contractId;
		String postcontractResp = null;
		try {
			// used for setting the parameters to post and executing the server call
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
			// set the set Asset id to the user object
			asset.setAssetId(json.get("assetId").toString());
		} catch (Exception e) {
			// returns the response if no values are present
			messagehandler.setMessage(postcontractResp);
			asset.setMessageHandler(messagehandler);
			e.printStackTrace();
			return asset;
		}
		return asset;
	}

	/**
	 * This method is used to authorize the contract.
	 */
	public Asset authorizeContract(URL url, String contractId, String assetId) {
		Asset asset = new Asset();// asset Object Creation
		MessageHandler messagehandler = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null) {
			messagehandler.setMessage("Host url not found");
			asset.setMessageHandler(messagehandler);
			return asset;
		}
		if (contractId == null || assetId == null) {
			messagehandler.setMessage("Contract Id or Asset Id not found");
			asset.setMessageHandler(messagehandler);
			return asset;
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
			// set the set Asset id to the user object
			asset.setAssetId(json.get("assetId").toString());
		} catch (Exception e) {
			// returns the response if no values are present
			messagehandler.setMessage(updatedresponse);
			asset.setMessageHandler(messagehandler);
			e.printStackTrace();
			return asset;
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
		MessageHandler messagehandler = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null) {
			messagehandler.setMessage("Host url not found");
			asset.setMessageHandler(messagehandler);
			return asset;
		}
		if (contractId == null) {
			messagehandler.setMessage("Contract Id not found");
			asset.setMessageHandler(messagehandler);
			return asset;
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
			// set the set Asset id to the user object
			asset.setAssetId(json.get("assetId").toString());
		} catch (Exception e) {
			// returns the response if no values are present
			messagehandler.setMessage(getContractResp);
			asset.setMessageHandler(messagehandler);
			e.printStackTrace();
			return asset;
		}
		return asset;
	}

	/**
	 * This method is used to settle Contract Asset.
	 */

	public Asset settleContract(URL url, String actorId, String contractId) {
		Asset asset = new Asset();// asset Object Creation
		MessageHandler messagehandler = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null) {
			messagehandler.setMessage("Host url not found");
			asset.setMessageHandler(messagehandler);
			return asset;
		}
		if (actorId == null || contractId == null) {
			messagehandler.setMessage("Actor Id or Contract Id not found");
			asset.setMessageHandler(messagehandler);
			return asset;
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
			// set the set Asset id to the user object
			asset.setAssetId(json.get("assetId").toString());
		} catch (Exception e) {
			// returns the response if no values are present
			messagehandler.setMessage(updatedresponse);
			asset.setMessageHandler(messagehandler);
			e.printStackTrace();
			return asset;
		}
		return asset;
	}

	/**
	 * This method is used to add asset listing (Market asset end point)
	 */

	public Asset addAssetListing(URL url, String assetId, String publisherId) {
		Asset asset = new Asset();// asset Object Creation
		MessageHandler messagehandler = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null) {
			messagehandler.setMessage("Host url not found");
			asset.setMessageHandler(messagehandler);
			return asset;
		}
		if (assetId == null || publisherId == null) {
			messagehandler.setMessage("Asset Id or Publisher Id not found");
			asset.setMessageHandler(messagehandler);
			return asset;
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
			// Set asset id into asset
			asset.setAssetId(json.get("assetId").toString());
		} catch (Exception e) {
			// returns the response if no values are present
			messagehandler.setMessage(postcontractResp);
			asset.setMessageHandler(messagehandler);
			e.printStackTrace();
			return asset;
		}
		return asset;
	}
}
