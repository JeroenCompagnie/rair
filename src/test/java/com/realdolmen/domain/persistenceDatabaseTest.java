	package com.realdolmen.domain;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.junit.Test;

import com.realdolmen.course.utilities.persistence.JpaPersistenceTest;
import com.realdolmen.domain.flight.Airport;
import com.realdolmen.domain.flight.Booking;
import com.realdolmen.domain.flight.BookingOfFlight;
import com.realdolmen.domain.flight.Discount;
import com.realdolmen.domain.flight.Flight;
import com.realdolmen.domain.flight.PaymentStatus;
import com.realdolmen.domain.flight.Seat;
import com.realdolmen.domain.flight.SeatType;
import com.realdolmen.domain.flight.locationReaders.CSVReader;
import com.realdolmen.domain.user.Address;
import com.realdolmen.domain.user.Customer;
import com.realdolmen.domain.user.Employee;
import com.realdolmen.domain.user.Partner;
import com.realdolmen.domain.user.User;

public class persistenceDatabaseTest extends JpaPersistenceTest {

	@Test
	public void initPersistTest() {

		// Make EntityManager with function from JpaPersistenceTest
		EntityManager em = entityManager();

		// Make Address a, persist for Id and check if Id exists
		Address a = new Address("DummyStraat", 42, 9999, "DummyCity", "Belgium");
		em.persist(a);
		assertNotNull(a.getId());

		// Make Customer c, persist, check Id for null and check if link with
		// Address a is there
		Customer c = new Customer(a, "jon.neige@gmail.com", "Jon", "Snow", "iknownothing", "jonneige");
		em.persist(c);
		assertNotNull(c.getId());
		assertEquals("DummyStraat", em.find(Customer.class, c.getId()).getAddress().getStreet());

		// Make Partner p, persist, check Id for null and check if a String
		// field checks out
		Partner p = new Partner("KLM", "fName", "lName", "KLM", "pass");
		em.persist(p);
		assertNotNull(p.getId());
		assertEquals("KLM", em.find(Partner.class, p.getId()).getName());

		// Make two Location objects l1 and l2, persist both and check Ids for
		// null
		Airport l1 = new Airport("city1", "country1", "cc1", "airportname1", "421", "globalregion1");
		Airport l2 = new Airport("city2", "country2", "cc2", "airportname2", "422", "globalregion2");
		em.persist(l1);
		assertNotNull(l1.getId());
		em.persist(l2);
		assertNotNull(l2.getId());

		// Make Fligt f with l1 and l2, persist and check Id for null
		Flight f = new Flight(p, l1, l2, new Date(), Duration.ofMinutes(120));
		em.persist(f);
		assertNotNull(f.getId());
		assertEquals(120, em.find(Flight.class, f.getId()).getFlightDuration().toMinutes());

		Seat seat = new Seat(SeatType.Business, 999.99);
		em.persist(seat);
		assertNotNull(seat.getId());
		f.addSeat(seat);

		assertEquals("Business", em.find(Flight.class, f.getId()).getSeatList().get(0).getType().toString());

		// Find persistedFlight and check if l1 and l2 are linked
		// through globalRegion, this way the enum is also tested
		Flight persistedFlight = em.find(Flight.class, f.getId());
		assertEquals("globalregion1", persistedFlight.getDepartureAirport().getGlobalRegion().toString());
		assertEquals("globalregion2", persistedFlight.getDestinationAirport().getGlobalRegion().toString());

		// Check departure date stuff:
		// - check if depature date is before current date
		// - check if getFlightDurationMethod gives the correct time
		// - check the print versions...
		// - check if the time between landingtime (calculated by
		// getLandingTime())
		// and dateOfDeparture minus the flight duration equals zero
		assertTrue(new Date().after(persistedFlight.getDateOfDeparture()));
		assertEquals(120, persistedFlight.getFlightDuration().toMinutes());
		System.err.println("Date of departure: " + f.getDateOfDeparture().toString());
		System.err.println("Flight duration " + f.getFlightDuration().toMinutes());
		System.err.println("Date of landing: " + f.getLandingTime().toString());
		assertEquals(0, (int) ((f.getLandingTime().getTime()) - f.getDateOfDeparture().getTime()) / 60000
				- f.getFlightDuration().toMinutes());

		// Make Seat s, persist and check Id for Null
		Seat s = new Seat(SeatType.Business, 50.0);
		em.persist(s);
		assertNotNull(s.getId());

		// Add Seat s to Flight f and check if it checks out
		// => enum SeatType tested
		f.addSeat(s);
		// em.merge(f); // TODO: WAT DOET MERGE juist want hier is het niet
		// nodig?!
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
			Seat bofSeat = new Seat(SeatType.Business, 800.0);
			em.persist(bofSeat);
			assertNotNull(bofSeat.getId());
			BookingOfFlight bf = new BookingOfFlight(999.99, f, b, bofSeat);

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
		assertEquals(PaymentStatus.PENDING,
				em.find(Customer.class, c.getId()).getBookingsList().get(0).getPaymentStatus());

		assertEquals(999.99, em.find(Booking.class, b.getId()).getBookingOfFlightList().get(0).getPrice(), 0.01);

		// Check if the Booking and Flight link is good
		// Check if the Booking and Customer link is good
		assertEquals(f.getId(), em.find(Booking.class, b.getId()).getBookingOfFlightList().get(0).getFlight().getId());
		assertEquals("Jon", em.find(Booking.class, b.getId()).getCustomer().getFirstName());
	}

