package com.realdolmen.repository;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.realdolmen.domain.flight.Airport;
import com.realdolmen.domain.flight.Flight;
import com.realdolmen.domain.flight.Seat;
import com.realdolmen.domain.flight.SeatType;
import com.realdolmen.domain.user.Partner;
import com.realdolmen.domain.user.User;

@Stateless
public class PartnerRepository {

	@EJB
	private FlightRepository flightRepository;
	
	@EJB
	private AirportRepository airportRepository;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private CriteriaBuilder cb;

	@PersistenceContext
	EntityManager em;

	public Partner save(Partner partner) {
		em.persist(partner);
		return partner;
	}

	public Partner create(Partner partner) {
		em.persist(partner);
		return partner;
	}

	public Partner findById(Long id) {
		return em.find(Partner.class, id);
	}

	public List<Partner> findAll() {
		cb = em.getCriteriaBuilder();
		CriteriaQuery<Partner> cq = cb.createQuery(Partner.class);
		Root<Partner> rootElement = cq.from(Partner.class);
		CriteriaQuery<Partner> all = cq.select(rootElement);
		TypedQuery<Partner> allQuery = em.createQuery(all);
        return allQuery.getResultList();
	}

	public void remove(long partnerId) {
		logger.info("Removing partner with id " + partnerId);
		em.remove(em.getReference(Partner.class, partnerId));
	}
	
	public void addFlight(Partner p, Flight f, int nrBusinessSeats, double priceBusinessSeats, int nrEconomySeats, double priceEconomySeats, int nrFirstSeats, double priceFirstSeats){
		ArrayList<Seat> seats = new ArrayList<Seat>();
		seats.addAll(addSeats(nrBusinessSeats, priceBusinessSeats, SeatType.Business));
		seats.addAll(addSeats(nrEconomySeats, priceEconomySeats, SeatType.Economy));
		seats.addAll(addSeats(nrFirstSeats, priceFirstSeats, SeatType.FirstClass));
		if(p != null && f != null){
			f.addSeats(seats);
			em.persist(f);
			p.addFlight(f);
		}
	}
	
	private ArrayList<Seat> addSeats(int nrOfSeats, double priceOfSeats, SeatType seatType) {
		ArrayList<Seat> seats = new ArrayList<Seat>();
		for(int i = 0; i<nrOfSeats; i++){
			Seat s = new Seat(seatType, priceOfSeats);
			em.persist(s);
			seats.add(s);
		}
		return seats;
	}
	
	public List<Airport> getAllAirports(String searchCriteriaCountry){
		return airportRepository.findWithCountry(searchCriteriaCountry);
	}

	public ArrayList<Flight> getFlightsByPartner(User partner){
		if(checkForPartner(partner)){
			return flightRepository.getFlightsByPartner((Partner) partner);
		}
		else{
			return new ArrayList<Flight>();
		}
	}

	public Flight getFlightByPartner(User partner, long partnerFlightId) {
		if(checkForPartner(partner)){
			Flight f = flightRepository.getFlightByPartner((Partner) partner, partnerFlightId);
			return f;
		}
		return null;
	}

	public void setSeatPrice(User partner, long partnerFlightId, SeatType seatType, double newPrice) {
		if(checkForPartner(partner)){
			flightRepository.setSeatPrice((Partner) partner, partnerFlightId, seatType, newPrice);
		}
	}

	private boolean checkForPartner(User partner) {
		return partner.getClass().equals(Partner.class);
	}
	
	public int getNumberOfSeatsLeft(User partner, Flight flight, List<SeatType> seatTypes){
		if(checkForPartner(partner)){
			return flightRepository.getNumberOfSeatsLeft((Partner) partner, flight, seatTypes);
		}
		else{
			return -1;
		}
	}
	
	public int getNumberOfSeatsBooked(User partner, Flight flight, List<SeatType> seatTypes){
		if(checkForPartner(partner)){
			return flightRepository.getNumberOfSeatsBooked((Partner) partner, flight, seatTypes);
		}
		else{
			return -1;
		}
	}

	public List<String> getAirlines() {
		return em.createQuery("SELECT DISTINCT p.name FROM Partner p").getResultList();
	}
	
	/*
	 * public List<Flight> findByParams(SeatType t,Partner partner,Date
	 * departureDate) { CriteriaBuilder cb = em.getCriteriaBuilder();
	 * CriteriaQuery<Flight> cq = cb.createQuery(Flight.class); Root<Flight>
	 * flight = cq.from(Flight.class); ParameterExpression<Partner> p =
	 * cb.parameter(Partner.class);
	 * cq.select(flight).where(cb.equal(flight.get("partner"), p)); return
	 * em.createQuery(cq).getResultList(); }
	 */

}
