package com.smart.smartcontactmanager.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Message.RecipientType;
import org.springframework.stereotype.Service;

import com.smart.smartcontactmanager.helper.Message;

@Service
public class EmailService {
  public void sendEmail(String subject,String message,String to)
  {
	  String from="connectwithshakirali@gmail.com";
	  //variable for gmail
	  String host="smtp.gmail.com";
	  //get the system properties
	  Properties properties =System.getProperties(); 
	  System.out.println("Properties"+properties);
	  //setting important information to properties object
	  //host set
	  properties.put("mail.smtp.host", host);
      properties.put("mail.smtp.port", "465");
      properties.put("mail.smtp.ssl.enable", "true");
      properties.put("mail.smtp.auth", "true");
	  
      //step 1: to get the session object
      Session session=Session.getInstance(properties, new Authenticator(){
    	 @Override
    	 protected PasswordAuthentication getPasswordAuthentication() {
    		 return new PasswordAuthentication(from, "*****");
    	 }
	 });
	//Step 2: compose the message(text,multi,media)
      session.setDebug(true);
      MimeMessage m=new MimeMessage(session);
    
      try {
    	  //from
    	  m.setFrom(from);
    	  //reciepent 
    	  //m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
    	 // m.addRecipient(Message.RecipientType., new InternetAddress("dd@gmail.com"));
    	  m.addRecipient(RecipientType.TO, new InternetAddress(to));
    	  //set Subject
    	  m.setSubject(subject);
    	  //adding text to message
    	  m.setText(message);
    	  //Step :3 Send mail
    	  Transport.send(m);
    	  System.out.println("send Msg Successfully");
      }catch(Exception e) {
    	  e.printStackTrace();
      }
     
  }
}
