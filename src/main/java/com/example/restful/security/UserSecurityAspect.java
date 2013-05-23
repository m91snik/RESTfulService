package com.example.restful.security;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.restful.dom.User;
import com.example.restful.service.UserService;

@Aspect
@Component
public class UserSecurityAspect {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserSecurityAspect.class);

	@Autowired
	UserService userService;

	//
	@Before("execution(* *.*(..)) && args(user) && @annotation(com.example.restful.annotation.UserSecurityNeeded)")
	public void checkUserPermission(User user) {
		checkPermission(user.getId());
	}

	private void checkPermission(long userId) {
		User storedUser = userService.read(userId);
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (auth == null) {
			LOGGER.warn("Access is forbidden for not authentacated user with user "
					+ storedUser.getEmail());
			throw new SecurityException(
					"Access is forbidden for not authentacated user");
		}
		if (auth.getAuthorities().contains("ROLE_ADMIN")
				|| auth.getName().equals(storedUser.getEmail())) {
			return;
		}

		LOGGER.warn("Forbidden operation for user " + auth.getName()
				+ " with user " + storedUser.getEmail());
		throw new SecurityException("Forbidden operation for user "
				+ auth.getName());

	}

	@Before("execution(* *.*(..)) && args(userId) && @annotation(com.example.restful.annotation.UserSecurityNeeded)")
	public void checkUserPermission(long userId) {
		checkPermission(userId);
	}

}
