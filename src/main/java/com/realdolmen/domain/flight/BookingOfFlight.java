package com.realdolmen.domain.flight;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class BookingOfFlight implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9176140862127754720L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private double price;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="flight_id")
	private Flight flight;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="booking_id")
	private Booking booking;
	
	@NotNull
	private int numberInBooking;
	
	@NotNull
	@OneToOne
	private Seat seat;
	
	public BookingOfFlight(){
		
	}

	public BookingOfFlight(double price, Flight flight, Booking booking, Seat seat) {
		this.price = price;
		this.flight = flight;
		this.booking = booking;
		this.seat = seat;
	}

	public double getPrice() {
		return price;
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

	public int getNumberInBooking() {
		return numberInBooking;
	}

	public void setNumberInBooking(int numberInBooking) {
		this.numberInBooking = numberInBooking;
	}

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}
}
