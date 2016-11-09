package com.realdolmen.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.realdolmen.domain.flight.Flight;
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
	
	public Flight create(Flight flight)
	{
		em.persist(flight);
		return flight;
	}

	public Flight findById(Long id) {
		return em.find(Flight.class, id);
	}

	public List<Flight> findAll() {
		return em.createNamedQuery("Flight.findAll",Flight.class).getResultList();
	}
	
	public void remove(long flightId) {
		logger.info("Removing user with id " + flightId);
		em.remove(em.getReference(User.class, flightId));
	}
	
	public List<Flight> findByParams(SeatType t,Partner partner,Date departureDate)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Flight> cq = cb.createQuery(Flight.class);
		Root<Flight> flight = cq.from(Flight.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

	    //Adding predicates in case of parameter not being null
	    if (t != null) {
	        predicates.add(
	                cb.equal(flight.get("seatList").get("type"), t));
	    }
	    if (partner != null) {
	        predicates.add(
	                cb.equal(flight.get("partner"), partner));
	    }
	    if (departureDate!= null) {
	        predicates.add(
	                cb.equal(flight.get("dateOfDeparture"), departureDate));
	    }
	    cq.select(flight)
        .where(predicates.toArray(new Predicate[]{}));
		return em.createQuery(cq).getResultList();
	}

}
