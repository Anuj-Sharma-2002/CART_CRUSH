package com.ecom.service;

import java.util.List;

import com.ecom.model.UserDtls;

public interface UserService {
    
	public UserDtls saveUser(UserDtls  userDtls);
	
	public UserDtls getUserDtlsByEmail(String email);

	public List<UserDtls> findByRole(String Role);

	public Boolean updateAcountStatus(Boolean status, Integer id);
	
	public void increaseFailedAttempt(UserDtls userDtls);
	
	public void userAccountLock(UserDtls userDtls);
	
	public Boolean unlockAccountTimeExpired(UserDtls userDtls);
	
	public void resetAttempt(int userId);

	public void updateUserResetToken(String email, String token);

	public UserDtls getUserByToken(String token);

	public void updateUser(UserDtls userByToken);
}
