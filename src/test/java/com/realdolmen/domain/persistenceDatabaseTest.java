package com.realdolmen.domain;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.junit.Test;

import com.realdolmen.course.utilities.persistence.JpaPersistenceTest;
import com.realdolmen.domain.flight.Airport;
import com.realdolmen.domain.flight.Booking;
import com.realdolmen.domain.flight.BookingOfFlight;
import com.realdolmen.domain.flight.Flight;
import com.realdolmen.domain.flight.GlobalRegion;
import com.realdolmen.domain.flight.PaymentStatus;
import com.realdolmen.domain.flight.Seat;
import com.realdolmen.domain.flight.SeatType;
import com.realdolmen.domain.flight.locationReaders.CSVReader;
import com.realdolmen.domain.user.Address;
import com.realdolmen.domain.user.Customer;
import com.realdolmen.domain.user.Employee;
import com.realdolmen.domain.user.Partner;
import com.realdolmen.domain.user.User;
import com.realdolmen.repository.UserRepository;

public class persistenceDatabaseTest extends JpaPersistenceTest{
	
	@Test
	public void initPersistTest() {
		
		// Make EntityManager with function from JpaPersistenceTest
		EntityManager em = entityManager();
		
		//Make Address a, persist for Id and check if Id exists
		Address a = new Address("DummyStraat", 42, 9999, "DummyCity", "Belgium");
		em.persist(a);
		assertNotNull(a.getId());
		
		//Make Customer c, persist, check Id for null and check if link with Address a is there
		Customer c = new Customer(a, "jon.neige@gmail.com", 
				"Jon", "Snow", "iknownothing", "jonneige");
		em.persist(c);
		assertNotNull(c.getId());
		assertEquals("DummyStraat", em.find(Customer.class, c.getId()).getAddress().getStreet());
		
		
		//Make Partner p, persist, check Id for null and check if a String field checks out
		Partner p = new Partner("KLM", "fName", "lName", "KLM", "pass");
		em.persist(p);
		assertNotNull(p.getId());
		assertEquals("KLM", em.find(Partner.class, p.getId()).getName());
		
		
		// Make two Location objects l1 and l2, persist both and check Ids for null
		Airport l1 = new Airport("city1", "country1", "cc1", "airportname1", "421","globalregion1");
		Airport l2 = new Airport("city2", "country2", "cc2", "airportname2", "422","globalregion2");
		em.persist(l1);
		assertNotNull(l1.getId());
		em.persist(l2);
		assertNotNull(l2.getId());

		// Make Fligt f with l1 and l2, persist and check Id for null
		Flight f = new Flight(p, new ArrayList<BookingOfFlight>(), l1, l2,
				new Date(), Duration.ofMinutes(120));
		em.persist(f);
		assertNotNull(f.getId());
		assertEquals(120, em.find(Flight.class, f.getId()).getFlightDuration().toMinutes());
		
		Seat seat = new Seat(SeatType.Business, 999.99);
		em.persist(seat);
		assertNotNull(seat.getId());
		f.addSeat(seat);
				
		assertEquals("Business", em.find(Flight.class, f.getId()).getSeatList().get(0).getType().toString());
		
		// Find persistedFlight and check if l1 and l2 are linked 
		// 	through globalRegion, this way the enum is also tested
		Flight persistedFlight = em.find(Flight.class, f.getId());
		assertEquals("globalregion1", persistedFlight.getDepartureAirport().getGlobalRegion().toString());
		assertEquals("globalregion2", persistedFlight.getDestinationAirport().getGlobalRegion().toString());
		
		// Check departure date stuff:
		//	- check if depature date is before current date
		//	- check if getFlightDurationMethod gives the correct time
		//	- check the print versions...
		//	- check if the time between landingtime (calculated by getLandingTime()) 
		//		and dateOfDeparture minus the flight duration equals zero
		assertTrue(new Date().after(persistedFlight.getDateOfDeparture()));
		assertEquals(120, persistedFlight.getFlightDuration().toMinutes());
		System.err.println("Date of departure: " + f.getDateOfDeparture().toString());
		System.err.println("Flight duration " + f.getFlightDuration().toMinutes());
		System.err.println("Date of landing: " + f.getLandingTime().toString());
		assertEquals(0, (int)((f.getLandingTime().getTime()) 
				- f.getDateOfDeparture().getTime())/60000
				- f.getFlightDuration().toMinutes());
		
		// Make Seat s, persist and check Id for Null
		Seat s = new Seat(SeatType.Business, 50.0);
		em.persist(s);
		assertNotNull(s.getId());
		
		// Add Seat s to Flight f and check if it checks out
		//	=> enum SeatType tested
		f.addSeat(s);
//		em.merge(f); // TODO: WAT DOET MERGE juist want hier is het niet nodig?!
		assertEquals("Business", em.find(Flight.class, f.getId()).getSeatList().get(0).getType().toString());
		
		// Partner with Flight linkt test
		p.addFlight(f);
		assertEquals("KLM", persistedFlight.getPartner().getUserName());
		
		// Make Booking b, persist and check Id for null
		Booking b = new Booking(PaymentStatus.PENDING, c, new Date());
		em.persist(b);
		assertNotNull(b.getId());
		
		// Make multiple BookingOfFLight bf, persist, check Id for null
		//		and add it to the Booking b
		for(int i = 0; i < 4; i++){
			BookingOfFlight bf = new BookingOfFlight(999.99, f, b);
			em.persist(bf);
			assertNotNull(bf.getId());
			b.addBookingOfFlight(bf);
			em.merge(bf);
			f.addBookingOfFlight(bf);
		}
		
		// merge it
		em.merge(b);
		em.merge(f);
		
		c.addBooking(b);
		em.merge(c);
		assertEquals(PaymentStatus.PENDING, em.find(Customer.class, c.getId())
				.getBookingsList().get(0).getPaymentStatus());
		
		assertEquals(999.99, em.find(Booking.class, b.getId()).getBookingOfFlightList().get(0).getPrice(),0.01);
		
		// Check if the Booking and Flight link is good
		// Check if the Booking and Customer link is good
		assertEquals(f.getId(), 
				em.find(Booking.class, b.getId()).getBookingOfFlightList().get(0).getFlight().getId());
		assertEquals("Jon", 
				em.find(Booking.class, b.getId()).getCustomer().getFirstName());
	}
	
