package com.realdolmen.domain.beans;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.junit.Before;

import com.realdolmen.domain.flight.Airport;
import com.realdolmen.domain.flight.BookingOfFlight;
import com.realdolmen.domain.flight.Flight;
import com.realdolmen.domain.flight.Seat;
import com.realdolmen.domain.flight.SeatType;
import com.realdolmen.domain.user.Partner;
import com.realdolmen.repository.PartnerRepository;

@Named("partnerBean")
@SessionScoped
public class PartnerBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4517882120486152250L;

	@Inject
	private LoginBean loginBean;
	
	@EJB
	private PartnerRepository partnerRepository;
	
	@Before
	public void init(){
		System.err.println(this.hashCode());
	}
	
	@NotNull(message = "Number of seats needs to be filled in")
	@Min(value=0, message = "Number of seats needs to be between 0 and 1200")
	@Max(value=1200, message = "Number of seats needs to be between 0 and 1200")
	private int nrBusinessSeats;
	
	@NotNull(message = "Number of seats needs to be filled in")
	@Min(value=0, message = "Number of seats needs to be between 0 and 1200")
	@Max(value=1200, message = "Number of seats needs to be between 0 and 1200")
	private int nrEconomySeats;
	
	@NotNull(message = "Number of seats needs to be filled in")
	@Min(value=0, message = "Number of seats needs to be between 0 and 1200")
	@Max(value=1200, message = "Number of seats needs to be between 0 and 1200")
	private int nrFirstSeats;
	
	@NotNull(message = "Price of a seat needs to be filled in")
	@Min(value=0, message = "Price of a seat needs to be higher as 0")
	private double priceBusinessSeats;
	
	@NotNull(message = "Price of a seat needs to be filled in")
	@Min(value=0, message = "Price of a seat needs to be higher as 0")
	private double priceEconomySeats;
	
	@NotNull(message = "Price of a seat needs to be filled in")
	@Min(value=0, message = "Price of a seat needs to be higher as 0")
	private double priceFirstSeats;
	
	@NotNull(message = "Needs a departure date")
	private Date dateOfDeparture;
	
	@NotNull(message = "Needs a flight duration")
	@Min(value=0, message = "Flight duration needs to be higher as 0")
	private int flightDuration;
	
	@NotNull(message = "Needs a departure airport")
	private Airport departureAirport;
	
	@NotNull(message = "Needs a destination airport")
	private Airport destinationAirport;
//	
//	@NotEmpty(message = "Needs a departure location")
//	private String departureLocation;
//	
//	@NotEmpty(message = "Needs a destination location")
//	private String destinationLocation;
	
	private String searchCriteriaCountryDeparture = "";
	
	private String searchCriteriaCountryDestination = "";
	
	private List<Airport> airportsDeparture = new ArrayList<>();
	
	private List<Airport> airportsDestination = new ArrayList<>();
	
	private int partnerFlightId = -1;
	
	private Flight partnerFlight;

	private double economySeatTypePrice;

	private double businessSeatTypePrice;
	
	private double firstSeatTypePrice;
//	private boolean partnerFlightIdIsNull;
	
	public int getNrBusinessSeats() {
		return nrBusinessSeats;
	}

	public void setNrBusinessSeats(int nrBusinessSeats) {
		this.nrBusinessSeats = nrBusinessSeats;
	}

	public int getNrEconomySeats() {
		return nrEconomySeats;
	}

	public void setNrEconomySeats(int nrEconomySeats) {
		this.nrEconomySeats = nrEconomySeats;
	}

	public int getNrFirstSeats() {
		return nrFirstSeats;
	}

	public void setNrFirstSeats(int nrFirstSeats) {
		this.nrFirstSeats = nrFirstSeats;
	}

	public double getPriceBusinessSeats() {
		return priceBusinessSeats;
	}

	public void setPriceBusinessSeats(double priceBusinessSeats) {
		this.priceBusinessSeats = priceBusinessSeats;
	}

	public double getPriceEconomySeats() {
		return priceEconomySeats;
	}

	public void setPriceEconomySeats(double priceEconomySeats) {
		this.priceEconomySeats = priceEconomySeats;
	}

	public double getPriceFirstSeats() {
		return priceFirstSeats;
	}

	public void setPriceFirstSeats(double priceFirstSeats) {
		this.priceFirstSeats = priceFirstSeats;
	}
	
