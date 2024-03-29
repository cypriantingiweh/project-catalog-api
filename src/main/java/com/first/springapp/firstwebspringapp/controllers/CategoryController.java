package com.first.springapp.firstwebspringapp.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.first.springapp.firstwebspringapp.dto.CategoryDTO;
import com.first.springapp.firstwebspringapp.exception.ResourceNotFoundException;
import com.first.springapp.firstwebspringapp.model.Category;
import com.first.springapp.firstwebspringapp.services.CategoryService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CategoryController {

	private CategoryService categoryService;

	@Autowired
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	@ResponseBody
	@PostMapping("category")
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDTO categoryDTO) {
		Category category = categoryService.add(categoryDTO);
		HttpHeaders responseHeaders = new HttpHeaders();
		URI newCategoryUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{categoryId}")
				.buildAndExpand(category.getId()).toUri();
		responseHeaders.setLocation(newCategoryUri);
		
		return ResponseEntity.created(newCategoryUri).build();
	}
	@GetMapping("category")
	public ResponseEntity<List<CategoryDTO>> getAllCategories() {
		List<CategoryDTO> category = categoryService.generate();
		if(category==null) 
			throw new ResourceNotFoundException("No Category Exist in List For now");
		else
		return ResponseEntity.ok(category);
	}

	
	@GetMapping("category/{categoryId}")
	public ResponseEntity<CategoryDTO> getCategory( @PathVariable int categoryId) {
		CategoryDTO category=categoryService.generateOne(categoryId);
		if(category==null)
			throw new ResourceNotFoundException("Category_id: "+ categoryId);
		else
				return ResponseEntity.ok().body(category);
		
	}


	@PutMapping("category/{categoryId}")
	public ResponseEntity<?> editCategory(@PathVariable Integer categoryId, @RequestBody CategoryDTO categoryDTO) {
		categoryService.edit(categoryId, categoryDTO);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}
	@DeleteMapping("category/{categoryId}")
	public ResponseEntity<?> delCategory(@PathVariable int categoryId ){
		 Integer del = categoryService.del(categoryId);
		if(del== null)
			throw new ResourceNotFoundException("Category_id: "+ categoryId);
		
		else
			categoryService.del(categoryId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
