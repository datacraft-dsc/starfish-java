/*****************************************************************************************************************************
 * ***************************************************************************************************************************
 * Ocean protocol client API used for connecting to ocean protocol using Java and Spring Boot.
 * UserController Class includes - User Registration
 * 								 - Get Actor
 * 								 - Update Actor
 *								 - Disable Actor
 * User Registration - This method registers an actor with the Ocean network. 
 * 					   POST "/api/v1/keeper/actors/actor/" 
 * 					   Parameter : actorId
 * Get Actor		 - This method used to fetch the actor information from ocean network
 * 			   		   GET "/api/v1/keeper/actors/actor/<actor_id>"
 * 					   This should take actorId along with url
 * Update Actor 	 - This method used to update the actor details
 * 				 	   PUT "/api/v1/keeper/actors/actor/<actor_id>"
 * 					   Parameter : Actor Name
 *					   This should take actorId along with url
 * Disable Actor	 - This method used to disable the actor.
 *					   DELETE "/api/v1/keeper/actors/actor/<actor_id>"
 *					   This should take actorId along with url
 *
 * Author : Aleena , Athul ,Arun (Uvionics Tec)
 * 
 * ********************************************************************************************************************************
 ***********************************************************************************************************************************/

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

import com.oceanprotocol.interfaces.ActorInterface;
import com.oceanprotocol.model.Actor;
import com.oceanprotocol.model.MessageHandler;

public class ActorController implements ActorInterface {
	public String actorURL = "/api/v1/keeper/actors/actor";

	/**
	 * This method registers an actor with the Ocean network. 
	 * POST "/api/v1/keeper/actors/actor/" 
	 *
	 * @Param url
	 * @Param actorId
	 * @return actor
	 */

