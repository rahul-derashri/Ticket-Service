package com.ticketservice;
import java.util.List;

import com.db.Seat;
import com.db.SeatsDB;
import com.helper.VenueLevel;


/**
 * This class hold all the booking information related to user
 * @author RahulDerashri
 */
public class SeatHold{
	private int seatHoldId;
	private String emailId;
	private int noOfSeats;
	private VenueLevel venueLevel;
	private List<Seat> seats;
	// timestamp to keep track of the expiration
	private long timestamp;
	private String confirmationCode; 
	private double totalAmount;
	
	public SeatHold(int noOfSeats, VenueLevel venueLevel){
		this.seatHoldId = SeatsDB.generateSeatHoldId();
		this.noOfSeats = noOfSeats;
		this.venueLevel = venueLevel;
		this.timestamp = System.currentTimeMillis();
		this.confirmationCode = null;
		this.totalAmount = venueLevel.getPrice() * noOfSeats;
	}
	
	public int getSeatHoldId() {
		return seatHoldId;
	}
	
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public int getNoOfSeats() {
		return noOfSeats;
	}
	
	public void setNoOfSeats(int noOfSeats) {
		this.noOfSeats = noOfSeats;
	}
	
	public VenueLevel getVenueLevel() {
		return venueLevel;
	}
	
	public void setVenueLevel(VenueLevel venueLevel) {
		this.venueLevel = venueLevel;
	}

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public String getConfirmationCode() {
		return confirmationCode;
	}

	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}
	
	public double getTotalAmount() {
		return totalAmount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("\n****** Booking Details ******");
		builder.append("\nBooking ID:  "+seatHoldId);
		
		if( confirmationCode != null )
			builder.append("\n COnfirmation Code :"+confirmationCode);
		
		builder.append("\nEmailID:  "+emailId);
		builder.append("\nNumber of seats reserved:  "+noOfSeats);
		builder.append("\nVenue Level:  "+venueLevel);
		builder.append("\nSeats Details :");
		int rowId = seats.get(0).getRowId();
		builder.append(" Row :"+(rowId+1)+" Seat Numbers :");
		for( Seat seat : seats ){
			if( rowId != seat.getRowId() ){
				rowId = seat.getRowId();
				builder.append("\n                Row :"+(rowId+1)+" Seat Numbers :");
			}
			builder.append(seat+" ");
		}
		builder.append("\nStatus :"+seats.get(0).getStatus());
		builder.append("\nTotal Amount :"+totalAmount);
		builder.append("\n****** ***************** ******\n");
		return builder.toString();
	}

}
