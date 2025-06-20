package com.ecom.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.Category;
import com.ecom.model.UserDtls;
import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired 
	private UserService userService; 
     
	
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
	
	@GetMapping(value="/")
	public String index() {
		return "index";
	}
	
	@GetMapping(value="/signin")
	public String login() {
		return "login";
	}
	
	@GetMapping(value="/register")
	public String register(Model m) {
		m.addAttribute("user", new UserDtls());
		return "register";
	}
	
	@GetMapping(value="/products")
	public String products(Model m , @RequestParam(value = "category" ,defaultValue="") String category) {
		m.addAttribute("products", productService.getAllActiveProduct(category));
		m.addAttribute("categories", categoryService.getAllActiveCategory());
		m.addAttribute("paramValue", category);
		return "product";
	}
	
	@GetMapping(value="/product/{id}" )
	public String view_products(@PathVariable int id , Model m) {
		m.addAttribute("product", productService.getProductById(id));
		return "view_product";
	}
	
	@PostMapping(value="/saveUser")
	public String saveUser(@ModelAttribute UserDtls user , @RequestParam("img") MultipartFile file , HttpSession session) throws IOException {
		
		String imageName = file.isEmpty()?"defaul.jpg" : file.getOriginalFilename();
		user.setProfileImage(imageName);
		
		UserDtls userDtls =userService.saveUser(user);
		
		if(!ObjectUtils.isEmpty(userDtls)) {
			
			if(!file.isEmpty()) {
				File saveFile = new ClassPathResource("/static/img").getFile();
				Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+"profile_img"+File.separator+ imageName);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println(path);
			}
			
			
			session.setAttribute("succMsg", "User Register Succsessfull ");
			return "redirect:/register";
			
		}else {
			session.setAttribute("errorMsg","User not save due to some erorr");
			return "redirect:/login";
		}
		
	}
}
