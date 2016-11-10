package com.realdolmen.domain.flight;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Airport implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1675720280096531480L;

	private String city; 
	
	private String country; 

	private String countryCode;
	
	private String airportName;
	
	@Id
	private String id;
	
	private String globalRegion;
	
	public Airport(){
		
	}
	
	public Airport(String city, String country, String countryCode, 
			String airportName, String internationalAirportCode) { 
		this.city=city;
		this.country=country;
		this.countryCode=countryCode;
		this.airportName = airportName;
		this.id = internationalAirportCode;
	}

	public Airport(String city, String country, String countryCode, 
			String airportName, String internationalAirportCode, String globalRegion) {
		this.city=city;
		this.country=country;
		this.countryCode=countryCode;
		this.airportName = airportName;
		this.id = internationalAirportCode;
		this.globalRegion=globalRegion;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getAirportName() {
		return airportName;
	}

	public void setAirportName(String airportName) {
		this.airportName = airportName;
	}
	
	public String getId() {
		return id;
	}

	public String getGlobalRegion() {
		return globalRegion;
	}

	public void setGlobalRegion(String globalRegion) {
		this.globalRegion = globalRegion;
	}
	
	@Override
	public String toString(){
		return id + ": " + 
				country + ", " + city +" - " + airportName + " ("
				+ globalRegion + ")";
	}
	
}
