package com.realdolmen.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.realdolmen.domain.flight.Airport;

@Stateless
public class AirportRepositroy {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext
	EntityManager em;

	public Airport save(Airport airport) {
		em.persist(airport);
		return airport;
	}

	public Airport create(Airport airport) {
		em.persist(airport);
		return airport;
	}

	public Airport findById(Long airportId) {
		return em.find(Airport.class, airportId);
	}

	public List<Airport> findAll() {
		return em.createQuery("select a from Airport a", Airport.class).getResultList();
	}

	public void remove(long airportId) {
		logger.info("Removing airport with id " + airportId);
		em.remove(em.getReference(Airport.class, airportId));
	}
}
