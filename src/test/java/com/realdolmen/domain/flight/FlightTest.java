package com.realdolmen.domain.flight;

import static org.junit.Assert.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.realdolmen.domain.user.Partner;

public class FlightTest {

	Flight f;
	Partner p;
	Airport l1;
	Airport l2;
	Date d;
	Duration du;
	Seat s;

	@Before
	public void init() {
		p = new Partner("Str8ightDown", "Str8ight", "Down", "Str8ightDown", "password");
		l1 = new Airport("city1", "country1", "cc1", "airportname1", "421","globalregion1");
		l2 = new Airport("city2", "country2", "cc2", "airportname2", "422","globalregion2");
		
		d = new Date();
		du = Duration.ofHours(3L);
		f = new Flight(p, new ArrayList<BookingOfFlight>(), l1, l2, d, du);

	}

	@Test
	public void flightUnitTest() {
		assertEquals(p, f.getPartner());
		assertEquals(l1, f.getDepartureAirport());
		assertEquals(l2, f.getDestinationAirport());
		assertEquals(new Date(d.getTime()+du.toMillis()), f.getLandingTime());
		assertEquals(du, f.getFlightDuration());
	}

	@Test
	public void addSeatsTest() {
		assertEquals(0,f.getSeatList().size());
		for (int i = 1; i <= 7; i++) {
			System.out.println(i);
			if (i < 3) {
				System.out.println("price is 64");
				s = new Seat(SeatType.Business, (64));
				f.addSeat(s);
			} else {
				System.out.println("price is 28");
				s = new Seat(SeatType.Economy, (28));
				f.addSeat(s);
			}
		}
		assertEquals(7,f.getSeatList().size());
		assertEquals(64,f.getSeatList().get(1).getBasePrice(),2);
		assertEquals(28,f.getSeatList().get(5).getBasePrice(),2);
		assertEquals(2,f.getNumberOfSeatForType(SeatType.Business));
	}

}
