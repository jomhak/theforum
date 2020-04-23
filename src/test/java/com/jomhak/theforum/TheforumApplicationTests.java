package com.jomhak.theforum;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jomhak.theforum.control.CategoryController;
import com.jomhak.theforum.control.CommentController;
import com.jomhak.theforum.control.PostController;
import com.jomhak.theforum.control.TheforumController;
import com.jomhak.theforum.control.UserController;

@SpringBootTest
class TheforumApplicationTests {

	@Autowired
	private CategoryController categoryController;
	
	@Autowired
	private CommentController commentController;
	
	@Autowired
	private PostController postController;
	
	@Autowired
	private TheforumController theforumController;
	
	@Autowired
	private UserController userController;
	
	
	@Test
	void contextLoads() throws Exception {
		assertThat(categoryController).isNotNull();
		assertThat(commentController).isNotNull();
		assertThat(postController).isNotNull();
		assertThat(theforumController).isNotNull();
		assertThat(userController).isNotNull();
	}

}
