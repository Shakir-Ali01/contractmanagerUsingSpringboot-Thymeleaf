package com.smart.smartcontactmanager.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.smartcontactmanager.entities.UserEntity;
import com.smart.smartcontactmanager.helper.Message;
import com.smart.smartcontactmanager.repository.UserRepository;
import com.smart.smartcontactmanager.service.EmailService;

@Controller
public class forgotController {
//email id from open Handler
	@Autowired 
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private EmailService emailSerservice;
	Random random=new Random(1000);
	
	@GetMapping("forgot")
	public String openEmailForm()
	{
		
		return"forgot_email_form";
	}
	@PostMapping("send-otp")
	public String sendOTP(@RequestParam("email") String email,HttpSession session)
	{
		//generate otp of 6 digit
		int otp=random.nextInt(999999);
		//write code for send OTP to Email
		String subject="OTP form SCM";
		String message="<h1>OTP="+otp+"</h1>";
		String to=email;
		//emailSerservice.sendEmail(message,subject,to);
		//boolean flag=this.emailService.sendEmail(subject,message,to);
		boolean flag=true;
		if(flag)
		{
			session.setAttribute("otp",otp);
			session.setAttribute("email",email);
			System.out.println(otp);
			return "verify_otp";}
		else {
			session.setAttribute("message", "Server Error Occurs Please Try Again");
		    return"forgot_email_form";
		}
	}
	//verifyotp
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp")int otp,HttpSession session)
	{
	    	int myOtp=(int)session.getAttribute("otp");
	  
	    	String email=(String)session.getAttribute("email");
	    	System.out.println(email);
	    	System.out.println("My OTP"+myOtp);
	    	System.out.println("OTP: "+otp);
    	   
	    	if(myOtp==otp)
	    	{
	    		System.out.println("correct");
	    		UserEntity user=this.userRepository.getUserByUserName(email);
	    		if(user==null)
	    		{
	    			//send error message
	    			session.setAttribute("message", "User does not exists with email !!");
		    		
		    		return "forgot_email_form";
	    		}else {
	    			//send change password form
	    			return "password_change_form";
	    		}
	    		
	    	}else {
	    		session.setAttribute("message", "You Have Entered Wrong OTP");
	    		System.out.println("Your Otp Is Wrong");
	    		return "verify_otp";
	    	}
	    	
	}
	//change Password
	@PostMapping("/change-password")
	public String changePassword(HttpSession session,@RequestParam("newPassword") String newPassword)
	{
		String email=(String)session.getAttribute("email");
		UserEntity user=this.userRepository.getUserByUserName(email);
		user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
		this.userRepository.save(user);
		return"redirect:/signin?change=password changed Successfully";
	}
	
}
