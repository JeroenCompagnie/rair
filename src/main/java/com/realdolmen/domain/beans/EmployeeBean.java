package com.realdolmen.domain.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;

import com.realdolmen.domain.flight.Airport;
import com.realdolmen.domain.flight.Flight;
import com.realdolmen.domain.flight.SeatType;
import com.realdolmen.repository.AirportRepository;
import com.realdolmen.repository.EmployeeRepository;
import com.realdolmen.repository.FlightRepository;

@Named("employeeBean")
@SessionScoped
public class EmployeeBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4517882120486152250L;

	@Inject
	private LoginBean loginBean;

	@EJB
	private EmployeeRepository employeeRepository;

	@EJB
	private FlightRepository flightRepository;

	@EJB
	private AirportRepository airportReposistory;

	@Before
	public void init(){
		System.err.println(this.hashCode());
	}

	private String searchCriteriaCountryDeparture = "";

	private String searchCriteriaCountryDestination = "";

	private List<Airport> airportsDeparture = new ArrayList<>();

	private List<Airport> airportsDestination = new ArrayList<>();

	private int flightId = -1;

	private Flight flight;
	
	private double newDefaultPriceCharge; 
	
	private String newDefaultPriceChargeDiscountType;
	
	

	public int getFlightId() {
		return flightId;
	}

	public void setFlightId(int flightId) {
		this.flightId = flightId;
	}

	public boolean getFlightIdExists(){
		if(getFlight() == null){
			return false;
		}
		else{
			return true;
		}
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
		airportsDeparture =  airportReposistory.findWithCountry(searchCriteriaCountryDeparture);
		return airportsDeparture;
	}

	public List<Airport> getAirportsDestination(){
		airportsDestination = airportReposistory.findWithCountry(searchCriteriaCountryDestination);
		return airportsDestination;
	}

	public void searchAirportDepartureCountry(){
		getAirportsDeparture();
	}

	public void searchAirportDestinationCountry(){
		getAirportsDestination();
	}

	public List<Flight> getFlights(){
		return flightRepository.findAll();
	}

	public Flight getFlight(){
		if(flight != null && flightId == flight.getId()){
			return flight;
		}
		return flightRepository.findById(flightId);
	}

	public boolean getPartnerFlightIdExists(){
		if(getFlight() == null){
			return false;
		}
		else{
			return true;
		}
	}

	public double getEconomySeatPrice(){
		return getFlight().getSeatBasePrice(SeatType.Economy);
	}

	public double getBusinessSeatPrice(){
		return getFlight().getSeatBasePrice(SeatType.Business);
	}

	public double getFirstSeatPrice(){
		return getFlight().getSeatBasePrice(SeatType.FirstClass);
	}

	/**
	 * Returns number of seats (for each seattype) left
	 * @return
	 */
	public int getNumberOfSeatsLeft(Flight f){
		return flightRepository.getNumberOfSeatsLeft(
				f, Arrays.asList(SeatType.values()));
	}

	public int getNumberOfSeatsLeft(String type){
		return flightRepository.getNumberOfSeatsLeft(
				getFlight(), Arrays.asList(SeatType.valueOf(type)));
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
		return flightRepository.getNumberOfSeatsBooked(
				f, Arrays.asList(SeatType.values()));
	}

	public int getNumberOfSeatsBooked(String type){
		return flightRepository.getNumberOfSeatsBooked( 
				getFlight(), Arrays.asList(SeatType.valueOf(type)));
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
	
	/**
	 * 
	 * For changing default price charge.
	 * 
	 */
	public double getNewDefaultPriceCharge() {
		return newDefaultPriceCharge;
	}

	public void setNewDefaultPriceCharge(double newDefaultPriceCharge) {
		this.newDefaultPriceCharge = newDefaultPriceCharge;
	}

	public String getNewDefaultPriceChargeDiscountType() {
		return newDefaultPriceChargeDiscountType;
	}

	public void setNewDefaultPriceChargeDiscountType(String newDefaultPriceChargeDiscountType) {
		this.newDefaultPriceChargeDiscountType = newDefaultPriceChargeDiscountType;
	}
	
	public List<String> getDiscountTypesDefaultPriceCharge(){
		ArrayList<String> types = new ArrayList<String>();
		types.add("Percentage");
		types.add("Real value");
		return types;
	}

	public void setDefaultPriceCharge(){
		
	}
	
	
}
