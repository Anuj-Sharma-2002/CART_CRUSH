package com.ecom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecom.model.UserDtls;
import com.ecom.repository.UserRepository;
import com.ecom.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
   private	UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDtls saveUser(UserDtls userDtls) {
		
		userDtls.setRole("ROLE_USER");
		String encodedPassword = passwordEncoder.encode(userDtls.getPassword());
		userDtls.setPassword(encodedPassword);
		
		return userRepository.save(userDtls);
	}

}
