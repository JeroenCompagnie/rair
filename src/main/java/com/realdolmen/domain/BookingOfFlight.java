package com.realdolmen.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class BookingOfFlight implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private float price;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="flight_id", nullable=false)
	private Flight flight;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="booking_id", nullable=false)
	private Booking booking;
	
	public BookingOfFlight(){
		
	}

	public BookingOfFlight(float price, Flight flight) {
		super();
		this.price = price;
		this.flight = flight;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public Long getId() {
		return id;
	}
	
		
}
