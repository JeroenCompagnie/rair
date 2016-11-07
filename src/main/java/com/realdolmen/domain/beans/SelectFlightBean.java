package com.realdolmen.domain.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedProperty;

@ManagedBean
@SessionScoped
public class SelectFlightBean implements Serializable {
	@ManagedProperty("#{flightclasses}")
	private List<String> flightclasses;
	@ManagedProperty("#{airlines}")
	private List<String> airlines;
	
	

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
