package com.example.restful.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restful.annotation.UserSecurityNeeded;
import com.example.restful.dao.UserDao;
import com.example.restful.dom.User;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;

	@Autowired
	MailSender mailSender;

	@Override
	public User create(User user) {
		return userDao.create(user);
	}

	@Transactional(readOnly = true)
	@Override
	public User read(long userId) {
		return userDao.read(userId);
	}

	@UserSecurityNeeded
	@Override
	public User update(User user) {
		return userDao.update(user);
	}

	@UserSecurityNeeded
	@Override
	public User delete(long userId) {
		return userDao.delete(userId);
	}

	@Transactional(readOnly = true)
	@Override
	public Collection<User> list() {
		return userDao.list();
	}

	@Override
	public void forgotPassword(long userId) {
		User user = read(userId);
		if (user == null) {
			throw new RuntimeException("No user find for specified id");
		}
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setCc(user.getEmail());
		// NOTE: can be extracted to messages file
		simpleMailMessage.setSubject("Your password for RESTfulService");
		simpleMailMessage.setText("Hi. Your password is " + user.getPassword());
		mailSender.send(simpleMailMessage);
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		// NOTE: username==email
		User user = userDao.findUserByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(username + " was not found");
		}
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return new org.springframework.security.core.userdetails.User(
				user.getEmail(), user.getPassword(), authorities);
	}

}
