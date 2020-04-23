package com.jomhak.theforum.control;

import java.time.LocalDateTime;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.jomhak.theforum.domain.Category;
import com.jomhak.theforum.domain.CategoryRepository;
import com.jomhak.theforum.domain.Post;
import com.jomhak.theforum.domain.PostRepository;
import com.jomhak.theforum.domain.UserRepository;

@Controller
public class PostController {
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	
	// addPost
	
	@GetMapping("/category/{categoryName}/new")
	public String showAddPost(@PathVariable String categoryName, Model model) {
		model.addAttribute("categoryName", categoryName);
		model.addAttribute("post", new Post());
		return "post/addpost";
	}
	
	@PostMapping("/category/{categoryName}/new")
	public String saveAddPost(@PathVariable String categoryName, @ModelAttribute("post") @Valid Post post, BindingResult bindingResult, Authentication authentication) {
		if (bindingResult.hasErrors()) {
			return "post/addpost";
		}
		
		Category category = categoryRepository.findByName(categoryName);
		post.setCategory(category);
		LocalDateTime dt = LocalDateTime.now();
		post.setCreated(dt);
		String username = authentication.getName();
		post.setUser(userRepository.findByUsername(username));
		postRepository.save(post);
		
		return "redirect:../../category/" + categoryName;
	}
	
	// editPost
	
	@GetMapping("/category/{categoryName}/{postId}/edit")
	public String showEditPost(@PathVariable String categoryName, @PathVariable Long postId, Model model) {
		model.addAttribute("categoryName", categoryName);
		model.addAttribute("post", postRepository.findById(postId).get());
		return "post/editpost";
	}
	
	@PostMapping("/category/{categoryName}/{postId}/edit")
	public String saveEditPost(@ModelAttribute("post") @Valid Post post, @PathVariable String categoryName, @PathVariable Long postId, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "post/editpost";
		}
		
		post.setPostId(postId);
		post.setCategory(categoryRepository.findByName(categoryName));
		Post oldPost = postRepository.findById(postId).get();
		post.setUser(oldPost.getUser());
		postRepository.save(post);
		return "redirect:../../" + categoryName + "/" + postId;
	}
	
	// deletePost
	
	@GetMapping("/category/{categoryName}/{postId}/delete")
	public String deletePost(@PathVariable String categoryName, @PathVariable Long postId) {
		postRepository.deleteById(postId);
		return "redirect:../../" + categoryName;
	}
	

}
