package com.exceptions;

/**
 * This class is a custom exception thrown when the levels entered are invalid
 * 
 * @author RahulDerashri
 */
public class InvalidLevelException extends RuntimeException{
	
	private static final long serialVersionUID = 2009158725928578942L;

	public InvalidLevelException(String message) {
		super(message);
	}
}
