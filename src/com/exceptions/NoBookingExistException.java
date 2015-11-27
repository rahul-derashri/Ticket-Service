package com.exceptions;

/**
 * This class is a custom exception thrown when no booking 
 * found in the database for the confirmationCode.
 * 
 * @author RahulDerashri
 */
public class NoBookingExistException extends RuntimeException{

	private static final long serialVersionUID = -417147349641390200L;

	public NoBookingExistException(String message) {
		super(message);
	}
}