	public Actor actorRegistration(URL url, String actorId) {
		// Create object for actor class..it include all actor details
		Actor actor = new Actor();
		MessageHandler oceanresponse = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null) {
			oceanresponse.setMessage("Host url not found");
			actor.setMessageHandler(oceanresponse);
			return actor;
		}
		if (actorId == null) {
			oceanresponse.setMessage("Actor Id not found");
			actor.setMessageHandler(oceanresponse);
			return actor;
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
			//sent the parameters to ocean network
			httpclient.executeMethod(postActor);
			// Response from ocean network
		    postActorResp = postActor.getResponseBodyAsString();
			/**
			 * Used for getting WalletId and PrivateKey
			 */
			String prepostToJson = postActorResp.substring(1, postActorResp.length() - 1);
			// Data coming from ocean network is a json string..
			//This line remove  the "\\" from the response
			String postactorResponseToJson = prepostToJson.replaceAll("\\\\", "");
			JSONParser parser = new JSONParser(); // create json parser
			// parse the data to json object
			JSONObject json = (JSONObject) parser.parse(postactorResponseToJson);
			// get the wallet address from ocean network response
			String walletId = (String) json.get("defaultWalletAddress");
			// set the wallet id to the actor object
			actor.setWalletId(walletId);
			// get the private key from ocean network response
			// set the private key to the actor object
			actor.getOceanResponse().put("privateKey", json.get("privateKey").toString());
			// set the actorid to the actor object
			actor.setActorId(actorId);
			// set the updateDatetime to the actor object
			actor.getOceanResponse().put("updateDatetime", json.get("updateDatetime").toString());
			// set the actor state to the actor object
			actor.getOceanResponse().put("state", json.get("state").toString());
			// set the creationDatetime to the actor object
			actor.getOceanResponse().put("creationDatetime", json.get("creationDatetime").toString());
		} catch (Exception e) {
			// returns the response if no values are present
			oceanresponse.setMessage(postActorResp);
			actor.setMessageHandler(oceanresponse);
			e.printStackTrace();
			return actor;
		}
		return actor;
	}

	/**
	 * This method used to fetch the actor information from ocean network
	 * GET "/api/v1/keeper/actors/actor/<actor_id>"
	 * This should take actorId along with url
	 * @Param url 
	 * @Param actorId
	 * @return actor
	 */

	public Actor getActor(URL url, String actorId) {
		// Create object for actor class..it include all actor details
		Actor actor = new Actor();
		MessageHandler oceanresponse = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null) {
			oceanresponse.setMessage("Host url not found");
			actor.setMessageHandler(oceanresponse);
			return actor;
		}
		if (actorId == null) {
			oceanresponse.setMessage("Actor Id not found");
			actor.setMessageHandler(oceanresponse);
			return actor;
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
			// set the wallet id to the actor object
			actor.setWalletId(json.get("defaultWalletAddress").toString());
			// set the actor id to the actor object
			actor.setActorId(json.get("actorId").toString());
			// set the updateDatetime to the actor object
			actor.getOceanResponse().put("updateDatetime", json.get("updateDatetime").toString());
			// set the state to the actor object
			actor.getOceanResponse().put("state", json.get("state").toString());
			// set the creationDatetime to the actor object
			actor.getOceanResponse().put("creationDatetime", json.get("creationDatetime").toString());
		} catch (Exception e) {
			// returns the response if no values are present
			oceanresponse.setMessage(getActorResp);
			actor.setMessageHandler(oceanresponse);
			e.printStackTrace();
			return actor;
		}
		return actor;
	}

	/**
	 * JSON-encoded key-value pairs from the Actor schema that are allowed to be
	 * updated (only 'name' and 'attributes')
	 * 
	 * @param url
	 * @param actorName
	 * @return actor
	 *
	 */
	public Actor updateActor(URL url, String actorId, String actorName) {
		// Create object for actor class..it include all actor details
		Actor actor = new Actor();
		MessageHandler oceanresponse = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null) {
			oceanresponse.setMessage("Host url not found");
			actor.setMessageHandler(oceanresponse);
			return actor;
		}
		if (actorId == null || actorName == null) {
			oceanresponse.setMessage("Actor Id or Actor Name not found");
			actor.setMessageHandler(oceanresponse);
			return actor;
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
			// set the wallet id to the actor object
			actor.setActorName(json.get("name").toString());
			// set the actor id to the actor object
			actor.setActorId(json.get("actorId").toString());
			// set the updateDatetime to the actor object
			actor.getOceanResponse().put("updateDatetime", json.get("updateDatetime").toString());
			// set the state to the actor object
			actor.getOceanResponse().put("state", json.get("state").toString());
			// set the creationDatetime to the actor object
			actor.getOceanResponse().put("creationDatetime", json.get("creationDatetime").toString());
		} catch (Exception e) {
			// returns the response if no values are present
			oceanresponse.setMessage(updatedresponse);
			actor.setMessageHandler(oceanresponse);
			e.printStackTrace();
			return actor;
    }
  return actor;
	}

	/**
	 * This method is used to disable the actor.
	 * 
	 * @param url
	 * @param actorId
	 * @return actor
	 */
	public Actor disableActor(URL url, String actorId) {
		// Create object for actor class..it include all actor details
		Actor actor = new Actor();
		MessageHandler oceanresponse = new MessageHandler();
		// Checks the argument values is present or not
		if (url == null) {
			oceanresponse.setMessage("Host url not found");
			actor.setMessageHandler(oceanresponse);
			return actor;
		}
		if (actorId == null) {
			oceanresponse.setMessage("Actor Id not found");
			actor.setMessageHandler(oceanresponse);
			return actor;
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
			// set the wallet id to the actor object
			actor.setActorName(json.get("name").toString());
			// set the actor id to the actor object
			actor.setActorId(json.get("actorId").toString());
			// set the updateDatetime to the actor object
			actor.getOceanResponse().put("updateDatetime", json.get("updateDatetime").toString());
			// set the state to the actor object
			actor.getOceanResponse().put("state", json.get("state").toString());
			// set the creationDatetime to the actor object
			actor.getOceanResponse().put("creationDatetime", json.get("creationDatetime").toString());
		} catch (Exception e) {
			// returns the response if no values are present
			oceanresponse.setMessage(deletedresponse);
			actor.setMessageHandler(oceanresponse);
			e.printStackTrace();
			return actor;
		}
		return actor;
	}
}
