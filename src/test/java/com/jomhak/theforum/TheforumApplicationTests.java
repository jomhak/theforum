package com.jomhak.theforum;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jomhak.theforum.control.PostController;

@RunWith(SpringRunner.class)
@SpringBootTest
class TheforumApplicationTests {

	@Autowired
	private PostController postController;
	
	@Test
	void contextLoads() throws Exception {
		assertThat(postController).isNotNull();
	}

}
