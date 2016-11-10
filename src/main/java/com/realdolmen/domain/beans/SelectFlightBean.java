package com.realdolmen.domain.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

@Named("flights")
@RequestScoped
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
	
	
	private Date dateOfDeparture;
	
	public Date getDateOfDeparture() {
		return dateOfDeparture;
	}

	public void setDateOfDeparture(Date dateOfDeparture) {
		this.dateOfDeparture = dateOfDeparture;
	}

	public Date getDateOfReturn() {
		return dateOfReturn;
	}

	public void setDateOfReturn(Date dateOfReturn) {
		this.dateOfReturn = dateOfReturn;
	}

	private Date dateOfReturn;

	public void setSelectedAirline(String selectedAirline) {
		this.selectedAirline = selectedAirline;
	}

	public String getSelectedFlightclass() {
		return selectedFlightClass;
	}

	public void setSelectedFlightclass(String selectedFlightclass) {
		this.selectedFlightClass = selectedFlightclass;
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
