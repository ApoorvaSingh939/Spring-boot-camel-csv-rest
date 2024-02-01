package com.apoorva.UserAssignment.exceptions;

/*
 * A custom Exception for Invalid Request Handling
 * @param message
 */
public class BadRequestException extends RuntimeException {
	public BadRequestException(String message) {
		super(message);
	}
}
