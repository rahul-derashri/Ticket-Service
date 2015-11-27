package com.helper;


/**
 * This enum hold the possible booking status 
 * @author RahulDerashri
 * 
 */
public enum BookingStatus {
	RESERVED,  // Permanently reserved seats
	HOLD,      // Holed seats which can be either confirmed or will expire and become available
	AVAILABLE  // available seats
}
