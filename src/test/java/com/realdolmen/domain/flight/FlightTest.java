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
	Location l1;
	Location l2;
	Date d;
	Duration du;
	Seat s;

	@Before
	public void init() {
		p = new Partner("Str8ightDown", "Str8ight", "Down", "Str8ightDown", "password");
		l1 = new Location("SaltzBurg", "Austria", "SLZB", GlobalRegion.EasternEurope);
		l2 = new Location("Cama", "Niguea", "CANI", GlobalRegion.Africa);
		d = new Date();
		du = Duration.ofHours(3L);
		f = new Flight(p, new ArrayList<BookingOfFlight>(), l1, l2, d, du);

	}

	@Test
	public void flightUnitTest() {
		assertEquals(p, f.getPartner());
		assertEquals(l1, f.getDepartureLocation());
		assertEquals(l2, f.getDestinationLocation());
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
