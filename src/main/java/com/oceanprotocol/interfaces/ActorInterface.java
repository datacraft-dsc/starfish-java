package com.oceanprotocol.interfaces;

import java.net.URL;

import com.oceanprotocol.model.Actor;

public interface ActorInterface {
	/**
	 * Used to registers an actor with the Ocean network
	 * 
	 * @param url -  Ocean network host and port 
	 * @param actorId - Actor Id
	 * @return
	 */
	Actor actorRegistration(URL url,String actorId);
	/**
	 * Get actor information
	 * 
	 * @param url - Ocean network host and port 
	 * @param actorId - Actor Id
	 * @return
	 */
	Actor getActor(URL url,String actorId);
	/**
	 * To update the actor
	 * 
	 * @param url - Ocean network host and port 
	 * @param actorId - Actor Id
	 * @param actorName - Actor Actor Name
	 * @return
	 */
	Actor updateActor(URL url, String actorId,String actorName);
	/**
	 * To disable the actor
	 * 
	 * @param url - Ocean network host and port 
	 * @param id - actorId
	 * @return
	 */
	Actor disableActor(URL url, String actorId);
	}

