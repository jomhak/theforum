package com.jomhak.theforum.control;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jomhak.theforum.domain.Category;
import com.jomhak.theforum.domain.CategoryRepository;
import com.jomhak.theforum.domain.Comment;
import com.jomhak.theforum.domain.CommentRepository;
import com.jomhak.theforum.domain.Post;
import com.jomhak.theforum.domain.PostRepository;
import com.jomhak.theforum.domain.User;
import com.jomhak.theforum.domain.UserRepository;

@Controller
public class PostController {
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
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
	
	// Profile
	@GetMapping("/profile")
	public String showProfile(Model model, Authentication authentication) {
		String userName = authentication.getName();
		User user = userRepository.findByUsername(userName);
		
		
		model.addAttribute("userName", user.getUsername());
		model.addAttribute("userEmail", user.getEmail());
		return "profile";
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
		if (categoryRepository.findByName(categoryName) == null) {
			return "redirect:/";
		}
		
		model.addAttribute("posts", postRepository.findByCategoryName(categoryName));
		model.addAttribute("categoryName", categoryName);
		
		// Check if category exists
		
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
	
	
	// DEMO - List all posts
	@GetMapping("/postlist")
	public String showPostList(Model model) {
		List<Post> posts = (List<Post>) postRepository.findAll();
		model.addAttribute("posts", posts);
		return "postlist";
	}
	
	@GetMapping("/postilista")
	public @ResponseBody List<Post> findPostilista() {
		return (List<Post>) postRepository.findAll();
	}
	
	
	// addPost
	
	@GetMapping("/category/{categoryName}/new")
	public String showAddPost(@PathVariable String categoryName, Model model) {
		model.addAttribute("categoryName", categoryName);
		model.addAttribute("post", new Post());
		return "addpost";
	}
	
	@PostMapping("/category/{categoryName}/new")
	public String saveAddPost(@PathVariable String categoryName, @ModelAttribute("post") @Valid Post post, BindingResult bindingResult, Authentication authentication) {
		if (bindingResult.hasErrors()) {
			return "addpost";
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
		return "editpost";
	}
	
	@PostMapping("/category/{categoryName}/{postId}/edit")
	public String saveEditPost(@ModelAttribute("post") @Valid Post post, @PathVariable String categoryName, @PathVariable Long postId, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "editpost";
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
	
	// addComment
	
	@GetMapping("/category/{categoryName}/{postId}/comment")
	public String showAddComment(@PathVariable String categoryName, @PathVariable String postId, Model model) {
		model.addAttribute("comment", new Comment());
		model.addAttribute("categoryName", categoryName);
		model.addAttribute("postId", postId);
		return "addcomment";
	
	}
	
	@PostMapping("/category/{categoryName}/{postId}/comment")
	public String saveAddComment(@PathVariable Long postId, @ModelAttribute("comment") @Valid Comment comment, BindingResult bindingResult, Authentication authentication) {
		if (bindingResult.hasErrors()) {
			return "addcomment";
		}
		
		Post post = postRepository.findById(postId).get();
		comment.setPost(post);
		LocalDateTime dt = LocalDateTime.now();
		comment.setCreated(dt);
		String username = authentication.getName();
		comment.setUser(userRepository.findByUsername(username));
		commentRepository.save(comment);
		
		return "redirect:../" + postId;
	}
	
	// editComment
	
	@GetMapping("/category/{categoryName}/{postId}/{commentId}/edit")
	public String showEditComment(@PathVariable String categoryName, @PathVariable String postId, @PathVariable Long commentId, Model model) {
		Comment comment = commentRepository.findById(commentId).get();
		model.addAttribute("comment", comment);
		model.addAttribute("categoryName", categoryName);
		model.addAttribute("postId", postId);
		return "editcomment";
	
	}
	
	@PostMapping("/category/{categoryName}/{postId}/{commentId}/edit")
	public String saveEditComment(@PathVariable Long postId, @PathVariable Long commentId, @ModelAttribute("comment") @Valid Comment comment, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "editcomment";
		}
		
		Post post = postRepository.findById(postId).get();
		comment.setPost(post);
		comment.setCommentId(commentId);
		Comment oldComment = commentRepository.findById(commentId).get();
		comment.setUser(oldComment.getUser());
		commentRepository.save(comment);
		
		return "redirect:../../" + postId;
	}
	
	// deleteComment
	
	@GetMapping("/category/{categoryName}/{postId}/{commentId}/delete")
	public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
		commentRepository.deleteById(commentId);
		return "redirect:../../" + postId;
	}
	
	// addCategory
	
	@GetMapping("/category/new")
	public String showAddCategory(Model model) {
		model.addAttribute("category", new Category());
		return "addcategory";
	}
	
	@PostMapping("/category/new")
	public String saveAddCategory(@ModelAttribute("category") @Valid Category category, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "addcategory";
		}
		
		categoryRepository.save(category);
		return "redirect:../";
	}
	
	// editCategory
	
	@GetMapping("/category/{categoryName}/edit")
	public String showEditCategory(@PathVariable String categoryName, Model model) {
		model.addAttribute("category", categoryRepository.findByName(categoryName));
		return "editcategory";
	}
	
	@PostMapping("/category/{categoryName}/edit")
	public String saveEditCategory(@ModelAttribute("category") @Valid Category category, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "addcomment";
		}
		
		categoryRepository.save(category);
		return "redirect:../";
	}
	
	// deleteCategory
	
	@GetMapping("/category/{categoryName}/delete")
	public String deleteCategory(@PathVariable String categoryName) {
		Long categoryId = categoryRepository.findByName(categoryName).getCategoryId();
		categoryRepository.deleteById(categoryId);
		return "redirect:../../";
	}
	
	// addUser
	
	// editUser
	
	// deleteUser
	
	// addRole
	
	// deleteRole
	
	
	// AJAX sääpalvelu
	
	

}
