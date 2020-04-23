package com.jomhak.theforum.control;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jomhak.theforum.domain.CategoryRepository;
import com.jomhak.theforum.domain.Post;
import com.jomhak.theforum.domain.PostRepository;
import com.jomhak.theforum.domain.User;
import com.jomhak.theforum.domain.UserRepository;

@Controller
public class TheforumController {
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	// Index
	@GetMapping({"/", "/category"})
	public String showIndex(Model model) {
		model.addAttribute("categories", categoryRepository.findAll());
		return "index";
	}
		
	// Login
	@GetMapping("/login")
	public String showLogin() {
		return "login";
	}
		
		
		
	// Search for posts by name
	@PostMapping("/search")
	public String searchPosts(@RequestParam("searchField") String searchField, HttpServletRequest request) {
		if (searchField == "") {
			String whereFrom = request.getHeader("Referer");
			return "redirect:" + whereFrom;
		}
		
		return "redirect:/search/" + searchField;
	}
		
	// Show search results
	@GetMapping("/search/{searchedThing}")
	public String showSearchResults(@PathVariable String searchedThing, Model model) {
		List<Post> posts = postRepository.findByTitleContaining(searchedThing);
		model.addAttribute("posts", posts);
		return "search";
	}
		
	// List posts in category
	@GetMapping("/category/{categoryName}")
	public String showPostsOfCategory(@PathVariable String categoryName, Model model) {
		// Check if category exists
		if (categoryRepository.findByName(categoryName) == null) {
			return "redirect:/";
		}
			
		model.addAttribute("posts", postRepository.findByCategoryName(categoryName));
		model.addAttribute("categoryName", categoryName);
			
		return "postlist";
	}
		
	// List single post and it's comments
	@GetMapping("/category/{categoryName}/{postId}")
	public String showSinglePostAndComments(@PathVariable String categoryName, @PathVariable Long postId, Model model, Authentication authentication) {
		Optional<Post> postOptional = postRepository.findById(postId);
		Post post = postOptional.get();
		model.addAttribute("post", post);
		model.addAttribute("categoryName", categoryName);
			
		if (authentication != null) {
			String username = authentication.getName();
			User user = userRepository.findByUsername(username);
			String role = user.getRole().getName();
			model.addAttribute("loggedRole", role);
			model.addAttribute("loggedUsername", username);
		}
		return "postcomments";
	}
	
	// REST - Get posts of a category
	
	@GetMapping("/api/category/{categoryName}")
	public @ResponseBody List<Post> findPostilista(@PathVariable("categoryName") String categoryName) {
		return postRepository.findByCategoryName(categoryName);
	}

}
