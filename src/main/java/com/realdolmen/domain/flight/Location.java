package com.realdolmen.domain.flight;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Location implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1318827927408319376L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String airportName;
	
	private String airportCountry;
	
	private String airportInternationalCode;
	
	@Enumerated(EnumType.STRING)
	private GlobalRegion globalRegion;
	
	public Location(){
		
	}

	public Location(String airportName, String airportCountry, String airportInternationalCode,
			GlobalRegion globalRegion) {
		this.airportName = airportName;
		this.airportCountry = airportCountry;
		this.airportInternationalCode = airportInternationalCode;
		this.globalRegion = globalRegion;
	}

	public String getAirportName() {
		return airportName;
	}

	public void setAirportName(String airportName) {
		this.airportName = airportName;
	}

	public String getAirportCountry() {
		return airportCountry;
	}

	public void setAirportCountry(String airportCountry) {
		this.airportCountry = airportCountry;
	}

	public String getAirportInternationalCode() {
		return airportInternationalCode;
	}

	public void setAirportInternationalCode(String airportInternationalCode) {
		this.airportInternationalCode = airportInternationalCode;
	}

	public GlobalRegion getGlobalRegion() {
		return globalRegion;
	}

	public void setGlobalRegion(GlobalRegion globalRegion) {
		this.globalRegion = globalRegion;
	}

	public Integer getId() {
		return id;
	}
	
	
}
