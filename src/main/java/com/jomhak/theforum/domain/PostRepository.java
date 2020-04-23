package com.jomhak.theforum.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {
	
	public List<Post> findByCategoryName(String category);
	
	public List<Post> findByTitleContaining(String title);

}
