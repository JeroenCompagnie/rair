package com.realdolmen.domain.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import com.realdolmen.domain.flight.Airport;
import com.realdolmen.domain.flight.Booking;
import com.realdolmen.domain.flight.Flight;
import com.realdolmen.domain.flight.GlobalRegion;
import com.realdolmen.domain.flight.PaymentMethod;
import com.realdolmen.domain.flight.PaymentStatus;
import com.realdolmen.domain.flight.SeatType;
import com.realdolmen.domain.user.Customer;
import com.realdolmen.domain.user.Partner;
import com.realdolmen.email.Email;
import com.realdolmen.repository.AirportRepository;
import com.realdolmen.repository.BookingRepository;
import com.realdolmen.repository.FlightRepository;
import com.realdolmen.repository.PartnerRepository;


@Named("search")
@SessionScoped ////////////////////////////////// IF DONT WORK TRY SESSIONSCOPE
public class SearchBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7245024228992246453L;
	private List<Flight> flights;

	@PersistenceContext
	private EntityManager entityManager;

	@Inject
	private FlightRepository flightRepository;

	@Inject
	private PartnerRepository partnerRepository;
	
	@Inject
	private AirportRepository airportRepository;
	
	@Inject
	private BookingRepository bookingRepository;
	
	@Inject
	private BookingBean bookingBean;
	
	@Inject
	private LoginBean loginBean;
	

	// @ManagedProperty("#{flightclasses}")
	private List<SeatType> flightclasses;

	private SeatType selectedFlightClass;
	
	//@NotNull(message="Date of departure needs to be filled in!")
	//@Future(message="Date of depature needs to be in the future")
	private Date dateOfDeparture;
	
	private Date dateOfReturn;
	
	private List<PaymentMethod> paymentMethods;
	
	private PaymentMethod selectedPaymentMethod;
	
	private Long selectedFlightId;
	
	private List<Airport> airports;
	
	private Airport selectedDestination;
	@NotNull
	private Airport selectedDeparture;
	
	private List<GlobalRegion> globalRegions;
	
	private GlobalRegion selectedGlobalRegion;
	@NotNull
	private int numberOfSeats;

	
	public Long getSelectedFlightId() {
		return selectedFlightId;
	}
	
	private Partner selectedPartner;
	

	public SeatType getSelectedFlightClass() {
		return selectedFlightClass;
	}
	

	public void setSelectedFlightClass(SeatType selectedFlightClass) {
		this.selectedFlightClass = selectedFlightClass;
	}

	public List<Airport> getAirports() {
		return airports;
	}

	public void setAirports(List<Airport> airports) {
		this.airports = airports;
	}

	public Airport getSelectedDestination() {
		return selectedDestination;
	}

	public void setSelectedDestination(Airport selectedDestination) {
		this.selectedDestination = selectedDestination;
	}

	public Airport getSelectedDeparture() {
		return selectedDeparture;
	}

	public void setSelectedDeparture(Airport selectedDeparture) {
		this.selectedDeparture = selectedDeparture;
	}

	public List<GlobalRegion> getGlobalRegions() {
		return globalRegions;
	}

	public void setGlobalRegions(List<GlobalRegion> globalRegions) {
		this.globalRegions = globalRegions;
	}

	private List<Partner> partners;
	

	public void setSelectedFlightId(Long selectedFlightId) {
		this.selectedFlightId = selectedFlightId;
	}
	
	@PostConstruct
	public void init() {
		globalRegions = new ArrayList<GlobalRegion>();
		for(GlobalRegion gr : GlobalRegion.values())
		{
			globalRegions.add(gr);
		}
		setNumberOfSeats(1);
		airports = new ArrayList<Airport>();
		airports = airportRepository.findAll();
		setFlights(new ArrayList<Flight>());
		setFlights(entityManager.createQuery("select f from Flight f", Flight.class).getResultList());
		flightclasses = new ArrayList<SeatType>();
		for(SeatType st : SeatType.values())
		{
			flightclasses.add(st);
		}
		
		setPartners(new ArrayList<Partner>());
		setPartners(partnerRepository.findAll());
		paymentMethods = new ArrayList<PaymentMethod>();
		for(PaymentMethod p : PaymentMethod.values())
		{
			paymentMethods.add(p);
		}
	}
	
	public String checkFlightData(Long id)
	{
		setSelectedFlightId(id);
		System.out.println(getSelectedFlightId() + "selected flight Id");
		return "booking.xhtml";
	}
	
	public void clearAll()
	{
		setSelectedDeparture(null);
		setSelectedDestination(null);
		setSelectedFlightClass(null);
		setSelectedFlightId(null);
		setSelectedGlobalRegion(null);
		setSelectedPartner(null);
		setSelectedPaymentMethod(null);
		setDateOfDeparture(null);
		setDateOfReturn(null);
		setNumberOfSeats(1);
		setFlights(null);
	}
	
	public String edit()
	{
		return "index?faces-redirect=true";
	}
	
	public String bookFlight()
	{	
		if (loginBean.getUserIsCustomer())
		{	
			System.out.println(selectedFlightId + " Flight Id");
			PaymentStatus ps;
			if(getSelectedPaymentMethod()==PaymentMethod.CreditCard)
			{
				ps=PaymentStatus.SUCCESS;
			}
			else
			{
				ps=PaymentStatus.PENDING;
			}

			Customer c = null;
			Booking booking = new Booking(ps,loginBean.getUser());
			System.err.println("Booking: 2");
			bookingRepository.save(booking);
			System.err.println("Booking: 3");
			Flight f = flightRepository.findById(selectedFlightId);
			System.out.println(f.getNumberOfSeatForType(getSelectedFlightClass())+ " free seats before booking");
			HashMap<SeatType,Integer> hm =	new HashMap<SeatType,Integer>();
			hm.put(getSelectedFlightClass(), getNumberOfSeats());
			
			//TODO: HERE ADD DISCOUNT IF ps == PaymentStatus.SUCCESS on each BookingOfFlight
				// or give it as an extra discount in addBookingOfFlight
			
			booking = f.addBooking(hm, booking);
			
			// TODO: THIS with f2 the retour flight to add it to the booking (normally)
			//booking = f2.addBooking(hm, booking);
			
			//bookingRepository.update(booking);
			
		//	bookingRepository.update(booking);
			flightRepository.update(f);
			System.out.println(f.getNumberOfSeatForType(getSelectedFlightClass())+ " free seats after booking");
			if(loginBean.getUserIsCustomer())
			{
				c =(Customer)loginBean.getUser();
			}
			String [] addresses= {c.getEmail()};
			Email.sendMailStandardSender(addresses, "Booking Confirmation", booking.printBooking());
			clearAll();

			bookingBean.setUrlCode(booking.getId());
			return "invoice.xhtml";
		}
		else 
		{
			return "login.xhtml?nouser=true";
		}
	}

	public List<Flight> getFlights() {
		return flights;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}

	//@NotNull
	public Date getDateOfDeparture() {
		return dateOfDeparture;
	}

	public void setDateOfDeparture(Date dateOfDeparture) {
		this.dateOfDeparture = dateOfDeparture;
	}

	public Date getDateOfReturn() {
		return dateOfReturn;
	}
	//REMOVE
	/*private Date removeTimeFromDate(Date d)
	{
		calendar.clear();
		calendar.setTime(d);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
		Date date = calendar.getTime();	
		return date;
	}*/
	
	public void setDateOfReturn(Date dateOfReturn) {
		this.dateOfReturn = dateOfReturn;
	}

	public SeatType getSelectedFlightclass() {
		return selectedFlightClass;
	}


	public List<SeatType> getFlightclasses() {
		return flightclasses;
	}

	public void setFlightclasses(List<SeatType> flightclasses) {
		this.flightclasses = flightclasses;
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
	
	public String search()
	{
		System.out.println("called search from searchbean");
		setFlights(flightRepository.findByParams(getSelectedFlightclass(), getSelectedPartner(), getDateOfDeparture(),getSelectedDestination(),getSelectedDeparture(),getSelectedGlobalRegion(),getNumberOfSeats()));
		return "search.xhtml";
	}

	public Partner getSelectedPartner() {
		return selectedPartner;
	}

	public void setSelectedPartner(Partner selectedPartner) {
		this.selectedPartner = selectedPartner;
	}

	public List<Partner> getPartners() {
		return partners;
	}

	public void setPartners(List<Partner> partners) {
		this.partners = partners;
	}
	
	public int getNumberOfSeats(){
		return numberOfSeats;
	}
	
	public void setNumberOfSeats(int numberOfSeats){
		this.numberOfSeats = numberOfSeats;
	}

	public GlobalRegion getSelectedGlobalRegion() {
		return selectedGlobalRegion;
	}

	public void setSelectedGlobalRegion(GlobalRegion selectedGlobalRegion) {
		this.selectedGlobalRegion = selectedGlobalRegion;
	}

}