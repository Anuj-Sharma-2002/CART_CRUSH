package com.ecom.service;

import java.util.List;

import com.ecom.model.Product;


public interface ProductService {
	public Product SaveProduct(Product product);
	
	public List<Product> getAllProduct();
	
	public boolean deleteProduct(int id);
	
	public Product getProductById(int id);
}
