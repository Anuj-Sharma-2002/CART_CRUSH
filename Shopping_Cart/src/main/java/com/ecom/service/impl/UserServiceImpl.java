package com.ecom.service.impl;

import java.util.List;
import java.util.Optional;

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
		userDtls.setIsEnable(true);
		String encodedPassword = passwordEncoder.encode(userDtls.getPassword());
		userDtls.setPassword(encodedPassword);
		
		return userRepository.save(userDtls);
	}

	@Override
	public UserDtls getUserDtlsByEmail(String email) {
		
		return userRepository.findByEmail(email);
	}

	@Override
	public List<UserDtls> findByRole(String role) {
		
		return userRepository.findByRole(role);
	}

	@Override
	public Boolean updateAcountStatus(Boolean status, Integer id) {
		
		Optional<UserDtls> userDtls = userRepository.findById(id);
		
		if(userDtls.isPresent()) {  // isPresent() are used in Optional if object is not present provide us Boolean value if null give false
			UserDtls user = userDtls.get();
			user.setIsEnable(status);
			userRepository.save(user);
			return true;
		}
		return false;
	}

}
