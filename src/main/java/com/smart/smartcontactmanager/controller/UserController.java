package com.smart.smartcontactmanager.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.smart.smartcontactmanager.entities.ContactEntity;
import com.smart.smartcontactmanager.entities.UserEntity;
import com.smart.smartcontactmanager.helper.Message;
import com.smart.smartcontactmanager.repository.ContactRepository;
import com.smart.smartcontactmanager.repository.UserRepository;





@Controller
@RequestMapping("/user")
public class UserController{
	@Autowired
	private UserRepository userrepository;
	@Autowired
	private ContactRepository contactrepository;
	//method for adding common data to response
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@ModelAttribute
	public void addCommonData(Model m,Principal principle)
	{     //get the username from spring security after succussfully login
	      String userName=principle.getName();

	      //get the user using username
	       UserEntity user=userrepository.getUserByUserName(userName);
	       m.addAttribute("user",user);
	}
	//dashboard home 
    @RequestMapping("/index")
	public String dashboard(Model model,Principal principle)
    {
    	model.addAttribute("title", "User Dashboard");
    	//get the username from spring security after succussfully login
      String userName=principle.getName();
      //System.out.println(userName);
      //get the user using username
       UserEntity user=userrepository.getUserByUserName(userName);
       //System.out.println(user);
       //send user information into the view or Html file
       model.addAttribute("user",user);
      return "normal/user_dashboard";
    }
    //open add form handler
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model)
    {
    	model.addAttribute("title", "Add Contact");
    	model.addAttribute("contact", new ContactEntity());
        return"normal/add_contact_form";	
    }
    //processing add contact form
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute ContactEntity contact,@RequestParam("ProfileImage") MultipartFile file, Principal principal,HttpSession session)
    {  
    	try {
    		//get file detail from submit form and Uploading too
    		if(file.isEmpty())
    		{
    			System.out.println("Your file is Empty");
    			contact.setImage("contact.png");
    		}else{
    			//upload the file to the folder
    			SimpleDateFormat b = new SimpleDateFormat("hhmmss");
    			String fileName="_"+b.format(new Date())+file.getOriginalFilename();
  			     contact.setImage(fileName);
    			//contact.setImage(file.getOriginalFilename());
    			File saveFile=new ClassPathResource("static/img").getFile();
    			Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+contact.getImage());
    			//first parameter is stream file.getstream means from file we are getting 
    			//file stream to save into folder Or from where we want to store
    			//second path of file where we want to store 
    			//third one if have existing file then replace it
    			Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
    			System.out.println("images is uploaded");
    		}
    		String name=principal.getName();
        	System.out.println("contact data: "+contact);
        	UserEntity user=this.userrepository.getUserByUserName(name);
        	//set user info in contact entity user field
        	contact.setUser(user);
        	//add form data in the userEntity contact List Type field  and save it. 
        	//Bcoz it has relatioship.Now User Entity Set with the conact List Also
        	user.getContacts().add(contact);
        	this.userrepository.save(user);
        	System.out.println("Data"+contact);
        	System.out.println("Added to contact");
        	//Success Message....
        	session.setAttribute("message", new Message("Contact Info Saved Successfully","success"));
    	}catch(Exception e)
    	{
    		System.out.println("ERROR"+e.getMessage());
    		e.printStackTrace();
    		session.setAttribute("message", new Message("Something Went Wrong Try Again !!","danger"));
    	}
    	
    	return "normal/add_contact_form";
    }
    //show contact Information and apply pagination also
    //perpage=5[n]
    //current page=0[page]
    @GetMapping("show-contacts/{page}")
    public String showContacts(@PathVariable("page")Integer page,Model m,Principal principal,HttpSession session){
    	m.addAttribute("title","Show Contact Page");
    	String name=principal.getName();
    	UserEntity user=this.userrepository.getUserByUserName(name);
    	//UserEntity user=this.userrepository.getUserByUserName(name);
    	//System.out.println(user.getContacts());
    	Pageable pageable=PageRequest.of(page,5);
    	Page<ContactEntity> contacts=this.contactrepository.findContactsByUser(user.getId(),pageable);
    	m.addAttribute("currentPage", page);
    	m.addAttribute("totalPages", contacts.getTotalPages());
    	m.addAttribute("contacts",contacts);
    	
    	return "normal/show_contacts";
    }
    //showing particular contact details
    @GetMapping("/{cid}/contact")
    public String showContactDetail(@PathVariable("cid") Integer cid,Model m,Principal principal)
    {
    	
    	
    	System.out.println("CID:  "+cid);
       Optional<ContactEntity> contactOptional=contactrepository.findById(cid);
       ContactEntity contact=contactOptional.get();
       
     //get current user details
   	 String userName=principal.getName();
   	 UserEntity user=userrepository.getUserByUserName(userName);
   	 if(user.getId()==contact.getUser().getId())
   	 {
   		m.addAttribute("contact", contact);
   		m.addAttribute("title",contact.getName());
   	 }
    	return"normal/single_conact_detail";
    }
    //Delete Particular contact Info
    @GetMapping("/delete/{cid}")
    public String deleteContact(@PathVariable("cid") Integer cid,Principal principal,HttpSession session)
    {
    	Optional <ContactEntity> contctOptional=contactrepository.findById(cid);
    	ContactEntity contact=contctOptional.get();
    	 //unlink user from contact
    	  //contact.setUser(null);
    	  //get current user details
      	 String userName=principal.getName();
      	 UserEntity user=userrepository.getUserByUserName(userName);
      	//check for security 
      	 if(user.getId()==contact.getUser().getId())
      	 {
      		 //delete file from folder
//      		File saveFile=new ClassPathResource("static/img").getFile();
//			Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+contact.getImage());
			try{
				//delete file from folder
				File saveFile=new ClassPathResource("static/img").getFile();
				Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+contact.getImage());
                //System.out.println(Paths.get(saveFile.getAbsolutePath()+contact.getImage()));
		        //Files.deleteIfExists(Paths.get(saveFile.getAbsolutePath()+contact.getImage()));
				 Files.delete(path);
			}catch(Exception ex){
		        ex.printStackTrace();
		        System.out.println("File not deleted");
		    } 
			//Files.deleteIfExists(Paths.get("WEB\\images\\" + .getName() +       				".png"));
      		contactrepository.delete(contact);
      		session.setAttribute("message", new Message("contact Deleted Sucessfully","success"));
      		
      		//session.setAttribute("message", new Message("Contact Info Saved Successfully","success"));
      	 }
      	
    	return "redirect:/user/show-contacts/0";
    }
  //Open_upadate_Handler
  @PostMapping("/update-contact/{cid}")
  public String openUpdateForm(@PathVariable("cid") Integer cid,Model m)
  {
	  m.addAttribute("title","Update Contact");
	 ContactEntity contact= contactrepository.findById(cid).get();
	 m.addAttribute("contact", contact);
	  return "normal/update_form";
  }
  
  //update-Handler
  @PostMapping("/process-update")
  public String updateContactInfo(@ModelAttribute ContactEntity contact,@RequestParam("ProfileImage") MultipartFile file, Principal principal,HttpSession session)
  {
	  try{
		 //image
		//old contact detail
		  ContactEntity oldContactDetail=contactrepository.findById(contact.getCid()).get();
		  System.out.println(oldContactDetail.getImage());
		  if(!file.isEmpty())
		  {
			  //update new Image
			  //contact.setImageUrl(file.getOriginalFilename());
			  SimpleDateFormat b = new SimpleDateFormat("hhmmss");
			 
			  String fileName="_"+b.format(new Date())+file.getOriginalFilename();
			  contact.setImage(fileName);
			  System.out.println(contact.getImage());
			  File saveFile=new ClassPathResource("static/img").getFile();
			  Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+fileName);
			  //Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
//			  StringBuilder fileNames = new StringBuilder();
//			  fileNames.append(file.getOriginalFilename()+"_"+b.format(new Date()));
			  Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
  			  //delete previous image 
     		  Path oldImageDelete=Paths.get(saveFile.getAbsolutePath()+File.separator+oldContactDetail.getImage());
    		  Files.delete(oldImageDelete);
		  }else{
			  contact.setImage(oldContactDetail.getImage());
		  }
		  
		  UserEntity user=userrepository.getUserByUserName(principal.getName());
		  contact.setUser(user);
		  
		  //System.out.println(contact.getImage());
		  //update info
		  contactrepository.save(contact);
		  session.setAttribute("message", new Message("Your contact is updated","success"));
	  }catch(Exception e) {
		  e.printStackTrace();
	  }
