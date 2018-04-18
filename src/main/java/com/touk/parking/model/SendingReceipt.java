package com.touk.parking.model;

import java.time.LocalDateTime;

public class SendingReceipt {

    private String carPlateNumber;
    private LocalDateTime startingTime;
    private LocalDateTime endingTime;
    private double price;
    private String currencyName;
    private String parkingName;
    
	public SendingReceipt(String carPlateNumber, LocalDateTime startingTime, LocalDateTime endingTime, double price,
			String currencyName, String parkingName) {
		this.carPlateNumber = carPlateNumber;
		this.startingTime = startingTime;
		this.endingTime = endingTime;
		this.price = price;
		this.currencyName = currencyName;
		this.parkingName = parkingName;
	}
	
	public String getCarPlateNumber() {
		return carPlateNumber;
	}
	public void setCarPlateNumber(String carPlateNumber) {
		this.carPlateNumber = carPlateNumber;
	}
	public LocalDateTime getStartingTime() {
		return startingTime;
	}
	public void setStartingTime(LocalDateTime startingTime) {
		this.startingTime = startingTime;
	}
	public LocalDateTime getEndingTime() {
		return endingTime;
	}
	public void setEndingTime(LocalDateTime endingTime) {
		this.endingTime = endingTime;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public String getParkingName() {
		return parkingName;
	}
	public void setParkingName(String parkingName) {
		this.parkingName = parkingName;
	}
    
    
	
}
