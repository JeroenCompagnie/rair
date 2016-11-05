package com.realdolmen.domain;

import java.util.ArrayList;
import java.util.Calendar;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.realdolmen.course.utilities.persistence.JpaPersistenceTest;

public class persistenceDatabaseTest extends JpaPersistenceTest{
	
	@Test
	public void initPersistTest() {
		EntityManager em = entityManager();
		Address a = new Address("DummyStraat", 42, 9999, "Belgium");
		//Address a persisten zodat het een ID heeft
		em.persist(a);
		assertNotNull(a.getId());
		Customer c = new Customer(a, "jon.snow@gmail.com", 
				"Jon", "Snow", "iknownothing", "KingOfTheNorth");
		em.persist(c);
		assertNotNull(c.getId());
		assertEquals("DummyStraat", em.find(Customer.class, c.getId()).getAddress().getStreet());
		
		Partner p = new Partner("BryanAir", "fName", "lName", "BryanAir", "pass");
		em.persist(p);
		assertNotNull(p.getId());
		assertEquals("BryanAir", em.find(Partner.class, p.getId()).getName());
		
		Flight f = new Flight(p, new ArrayList<BookingOfFlight>());
		em.persist(f);
		assertNotNull(f.getId());
		
		p.addFlight(f);
		assertEquals("BryanAir", em.find(Flight.class, f.getId()).getPartner().getUserName());
		
		Booking b = new Booking("Payed", c, Calendar.getInstance());
		em.persist(b);
		assertNotNull(b.getId());
		
		BookingOfFlight bf = new BookingOfFlight(999.99, f, b);
		em.persist(bf);
		assertNotNull(bf.getId());
		b.addBookingOfFlight(bf);
		em.merge(b);
		
		assertEquals(f.getId(), 
				em.find(Booking.class, b.getId()).getBookingOfFlightList().get(0).getFlight().getId());
		assertEquals("Jon", 
				em.find(Booking.class, b.getId()).getCustomer().getFirstName());
		
	}
}
