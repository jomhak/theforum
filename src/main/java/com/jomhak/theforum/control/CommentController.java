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

import com.jomhak.theforum.domain.Comment;
import com.jomhak.theforum.domain.CommentRepository;
import com.jomhak.theforum.domain.Post;
import com.jomhak.theforum.domain.PostRepository;
import com.jomhak.theforum.domain.UserRepository;

@Controller
public class CommentController {
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	// addComment
	
	@GetMapping("/category/{categoryName}/{postId}/comment")
	public String showAddComment(@PathVariable String categoryName, @PathVariable String postId, Model model) {
		model.addAttribute("comment", new Comment());
		model.addAttribute("categoryName", categoryName);
		model.addAttribute("postId", postId);
		return "comment/addcomment";
		
	}
		
	@PostMapping("/category/{categoryName}/{postId}/comment")
	public String saveAddComment(@PathVariable Long postId, @ModelAttribute("comment") @Valid Comment comment, BindingResult bindingResult, Authentication authentication) {
		if (bindingResult.hasErrors()) {
			return "comment/addcomment";
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
		return "comment/editcomment";
		
	}
		
	@PostMapping("/category/{categoryName}/{postId}/{commentId}/edit")
	public String saveEditComment(@PathVariable Long postId, @PathVariable Long commentId, @ModelAttribute("comment") @Valid Comment comment, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "comment/editcomment";
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

}
