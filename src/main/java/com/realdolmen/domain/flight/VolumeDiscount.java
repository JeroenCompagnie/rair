package com.realdolmen.domain.flight;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;

@Entity
public class VolumeDiscount extends DiscountSuper implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6429825830827322762L;
	
	protected int nrOfSeatsFromWhenApplicable;
	
	public VolumeDiscount(){
		
	}
	
	public VolumeDiscount(boolean byEmployee, double discount,
			boolean isPeriodical, Date beginDate, Date endDate, int nrOfSeatsFromWhenApplicable){
		super(byEmployee, discount, isPeriodical, beginDate, endDate);
		this.nrOfSeatsFromWhenApplicable = nrOfSeatsFromWhenApplicable;
	}

	public VolumeDiscount(boolean byEmployee, boolean isPercentage, double discount, 
			int nrOfSeatsFromWhenApplicable){
		this.nrOfSeatsFromWhenApplicable = nrOfSeatsFromWhenApplicable;
	}
	
	public double addDiscountToPrice(double price, int nrOfSeats){
		if(nrOfSeats >= nrOfSeatsFromWhenApplicable){
			return super.addDiscountToPrice(price);
		}
		else{
			return price;
		}
	}

	@Override
	public String toString(){
		return super.toString() + " when ordering " + nrOfSeatsFromWhenApplicable + " or more seats";
	}
	
	public int getNrOfSeatsFromWhenApplicable() {
		return nrOfSeatsFromWhenApplicable;
	}

	public void setNrOfSeatsFromWhenApplicable(int nrOfSeatsFromWhenApplicable) {
		this.nrOfSeatsFromWhenApplicable = nrOfSeatsFromWhenApplicable;
	}

	@Override
	protected double addDiscountToPrice2(double price) {
		// TODO Auto-generated method stub
		return 0;
	}
}
