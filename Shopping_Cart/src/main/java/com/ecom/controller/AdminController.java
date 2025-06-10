package com.ecom.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value="/admin")
public class AdminController {
   //  To Get The Admin Home Page 
	 @Autowired
	 private CategoryService categoryService;
	 
	 @Autowired
	 private ProductService productService;
	 
	 // To get index Page 
		@GetMapping(value="/")
		public String index() {
			return"admin/index";
		}
	   
	// To Get Add All The Category present inside my database 
		@GetMapping(value="/category")
		public String category(Model m) {
			m.addAttribute("categorys", categoryService.getAllCategory());
			return"admin/category";
		}
		
		
	// Add new Category in our Data base 
		//@RequestParam ("file") MultipartFile file : to get the file in input 
		
		@PostMapping(value="/saveCategory")
		public String saveCategory(@ModelAttribute Category category , @RequestParam ("file") MultipartFile file ,
				HttpSession session) throws IOException{
			// Get File Name so we store file name in SQL
			    String imageName=  file.isEmpty() ? "default.jpg" :file.getOriginalFilename();
			// Set file Name in Category Object
			    category.setImageName(imageName);
			    
			    
			Boolean existscategory = categoryService.existsCategory(category.getName());
			
			if(existscategory) {
				session.setAttribute("errorMsg", "Category Name Alredy Exist !");
			}else {
				Category savecategory = categoryService.saveCategory(category);
				System.out.println(savecategory.getIsActive());
				if(ObjectUtils.isEmpty(savecategory)) {
					session.setAttribute("errorMsg", "Category is Not add Due to some enternal error !");
				}else {
					// Start File Storing work
					File saveFile = new ClassPathResource("/static/img").getFile();
		            Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+"category_img"+File.separator+imageName);
				System.out.println(path);
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					// End File Storing work 
					session.setAttribute("succMsg", " saved sucsessfull");
				}
				
			}
			return "redirect:/admin/category";
		}
	
		
	// Delete Category  in Data Base 
		
		@GetMapping(value="/deleteCategory/{id}")
		public String deleteCategory(@PathVariable int id , HttpSession session) {
			System.out.println("i am in delteCategory");
		Boolean status=	categoryService.deleteCategory(id);
			
		if(status) {
			session.setAttribute("succMsg", "Category Deleted SuccsessFull ");
		}else {
			session.setAttribute("errorMsg", "Category Not Deleted Due to some error ");
		}
			return "redirect:/admin/category";
		}
		
		
	// Edit Category in Data Base 	
		
		@GetMapping("/loadEditCategory/{id}")
		public String loadEditCategory(@PathVariable int id, Model m) {
		
			m.addAttribute("category", categoryService.getCategoryById(id));
			return "admin/edit_category";
		}
		
	// Edit Category Post Mapping
		
		@PostMapping(value="/updateCategory")
		public String updateCategory(@ModelAttribute Category category , @RequestParam("file") MultipartFile file , HttpSession session) throws IOException {
			
			Category oldCategory = categoryService.getCategoryById(category.getID());
			if (ObjectUtils.isEmpty(oldCategory)) {
			    session.setAttribute("errorMsg", "Category not updated due to internal error");
			    return "redirect:/admin/loadEditCategory/"+category.getID();
			}

			String imageName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();
			category.setImageName(imageName);

			Category updatedCategory = categoryService.saveCategory(category);

			if (updatedCategory != null) {
			    if (!file.isEmpty()) {
			        File saveFile = new ClassPathResource("static/img").getFile();
			        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator + imageName);
			        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			        System.out.println(path);
			    }
			    session.setAttribute("succMsg", "Category updated successfully");
			} else {
			    session.setAttribute("errorMsg", "Category not updated due to internal error");
			}

			
			return "redirect:/admin/loadEditCategory/"+category.getID();
		}
	 //  To Load  Add Product Page 
		@GetMapping(value="/loadAddProduct")
		
		public String loadAddProduct(Model m) {
			m.addAttribute("categories", categoryService.getAllCategory());
			return"admin/add_product";
		}
    	
	// To submit product 
		@PostMapping(value="/saveProduct")
		public String saveProduct(@ModelAttribute Product product , @RequestParam ("file") MultipartFile image , HttpSession session) throws IOException {
			
		  String imageName = image.isEmpty()?"defaul.jpg" : image.getOriginalFilename();
		  
		  product.setImage(imageName);
		  product.setDiscount(0);
		  product.setDiscountPrice(product.getPrice());
		  
		  
		Product saveproduct=	productService.SaveProduct(product);
		
		if(!ObjectUtils.isEmpty(saveproduct)) {
			
			File saveFile = new ClassPathResource("/static/img").getFile();
			
	         Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+"product_img"+File.separator+ imageName);
	         
	         Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	         
	         System.out.println(path);
	         
			session.setAttribute("succMsg", "Product Save Succsessfull ");
			
		}else {
			
			session.setAttribute("errorMsg","Product not save due to some erorr");
			
		}
			return "redirect:/admin/loadAddProduct";
		}
		
	 //To get All the product in view 
		@GetMapping(value="/products")
		public String loadViewProduct(Model m) {
			m.addAttribute("products", productService.getAllProduct());
			return "/admin/products";
		}
		
	// Delete Mapping for Delete product
		@GetMapping(value="/deleteProduct/{id}")
		public String deletePreoduct(@PathVariable int id, HttpSession session) {
			boolean status = productService.deleteProduct(id);
			
			if(status) {
				session.setAttribute("succMsg", "Product Deleted Succsessfully");
			}else {
				session.setAttribute("errorMsg", "Product not deleted due to some error ");
			}
			return "redirect:/admin/products";
		}
		
		
	// Edit Product load page 
		
		@GetMapping(value ="/editProduct/{id}")
		public String loadEditProduct(@PathVariable int id , Model m) {
			m.addAttribute("product", productService.getProductById(id));
			m.addAttribute("categories",categoryService.getAllCategory());
			return "/admin/edit_product";
		}
		
		
	// Update product  by edit method 	
		
		
	   @PostMapping(value="/updateProduct")
	   public String updateProduct(@ModelAttribute Product product , @RequestParam ("file") MultipartFile file , HttpSession session) throws IOException {
		   
		   if (product.getDiscount() < 0 || product.getDiscount() > 100) {
				session.setAttribute("errorMsg", "invalid Discount");
			} else {
				Product updateProduct = productService.updateProduct(product, file);
				if (!ObjectUtils.isEmpty(updateProduct)) {
					session.setAttribute("succMsg", "Product update success");
				} else {
					session.setAttribute("errorMsg", "Something wrong on server");
				}
			}
		   return "redirect:/admin/editProduct/" +product.getId();
	   }
}
