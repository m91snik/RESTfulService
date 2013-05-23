package com.example.restful.dao;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.example.restful.dom.User;
import com.example.restful.exception.UserDaoException;

@Repository
public class UserDaoInMemoryImpl implements UserDao {

	private final AtomicLong USER_ID_SEQ = new AtomicLong();
	private static final ConcurrentMap<Long, User> USERS_MAP = new ConcurrentHashMap<Long, User>();

	@Override
	public User create(User user) {
		if (findUserByEmail(user.getEmail()) != null) {
			throw new UserDaoException("Email " + user.getEmail()
					+ " already exists");
		}
		user.setId(USER_ID_SEQ.incrementAndGet());
		getUsersMap().put(user.getId(), user);
		return user;
	}

	@Override
	public User read(long userId) {
		return getUsersMap().get(userId);
	}

	@Override
	public User update(User user) {
		User storedUser = getUsersMap().get(user.getId());
		if (!storedUser.getEmail().equals(user.getEmail())) {
			if (findUserByEmail(user.getEmail()) != null) {
				throw new UserDaoException("Email " + user.getEmail()
						+ " already exists");
			}
		}
		User updatedUser = getUsersMap().replace(user.getId(), user);
		return updatedUser;
	}

	@Override
	public User delete(long userId) {
		User removedUser = getUsersMap().remove(userId);
		return removedUser;
	}

	@Override
	public Collection<User> list() {
		return getUsersMap().values();
	}

	// NOTE: it's primarily for testing purposes because when we work with DB we
	// can access it directly, so here we should have ability to access store
	// directly too
	public ConcurrentMap<Long, User> getUsersMap() {
		return USERS_MAP;
	}

	@Override
	public User findUserByEmail(String email) {
		for (User user : USERS_MAP.values()) {
			if (user.getEmail().equals(email)) {
				return user;
			}
		}
		return null;
	}

}
