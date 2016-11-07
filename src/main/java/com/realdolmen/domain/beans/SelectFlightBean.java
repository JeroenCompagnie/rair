package com.realdolmen.domain.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("flights")
@SessionScoped
public class SelectFlightBean implements Serializable {
	// @ManagedProperty("#{flightclasses}")
	private List<String> flightclasses;
	// @ManagedProperty("#{airlines}")
	private List<String> airlines;

	private String selectedAirline;

	private String selectedFlightClass;

	public String getSelectedAirline() {
		return selectedAirline;
	}
	
	private Calendar dateOfDeparture;
	
	public Calendar getDateOfDeparture() {
		return dateOfDeparture;
	}

	public void setDateOfDeparture(Calendar dateOfDeparture) {
		this.dateOfDeparture = dateOfDeparture;
	}

	public Calendar getDateOfReturn() {
		return dateOfReturn;
	}

	public void setDateOfReturn(Calendar dateOfReturn) {
		this.dateOfReturn = dateOfReturn;
	}

	private Calendar dateOfReturn;

	public void setSelectedAirline(String selectedAirline) {
		this.selectedAirline = selectedAirline;
	}

	public String getSelectedFlightclass() {
		return selectedFlightClass;
	}

	public void setSelectedFlightclass(String selectedFlightclass) {
		this.selectedFlightClass = selectedFlightClass;
	}

	private String message = "testing bean";

	public String getMessage() {
		return message;
	}

	@PostConstruct
	public void init() {
		flightclasses = new ArrayList<String>();
		airlines = new ArrayList<String>();
		flightclasses.add("Economy");
		flightclasses.add("Business");
		airlines.add("AirTerror");
		airlines.add("CrashAirline");
	}

	public List<String> getFlightclasses() {
		return flightclasses;
	}

	public void setFlightclasses(List<String> flightclasses) {
		this.flightclasses = flightclasses;
	}

	public List<String> getAirlines() {
		return airlines;
	}

	public void setAirlines(List<String> airlines) {
		this.airlines = airlines;
	}

}
