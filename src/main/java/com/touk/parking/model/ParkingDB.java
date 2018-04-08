package com.touk.parking.model;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
 
public class ParkingDB {
  Connection conn;
  Properties prop = new Properties();
 
  public void connectionToDerby() {
    // -------------------------------------------
    // URL format is
    // jdbc:derby:<local directory to save data>
    // -------------------------------------------
	try {
	    String dbUrl = "jdbc:derby:parkingDB;create=true";
	    conn= DriverManager.getConnection(dbUrl);
	}catch (SQLException e){
		System.out.println(e);
	}
  }
 
  public void createNewTables(){
	  try {
		Statement stmt = conn.createStatement();
	 
	    // Drop tables
	    stmt.executeUpdate("Drop Table driver");
	    stmt.executeUpdate("Drop Table car");
	    stmt.executeUpdate("Drop Table receipt");
	    stmt.executeUpdate("Drop Table parking");
	    stmt.executeUpdate("Drop Table parkingmeter");
	    stmt.executeUpdate("Drop Table currency");
	  } catch (Exception e){
		  System.out.println(e);
	  } 
	  
	  try{
		Statement stmt = conn.createStatement();
		
		//Create new tables (create table if not exists is not possible in Derby DB)
		stmt.executeUpdate("Create table driver (driverID int primary key, name varchar(30),isVIP boolean)");
	    stmt.executeUpdate("Create table car (carID int primary key, driverID int, plateNumber varchar(30))");
	    stmt.executeUpdate("Create table receipt (carID int,parkingMeterID int, isDriverVIPWhenStarted boolean,startingTime Date,endingTime Date, price float,currencyID int)");
	    stmt.executeUpdate("Create table parkingmeter (parkingMeterID int primary key, parkingID int)");
	    stmt.executeUpdate("Create table parking (parkingID int primary key, name varchar(30))");
	    stmt.executeUpdate("Create table currency (currencyID int primary key, currencyName varchar(30),initialPrice float)");

	  } catch (SQLException e){
			System.out.println(e);
	  }
  }
  public void prepareTestValues(){
	  try {
		  InputStream input = new FileInputStream("src/resources/TestValues.properties");
		  prop.load(input);

		Statement stmt = conn.createStatement();
		
		//Drivers and cars
	    stmt.executeUpdate("insert into driver values ("+prop.getProperty("driver1ID")+",'"+prop.getProperty("driver1Name")+"','"+prop.getProperty("driver1IsVIP")+"')");
	    stmt.executeUpdate("insert into car values ("+prop.getProperty("car1ID")+","+prop.getProperty("driver1ID")+",'"+prop.getProperty("car1PlateNumber")+"')");
	    stmt.executeUpdate("insert into car values ("+prop.getProperty("car2ID")+","+prop.getProperty("driver1ID")+",'"+prop.getProperty("car2PlateNumber")+"')");
	    
	    stmt.executeUpdate("insert into driver values ("+prop.getProperty("driver2ID")+",'"+prop.getProperty("driver2Name")+"','"+prop.getProperty("driver2IsVIP")+"')");
	    stmt.executeUpdate("insert into car values ("+prop.getProperty("car3ID")+","+prop.getProperty("driver2ID")+",'"+prop.getProperty("car3PlateNumber")+"')");
	    stmt.executeUpdate("insert into car values ("+prop.getProperty("car4ID")+","+prop.getProperty("driver2ID")+",'"+prop.getProperty("car4PlateNumber")+"')");
	    
	    //Parkings and parking meters
	    stmt.executeUpdate("insert into parking values ("+prop.getProperty("parking1ID")+",'"+prop.getProperty("parking1Name")+"')");
	    stmt.executeUpdate("insert into parkingmeter values ("+prop.getProperty("parkingMeter1ID")+","+prop.getProperty("parking1ID")+")");
	    stmt.executeUpdate("insert into parkingmeter values ("+prop.getProperty("parkingMeter2ID")+","+prop.getProperty("parking1ID")+")");

	    //Currency
	    stmt.executeUpdate("insert into currency values ("+prop.getProperty("currency1ID")+",'"+prop.getProperty("currency1Name")+"',"+prop.getProperty("currency1InitialPrice")+")");
	  } catch (Exception e){
			System.out.println(e);
	  }
  }
  
}