package com.touk.parking.model;

public class ParkingMeter {

    private int parkingID;
    private int parkingMeterID;
	public ParkingMeter(int parkingID, int parkingMeterID) {
		this.parkingID = parkingID;
		this.parkingMeterID = parkingMeterID;
	}
	public int getParkingID() {
		return parkingID;
	}
	public void setParkingID(int parkingID) {
		this.parkingID = parkingID;
	}
	public int getParkingMeterID() {
		return parkingMeterID;
	}
	public void setParkingMeterID(int parkingMeterID) {
		this.parkingMeterID = parkingMeterID;
	}
	@Override
	public String toString() {
		return "ParkingMeter [parkingID=" + parkingID + ", parkingMeterID=" + parkingMeterID + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + parkingMeterID;
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
		ParkingMeter other = (ParkingMeter) obj;
		if (parkingMeterID != other.parkingMeterID)
			return false;
		return true;
	}
	
    
}
