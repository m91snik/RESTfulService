package com.example.restful.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.restful.dom.User;
import com.example.restful.service.UserService;

@Controller
@RequestMapping(value = "/")
public class HomeController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(HomeController.class);

	@Autowired
	UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	public String defaultPage() {
		return "redirect:/login.html";
	}

	@RequestMapping(value = "/logged_in")
	public String afterLogin(HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		if (authentication.getAuthorities().contains(
				new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return "redirect:/admin.html";
		}
		User user = userService.findByUsername(authentication.getName());
		response.addCookie(new Cookie("user_id", user.getId() + ""));
		return "redirect:/user.html";
	}

	@RequestMapping(value = "/forgot_password", method = RequestMethod.GET)
	public @ResponseBody
	void forgotPassword(@RequestParam String email) {
		userService.forgotPassword(email);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public String handleServerErrors(Exception ex) {
		LOGGER.error(ex.getMessage(), ex);
		return ex.getMessage();
	}
}
