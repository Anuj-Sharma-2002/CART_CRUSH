package com.ecom.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.ecom.util.CommonUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired 
	private UserService userService; 
     
	@Autowired
	private CommonUtil commonutil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	// This method is for Checking witch person is login if login provide a respective panel for it. This method call every time 
		// when ever this controller call
	
	// Hear Principle is an interface it provide a name of user that login it has only one method p.getName()
	@ModelAttribute
	public void getUserLoginDetails(Principal p , Model m) {
		
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
		//m.addAttribute("user", new UserDtls());
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
	
	
	@GetMapping(value ="/forgot-password")
	public String showforgotPassword() {
		return "forgot_password";
	}
	
	
	@PostMapping(value="/forgot-password")
	public String processForgotPassword(@RequestParam String email , HttpSession session, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		
		UserDtls  userDtls = userService.getUserDtlsByEmail(email);
		
		if(ObjectUtils.isEmpty(userDtls)) {
			session.setAttribute("errorMsg", "Invalid Email !!");
			
		}else {
			  String token = UUID.randomUUID().toString();
			 userDtls.setResetToken(token);
			userService.updateUserResetToken(email,token);
			
			// Generate URL :
			// http://localhost:8080/reset-password?token=sfgdbgfswegfbdgfewgvsrg

						String url = commonutil.generateUrl(request) + "/reset-password?token=" +token;
						
						Boolean status = commonutil.sendMail(url, email);
						
						if(status) {
							session.setAttribute("succMsg", "Pleace check your mail... Password Rest link send");
						}else {
							session.setAttribute("errorMsg", "Email not send due to some error");
						}
		}
		
		return "redirect:/forgot-password";
	}
	
	@GetMapping("/reset-password")
	public String resetPassword(@RequestParam String token,Model m) {
		System.out.println(token);
		UserDtls userDtls = userService.getUserByToken(token);
		
		if(ObjectUtils.isEmpty(userDtls)) {
			m.addAttribute("msg", "Your link is invalid or expired !!");
			return "message";
		}
		m.addAttribute("token", token);
		return "reset_password";
	}
	
	@PostMapping("/reset-password")
	public String resetPassword(@RequestParam String token, @RequestParam String password, HttpSession session,
			Model m) {

		UserDtls userByToken = userService.getUserByToken(token);
		if (userByToken == null) {
			m.addAttribute("errorMsg", "Your link is invalid or expired !!");
			return "message";
		} else {
			userByToken.setPassword(passwordEncoder.encode(password));
			userByToken.setResetToken(null);
			userService.updateUser(userByToken);
			//session.setAttribute("succMsg", "Password change successfully");
			m.addAttribute("msg","Password change successfully");
			
			return "message";
		}

	}
}
