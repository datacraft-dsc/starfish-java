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
import java.net.URL;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.oceanprotocolclient.interfaces.AssetsInterface;
import com.oceanprotocolclient.model.Asset;
import com.oceanprotocolclient.model.User;

@SuppressWarnings("deprecation")
public class Assetcontroller implements AssetsInterface {
	
	@Autowired
	Environment env;
	
	
	
	/**
	 * This method used to register an asset Json-encoded payload containing the
	 * Asset schema with the assetId, creationDatetime and contentState filled
	 * in.
	 * 
	 * Minimum required: name, publisherId
	 * 
	 * @param publisherId
	 *            - publisher id
	 * @param name
	 *            - publisher name
	 * @param targetUrl
	 *            - target URL * @return java object asset
	 */

	@Override
	public Asset assetRegistration(URL url, String publisherId, String assetName) {
		String oceanUrl = url + env.getProperty("keeperURL") + "/assets/metadata";
		// Asset object creation
		Asset assets = new Asset(); 
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
			httpclient.executeMethod(postasset);// post data to a url
			postAssetResp = postasset.getResponseBodyAsString(); // got response
																	// here
			// check wether the ocean network response is empty or not
			if (postAssetResp == null) {
				return assets;
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
	 * It is used to get the asset response from ocean network
	 * 
	 * @param targetUrl
	 * 
	 * @return "
	 */
	@Override
	public Asset getAsset(URL url, String assetId) {
		String oceanUrl = url + env.getProperty("keeperURL") + "/assets/metadata/" + assetId;
		Asset assets = new Asset(); // asset object creation
		JSONObject json = null; // initialize the json object into null
		try {

			GetMethod get = new GetMethod(oceanUrl);
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
	 * Update the asset by using the given details PUT parametes targetUrl,asset
	 * 
	 * @return assets
	 *
	 */
	@Override
	public Asset updateAssets(URL url, String assetId, String assetName) {
		String oceanUrl = url + env.getProperty("keeperURL") + "/assets/metadata/" + assetId;
		ResponseEntity<String> updatedresponse;
		Asset asset = new Asset();
		try {
			RestTemplate restTemplate = new RestTemplate();
			// setting the headers for the url
			HttpHeaders headers = new HttpHeaders();
			// content-type setting
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			// create a json object to accept the asset name
			JSONObject assetupdation = new JSONObject();
			// insert asset name to the json object
			assetupdation.put("name", assetName);
			// create and http entity to attach with the rest url
			org.springframework.http.HttpEntity<JSONObject> entity = new org.springframework.http.HttpEntity<>(
					assetupdation, headers);
			// sent data request fro update data to ocean network
			 updatedresponse = restTemplate.exchange(oceanUrl, HttpMethod.PUT, entity, String.class);
			
			String prepostToJson = updatedresponse.getBody().substring(1, updatedresponse.getBody().length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String updateAssetToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(updateAssetToJson);
			// set the setAssetname to the user object
			asset.setAssetname(json.get("name").toString());
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
	@Override
	public Asset uploadAsset(URL url, File file, String assetId) {
		String uploadassetResp = null;
		String oceanUrl = url + env.getProperty("providerURL") + "/assets/asset/" + assetId;
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
	@Override
	public Asset downloadAsset(URL url, String assetId) {
		String oceanUrl = url + env.getProperty("providerURL") + "/assets/asset/" + assetId;
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
	@Override
	public Asset disableAssets(URL url, String assetId, String assetName,String actorId) {
		String oceanUrl = url + env.getProperty("keeperURL") + "/metadata/" + assetId;
		Asset asset = new Asset();
		ResponseEntity<String> disableAssetResponse = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			// setting the headers for the url
			HttpHeaders headers = new HttpHeaders();
			// content-type setting
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			// create a json object to accept the asset name
			JSONObject assetDisabled = new JSONObject();
			// insert asset name to the json object
			assetDisabled.put("requestor_actor_id", actorId);
			// create and http entity to attach with the rest url
			org.springframework.http.HttpEntity<JSONObject> entity = new org.springframework.http.HttpEntity<>(
					assetDisabled, headers);
			// sent data request fro delete asset from ocean network
			disableAssetResponse = restTemplate.exchange(oceanUrl, HttpMethod.DELETE, entity, String.class);
			String prepostToJson = disableAssetResponse.getBody().substring(1, disableAssetResponse.getBody().length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String diabledAssetToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(diabledAssetToJson);
			// set the setAssetname to the user object
			asset.setAssetname(json.get("name").toString());
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
	@Override
	public Asset getAssets(URL url, String assetId) {
		String oceanUrl = url + env.getProperty("keeperURL") + "/metadata/";
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
			asset.setAssetname(json.get("name").toString());
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
	@Override
	public Asset addAssetProvider(URL url, String actorId, String assetId) {
		String oceanUrl = url + env.getProperty("keeperURL") + "/assets/provider/";
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
	@Override
	public Asset addContract(URL url, String assetId) {
		String oceanUrl = url + env.getProperty("keeperURL") + "/contracts/contract/";
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

	@Override
	public Asset getContract(URL url, String contractId) {
		String oceanUrl = url + env.getProperty("keeperURL") + "/contracts/contract/"+contractId;
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
	@Override
	public Asset signContract(URL url, String contractId, String signingActorId) {
		String oceanUrl = url + env.getProperty("keeperURL") + "/contracts/contract/"+contractId;
		Asset asset = new Asset();
		ResponseEntity<String> signedContractResponse = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			// setting the headers for the url
			HttpHeaders headers = new HttpHeaders();
			// content-type setting
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			// create a json object to accept the asset name
			JSONObject contract = new JSONObject();
			// insert asset name to the json object
			contract.put("actor_id", signingActorId);
			// create and http entity to attach with the rest url
			org.springframework.http.HttpEntity<JSONObject> entity = new org.springframework.http.HttpEntity<>(contract,
					headers);
			// sent data request fro delete asset from ocean network
			signedContractResponse = restTemplate.exchange(oceanUrl, HttpMethod.PUT, entity, String.class);
			
			String prepostToJson = signedContractResponse.getBody().substring(1, signedContractResponse.getBody().length() - 1);
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
	@Override
	public Asset authorizeContract(URL url, String contractId, String assetId) {
		String oceanUrl = url + env.getProperty("keeperURL") + "/contracts/contract/"+contractId+"/auth";
		Asset asset = new Asset();
		ResponseEntity<String> authorizeContractResponse = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			// setting the headers for the url
			HttpHeaders headers = new HttpHeaders();
			// content-type setting
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			// create a json object to accept the asset name
			JSONObject contract = new JSONObject();
			// insert asset contractid to the json object
			contract.put("contractid", contractId);
			// insert asset assetid to the json object
			contract.put("assetid", assetId);
			// create and http entity to attach with the rest url
			org.springframework.http.HttpEntity<JSONObject> entity = new org.springframework.http.HttpEntity<>(contract,
					headers);
			// sent data request fro delete asset from ocean network
			authorizeContractResponse = restTemplate.exchange(oceanUrl, HttpMethod.PUT, entity, String.class);
			
			String prepostToJson = authorizeContractResponse.getBody().substring(1, authorizeContractResponse.getBody().length() - 1);
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

	@Override
	public Asset revokeContractAuthorization(URL url, Asset asset) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method is used to access Contract Asset.
	 */
	@Override
	public Asset accessContractAsset(URL url, String contractId) {
		String oceanUrl = url + env.getProperty("keeperURL") + "/contracts/contract/"+contractId+"/access";
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

	@Override
	public Asset settleContract(URL url, String actorId,String contractId) {
		String oceanUrl = url + env.getProperty("keeperURL") + "/contracts/contract/"+contractId+"/settlement";
		Asset asset = new Asset();
		ResponseEntity<String> settleContractResponse = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			// setting the headers for the url
			HttpHeaders headers = new HttpHeaders();
			// content-type setting
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			// create a json object to accept the contract
			JSONObject contract = new JSONObject();
			// insert actorId to the json object
			contract.put("actorId", actorId);
			// create and http entity to attach with the rest url
			org.springframework.http.HttpEntity<JSONObject> entity = new org.springframework.http.HttpEntity<>(contract,
					headers);
			// sent data request fro delete asset from ocean network
			settleContractResponse = restTemplate.exchange(oceanUrl, HttpMethod.PUT, entity, String.class);
			
			String prepostToJson = settleContractResponse.getBody().substring(1, settleContractResponse.getBody().length() - 1);
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
	@Override
	public Asset addAssetListing(URL url, String assetId, String publisherId) {
		String oceanUrl = url + env.getProperty("keeperURL") + "/market/asset/"+assetId;
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
