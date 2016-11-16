package com.realdolmen.domain.flight;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.realdolmen.dateConverter.DateConverter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class DiscountSuper implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4954739235029957590L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected long id;
	
	// This can be a percentage or a real number, depending on the boolean isPercentage
	protected double discount;
	
	// Determines if it is only valid during a period
	protected boolean isPeriodical;
	
	protected boolean byEmployee;
	
	protected Date beginDate = null;
	
	protected Date endDate = null;
	
	public DiscountSuper(){
		
	}

	public DiscountSuper(boolean byEmployee, double discount, boolean isPeriodical, Date beginDate, Date endDate) {
		this.byEmployee = byEmployee;
		this.discount = discount;
		this.isPeriodical = isPeriodical;
		if(this.isPeriodical){
			this.beginDate = beginDate;
			this.endDate = endDate;
		}
	}
	
	public DiscountSuper(boolean byEmployee, double discount) {
		this.byEmployee = byEmployee;
		this.discount = discount;
		this.isPeriodical = false;
	}

	protected double addDiscountToPrice(double price){
		if(isPeriodical){
			Date d = new Date();
			// Test if its in the right period
			if(d.before(endDate) && d.after(beginDate)){
				return addDiscountToPrice2(price);
			}
			// Not in the right period
			else{
				return price;
			}
		}
		//If it's not periodical
		else{
			return addDiscountToPrice2(price);
		}
	}
	
	protected abstract double addDiscountToPrice2(double price);
	
	@Override
	public String toString(){
		String result = "";
		
		if(discount == 0){
			return "No discount or charge added";
		}
		else if(discount > 0){
			result += "Discount of ";
		}
		else{
			result += "Charge of ";
		}
		return result;
	}
	
	protected String getStringIfPeriodical(){
		String result = "";
		if(isPeriodical){
			result += " from " + DateConverter.format(beginDate);
			result += " to " + DateConverter.format(endDate);
		}
		return result;
	}
	
	
	/******************************************************************
	 * 
	 * GETTERS AND SETTERS
	 * 
	 ******************************************************************/

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public boolean isPeriodical() {
		return isPeriodical;
	}

	public void setPeriodical(boolean isPeriodical) {
		this.isPeriodical = isPeriodical;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public long getId() {
		return id;
	}

	public boolean isByEmployee() {
		return byEmployee;
	}

	public void setByEmployee(boolean byEmployee) {
		this.byEmployee = byEmployee;
	}
	
	
}

