package com.ecom.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecom.model.Category;
import com.ecom.model.UserDtls;
import com.ecom.service.CategoryService;
import com.ecom.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
     
	
	@Autowired
	 private UserService  userService;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/")
	public String Home() {
		return "user/home";
	}
	
	// This method is for Checking witch person is login if login provide a respective panal for it. This method call every time 
	// when ever this controller call
			@ModelAttribute
			public void getUserDetails(Principal p , Model m) {
				
				if(p != null) {
					String email  = p.getName();
					UserDtls userDtls = userService.getUserDtlsByEmail(email);
					m.addAttribute("user", userDtls);
				}
				
				List<Category> allActiveCategory = categoryService.getAllActiveCategory();
				m.addAttribute("category1", allActiveCategory);
			}
}
