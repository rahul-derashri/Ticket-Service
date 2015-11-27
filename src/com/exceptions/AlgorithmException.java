package com.exceptions;


/**
 * This class is a custom exception thrown when any exception occurs 
 * during search for best seats
 * 
 * @author RahulDerashri
 */
public class AlgorithmException extends RuntimeException{

	private static final long serialVersionUID = -8106794660204919432L;

	public AlgorithmException(String message) {
		super(message);
	}
}
