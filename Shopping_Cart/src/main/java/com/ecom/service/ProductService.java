package com.ecom.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.Product;


public interface ProductService {
	public Product SaveProduct(Product product);
	
	public List<Product> getAllProduct();
	
	public boolean deleteProduct(int id);
	
	public Product getProductById(int id);

	Product updateProduct(Product product, MultipartFile image);
	
	public List<Product> getAllActiveProduct(String category);
}
