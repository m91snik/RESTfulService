package com.example.restful.service;

import java.util.Collection;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.restful.dom.User;

public interface UserService extends UserDetailsService {
	User create(User user);

	User read(long userId);

	User update(User user);

	User delete(long userId);

	Collection<User> list();

	void forgotPassword(long userId);
}
