package com.smart.smartcontactmanager.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import com.smart.smartcontactmanager.dto.UserDTO;
import com.smart.smartcontactmanager.entities.UserEntity;
import com.smart.smartcontactmanager.helper.Message;
import com.smart.smartcontactmanager.repository.UserRepository;
import com.smart.smartcontactmanager.service.SmartService;
@Controller
@Validated

public class HomeController {
@Autowired
private SmartService smartservice;
@Autowired
private UserRepository userrepo;
@Autowired
private BCryptPasswordEncoder passwordEncoder;
@RequestMapping("/")
public String home(Model model) {
model.addAttribute("title", "Home");
	return "home";
}
@RequestMapping("/about")
public String about(Model model) {
model.addAttribute("title", "About Us");
	return "about";
}
@RequestMapping("/signup")
public String signup(Model model) {
model.addAttribute("title", "SignUp");
model.addAttribute("user",new UserDTO());
	return "signup";
}
//handler for customLogin
@RequestMapping("/signin")
public String customLogin(Model model) {
model.addAttribute("title", "Login");
	return "login";
}
//this handler for registering user
@RequestMapping(value="/do_register", method=RequestMethod.POST)
public String registerUser(@ModelAttribute("user") UserEntity user,BindingResult result1,@RequestParam(value="check",defaultValue="false") boolean check,Model model,HttpSession session)
{
	
	try{
		if(!check)
		{
			//session.setAttribute("message", new Message("Please Accept Term & Condition","alert-danger"));
			throw new Exception("You have not Aggred the Terms And Condition");
		}
		if(result1.hasErrors())
		{
//		 System.out.println("Error"+result1.toString());
			System.out.println("hello");
//			 model.addAttribute("user", user);
			 return "signup";
		 }
		user.setRole("ROLE_USER");
		user.setStatus(true);
		user.setImageUrl("default.png");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		UserEntity result=this.userrepo.save(user);
		session.setAttribute("message", new Message("Registration Done Successfully","alert-success"));
		model.addAttribute("user", new UserEntity());
		return "signup";
	}catch(Exception e){
		e.printStackTrace();
		model.addAttribute("user", user);
		session.setAttribute("message", new Message("something went wrong","alert-danger"));
		return "signup";
	}
	

	//server side validation
	
//	 if(result1.hasErrors())
//	 {
//		 System.out.println("Error"+result1.toString());
//		 model.addAttribute("user", user);
//		 return "signup";
//	 }
	//call service layer
//	String s=smartservice.saveUser(user);
//	if(s=="true"){
//		model.addAttribute("user",new UserDTO());
//		session.setAttribute("message", new Message("Your Registration Done Successfully","alert-success"));
//		return "signup";
//	}else{
//		model.addAttribute("user",user);
//		session.setAttribute("message", new Message("Something Went Wrong","alert-danger"));
//		return "signup";
//	}

	
}
}