package com.oceanprotocolclient.interfaces;

import org.springframework.http.ResponseEntity;

import com.oceanprotocolclient.model.User;

public interface UserInterface {
	/**
	 * Used to registers an actor with the Ocean network
	 * 
	 * @param actorId - Id of an actor
	 * @param targetUrl -  Ocean network host and port 
	 * @return
	 */
	User userRegistration(String actorId,String targetUrl);
	/**
	 * Get actor information
	 * 
	 * @param actorId - Id of an actor
	 * @param targetUrl - Ocean network host and port 
	 * @return
	 */
	User getActor(String actorId,String targetUrl);
	/**
	 * Update actor name
	 * 
	 * @param targetUrl - Ocean network host and port 
	 * @param name - actor name
	 * @return
	 */
	ResponseEntity<Object> updateActor(String targetUrl, String name);
	/**
	 * 
	 * @param targetUrl - Ocean network host and port 
	 * @param id - To disable the actor
	 * @return
	 */
	ResponseEntity<Object> disableActor(String targetUrl, String id);
	}

