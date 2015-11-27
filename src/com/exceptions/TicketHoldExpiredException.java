package com.exceptions;


/**
 * This class is a custom exception thrown when SeatHold 
 * object expires and user tries to access/confirmation the same. 
 * 
 * @author RahulDerashri
 */
public class TicketHoldExpiredException extends RuntimeException{
	
	private static final long serialVersionUID = 8013048017325906051L;

	public TicketHoldExpiredException(String message){
		super(message);
	}
}
