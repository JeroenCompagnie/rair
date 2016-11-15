package com.realdolmen.domain.flight;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
public class DiscountRealvalue extends DiscountSuper implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8982985166234703425L;

	public DiscountRealvalue(){
		
	}

	public DiscountRealvalue(boolean byEmployee, double discount, boolean isPeriodical, Date beginDate, Date endDate) {
		super(byEmployee, discount, isPeriodical, beginDate, endDate);
	}
	
	public DiscountRealvalue(boolean byEmployee, double discount) {
		super(byEmployee, discount);
	}
	
	public double addDiscountToPrice(double price){
		return super.addDiscountToPrice(price);
	}
	
	protected double addDiscountToPrice2(double price){
		return price - discount;
	}
	
	@Override
	public String toString(){
		String result = "";
		result += super.toString();
		result = result + "€" + Math.abs(discount);
		result += super.getStringIfPeriodical();
		return result;
	}

	public String getString() {
		return toString();
	}	
	
}