//	public String getDepartureLocation() {
//		return departureLocation;
//	}
//
//	public void setDepartureLocation(String departureLocation) {
//		this.departureLocation = departureLocation;
//	}
//
//	public String getDestinationLocation() {
//		return destinationLocation;
//	}
//
//	public void setDestinationLocation(String destinationLocation) {
//		this.destinationLocation = destinationLocation;
//	}

	public Date getDateOfDeparture() {
		return dateOfDeparture;
	}

	public void setDateOfDeparture(Date dateOfDeparture) {
		this.dateOfDeparture = dateOfDeparture;
	}

	public int getFlightDuration() {
		return flightDuration;
	}

	public void setFlightDuration(int flightDuration) {
		this.flightDuration = flightDuration;
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
	
	public int getPartnerFlightId() {
		return partnerFlightId;
	}

	public void setPartnerFlightId(int partnerFlightId) {
		this.partnerFlightId = partnerFlightId;
	}

	public String getSearchCriteriaCountryDeparture() {
		return searchCriteriaCountryDeparture;
	}

	public void setSearchCriteriaCountryDeparture(String searchCriteriaCountryDeparture) {
		this.searchCriteriaCountryDeparture = searchCriteriaCountryDeparture;
	}

	public String getSearchCriteriaCountryDestination() {
		return searchCriteriaCountryDestination;
	}

	public void setSearchCriteriaCountryDestination(String searchCriteriaCountryDestination) {
		this.searchCriteriaCountryDestination = searchCriteriaCountryDestination;
	}

	public List<Airport> getAirportsDeparture(){
		airportsDeparture =  partnerRepository.getAllAirports(searchCriteriaCountryDeparture);
		return airportsDeparture;
	}
	
	public List<Airport> getAirportsDestination(){
		airportsDestination = partnerRepository.getAllAirports(searchCriteriaCountryDestination);
		return airportsDestination;
	}
	
	public void searchAirportDepartureCountry(){
		getAirportsDeparture();
	}
	
	public void searchAirportDestinationCountry(){
		getAirportsDestination();
	}
	
	public ArrayList<Flight> getFlightsOfPartner(){
		return partnerRepository.getFlightsByPartner(loginBean.getUser());
	}
	
	public Flight getPartnerFlight(){
		if(partnerFlight != null && partnerFlightId == partnerFlight.getId()){
			return partnerFlight;
		}
		return partnerRepository.getFlightByPartner(loginBean.getUser(), new Long(partnerFlightId));
	}
	
	public boolean getPartnerFlightIdExists(){
		if(getPartnerFlight() == null){
			return false;
		}
		else{
			return true;
		}
	}
	
	public double getEconomySeatPrice(){
		return getPartnerFlight().getSeatPrice(SeatType.Economy);
	}
	
	public void setEconomySeatPrice(double newPrice){
		economySeatTypePrice = newPrice;
	}
	
	public String setEconomySeatPriceInDb(){
		partnerRepository.setSeatPrice(loginBean.getUser(), new Long(partnerFlightId), SeatType.Economy, economySeatTypePrice);
		return "";
	}
	
	public double getBusinessSeatPrice(){
		return getPartnerFlight().getSeatPrice(SeatType.Business);
	}
	
	public void setBusinessSeatPrice(double newPrice){
		businessSeatTypePrice = newPrice;
	}
	
	public String setBusinessSeatPriceInDb(){
		partnerRepository.setSeatPrice(loginBean.getUser(), new Long(partnerFlightId), SeatType.Business, businessSeatTypePrice);
		return "";
	}
	
	public double getFirstSeatPrice(){
		return getPartnerFlight().getSeatPrice(SeatType.FirstClass);
	}
	
	public void setFirstSeatPrice(double newPrice){
		firstSeatTypePrice=newPrice;
	}
	
	public String setFirstSeatPriceInDb(){
		partnerRepository.setSeatPrice(loginBean.getUser(), new Long(partnerFlightId), SeatType.FirstClass, firstSeatTypePrice);
		return "";
	}

	/**
	 * Returns number of seats (for each seattype) left
	 * @return
	 */
	public int getNumberOfSeatsLeft(Flight f){
		return partnerRepository.getNumberOfSeatsLeft(loginBean.getUser(), 
				f, Arrays.asList(SeatType.values()));
	}
	
	public int getNumberOfSeatsLeft(String type){
		return partnerRepository.getNumberOfSeatsLeft(loginBean.getUser(), 
				getPartnerFlight(), Arrays.asList(SeatType.valueOf(type)));
		//return getPartnerFlight().getNumberOfSeatForType(SeatType.valueOf(type));
	}
	
	public int getNumberOfSeatsLeftEconomy(){
		return getNumberOfSeatsLeft(SeatType.Economy.toString());
	}
	
	public int getNumberOfSeatsLeftBusiness(){
		return getNumberOfSeatsLeft(SeatType.Business.toString());
	}
	
	public int getNumberOfSeatsLeftFirst(){
		return getNumberOfSeatsLeft(SeatType.FirstClass.toString());
	}
	
	/**
	 * Returns number of seats (for each seattype) booked 
	 * @return
	 */
	public int getNumberOfSeatsBooked(Flight f){
		return partnerRepository.getNumberOfSeatsBooked(loginBean.getUser(), 
				f, Arrays.asList(SeatType.values()));
	}
	
	public int getNumberOfSeatsBooked(String type){
		return partnerRepository.getNumberOfSeatsBooked(loginBean.getUser(), 
				getPartnerFlight(), Arrays.asList(SeatType.valueOf(type)));
//		return getPartnerFlight().getSeatsSold(SeatType.valueOf(type));
	}
	
	public int getNumberOfSeatsBookedEconomy(){
		return getNumberOfSeatsBooked(SeatType.Economy.toString());
	}
	
	public int getNumberOfSeatsBookedBusiness(){
		return getNumberOfSeatsBooked(SeatType.Business.toString());
	}
	
	public int getNumberOfSeatsBookedFirst(){
		return getNumberOfSeatsBooked(SeatType.FirstClass.toString());
	}
	
	public String addFlight(){
		System.err.println("Tried to add flight");
		Flight f = new Flight((Partner) loginBean.getUser(), 
				departureAirport, 
				destinationAirport, 
				dateOfDeparture, Duration.ofMinutes(flightDuration));
		
		partnerRepository.addFlight((Partner) loginBean.getUser(), f, 
				nrBusinessSeats, priceBusinessSeats,
				nrEconomySeats, priceEconomySeats,
				nrFirstSeats, priceFirstSeats);
		
		String message = "Fligt added!";
	    FacesContext.getCurrentInstance().addMessage(null, 
	        new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
	    
	    clearAllInput();
		return "";
	}

	private void clearAllInput() {
		setNrBusinessSeats(0);
		setNrEconomySeats(0);
		setNrFirstSeats(0);
		setPriceBusinessSeats(0.0);
		setPriceEconomySeats(0.0);
		setPriceFirstSeats(0.0);
			//		setDepartureLocation("");
			//		setDestinationLocation("");
		setDepartureAirport(null);
		setDestinationAirport(null);
		setFlightDuration(0);
		setDateOfDeparture(null);
	}
	
}
