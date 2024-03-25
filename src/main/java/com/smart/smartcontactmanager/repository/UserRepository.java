package com.smart.smartcontactmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import com.smart.smartcontactmanager.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity,Integer>{
//    @Query("select u From User u WHERE u.email= :email")
//	public UserEntity getUserByUserName(@Param("email") String email);
    //Native Query
    @Query(value="select * from user u where u.email= :email ", nativeQuery=true)
    public UserEntity getUserByUserName(@Param("email") String email);
    
}