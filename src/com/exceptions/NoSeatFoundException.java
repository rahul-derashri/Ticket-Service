package com.exceptions;


/**
 * This class is a custom exception thrown when required number of seats are not available at 
 * the range of levels passed. 
 * 
 * @author RahulDerashri
 */
public class NoSeatFoundException extends RuntimeException{
	
	private static final long serialVersionUID = -3458299376840900755L;

	public NoSeatFoundException(String message){
		super(message);
	}
}
