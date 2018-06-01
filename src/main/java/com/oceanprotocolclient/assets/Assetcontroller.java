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

package com.oceanprotocolclient.assets;

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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.oceanprotocolclient.interfaces.AssetInterface;
import com.oceanprotocolclient.model.Asset;

@SuppressWarnings("deprecation")
public class Assetcontroller implements AssetInterface {
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
		String oceanUrl = url + keeperURL + "/assets/metadata";
		// Asset object creation
		Asset asset = new Asset();
		// check the url is null or not
		if (url == null) {
			asset.setMessage("Host url not found");
			return asset;
		}
		if (publisherId == null || assetName == null) {
			asset.setMessage("Publisher Id or Asset Name not found");
			return asset;
		}
		// Initialize the varible to null
		String postAssetResp = null;
		// set parameters to PostMethod
		PostMethod postasset = new PostMethod(oceanUrl);
		// set the parametre publisherId
		postasset.setParameter("publisherId", publisherId);
		// set the parametre name
		postasset.setParameter("name", assetName);
		HttpClient httpclient = new HttpClient();
		try {
			// post data to a url
			httpclient.executeMethod(postasset);
			// Response from ocean network
			postAssetResp = postasset.getResponseBodyAsString();
			// check wether the ocean network response is empty or not
			if (postAssetResp == null) {
				asset.setMessage("Response from ocean network is not found");
				return asset;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Convert the string into jsonobject
		String prepostToJson = postAssetResp.substring(1, postAssetResp.length() - 1);
		// Remove "\\" from the json string from ocean network
		String postAssetToJson = prepostToJson.replaceAll("\\\\", "");
		// create a json parser
		JSONParser parser = new JSONParser();
		// Initialise the json variable
		JSONObject json = null;
		try {
			// parse the json string into json object
			json = (JSONObject) parser.parse(postAssetToJson);
			// Set asset id from json object into asset
			asset.setAssetId((String) json.get("assetId"));
			// Set marketplaceId from json object into asset
			asset.setMarketplaceId((String) json.get("marketplaceId"));
			// Set publisherId from json object into asset
			asset.setPublisherId((String) json.get("publisherId"));
			// Set updateDatetime from json object into asset
			asset.setUpdateDatetime(json.get("updateDatetime").toString());
			// Set contentState from json object into asset
			asset.setContentState((String) json.get("contentState"));
			// Set name from json object into asset
			asset.setAssetName((String) json.get("name"));
			// Set creationDatetime from json object into asset
			asset.setCreationDatetime(json.get("creationDatetime").toString());

		} catch (ParseException e) {
			e.printStackTrace();
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
		String oceanUrl = url + keeperURL + "/assets/metadata/" + assetId;
		Asset asset = new Asset(); // asset object creation
		JSONObject json = null; // initialize the json object into null
		if (assetId == null) {
			asset.setMessage("Response from ocean network is not found");
			return asset;
		}
		try {

			GetMethod get = new GetMethod(oceanUrl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(get);
			// used to get response from ocean server
			String getResp = get.getResponseBodyAsString();
			// check the response from ocean network
			if (getResp == null) {
				asset.setMessage("Response from ocean network is not found");
				return asset;
			}
			// Convert the string into jsonobject
			String prepostToJson = getResp.substring(1, getResp.length() - 1);
			// repalcing '\' with space
			String postToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			json = (JSONObject) parser.parse(postToJson);
			// Set asset id into asset
			asset.setAssetId((String) json.get("assetId"));
			// Set publisherId into asset
			asset.setPublisherId((String) json.get("publisherId"));
			// Set name into asset
			asset.setAssetName((String) json.get("name"));
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
		String oceanUrl = url + keeperURL + "/assets/metadata/" + assetId;
		Asset asset = new Asset();
		String updatedresponse = null;
		try {
			PutMethod put = new PutMethod(oceanUrl);
			HttpClient httpclient = new HttpClient();
			HttpMethodParams httpmethod = new HttpMethodParams();
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
			asset.setUpdateDatetime(json.get("updateDatetime").toString());
			// set the state to the user object
			asset.setContentState((String) json.get("state"));
			// set the creationDatetime to the user object
			asset.setCreationDatetime(json.get("creationDatetime").toString());
			// set the publisher Id to the user object
			asset.setPublisherId(json.get("publisherId").toString());

		} catch (Exception ex) {
			ex.printStackTrace();
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

	@SuppressWarnings({ "resource", "unchecked" })
	public Asset uploadAsset(URL url, String assetId, File file) {
		String uploadassetResp = null;
		String oceanUrl = url + providerURL + "/assets/asset/" + assetId;
		Asset asset = new Asset();
		JSONObject uploadedassetObject = new JSONObject();
		// set parameters to PostMethod
		org.apache.http.client.HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(oceanUrl);
		MultipartEntity entity = new MultipartEntity();
		entity.addPart("file", new FileBody(file));
		post.setEntity(entity);

		// used to get respose from ocean server
		try {
			HttpResponse response = client.execute(post);
			HttpEntity entity2 = response.getEntity();
			uploadassetResp = EntityUtils.toString(entity2);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		asset.setMessage(uploadassetResp);
		return asset;
	}

	/**
	 * Allow downloading the asset file from the provider. GET
	 * "/api/v1/provider/assets/asset/"
	 */

	@SuppressWarnings("unchecked")
	public Asset downloadAsset(URL url, String assetId) {
		String oceanUrl = url + providerURL + "/assets/asset/" + assetId;
		Asset asset = new Asset();
		JSONObject downloadedassetObject = new JSONObject();
		String getResp = null;
		// Execute a get Method and get respose from ocean server
		try {
			GetMethod get = new GetMethod(oceanUrl.toString());
			// setting the headers for the url
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(get);
			// got response from ocean network
			getResp = get.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		asset.setFileContent(getResp);

		return asset;
	}

	/**
	 * Method used to Delete the asset
	 * "/api/v1/keeper/assets/metadata/{asset_id}" parametes targetUrl,asset
	 */

	public Asset disableAssets(URL url, String assetId, String assetName, String actorId) {
		String oceanUrl = url + keeperURL + "/metadata/" + assetId;
		Asset asset = new Asset();
		String disableAssetResponse = null;
		try {
			DeleteMethod delete = new DeleteMethod(oceanUrl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(delete);
			// got response from ocean network
			disableAssetResponse = delete.getResponseBodyAsString();
			String predeleteToJson = disableAssetResponse.substring(1, disableAssetResponse.length() - 1);
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
			asset.setUpdateDatetime(json.get("updateDatetime").toString());
			// set the state to the user object
			asset.setContentState((String) json.get("state"));
			// set the creationDatetime to the user object
			asset.setCreationDatetime(json.get("creationDatetime").toString());
			// set the publisher Id to the user object
			asset.setPublisherId(json.get("publisherId").toString());

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return asset;
	}

	/**
	 * This method used to get all assets from ocean network
	 */

	public Asset getAssets(URL url, String assetId) {
		String oceanUrl = url + keeperURL + "/metadata/";
		JSONObject resultObject = new JSONObject();
		Asset asset = new Asset();
		try {

			GetMethod get = new GetMethod(oceanUrl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(get);
			// used to get response from ocean server
			String getAssetResp = get.getResponseBodyAsString();
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
			asset.setUpdateDatetime(json.get("updateDatetime").toString());
			// set the state to the user object
			asset.setContentState((String) json.get("state"));
			// set the creationDatetime to the user object
			asset.setCreationDatetime(json.get("creationDatetime").toString());
			// set the publisher Id to the user object
			asset.setPublisherId(json.get("publisherId").toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	/**
	 * This is used to add asset provider
	 */

	public Asset addAssetProvider(URL url, String actorId, String assetId) {
		String oceanUrl = url + keeperURL + "/assets/provider/";
		JSONObject assetProviderObject = new JSONObject();
		Asset asset = new Asset();
		try {

			PostMethod postassetprovider = new PostMethod(oceanUrl);
			// set the assetId
			postassetprovider.setParameter("assetId", assetId);
			// set the providerId
			postassetprovider.setParameter("providerId", actorId);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(postassetprovider);
			// used to get response from ocean server
			String getAssetProviderResp = postassetprovider.getResponseBodyAsString();
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
			e.printStackTrace();
		}
		return asset;
	}

	/**
	 * This is used to create a contract
	 */

	public Asset addContract(URL url, String assetId) {
		String oceanUrl = url + keeperURL + "/contracts/contract/";
		JSONObject resultObject = new JSONObject();
		Asset asset = new Asset();
		JSONObject json;
		try {
			PostMethod postcontract = new PostMethod(oceanUrl);
			postcontract.setParameter("assetId", assetId);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(postcontract);
			// used to get response from ocean server
			String postcontractResp = postcontract.getResponseBodyAsString();
			// Convert the string into jsonobject
			String prepostToJson = postcontractResp.substring(1, postcontractResp.length() - 1);
			// replacing '\' with space
			String postcontactToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			json = (JSONObject) parser.parse(postcontactToJson);
			// set the set Asset id to the user object
			asset.setAssetId(json.get("assetId").toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	/**
	 * This method is used to get contract from alredy contracted stackholders
	 */

	public Asset getContract(URL url, String contractId) {
		String oceanUrl = url + keeperURL + "/contracts/contract/" + contractId;
		JSONObject resultObject = new JSONObject();
		Asset asset = new Asset();
		JSONObject json = null; // initialize the json object into null
		try {

			GetMethod getContract = new GetMethod(oceanUrl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(getContract);
			// used to get response from ocean server
			String getContractResp = getContract.getResponseBodyAsString();
			// Convert the string into jsonobject
			String prepostToJson = getContractResp.substring(1, getContractResp.length() - 1);
			// replacing '\' with space
			String postcontractToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			json = (JSONObject) parser.parse(postcontractToJson);
			// Set asset id into asset
			asset.setAssetId(json.get("assetId").toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	/**
	 * This method is used to sign the contract.
	 */
	public Asset signContract(URL url, String contractId, String signingActorId) {
		String oceanUrl = url + keeperURL + "/contracts/contract/" + contractId;
		Asset asset = new Asset();
		try {
			PostMethod postcontract = new PostMethod(oceanUrl);
			postcontract.setParameter("actorId", signingActorId);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(postcontract);
			// used to get response from ocean server
			String postcontractResp = postcontract.getResponseBodyAsString();
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

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return asset;
	}

	/**
	 * This method is used to authorize the contract.
	 */
	public Asset authorizeContract(URL url, String contractId, String assetId) {
		String oceanUrl = url + keeperURL + "/contracts/contract/" + contractId + "/auth";
		Asset asset = new Asset();
		String updatedresponse = null;
		try {
			PutMethod put = new PutMethod(oceanUrl);
			HttpMethodParams httpmethod = new HttpMethodParams();
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
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return asset;
	}

	public Asset revokeContractAuthorization(URL url, Asset asset) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method is used to access Contract Asset.
	 */
	public Asset accessContractAsset(URL url, String contractId) {
		String oceanUrl = url + keeperURL + "/contracts/contract/" + contractId + "/access";
		Asset asset = new Asset();
		JSONObject resultObject = new JSONObject();
		JSONObject json = null; // initialize the json object into null
		try {

			GetMethod getContract = new GetMethod(oceanUrl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(getContract);
			// used to get response from ocean server
			String getContractResp = getContract.getResponseBodyAsString();
			// check the response from ocean network
			if (getContractResp == null) {

			}
			// Convert the string into jsonobject
			String prepostToJson = getContractResp.substring(1, getContractResp.length() - 1);
			// replacing '\' with space
			String accessContractToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			json = (JSONObject) parser.parse(accessContractToJson);
			// set the set Asset id to the user object
			asset.setAssetId(json.get("assetId").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	/**
	 * This method is used to settle Contract Asset.
	 */

	public Asset settleContract(URL url, String actorId, String contractId) {
		String oceanUrl = url + keeperURL + "/contracts/contract/" + contractId + "/settlement";
		Asset asset = new Asset();
		String updatedresponse = null;
		try {
			PutMethod put = new PutMethod(oceanUrl);
			HttpMethodParams httpmethod = new HttpMethodParams();
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

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return asset;
	}

	/**
	 * This method is used to add asset listing (Market asset end point)
	 */

	public Asset addAssetListing(URL url, String assetId, String publisherId) {
		String oceanUrl = url + keeperURL + "/market/asset/" + publisherId;
		JSONObject json = null; // initialize the json object into null
		Asset asset = new Asset();
		try {

			PostMethod postcontract = new PostMethod(oceanUrl);
			// insert asset publisherId to the json object
			postcontract.setParameter("publisherId", publisherId);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(postcontract);
			// used to get response from ocean server
			String postcontractResp = postcontract.getResponseBodyAsString();
			// Convert the string into jsonobject
			String prepostToJson = postcontractResp.substring(1, postcontractResp.length() - 1);
			// replacing '\' with space
			String postcontactToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			json = (JSONObject) parser.parse(postcontactToJson);
			// Set asset id into asset
			asset.setAssetId(json.get("assetId").toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

}
