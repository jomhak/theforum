package com.jomhak.theforum.domain;

import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
	
	public Role findByName(String name);

}
