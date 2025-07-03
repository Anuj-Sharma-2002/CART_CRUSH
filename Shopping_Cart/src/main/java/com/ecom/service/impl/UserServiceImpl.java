package com.ecom.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecom.model.UserDtls;
import com.ecom.repository.UserRepository;
import com.ecom.service.UserService;
import com.ecom.util.AppConstant;

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
		userDtls.setAccountNonLocked(true);
		userDtls.setFailedAttempt(0);
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

	@Override
	public void increaseFailedAttempt(UserDtls userDtls) {
		
		userDtls.setFailedAttempt(userDtls.getFailedAttempt() +1);
		userRepository.save(userDtls);
		
	}

	@Override
	public void userAccountLock(UserDtls userDtls) {
		userDtls.setAccountNonLocked(false);
		userDtls.setLockTime(new Date());
		userRepository.save(userDtls);
		
	}

	@Override
	public Boolean unlockAccountTimeExpired(UserDtls userDtls) {
		long lockTime = userDtls.getLockTime().getTime();
		
		long unlockTime = lockTime +AppConstant.Unlock_Duration_Time;
		
		long currentTime = System.currentTimeMillis();
		
		if(currentTime >= unlockTime) {
			userDtls.setFailedAttempt(0);
			userDtls.setLockTime(null);
			userDtls.setAccountNonLocked(true);
			userRepository.save(userDtls);
			return true;
		}
		return false;
	}

	@Override
	public void resetAttempt(int userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUserResetToken(String email, String token) {
		
		UserDtls userDtls =userRepository.findByEmail(email);
		userDtls.setResetToken(token);
		userRepository.save(userDtls);
		
	}

	@Override
	public UserDtls getUserByToken(String token) {
		
		UserDtls userDtls = userRepository.findByResetToken(token);
	
		return userDtls;
	}

	@Override
	public void updateUser(UserDtls userByToken) {
		userRepository.save(userByToken);
		
	}

	
    
	 
}
