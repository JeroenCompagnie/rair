package com.realdolmen.domain.flight;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CollectionType;

import com.realdolmen.domain.user.Customer;
import com.realdolmen.domain.user.Partner;

@NamedQueries(@NamedQuery(name = Flight.findAll, query = "SELECT f FROM Flight f"))

@Entity
public class Flight implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6851290785176539664L;
	public static final String findAll = "Flight.findAll";

	/**
	 * TODO: params
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "partner_id", nullable = false)
	private Partner partner;

	// TODO cascadetype hier oppassen! bookingOfFlight hoort bij zowel een
	// Booking als een Flight
	@OneToMany(fetch=FetchType.EAGER)
	private List<BookingOfFlight> bookingOfFlightList;
	
	@OneToOne
	private Airport departureAirport;

	@OneToOne
	private Airport destinationAirport;

	//TODO CHECK CASCADETYPES
	@OneToMany(fetch = FetchType.EAGER)
	private List<Seat> seatList;

	private Date dateOfDeparture;

	private Duration flightDuration;

	@ElementCollection
	private List<Double> discountsListEmployee = new ArrayList<Double>();

	@ElementCollection
	private List<Double> discountsListPartner = new ArrayList<Double>();
	
	private static final int milliSecondsInOneSecond = 60000;

	public Flight() {

	}

	public int getNumberOfSeatForType(SeatType st) {
		int count = 0;
		for (int i = 0; i < seatList.size(); i++) {
			if (seatList.get(i).getType().equals(st)) {
				count++;
			}
		}
		return count;
	}

	public Flight(Partner partner,  Airport departureAirport,
			Airport destinationAirport, Date dateOfDeparture, Duration flightDuration) {
		this.partner = partner;
		this.departureAirport = departureAirport;
		this.destinationAirport = destinationAirport;
		this.dateOfDeparture = dateOfDeparture;
		this.flightDuration = flightDuration;
		this.bookingOfFlightList = new ArrayList<>();
		this.seatList = new ArrayList<>();
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

	public Airport getDepartureAirport() {
		return departureAirport;
	}

	public void setDepartureAirport(Airport departureAirport) {
		this.departureAirport = departureAirport;
	}

	public Airport getDestinationAirport() {
		return destinationAirport;
	}

	public void setDestinationAirport(Airport destinationAirport) {
		this.destinationAirport = destinationAirport;
	}

	public List<Seat> getSeatList() {
		return seatList;
	}

	public void setSeatList(List<Seat> seatList) {
		this.seatList = seatList;
	}

	public Date getDateOfDeparture() {
		return dateOfDeparture;
	}

	public void setDateOfDeparture(Date dateOfDeparture) {
		this.dateOfDeparture = dateOfDeparture;
	}

	public Duration getFlightDuration() {
		return flightDuration;
	}
	
	public int getFlightDurationInMinutes() {
		return ((int) flightDuration.toMinutes());
//		return ((int) flightDuration.toMillis())*milliSecondsInOneSecond;
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

	public void addBookingOfFlight(BookingOfFlight bof) {
		if(bookingOfFlightList.contains(bof)){
			System.err.println("9999988888  TRIED TO ADD BookingOfFlight that was already there");
			return;
		}
		System.err.println("Booking of flight with id: " + bof.getId() + " added");
		this.bookingOfFlightList.add(bof);
	}
	
	/**
	 * RETURNS NULL IF NOT ENOUGH SEATS AVAILABLE
	 * @param list
	 * @param customer
	 * @return
	 */
	public Booking addBooking(HashMap<SeatType, Integer> list, Booking booking){
		/**
		 * TODO: add discounts to price of seat
		 * TODO: add locking!!
		 */
		if(checkIfEnoughSeatsAvailable(list)){


			for(Entry<SeatType, Integer> entry : list.entrySet()){
				for(int i = 0; i < entry.getValue();i++){
					Seat seat = getSeatWithType(entry.getKey());
					//TODO: add discounts to price here!
					double price = seat.getBasePrice();
					BookingOfFlight bof = new BookingOfFlight(price, this, booking, seat);
					this.addBookingOfFlight(bof);
					booking.addBookingOfFlight(bof);
				}
			}
			return booking;
		}
		else{
			return null;
		}
	}

	/**
	 * RETURNS NULL IF SEAT NOT FOUND WITH THE GIVEN TYPE
	 * @param type
	 * @return
	 */
	private Seat getSeatWithType(SeatType type) {
		for(int i = 0; i < seatList.size(); i++ ){
			if(seatList.get(i).getType() == type){
				return seatList.remove(i);
			}
		}
		return null;
	}

	private boolean checkIfEnoughSeatsAvailable(HashMap<SeatType, Integer> list) {
		for(Entry<SeatType, Integer> entry : list.entrySet()){
			if(getSeatsLeft(entry.getKey()) < entry.getValue()){
				System.err.println("NOT ENOUGH SEATS");
				return false;
			}
		}
		return true;
	}

	public void addSeat(Seat s) {
		this.seatList.add(s);
	}

	public void addSeats(ArrayList<Seat> seats) {
		for(Seat s : seats){
			addSeat(s);
		}
	}

	public boolean removeSeat(Seat s) {
		return this.seatList.remove(s);
	}

	public void addDiscountEmployee(Double d) {
		this.discountsListEmployee.add(d);
	}

	public boolean removeDiscountEmployee(Double d) {
		return this.discountsListEmployee.remove(d);
	}

	public void addDiscountPartner(Double d) {
		this.discountsListPartner.add(d);
	}

	public boolean removeDiscountPartner(Double d) {
		return this.discountsListPartner.remove(d);
	}

	public Date getLandingTime() {
		return new Date(getDateOfDeparture().getTime() + getFlightDuration().toMillis());
	}
	
	public int getSeatsLeft(){
		return seatList.size();
	}
	
	/**
	 * Rerturns the seatprice with SeatType type. If no more seats available of this type the return value will be -1.
	 * @param type
	 * @return
	 */
	public double getSeatPrice(SeatType type){
		for(Seat s: seatList){
			if(s.getType() == type){
				return s.getBasePrice();
			}
		}
		return -1.0;
	}
	
	public void setSeatPrice(double newPrice, SeatType type){
		for(Seat s : seatList){
			if(s.getType().equals(type)){
				s.setBasePrice(newPrice);
			}
		}
	}
	
	public int getSeatsLeft(SeatType type){
		int count = 0;
		for(Seat s : seatList){
			if(s.getType().toString().equals(type.toString())){
				System.err.println("TYPE OF THE SEATTHING: " +s.getType());
				count++;
			}
		}
		System.err.println("Seats with type: "+type+ " left: " + count);
		return count;
	}

	public int getSeatsSold(SeatType type){
		int count = 0;
		System.err.println("999999999999999 --- " + bookingOfFlightList.size());
		for(BookingOfFlight b : bookingOfFlightList){
			if(b.getSeat().getType() == type){
				count++;
				System.err.println("Seat id: " + b.getSeat().getId());
				System.err.println("BookingOfFlight id: " + b.getId());
			}
		}
		return count;
	}
}
