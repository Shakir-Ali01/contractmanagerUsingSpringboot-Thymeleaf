package com.smart.smartcontactmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.smartcontactmanager.entities.UserEntity;
import com.smart.smartcontactmanager.repository.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
	private UserRepository userrepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//fetching user from database
		UserEntity user=userrepo.getUserByUserName(username);
		if(user==null)
		{
			throw new UsernameNotFoundException("could not found user");
		}
		CustomUserDetails customUserDetails=new CustomUserDetails(user);
		return customUserDetails;
	}

}
