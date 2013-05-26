package com.example.restful.service;

import java.util.Collection;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.restful.dom.User;

public interface UserService extends UserDetailsService {
	User create(User user);

	User find(long userId);

	User findByUsername(String username);

	User update(User user);

	void delete(long userId);

	Collection<User> list();

	void forgotPassword(String email);
}
