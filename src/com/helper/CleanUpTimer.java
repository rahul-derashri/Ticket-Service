package com.helper;
import java.util.List;
import java.util.Queue;
import java.util.TimerTask;

import com.db.Seat;
import com.db.SeatsDB;
import com.ticketservice.SeatHold;


/**
 * this is Timer thread which continuously check for the expiration 
 * of the seatHold objects to be removed and releases the seats.
 * 
 * @author RahulDerashri
 */
public final class CleanUpTimer extends TimerTask{

	@Override
	public void run() {
		Queue<SeatHold> originalQueue = SeatsDB.getQueue();
		
		while( originalQueue.peek() != null ){
			SeatHold seatHold = originalQueue.peek();
        	if( (System.currentTimeMillis() - seatHold.getTimestamp()) > SeatsDB.TIME_OUT ){
        		originalQueue.poll();
        		SeatsDB.removeFromHoldMap(seatHold.getSeatHoldId());
        		List<Seat> seatList = seatHold.getSeats();
        		Seat[][] seats = SeatsDB.getSeats();
        		for( Seat seat : seatList ){
        			seats[seat.getRowId()][seat.getSeatId()].setStatus(BookingStatus.AVAILABLE);
        		}
        		
        		SeatsDB.increaseAvailability(seatList.size(), seatHold.getVenueLevel());
        	}
        	else{
        		break;
        	}
		}
	}

}
