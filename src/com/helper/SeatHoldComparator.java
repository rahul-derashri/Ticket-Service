package com.helper;
import java.util.Comparator;

import com.ticketservice.SeatHold;


/**
 * This is a comparator to sort SeatHold objects based on 
 * creation timestamp (oldest first) used by holdingQueue.
 * 
 * @author RahulDerashri
 */
public class SeatHoldComparator implements Comparator<SeatHold>{

	@Override
	public int compare(SeatHold o1, SeatHold o2) {
		if(o1.getTimestamp() <= o2.getTimestamp()){
			return -1;
		}
		
		return 1;
	}

}
