package com.realdolmen.domain.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.realdolmen.domain.flight.DiscountPercentage;
import com.realdolmen.domain.flight.DiscountRealvalue;
import com.realdolmen.domain.flight.DiscountSuper;
import com.realdolmen.domain.flight.Flight;
import com.realdolmen.domain.flight.VolumeDiscountPercentage;
import com.realdolmen.domain.flight.VolumeDiscountRealvalue;
import com.realdolmen.repository.FlightRepository;

@Named("discountBean")
@SessionScoped
public class DiscountBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4305676802381146759L;

	@Inject
	private FlightRepository flightRepository;
	
	@Inject
	private PartnerBean partnerBean;
	
	@Inject
	private EmployeeBean employeeBean;
	
	@Inject
	private LoginBean loginBean;
	
	private long discountToRemove;

	private double newDiscount;

	private String newDiscountType = "Percentage";
	
	private int newDiscountVolume;

	private Boolean withDates = false;

	private Date beginDate;

	private Date endDate;

	private Flight flight;

	/**
	 * Remove a discount
	 */
	public String removeDiscount(long id){
		System.err.println("EmployeeBean REMOVED THE DISCOUNT 1/2");
		if(loginBean.getUserIsEmployee()){
			flight = flightRepository.removeDiscount(loginBean.getUser(), id, employeeBean.getFlight());
			employeeBean.setFlight(flight);
		}
		else{
			flight = flightRepository.removeDiscount(loginBean.getUser(), id, partnerBean.getPartnerFlight());
			partnerBean.setPartnerFlight(flight);
		}
		
		System.err.println("EmployeeBean REMOVED THE DISCOUNT 2/2");
		return "";
	}

	public String removeDiscount(){
		System.err.println("EmplyeeBean remove discount dummy");
		if(loginBean.getUserIsEmployee()){
			flight = flightRepository.removeDiscount(loginBean.getUser(), discountToRemove, employeeBean.getFlight());
			employeeBean.setFlight(flight);
		}
		else{
			flight = flightRepository.removeDiscount(loginBean.getUser(), discountToRemove, partnerBean.getPartnerFlight());
			partnerBean.setPartnerFlight(flight);
		}
		return "";
	}

	public long getDiscountToRemove() {
		return discountToRemove;
	}

	public void setDiscountToRemove(long discountToRemove) {
		this.discountToRemove = discountToRemove;
	}

	/**
	 * Add new discount
	 */	
	public double getNewDiscount() {
		return newDiscount;
	}

	public void setNewDiscount(double newDiscount) {
		this.newDiscount = newDiscount;
	}

	public String getNewDiscountType() {
		return newDiscountType;
	}

	public void setNewDiscountType(String newDiscountType) {
		this.newDiscountType = newDiscountType;
	}

	public String addNewDiscount(){
		System.err.println("Tried adding new discount");
		Boolean isPercentage = false;
		if(newDiscountType.equals("Percentage")){
			isPercentage = true;
		}
		DiscountSuper d;
		Boolean isPeriodical = (beginDate != null && endDate != null);
		if(isPercentage){
			if(newDiscountVolume < 1){
				d = new DiscountPercentage((loginBean.getUserIsEmployee()), newDiscount,
				isPeriodical, beginDate, endDate);
			}
			else{
				d = new VolumeDiscountPercentage((loginBean.getUserIsEmployee()), newDiscount,
						isPeriodical, beginDate, endDate, newDiscountVolume);
			}
		}
		else{
			if(newDiscountVolume < 1){
				d = new DiscountRealvalue((loginBean.getUserIsEmployee()), newDiscount,
						isPeriodical, beginDate, endDate);
			}
			else{
				d = new VolumeDiscountRealvalue((loginBean.getUserIsEmployee()), newDiscount,
						isPeriodical, beginDate, endDate, newDiscountVolume);
			}
		}
		
		if(loginBean.getUserIsEmployee()){
			flightRepository.addDiscount(d, employeeBean.getFlight());
		}
		else{
			flightRepository.addDiscount(d, partnerBean.getPartnerFlight());
		}

		return "";
	}

	public ArrayList<String> getDiscountTypes(){
		ArrayList<String> types = new ArrayList<>();
		types.add("Percentage");
		types.add("Real value");
		return types;
	}

	public void changeDiscountType(){
		System.err.println("Changed discount type to " + newDiscountType);
	}

	public String getDiscountTypeSymbol(){
		if(newDiscountType.equals("Percentage")){
			return "%";
		}
		else{
			return "€";
		}
	}

	/**
	 * For dates
	 */
	public void toggleWithDates(){
		System.err.println("Changed withDates to " + withDates);
	}

	public Boolean getWithDates() {
		return withDates;
	}

	public void setWithDates(Boolean withDates) {
		this.withDates = withDates;
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

	/**
	 * For volume discounts
	 */
	public int getNewDiscountVolume() {
		return newDiscountVolume;
	}
	
	public void setNewDiscountVolume(int newDiscountVolume) {
		this.newDiscountVolume = newDiscountVolume;
	}
	
}
