package com.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.db.Seat;
import com.db.SeatsDB;
import com.exceptions.InvalidLevelException;
import com.exceptions.NoSeatFoundException;
import com.exceptions.TicketHoldExpiredException;
import com.helper.BookingStatus;
import com.helper.VenueLevel;
import com.ticketservice.SeatHold;
import com.ticketservice.TicketService;
import com.ticketservice.TicketServiceImpl;

public class TestTicketService {
	
	TicketService service;
	
	
	/**
	 * Initialize the database before each test
	 */
	@Before
	public void initialize(){
		service = new TicketServiceImpl();
		SeatsDB.initializeDatabase();
	}
	
	/**
	 * Tests numSeatsAvailable() method after booking seats at each level
	 */
	@Test
	public void testNumSeatsAvailable(){
		
		assertEquals(1250, service.numSeatsAvailable(1));
		assertEquals(2000, service.numSeatsAvailable(2));
		assertEquals(1500, service.numSeatsAvailable(3));
		assertEquals(1500, service.numSeatsAvailable(4));
		
		service.findAndHoldSeats(5, 1, 2, "abc@gmail.com");
		assertEquals(1245, service.numSeatsAvailable(1));
		service.findAndHoldSeats(11, 1, 2, "xyz@gmail.com");
		assertEquals(1234, service.numSeatsAvailable(1));
		service.findAndHoldSeats(30, 1, 2, "pqr@gmail.com");
		assertEquals(1204, service.numSeatsAvailable(1));
		
		service.findAndHoldSeats(5, 2, 2, "abc@gmail.com");
		assertEquals(1995, service.numSeatsAvailable(2));
		service.findAndHoldSeats(11, 2, 2, "xyz@gmail.com");
		assertEquals(1984, service.numSeatsAvailable(2));
		service.findAndHoldSeats(30, 2, 2, "pqr@gmail.com");
		assertEquals(1954, service.numSeatsAvailable(2));
		
		service.findAndHoldSeats(5, 3, 3, "abc@gmail.com");
		assertEquals(1495, service.numSeatsAvailable(3));
		service.findAndHoldSeats(11, 3, 3, "xyz@gmail.com");
		assertEquals(1484, service.numSeatsAvailable(3));
		service.findAndHoldSeats(30, 3, 3, "pqr@gmail.com");
		assertEquals(1454, service.numSeatsAvailable(3));
		
		service.findAndHoldSeats(5, 4, 4, "abc@gmail.com");
		assertEquals(1495, service.numSeatsAvailable(4));
		service.findAndHoldSeats(11, 4, 4, "xyz@gmail.com");
		assertEquals(1484, service.numSeatsAvailable(4));
		service.findAndHoldSeats(30, 4, 4, "pqr@gmail.com");
		assertEquals(1454, service.numSeatsAvailable(4));
		
	}
	
	
	/**
	 * Tests InvalidLevelException whenever the levels entered 
	 * are invalid. 
	 */
	@Test(expected = InvalidLevelException.class)
	public void testNumSeatsAvailable_invalidLevel(){
		
		service.numSeatsAvailable(5);
		service.numSeatsAvailable(-1);
		service.numSeatsAvailable(0);
		service.numSeatsAvailable(6);
		
	}
	
	
	/**
	 * Tests InvalidLevelException whenever the levels entered 
	 * are invalid or order of levels are invalid 
	 * (minLevel should be lesser than maxLevel) 
	 */
	@Test(expected = InvalidLevelException.class)
	public void testFindAndHoldSeats_invalidLevel(){
		
		service.findAndHoldSeats(5, 5, 5, "abc@gmail.com");
		service.findAndHoldSeats(5, 0, 5, "abc@gmail.com");
		service.findAndHoldSeats(5, 0, 3, "abc@gmail.com");
		service.findAndHoldSeats(5, 1, 5, "abc@gmail.com");
		service.findAndHoldSeats(5, -1, 4, "abc@gmail.com");
		service.findAndHoldSeats(5, 1, -4, "abc@gmail.com");
		service.findAndHoldSeats(5, 4, 1, "abc@gmail.com");
		service.findAndHoldSeats(5, 2, 1, "abc@gmail.com");
		
	}
	
	
	/**
	 * Tests the seats status before confirmation
	 */
	@Test
	public void testFindAndHoldSeats_seatsStatusHold(){
		SeatHold hold = null;
		
		hold = service.findAndHoldSeats(5, 1, 2, "abc@gmail.com");
		
		for( Seat seat : hold.getSeats() ){
			assertEquals(BookingStatus.HOLD, seat.getStatus());
		}
		
	}
	
	
	/**
	 * Tests the seats allocation based on priority of the levels
	 */
	@Test
	public void testFindAndHoldSeats_levelPriority(){
		
		SeatHold hold = null;
		int counter = 0;
		
		while( counter < 25 ){
			service.findAndHoldSeats(25, 1, 4, "abc"+counter+"@gmail.com");
			service.findAndHoldSeats(25, 1, 4, "abc"+counter+"@gmail.com");
			counter++;
		}
		hold = service.findAndHoldSeats(25, 1, 4, "abc"+26+"@gmail.com");
		assertEquals(VenueLevel.MAIN, hold.getVenueLevel());
		
		counter = 0;
		
		service.findAndHoldSeats(75, 1, 4, "abc"+26+"@gmail.com");
		while( counter < 19 ){
			service.findAndHoldSeats(50, 1, 4, "abc"+counter+"@gmail.com");
			service.findAndHoldSeats(50, 1, 4, "abc"+counter+"@gmail.com");
			counter++;
		}
		
		hold = service.findAndHoldSeats(25, 1, 4, "abc"+26+"@gmail.com");
		assertEquals(VenueLevel.BALCONY1, hold.getVenueLevel());
		
		counter = 0;
		
		service.findAndHoldSeats(75, 1, 4, "abc"+26+"@gmail.com");
		
		while( counter < 14 ){
			service.findAndHoldSeats(50, 1, 4, "abc"+counter+"@gmail.com");
			service.findAndHoldSeats(50, 1, 4, "abc"+counter+"@gmail.com");
			counter++;
		}
		
		hold = service.findAndHoldSeats(25, 1, 4, "abc"+26+"@gmail.com");
		assertEquals(VenueLevel.BALCONY2, hold.getVenueLevel());
		
	}
	
	
	/**
	 * Tests NoSeatFoundException when required number of seats 
	 * are not available at any level from the range of levels entered
	 */
	@Test(expected = NoSeatFoundException.class)
	public void testFindAndHoldSeats_noSeatAvailable(){
		int counter = 0;
		
		while( counter < 25 ){
			service.findAndHoldSeats(24, 1, 1, "abc"+counter+"@gmail.com");
			service.findAndHoldSeats(25, 1, 1, "abc"+counter+"@gmail.com");
			counter++;
		}
		
		service.findAndHoldSeats( 26, 1, 1, "abc26@gmail.com");
	}
	
	
	/**
	 * Tests the totalAmount calculation for each level
	 */
	@Test
	public void testReserveSeats_totalAmount(){
		SeatHold hold = null;
		
		hold = service.findAndHoldSeats(5, 1, 2, "abc@gmail.com");
		assertEquals(5*VenueLevel.ORCHESTRA.getPrice(), hold.getTotalAmount(),0.00);
		
		hold = service.findAndHoldSeats(5, 2, 2, "abc@gmail.com");
		assertEquals(5*VenueLevel.MAIN.getPrice(), hold.getTotalAmount(), 0.00);
		
		hold = service.findAndHoldSeats(5, 3, 3, "abc@gmail.com");
		assertEquals(5*VenueLevel.BALCONY1.getPrice(), hold.getTotalAmount(), 0.00);
		
		hold = service.findAndHoldSeats(5, 4, 4, "abc@gmail.com");
		assertEquals(5*VenueLevel.BALCONY2.getPrice(), hold.getTotalAmount(), 0.00);
		
	}
	
	
	