	@Test
	public void testQueryUserRepo() {
		EntityManager em = entityManager();
		Customer c = (Customer) em.createQuery("SELECT c FROM Customer c").getResultList().get(0);
		assertEquals("Jon", c.getFirstName());
	}

	@Test
	public void testUsersAndPeristInitData() {
		EntityManager em = entityManager();
		Address a = new Address("1", 1, 1, "1", "1");
		em.persist(a);

		Customer customer = new Customer(a, "1@gmail.com", "12345678", "12345678", "12345678", "123");
		Partner partner = new Partner("partner", "partner", "partner", "partner", "12345678");
		Employee employee = new Employee("employee", "employee", "12345678", "employee");

		em.persist(customer);
		em.persist(partner);
		em.persist(employee);

		TypedQuery<User> query = em.createQuery("select u from User u where u.userName = :arg1", User.class);
		query.setParameter("arg1", "employee");
		try {
			query.getSingleResult();
			System.err.println("Succeeded");
		} catch (NoResultException e) {
			System.err.println("Failed");
		}
		String[] split = employee.getClass().getName().split("\\.");
		System.err.println(split[split.length - 1]);

		/**
		 * For airports
		 */
//		Airport air = new Airport("", "", "", "", "", "");
//		em.persist(air);
		
		ArrayList<Airport> airportsFromCSV = CSVReader.getAirportsFromCSV();

		int count = 0;
		for (Airport airport : airportsFromCSV) {
			if (null != airport.getGlobalRegion()) {
				count++;
			}
		}
		System.err.println("Number of global regions which are null: " + count);

		for (Airport entry : airportsFromCSV) {
			Airport airport = new Airport(entry.getCity(), entry.getCountry(), entry.getCountryCode(),
					entry.getAirportName(), entry.getInternationalAirportCode(), entry.getGlobalRegion());
			em.persist(airport);
		}

		ArrayList<Airport> airports = (ArrayList<Airport>) em.createQuery("select a from Airport a", Airport.class)
				.getResultList();
		assertEquals(airportsFromCSV.size(), airports.size());

		
		
		Airport a1 = getAirportByCode(em, "BRU");
		assertNotNull(a1);
		Airport a2 = getAirportByCode(em, "VCE");
		assertNotNull(a2);
		
		

		/** BEGIN
		 * Make flight, add 30 seats, 10 of each seattype
		 * Make bookingsofflights for 9 seats, 3 of each seattype;
		 * 	add it to a booking
		 */
		Flight flight = new Flight(partner,
				getAirportByCode(em, "JFK"), 
				getAirportByCode(em, "JFK"),
				new Date(), Duration.ofMinutes(9));
		em.persist(flight);
		assertNotNull(flight.getId());

		//		Booking booking = new Booking(PaymentStatus.SUCCESS, 
		//				customer, 
		//				new Date());
		//		em.persist(booking);
		//		assertNotNull(booking.getId());

		List<SeatType> seatTypeList = new ArrayList<>();
		seatTypeList.add(SeatType.Business);
		seatTypeList.add(SeatType.Economy);
		seatTypeList.add(SeatType.FirstClass);

		for(int j=0;j <3; j++){
			for(int i=0; i<10; i++){
				Seat seat = new Seat(seatTypeList.get(j), 1.99*(j+1));
				em.persist(seat);
				assertNotNull(seat.getId());

				flight.addSeat(seat);
				em.merge(flight);
			}
		}


		HashMap<SeatType, Integer> map = new HashMap<>();
		map.put(SeatType.Business, 3);
		map.put(SeatType.Economy, 3);
		map.put(SeatType.FirstClass, 3);

		Booking addBooking = new Booking(PaymentStatus.PENDING, customer, new Date());
		assertNull(addBooking.getId());
		em.persist(addBooking);
		assertNotNull(addBooking.getId());
		addBooking = flight.addBooking(map, addBooking);		

		for(BookingOfFlight element:addBooking.getBookingOfFlightList()){
			em.persist(element);
			assertNotNull(element.getId());
		}

		em.merge(flight);
		em.merge(addBooking);
		assertNotNull(addBooking.getId());

		assertEquals(21, em.find(Flight.class, flight.getId()).getSeatsLeft());
		assertEquals(9, em.find(Booking.class, addBooking.getId()).getBookingOfFlightList().size());

		//		int number = (int) em.createQuery("Select COUNT(s) from BookingOfFlight f join Seat s "
		//				+ "where f.flight_id = :arg1 and type= :arg2"
		//				+ "and seat.id = seat_id")
		//		.setParameter("arg1", flight.getId())
		//		.setParameter("arg2", SeatType.Economy.toString())
		//		.getSingleResult();

		// COUNT NUMBER OF SEATS, which is 30
		int number = (int)(long) em.createQuery("select count(s) from Seat s")
				.getSingleResult();
		assertEquals(30, number);

		// COUNT NUMBER OF join between bookingofflight and seat
		// which is 9 => nr of bookingofflight is 9 and each one has a seat
		number = (int)(long) em.createQuery("select count(s) from BookingOfFlight bof join bof.seat s")
				.getSingleResult();
		assertEquals(9, number);

		// with flight matching the flight made above
		number = (int)(long) em.createQuery(
				"select count(s) "
				+ "from BookingOfFlight bof join bof.seat s "
				+ "where bof.flight = :arg1 "
				+ "and bof.seat = s")
				.setParameter("arg1", flight)
				.getSingleResult();
		assertEquals(9, number);
		
		// just economy seats
		number = (int)(long) em.createQuery(
				"select count(s) "
				+ "from BookingOfFlight bof join bof.seat s "
				+ "where bof.flight = :arg1 "
				+ "and bof.seat = s "
				+ "and s.type = :arg2")
				.setParameter("arg1", flight)
				.setParameter("arg2", SeatType.Economy)
				.getSingleResult();
		assertEquals(3, number);
		
		// GET NUMBER OF SEATS left PER SEAT TYPE
		number = (int)(long) em.createQuery(
				"select count(f) "
				+ "from Flight f join "
				+ "f.seatList s WHERE f.id = :flightId "
				+ "and s.type = :seatType")
				.setParameter("flightId", flight.getId())
				.setParameter("seatType", SeatType.Economy)
				.getSingleResult();
		assertEquals(7, number);
		
		// GET NUMBER OF TOTAL SEATS LEFT
		number = (int)(long) em.createQuery(
				"SELECT count(f) "
				+ "FROM Flight f JOIN "
				+ "f.seatList s WHERE f.id = :flightId "
				+ "AND (s.type IN :seatType)")
				.setParameter("flightId", flight.getId())
				.setParameter("seatType", Arrays.asList(SeatType.values()))
				.getSingleResult();
		assertEquals(21, number);
		
		// GET NUMBER OF TOTAL SEATS LEFT
				number = (int)(long) em.createQuery(
						"SELECT count(f) "
						+ "FROM Flight f JOIN "
						+ "f.seatList s WHERE f.id = :flightId "
						+ "AND (s.type IN :seatType)")
						.setParameter("flightId", flight.getId())
						.setParameter("seatType", SeatType.Economy)
						.getSingleResult();
				assertEquals(7, number);
				
		// ADD some discounts to the flight:
		flight.addDiscount(new Discount(true, true, 0.01));
		flight.addDiscount(new Discount(false, true, 0.01));
		flight.addDiscount(new Discount(true, false, 1.0));
		flight.addDiscount(new Discount(false, false, 1.0));
		/** ENDtype
		 * Make flight, add 30 seats, 10 of each seattype
		 * Make bookingsofflights for 9 seats, 3 of each seattype;
		 * 	add it to a booking
		 */

		for (int y = 1; y <= 10; y++) {
			Flight f = new Flight(partner, a1, a2, new Date(),
					Duration.ofMinutes(120*y));
			em.persist(f);
			assertNotNull(f.getId());

			for (int i = 1; i < 120; i++) {
				if (i <= 60) {
					/*Seat seat = new Seat(SeatType.Business, 999.99);
					em.persist(seat);
					assertNotNull(seat.getId());
					f.addSeat(seat);*/
					Seat seat = new Seat(SeatType.FirstClass, 567.99);
					em.persist(seat);
					assertNotNull(seat.getId());
					f.addSeat(seat);
				} else if (i <= 90) {
					Seat seat = new Seat(SeatType.FirstClass, 567.99);
					em.persist(seat);
					assertNotNull(seat.getId());
					f.addSeat(seat);
				} else {
					if(i!=5)
					{
						Seat seat = new Seat(SeatType.Economy, 237.99);
						em.persist(seat);
						assertNotNull(seat.getId());
						f.addSeat(seat);}
				}
			}

			Booking b = new Booking(PaymentStatus.SUCCESS, customer, new Date());
			em.persist(b);
			assertNotNull(b.getId());

			Seat s = new Seat(SeatType.Economy, 424242.42);
			em.persist(s);
			assertNotNull(s.getId());
			BookingOfFlight bof = new BookingOfFlight(100.0, f, b, s);
			em.persist(bof);
			assertNotNull(bof.getId());

			b.addBookingOfFlight(bof);
			em.merge(b);
			assertEquals(1, em.find(Booking.class, b.getId()).getBookingOfFlightList().size());

			f.addBookingOfFlight(bof);
			em.merge(f);
			assertEquals(1, em.find(Flight.class, f.getId()).getBookingOfFlightList().size());



		}
		
	}

	private Airport getAirportByCode(EntityManager em, String code) {
		return em.createQuery(
				"select a from Airport a "
				+ "where a.internationalAirportCode = :arg", Airport.class)
		.setParameter("arg", code)
		.getSingleResult();
	}
}

