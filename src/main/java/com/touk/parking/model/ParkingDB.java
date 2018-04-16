package com.touk.parking.model;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.springframework.web.bind.annotation.RequestParam;

import com.touk.parking.Greeting;
import com.touk.parking.Response;
 
public class ParkingDB {
  Connection conn;
  Properties prop = new Properties();
  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
 
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
	    stmt.executeUpdate("Create table receipt (receiptID int primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),carID int,parkingMeterID int, isDriverVIPWhenStarted boolean,startingTime TimeStamp,endingTime TimeStamp, price float,currencyName varchar(30))");
	    stmt.executeUpdate("Create table parkingmeter (parkingMeterID int primary key, parkingID int)");
	    stmt.executeUpdate("Create table parking (parkingID int primary key, name varchar(30),currencyID int)");
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
			  System.out.println("startParkingMeter with carID="+carID+" parkingMeterID="+parkingMeterID+" ended up successfully");
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
  
  public Response stopParkingMeter(int carID){
	  try {		  
		  Statement stmt = conn.createStatement();
		  StringBuffer selectRightReceipt=new StringBuffer("");
		  selectRightReceipt.append("SELECT receipt.receiptID,receipt.isDriverVIPWhenStarted,receipt.startingTime,currency.currencyName,currency.initialPrice");
		  selectRightReceipt.append("		FROM receipt,parkingMeter,parking,currency");
		  selectRightReceipt.append("		WHERE receipt.parkingMeterID=parkingMeter.parkingMeterID");
		  selectRightReceipt.append("		AND parkingMeter.parkingID=parking.parkingID");
		  selectRightReceipt.append("		AND parking.currencyID=currency.currencyID");
		  selectRightReceipt.append("		AND receipt.carID=").append(carID);
		  selectRightReceipt.append("		AND receipt.endingTime is null");
		  
		  ResultSet rsRightReceipt = stmt.executeQuery(new String(selectRightReceipt));
		  
		  if (rsRightReceipt.next()) {
			  LocalDateTime now = LocalDateTime.now();
			  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
			  LocalDateTime startingTime = LocalDateTime.parse(rsRightReceipt.getString("startingTime"), formatter);
			 
			  double parkingCost=calculateValue (startingTime,now,rsRightReceipt.getBoolean("isDriverVIPWhenStarted"),rsRightReceipt.getFloat("initialPrice"));
			  
			  StringBuffer updateReceiptQuery=new StringBuffer("");
			  updateReceiptQuery.append("UPDATE Receipt ");
			  updateReceiptQuery.append("	SET price=").append(parkingCost).append(",");
			  updateReceiptQuery.append("	endingTime='").append(dtf.format(now)).append("',");
			  updateReceiptQuery.append("	currencyName='").append(rsRightReceipt.getString("currencyName")).append("'");
			  updateReceiptQuery.append("	WHERE receipt.receiptID=").append(rsRightReceipt.getString("receiptID"));
			  
			  stmt.executeUpdate(new String(updateReceiptQuery));
			  System.out.println("stopParkingMeter with carID="+carID+" ended up successfully");
		  } else {
			  System.out.println("Problem getting the receipt");
			  return new Response("KO");
		  }
		  
	  } catch (SQLException e) {
		  System.out.println(e);
		  return new Response("KO");
	  }
	  return new Response("Ok");
  }
  
  private double calculateValue (LocalDateTime startingTime,LocalDateTime endingTime,boolean isDriverVIPWhenStarted, float initialPriceCurrency){
	  System.out.println("startingTime="+startingTime);
	  System.out.println("endingTime="+endingTime);
	  System.out.println("isDriverVIPWhenStarted="+isDriverVIPWhenStarted);
	  System.out.println("initialPriceCurrency="+initialPriceCurrency);
	  double totalValue=0;
	  
	  int numberOfHours = (int) Duration.between(startingTime, endingTime).toHours();
	  if (isDriverVIPWhenStarted) {
		  if (numberOfHours==0 || numberOfHours==1) {
			  return 0;
		  } else {
			  for (int i=numberOfHours-1;i>0;i--) {
				  totalValue+=Math.pow(1.5, numberOfHours)+0.5*Math.pow(1.5, numberOfHours-1);
			  }
		  }
	  } else {
		  return Math.pow(2, numberOfHours)-1;
	  }
	  
	  return totalValue;
  }
}