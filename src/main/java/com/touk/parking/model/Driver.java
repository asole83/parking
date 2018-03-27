package com.touk.parking.model;

public class Driver {

    private int driverID;
    private boolean isVIP;

    public Driver(boolean isVIP, int driverID) {
		this.isVIP = isVIP;
		this.driverID = driverID;
	}

	public int getDriverID() {
		return driverID;
	}

	public void setDriverID(int driverID) {
		this.driverID = driverID;
	}

	public boolean isVIP() {
		return isVIP;
	}

	public void setVIP(boolean isVIP) {
		this.isVIP = isVIP;
	}

	@Override
	public String toString() {
		return "Driver [driverID=" + driverID + ", isVIP=" + isVIP + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + driverID;
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
		Driver other = (Driver) obj;
		if (driverID != other.driverID)
			return false;
		return true;
	}
	
}
