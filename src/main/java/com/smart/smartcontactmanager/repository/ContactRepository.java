package com.smart.smartcontactmanager.repository;

import java.util.List;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.smartcontactmanager.entities.ContactEntity;
import com.smart.smartcontactmanager.entities.UserEntity;

public interface ContactRepository extends JpaRepository<ContactEntity,Integer>{
       //pagination .....
	   @Query("from ContactEntity as c where c.user.id= :id")
	    //public List<ContactEntity> findContactByUserId(@Param("id") int id);
	   //it need current page-page
	   //contact per page-5
	   public Page<ContactEntity>findContactsByUser(@Param("id")int userId,Pageable pePageable);
	   
	   
	   //search By Name For Search Functionality
	   //containing Means:- it give the result from name which have passing value like we pass t the  
	   //it give all the name which have t letter that it 
	   //AndUserEntity means:-check user should be same which pass by us. we give this one for
	   //security validation bcoz we need contact which created by only login user na.
	   public List<ContactEntity> findByNameContainingAndUser(String name,UserEntity user);
}