	@Test
	public void testQueryUserRepo(){
		EntityManager em = entityManager();
		Customer c = (Customer) em.createQuery("SELECT c FROM Customer c").getResultList().get(0);
		assertEquals("Jon", c.getFirstName());
	}
	
	@Test
	public void testUsersAndPeristInitData(){
		EntityManager em = entityManager();
		Address a = new Address("1",1,1,"","");
		em.persist(a);
		
		Customer customer = new Customer(a,"1@gmail.com","12345678","12345678","12345678","123");
		Partner partner = new Partner("partner", "partner", "partner", "partner", "12345678");
		Employee employee = new Employee("employee", "employee", "12345678", "employee");
		
		
		em.persist(customer);
		em.persist(partner);
		em.persist(employee);
		
		TypedQuery<User> query = em.createQuery("select u from User u where u.userName = :arg1", User.class);
		query.setParameter("arg1", "employee");
		try{
			query.getSingleResult();
			System.err.println("Succeeded");
		}
		catch (NoResultException e){
			System.err.println("Failed");
		}
		String[] split = employee.getClass().getName().split("\\.");
		System.err.println(split[split.length-1]);
		
		/**
		 * For airports
		 */
		ArrayList<Airport> airportsFromCSV = CSVReader.getAirportsFromCSV();
		
		int count = 0;
		for(Airport airport : airportsFromCSV){
        	if(null != airport.getGlobalRegion()){
        		count++;
        	}
        }
		System.err.println("Number of global regions which are null: " + count);
		
		for(Airport entry : airportsFromCSV){
			Airport airport = new Airport(entry.getCity(),
					entry.getCountry(), 
					entry.getCountryCode(), 
					entry.getAirportName(), 
					entry.getId(),
					entry.getGlobalRegion());
			em.persist(airport);
		}
		
		ArrayList<Airport> airports = (ArrayList<Airport>) em.createQuery("select a from Airport a", Airport.class).getResultList();
		assertEquals(airportsFromCSV.size(), airports.size());
		
		Airport a1 = em.find(Airport.class, "BRU");
		assertNotNull(a1);
		Airport a2 = em.find(Airport.class, "VCE");
		assertNotNull(a2);
		
		Flight f = new Flight(partner, new ArrayList<BookingOfFlight>(),
				a1, a2, new Date(), Duration.ofMinutes(120));
		em.persist(f);
		assertNotNull(f.getId());
		
		Booking b = new Booking(PaymentStatus.SUCCESS, customer, new Date());
		em.persist(b);
		assertNotNull(b.getId());
		
		BookingOfFlight bof = new BookingOfFlight(100.0, f, b);
		em.persist(bof);
		assertNotNull(bof.getId());
		
		f.addBookingOfFlight(bof);
		em.merge(f);
		assertEquals(1, em.find(Flight.class, f.getId()).getBookingOfFlightList().size());
		
	}
}
