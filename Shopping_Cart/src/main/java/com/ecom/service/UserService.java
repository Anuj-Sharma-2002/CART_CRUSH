package com.ecom.service;

import java.util.List;

import com.ecom.model.UserDtls;

public interface UserService {
    
	public UserDtls saveUser(UserDtls  userDtls);
	
	public UserDtls getUserDtlsByEmail(String email);

	public List<UserDtls> findByRole(String Role);

	public Boolean updateAcountStatus(Boolean status, Integer id);
}
