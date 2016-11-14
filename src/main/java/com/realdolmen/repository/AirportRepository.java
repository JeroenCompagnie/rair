package com.realdolmen.repository;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.realdolmen.domain.flight.Airport;

@Stateless
public class AirportRepository {
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

	public Airport findById(long airportId) {
		return em.find(Airport.class, airportId);
	}

	public List<Airport> findAll() {
		return em.createQuery("select a from Airport a", Airport.class).getResultList();
	}

	public void remove(Long airportId) {
		logger.info("Removing airport with id " + airportId);
		em.remove(em.getReference(Airport.class, airportId));
	}

	public List<Airport> findWithCountry(String searchCriteriaCountry) {
		List<Airport> list = new ArrayList<Airport>();
		try {
			list = em.createQuery("select a from Airport a where a.country = :arg", Airport.class)
					.setParameter("arg", searchCriteriaCountry)
					.getResultList();
		} catch (Exception e) {
			return findAll();
		}
		if(list == null || list.isEmpty()){
			return findAll();
		}
		return list;
	}
}
