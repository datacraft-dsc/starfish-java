package com.oceanprotocolclient.interfaces;

import java.net.URL;
import com.oceanprotocolclient.model.User;

public interface UserInterface {
	/**
	 * Used to registers an actor with the Ocean network
	 * 
	 * @param actorId - Id of an actor
	 * @param targetUrl -  Ocean network host and port 
	 * @return
	 */
	User userRegistration(URL url,String actorId);
	/**
	 * Get actor information
	 * 
	 * @param actorId - Id of an actor
	 * @param targetUrl - Ocean network host and port 
	 * @return
	 */
	User getActor(URL url,String actorId);
	/**
	 * Update actor name
	 * 
	 * @param targetUrl - Ocean network host and port 
	 * @param name - actor name
	 * @return
	 */
	User updateActor(URL url, String actorId,String actorName);
	/**
	 * 
	 * @param targetUrl - Ocean network host and port 
	 * @param id - To disable the actor
	 * @return
	 */
	User disableActor(URL url, String actorId);
	}

