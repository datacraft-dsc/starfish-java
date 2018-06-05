package com.oceanprotocol.client;

import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.oceanprotocol.model.Actor;
import com.oceanprotocol.model.MessageHandler;

public class SessionController {

	public static final String actorURL = "/api/v1/keeper/actors/actor";

	/**
	 * This method registers an actor with the Ocean network. POST
	 * "/api/v1/keeper/actors/actor/"
	 * 
	 * @Param actorId
	 * @Param url
	 * @return actor object
	 */

	public Actor actorRegistration(URL url, String actorId) {
		// Create object for actor class..it include all actor details
		Actor actor = new Actor();
		MessageHandler oceanresponse = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null || actorId == null) {
			throw new NullPointerException();
		}
		String oceanurl = actorURL;
		String postActorResp = null;
		// Initialize postResp - response from ocean network is given to this
		// variable
		try {
			/**
			 * Used for posting the data to ocean network
			 */
			PostMethod postActor = new PostMethod(oceanurl);
			// set Parameter actorId
			postActor.setParameter("actorId", actorId);
			HttpClient httpclient = new HttpClient();
			// sent the parameters to ocean network
			httpclient.executeMethod(postActor);
			// Response from ocean network
			postActorResp = postActor.getResponseBodyAsString();
			String prepostToJson = postActorResp.substring(1, postActorResp.length() - 1);
			// Data coming from ocean network is a json string..
			// This line remove the "\\" from the response
			String postactorResponseToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser(); // create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(postactorResponseToJson);
			// set the result json to the actor object
			actor.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actor;
	}

	/**
	 * This method used to fetch the actor information from ocean network GET
	 * "/api/v1/keeper/actors/actor/<actor_id>" This should take actorId along
	 * with url
	 * 
	 * @Param actorId
	 * @Param url
	 * @return actor object
	 */

	public Actor getActor(URL url, String actorId) {
		// Create object for actor class..it include all actor details
		Actor actor = new Actor();
		// Checks the argument values is present or not
		if (url == null || actorId == null) {
			throw new NullPointerException();
		}
		String oceanurl = actorURL + actorId;
		String getActorResp = null;
		/**
		 * Used for getting the data to ocean network
		 */
		try {
			GetMethod getActor = new GetMethod(oceanurl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(getActor);
			// Response from ocean network
			getActorResp = getActor.getResponseBodyAsString();
			String prepostToJson = getActorResp.substring(1, getActorResp.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String getActorToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(getActorToJson);
			// set the result json to the actor object
			actor.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actor;
	}

	/**
	 * JSON-encoded key-value pairs from the Actor schema that are allowed to be
	 * updated (only 'name' and 'attributes')
	 * 
	 * @param targetUrl
	 * @param name
	 * @return updatedresponse
	 *
	 */
	public Actor updateActor(URL url, String actorId, String actorName) {
		// Create object for actor class..it include all actor details
		Actor actor = new Actor();
		MessageHandler oceanresponse = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null || actorId == null || actorName == null) {
			throw new NullPointerException();
		}
		String oceanurl = actorURL + actorId;
		String updatedresponse = null;
		try {
			PutMethod put = new PutMethod(oceanurl);
			HttpMethodParams httpmethod = new HttpMethodParams();
			httpmethod.setParameter("name", actorName);
			put.setParams(httpmethod);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(put);
			// got response from ocean network
			updatedresponse = put.getResponseBodyAsString();
			String prepostToJson = updatedresponse.substring(1, updatedresponse.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String getActorToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(getActorToJson);
			// set the result json to the actor object
			actor.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actor;
	}

	/**
	 * This method is used to disable the actor.
	 * 
	 * @param targetUrl
	 * @param name
	 * @return response
	 */
	public Actor disableActor(URL url, String actorId) {
		// Create object for actor class..it include all actor details
		Actor actor = new Actor();
		MessageHandler oceanresponse = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null || actorId == null) {
			throw new NullPointerException();
		}
		String oceanurl = actorURL + actorId;
		String deletedresponse = null;
		try {
			DeleteMethod delete = new DeleteMethod(oceanurl);
			HttpClient httpclient = new HttpClient();
			httpclient.executeMethod(delete);
			// got response from ocean network
			deletedresponse = delete.getResponseBodyAsString();
			String predeleteToJson = deletedresponse.substring(1, deletedresponse.length() - 1);
			// Data coming from ocean network is a json string..This line remove
			// the "\\" from the response
			String getActorToJson = predeleteToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser();// create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(getActorToJson);
			// set the result json to the actor object
			actor.getOceanResponse().put("result", json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actor;
	}
}
