package com.realdolmen.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Flight implements Serializable{
	/**
	 * TODO: params
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private Partner partner;
	
	//TODO cascadetypes nakijken
	@OneToMany(cascade=CascadeType.ALL,mappedBy="flight")
	private List<BookingOfFlight> bookingOfFlightList = new ArrayList<>();
	
	public Flight(){
		
	}
	
	public Flight(Partner partner, List<BookingOfFlight> bookingOfFlightList) {
		super();
		this.partner = partner;
		this.bookingOfFlightList = bookingOfFlightList;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public List<BookingOfFlight> getBookingOfFlightList() {
		return bookingOfFlightList;
	}

	public void setBookingOfFlightList(List<BookingOfFlight> bookingOfFlightList) {
		this.bookingOfFlightList = bookingOfFlightList;
	}

	public Long getId() {
		return id;
	}
	
	
}
