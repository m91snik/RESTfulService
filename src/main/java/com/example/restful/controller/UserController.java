package com.example.restful.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.restful.dom.User;
import com.example.restful.exception.UserServiceException;
import com.example.restful.service.UserService;

@Controller
@RequestMapping(value = "/users")
public class UserController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public User create(@Valid @RequestBody User user) {
		LOGGER.info("Creating new user {}", user);
		return userService.create(user);
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public User find(@PathVariable(value = "userId") long userId) {
		LOGGER.info("Reading user with id {}", userId);
		return userService.find(userId);
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void update(@PathVariable(value = "userId") long userId,
			@Valid @RequestBody User user) {
		user.setId(userId);
		LOGGER.info("Updating user with id {} with {}", userId, user);
		userService.update(user);
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(value = "userId") long userId) {
		LOGGER.info("Deleting user with id {}", userId);
		userService.delete(userId);
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<User> list() {
		LOGGER.info("Listing users");
		return new ArrayList<User>(userService.list());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String handleClientErrors(Exception ex) {
		LOGGER.error(ex.getMessage(), ex);
		return ex.getMessage();
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public String handleServerErrors(Exception ex) {
		LOGGER.error(ex.getMessage(), ex);
		return "An internal server error occured:" + ex.getMessage();
	}

	@ExceptionHandler(UserServiceException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public String handleDataAccessErrors(UserServiceException ex) {
		LOGGER.error(ex.getMessage(), ex);
		return ex.getMessage();

	}
}
