package com.realdolmen.domain.flight;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.realdolmen.domain.user.Partner;

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
	
	@OneToOne(cascade=CascadeType.ALL)
	private Location departureLocation;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Location destinationLocation;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Seat> seatList = new ArrayList<>();
	
	private Calendar dateOfDeparture;
	
	private Duration flightDuration;
	
	@ElementCollection
	private List<Double> discountsListEmployee = new ArrayList<Double>();
	
	@ElementCollection
	private List<Double> discountsListPartner = new ArrayList<Double>();
	
	public Flight(){
		
	}
	
	public Flight(Partner partner, List<BookingOfFlight> bookingOfFlightList,
			Location departureLocation, Location destinationLocation,
			Calendar dateOfDeparture, Duration flightDuration) {
		this.partner = partner;
		this.bookingOfFlightList = bookingOfFlightList;
		this.departureLocation = departureLocation;
		this.destinationLocation = destinationLocation;
		this.dateOfDeparture = dateOfDeparture;
		this. flightDuration = flightDuration;
	}
	
	public Long getId() {
		return id;
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

	public Location getDepartureLocation() {
		return departureLocation;
	}

	public void setDepartureLocation(Location departureLocation) {
		this.departureLocation = departureLocation;
	}

	public Location getDestinationLocation() {
		return destinationLocation;
	}

	public void setDestinationLocation(Location destinationLocation) {
		this.destinationLocation = destinationLocation;
	}

	public List<Seat> getSeatList() {
		return seatList;
	}

	public void setSeatList(List<Seat> seatList) {
		this.seatList = seatList;
	}
	
	public Calendar getDateOfDeparture() {
		return dateOfDeparture;
	}

	public void setDateOfDeparture(Calendar dateOfDeparture) {
		this.dateOfDeparture = dateOfDeparture;
	}

	public Duration getFlightDuration() {
		return flightDuration;
	}

	public void setFlightDuration(Duration flightDuration) {
		this.flightDuration = flightDuration;
	}

	public List<Double> getDiscountsListEmployee() {
		return discountsListEmployee;
	}

	public List<Double> getDiscountsListPartner() {
		return discountsListPartner;
	}

	public void addBookingOfFlight(BookingOfFlight bof){
		this.bookingOfFlightList.add(bof);
	}
	
	public void addSeat(Seat s){
		this.seatList.add(s);
	}
	
	public boolean removeSeat(Seat s){
		return this.seatList.remove(s);
	}
	
	public void addDiscountEmployee(Double d){
		this.discountsListEmployee.add(d);
	}
	
	public boolean removeDiscountEmployee(Double d){
		return this.discountsListEmployee.remove(d);
	}
	
	public void addDiscountPartner(Double d){
		this.discountsListPartner.add(d);
	}
	
	public boolean removeDiscountPartner(Double d){
		return this.discountsListPartner.remove(d);
	}
	
	public Calendar getLandingTime(){
		this.getDateOfDeparture();
		this.getFlightDuration();
		Calendar c = Calendar.getInstance();
		c.setTime(dateOfDeparture.getTime());
		c.add(Calendar.MINUTE, Math.toIntExact(flightDuration.toMinutes()));
		return c;
	}
}