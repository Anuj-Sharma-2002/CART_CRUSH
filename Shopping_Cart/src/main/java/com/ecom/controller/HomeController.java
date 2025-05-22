package com.ecom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping(value="/")
	public String index() {
		return "index";
	}
	
	@GetMapping(value="/login")
	public String login() {
		return "login";
	}
	
	@GetMapping(value="/register")
	public String register() {
		return "register";
	}
	
	@GetMapping(value="/products")
	public String products() {
		return "product";
	}
	
	@GetMapping(value="/product")
	public String view_products() {
		return "view_product";
	}
}