//	  System.out.println(contact.getName());
//	  System.out.println(contact.getCid());
	  return"redirect:/user/"+contact.getCid()+"/contact";
  }
  //your profile Handler
  @GetMapping("/profile")
  public String yourProfile(Model m)
  {
	  m.addAttribute("title", "Profile Page");
	  return"normal/profile";
  }
  //Delete  user Profile
  @GetMapping("/deleteUser/{uid}")
  public String deleteUser(@PathVariable("uid") Integer uid,HttpSession session)
  {
  	Optional <UserEntity> userOptional=userrepository.findById(uid);
  	UserEntity user=userOptional.get();
  	 //unlink user from contact
  	  //contact.setUser(null);
  	  //get current user details
    	 //String userName=principal.getName();
    	// UserEntity user=userrepository.getUserByUserName(userName);
    	//check for security 
    	 
    		 //delete file from folder
//    		File saveFile=new ClassPathResource("static/img").getFile();
//			Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+contact.getImage());
			try{
				//delete file from folder
				File saveFile=new ClassPathResource("static/img").getFile();
				Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+user.getImageUrl());
              //System.out.println(Paths.get(saveFile.getAbsolutePath()+contact.getImage()));
		        //Files.deleteIfExists(Paths.get(saveFile.getAbsolutePath()+contact.getImage()));
				 Files.delete(path);
			}catch(Exception ex){
		        ex.printStackTrace();
		        System.out.println("File not deleted");
		    } 
			//Files.deleteIfExists(Paths.get("WEB\\images\\" + .getName() +       				".png"));
    		userrepository.delete(user);
    		session.setAttribute("message", new Message("contact Deleted Sucessfully","success"));
    		//session.setAttribute("message", new Message("Contact Info Saved Successfully","success"));
    	 
  	return "redirect:/";
  }
 
  //Open_user_Profile_upadate_Handler
  @PostMapping("/update-Profile/{uid}")
  public String openUpdateProfileForm(@PathVariable("uid") Integer uid,Model m)
  {
	  m.addAttribute("title","Update Profile");
	  Optional <UserEntity> userOptional=userrepository.findById(uid);
	  UserEntity user=userOptional.get();
	  m.addAttribute("user", user);
	  return "normal/update_Profile_form";
  }
  //update User Profle
  //update-Handler
  @PostMapping("/process-user-profile-update")
  public String updateUserProfile(@ModelAttribute UserEntity user,@RequestParam("image_url") MultipartFile file, Principal principal,HttpSession session)
  {
	  try{
			 //image
			//old User detail
			  UserEntity oldUserDetail=userrepository.findById(user.getId()).get();
			  //set password as a previous bcoz we dont want to change these two value
			  user.setPassword(oldUserDetail.getPassword());
			  user.setStatus(oldUserDetail.isStatus());
			  user.setRole(oldUserDetail.getRole());
			
			  System.out.println("Old Image Details"+oldUserDetail.getImageUrl());
//			  user.setImageUrl(file.getOriginalFilename());
			  if(!file.isEmpty())
			  {
				  SimpleDateFormat b = new SimpleDateFormat("hhmmss");
					 
				  String fileName="_"+b.format(new Date())+file.getOriginalFilename();
				  user.setImageUrl(fileName);
				  //update new Image
				  File saveFile=new ClassPathResource("static/img").getFile();
				  Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+user.getImageUrl());
	  			  Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
	  			  
	  			  //delete previous image 
	  			Path oldImageDelete=Paths.get(saveFile.getAbsolutePath()+File.separator+oldUserDetail.getImageUrl());
				Files.delete(oldImageDelete);
			  }else {
				  user.setImageUrl(oldUserDetail.getImageUrl());
			  }
			  
//			  UserEntity user=userrepository.getUserByUserName(principal.getName());
//			  contact.setUser(user);
			  
			  System.out.println(user.getImageUrl());
			  //update info
			  userrepository.save(user);
			  session.setAttribute("message", new Message("Your Profile is updated","success"));
		  }catch(Exception e) {
			  e.printStackTrace();
		  }
		  System.out.println(user.getName());
		  System.out.println(user.getId());
		  return"redirect:/user/index";
  }
  //update password Handler
  @PostMapping("/change-password")
  public String changePassword(HttpSession session,Principal principal,@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword,@RequestParam("confirmPassword") String confirmPassword)
  {
	  
	  String userName=principal.getName();
	  UserEntity currentUser=this.userrepository.getUserByUserName(userName);
	  System.out.println(currentUser.getPassword());
	  System.out.println(this.bCryptPasswordEncoder.encode(newPassword));
	  //encrypt the password 
	  if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword()))
	  {
		//Encode and change the password
		  currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
		  this.userrepository.save(currentUser);
		  session.setAttribute("message", new Message("Your Password updated successfully","success"));
		  return"redirect:/user/index";
	  }else{
		  //error
		  session.setAttribute("message", new Message("Your Password is Wrong","danger"));
		  return"redirect:/user/settings";
	  }
	  
  }
  //Open Setting handler
  @GetMapping("/settings")
  public String openSetting(Model model)
  {
	  model.addAttribute("title", "Settings");
	  return "normal/settings";
  }
  //Call Functionality Controller
  @GetMapping("/doCall/{number}")
  public String doCall(@PathVariable("number") Long number)
  {

	  System.out.println(number);
	  return "redirect:/user/settings";
  }
}