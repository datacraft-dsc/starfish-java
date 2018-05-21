/**
 *  
 * To register an user
 * This should take username password,dob,email,contact,walletId(optional)... as parameters
 * This data should be store into the mongo database .... The actorId sent to oceanprotocol url
 * For registering an user.
 * url to register an user   :  http://host:8000/api/v1/keeper/users/user/
 * paramter :actorId
 * Author : Aleena john (Uvionics Tec)
 */

package com.oceanprotocol.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.oceanprotocol.model.User;
import com.oceanprotocol.repository.UserRepository;
import com.oceanprotocol.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserRepository userRepo;

	@Autowired
	UserService userService;

	@Autowired
	UserRegistration userRegistration;

	/**
	 * 
	 * @param parameter
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * 
	 *             Registering a passing to url ans save into user respository
	 */
	@SuppressWarnings({ "unchecked" })
	@PostMapping(path = "/actor/actorregistration")
	public ResponseEntity<?> userregistration(@RequestBody User user) throws IOException, ParseException {
		JSONObject resultObject = new JSONObject();
		/**
		 * Used for checking the Json attribute
		 */
		if (user == null) {
			resultObject.put("result", "Registration Failed");
			resultObject.put("reason", "No Value Obtained");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}
		/**
		 * Used for casting the Json attribute to Java user Object
		 */
		User userSave = user;
		String actorId = user.getActorId();
		/**
		 * Used for function call to User Registration Rest Controller Function
		 */
		/**************************************************************************/
		
		com.oceanprotocolclient.user.UserController us = new com.oceanprotocolclient.user.UserController();
		JSONObject json = us.userRegistration(actorId);
		
		/**************************************************************************/
		int status = (int) json.get("status");
		if(status ==1){
			String walletId = (String) json.get("walletId");
			userSave.setWalletId(walletId);
			String privateKey = (String) json.get("privateKey");
			userSave.setPrivateKey(privateKey);
			String actorID = (String) json.get("actorId");
			userSave.setActorId(actorID);
		}
		else{
			String failedResult = (String) json.get("failedResult");
			resultObject.put("result", "Registration Failed");
			resultObject.put("reason",failedResult);
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}
		
		/**
		 * Used for Validation of mandatory Fields
		 */
		User u = userService.getUserInformation(userSave.getEmail());
		if (u != null) {
			resultObject.put("result", "Registration Failed");
			resultObject.put("reason", "Email already exists");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}

		if (userSave.getEmail() == null || userSave.getPassword() == null || userSave.getContact() == null
				|| userSave.getUserName() == null) {
			resultObject.put("result", "Registration Failed");
			resultObject.put("reason", "Mandtory Field/Fields Are Missing");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}

		/**
		 * Used for saving User
		 */
		User userResp = userService.save(userSave);
		/**
		 * Used for giving response to the User
		 */
		if (userResp != null) {
			resultObject.put("result", "Successfully Registered");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
		} else {
			resultObject.put("result", "Registration Failed");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * 
	 * @param parameter
	 *            email,password
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(path = "/actorlogin")
	public ResponseEntity<?> userlogin(@RequestBody User userdata) throws IOException, ParseException {
		JSONObject resultObject = new JSONObject();
		/**
		 * used to fetch user by email and password for login
		 * 
		 */
		User user = userService.findByEmailAndPassword(userdata.getEmail(), userdata.getPassword());

		if (user != null) {
			resultObject.put("result", "Success");
			resultObject.put("actorId", user.getActorId());
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.OK);
		} else {
			resultObject.put("result", "Failed");
			return new ResponseEntity<JSONObject>(resultObject, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * 
	 * @param req
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * 
	 */

	@PostMapping(path = "/personlogout")
	public ResponseEntity<String> personlogout(HttpServletRequest req) throws IOException, ParseException {
		HttpSession session = req.getSession();
		session.invalidate();
		return new ResponseEntity<String>("Successfully Logged Out", HttpStatus.OK);
	}

	/**
	 * 
	 * @param req
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * 
	 */

	public ResponseEntity<?> saveCSVFile(@RequestParam("csv") ByteArrayInputStream csvFile) {

		return null;

	}

	/**
	 * change password
	 * 
	 * @param email
	 * @param currentPassword
	 * @param newPassword
	 * @return
	 */

	@PostMapping("/actor/changepassword")
	public ResponseEntity<String> changePassword(String email, @RequestParam("currentpassword") String currentPassword,
			@RequestParam("newpassword") String newPassword) {
		ResponseEntity<String> mesage = userService.changePassword(email, currentPassword, newPassword);
		if (mesage == null) {
			return new ResponseEntity<String>("unsuccessful", HttpStatus.BAD_REQUEST);
		}
		return mesage;
	}

	/**
	 * get actor information
	 * 
	 * @param email
	 * @return
	 */
	@GetMapping("/actor/information")
	public ResponseEntity<?> getUserInformation(@RequestParam String email) {
		User user = userService.getUserInformation(email);
		if (user == null) {
			return new ResponseEntity<String>("email not found", HttpStatus.BAD_REQUEST);
		}
		System.out.println(user);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

}
