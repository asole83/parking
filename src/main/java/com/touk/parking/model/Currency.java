package com.touk.parking.model;

public class Currency {

    private int currencyID;
    private String currencyName;
    private float initialPrice;
    
	public Currency(int currencyID, String currencyName, float initialPrice) {
		super();
		this.currencyID = currencyID;
		this.currencyName = currencyName;
		this.initialPrice = initialPrice;
	}
	public int getCurrencyID() {
		return currencyID;
	}
	public void setCurrencyID(int currencyID) {
		this.currencyID = currencyID;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public float getInitialPrice() {
		return initialPrice;
	}
	public void setInitialPrice(float initialPrice) {
		this.initialPrice = initialPrice;
	}
	@Override
	public String toString() {
		return "Currency [currencyID=" + currencyID + ", currencyName=" + currencyName + ", initialPrice="
				+ initialPrice + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + currencyID;
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
		Currency other = (Currency) obj;
		if (currencyID != other.currencyID)
			return false;
		return true;
	}

    
}
