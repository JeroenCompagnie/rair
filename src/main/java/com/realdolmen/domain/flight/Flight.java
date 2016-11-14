package com.realdolmen.domain.flight;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.realdolmen.domain.user.Employee;
import com.realdolmen.domain.user.Partner;
import com.realdolmen.domain.user.User;

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

	private double STANDARD_CHARGING_RAIR = -0.1;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "partner_id", nullable = false)
	private Partner partner;

	// TODO cascadetype hier oppassen! bookingOfFlight hoort bij zowel een
	// Booking als een Flight
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private List<BookingOfFlight> bookingOfFlightList;

	@OneToOne
	private Airport departureAirport;

	@OneToOne
	private Airport destinationAirport;

	//TODO CHECK CASCADETYPES
	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	private List<Seat> seatList;


	//@Temporal(TemporalType.DATE)
	private Date dateOfDeparture;

	private Duration flightDuration;	
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<Discount> discountsList = new ArrayList<Discount>();
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private Discount defaultPriceCharge;

	public Flight() {

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
		this.discountsList = new ArrayList<Discount>();
		this.defaultPriceCharge = new Discount(true, true, STANDARD_CHARGING_RAIR);
	}

	public int getNumberOfSeatForType(SeatType st) {
		getSeatListWithoutNull();
		
		int count = 0;
		for (int i = 0; i < seatList.size(); i++) {
			if (seatList.get(i).getType().equals(st)) {
				count++;
			}
		}
		return count;
	}

	public void addBookingOfFlight(BookingOfFlight bof) {
		getBookingOfFlightsListWithoutNull();
		if(bookingOfFlightList.contains(bof)){
			System.err.println("9999988888  TRIED TO ADD BookingOfFlight that was already there");
			return;
		}
		System.err.println("Booking of flight with id: " + bof.getId() + " added");
		this.bookingOfFlightList.add(bof);
		getBookingOfFlightsListWithoutNull();
	}

	/**
	 * RETURNS NULL IF NOT ENOUGH SEATS AVAILABLE
	 * @param list
	 * @param customer
	 * @return
	 */
	public Booking addBooking(HashMap<SeatType, Integer> list, Booking booking){
		/**
		 * TODO: add discounts to price of seat => DONE; add to UI's => mostly done
		 * TODO: add locking!!
		 */
		if(checkIfEnoughSeatsAvailable(list)){

			for(Entry<SeatType, Integer> entry : list.entrySet()){
				for(int i = 0; i < entry.getValue();i++){
					Seat seat = getSeatWithType(entry.getKey());

					double price = seat.getBasePrice();
					price = applyDiscountsToPrice(price);


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

	protected double applyDiscountsToPrice(double price) {
		getDiscountsListWithoutNull();
		HashMap<Long, Discount> discountsRealValues = new HashMap<>(); 
		HashMap<Long, Discount> discountsPercentages = new HashMap<>();
		ArrayList<Discount> discounts = new ArrayList<>();
		discounts.addAll(discountsList);
		
		for(Discount d : discounts){
			// SEPARATE PERCENTAGES FROM REAL VALUES + make sure that each id is only put in a map once 
			if(d.isPercentage()){
				discountsPercentages.put(d.getId(), d);
				System.err.println(d.toString() + " " + d.getId());
			}
			else{
				discountsRealValues.put(d.getId(), d);
				System.err.println(d.toString() + " " + d.getId());
			}
		}
		System.err.println("-+-+-");
		// FIRST APPLY STANDARD RAIR PERCENTAGE:
		price = defaultPriceCharge.addDiscountToPrice(price);
		System.err.println(price + " from:" + defaultPriceCharge.toString());
		// FIRST APPLY DISCOUNT PERCENTAGES
		for(Entry<Long, Discount> entry : discountsPercentages.entrySet()){
			price = entry.getValue().addDiscountToPrice(price);
			System.err.println(price + " from:" + entry.getValue().toString());
		}
		// SECOND APPLY DISCOUNT REAL VALUES
		for(Entry<Long, Discount> entry : discountsRealValues.entrySet()){
			price = entry.getValue().addDiscountToPrice(price);
			System.err.println(price + " from:" + entry.getValue().toString());
		}
		return price;
	}
	
	public List<Discount> getListOfDiscountByEmployee(){
		getDiscountsListWithoutNull();
		HashMap<Long, Discount> discountsMap = new HashMap<>();
		for(Discount d : this.discountsList){ 
			if(d.isByEmployee()){
				discountsMap.put(d.getId(),d);
			}
		}
		ArrayList<Discount> discounts = new ArrayList<>();
		for(Entry<Long,Discount> d : discountsMap.entrySet()){
			discounts.add(d.getValue());
		}
		return discounts;
	}
	
	public List<Discount> getListOfDiscountByPartner(){
		getDiscountsListWithoutNull();
//		HashMap<Long, Discount> discountsMap = new HashMap<>();
		ArrayList<Discount> discountListByPartner = new ArrayList<>();
		for(Discount d : this.discountsList){ 
			if(!d.isByEmployee()){
				discountListByPartner.add(d);
			}
		}
		return discountListByPartner;
	}
	
	public void addDiscount(Discount d){
		getDiscountsListWithoutNull();
		this.discountsList.add(d);
	}

	public void getDiscountsListWithoutNull(){
		HashSet<Discount> hs = new HashSet<Discount>();
		hs.addAll(discountsList);
		discountsList.clear();
		discountsList.addAll(hs);
		while(discountsList.contains(null)){
			discountsList.remove(null);
		}
	}
	
	public void getBookingOfFlightsListWithoutNull(){
		HashSet<BookingOfFlight> hs = new HashSet<BookingOfFlight>();
		hs.addAll(bookingOfFlightList);
		bookingOfFlightList.clear();
		bookingOfFlightList.addAll(hs);
		while(bookingOfFlightList.contains(null)){
			bookingOfFlightList.remove(null);
		}
	}
	
	public void getSeatListWithoutNull(){
		HashSet<Seat> hs = new HashSet<Seat>();
		hs.addAll(seatList);
		seatList.clear();
		seatList.addAll(hs);
		while(seatList.contains(null)){
			seatList.remove(null);
		}
	}
	
	public void removeDiscount(Discount d) {
		getDiscountsListWithoutNull();
		HashSet<Discount> hs = new HashSet<Discount>();
		hs.addAll(discountsList);
		discountsList.clear();
		discountsList.addAll(hs);
		for(int i = 0; i < discountsList.size(); i++){
			if(discountsList.get(i).getId() == d.getId()){
				System.err.println("removeDiscount: SET DISCOUNT TO BE REMOVED TO NULL");
				discountsList.set(i, null);
			}
		}
		
//		for(int i = 0; i < discountsList.size() ; i++){
//			if(discountsList.get(i) != null && discountsList.get(i).getId() == d.getId() ){
//				discountsList.set(i, null);
//			}
//		}
		//		while(discountsList.contains(d)){
//			this.discountsList.remove(d);
//		}
	}

	/**
	 * RETURNS NULL IF SEAT NOT FOUND WITH THE GIVEN TYPE
	 * @param type
	 * @return
	 */
	protected Seat getSeatWithType(SeatType type) {
		getSeatListWithoutNull();
		Seat returnSeat = null;
		for(int i = 0; i < seatList.size(); i++ ){
			if(seatList.get(i).getType() == type){
				returnSeat = seatList.get(i);
			}
		}
		removeSeat(returnSeat);
		return returnSeat;
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
		getSeatListWithoutNull();
		this.seatList.add(s);
	}

	public void addSeats(ArrayList<Seat> seats) {
		for(Seat s : seats){
			addSeat(s);
		}
	}

	public void removeSeat(Seat s) {
		getSeatListWithoutNull();
		for(int i = 0; i < seatList.size(); i++){
			if(seatList.get(i).getId() == s.getId()){
				seatList.set(i, null);
			}
		}
//		while(seatList.contains(s)){
//			this.seatList.remove(s);
//		}
	}

	public Date getLandingTime() {
		return new Date(getDateOfDeparture().getTime() + getFlightDuration().toMillis());
	}

	public int getSeatsLeft(){
		getSeatListWithoutNull();
		return seatList.size();
	}

	/**
	 * Rerturns the seatprice with SeatType type. If no more seats available of this type the return value will be -1.
	 * @param type
	 * @return
	 */
	public double getSeatBasePrice(SeatType type){
		getSeatListWithoutNull();
		for(Seat s: seatList){
			if(s.getType() == type){
				return s.getBasePrice();
			}
		}
		return -1.0;
	}

	public void setSeatBasePrice(double newPrice, SeatType type){
		getSeatListWithoutNull();
		for(Seat s : seatList){
			if(s.getType().equals(type)){
				s.setBasePrice(newPrice);
			}
		}
	}
	
	public double getSeatPriceAfterDiscounts(SeatType type){
		getSeatListWithoutNull();
		for(Seat s: seatList){
			if(s.getType() == type){
				return applyDiscountsToPrice(s.getBasePrice());
			}
		}
		return -1.0;
	}
	
	public double getTotalDiscountPercentagePartner(){
		HashMap<Long, Discount> discountsByPartner = new HashMap<Long, Discount>();
		for(Discount d : discountsList){
			if(!d.isByEmployee()){
				discountsByPartner.put(d.getId(), d);
			}
		}
		double totalPercentage = 0.0;
		for(Entry<Long, Discount> entry : discountsByPartner.entrySet()){
			totalPercentage += entry.getValue().getDiscount();
		}
		return totalPercentage;
	}

	public int getSeatsLeft(SeatType type){
		getSeatListWithoutNull();
		int count = 0;
		for(Seat s : seatList){
			if(s.getType().toString().equals(type.toString())){
				count++;
			}
		}
		System.err.println("Seats with type: "+type+ " left: " + count);
		return count;
	}

	public int getSeatsSold(SeatType type){
		getBookingOfFlightsListWithoutNull();
		int count = 0;
		System.err.println("999999999999999 --- " + bookingOfFlightList.size());
		for(BookingOfFlight b : bookingOfFlightList){
			if(b.getSeat().getType() == type){
				count++;
			}
		}
		return count;
	}
	
	
	/*******************************************
	 * 
	 * GETTERS AND SETTERS
	 * 
	 *******************************************/
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
		getBookingOfFlightsListWithoutNull();
		return bookingOfFlightList;
	}

	public void setBookingOfFlightList(List<BookingOfFlight> bookingOfFlightList) {
		this.bookingOfFlightList = bookingOfFlightList;
		getBookingOfFlightsListWithoutNull();
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
		getSeatListWithoutNull();
		return seatList;
	}

	public void setSeatList(List<Seat> seatList) {
		this.seatList = seatList;
		getSeatListWithoutNull();
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


	public List<Discount> getDiscountsList() {
		return discountsList;
	}

	public Discount getDefaultPriceCharge() {
		return defaultPriceCharge;
	}

	public void setDefaultPriceCharge(User employee, Discount defaultPriceCharge) {
		if(employee.getClass() == Employee.class){
			this.defaultPriceCharge = defaultPriceCharge;
		}
	}
}
