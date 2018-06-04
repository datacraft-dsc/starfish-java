package com.oceanprotocol.interfaces;

import java.net.URL;

import com.oceanprotocol.model.Actor;

public interface ActorInterface {
	/**
	 * Used to registers an actor with the Ocean network
	 * 
	 * @param actorId - Id of an actor
	 * @param targetUrl -  Ocean network host and port 
	 * @return
	 */
	Actor actorRegistration(URL url,String actorId);
	/**
	 * Get actor information
	 * 
	 * @param actorId - Id of an actor
	 * @param targetUrl - Ocean network host and port 
	 * @return
	 */
	Actor getActor(URL url,String actorId);
	/**
	 * Update actor name
	 * 
	 * @param targetUrl - Ocean network host and port 
	 * @param name - actor name
	 * @return
	 */
	Actor updateActor(URL url, String actorId,String actorName);
	/**
	 * 
	 * @param targetUrl - Ocean network host and port 
	 * @param id - To disable the actor
	 * @return
	 */
	Actor disableActor(URL url, String actorId);
	}

