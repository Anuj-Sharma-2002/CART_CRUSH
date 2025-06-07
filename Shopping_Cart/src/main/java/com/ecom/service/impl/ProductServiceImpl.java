package com.ecom.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ecom.model.Product;
import com.ecom.repository.ProductRepository;
import com.ecom.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
	
  @Autowired
  private ProductRepository productRepository;
	
	@Override
	public Product SaveProduct(Product product) {
	
		return productRepository.save(product);
	}

	@Override
	public List<Product> getAllProduct() {
		
		return productRepository.findAll();
	}

	@Override
	public boolean deleteProduct(int id) {
		 
		Product existProduct = productRepository.findById(id).orElse(null);
		
		if(!ObjectUtils.isEmpty(existProduct)) {
			productRepository.delete(existProduct);
			return true;
		}
		return false;
	}

	@Override
	public Product getProductById(int id) {
		
		
		return productRepository.findById(id).orElse(null);
	}

}
