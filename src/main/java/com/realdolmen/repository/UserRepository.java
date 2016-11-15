package com.realdolmen.repository;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.realdolmen.domain.user.Customer;
import com.realdolmen.domain.user.User;



@Stateless
public class UserRepository {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@PersistenceContext
	private EntityManager em;
	
	@PostConstruct
	public void init(){
//		try{
//			em.createQuery("select c from Customer c where c.userName = :arg", Customer.class).setParameter("arg","customer").getSingleResult();
//		}
//		catch (NoResultException e){
//			System.err.println("no result exception caught");
//			Customer c = new Customer(new Address("Street", 42, 4242, "City", "Country"), 
//					"cust@mail.com", "Cust", "Omer", "customer", "customer");
//			saveCustomer(c);
//			Booking b = new Booking(PaymentStatus.SUCCESS, c, new Date());
//			em.persist(b);
//			
//			for(int i = 0; i < 3; i++){
//				
//				BookingOfFlight bf = new BookingOfFlight(100.0,, b);
//				em.persist(bf);
//				b.addBookingOfFlight(bf);
//			}
//			c.addBooking(b);
//		}
	}

	public User save(User user) {
		em.persist(user);
		return user;
	}

	public Customer saveCustomer(Customer c) {
		em.persist(c.getAddress());
		em.persist(c);
		return c;
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

	public Customer findCustomer(String userName) {
//		System.err.println("In findCustomer");
//		System.err.println("username: " + userName);
//		//TypedQuery<Customer> q = em.createQuery("select c from Customer c ", Customer.class);
////		query.setParameter("arg1", userName);
//		System.err.println("test if em = null: ");
//		System.err.println(em == null);
//		System.err.println("test if em = null: end");
//		return (Customer) em.createQuery("SELECT c FROM Customer c").getResultList().get(0);
		
		TypedQuery<Customer> query = em.createQuery("select c from Customer c where c.userName = :arg1", Customer.class);
		query.setParameter("arg1", userName);
		
		return query.getSingleResult();
	}

	public User findUser(String userName) {
		TypedQuery<User> query = em.createQuery("select u from User u where u.userName = :arg1", User.class);
		query.setParameter("arg1", userName);
		try{
			return query.getSingleResult();
		}
		catch (NoResultException e){
			return null;
		}
	}
}
