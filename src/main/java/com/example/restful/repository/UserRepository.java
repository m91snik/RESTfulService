package com.example.restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restful.dom.User;

public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Find user by email
	 * 
	 * @param email
	 * @return
	 */
	User findOneByEmail(String email);

}
