
package com.oceanprotocol.controller;

import java.io.IOException;
import java.util.Arrays;


/**
 *  
 * To register an user in the oceanprotocol
 * This should take actorId as parameters
 * This data sent to oceanprotocol url to registering an user.
 * url to register an user   :  http://host:8000/api/v1/keeper/users/user/
 * paramter :actorId
 * Author : Aleena john (Uvionics Tec)
 */

import java.util.Random;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.oceanprotocol.model.User;
import com.oceanprotocol.service.UserService;

@RestController
public class UserRegistration {

	@Value("${targethost}")
	private String targetHost;

	@Value("${userUrl}")
	private String userUrl;

	@Autowired
	UserService userService;

	JSONObject resultObject = null;
	
	/**
	 * ACT.001 : Registering an Actor
	 * /api/v1/keeper/actors/actor
	 * TO register an user Pass actiorId and wallet Id into the url . But the
	 * payload is not mandatory.The system is now able to register an user
	 * irrespective of any payload.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/actors/registeractor")
	public JSONObject userRegistration(User user) throws ArrayIndexOutOfBoundsException {
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
			String actorId = s + user.getActorId();
			user.setActorId(actorId);
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
			String prepostToJson = postResp.substring(1, postResp.length()-1);
			String postToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser(); 
			JSONObject json = (JSONObject) parser.parse(postToJson);
			System.out.println(json);
			String walletId = (String) json.get("defaultWalletAddress");
			String privateKey = (String) json.get("privateKey");
			System.out.println(walletId);
			
			if (user.getWalletId() == null) {
				user.setWalletId(walletId);
			}
			if (user.getPrivateKey() == null) {
				user.setPrivateKey(privateKey);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		userService.save(user);
		resultObject.put("result", "Success");
		return resultObject;
	}

	/**
	 * ACT.002 : Get an Actor
	 * /api/v1/keeper/actors/actor/{actorId}
	 * To fetch the user
	 * 
	 * @param actorId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/actors/{actorId}")
	public ResponseEntity<?> getUser(@PathVariable("actorId") String actorId) {
		/**************************************************************************/
		
		com.oceanprotocolclient.user.UserController us = new com.oceanprotocolclient.user.UserController();
		JSONObject json = us.getActor(actorId);
		
		/**************************************************************************/
		int status = (int) json.get("status");
		if(status ==1){
			String result = (String) json.get("result");
			resultObject.put("result", result);
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
		}
		else{
			String failedResult = (String) json.get("failedResult");
			resultObject.put("result",failedResult);
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}
		
	}

	/**
	 * ACT.003 : Update an Actor
	 * /api/v1/keeper/actors/actor/{actorId}
	 * @param actorId
	 * @return
	 */
	@PutMapping("/actors/updateactor/{actorId}")
	public ResponseEntity<JSONObject> updateAssets(@PathVariable("actorId") String actorId,String name,String email) throws HttpException, IOException {
		resultObject = new JSONObject();
		String targetUrl = targetHost + userUrl+actorId;// combine target url assseturl

		resultObject = new JSONObject();
		User user = userService.getUserInformation(email);

		user.setName(name);
		user = userService.save(user);	   
	    System.out.println(user);
	 
	    User myGreeting = new User();
	    myGreeting.setName(name);
//	    com.oceanprotocolclient.user.UserController us = new com.oceanprotocolclient.user.UserController();
//		JSONObject json = us.updateActor();
//		System.out.println(json);
	    
	    try {
	        RestTemplate restTemplate = new RestTemplate();
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
	        HttpEntity<User> entity = new HttpEntity<User>(myGreeting, headers);

	        Object response = restTemplate.exchange(targetUrl, HttpMethod.PUT,entity,Object.class);
	        System.out.println(response);
	    } catch (Exception ex) {
	       ex.printStackTrace();

	    }
	    
		return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
		
	}
	
}
