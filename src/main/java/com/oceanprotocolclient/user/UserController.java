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

import java.util.Random;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import com.oceanprotocolclient.interfaces.UserInterface;

public class UserController implements UserInterface {

	@Value("${targethost}")
	private String targetHost = "http://20.188.98.205:8000";

	@Value("${userUrl}")
	private String userUrl = "/api/v1/keeper/actors/actor/";

	/**
	 * 
	 * @param actorId
	 * @return JSONObject
	 * 
	 *         Posting a url and return result into user Registration
	 */

	@SuppressWarnings("unchecked")
	public JSONObject userRegistration(String actorId) {
		JSONObject resultObject = new JSONObject();
		String targetUrl = targetHost + userUrl;
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
				resultObject.put("failedResult", "Post Response is not Present");
				resultObject.put("status", 0);
				return resultObject;
			}
			/**
			 * Used for getting WalletId and PrivateKey
			 */
			String prepostToJson = postResp.substring(1, postResp.length() - 1);
			String postToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(postToJson);
			String walletId = (String) json.get("defaultWalletAddress");
			String privateKey = (String) json.get("privateKey");
			/**
			 * Used for adding WalletId, PrivateKey,actorId and status to Json
			 * Object
			 */
			resultObject.put("walletId", walletId);
			resultObject.put("privateKey", privateKey);
			resultObject.put("actorId", actorId);
			resultObject.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultObject;
	}

	/**
	 * 
	 * @param actorId
	 * @return JSONObject
	 * 
	 *         Posting a url and return result into user Registration
	 */

	@SuppressWarnings("unchecked")
	public JSONObject getActor(String actorId) {
		String getResp = null;
		JSONObject resultObject = new JSONObject();
		String targetUrl = targetHost + userUrl;
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
				resultObject.put("failedResult", "No Response From Server");
				resultObject.put("status", 0);
				return resultObject;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * Used for adding response and status
		 */
		resultObject.put("result", getResp);
		resultObject.put("status", 1);
		return resultObject;
	}

	@Override
	public JSONObject updateActor() {
		return null;
	}
}
