package com.example.restful.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Value("${security.adminName}")
	private String adminName;
	@Value("${security.adminPassword}")
	private String adminPassword;

	@Autowired
	UserDetailsService userDetailsService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CustomAuthenticationProvider.class);

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
		// NOTE: as we don't have user name than use email
		String email = String.valueOf(auth.getPrincipal());
		String password = String.valueOf(auth.getCredentials());

		if (!StringUtils.hasText(email) || !StringUtils.hasText(password)) {
			LOGGER.error("Username/password is empty");
			throw new BadCredentialsException("Username/password is empty");
		}
		if (adminName.equals(email) && adminPassword.equals(adminPassword)) {
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			return new UsernamePasswordAuthenticationToken(
					authentication.getName(), authentication.getCredentials(),
					authorities);
		}
		UserDetails userDetails = userDetailsService.loadUserByUsername(email);
		if (!verigyUserPassword(userDetails, password)) {
			LOGGER.error("Username/password is empty");
			throw new BadCredentialsException("Username/password is empty");
		}

		return new UsernamePasswordAuthenticationToken(
				authentication.getName(), authentication.getCredentials(),
				userDetails.getAuthorities());
	}

	private boolean verigyUserPassword(UserDetails userDetails, String password) {
		return userDetails.getPassword().equals(password);
	}

	@Override
	public boolean supports(Class aClass) {
		return (aClass.equals(UsernamePasswordAuthenticationToken.class));
	}

}
