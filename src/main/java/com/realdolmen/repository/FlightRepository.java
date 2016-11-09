package com.realdolmen.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

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
	
	public List<Flight> findByParams2(SeatType t, Partner p,Date d)
	{  
		/*
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Flight> cq = cb.createQuery(Flight.class);
		Metamodel m = em.getMetamodel();
		EntityType<Flight> Flight_ = m.entity(Flight.class);
		EntityType<List<Seat>> Seat_ = m.entity(Seat.class);
		Root<Flight> flight = cq.from(Flight.class);
		Join<Flight, List<Seat>> seatList = flight.join(Flight_.);
		cq.where(cb.equal(flight.get("seatList").get("type"), t));
		/*
		CriteriaBuilder cb = em.getCriteriaBuilder(); //em is EntityManager
		CriteriaQuery<Flight> cq = cb.createQuery(Flight.class);
		Root<Flight> root = cq.from(Flight.class);

		Expression<Collection<Seat>> seatlist = root.get("seatList");
		Predicate containsWantedSeatType = cb.isMember("favorite-id", productIds);

		cq.where(containsFavoritedProduct);

		List<FavoritesFolder> favoritesFolders = em.createQuery(cq).getResultList();
		return null;*/
		//return em.createQuery(cq).getResultList();
		return null;
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
	                cb.equal(flight.get("seatList")	, t));
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
