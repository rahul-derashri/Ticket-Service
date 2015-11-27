package com.ticketservice;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.db.Seat;
import com.db.SeatsDB;
import com.exceptions.AlgorithmException;
import com.exceptions.InvalidLevelException;
import com.exceptions.NoBookingExistException;
import com.exceptions.NoSeatFoundException;
import com.exceptions.TicketHoldExpiredException;
import com.helper.BookingStatus;
import com.helper.CleanUpTimer;
import com.helper.VenueLevel;


/**
 * This class implement the TicketService interface and the algorithm to find and allocated 
 * the seats to the users based on best possibility and availability
 * 
 * @author RahulDerashri
 */
public class TicketServiceImpl implements TicketService{
	
	// Timer to keep checking the expiration of the seatHold objects 
	Timer timer;
	
	public TicketServiceImpl() {
		timer = new Timer();
		timer.schedule(new CleanUpTimer(), 100, 100);
	}

	
	/**
	* The number of seats in the requested level that are neither held nor reserved
	*
	* @param venueLevel a numeric venue level identifier to limit the search
	* @return the number of tickets available on the provided level
	* @exception throws InvalidLevelException if the level passed is invalid
	*/
	@Override
	public int numSeatsAvailable(Integer venueLevel) throws InvalidLevelException{
		
		if( venueLevel < 1 || venueLevel > VenueLevel.values().length ){
			throw new InvalidLevelException("Invalid Venue level.");
		}
		
		return SeatsDB.getSeatsAvailability(VenueLevel.valueOf(venueLevel));
	}
	
	
	/**
	* Find and hold the best available seats for a customer
	*
	* @param numSeats the number of seats to find and hold
	* @param minLevel the minimum venue level
	* @param maxLevel the maximum venue level
	* @param customerEmail unique identifier for the customer
	* @return a SeatHold object identifying the specific seats and related information
	* @exception throws NoSeatFoundException if number of seats available at each level is less than numSeats
	*            throws InvalidLevelException if levels are invalid
	*            throws AlgorithmException for all other algorithm related exceptions
	*/
	@Override
	public SeatHold findAndHoldSeats(int numSeats, Integer minLevel,
			Integer maxLevel, String customerEmail) throws NoSeatFoundException, InvalidLevelException,
			AlgorithmException{
		if( minLevel > maxLevel || minLevel < 1 || maxLevel > VenueLevel.values().length ){
			throw new InvalidLevelException("Invalid Lowest level and/or Highest level values.");
		}
		
		SeatHold sHold = findAndHoldSeats(numSeats, VenueLevel.valueOf(minLevel), VenueLevel.valueOf(maxLevel));
		
		if( sHold != null ){
			sHold.setEmailId(customerEmail);
			SeatsDB.enqueue(sHold);
			SeatsDB.addToHoldMap(sHold);
		}
		else{
			throw new NoSeatFoundException(numSeats + " not available");
		}
		
		return sHold;
	}

	
	/**
	* Commit seats held for a specific customer
	*
	* @param seatHoldId the seat hold identifier
	* @param customerEmail the email address of the customer to which the seat hold is assigned
	* @return a reservation confirmation code
	* @exception throws TicketHoldExpiredException if the tickets holed are expired before reserving
	*/
	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) throws TicketHoldExpiredException{
		
		SeatHold seatHold = SeatsDB.getFromHoldMap(seatHoldId);
		String confirmationCode = null;
		/* If the corresponding seatHold object is present in the seatHold queue then 
		 * confirms otherwise throw exception conveying expiration.
		 */
		if( !SeatsDB.isPresentInQueue(seatHold) ){
			throw new TicketHoldExpiredException("Seat Hold expired");
		}
		else{
			confirmationCode = confirmSeats(SeatsDB.getQueue().poll());
		}
		
		return confirmationCode;
	}
	
	
	/**
	* gets booking details based on confirmationCode
	*
	* @param confirmationCode booking details identifier
	* @return a SeatHold object containing the booking information
	* @exception throws NoBookingExistException if the confirmationCode does not exists in bookingDetails Map
	*/
	public SeatHold getBookingDetails(String confirmationCode) throws NoBookingExistException{
		SeatHold hold = SeatsDB.getFromBookingMap(confirmationCode);
		
		if( hold == null ){
			throw new NoBookingExistException("No booking exist for this confrimationCode.");
		}
		
		return hold;
	}
	
	
	/**
	* Find and hold the best available seats starting from minLevel for a customer
	*
	* @param numSeats the number of seats to find and hold
	* @param minLevel the minimum venue level
	* @param maxLevel the maximum venue level
	* @return a SeatHold object identifying the specific seats and related information
	* @exception throws AlgorithmException for all algorithm related exceptions
	*/
	private SeatHold findAndHoldSeats(int numSeats, VenueLevel minLevel,
			VenueLevel maxLevel) throws AlgorithmException{
		SeatHold sHold = null;
		
		VenueLevel firstLevelWithSeats = null;
		
		VenueLevel currentLevel = minLevel;
		// Search best contiguous seats possible for the user stating from minLevel 
		while( currentLevel.getLevelId() <= maxLevel.getLevelId() ){
			if( SeatsDB.getSeatsAvailability(currentLevel) >= numSeats ){
				Seat startSeat = findBestSeatsAtLevel(numSeats , currentLevel);
				
				if( startSeat != null ){
					sHold = new SeatHold(numSeats, currentLevel);
					sHold.setSeats(getSeatsList(startSeat , numSeats));
					SeatsDB.reduceAvailability(numSeats, currentLevel);
					
					return sHold;
				}
				else if( firstLevelWithSeats == null ){
					firstLevelWithSeats = currentLevel;
				}
			}
			
			currentLevel = currentLevel.getNext();
		}
		
		/* If the best combination is not possible but the required number of seats are 
		 * available at a level then assign seats based on availability randomly. 
		 */
		if( firstLevelWithSeats != null ){
			sHold = new SeatHold(numSeats, firstLevelWithSeats);
			sHold.setSeats(findRandomSeats( numSeats, firstLevelWithSeats ));
			SeatsDB.reduceAvailability(numSeats, firstLevelWithSeats);
		}
		
		return sHold;
	}
	
	
	/**
	* Find best seats available at venue level
	*
	* @param numSeats the number of seats to find and hold
	* @param level the level at which seats have to be searched
	* @return a Seat object identifying the specific starting seats from the group of contiguous seats
	* @exception throws AlgorithmException for all algorithm related exceptions
	*/
	private Seat findBestSeatsAtLevel(int numSeats, VenueLevel level) throws AlgorithmException{
		for(int counter = level.startIndex(); counter <= level.endIndex(); counter++){
			Seat startSeat = findSeatsAtRow(numSeats , level , counter);
			if( startSeat != null ){
				startSeat.setRowId(counter);
				return startSeat;
			}
		}
		
		return null;
	}
	
	
	
	/**
	* Find best seats available at a particular row of a level
	*
	* @param numSeats the number of seats to find and hold
	* @param level the level at which seats have to be searched
	* @param rowId the row at which seats have to be searched
	* @return a Seat object identifying the specific starting seats from the group of contiguous seats
	* @exception throws AlgorithmException for all algorithm related exceptions
	*/
	private Seat findSeatsAtRow(int numSeats, VenueLevel level , int rowId) throws AlgorithmException{
		Seat[] seats = SeatsDB.getSeats()[rowId];
		int countSeats = 0;
		
		for(int counter = 0; counter < seats.length; counter++){
			if( seats[counter].getStatus().equals(BookingStatus.AVAILABLE) ){
				countSeats++;
			}
			else{
				countSeats = 0;
			}
			
			if( countSeats == numSeats ){
				int startSeatId = counter - numSeats + 1;
				seats[startSeatId].setSeatId(startSeatId);
				return seats[startSeatId];
			}
		}
		
		return null;
	}

	
	/**
	* Generate the list of all contiguous seats to be assigned starting with the given startSeat
	*
	* @param startSeat the startSeat where the best possibility is found
	* @param numSeats the number of seats to find and hold
	* @return a list of seats to be assigned to the user
	*/
	private List<Seat> getSeatsList(Seat startSeat, int numSeats){
		List<Seat> seatList = new ArrayList<Seat>();
		int rowId = startSeat.getRowId();
		int seatId = startSeat.getSeatId();
		Seat[] seats = SeatsDB.getSeats()[rowId];
		int counter = 0;
		while( counter < numSeats ){
			Seat seat = seats[seatId+counter];
			seat.setStatus(BookingStatus.HOLD);
			seat.setSeatId(seatId+counter);
			seat.setRowId(rowId);
			seatList.add(seat);
			counter++;
		}
		
		return seatList;
	}
	
	/**
	* Find random seats at a level based on sequential access and availability
	*
	* @param numSeats the number of seats to find and hold
	* @param level the level at which seats have to be searched
	* @return a list of seats to be assigned to the user
	* @exception throws AlgorithmException for all algorithm related exceptions
	*/
	private List<Seat> findRandomSeats( int numSeats , VenueLevel level ) throws AlgorithmException{
		List<Seat> list = new ArrayList<Seat>();
		boolean seatAssigned = false;
		int counter = level.startIndex();
		while( !seatAssigned && counter <= level.endIndex()){
			Seat[] seats = SeatsDB.getSeats()[counter];
			int innerCounter = 0;
			while( !seatAssigned && innerCounter < seats.length){
				Seat seat = seats[innerCounter];
				if( seat.getStatus().equals(BookingStatus.AVAILABLE) ){
					seat.setStatus(BookingStatus.HOLD);
					seat.setSeatId(innerCounter);
					seat.setRowId(counter);
					list.add(seat);
					
					if( list.size() == numSeats ){
						seatAssigned = true;
					}
				}
				
				innerCounter++;
			}
			
			counter++;
		}
		
		return list;
	}
	
	
	/**
	* Commit seats and update the status of the seats from HOLD to RESERVED and generates a confirmationCode
	*
	* @param seatHold the seat hold object containing details of the seats hold
	* @return a reservation confirmation code
	*/
	public String confirmSeats(SeatHold seatHold){
		List<Seat> seatList = seatHold.getSeats();
		Seat[][] seats = SeatsDB.getSeats();
		for( Seat seat : seatList ){
			seat.setStatus(BookingStatus.RESERVED);
			seats[seat.getRowId()][seat.getSeatId()].setStatus(BookingStatus.RESERVED);
		}
		
		String confirmationCode = String.valueOf(seatHold.getTimestamp());
		seatHold.setConfirmationCode(confirmationCode);
		SeatsDB.addToBookingMap(seatHold);
		SeatsDB.removeFromHoldMap(seatHold.getSeatHoldId());
		
		return confirmationCode;
	}

	
}
