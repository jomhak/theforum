package com.jomhak.theforum;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jomhak.theforum.domain.Category;
import com.jomhak.theforum.domain.CategoryRepository;
import com.jomhak.theforum.domain.CommentRepository;
import com.jomhak.theforum.domain.PostRepository;
import com.jomhak.theforum.domain.Role;
import com.jomhak.theforum.domain.RoleRepository;
import com.jomhak.theforum.domain.UserRepository;

@RunWith(SpringRunner.class)
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
	
	// Comment tests
	
	@Test
	public void createNewRoleUserPostCategoryComment() {
		Role role = new Role("user");
		roleRepository.save(role);
		
		Category category = new Category("Movies");
		
		categoryRepository.save(category);
		assertThat(categoryRepository.findByName("Movies")).isNotNull();
	}
	
	@Test
	public void editNewRoleUserPostCategoryComment() {
		Category category = new Category("Movies");
		categoryRepository.save(category);
		assertThat(category.getCategoryId()).isNotNull();
		category.setName("TV");
		categoryRepository.save(category);
		assertThat(category.getCategoryId()).isNotNull();
	}
	
	@Test
	public void deleteNewRoleUserPostCategoryComment() {
		Category category = new Category("Movies");
		categoryRepository.save(category);
		assertThat(category.getCategoryId()).isNotNull();
		long categoryCount = categoryRepository.count();
		categoryRepository.deleteById(category.getCategoryId());
		assertThat(categoryRepository.count()).isEqualTo(categoryCount - 1);
	}

}
