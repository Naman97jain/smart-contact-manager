package com.smartcontact.manager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smartcontact.manager.entities.Contact;
import com.smartcontact.manager.entities.User;
import com.smartcontact.manager.repository.ContactRepo;
import com.smartcontact.manager.repository.UserRepo;

@RestController
public class SearchController {
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ContactRepo contactRepo;
	
	@GetMapping("/contact/search/{query}")
	public ResponseEntity<List<Contact>> searchContact(Principal principal, @PathVariable("query") String contactName){
		User user = this.userRepo.getUserByEmail(principal.getName());
		List<Contact> contacts = this.contactRepo.findByNameContainingAndUser(contactName, user);
		return ResponseEntity.ok(contacts);
	}
	
}
