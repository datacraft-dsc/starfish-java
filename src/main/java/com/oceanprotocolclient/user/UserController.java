/**
 *  
 * To handle API Calls from User Side 
 * This should take actorId... as parameters
 * This data should be returned 
 * For registering an user.
 * url to register an user   :  http://host:8000/api/v1/keeper/users/user/
 * paramter :actorId
 * Author : Athul (Uvionics Tec)
 */

package com.oceanprotocolclient.user;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.oceanprotocolclient.interfaces.UserInterface;
import com.oceanprotocolclient.model.User;

public class UserController implements UserInterface {


	/**
	 * 
	 * @param actorId
	 * @return JSONObject
	 * 
	 *         Posting a url and return result into user Registration
	 */

	public User userRegistration(String actorId,String targetUrl) {
		User user = new User();
		String postResp = null;
		try {
			/**
			 * Used for generating a random Alphabet and add to ActorId
			 */
			Random rnd = new Random();
			char c = (char) (rnd.nextInt(26) + 'a');
			String s = Character.toString(c);
			actorId = s + actorId;

			/**
			 * Used for posting the targetUrl
			 */
			PostMethod post = new PostMethod(targetUrl);
			post.setParameter("actorId", actorId);// set Parameter actorId
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(post);
			postResp = post.getResponseBodyAsString();
			/**
			 * Used for return a Json Object with failed result and status
			 */
			if (postResp == null) {
				return user;
			}
			/**
			 * Used for getting WalletId and PrivateKey
			 */
			String prepostToJson = postResp.substring(1, postResp.length() - 1);
			String postToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(postToJson);
			System.out.println(json);
			String walletId = (String) json.get("defaultWalletAddress");
			user.setWalletId(walletId);
			String privateKey = (String) json.get("privateKey");
			user.setPrivateKey(privateKey);
			user.setActorId(actorId);
			user.setUpdateDatetime( json.get("updateDatetime").toString());
			user.setState((String) json.get("state"));
			user.setCreationDatetime(json.get("creationDatetime").toString());
			/**
			 * Used for adding WalletId, PrivateKey,actorId and status to Json
			 * Object
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * 
	 * @param actorId
	 * @return JSONObject
	 * 
	 *         Posting a url and return result into user Registration
	 */

	public User getActor(String actorId,String targetUrl) {
		User user = new User();
		String getResp = null;
		
		/**
		 * Used for posting the targetUrl
		 */
		try {
			String targetURL = targetUrl + actorId;
			GetMethod get = new GetMethod(targetURL);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(get);
			getResp = get.getResponseBodyAsString();
			/**
			 * Used for return a Json Object with failed result and status
			 */
			if (getResp == null) {
				return user;
			}
			/**
			 * Used for getting WalletId and PrivateKey
			 */
			String prepostToJson = getResp.substring(1, getResp.length() - 1);
			String postToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(postToJson);
			System.out.println(json);
			user.setWalletId( json.get("defaultWalletAddress").toString());
			user.setActorId( json.get("actorId").toString());
			user.setUpdateDatetime( json.get("updateDatetime").toString());
			user.setState((String) json.get("state"));
			user.setCreationDatetime(json.get("creationDatetime").toString());
		} catch (Exception e) {
		}
		/**
		 * Used for adding response and status
		 */
		return user;
	}

	/**
	 * To update a user based on the given the details
	 * 
	 * @param actorId
	 * @param user
	 * @return
	 *
	 */
	@Override
	public ResponseEntity<Object> updateActor(String targetUrl, String name) {

		try {
			RestTemplate restTemplate = new RestTemplate();
			// setting the headers for the url
			HttpHeaders headers = new HttpHeaders();
			// content-type setting
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			// create a json object to accept the user name
			JSONObject userName = new JSONObject();
			//insert user name to the json object
			userName.put("name", name);

			// create and http entity to attach with the rest url
			HttpEntity<JSONObject> entity = new HttpEntity<>(userName, headers);;
			ResponseEntity<Object> response = restTemplate.exchange(targetUrl, HttpMethod.PUT, entity, Object.class);
			
			return response;

		

	} catch (Exception ex) {
		ex.printStackTrace();

	}

	return null;
	}
}
