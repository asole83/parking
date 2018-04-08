package com.touk.parking.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.tomcat.jni.File;

import com.touk.parking.ParkingControllerTests;
 
public class ParkingDB {
  Connection conn;
 
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
	    stmt.executeUpdate("Create table currency (currencyID int, currencyName varchar(30),initialPrice float)");
	    
	 
	    // insert 2 rows
	    /*stmt.executeUpdate("insert into users values (1,'tom')");
	    stmt.executeUpdate("insert into users values (2,'peter')");
	 
	    // query
	    ResultSet rs = stmt.executeQuery("SELECT * FROM users");
	 
	    // print out query result
	    while (rs.next()) { 
	      System.out.printf("%d\t%s\n", rs.getInt("id"), rs.getString("name"));
	    }*/
	  } catch (SQLException e){
			System.out.println(e);
	  }
  }
  public void prepareTestValues(){
	  try {
		Statement stmt = conn.createStatement();
		final ResourceBundle rb = ResourceBundle.getBundle("TestValues");

		System.out.println(rb.getString("hola"));
		
	    stmt.executeUpdate("insert into driver values ("+rb.getString("driver1ID")+",'"+rb.getString("driver1Name")+"','"+rb.getString("driver1IsVIP")+"')");
	    /*stmt.executeUpdate("insert into users values (1,'tom')");
	    stmt.executeUpdate("insert into users values (1,'tom')");
	    stmt.executeUpdate("insert into users values (1,'tom')");
	    */
	  } catch (SQLException e){
			System.out.println(e);
	  }
  }
  
}