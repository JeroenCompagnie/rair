package com.realdolmen.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.realdolmen.domain.flight.Booking;

@Stateless
public class BookingRepository {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext
	private EntityManager em;
	
	public Booking save(Booking b) {
		em.persist(b);
		return b;
	}

	public Booking findById(String id) {
		return em.find(Booking.class, id);
	}

	public List<Booking> findAll() {
		return em.createQuery("select b from Booking b", Booking.class).getResultList();
	}
	
	public Booking update(Booking booking)
	{
		return em.merge(booking);
	}
	
	public void remove(String bookingId) {
		logger.info("Removing user with id " + bookingId);
		em.remove(em.getReference(Booking.class, bookingId));
	}
}
