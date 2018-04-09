package com.touk.parking.model;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.springframework.web.bind.annotation.RequestParam;

import com.touk.parking.Greeting;
 
public class ParkingDB {
  Connection conn;
  Properties prop = new Properties();
  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
 
  public void connectionToDerby() {
	System.out.println("init connectionToDerby");
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
	  System.out.println("init createNewTables");
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
	    stmt.executeUpdate("Create table receipt (carID int,parkingMeterID int, isDriverVIPWhenStarted boolean,startingTime TimeStamp,endingTime TimeStamp, price float)");
	    stmt.executeUpdate("Create table parkingmeter (parkingMeterID int primary key, parkingID int)");
	    stmt.executeUpdate("Create table parking (parkingID int primary key, name varchar(30),currencyID int)");
	    stmt.executeUpdate("Create table currency (currencyID int primary key, currencyName varchar(30),initialPrice float)");

	  } catch (SQLException e){
			System.out.println(e);
	  }
  }
  public void prepareTestValues(){
	  System.out.println("init prepareTestValues");
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
	    stmt.executeUpdate("insert into parking values ("+prop.getProperty("parking1ID")+",'"+prop.getProperty("parking1Name")+"',"+prop.getProperty("parking1CurrencyID")+")");
	    stmt.executeUpdate("insert into parkingmeter values ("+prop.getProperty("parkingMeter1ID")+","+prop.getProperty("parking1ID")+")");
	    stmt.executeUpdate("insert into parkingmeter values ("+prop.getProperty("parkingMeter2ID")+","+prop.getProperty("parking1ID")+")");

	    //Currency
	    stmt.executeUpdate("insert into currency values ("+prop.getProperty("currency1ID")+",'"+prop.getProperty("currency1Name")+"',"+prop.getProperty("currency1InitialPrice")+")");
	  } catch (Exception e){
			System.out.println(e);
	  }
  }
  public Response isCarInMyParking(String plateNumber, int parkingID){
	  try {

		  Statement stmt = conn.createStatement();
		  StringBuffer selectQuery=new StringBuffer("");
		  selectQuery.append("SELECT count(*)");
		  selectQuery.append("		FROM parking, parkingmeter, receipt, car");
		  selectQuery.append("		WHERE parking.parkingID=parkingmeter.parkingID");
		  selectQuery.append("		AND parkingmeter.parkingMeterID=receipt.parkingMeterID");
		  selectQuery.append("		AND receipt.carID=car.carID");
		  selectQuery.append("		AND car.plateNumber='").append(plateNumber).append("'");
		  selectQuery.append("		AND receipt.endingTime is null");
		  
		  ResultSet rs = stmt.executeQuery(new String(selectQuery));
		  if (rs.next()) {
			  	if (rs.getInt(1)==0) {
			  		return new Response("No");
			  	}
			  	else {
			  		return new Response("Yes");
			  	}
	      }
		  
	  } catch (SQLException e) {
		  System.out.println(e);
	  }
	  return null;
  }
  
  public Response startParkingMeter(int carID, int parkingMeterID){
	  try {		  
		  Statement stmt = conn.createStatement();
		  StringBuffer selectDriverIsVIP=new StringBuffer("");
		  selectDriverIsVIP.append("SELECT *");
		  selectDriverIsVIP.append("		FROM car, driver");
		  selectDriverIsVIP.append("		WHERE car.driverID=driver.driverID");
		  selectDriverIsVIP.append("		AND car.carID=").append(carID);
		  
		  ResultSet rsDriverIsVIP = stmt.executeQuery(new String(selectDriverIsVIP));
		  
		  if (rsDriverIsVIP.next()) {
			  LocalDateTime now = LocalDateTime.now();
			  
			  StringBuffer insertQuery=new StringBuffer("");
			  insertQuery.append("insert into Receipt ");
			  insertQuery.append("(carID,parkingMeterID,isDriverVIPWhenStarted,");
			  insertQuery.append("  startingTime,endingTime,price) ");
			  insertQuery.append("  values (");
			  insertQuery.append(carID).append(",");
			  insertQuery.append(parkingMeterID).append(",");
			  insertQuery.append("'").append(rsDriverIsVIP.getString("isVIP")).append("',");
			  insertQuery.append("'").append(dtf.format(now)).append("',");
			  //'1960-01-01 23:03:20'
			  insertQuery.append("null").append(",");
			  insertQuery.append("null").append(")");
			  
			  stmt.executeUpdate(new String(insertQuery));
		  } else {
			  System.out.println("Problem getting if driver is VIP");
			  return new Response("KO");
		  }
		  
	  } catch (SQLException e) {
		  System.out.println(e);
		  return new Response("KO");
	  }
	  return new Response("Ok");
  }
  
}