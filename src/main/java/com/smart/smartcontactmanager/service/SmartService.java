package com.smart.smartcontactmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.smartcontactmanager.dto.UserDTO;
import com.smart.smartcontactmanager.entities.UserEntity;
import com.smart.smartcontactmanager.repository.UserRepository;

@Service
public class SmartService {
@Autowired
UserRepository userrepo;
//save register form data into database
public String saveUser(UserDTO user)
{
	try{
		user.setRole("ROLE_USER");
		user.setStatus(true);
		user.setImageUrl("default.png");
		UserEntity ue=new UserEntity();
		ue.setName(user.getName());
		ue.setEmail(user.getEmail());
		ue.setPassword(user.getPassword());
		ue.setRole(user.getRole());
		ue.setStatus(user.isStatus());
		ue.setImageUrl(user.getImageUrl());
		ue.setAbout(user.getAbout());
		UserEntity result=userrepo.saveAndFlush(ue);
//		System.out.println(result);
		return "true";
	}catch(Exception e){
		e.printStackTrace();
		return "false";
	}

  
}
}
