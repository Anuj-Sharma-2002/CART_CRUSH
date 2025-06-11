package com.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecom.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>  {
	
		public Boolean existsByName(String name); // check category exist or not
		
		public List<Category> findByIsActiveTrue(); // Gating only Active category those are true
}
