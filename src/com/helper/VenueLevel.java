package com.helper;
import java.util.HashMap;
import java.util.Map;


/**
 * This enum contains the information such as levelId, name, price per 
 * seat, number of rows and seats per row for each Venue level
 * 
 * @author RahulDerashri
 */
public enum VenueLevel {
	
	ORCHESTRA(1, 100.00 , 25 , 50),
	MAIN(2, 75.0 , 20, 100),
	BALCONY1(3, 50.0, 15 , 100),
	BALCONY2(4, 25.0, 15 ,100);
	
	private int levelId;
	private double price;
	private int rows;
	private int rowSeats;
	
	private static Map<Integer, VenueLevel> levelMap = new HashMap<Integer, VenueLevel>();
	
	static{
		for( VenueLevel level : VenueLevel.values() ){
			levelMap.put(level.levelId, level);
		}
	}
	
	private VenueLevel(int levelId, double price, int rows, int rowSeats){
		this.levelId = levelId;
		this.price = price;
		this.rows = rows;
		this.rowSeats = rowSeats;
	}

	public int getLevelId() {
		return levelId;
	}

	public double getPrice() {
		return price;
	}

	public int getRows() {
		return rows;
	}

	public int getRowSeats() {
		return rowSeats;
	}
	
	public VenueLevel getNext(){
		VenueLevel[] levels = values();
		return levels[(this.ordinal()+1)%levels.length];
	}

	/**
	* Provide the the index of the first row of the level
	*/
	public int startIndex(){
		switch (this) {
			case ORCHESTRA:
				return 0;
			case MAIN:
				return ORCHESTRA.getRows();
			case BALCONY1:
				return ORCHESTRA.getRows()+MAIN.getRows();
			default:
				return ORCHESTRA.getRows()+MAIN.getRows()+BALCONY1.getRows();
		}
	}
	
	/**
	* Provide the the index of the last row of the level
	*/
	public int endIndex(){
		return this.startIndex()+this.getRows()-1;
	}
	
	public static VenueLevel valueOf(int levelId){
		return levelMap.get(levelId);
	}
	
	@Override
	public String toString() {
		return this.getLevelId()+" - "+this.name();
	}
	
}
