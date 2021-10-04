package com.gaggle.assessment_1.controllers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.gaggle.assessment_1.database_services.IRepositoryService;
import com.gaggle.assessment_1.exceptions.EntryNotFoundException;
import com.gaggle.assessment_1.exceptions.RepositoryException;
import com.gaggle.assessment_1.user_requests.UserRequestByID;
import com.gaggle.assessment_1.user_requests.UserRequestByName;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PostController {
	
	private static final String ID_NOT_FOUND_ERROR_STRING = "No Contact with that ID is available";
	private static final String ERROR_KEY = "Error";

	/*  NOTE: Due to the simple nature of the returned JSON objects I'm keeping it as a Hashmap
		Although if this were to go live with larger/more complicated JSON repies I would
		make each return have a class to better organize the data.
	*/
	
	private final IRepositoryService db;
	public PostController(final IRepositoryService db){
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
	@PostMapping(value = "/api/contact/add", consumes = "application/json", produces = "application/json")
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

	/**
	 * This is a method that is called when end users make a POST request.
	 * It will search the repository for a contact with a matching ID that 
	 * that they give in the form of a JSON.
	 * 
	 * It accepts JSONS in the following format:
	 * {"id": id_int }
	 * Where id_int is an interger of the id they are looking up.
	 * 
	 * It will return a HTTP code of 200 and a json in the following format:
	 * { "Contact": "name" }
	 * Where name is a string of the name of the contact with the given ID
	 * 
	 * If the ID can't be found then it returns an error and gives a HTTP code of 404.
	 * 
	 * If there is any other error it will return a HTTP code of 500, with an appropriate message. 
	 * 
	 * 	NOTE: I'm leaving this as is, but if there was a "next version" of this code I would 
	 *	edit this to allow a list of ID's to be searched at once sending back a matching list 
	 *	of names. This would limit the number API calls.
 	 *
	 * @param userRequest
	 * @return
	 */
	@PostMapping(value = "/api/contact/search/id", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> findContactByID(@RequestBody UserRequestByID userRequest) {
		try{
			String name = db.getNameByID(userRequest.getId());
			Map<String, String> map = new HashMap<>(); 
			map.put("Contact", name);
			return ResponseEntity.ok(map);
		}
		catch(EntryNotFoundException e){
			Map<String, String> map = new HashMap<>(); 
			map.put(ERROR_KEY, ID_NOT_FOUND_ERROR_STRING);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
		} catch (RepositoryException e) {
			Map<String, String> map = new HashMap<>(); 
			map.put(ERROR_KEY, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
	}

	
	/**
	 * This is a method that is called when end users make a POST request.
	 * It will search the repository a list of contacts containing the given substring. 
	 * 
	 * 
	 * It accepts JSONS in the following format:
	 * {"nameQuery": "query" }
	 * Where query is the substring they are searching for.
	 * 
	 * It will return a HTTP code of 200 and a json in the following format:
	 * { "Contacts": ["name1","name2","name3",...] }
	 * Where name1,2,3 etc are strings of the names containing the substring, listed in
	 * alphabetical order.
	 * 
	 * If no names can be found then it returns an empty list.
	 * 
	 * If there is any other error it will return a HTTP code of 500, with an appropriate message. 
	 * 
	 * @param userRequest
	 * @return
	 */
	@PostMapping(value = "/api/contact/search/name", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> searchContactNames(@RequestBody UserRequestByName userRequest) {
		try{
			ArrayList<String> contacts = db.searchBasedOnName(userRequest.getName(), userRequest.getMaxNames());
			Map<String, ArrayList<String>> map = new HashMap<>(); 
			map.put("Contact", contacts);
			return ResponseEntity.ok(map);
		}
		catch (RepositoryException e) {
			Map<String, String> map = new HashMap<>(); 
			map.put(ERROR_KEY, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
	}

}