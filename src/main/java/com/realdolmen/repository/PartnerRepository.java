package com.realdolmen.repository;

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

import com.realdolmen.domain.flight.Flight;
import com.realdolmen.domain.user.Partner;

@Stateless
public class PartnerRepository {

	@EJB
	FlightRepository flightRepository;
	
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
	
	public void addFlight(Partner p, Flight f){
		if(p != null && f != null){
			em.persist(f);
			p.addFlight(f);
		}
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
