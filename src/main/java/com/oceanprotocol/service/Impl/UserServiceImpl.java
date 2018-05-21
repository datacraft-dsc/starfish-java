package com.oceanprotocol.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.oceanprotocol.model.User;
import com.oceanprotocol.repository.UserRepository;
import com.oceanprotocol.service.UserService;



@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepo;

	@Override
	public User save(User user) {
		User userResp = userRepo.save(user);
		return userResp;
		
	}

	@Override
	public User findByEmailAndPassword(String email,String password) {
		User user = userRepo.findByEmailAndPassword(email, password);
		return user;
	}

	@Override
	public ResponseEntity<String> changePassword(String email, String currentPassword, String newPassword) {
	 	User user = userRepo.findByEmail(email);
		if(!user.getPassword().equals(currentPassword)) {
			return new ResponseEntity<String>("CURRENT PASSWORD IS WRONG PLEASE TRY AGAIN", HttpStatus.OK);
		}
		else
		{
			user.setPassword(newPassword);
			userRepo.save(user);
			return new ResponseEntity<String>("PASSWORD CHANGED SUCCESSFULLY", HttpStatus.OK);

		}
	}

	@Override
	public User getUserInformation(String email) {
		
		
		return userRepo.findByEmail(email);
	}

	
	
}
