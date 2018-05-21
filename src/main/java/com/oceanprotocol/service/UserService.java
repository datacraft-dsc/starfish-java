package com.oceanprotocol.service;

import org.springframework.http.ResponseEntity;

import com.oceanprotocol.model.User;

public interface UserService {

	User save(User person); //save user data

	User findByEmailAndPassword(String email,String password); //for login

	ResponseEntity<String> changePassword(String email, String currentPassword, String newPassword); //change password

	User getUserInformation(String email);//get user information using email
	
}