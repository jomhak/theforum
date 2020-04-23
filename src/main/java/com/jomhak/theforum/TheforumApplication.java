package com.jomhak.theforum;


import java.time.LocalDateTime;
import java.time.Month;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.jomhak.theforum.domain.Category;
import com.jomhak.theforum.domain.CategoryRepository;
import com.jomhak.theforum.domain.Comment;
import com.jomhak.theforum.domain.CommentRepository;
import com.jomhak.theforum.domain.Post;
import com.jomhak.theforum.domain.PostRepository;
import com.jomhak.theforum.domain.Role;
import com.jomhak.theforum.domain.RoleRepository;
import com.jomhak.theforum.domain.User;
import com.jomhak.theforum.domain.UserRepository;

@SpringBootApplication
public class TheforumApplication {
	
	private static final Logger log = LoggerFactory.getLogger(TheforumApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TheforumApplication.class, args);
	}
	
	
	//Test data
	@Bean
	public CommandLineRunner forumDemo(PostRepository postRepository, CategoryRepository categoryRepository, CommentRepository commentRepository, UserRepository userRepository, RoleRepository roleRepository) {
		return (args) -> {
			log.info("Create roles");
			roleRepository.save(new Role("user"));
			roleRepository.save(new Role("moderator"));
			roleRepository.save(new Role("admin"));
			
			Role userRole = roleRepository.findByName("user");
			Role adminRole = roleRepository.findByName("admin");
			
			log.info("Create users");
			// user/salasana, admin/sanasala
			userRepository.save(new User("user", "$2a$10$nBgjJ9qQB2poMNZkY2D5HuAscR7wTmOVtJvMb7Dn.r2JNGyCZNvEG", "user@email.com", userRole));
			userRepository.save(new User("admin", "$2a$10$rK1ortSlb6ompzrcsDTSaOG0DPxPm3scube7fFNiXYN3w9IYX08TG", "admin@email.com", adminRole));
			
			log.info("Create categories");
			categoryRepository.save(new Category("Music"));
			categoryRepository.save(new Category("Sport"));
			categoryRepository.save(new Category("News"));
			
			log.info("Create posts");
			LocalDateTime dt1 = LocalDateTime.of(2020, Month.MARCH, 23, 8, 45);
			LocalDateTime dt2 = LocalDateTime.of(2020, Month.APRIL, 1, 17, 56);
			LocalDateTime dt3 = LocalDateTime.of(2020, Month.MARCH, 28, 11, 4);
			postRepository.save(new Post("Check out this new music video", "Hey! I just found this masterpiece.", dt1, categoryRepository.findByName("Music"), userRepository.findByUsername("user")));
			postRepository.save(new Post("Tomorrows news", "Here are the news of tomorrow.", dt2, categoryRepository.findByName("News"), userRepository.findByUsername("user")));
			postRepository.save(new Post("Cool news here", "How cool was it?", dt3, categoryRepository.findByName("News"), userRepository.findByUsername("admin")));
			
			log.info("Create comments");
			Post post = postRepository.findById((long) 9).get();
			commentRepository.save(new Comment("Nice comment", dt3, post, userRepository.findByUsername("user")));
			commentRepository.save(new Comment("Cool comment", dt2, post, userRepository.findByUsername("user")));
			commentRepository.save(new Comment("Perfect comment", dt1, post, userRepository.findByUsername("admin")));
			
			
		};
	}

}
