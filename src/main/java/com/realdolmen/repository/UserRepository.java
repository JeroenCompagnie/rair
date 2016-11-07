package com.realdolmen.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.realdolmen.domain.user.Customer;
import com.realdolmen.domain.user.User;

@Stateless
public class UserRepository {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@PersistenceContext
	EntityManager em;

	public User save(User user) {
		em.persist(user);
		return user;
	}

	public User findById(Long id) {
		return em.find(User.class, id);
	}

	public List<User> findAll() {
		return em.createQuery("select u from User u", User.class).getResultList();
	}
	
	public List<Customer> findAllCustomers(){
		return em.createQuery("select c from Customer c", Customer.class).getResultList();
	}
	
	public List<Customer> findAllEmployees(){
		return em.createQuery("select e from Employee e", Customer.class).getResultList();
	}

	public List<Customer> findAllPartners(){
		return em.createQuery("select p from Partner p", Customer.class).getResultList();
	}
	
	public void remove(long userId) {
		logger.info("Removing user with id " + userId);
		em.remove(em.getReference(User.class, userId));
	}
}
