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
import com.realdolmen.domain.flight.DiscountPercentage;
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
	private List<Flight> returnFlights;

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

	// @NotNull(message="Date of departure needs to be filled in!")
	// @Future(message="Date of depature needs to be in the future")
	private Date dateOfDeparture;

	private Date dateOfReturn;

	private List<PaymentMethod> paymentMethods;

	private PaymentMethod selectedPaymentMethod;

	private Long selectedFlightId;
	
	private Long selectedReturnFlightId;

	private List<Airport> airports;
	@NotNull
	private Airport selectedDestination;
	@NotNull
	private Airport selectedDeparture;

	private List<GlobalRegion> globalRegions;

	private GlobalRegion selectedGlobalRegion;
	@NotNull
	private int numberOfSeats;

	private boolean returnDate;

	public boolean getIsReturnDate() {
		System.out.println("returnDate " + returnDate);
		return returnDate;
	}

	public void setReturnDate(boolean returnDate) {
		this.returnDate = returnDate;
	}

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
		System.out.println("selectedFlight altered");
		this.selectedFlightId = selectedFlightId;
	}

	public boolean getSelectedFlightIdIsNull(){
		return selectedFlightId == null;
	}
	

	@PostConstruct
	public void init() {
		globalRegions = new ArrayList<GlobalRegion>();
		for (GlobalRegion gr : GlobalRegion.values()) {
			globalRegions.add(gr);
		}
		setNumberOfSeats(1);
		airports = new ArrayList<Airport>();
		airports = airportRepository.findAll();
		setFlights(new ArrayList<Flight>());
		setFlights(entityManager.createQuery("select f from Flight f", Flight.class).getResultList());
		flightclasses = new ArrayList<SeatType>();
		for (SeatType st : SeatType.values()) {
			flightclasses.add(st);
		}

		setPartners(new ArrayList<Partner>());
		setPartners(partnerRepository.findAll());
		paymentMethods = new ArrayList<PaymentMethod>();
		for (PaymentMethod p : PaymentMethod.values()) {
			paymentMethods.add(p);
		}
	}

	public String checkFlightData(Long id) {
		setSelectedFlightId(id);
		System.out.println(getSelectedFlightId() + "selected flight Id");
		return "booking.xhtml";
	}
	
	public String checkFlightData() {
		return "booking.xhtml";
	}

	public void clearAll() {
		setSelectedDeparture(null);
		setSelectedDestination(null);
		setSelectedFlightClass(null);
		setSelectedFlightId(null);
		setSelectedReturnFlightId(null);
		setSelectedGlobalRegion(null);
		setSelectedPartner(null);
		setSelectedPaymentMethod(null);
		setDateOfDeparture(null);
		setDateOfReturn(null);
		setNumberOfSeats(1);
		setFlights(null);
		setReturnFlights(null);
		setDateOfReturn(null);
	}

	public String edit() {
		return "index?faces-redirect=true";
	}

	public String bookFlight() {
		if (loginBean.getUserIsCustomer()) {
			System.out.println(selectedFlightId + " Flight Id");
			PaymentStatus ps;
			if (getSelectedPaymentMethod() == PaymentMethod.CreditCard) {
				ps = PaymentStatus.SUCCESS;
			} else {
				ps = PaymentStatus.PENDING;
			}

			Customer c = null;
			Booking booking = new Booking(ps, loginBean.getUser());
			System.err.println("Booking: 2");
			bookingRepository.save(booking);
			System.err.println("Booking: 3");
			Flight f = flightRepository.findById(selectedFlightId);
			HashMap<SeatType, Integer> hm = new HashMap<SeatType, Integer>();
			hm.put(getSelectedFlightClass(), getNumberOfSeats());
			
			DiscountPercentage discountCredit = null;
			if(getSelectedPaymentMethod()==PaymentMethod.CreditCard){
				discountCredit = new DiscountPercentage(true, PaymentMethod.CreditCard.getDiscount());
				System.err.println("MADE CREDITCARD DISCOUNT WITH DISCOUNT: " + PaymentMethod.CreditCard.getDiscount());
			}
			booking = f.addBooking(hm, booking,discountCredit);
			flightRepository.update(f);
			if(getSelectedReturnFlightId() != null)
			{
				Flight f2 = flightRepository.findById(selectedReturnFlightId);
				booking = f2.addBooking(hm, booking,discountCredit);
				flightRepository.update(f2);
			}

			
			System.out.println(f.getNumberOfSeatForType(getSelectedFlightClass()) + " free seats after booking");
			if (loginBean.getUserIsCustomer()) {
				c = (Customer) loginBean.getUser();
			}
			String[] addresses = { c.getEmail() };
			Email.sendMailStandardSender(addresses, "Booking Confirmation", booking.printBooking());
			clearAll();
			
			bookingBean.setUrlCode(booking.getId());
			bookingBean.setAfterBooking(true);
			return "invoice.xhtml";
		} else {
			return "login.xhtml?nouser=true";
		}
	}
	
	public String returnBack()
	{
		//setSelectedFlightId(null);
		//setSelectedReturnFlightId(null);
		return "index.xhtml";
	}

	public List<Flight> getFlights() {
		return flights;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}

	// @NotNull
	public Date getDateOfDeparture() {
		return dateOfDeparture;
	}

	public void setDateOfDeparture(Date dateOfDeparture) {
		this.dateOfDeparture = dateOfDeparture;
	}

	public Date getDateOfReturn() {
		return dateOfReturn;
	}
	// REMOVE
	/*
	 * private Date removeTimeFromDate(Date d) { calendar.clear();
	 * calendar.setTime(d); calendar.set(Calendar.MINUTE, 0);
	 * calendar.set(Calendar.SECOND, 0); calendar.set(Calendar.MILLISECOND, 0);
	 * Date date = calendar.getTime(); return date; }
	 */

	public void setDateOfReturn(Date dateOfReturn) {
		if(dateOfReturn != null)
		{
			setReturnDate(true);
		}
		this.dateOfReturn = dateOfReturn;
	}

	public List<SeatType> getFlightclasses() {
		return flightclasses;
	}

	public void setFlightclasses(List<SeatType> flightclasses) {
		this.flightclasses = flightclasses;
	}

	public String goToSearchPage() {
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

	public String search() {
		System.out.println("called search from searchbean");
		setFlights(flightRepository.findByParams(getSelectedFlightClass(), getSelectedPartner(), getDateOfDeparture(),
				getSelectedDestination(), getSelectedDeparture(), getSelectedGlobalRegion(), getNumberOfSeats()));
		setReturnFlights(flightRepository.findByParams(getSelectedFlightClass(), getSelectedPartner(), getDateOfReturn(),
				getSelectedDeparture(), getSelectedDestination(), null, getNumberOfSeats()));
		return "search.xhtml";
	}

	/*public void someListener(SelectEvent event) {  
	    selectedFlightId=(Flight)event.   ;  // cast "Object" to "Foo" 
	} */ 
	
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

	public int getNumberOfSeats() {
		return numberOfSeats;
	}

	public void setNumberOfSeats(int numberOfSeats) {
		this.numberOfSeats = numberOfSeats;
	}

	public GlobalRegion getSelectedGlobalRegion() {
		return selectedGlobalRegion;
	}

	public void setSelectedGlobalRegion(GlobalRegion selectedGlobalRegion) {
		this.selectedGlobalRegion = selectedGlobalRegion;
	}

	public List<Flight> getReturnFlights() {
		return returnFlights;
	}

	public void setReturnFlights(List<Flight> returnFlights) {
		this.returnFlights = returnFlights;
	}

	public Long getSelectedReturnFlightId() {
		return selectedReturnFlightId;
	}

	public void setSelectedReturnFlightId(Long selectedReturnFlightId) {
		this.selectedReturnFlightId = selectedReturnFlightId;
	}

}