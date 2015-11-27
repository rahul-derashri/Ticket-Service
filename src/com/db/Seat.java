package com.db;

import com.helper.BookingStatus;


/**
 * The class contains information about the the seats such as rowId, seatId and status.
 * @author RahulDerashri
 *
 */
public class Seat {
	private int rowId;
	private int seatId;
	private BookingStatus status;
	
	public Seat(){
		this.status = BookingStatus.AVAILABLE;
	}
	
	public int getRowId() {
		return rowId;
	}
	public void setRowId(int rowId) {
		this.rowId = rowId;
	}
	public int getSeatId() {
		return seatId;
	}
	public void setSeatId(int seatId) {
		this.seatId = seatId;
	}
	
	@Override
	public String toString() {
		return String.valueOf(seatId+1);
	}
	
	public BookingStatus getStatus() {
		return status;
	}
	
	public void setStatus(BookingStatus status) {
		this.status = status;
	}
	
}
