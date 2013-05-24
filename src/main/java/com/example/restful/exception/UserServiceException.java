package com.example.restful.exception;

public class UserServiceException extends RuntimeException {

	private static final long serialVersionUID = 2187233219073318779L;

	public UserServiceException() {
		super();
	}

	public UserServiceException(String message) {
		super(message);
	}

	public UserServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
