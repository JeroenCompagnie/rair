package com.realdolmen.domain.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.junit.Before;

import com.realdolmen.domain.flight.BookingOfFlight;
import com.realdolmen.domain.flight.Flight;
import com.realdolmen.domain.flight.GlobalRegion;
import com.realdolmen.domain.flight.Location;
import com.realdolmen.domain.user.Partner;
import com.realdolmen.repository.PartnerRepository;

@Named("partnerBean")
@SessionScoped
public class PartnerBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4517882120486152250L;

	@Inject
	private LoginBean loginBean;
	
	@EJB
	private PartnerRepository partnerRepository;
	
	@Before
	public void init(){
		System.err.println(this.hashCode());
	}
	
	@NotNull(message = "Number of seats needs to be filled in")
	@Min(value=0, message = "Number of seats needs to be between 0 and 1200")
	@Max(value=1200, message = "Number of seats needs to be between 0 and 1200")
	private int nrBusinessSeats;
	
	@NotNull(message = "Number of seats needs to be filled in")
	@Min(value=0, message = "Number of seats needs to be between 0 and 1200")
	@Max(value=1200, message = "Number of seats needs to be between 0 and 1200")
	private int nrEconomySeats;
	
	@NotNull(message = "Number of seats needs to be filled in")
	@Min(value=0, message = "Number of seats needs to be between 0 and 1200")
	@Max(value=1200, message = "Number of seats needs to be between 0 and 1200")
	private int nrFirstSeats;
	
	@NotNull(message = "Price of a seat needs to be filled in")
	@Min(value=0, message = "Price of a seat needs to be higher as 0")
	private double priceBusinessSeats;
	
	@NotNull(message = "Price of a seat needs to be filled in")
	@Min(value=0, message = "Price of a seat needs to be higher as 0")
	private double priceEconomySeats;
	
	@NotNull(message = "Price of a seat needs to be filled in")
	@Min(value=0, message = "Price of a seat needs to be higher as 0")
	private double priceFirstSeats;
	
	@NotNull(message = "Needs a departure date")
	private Date dateOfDeparture;
	
	@NotNull(message = "Needs a flight duration")
	@Min(value=0, message = "Flight duration needs to be higher as 0")
	private int flightDuration;
	
	@NotEmpty(message = "Needs a departure location")
	private String departureLocation;
	
	@NotEmpty(message = "Needs a destination location")
	private String destinationLocation;
	
	public int getNrBusinessSeats() {
		return nrBusinessSeats;
	}

	public void setNrBusinessSeats(int nrBusinessSeats) {
		this.nrBusinessSeats = nrBusinessSeats;
	}

	public int getNrEconomySeats() {
		return nrEconomySeats;
	}

	public void setNrEconomySeats(int nrEconomySeats) {
		this.nrEconomySeats = nrEconomySeats;
	}

	public int getNrFirstSeats() {
		return nrFirstSeats;
	}

	public void setNrFirstSeats(int nrFirstSeats) {
		this.nrFirstSeats = nrFirstSeats;
	}

	public double getPriceBusinessSeats() {
		return priceBusinessSeats;
	}

	public void setPriceBusinessSeats(double priceBusinessSeats) {
		this.priceBusinessSeats = priceBusinessSeats;
	}

	public double getPriceEconomySeats() {
		return priceEconomySeats;
	}

	public void setPriceEconomySeats(double priceEconomySeats) {
		this.priceEconomySeats = priceEconomySeats;
	}

	public double getPriceFirstSeats() {
		return priceFirstSeats;
	}

	public void setPriceFirstSeats(double priceFirstSeats) {
		this.priceFirstSeats = priceFirstSeats;
	}
	
	public String getDepartureLocation() {
		return departureLocation;
	}

	public void setDepartureLocation(String departureLocation) {
		this.departureLocation = departureLocation;
	}

	public String getDestinationLocation() {
		return destinationLocation;
	}

	public void setDestinationLocation(String destinationLocation) {
		this.destinationLocation = destinationLocation;
	}

	public Date getDepartureDate() {
		return dateOfDeparture;
	}

	public void setDepartureDate(Date departureDate) {
		this.dateOfDeparture = departureDate;
	}

	public int getFlightDuration() {
		return flightDuration;
	}

	public void setFlightDuration(int flightDuration) {
		this.flightDuration = flightDuration;
	}
	
	public String addFlight(){
		System.err.println("Tried to add flight");
		Flight f = new Flight((Partner) loginBean.getUser(), new ArrayList<BookingOfFlight>(), 
				new Location("1","1","1",GlobalRegion.Africa), 
				new Location("2","2","2",GlobalRegion.Asia), 
				dateOfDeparture, Duration.ofMinutes(flightDuration));
		
		partnerRepository.addFlight((Partner) loginBean.getUser(), f, 
				nrBusinessSeats, priceBusinessSeats,
				nrEconomySeats, priceEconomySeats,
				nrFirstSeats, priceFirstSeats);
		
		String message = "Fligt added!";
	    FacesContext.getCurrentInstance().addMessage(null, 
	        new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
	    
	    clearAllInput();
		return "";
	}

	private void clearAllInput() {
		setNrBusinessSeats(0);
		setNrEconomySeats(0);
		setNrFirstSeats(0);
		setPriceBusinessSeats(0.0);
		setPriceEconomySeats(0.0);
		setPriceFirstSeats(0.0);
		setDepartureLocation("");
		setDestinationLocation("");
		setFlightDuration(0);
		setDepartureDate(null);
	}
	
}
