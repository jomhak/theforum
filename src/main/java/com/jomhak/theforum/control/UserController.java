package com.jomhak.theforum.control;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.jomhak.theforum.domain.Role;
import com.jomhak.theforum.domain.RoleRepository;
import com.jomhak.theforum.domain.SignupUser;
import com.jomhak.theforum.domain.User;
import com.jomhak.theforum.domain.UserRepository;

@Controller
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	// Signup / addUser
	
	@GetMapping("/signup")
	public String showSignup(Model model) {
		model.addAttribute("signupUser", new SignupUser());
		return "signup";
	}
	
	@PostMapping("/signup")
	public String saveSignup(@ModelAttribute("signupUser") @Valid SignupUser signupUser, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "signup";
		}
		if (signupUser.getPassword().equals(signupUser.getPasswordCheck())) {
			String password = signupUser.getPassword();
			BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
			String hashPassword = pwEncoder.encode(password);
			
			User newUser = new User();
			newUser.setUsername(signupUser.getUsername());
			newUser.setPasswordHash(hashPassword);
			newUser.setEmail(signupUser.getEmail());
			Role role = roleRepository.findByName("user");
			newUser.setRole(role);
			
			if (userRepository.findByUsername(signupUser.getUsername()) == null) {
				userRepository.save(newUser);
			} else {
				bindingResult.rejectValue("username", "err.username", "Username is already in use");
			}
			
		} else {
			bindingResult.rejectValue("passwordCheck", "err.passCheck", "Passwords do not match");
		}
		return "redirect:/login";
	}
	
	// Profile
	
	@GetMapping("/profile")
	public String showProfile(Model model, Authentication authentication) {
		String userName = authentication.getName();
		User user = userRepository.findByUsername(userName);

			
		model.addAttribute("userName", user.getUsername());
		model.addAttribute("userEmail", user.getEmail());
		return "profile/profile";
	}
	
	// editUser
	
	@GetMapping("/profile/edit")
	public String showEditUser(Authentication authentication, Model model) {
		String username = authentication.getName();
		User user = userRepository.findByUsername(username);
		
		model.addAttribute("user", user);
		return "profile/editprofile";
	}
	
	@PostMapping("/profile/edit")
	public String saveEditUser(@ModelAttribute("user") @Valid User editUser, Authentication authentication) {
		String username = authentication.getName();
		User user = userRepository.findByUsername(username);
		
		editUser.setUserId(user.getUserId());
		editUser.setUsername(username);
		editUser.setPasswordHash(user.getPasswordHash());
		userRepository.save(editUser);
		
		return "redirect:/profile";
	}
	

}
