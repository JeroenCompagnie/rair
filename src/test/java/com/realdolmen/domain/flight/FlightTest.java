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
	
	@Before
	public void init(){
		p = new Partner("Str8ightDown","Str8ight","Down","Str8ightDown","password");
		l1= new Location("SaltzBurg","Austria","SLZB",GlobalRegion.EasternEurope);
		l2= new Location("Cama","Niguea","CANI",GlobalRegion.Africa);
		d = new Date();
		du = Duration.ofHours(3L);
		f = new Flight(p,new ArrayList<BookingOfFlight>(),l1,l2,d,du);
	}
	
	

	@Test
	public void flightUnitTest() {
		assertEquals(p,f.getPartner());
		assertEquals(l1,f.getDepartureLocation());
		assertEquals(l1,f.getDestinationLocation());
		assertEquals(d,f.getLandingTime());
		assertEquals(du,f.getFlightDuration());
	}

}
