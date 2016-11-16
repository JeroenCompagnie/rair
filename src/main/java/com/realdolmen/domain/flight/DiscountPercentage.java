package com.realdolmen.domain.flight;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
public class DiscountPercentage extends DiscountSuper implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4954739235029957590L;
	
	public DiscountPercentage(){
		
	}

	public DiscountPercentage(boolean byEmployee, double discount, boolean isPeriodical, Date beginDate, Date endDate) {
		super(byEmployee, discount, isPeriodical, beginDate, endDate);
	}
	
	public DiscountPercentage(boolean byEmployee, double discount) {
		super(byEmployee, discount);
	}
	
	public double addDiscountToPrice(double price){
		return super.addDiscountToPrice(price);
	}
	
	protected double addDiscountToPrice2(double price){
		return price - price * discount;
	}
	
	@Override
	public String toString(){
		String result = "";
		result += super.toString();
		result = result + Math.abs(discount) + "%";
		result += super.getStringIfPeriodical();
		return result;
	}

	public String getString() {
		return toString();
	}	
}

