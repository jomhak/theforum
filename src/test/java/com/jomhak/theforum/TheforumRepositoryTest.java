package com.jomhak.theforum;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

@DataJpaTest
public class TheforumRepositoryTest {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	// Category tests
	@Test
	public void createNewCategory() {
		Category category = new Category("Movies");
		categoryRepository.save(category);
		assertThat(categoryRepository.findByName("Movies")).isNotNull();
	}
	
	@Test
	public void editCategory() {
		Category category = new Category("Movies");
		categoryRepository.save(category);
		assertThat(category.getCategoryId()).isNotNull();
		category.setName("TV");
		categoryRepository.save(category);
		assertThat(category.getCategoryId()).isNotNull();
	}
	
	@Test
	public void deleteCategory() {
		Category category = new Category("Movies");
		categoryRepository.save(category);
		assertThat(category.getCategoryId()).isNotNull();
		long categoryCount = categoryRepository.count();
		categoryRepository.deleteById(category.getCategoryId());
		assertThat(categoryRepository.count()).isEqualTo(categoryCount - 1);
	}
	
	// Tests of create, edit and delete
	
	@Test
	public void createNewRoleUserPostCategoryComment() {
		// Create one of each domain class
		Role role = new Role("user");
		roleRepository.save(role);
		assertThat(role.getRoleId()).isNotNull();
		User user = new User("user123", "$2a$10$nBgjJ9qQB2poMNZkY2D5HuAscR7wTmOVtJvMb7Dn.r2JNGyCZNvEG", "user123@email.com", role);
		userRepository.save(user);
		assertThat(user.getUserId()).isNotNull();
		Category category = new Category("Movies");
		categoryRepository.save(category);
		assertThat(category.getCategoryId()).isNotNull();
		LocalDateTime dt = LocalDateTime.of(2020, Month.JUNE, 14, 10, 31);
		Post post = new Post("Will anyone read this title?", "Cool test content", dt, category, user);
		postRepository.save(post);
		assertThat(post.getPostId()).isNotNull();
		Comment comment = new Comment("Nice comment", dt, post, user);
		commentRepository.save(comment);
		assertThat(comment.getCommentId()).isNotNull();
		
	}
	
	@Test
	public void editNewRoleUserPostCategoryComment() {
		// Create one of each domain class
		Role role = new Role("user");
		roleRepository.save(role);
		assertThat(role.getRoleId()).isNotNull();
		User user = new User("user123", "$2a$10$nBgjJ9qQB2poMNZkY2D5HuAscR7wTmOVtJvMb7Dn.r2JNGyCZNvEG", "user123@email.com", role);
		userRepository.save(user);
		assertThat(user.getUserId()).isNotNull();
		Category category = new Category("Movies");
		categoryRepository.save(category);
		assertThat(category.getCategoryId()).isNotNull();
		LocalDateTime dt = LocalDateTime.of(2020, Month.JUNE, 14, 10, 31);
		Post post = new Post("Will anyone read this title?", "Cool test content", dt, category, user);
		postRepository.save(post);
		assertThat(post.getPostId()).isNotNull();
		Comment comment = new Comment("Nice comment", dt, post, user);
		commentRepository.save(comment);
		assertThat(comment.getCommentId()).isNotNull();
		
		category.setName("TV");
		categoryRepository.save(category);
		assertThat(categoryRepository.findByName("TV")).isNotNull();
		
		
	}
	
	@Test
	public void deleteNewRoleUserPostCategoryComment() {
		// Create one of each domain class
		Role role = new Role("user");
		roleRepository.save(role);
		assertThat(role.getRoleId()).isNotNull();
		User user = new User("user123", "$2a$10$nBgjJ9qQB2poMNZkY2D5HuAscR7wTmOVtJvMb7Dn.r2JNGyCZNvEG", "user123@email.com", role);
		userRepository.save(user);
		assertThat(user.getUserId()).isNotNull();
		Category category = new Category("Movies");
		categoryRepository.save(category);
		assertThat(category.getCategoryId()).isNotNull();
		LocalDateTime dt = LocalDateTime.of(2020, Month.JUNE, 14, 10, 31);
		Post post = new Post("Will anyone read this title?", "Cool test content", dt, category, user);
		postRepository.save(post);
		assertThat(post.getPostId()).isNotNull();
		Comment comment = new Comment("Nice comment", dt, post, user);
		commentRepository.save(comment);
		assertThat(comment.getCommentId()).isNotNull();
		
		// Delete comment, post and category
		
		long commentCount = commentRepository.count();
		long postCount = postRepository.count();
		long categoryCount = categoryRepository.count();
		
		commentRepository.delete(comment);
		assertThat(commentCount == commentRepository.count() - 1);
		postRepository.delete(post);
		assertThat(postCount == postRepository.count() - 1);
		categoryRepository.delete(category);
		assertThat(categoryCount == categoryRepository.count() - 1);
	}

}
