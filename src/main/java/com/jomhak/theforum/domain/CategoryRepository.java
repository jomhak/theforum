package com.jomhak.theforum.domain;

import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {

	
	public Category findByName(String name);
}
