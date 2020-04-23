package com.jomhak.theforum.control;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.jomhak.theforum.domain.Category;
import com.jomhak.theforum.domain.CategoryRepository;

@Controller
public class CategoryController {

	
	@Autowired
	private CategoryRepository categoryRepository;

	
	// addCategory
	
	@GetMapping("/category/new")
	public String showAddCategory(Model model) {
		model.addAttribute("category", new Category());
		return "category/addcategory";
	}
		
	@PostMapping("/category/new")
	public String saveAddCategory(@ModelAttribute("category") @Valid Category category, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "category/addcategory";
		}
		
		if (categoryRepository.findByName(category.getName()) == null) {
			categoryRepository.save(category);
		} else {
			bindingResult.rejectValue("name", "err.name", "Name is already in use");
			return "category/addcategory";
		}
			
		
		return "redirect:../";
	}
		
		// editCategory
		
	@GetMapping("/category/{categoryName}/edit")
	public String showEditCategory(@PathVariable String categoryName, Model model) {
		model.addAttribute("category", categoryRepository.findByName(categoryName));
		return "category/editcategory";
	}
		
	@PostMapping("/category/{categoryName}/edit")
	public String saveEditCategory(@ModelAttribute("category") @Valid Category category, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "comment/addcomment";
		}
		
		if (categoryRepository.findByName(category.getName()) == null) {
			categoryRepository.save(category);
		} else {
			bindingResult.rejectValue("name", "err.name", "Name is already in use");
			return "category/editcategory";
		}
			
		
		return "redirect:../";
	}
		
	// deleteCategory
		
	@GetMapping("/category/{categoryName}/delete")
	public String deleteCategory(@PathVariable String categoryName) {
		Long categoryId = categoryRepository.findByName(categoryName).getCategoryId();
		categoryRepository.deleteById(categoryId);
		return "redirect:../../";
	}
}
