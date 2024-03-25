package com.smart.smartcontactmanager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.smart.smartcontactmanager.entities.ContactEntity;
import com.smart.smartcontactmanager.entities.UserEntity;
import com.smart.smartcontactmanager.repository.ContactRepository;
import com.smart.smartcontactmanager.repository.UserRepository;

@RestController
public class SearchController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;
	//Search Handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?> seacrh(@PathVariable("query") String query,Principal principal)
	{
		System.out.println(query);
		UserEntity user=this.userRepository.getUserByUserName(principal.getName());
		List<ContactEntity> contacts=this.contactRepository.findByNameContainingAndUser(query, user);
		return ResponseEntity.ok(contacts);
	}
}
