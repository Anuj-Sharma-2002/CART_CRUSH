package com.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
}
