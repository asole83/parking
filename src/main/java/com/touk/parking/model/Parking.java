package com.touk.parking.model;

public class Parking {

    private int parkingID;
    private String Name;
	public Parking(int parkingID, String name) {
		this.parkingID = parkingID;
		Name = name;
	}
	public int getParkingID() {
		return parkingID;
	}
	public void setParkingID(int parkingID) {
		this.parkingID = parkingID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	@Override
	public String toString() {
		return "Parking [parkingID=" + parkingID + ", Name=" + Name + "]";
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
