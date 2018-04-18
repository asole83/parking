package com.touk.parking.model;

import java.util.Date;

public class Receipt {

    private int carID;
    private int parkingMeterID;
    private boolean isDriverVIPWhenStarted;
    private Date startingTime;
    private Date endingTime;
    private float price;
    private int currencyID;
    
	public Receipt(int carID, int parkingMeterID, boolean isDriverVIPWhenStarted, Date startingTime, Date endingTime,
			float price, int currencyID) {
		this.carID = carID;
		this.parkingMeterID = parkingMeterID;
		this.isDriverVIPWhenStarted = isDriverVIPWhenStarted;
		this.startingTime = startingTime;
		this.endingTime = endingTime;
		this.price = price;
		this.currencyID = currencyID;
	}
	public int getCarID() {
		return carID;
	}
	public void setCarID(int carID) {
		this.carID = carID;
	}
	public int getParkingMeterID() {
		return parkingMeterID;
	}
	public void setParkingMeterID(int parkingMeterID) {
		this.parkingMeterID = parkingMeterID;
	}
	public boolean isDriverVIPWhenStarted() {
		return isDriverVIPWhenStarted;
	}
	public void setDriverVIPWhenStarted(boolean isDriverVIPWhenStarted) {
		this.isDriverVIPWhenStarted = isDriverVIPWhenStarted;
	}
	public Date getStartingTime() {
		return startingTime;
	}
	public void setStartingTime(Date startingTime) {
		this.startingTime = startingTime;
	}
	public Date getEndingTime() {
		return endingTime;
	}
	public void setEndingTime(Date endingTime) {
		this.endingTime = endingTime;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getCurrencyID() {
		return currencyID;
	}
	public void setCurrencyID(int currencyID) {
		this.currencyID = currencyID;
	}
	@Override
	public String toString() {
		return "Receipt [carID=" + carID + ", parkingMeterID=" + parkingMeterID + ", isDriverVIPWhenStarted="
				+ isDriverVIPWhenStarted + ", startingTime=" + startingTime + ", endingTime=" + endingTime + ", price="
				+ price + ", currencyID=" + currencyID + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + carID;
		result = prime * result + parkingMeterID;
		result = prime * result + ((startingTime == null) ? 0 : startingTime.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Receipt other = (Receipt) obj;
		if (carID != other.carID)
			return false;
		if (parkingMeterID != other.parkingMeterID)
			return false;
		if (startingTime == null) {
			if (other.startingTime != null)
				return false;
		} else if (!startingTime.equals(other.startingTime))
			return false;
		return true;
	}

}
