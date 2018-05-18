package com.oceanprotocolclient.user;

import java.util.Random;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;

import com.oceanprotocolclient.interfaces.UserInterface;

public class UserController implements UserInterface {

	@Value("${targethost}")
	private String targetHost = "http://20.188.98.205:8000";

	@Value("${userUrl}")
	private String userUrl = "/api/v1/keeper/actors/actor/";

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
			if (postResp == null) {
				resultObject.put("result", "Post Response is not Present");
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
			resultObject.put("walletId", walletId);
			resultObject.put("privateKey", privateKey);
			resultObject.put("actorId", actorId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultObject;
	}

	@SuppressWarnings("unchecked")
	public JSONObject getActor(String actorId) {
		String getResp = null;
		JSONObject resultObject = new JSONObject();
		String targetUrl = targetHost + userUrl;
		try {
			String targetURL = targetUrl + actorId;
			GetMethod get = new GetMethod(targetURL);

			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(get);
			getResp = get.getResponseBodyAsString();
			System.out.println(getResp);
			if (getResp == null) {
				resultObject.put("result", "No Response From Server");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (JSONObject) resultObject.put("result", getResp);
	}

	@Override
	public JSONObject updateActor() {
		// TODO Auto-generated method stub
		return null;
	}

}
