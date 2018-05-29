/**
 *  
 * To handle API Calls from User Side 
 * This should take actorId... as parameters
 * This data should be returned 
 * For registering an user.
 * url to register an user   :  http://host:8000/api/v1/keeper/users/user/
 * paramter :actorId
 * Author : Aleena , Athul ,Arun (Uvionics Tec)
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
	 *         Registers an actor with the Ocean network. POST
	 *         "/api/v1/keeper/actors/actor/"
	 */

	public User userRegistration(String actorId, String targetUrl) {
		// Create object for user class..it include all user details
		User user = new User();
		// Initialize postResp - response from ocean network is given to this
		// variable
		String postActorResp = null;
		try {
			// Used for generating a random Alphabet and add to ActorId
			// Random rnd = new Random();
			// char c = (char) (rnd.nextInt(26) + 'a');
			// String s = Character.toString(c);
			// actorId = s + actorId;
			/**
			 * Used for posting the data to ocean network
			 */
			PostMethod postActor = new PostMethod(targetUrl);
			postActor.setParameter("actorId", actorId);// set Parameter actorId
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(postActor);
			// Response from ocean network
			postActorResp = postActor.getResponseBodyAsString();

			/**
			 * Used for return a Json Object with failed result and status
			 */
			if (postActorResp == null) {
				return user;
			}
			/**
			 * Used for getting WalletId and PrivateKey
			 */
			String prepostToJson = postActorResp.substring(1, postActorResp.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String postactorResponseToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser(); // create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(postactorResponseToJson);
			// get the wallet address from ocean network response
			String walletId = (String) json.get("defaultWalletAddress");
			// set the wallet id to the user object
			user.setWalletId(walletId);
			// get the private key from ocean network response
			String privateKey = (String) json.get("privateKey");
			// set the private key to the user object
			user.setPrivateKey(privateKey);
			// set the actorid to the user object
			user.setActorId(actorId);
			// set the updateDatetime to the user object
			user.setUpdateDatetime(json.get("updateDatetime").toString());
			// set the user state to the user object
			user.setState((String) json.get("state"));
			// set the creationDatetime to the user object
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
	 *         This method used to get the actor details from ocean network GET
	 *         "/api/v1/keeper/actors/actor/<actor_id>"
	 */

	public User getActor(String actorId, String targetUrl) {
		// Create object for user class..it include all user details
		User user = new User();
		// Initialize postResp - response from ocean network is given to this
		// variable
		String getActorResp = null;
		/**
		 * Used for getting the data to ocean network
		 */
		try {
			String targetURL = targetUrl + actorId;
			GetMethod getActor = new GetMethod(targetURL);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(getActor);
			// Response from ocean network
			getActorResp = getActor.getResponseBodyAsString();
			/**
			 * Used for return a Json Object with failed result and status
			 */
			if (getActorResp == null) {
				return user;
			}
			/**
			 * Used for getting WalletId and PrivateKey
			 */
			String prepostToJson = getActorResp.substring(1, getActorResp.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String getActorToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(getActorToJson);
			// set the wallet id to the user object
			user.setWalletId(json.get("defaultWalletAddress").toString());
			// set the actor id to the user object
			user.setActorId(json.get("actorId").toString());
			// set the updateDatetime to the user object
			user.setUpdateDatetime(json.get("updateDatetime").toString());
			// set the state to the user object
			user.setState((String) json.get("state"));
			// set the creationDatetime to the user object
			user.setCreationDatetime(json.get("creationDatetime").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * JSON-encoded key-value pairs from the Actor schema 
	 * that are allowed to be updated (only 'name' and 'attributes')
	 * 
	 * @param targetUrl
	 * @param name
	 * @return updatedresponse
	 *
	 */
	@Override
	public ResponseEntity<Object> updateActor(String targetUrl, String name) {
		ResponseEntity<Object> updatedresponse = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			// setting the headers for the url
			HttpHeaders headers = new HttpHeaders();
			// content-type setting
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			// create a json object to accept the user name
			JSONObject userName = new JSONObject();
			// insert user name to the json object
			userName.put("name", name);
			// create and http entity to attach with the rest url
			HttpEntity<JSONObject> entity = new HttpEntity<>(userName, headers);
			// sent data to ocean network for updating the data
			updatedresponse = restTemplate.exchange(targetUrl, HttpMethod.PUT, entity, Object.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return updatedresponse;
	}

	/**
	 * This method is used to disable the actor.
	 * @param targetUrl
	 * @param name
	 * @return response
	 */
	@Override
	public ResponseEntity<Object> disableActor(String targetUrl, String id) {
		ResponseEntity<Object> updatedresponse = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			// setting the headers for the url
			HttpHeaders headers = new HttpHeaders();
			// content-type setting
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			// create a json object to accept the user name
			JSONObject userName = new JSONObject();
			// insert user name to the json object
			userName.put("actorId", id);
			// create and http entity to attach with the rest url
			HttpEntity<JSONObject> entity = new HttpEntity<>(userName, headers);
			// sent data to ocean network for disabling the user
			updatedresponse = restTemplate.exchange(targetUrl, HttpMethod.DELETE, entity, Object.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return updatedresponse;
	}
}
