package com.example.restful.dao;

import java.util.Collection;

import com.example.restful.dom.User;

public interface UserDao {
	User create(User user);

	User read(long userId);

	User update(User user);

	User delete(long userId);

	Collection<User> list();

	User findUserByEmail(String email);
}
