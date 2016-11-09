package com.realdolmen.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.realdolmen.domain.flight.Location;



@Stateless
public class LocationRepository {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private CriteriaBuilder cb;

	@PersistenceContext
	EntityManager em;

	protected void setEm(EntityManager em) {
		this.em = em;
	}

	public Location save(Location location) {
		em.persist(location);
		return location;
	}

	public Location create(Location location) {
		em.persist(location);
		return location;
	}

	public Location findById(Long id) {
		return em.find(Location.class, id);
	}

	public List<Location> findAll() {
		cb = em.getCriteriaBuilder();
		CriteriaQuery<Location> cq = cb.createQuery(Location.class);
		Root<Location> rootElement = cq.from(Location.class);
		CriteriaQuery<Location> all = cq.select(rootElement);
		TypedQuery<Location> allQuery = em.createQuery(all);
		return allQuery.getResultList();
	}

	public void remove(long locationId) {
		logger.info("Removing location with id " + locationId);
		em.remove(em.getReference(Location.class, locationId));
	}
}
