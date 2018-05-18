package com.oceanprotocolclient.interfaces;

import org.json.simple.JSONObject;

public interface UserInterface {

	JSONObject userRegistration(String actorId);
	JSONObject getActor(String actorId);
	JSONObject updateActor();
}
