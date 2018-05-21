/**
 *  
 * This class is used upload and download assets in the oceanprotocol
 * This should take file as parameters
 * This data sent to oceanprotocol url to upload assets.
 * url to register an user   :  http://host:8000//api/v1/provider/assets/asset/
 * paramter :file
 * Author : Aleena john (Uvionics Tec)
 */

package com.oceanprotocol.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.oceanprotocol.model.Assets;
import com.oceanprotocol.service.AssetService;
import com.oceanprotocolclient.assets.Assetcontroller;

@RestController
public class AssetController {
	@Value("${targethost}")
	private String targetHost;

	@Value("${userUrl}")
	private String userUrl;

	@Value("${assetUrl}")
	private String assetUrl;

	@Value("${assetupdownUrl}")
	private String assetupdownUrl;

	@Value("${publishimage}")
	private String publishimage;

	@Autowired
	AssetService assetService;

	@Autowired
	GridFsTemplate gridFsTemplate;

	JSONObject resultObject = null;
	Logger log = Logger.getLogger(AssetController.class);

	/**
	 * /assets/registerasset method used to register the assets before it
	 * uploads.
	 * 
	 * @param assets
	 * @param email
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */

	@SuppressWarnings({ "unchecked" })
	@PostMapping("/assets/registerasset")
	public ResponseEntity<?> assetRegistration(@RequestParam("assets") String assets, String email)
			throws IOException, ParseException {
		resultObject = new JSONObject();
		/**
		 * Used for casting the Json attribute to Java user Object
		 */
		Type assetType = new TypeToken<Assets>() {
		}.getType();
		Assets assetSave = new Gson().fromJson(assets, assetType);
		/**
		 * Used for Validation of mandatory Fields
		 */
		if (assetSave.getPublisherId() == null || assetSave.getAssetname() == null) {
			resultObject.put("result", "Asset Registration failed!!");
			resultObject.put("reason", "Publisher Id Not found");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}
		/**
		 * Used for posting the targetUrl
		 */

		String publisherId = assetSave.getPublisherId();
		String name = assetSave.getAssetname();
		/**************************************************************************/
		
		Assetcontroller assetController = new Assetcontroller();
		JSONObject json = assetController.assetsRegistration(publisherId, name);
		
		/**************************************************************************/
		int status = (int) json.get("status");
		Assets asset = null;
		if (status == 1) {
			JSONObject result = (JSONObject) json.get("result");
			assetSave.setAssetId((String) result.get("assetId"));
			asset = assetService.save(assetSave, email, "");
			DBObject metaData = new BasicDBObject();
			metaData.put("assetId", assetSave.getAssetId());
			resultObject.put("result", result);
			resultObject.put("Asset", asset);
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
		} else {
			String failedResult = (String) json.get("failedResult");
			resultObject.put("result", failedResult);
			resultObject.put("Asset", asset);
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Allow uploading a file for an already registered asset. The upload is
	 * submitted to the provider. Endpoint : POST
	 * "/api/v1/provider/assets/asset/"
	 * 
	 * @param assetId
	 * @param uploadfile
	 * @return
	 */

	@SuppressWarnings({ "unchecked" })

	@PostMapping("/assets/uploadasset/{assetId}")
	public @ResponseBody ResponseEntity<?> saveFile(@PathVariable("assetId") String assetId,
			@RequestParam("file") MultipartFile uploadfile) {
		resultObject = new JSONObject();
		if (uploadfile == null) {
			return new ResponseEntity<String>("please select a file!", HttpStatus.OK);
		}
		String filename = uploadfile.getOriginalFilename();
		File dir = new File(System.getProperty("user.dir") + "opt/tomcat/webapps/");
		dir.mkdirs();
		File convFile = new File(dir + "/" + filename); // server
		try {
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(uploadfile.getBytes());
			fos.close();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/**************************************************************************/
		
		Assetcontroller assetController = new Assetcontroller();
		JSONObject json = assetController.uploadAssest(assetId, convFile);
		
		/**************************************************************************/
		int status = (int) json.get("status");
		System.out.println(status);
		if (status == 1) {
			String result = (String) json.get("result");
			resultObject.put("Successfully uploaded - ", result);
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
		} else {
			String failedResult = (String) json.get("failedResult");
			resultObject.put("result", failedResult);
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Allow downloading the asset file from the provider. Endpoint : GET
	 * "/api/v1/provider/assets/asset/"
	 * 
	 * @param assetId
	 * @return
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/assets/downloadasset/{assetId}")
	public JSONObject downloadAssets(@PathVariable("assetId") String assetId) {
		resultObject = new JSONObject();
		/**************************************************************************/
		
		Assetcontroller assetController = new Assetcontroller();
		JSONObject json = assetController.downloadAssest(assetId);
		
		/**************************************************************************/
		int status = (int) json.get("status");
		if (status == 1) {
			String result = (String) json.get("result");
			Assets asset = assetService.setAssetStaus(assetId, 2);
			resultObject.put("result", result);
			resultObject.put("asset", asset);
			return resultObject;
		} else {
			String failedResult = (String) json.get("failedResult");
			resultObject.put("result", failedResult);
			return resultObject;
		}
	}

	/**
	 * This function used to "Get an Asset"
	 * 
	 * @param assetId
	 * @return
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/assets/getanasset/{assetId}")
	public ResponseEntity<JSONObject> getanAsset(@PathVariable("assetId") String assetId) {
		resultObject = new JSONObject();
		/**************************************************************************/
		
		Assetcontroller assetController = new Assetcontroller();
		JSONObject json = assetController.getAnAssests(assetId);
		
		/**************************************************************************/
		int status = (int) json.get("status");
		if (status == 1) {
			JSONObject result = (JSONObject) json.get("result");
			return new ResponseEntity<JSONObject>(result, HttpStatus.OK);
		} else {
			String failedResult = (String) json.get("result");
			resultObject.put("result", failedResult);
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Retrieve a get of assets that are available and active in the Ocean
	 * network. GET "/api/v1/keeper/assets/metadata"
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@GetMapping("/assets/getassets")
	public ResponseEntity<JSONObject> getAssets(String category, String type) throws HttpException, IOException {

		String targetUrl = targetHost + assetUrl;// combine target url assseturl

		resultObject = new JSONObject();

		JSONObject json = null;

		GetMethod get = new GetMethod(targetUrl);
		HttpClient httpclient = new HttpClient();
		httpclient.executeMethod(get);
		String getResp = get.getResponseBodyAsString(); // respose from ocean
														// server
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode data = objectMapper.readValue(getResp, ObjectNode.class);
		ArrayNode items = (ArrayNode) data.get("items");
		Iterator<JsonNode> a = items.iterator();
		List<JsonNode> objectNodes = new ArrayList<>();
		while (a.hasNext()) {
			JsonNode jsonNode = a.next();
			jsonNode = objectMapper.readTree(jsonNode.textValue());
			objectNodes.add(jsonNode);
		}

		ArrayNode array = objectMapper.valueToTree(objectNodes);
		data.replace("items", array);

		resultObject.put("result", data);
		List<JSONObject> object = assetService.findAllAssetswithImage(category, type);
		resultObject.put("result", object);
		return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
	}

	/**
	 * used to update the asset details PUT
	 * "/api/v1/keeper/actors/actor/<actor_id>" JSON-encoded key-value pairs
	 * from the Actor schema that are allowed to be updated (only 'name' and
	 * 'attributes')
	 * 
	 * @param actorId
	 * @param name
	 * @param email
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@PutMapping("/asset/updateasset/{assetId}")
	public ResponseEntity<JSONObject> updateAssets(@PathVariable("assetId") String actorId, Assets updatedasset,
			String email) throws HttpException, IOException {
		resultObject = new JSONObject();
		String targetUrl = targetHost + userUrl + actorId;// combine target url
															// assseturl

		resultObject = new JSONObject();
		Assets asset = assetService.updateAsset(updatedasset);

		asset = assetService.save(asset, email, ""); // save updated data

		Assets myGreeting = new Assets();
		myGreeting.setAssetname(updatedasset.getAssetname());
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.add("user-agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			org.springframework.http.HttpEntity<Assets> entity = new org.springframework.http.HttpEntity<Assets>(
					myGreeting, headers); // pass the information to update

			Object response = restTemplate.exchange(targetUrl, HttpMethod.PUT, entity, Object.class);
			if (response == null) {
				resultObject.put("result", "Response from server not found");
				return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		}

		return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);

	}

}