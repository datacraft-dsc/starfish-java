package com.oceanprotocolclient.interfaces;

import org.springframework.http.ResponseEntity;

import com.oceanprotocolclient.model.User;

public interface UserInterface {

	User userRegistration(String actorId,String targetUrl);
	User getActor(String actorId,String targetUrl);
	ResponseEntity<Object> updateActor(String targetUrl, String name);}

