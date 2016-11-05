package com.realdolmen.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Flight implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6851290785176539664L;

	/**
	 * TODO: params
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="partner_id", nullable=false)
	private Partner partner;
	
	//TODO cascadetypes nakijken
	@OneToMany(cascade=CascadeType.ALL,mappedBy="flight")
	private List<BookingOfFlight> bookingOfFlightList = new ArrayList<>();
	
	public Flight(){
		
	}
	
	public Flight(Partner partner, List<BookingOfFlight> bookingOfFlightList) {
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
