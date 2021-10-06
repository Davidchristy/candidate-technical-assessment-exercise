package com.gaggle.assessment_1.controllers;

import java.util.HashMap;
import java.util.Map;

import com.gaggle.assessment_1.database_services.IRepositoryService;
import com.gaggle.assessment_1.exceptions.RepositoryException;
import com.gaggle.assessment_1.user_requests.UserRequestByName;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class PutController {

	private static final String ERROR_KEY = "Error";
	
	private final IRepositoryService db;
	public PutController(final IRepositoryService db){
		this.db = db;
	}

    /**
	 * This is a method that is called when end users make a POST request.
	 * It will add a contact to the repository. 
	 * 
	 * 
	 * It accepts JSONS in the following format:
	 * { "Contact": "name" }
	 * Where name is a string of the name of the contact they are adding.
	 * 
	 * If no error it returns a blank 200 reply
	 * 
	 * If any error it returns a 500 error with an approate message.
	 * 
	 * Note: This will allow contacts of the same name.
 	 *
	 * @param userRequest
	 * @return
	 */
	@PutMapping(value = "/api/contact/", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> addNewContact(@RequestBody UserRequestByName userRequest) {
		try{
			db.addContact(userRequest.getName());
			return ResponseEntity.ok().build();
		}
		catch (RepositoryException e) {
			Map<String, String> map = new HashMap<>(); 
			map.put(ERROR_KEY, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
	}
}
