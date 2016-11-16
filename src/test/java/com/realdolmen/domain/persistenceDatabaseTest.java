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
import com.realdolmen.domain.flight.DiscountPercentage;
import com.realdolmen.domain.flight.DiscountRealvalue;
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

		Airport a3 = getAirportByCode(em, "BRO");
		assertNotNull(a3);
		Airport a4 = getAirportByCode(em, "VCA");
		assertNotNull(a4);

		/**
		 * BEGIN Make flight, add 30 seats, 10 of each seattype Make
		 * bookingsofflights for 9 seats, 3 of each seattype; add it to a
		 * booking
		 */

		Flight flight = new Flight(partner,
				getAirportByCode(em, "JFK"), 
				getAirportByCode(em, "JFK"),
				new Date(), Duration.ofMinutes(9));

		em.persist(flight);
		assertNotNull(flight.getId());

		// Booking booking = new Booking(PaymentStatus.SUCCESS,
		// customer,
		// new Date());
		// em.persist(booking);
		// assertNotNull(booking.getId());

		List<SeatType> seatTypeList = new ArrayList<>();
		seatTypeList.add(SeatType.Business);
		seatTypeList.add(SeatType.Economy);
		seatTypeList.add(SeatType.FirstClass);

		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < 10; i++) {
				Seat seat = new Seat(seatTypeList.get(j), 1.99 * (j + 1));
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

		Booking addBooking = new Booking(PaymentStatus.PENDING, customer);
		assertNull(addBooking.getId());
		em.persist(addBooking);
		assertNotNull(addBooking.getId());
		addBooking = flight.addBooking(map, addBooking, null);

		for (BookingOfFlight element : addBooking.getBookingOfFlightList()) {
			em.persist(element);
			assertNotNull(element.getId());
		}

		em.merge(flight);
		em.merge(addBooking);
		assertNotNull(addBooking.getId());

		assertEquals(21, em.find(Flight.class, flight.getId()).getSeatsLeft());
		assertEquals(9, em.find(Booking.class, addBooking.getId()).getBookingOfFlightList().size());

		// int number = (int) em.createQuery("Select COUNT(s) from
		// BookingOfFlight f join Seat s "
		// + "where f.flight_id = :arg1 and type= :arg2"
		// + "and seat.id = seat_id")
		// .setParameter("arg1", flight.getId())
		// .setParameter("arg2", SeatType.Economy.toString())
		// .getSingleResult();

		// COUNT NUMBER OF SEATS, which is 30
		int number = (int) (long) em.createQuery("select count(s) from Seat s").getSingleResult();
		assertEquals(30, number);

		// COUNT NUMBER OF join between bookingofflight and seat
		// which is 9 => nr of bookingofflight is 9 and each one has a seat
		number = (int) (long) em.createQuery("select count(s) from BookingOfFlight bof join bof.seat s")
				.getSingleResult();
		assertEquals(9, number);

		// with flight matching the flight made above
		number = (int) (long) em
				.createQuery("select count(s) " + "from BookingOfFlight bof join bof.seat s "
						+ "where bof.flight = :arg1 " + "and bof.seat = s")
				.setParameter("arg1", flight).getSingleResult();
		assertEquals(9, number);

		// just economy seats
		number = (int) (long) em
				.createQuery("select count(s) " + "from BookingOfFlight bof join bof.seat s "
						+ "where bof.flight = :arg1 " + "and bof.seat = s " + "and s.type = :arg2")
				.setParameter("arg1", flight).setParameter("arg2", SeatType.Economy).getSingleResult();
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


		// GET NUMBER OF SEATS BOOKED PER SEAT TYPE
		number = (int) (long) em
				.createQuery("select count(f) " + "from Flight f join " + "f.seatList s WHERE f.id = :flightId "
						+ "and s.type = :seatType")
				.setParameter("flightId", 1L).setParameter("seatType", SeatType.Economy).getSingleResult();
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
		number = (int) (long) em
				.createQuery("SELECT count(f) " + "FROM Flight f JOIN " + "f.seatList s WHERE f.id = :flightId "
						+ "AND (s.type IN :seatType)")

						.setParameter("flightId", flight.getId())
						.setParameter("seatType", SeatType.Economy)
						.getSingleResult();
				assertEquals(7, number);
				
		// ADD some discounts to the flight:
		flight.addDiscount(new DiscountPercentage(true, 0.01));
		flight.addDiscount(new DiscountPercentage(false, 0.01));
		flight.addDiscount(new DiscountRealvalue(true , 1.0));
		flight.addDiscount(new DiscountRealvalue(false , 1.0));
		/** ENDtype
		 * Make flight, add 30 seats, 10 of each seattype
		 * Make bookingsofflights for 9 seats, 3 of each seattype;
		 * 	add it to a booking
		 */

		for (int y = 1; y <= 10; y++) {

			Flight f;
			if (y <= 5) {
				f = new Flight(partner, a1, a2, new Date(), Duration.ofMinutes(120 * y));
				em.persist(f);
				assertNotNull(f.getId());
			} else {
				f = new Flight(partner, a3, a4, new Date(), Duration.ofMinutes(120 * y));
				em.persist(f);
				assertNotNull(f.getId());

			}

			for (int i = 1; i < 120; i++) {
				if (i <= 60) {
					/*
					 * Seat seat = new Seat(SeatType.Business, 999.99);
					 * em.persist(seat); assertNotNull(seat.getId());
					 * f.addSeat(seat);
					 */
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

					if (i != 5) {
						Seat seat = new Seat(SeatType.Economy, 237.99);
						em.persist(seat);
						assertNotNull(seat.getId());
						f.addSeat(seat);
					}

				}
			}

			Booking b = new Booking(PaymentStatus.SUCCESS, customer);
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
		Airport a5 = getAirportByCode(em, "BRN");
		assertNotNull(a5);
		Airport a6 = getAirportByCode(em, "VCB");
		assertNotNull(a6);
		Flight fl1;
		Flight fl2;

		fl1 = new Flight(partner, a5, a6, new Date(), Duration.ofMinutes(120));
		em.persist(fl1);
		assertNotNull(fl1.getId());
		

		fl2 = new Flight(partner, a6, a5, new Date(), Duration.ofMinutes(120));
		em.persist(fl2);
		assertNotNull(fl2.getId());

		for (int i = 1; i < 120; i++) {
			if (i <= 60) {

				Seat seat = new Seat(SeatType.Business, 999.99);
				em.persist(seat);
				assertNotNull(seat.getId());
				fl1.addSeat(seat);
				
				Seat seat2 = new Seat(SeatType.Business, 999.99);
				em.persist(seat2);
				assertNotNull(seat2.getId());
				fl2.addSeat(seat2);
			} else if (i <= 90) {
				Seat seat = new Seat(SeatType.FirstClass, 567.99);
				em.persist(seat);
				assertNotNull(seat.getId());
				fl1.addSeat(seat);
				
				Seat seat2 = new Seat(SeatType.FirstClass, 567.99);
				em.persist(seat2);
				assertNotNull(seat2.getId());
				fl2.addSeat(seat2);
			} else {		
				Seat seat = new Seat(SeatType.Economy, 237.99);
				em.persist(seat);
				assertNotNull(seat.getId());
				fl1.addSeat(seat);
				
				Seat seat2 = new Seat(SeatType.Economy, 237.99);
				em.persist(seat2);
				assertNotNull(seat2.getId());
				fl2.addSeat(seat2);
			}
			em.merge(fl1);
			em.merge(fl2);
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
