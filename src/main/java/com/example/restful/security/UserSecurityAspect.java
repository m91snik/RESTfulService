package com.example.restful.security;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.restful.dom.User;
import com.example.restful.exception.UserServiceException;
import com.example.restful.service.UserService;

@Aspect
@Component
public class UserSecurityAspect {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserSecurityAspect.class);

	@Autowired
	UserService userService;

	@Before("execution(* *.*(..)) && args(user) && @annotation(com.example.restful.annotation.UserSecurityNeeded)")
	public void checkUserPermission(User user) {
		checkPermission(user.getId());
	}

	@Before("execution(* *.*(..)) && args(userId) && @annotation(com.example.restful.annotation.UserSecurityNeeded)")
	public void checkUserPermission(long userId) {
		checkPermission(userId);
	}

	private void checkPermission(long userId) {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (auth == null) {
			LOGGER.warn("Access is forbidden for not authentacated user with userId "
					+ userId);
			throw new UserServiceException(
					"Access is forbidden for not authenticated user");
		}
		if (auth.getAuthorities().contains(
				new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return;
		}
		User storedUser = userService.find(userId);
		if (auth.getName().equals(storedUser.getEmail())) {
			return;
		}

		LOGGER.warn("Forbidden operation for user " + auth.getName()
				+ " with user " + storedUser.getEmail());
		throw new UserServiceException("Forbidden operation for user "
				+ auth.getName());

	}
}
