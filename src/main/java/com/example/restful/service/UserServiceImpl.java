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
import com.example.restful.dom.User;
import com.example.restful.exception.UserServiceException;
import com.example.restful.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MailSender mailSender;

	@Override
	public User create(User user) {
		if (getUserRepository().findOneByEmail(user.getEmail()) != null) {
			throw new UserServiceException("Duplicated email");
		}
		return getUserRepository().save(user);
	}

	@Transactional(readOnly = true)
	@Override
	public User find(long userId) {
		return getUserRepository().findOne(userId);
	}

	@UserSecurityNeeded
	@Override
	public User update(User user) {
		User storedUser = getUserRepository().findOne(user.getId());
		if (storedUser == null) {
			throw new UserServiceException("No such user exists");
		}
		User userWithTheSameEmail = getUserRepository().findOneByEmail(
				user.getEmail());
		if (userWithTheSameEmail != null
				&& !storedUser.getId().equals(userWithTheSameEmail.getId())) {
			throw new UserServiceException("Duplicated email");
		}
		return getUserRepository().save(user);
	}

	@UserSecurityNeeded
	@Override
	public void delete(long userId) {
		getUserRepository().delete(userId);
	}

	@Transactional(readOnly = true)
	@Override
	public Collection<User> list() {
		return getUserRepository().findAll();
	}

	@Override
	public void forgotPassword(String email) {
		User user = getUserRepository().findOneByEmail(email);
		if (user == null) {
			throw new RuntimeException("No user find for specified id");
		}
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setCc(user.getEmail());
		// NOTE: can be extracted to messages file
		simpleMailMessage.setSubject("Your password for RESTfulService");
		simpleMailMessage.setText("Hi. Your password is " + user.getPassword());
		getMailSender().send(simpleMailMessage);
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		// NOTE: username==email
		User user = getUserRepository().findOneByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(username + " was not found");
		}
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return new org.springframework.security.core.userdetails.User(
				user.getEmail(), user.getPassword(), authorities);
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public MailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

}
