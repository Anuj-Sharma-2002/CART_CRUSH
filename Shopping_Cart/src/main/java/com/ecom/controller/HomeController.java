package com.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;

@Controller
public class HomeController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
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
	public String products(Model m) {
		m.addAttribute("products", productService.getAllActiveProduct());
		m.addAttribute("categories", categoryService.getAllActiveCategory());
		return "product";
	}
	
	@GetMapping(value="/product")
	public String view_products() {
		return "view_product";
	}
}
