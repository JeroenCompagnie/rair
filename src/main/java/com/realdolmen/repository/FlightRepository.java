package com.realdolmen.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.realdolmen.domain.flight.Flight;
import com.realdolmen.domain.flight.Seat;
import com.realdolmen.domain.flight.SeatType;
import com.realdolmen.domain.user.Partner;
import com.realdolmen.domain.user.User;

@Stateless
public class FlightRepository {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext
	EntityManager em;

	public Flight save(Flight flight) {
		em.persist(flight);
		return flight;
	}

	public Flight create(Flight flight) {
		em.persist(flight);
		return flight;
	}

	public Flight findById(Long id) {
		return em.find(Flight.class, id);
	}

	public List<Flight> findAll() {
		return em.createNamedQuery("Flight.findAll", Flight.class).getResultList();
	}

	public void remove(long flightId) {
		logger.info("Removing user with id " + flightId);
		em.remove(em.getReference(User.class, flightId));
	}

	public List<Flight> findByParams2(SeatType t, Partner p, Date d) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Flight> cq = cb.createQuery(Flight.class);
		Root<Flight> from = cq.from(Flight.class);
		From<Flight, Seat> join = from.join("seatList");
		Path<Seat> s = join.get("type");
		Predicate predicate = cb.equal(s, t);
		cq.where(predicate);
		cq.select(from);
		TypedQuery<Flight> typedQuery = em.createQuery(cq);

		return typedQuery.getResultList();
	}

	public List<Flight> findByParams3(SeatType t, Partner p, Date d) {
//works but has duplicates
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Flight> cq = cb.createQuery(Flight.class);
		Root<Flight> from = cq.from(Flight.class);
		From<Flight, Seat> join = from.join("seatList");
		Path<Seat> s = join.get("type");
		Predicate predicate = cb.equal(s, t);
		cq.where(predicate);
		cq.select(from).distinct(true);
		TypedQuery<Flight> typedQuery = em.createQuery(cq);

		return typedQuery.getResultList();
	}

	public List<Flight> findByParams(SeatType t, Partner partner, Date departureDate) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Flight> cq = cb.createQuery(Flight.class);
		Root<Flight> from = cq.from(Flight.class);
		Date date2;
		Calendar c = Calendar.getInstance();
		c.setTime(departureDate);
		c.add(Calendar.DATE, 1); 
		date2 = c.getTime();
		System.out.println(date2.toString() + " added one day in flightrepository");
		List<Predicate> predicates = new ArrayList<Predicate>();

		// Adding predicates in case of parameter not being null
		if (t != null) {
			From<Flight, Seat> join = from.join("seatList");
			Path<Seat> s = join.get("type");
			predicates.add(cb.equal(s, t));
		}
		if (partner != null) {
			Path<Partner> dbPartner=from.<Partner>get("partner");
			predicates.add(cb.equal(dbPartner, partner));
		}
		if (departureDate != null) {
			Path<Date> dbDepartureDate=from.<Date>get("dateOfDeparture");
			predicates.add(cb.between(dbDepartureDate, departureDate, date2));
		}
		cq.select(from).where(predicates.toArray(new Predicate[] {})).distinct(true);
		return em.createQuery(cq).getResultList();
	}

	public ArrayList<Flight> getFlightsByPartner(Partner partner) {
		ArrayList<Flight> resultList = new ArrayList<Flight>();
		try{
			resultList = (ArrayList<Flight>) em.createQuery("select f from Flight f where f.partner = :arg", Flight.class).setParameter("arg", partner).getResultList();
		}
		catch (Exception e){
			logger.error("No flights found for partner: " + partner.getName());
			return resultList;
		}
		return (ArrayList<Flight>) resultList;
	}

	public Flight getFlightByPartner(Partner partner, long partnerFlightId) {
		Flight f = null;
		try{
			f = em.createQuery("select f from Flight f where f.partner = :arg1 and f.id = :arg2", Flight.class)
					.setParameter("arg1", partner)
					.setParameter("arg2", partnerFlightId)
					.getSingleResult();
		}
		catch (Exception e){
			logger.error("No flight found for partner: " + partner.getName() +" with flight id: " + partnerFlightId);
			return f;
		}
		return f;
	}

	public void setSeatPrice(Partner partner, long partnerFlightId, SeatType seatType, double newPrice) {
		Flight find = em.find(Flight.class, partnerFlightId);
		if(find.getPartner().getId() == partner.getId()){
			find.setSeatPrice(newPrice, seatType);
			em.merge(find);
		}
	}
	
	public int getNumberOfSeatsLeft(Partner partner, Flight flight, SeatType seatType){
		if(flight == null){
			System.err.println("flight was null");
			return -41;
		}
		if(flight.getPartner() == null) {
			System.err.println("flight partner was null");
			return -42;
		}
		if(partner == null){
			System.err.println("given partner was null");
			return -43;
		}
		if(flight.getPartner().getId() == partner.getId()){
			return (int)(long) em.createQuery(
					"select count(s) "
					+ "from BookingOfFlight bof join bof.seat s "
					+ "where bof.flight = :arg1 "
					+ "and bof.seat = s "
					+ "and s.type = :arg2")
					.setParameter("arg1", flight)
					.setParameter("arg2", seatType)
					.getSingleResult();
//			return (int) em.createQuery("Select COUNT(s) from BookingOfFlight f join Seat s "
//					+ "where f.flight_id = :arg1 and type= :arg2"
//					+ "and seat.id = seat_id")
//			.setParameter("arg1", flight.getId())
//			.setParameter("arg2", seatType.toString())
//			.getSingleResult();
		}
		else{
			System.err.println("Partner was not the right one!");
			return -1;
		}
	}

}
