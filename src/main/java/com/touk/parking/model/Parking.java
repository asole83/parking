package com.touk.parking.model;

public class Parking {

    private int parkingID;
    private String name;
    private int currencyID;
	public Parking(int parkingID, String name,int currencyID) {
		this.parkingID = parkingID;
		this.name = name;
	}
	public int getParkingID() {
		return parkingID;
	}
	public void setParkingID(int parkingID) {
		this.parkingID = parkingID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCurrencyID() {
		return currencyID;
	}
	public void setCurrencyID(int currencyID) {
		this.currencyID = currencyID;
	}
	
	@Override
	public String toString() {
		return "Parking [parkingID=" + parkingID + ", name=" + name + ", currencyID=" + currencyID + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + parkingID;
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
		Parking other = (Parking) obj;
		if (parkingID != other.parkingID)
			return false;
		return true;
	}

}
