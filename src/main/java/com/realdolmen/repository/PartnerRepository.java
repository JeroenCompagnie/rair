package com.realdolmen.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.realdolmen.domain.user.Partner;

@Stateless
public class PartnerRepository {

	private Logger logger = LoggerFactory.getLogger(getClass());

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
		return em.createNamedQuery("Flight.findAll", Partner.class).getResultList();
	}

	public void remove(long partnerId) {
		logger.info("Removing partner with id " + partnerId);
		em.remove(em.getReference(Partner.class, partnerId));
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