	/*@Test
	public void testFindAndHoldSeats_seatAllocationLevel(){
		SeatHold hold = null;
		int counter = 0;
		
		while( counter < 25 ){
			service.findAndHoldSeats(24, 1, 1, "abc"+counter+"@gmail.com");
			service.findAndHoldSeats(25, 1, 1, "abc"+counter+"@gmail.com");
			counter++;
		}
		
		hold = service.findAndHoldSeats( 22, 1, 1, "abc26@gmail.com");
		assertEquals( VenueLevel.ORCHESTRA, hold.getVenueLevel());
		
		counter = 0;
		while( counter < 20 ){
			service.findAndHoldSeats(48, 2, 2, "abc"+counter+"@gmail.com");
			service.findAndHoldSeats(49, 2, 2, "abc"+counter+"@gmail.com");
			counter++;
		}
		
		hold = service.findAndHoldSeats( 5, 1, 2, "abc26@gmail.com");
		assertEquals( VenueLevel.MAIN, hold.getVenueLevel());
	}*/
	
	
	
	/**
	 * Tests TicketHoldExpiredException when user tries to confirmation 
	 * the seats after expiration timeout.
	 */
	@Test(expected = TicketHoldExpiredException.class)
	public void testReserveSeats_expiration(){
		SeatHold hold = service.findAndHoldSeats(5, 1, 2, "abc@gmail.com");
		assertEquals(1245, service.numSeatsAvailable(1));
		
		while( System.currentTimeMillis() - hold.getTimestamp() < SeatsDB.TIME_OUT+100 ){
			
		}
		service.reserveSeats(hold.getSeatHoldId(), hold.getEmailId());
		assertEquals(1250, service.numSeatsAvailable(1));
		
	}
	
	
	/**
	 * Tests the seats status after confirmation
	 */
	@Test
	public void testGetBookingDetails_seatsStatusReserved(){
		SeatHold hold = null;
		String confirmationCode = null;
		
		hold = service.findAndHoldSeats(5, 1, 2, "abc@gmail.com");
		confirmationCode = service.reserveSeats(hold.getSeatHoldId(), hold.getEmailId());
		hold = service.getBookingDetails(confirmationCode);
		
		for( Seat seat : hold.getSeats() ){
			assertEquals(BookingStatus.RESERVED, seat.getStatus());
		}
		
	}
	
}
