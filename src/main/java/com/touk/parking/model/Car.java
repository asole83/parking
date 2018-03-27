package com.touk.parking.model;

public class Car {

    private int carID;
    private int driverID;
    private String plateNumber;
	public Car(int carID, int driverID, String plateNumber) {
		this.carID = carID;
		this.driverID = driverID;
		this.plateNumber = plateNumber;
	}
	public int getCarID() {
		return carID;
	}
	public void setCarID(int carID) {
		this.carID = carID;
	}
	public int getDriverID() {
		return driverID;
	}
	public void setDriverID(int driverID) {
		this.driverID = driverID;
	}
	public String getPlateNumber() {
		return plateNumber;
	}
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	@Override
	public String toString() {
		return "Car [carID=" + carID + ", driverID=" + driverID + ", plateNumber=" + plateNumber + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + carID;
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
		Car other = (Car) obj;
		if (carID != other.carID)
			return false;
		return true;
	}

   
}
