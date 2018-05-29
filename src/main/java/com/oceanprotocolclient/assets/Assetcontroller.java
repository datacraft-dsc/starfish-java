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
import com.oceanprotocolclient.model.User;

@SuppressWarnings("deprecation")
public class Assetcontroller implements AssetsInterface {
	/**
	 * This method used to register an asset Json-encoded payload containing the
	 * Asset schema with the assetId, creationDatetime and contentState filled
	 * in.
	 * 
	 * Minimum required: name, publisherId * @return java object asset
	 * @param  publisherId   - publisher id
	 * @param  name - publisher name
	 * @param targetUrl - target URL
	 * return asset
	 */

	@Override
	public Asset assetsRegistration(String publisherId, String name, String targetUrl) {
		Asset assets = new Asset(); // Asset object creation
		String postAssetResp = null; // Initialize the varibale
		// set parameters to PostMethod
		PostMethod postasset = new PostMethod(targetUrl);
		// set the parametre publisherId
		postasset.setParameter("publisherId", publisherId);
		// set the parametre name
		postasset.setParameter("name", name);
		HttpClient httpclient = new HttpClient();
		try {
			httpclient.executeMethod(postasset);// post data to a url
			postAssetResp = postasset.getResponseBodyAsString(); // got response here
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
	 * @param targetUrl
	 * 
	 * @return "
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
	 *  parametes targetUrl,asset
	 * 
	 * @return assets
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
	 * upload is submitted to the provider.
	 */

	@SuppressWarnings({ "resource", "unchecked" })
	@Override
	public JSONObject uploadAssest(File file, String targetUrl) {
		JSONObject uploadedassetObject = new JSONObject();
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
			uploadedassetObject.put("status", 0);
			uploadedassetObject.put("failedResult", "Get Response is not Present");
			return uploadedassetObject;

		}
		// Used for adding response and status to Json Object
		else {
			uploadedassetObject.put("status", 1);
			uploadedassetObject.put("result", file.getName());
			return uploadedassetObject;
		}
	}
	/**
	 * Allow downloading the asset file from the provider. GET "/api/v1/provider/assets/asset/"
	 */

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject downloadAssest(String targetUrl) {
		JSONObject downloadedassetObject = new JSONObject();
		String getResp = null;
		// Execute a get Method and get respose from ocean server
		try {
			GetMethod get = new GetMethod(targetUrl);
			// setting the headers for the url
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(get);
			//got response from ocean network
			getResp = get.getResponseBodyAsString();
			// Used for return a Json Object with failed result and status
			if (getResp == null) {
				downloadedassetObject.put("status", 0);
				downloadedassetObject.put("failedResult", "Get Response is not Present");
				return downloadedassetObject;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// Used for adding response and status to Json Object
		downloadedassetObject.put("result", getResp);
		downloadedassetObject.put("status", 1);

		return downloadedassetObject;
	}

	/**
	 * Method used to Delete the asset 
	 * "/api/v1/keeper/assets/metadata/{asset_id}" 
	 * parametes targetUrl,asset
	 */
	@Override
	public ResponseEntity<Object> disableAssets(String targetUrl, Asset asset) {
		ResponseEntity<Object> disableAssetResponse = null;
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
			disableAssetResponse = restTemplate.exchange(targetUrl, HttpMethod.DELETE, entity, Object.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return disableAssetResponse;
	}

	/**
	 * This method used to get all assets from ocean network
	 */
	@Override
	public JSONObject getAllAssets(String targetUrl) {
		JSONObject resultObject = new JSONObject();
		JSONObject json = null; // initialize the json object into null
		try {

			GetMethod get = new GetMethod(targetUrl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(get);
			// used to get response from ocean server
			String getResp = get.getResponseBodyAsString();
			// check the response from ocean network
			if (getResp == null) {
				resultObject.put("status", 0);
				resultObject.put("failedResult", "Get Response is not Present");
				return resultObject;
			}
			// Convert the string into jsonobject
			String prepostToJson = getResp.substring(1, getResp.length() - 1);
			// replacing '\' with space
			String postToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			json = (JSONObject) parser.parse(postToJson);
			// Set asset id into asset
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	
	/**
	 * This is used to add asset provider
	 */
	@Override
	public JSONObject addAssetProvider(String targetUrl, String actorId,Asset asset) {
		JSONObject assetProviderObject = new JSONObject();
		JSONObject json = null; // initialize the json object into null
		try {

			PostMethod postassetprovider = new PostMethod(targetUrl);
			// set the assetId
			postassetprovider.setParameter("assetId", asset.getAssetId());
			// set the providerId
			postassetprovider.setParameter("providerId", actorId);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(postassetprovider);
			// used to get response from ocean server
			String getAssetProviderResp = postassetprovider.getResponseBodyAsString();
			// check the response from ocean network
			if (getAssetProviderResp == null) {
				assetProviderObject.put("status", 0);
				assetProviderObject.put("failedResult", "Get Response is not Present");
				return assetProviderObject;
			}
			// Convert the string into jsonobject
			String prepostToJson = getAssetProviderResp.substring(1, getAssetProviderResp.length() - 1);
			// replacing '\' with space
			String postAssetProviderToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			json = (JSONObject) parser.parse(postAssetProviderToJson);
			// Set asset id into asset
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	/**
	 * This is used to create a contract
	 */
	@Override
	public JSONObject addContract(String contractUrlfrom, Asset asset) {
		JSONObject resultObject = new JSONObject();
		JSONObject json = null; // initialize the json object into null
		String contractUrl = contractUrlfrom+asset.getContractId();
		try {

			PostMethod postcontract = new PostMethod(contractUrl);
			postcontract.setParameter("assetId", asset.getAssetId());
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(postcontract);
			// used to get response from ocean server
			String postcontractResp = postcontract.getResponseBodyAsString();
			// check the response from ocean network
			if (postcontractResp == null) {
				resultObject.put("status", 0);
				resultObject.put("failedResult", "Get Response is not Present");
				return resultObject;
			}
			// Convert the string into jsonobject
			String prepostToJson = postcontractResp.substring(1, postcontractResp.length() - 1);
			// replacing '\' with space
			String postcontactToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			json = (JSONObject) parser.parse(postcontactToJson);
			// Set asset id into asset
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * This method is used to get  contract from alredy contracted stackholders
	 */

	@Override
	public JSONObject getContract(String contractUrlfrom, Asset asset) {
		JSONObject resultObject = new JSONObject();
		JSONObject json = null; // initialize the json object into null
		String contractUrl = contractUrlfrom+asset.getContractId();
		try {

			GetMethod getContract = new GetMethod(contractUrl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(getContract);
			// used to get response from ocean server
			String getContractResp = getContract.getResponseBodyAsString();
			// check the response from ocean network
			if (getContractResp == null) {
				resultObject.put("status", 0);
				resultObject.put("failedResult", "Get Response is not Present");
				return resultObject;
			}
			// Convert the string into jsonobject
			String prepostToJson = getContractResp.substring(1, getContractResp.length() - 1);
			// replacing '\' with space
			String postcontractToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			json = (JSONObject) parser.parse(postcontractToJson);
			// Set asset id into asset
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	/**
	 * This method is used to sign the contract.
	 */
	@Override
	public User signContract(String targetUrl, User user) {
		ResponseEntity<Object> signedContractResponse = null;
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
			contract.put("actor_id", user.getActorId());
			// create and http entity to attach with the rest url
			org.springframework.http.HttpEntity<JSONObject> entity = new org.springframework.http.HttpEntity<>(
					contract, headers);
			// sent data request fro delete asset from ocean network
			signedContractResponse = restTemplate.exchange(targetUrl, HttpMethod.PUT, entity, Object.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return user;
	}
	/**
	 * This method is used to authorize the contract.
	 */
	@Override
	public Asset authorizeContract(String targetUrl, Asset asset) {
		ResponseEntity<Object> authorizeContractResponse = null;
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
			contract.put("contractid", asset.getContractId()); 
			// insert asset assetid to the json object
			contract.put("assetid", asset.getAssetId());
			// create and http entity to attach with the rest url
			org.springframework.http.HttpEntity<JSONObject> entity = new org.springframework.http.HttpEntity<>(
					contract, headers);
			// sent data request fro delete asset from ocean network
			authorizeContractResponse = restTemplate.exchange(targetUrl, HttpMethod.PUT, entity, Object.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return asset;
	}

	@Override
	public Asset revokeContractAuthorization(String targetUrl, Asset asset) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * This method is used to access Contract Asset.
	 */
	@Override
	public JSONObject accessContractAsset(String contractUrlfrom, Asset asset) {
		JSONObject resultObject = new JSONObject();
		JSONObject json = null; // initialize the json object into null
		String contractUrl = contractUrlfrom+asset.getContractId()+"access";
		try {

			GetMethod getContract = new GetMethod(contractUrl);
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
			String postcontractToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			json = (JSONObject) parser.parse(postcontractToJson);
			// Set asset id into asset
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	/**
	 * This method is used to settle Contract Asset.
	 */

	@Override
	public User settleContract(String targetUrl, User user) {
		ResponseEntity<Object> settleContractResponse = null;
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
			contract.put("actorId", user.getActorId());
			// create and http entity to attach with the rest url
			org.springframework.http.HttpEntity<JSONObject> entity = new org.springframework.http.HttpEntity<>(
					contract, headers);
			// sent data request fro delete asset from ocean network
			settleContractResponse = restTemplate.exchange(targetUrl, HttpMethod.PUT, entity, Object.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return user;
	}
	
	/**
	 * This method is used to add asset listing (Market asset end point)
	 */
	@Override
	public Asset addAssetListing(String marketUrlfrom, Asset asset) {
		JSONObject resultObject = new JSONObject();
		JSONObject json = null; // initialize the json object into null
		String contractUrl = marketUrlfrom+asset.getAssetId();
		try {

			PostMethod postcontract = new PostMethod(contractUrl);
			// insert asset assetId to the json object
			postcontract.setParameter("assetId", asset.getAssetId());
			// insert asset publisherId to the json object
			postcontract.setParameter("publisherId", asset.getPublisherId());
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(postcontract);
			// used to get response from ocean server
			String postcontractResp = postcontract.getResponseBodyAsString();
			// check the response from ocean network
			if (postcontractResp == null) {
				
			}
			// Convert the string into jsonobject
			String prepostToJson = postcontractResp.substring(1, postcontractResp.length() - 1);
			// replacing '\' with space
			String postcontactToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			// parse string to json object
			json = (JSONObject) parser.parse(postcontactToJson);
			// Set asset id into asset
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return asset;
	}

	

	

}
