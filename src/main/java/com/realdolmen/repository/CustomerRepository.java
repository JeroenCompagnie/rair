package com.realdolmen.repository;

import java.util.ArrayList;
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

import com.realdolmen.domain.flight.Booking;
import com.realdolmen.domain.user.Customer;
import com.realdolmen.domain.user.User;

@Stateless
public class CustomerRepository {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private CriteriaBuilder cb;

	@PersistenceContext
	EntityManager em;

	protected void setEm(EntityManager em) {
		this.em = em;
	}

	public Customer save(Customer customer) {
		em.persist(customer);
		return customer;
	}

	public Customer create(Customer customer) {
		em.persist(customer);
		return customer;
	}

	public Customer findById(Long id) {
		return em.find(Customer.class, id);
	}

	public List<Customer> findAll() {
		cb = em.getCriteriaBuilder();
		CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
		Root<Customer> rootElement = cq.from(Customer.class);
		CriteriaQuery<Customer> all = cq.select(rootElement);
		TypedQuery<Customer> allQuery = em.createQuery(all);
		return allQuery.getResultList();
	}

	public void remove(Long customerId) {
		logger.info("Removing customer with id " + customerId);
		em.remove(em.getReference(Customer.class, customerId));
	}

	public ArrayList<Booking> getAllBookingsBy(User user) {
		ArrayList<Booking> list = new ArrayList<>();
		if(user.getClass() == Customer.class){
			ArrayList<Booking> resultList = (ArrayList<Booking>) em.createQuery(
					"select b from Booking b where b.customer = :cust",
					Booking.class)
			.setParameter("cust", user)
			.getResultList();
			if(resultList != null){
				return resultList;
			}
			
		}
		return list;
	}
}
