package com.realdolmen.domain.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import com.realdolmen.domain.flight.Flight;
import com.realdolmen.domain.flight.PaymentMethod;
import com.realdolmen.domain.flight.SeatType;
import com.realdolmen.domain.user.Partner;
import com.realdolmen.repository.FlightRepository;
import com.realdolmen.repository.PartnerRepository;

@Named("search")
@SessionScoped ////////////////////////////////// IF DONT WORK TRY SESSIONSCOPE
public class SearchBean implements Serializable {

	private List<Flight> flights;
	private List<Flight> flights2;

	@PersistenceContext
	private EntityManager entityManager;

	@EJB
	private FlightRepository flightRepository;

	@EJB
	private PartnerRepository partnerRepository;

	// @ManagedProperty("#{flightclasses}")
	private List<String> flightclasses;
	// @ManagedProperty("#{airlines}")
	private List<String> airlines;

	private String selectedAirline;

	private String selectedFlightClass;
	
	private Date dateOfDeparture;
	
	private Date dateOfReturn;
	
	private List<PaymentMethod> paymentMethods;
	
	private PaymentMethod selectedPaymentMethod;

	@PostConstruct
	public void init() {
		setFlights(new ArrayList<Flight>());
		setFlights(entityManager.createQuery("select f from Flight f", Flight.class).getResultList());
		// setFlights2(entityManager.createNamedQuery("Flight.findAll",Flight.class).getResultList());;
		Partner partner = partnerRepository.findById(1002L);
		// System.out.println("partnerName="+partner.getName());
		// setFlights2(flightRepository.findByParams(SeatType.Economy, partner,
		// new Date()));
		setFlights2(flightRepository.findByParams3(SeatType.Economy, partner, new Date()));
		flightclasses = new ArrayList<String>();
		airlines = new ArrayList<String>();
		flightclasses.add("Economy");
		flightclasses.add("Business");
		airlines.add("AirTerror");
		airlines.add("CrashAirline");
		paymentMethods = new ArrayList<PaymentMethod>();
		for(PaymentMethod p : PaymentMethod.values())
		{
			paymentMethods.add(p);
		}
	}

	public List<Flight> getFlights() {
		return flights;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}

	public List<Flight> getFlights2() {
		return flights2;
	}

	public void setFlights2(List<Flight> flights2) {
		this.flights2 = flights2;
	}

	public String getSelectedAirline() {
		return selectedAirline;
	}

	@NotNull
	public Date getDateOfDeparture() {
		return dateOfDeparture;
	}

	public void setDateOfDeparture(Date dateOfDeparture) {
		this.dateOfDeparture = dateOfDeparture;
	}

	public Date getDateOfReturn() {
		return dateOfReturn;
	}

	public void setDateOfReturn(Date dateOfReturn) {
		this.dateOfReturn = dateOfReturn;
	}

	public void setSelectedAirline(String selectedAirline) {
		this.selectedAirline = selectedAirline;
	}

	public String getSelectedFlightclass() {
		return selectedFlightClass;
	}

	public void setSelectedFlightclass(String selectedFlightclass) {
		this.selectedFlightClass = selectedFlightclass;
	}

	public List<String> getFlightclasses() {
		return flightclasses;
	}

	public void setFlightclasses(List<String> flightclasses) {
		this.flightclasses = flightclasses;
	}

	public List<String> getAirlines() {
		return airlines;
	}

	public void setAirlines(List<String> airlines) {
		this.airlines = airlines;
	}
	
	public String goToSearchPage()
	{
		return null;
	}

	public List<PaymentMethod> getPaymentMethods() {
		return paymentMethods;
	}

	public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

	public PaymentMethod getSelectedPaymentMethod() {
		return selectedPaymentMethod;
	}

	public void setSelectedPaymentMethod(PaymentMethod selectedPaymentMethod) {
		this.selectedPaymentMethod = selectedPaymentMethod;
	}
}
