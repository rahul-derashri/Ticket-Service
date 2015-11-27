package com.usage;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.db.SeatsDB;
import com.ticketservice.TicketService;
import com.ticketservice.TicketServiceImpl;


public class BookTicket {
	public static void main(String[] args) {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		TicketService service = new TicketServiceImpl();
		String emailID = null;
		int numSeats = 0;
		int seatHoldId = 0;
		int level = 0;
		int startLevel = 0;
		int endLevel = 0;
		while(true){
			try {
				System.out.println("Please select from the options");
				System.out.println("1 - Book tickets");
				System.out.println("2 - Confirm tickets");
				System.out.println("3 - Check seats availability at level");
				System.out.println("4 - How many are on Hold?");
				System.out.println("5 - How many are Confirmed?");
				System.out.println("6 - Reset the Ticket Service System");
				System.out.println("7 - Exit");
				String userEntry = reader.readLine();
				
				switch (userEntry) {
					case "1":
						System.out.println("Please Enter EmailID:");
						emailID = reader.readLine();
						System.out.println("Please Enter Number of seats:");
						numSeats = Integer.parseInt(reader.readLine());
						System.out.println("Please Enter lowest level:");
						startLevel = Integer.parseInt(reader.readLine());
						System.out.println("Please Enter end level:");
						endLevel = Integer.parseInt(reader.readLine());
						System.out.println(service.findAndHoldSeats(numSeats, startLevel, endLevel, emailID));
						break;
					case "2":
						System.out.println("Please Enter Book Hold ID:");
						seatHoldId = Integer.parseInt(reader.readLine());
						System.out.println("Please Enter EmailID:");
						emailID = reader.readLine();
						System.out.println(service.reserveSeats(seatHoldId, emailID));
						break;
					case "3":
						System.out.println("Please enter level:");
						level = Integer.parseInt(reader.readLine());
						System.out.println("Number of seats available at "+level+": "+service.numSeatsAvailable(level));
						break;
					case "4":
						System.out.println(SeatsDB.getSizeofHoldQ());
						break;
					case "5":
						System.out.println(SeatsDB.getSizeofBookingMap());
						break;
					case "6":
						SeatsDB.initializeDatabase();
						System.out.println("Database Initialized");
						break;
					default:
						System.exit(0);
				}
				
				/*if( userEntry.equalsIgnoreCase("1") ){
					System.out.println("Please Enter EmailID:");
					emailID = reader.readLine();
					System.out.println("Please Enter Number of seats:");
					numSeats = Integer.parseInt(reader.readLine());
					System.out.println("Please Enter lowest level:");
					startLevel = Integer.parseInt(reader.readLine());
					System.out.println("Please Enter end level:");
					endLevel = Integer.parseInt(reader.readLine());
					System.out.println(service.findAndHoldSeats(numSeats, startLevel, endLevel, emailID));
				}
				else if( userEntry.equalsIgnoreCase("2") ){
					System.out.println("Please Enter Book Hold ID:");
					seatHoldId = Integer.parseInt(reader.readLine());
					System.out.println("Please Enter EmailID:");
					emailID = reader.readLine();
					System.out.println(service.reserveSeats(seatHoldId, emailID));
				}
				else if( userEntry.equalsIgnoreCase("3") ){
					System.out.println("Please enter level:");
					level = Integer.parseInt(reader.readLine());
					System.out.println("Number of seats available at "+level+": "+service.numSeatsAvailable(level));
				}
				else if( userEntry.equalsIgnoreCase("4") ){
					System.out.println(SeatsDB.getSizeofHoldQ());
				}
				else if( userEntry.equalsIgnoreCase("5") ){
					System.out.println(SeatsDB.getSizeofBookingMap());
				}
				else if( userEntry.equalsIgnoreCase("6") ){
					SeatsDB.initializeDatabase();
				}
				else{
					System.exit(0);
				}*/
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
}
